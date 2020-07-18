package sskf.config.jwt;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sskf.common.Constants;
import sskf.service.impl.RedisService;
import sskf.util.CollectionUtil;
import sskf.util.StringUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisService redisService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                String token = (String) redisService.get("Access_Token_" + jwtToken);
                if (StringUtil.isEmpty(token)) {
                    throw new AuthenticationException("Access_Token has expired");
                } else {
                    redisService.setTimeOut("Access_Token_" + jwtToken, 4 * Constants.JWT_TOKEN_MAX_AGE);
                }
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
                List<String> roles = (List<String>) claims.get("ROLE");
                Collection authorities = null;
                if (CollectionUtil.isNotEmpty(roles)) {
                    authorities= roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                }
                // Once we get the token validate it.
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }
        }
        chain.doFilter(request, response);
    }

}
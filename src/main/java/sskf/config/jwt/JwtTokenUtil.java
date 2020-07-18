package sskf.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import sskf.common.Constants;
import sskf.exception.SSKFException;
import sskf.model.entity.ShainEntity;
import sskf.model.request.RefreshTokenRequest;
import sskf.model.response.TokenResponse;
import sskf.repository.ShainRepository;
import sskf.service.impl.RedisService;
import sskf.util.StringUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@PropertySource("classpath:application.properties")
@Slf4j
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;

    @Autowired
    private ShainRepository shainRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getBumonFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("bumonCd");
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //generate token for user
    public TokenResponse generateToken(UserDetails userDetails, Boolean rememberMe) {
        String accessToken = doGenerateToken(userDetails.getUsername());
        String refreshToken = null;
        if (rememberMe) {
            refreshToken = doGenerateRefreshToken(userDetails.getUsername());
        }
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        return tokenResponse;
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) throws AuthenticationException {

        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsernameFromToken(request.getRefreshToken()));

        String accessToken = doGenerateToken(userDetails.getUsername());
        String refreshToken = (String) redisService.get("Refresh_Token_" + request.getRefreshToken());
        if (StringUtil.isNotEmpty(refreshToken)) {
            redisService.delete("Refresh_Token_" + request.getRefreshToken());
            refreshToken = doGenerateRefreshToken(userDetails.getUsername());

            TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
            return tokenResponse;
        } else {
            throw new AuthenticationException("Refresh_Token invalid");
        }
    }

    public void deletedToken(TokenResponse request) {
        if (StringUtil.isNotEmpty(request.getRefreshToken())) {
            Object tokenExisted = redisService.get("Refresh_Token_" + request.getRefreshToken());
            if (ObjectUtils.isEmpty(tokenExisted)) {
                throw new SSKFException("500", "Token invalid!");
            }
            redisService.delete("Refresh_Token_" + request.getRefreshToken());
        }
        redisService.delete("Access_Token_" + request.getAccessToken());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        ShainEntity userSys = shainRepository.findByShainCd(username);
        claims.put("expiredHours", 4);
        try {
            String shainNm = Base64.getEncoder().encodeToString(userSys.getShainNm().getBytes("UTF-8"));
            claims.put("shainNm", shainNm);
            String bumonNm = Base64.getEncoder().encodeToString(userSys.getMstBumonEntity().getBumonNm().getBytes("UTF-8"));
            claims.put("bumonNm", bumonNm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String bumonCd = userSys.getMstBumonEntity().getBumonCd();
        claims.put("bumonCd", bumonCd);

        List<String> role = new ArrayList<>();
        if (Constants.USER_ROLE_ADMIN_BUMON.equals(bumonCd)) {
            role.add(Constants.ROLE_ADMIN);
            claims.put("ROLE", role);
        }
        if (Constants.USER_ROLE_ACCOUNTING_CHARGE_BUMON.equals(bumonCd)) {
            role.add(Constants.ROLE_ACCOUNTING_CHARGE);
            claims.put("ROLE", role);
        }
        String token = Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        redisService.put("Access_Token_" + token, token, 4 * Constants.JWT_TOKEN_MAX_AGE);
        return token;
    }

    private String doGenerateRefreshToken(String subject) {
        Long timeOut = 14 * 24 * Constants.JWT_TOKEN_MAX_AGE;
        Date expirationDate = new Date(System.currentTimeMillis() + (timeOut * 60 * 1000));
        String token = Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();

        redisService.put("Refresh_Token_" + token, token, timeOut);
        return token;
    }

}
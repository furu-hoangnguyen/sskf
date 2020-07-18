package sskf.controller;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sskf.config.jwt.JwtRequest;
import sskf.config.jwt.JwtTokenUtil;
import sskf.config.jwt.JwtUserDetailsService;
import sskf.model.BaseStatusError;
import sskf.model.request.RefreshTokenRequest;
import sskf.model.response.TokenResponse;

import javax.validation.Valid;

@RestController
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping(value = "/api/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest) {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());
            final TokenResponse token = jwtTokenUtil.generateToken(userDetails, authenticationRequest.getRememberMe());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("ユーザ名とパスワードを確認してください。");
        }
    }

    @RequestMapping(value = "/api/log-out", method = RequestMethod.POST)
    public ResponseEntity<?> logout(@Valid @RequestBody TokenResponse request) {
        jwtTokenUtil.deletedToken(request);
        return ResponseEntity.ok("Logout success!");
    }

    @RequestMapping(value = "/api/refresh-token", method = RequestMethod.POST)
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        try {
            final TokenResponse token = jwtTokenUtil.refreshToken(refreshToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        } catch (AuthenticationException exception) {
            BaseStatusError baseStatusError = new BaseStatusError(HttpStatus.UNAUTHORIZED.toString(), exception.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseStatusError);
        }
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            UsernamePasswordAuthenticationToken us = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(us);
        } catch (DisabledException e) {
            throw new AuthenticationException("ユーザ名とパスワードを確認してください。");
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("ユーザ名とパスワードを確認してください。");
        }
    }
}

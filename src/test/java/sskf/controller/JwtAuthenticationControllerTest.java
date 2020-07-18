package sskf.controller;

import org.apache.tomcat.websocket.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.config.jwt.JwtRequest;
import sskf.config.jwt.JwtTokenUtil;
import sskf.config.jwt.JwtUserDetailsService;
import sskf.model.request.RefreshTokenRequest;
import sskf.model.response.TokenResponse;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
public class JwtAuthenticationControllerTest {

    @InjectMocks
    private JwtAuthenticationController jwtAuthenticationController;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    private UsernamePasswordAuthenticationToken us;
    private TokenResponse token;
    @BeforeEach
    public void setup() {
        token = new TokenResponse("accessToken", "refreshToken");
        us = new UsernamePasswordAuthenticationToken("ADMIN", "1234");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAuthenticationTokenSuccessTest() {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("ADMIN", "1234", Collections.emptyList());
        UserDetails userDetails = new User("ADMIN", "1234", Collections.emptyList());
        Mockito.when(authenticationManager.authenticate(us)).thenReturn(usernamePasswordAuthenticationToken);
        Mockito.when(userDetailsService.loadUserByUsername("ADMIN")).thenReturn(userDetails);
        Mockito.when(jwtTokenUtil.generateToken(Mockito.any(), Mockito.anyBoolean())).thenReturn(token);
        JwtRequest authenticationRequest = new JwtRequest("Admin", "1234", false);
        ResponseEntity<?> response = jwtAuthenticationController.createAuthenticationToken(authenticationRequest);
        Assert.assertEquals(response.getStatusCodeValue(), 200);
        Assert.assertEquals(response.getBody(), token);
    }

    @Test
    public void refreshTokenSuccessTest() throws AuthenticationException {
        Mockito.when(jwtTokenUtil.refreshToken(Mockito.any(RefreshTokenRequest.class))).thenReturn(token);
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("refresh token");
        ResponseEntity<?> response = jwtAuthenticationController.refreshToken(refreshTokenRequest);
        Assert.assertEquals(response.getStatusCodeValue(), 201);
        Assert.assertEquals(response.getBody(), token);
    }

}

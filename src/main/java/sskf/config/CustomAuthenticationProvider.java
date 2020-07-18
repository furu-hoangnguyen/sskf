package sskf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import sskf.model.entity.ShainEntity;
import sskf.repository.ShainRepository;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationManager {

    @Autowired
    private ShainRepository shainRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        ShainEntity userSys = shainRepository.findByShainCd(username);
        if (userSys.getShainCd().equals(username) && userSys.getPassword().equals(password)) {
            return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
        } else {
            throw new BadCredentialsException("Authentication failed");
        }
    }
}

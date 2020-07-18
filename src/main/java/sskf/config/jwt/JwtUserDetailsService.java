package sskf.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sskf.model.entity.ShainEntity;
import sskf.repository.ShainRepository;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private ShainRepository shainRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ShainEntity userSys = shainRepository.findByShainCd(username);
        if (userSys == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(username, userSys.getPassword(),
                    new ArrayList<>());
    }
}
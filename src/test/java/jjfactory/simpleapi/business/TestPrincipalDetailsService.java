package jjfactory.simpleapi.business;

import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.domain.Role;
import jjfactory.simpleapi.global.config.auth.PrincipalDetails;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@Profile("test")
public class TestPrincipalDetailsService implements UserDetailsService {

    public static final String USERNAME = "wogud222";

    private Rider getRider() {
        return Rider.builder()
                .loginId(USERNAME)
                .name("이재형")
                .password("1234")
                .phone("01012341234")
                .roles(Collections.singletonList(Role.ROLE_USER))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if (name.equals(USERNAME)) {
            return new PrincipalDetails(getRider());
        }
        return null;
    }
}

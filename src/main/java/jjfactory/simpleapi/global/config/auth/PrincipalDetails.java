package jjfactory.simpleapi.global.config.auth;

import jjfactory.simpleapi.business.rider.domain.Rider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PrincipalDetails implements UserDetails {
    private Rider rider;

    public PrincipalDetails(Rider rider) {
        this.rider = rider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rider.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return rider.getPassword();
    }

    @Override
    public String getUsername() {
        return rider.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

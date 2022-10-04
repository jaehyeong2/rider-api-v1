package jjfactory.simpleapi.business;

import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.domain.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import javax.persistence.RollbackException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        String loginId = annotation.loginId();
        Role role = (annotation.role().equals("user")) ? Role.ROLE_USER : Role.ROLE_ADMIN;

        Rider authUser = Rider.builder()
                .loginId(loginId)
                .roles(Collections.singletonList(role))
                .build();
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(authUser, "password", List.of(new SimpleGrantedAuthority(role.toString())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}

package jjfactory.simpleapi.global.config.auth;

import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.repository.RiderRepository;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {
    private final RiderRepository riderRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Rider rider = riderRepository.findByLoginId(username).orElseThrow(()->{
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        });

        return new PrincipalDetails(rider);
    }
}

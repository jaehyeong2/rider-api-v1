package jjfactory.simpleapi.global.config.auth;
import com.google.gson.Gson;
import jjfactory.simpleapi.global.dto.res.ErrorResponse;
import jjfactory.simpleapi.global.ex.ErrorCode;
import jjfactory.simpleapi.global.ex.ExceptionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// 시큐리티 설정
@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final ExceptionFilter exceptionFilter;


    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/h2-console/**");
    }

    //시큐리티 설정 바꾸고 싶으면 여기 수정하면 됨
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic()
                .disable()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()//이거없으면 큰일남
                .antMatchers("/**").permitAll().
                and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                .and().
                addFilterBefore(new TokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class).
        addFilterBefore(exceptionFilter,TokenFilter.class)
        ;
    }

    private AccessDeniedHandler accessDeniedHandler() {
        log.info("accessDeniedHandler");

        return (httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            accessDeniedException(httpServletRequest, httpServletResponse);
        };
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        log.info("authenticationEntryPoint");

        return (httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            invalidTokenException(httpServletRequest, httpServletResponse);
        };
    }

    private void accessDeniedException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        log.info("accessDeniedException");


        PrintWriter out = httpServletResponse.getWriter();
        httpServletResponse.setContentType("application/json");
        httpServletRequest.setCharacterEncoding("UTF-8");

        final ErrorResponse exception = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);

        out.print(new Gson().toJson(exception));
        out.flush();
    }

    private void invalidTokenException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        log.info("invalidTokenException");


        PrintWriter out = httpServletResponse.getWriter();
        httpServletResponse.setContentType("application/json");
        httpServletRequest.setCharacterEncoding("UTF-8");

        final ErrorResponse exception = ErrorResponse.of(ErrorCode.HANDLE_INVALID_TOKEN);

        out.print(new Gson().toJson(exception));
        out.flush();
    }
}

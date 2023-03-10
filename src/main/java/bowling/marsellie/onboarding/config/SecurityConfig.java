package bowling.marsellie.onboarding.config;

import bowling.marsellie.onboarding.Endpoints;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    public static final String SESSION_COOKIE = "JSESSIONID";

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http.authorizeHttpRequests()
                .requestMatchers(Endpoints.PREFIX + "/**").permitAll()
                .anyRequest().authenticated()

                .and()

                .formLogin()
                .loginProcessingUrl(Endpoints.LOGIN)
                .successHandler(authSuccessHandler())
                .failureHandler(authFailureHandler())
                .permitAll()

                .and()

                .logout()
                .logoutUrl(Endpoints.LOGOUT)
                .invalidateHttpSession(true)
                .deleteCookies(SESSION_COOKIE)
                .logoutSuccessHandler(logoutSuccessHandler())

                .and()

                .csrf()
                .disable()
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationSuccessHandler authSuccessHandler() {
        return (req, resp, auth) -> resp.setStatus(HttpStatus.OK.value());
    }

    private AuthenticationFailureHandler authFailureHandler() {
        return (req, resp, e) -> resp.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return (req, resp, auth) -> resp.setStatus(HttpStatus.OK.value());
    }
}

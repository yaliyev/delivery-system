package de.yagub.deliverysystem.msuser.config;

import de.yagub.deliverysystem.msuser.service.UserService;
import de.yagub.deliverysystem.msuser.converter.JwtAuthConverter;
import de.yagub.deliverysystem.msuser.converter.RealmRoleConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.Jwt;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/public/**", "/api/users/register","/error").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )

                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .exceptionHandling(exceptions -> exceptions

                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthConverter converter = new JwtAuthConverter(new RealmRoleConverter());
        // Add any additional configuration here
        return converter;
    }
}

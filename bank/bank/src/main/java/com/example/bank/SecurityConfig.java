package com.example.bank;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService, JwtAuthenticationFilter jwtFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/**", "/accounts").permitAll()
                        .requestMatchers("/v3/api-docs/",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/accounts/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/accounts/getall").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/transactions/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/accounts/deposit/**", "/accounts/withdraw/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().permitAll()
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
package com.example.bank;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("SECURITY LOADED");

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.DELETE, "/accounts/**").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "/accounts").permitAll()
                        //.requestMatchers("/accounts/deposit/**", "/accounts/withdraw/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("Admin")
                .password("{noop}admin123")
                .roles("Admin")
                .build();
        return new InMemoryUserDetailsManager(admin);

    }
}

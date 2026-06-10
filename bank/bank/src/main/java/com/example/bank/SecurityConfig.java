package com.example.bank;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("SECURITY LOADED");

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, "/accounts").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/accounts/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/accounts/getall").hasAuthority("ADMIN")
                        .requestMatchers("/accounts/deposit/**", "/accounts/withdraw/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(BankAccountService bankAccountService, PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .authorities("ADMIN")
                .build();

        return username -> {
            if ("admin".equalsIgnoreCase(username)) {
                return admin;
            }
            return bankAccountService.loadUserByUsername(username);
        };
    }
    
}
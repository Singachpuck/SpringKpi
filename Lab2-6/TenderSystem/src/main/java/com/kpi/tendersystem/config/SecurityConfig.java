package com.kpi.tendersystem.config;

import com.kpi.tendersystem.model.auth.DaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    @DependsOn("passwordEncoder")
//    public InMemoryUserDetailsManager userDetailsService(@Autowired final PasswordEncoder passwordEncoder) {
//        final UserDetails adam = User.builder()
//                .username("adam")
//                .password(passwordEncoder.encode("adamPassword"))
//                .roles("USER")
//                .build();
//
//        final UserDetails linda = User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("lindaPassword"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(adam, linda);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                )
                .formLogin();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    @DependsOn("passwordEncoder")
    public DaoAuthenticationProvider daoAuthenticationProvider(final PasswordEncoder passwordEncoder, final DaoUserDetailsService detailsService) {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(detailsService);
        return provider;
    }

}

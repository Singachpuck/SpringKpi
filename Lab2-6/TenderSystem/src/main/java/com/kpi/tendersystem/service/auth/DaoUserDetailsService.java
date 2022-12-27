package com.kpi.tendersystem.service.auth;

import com.kpi.tendersystem.model.auth.UserPrincipal;
import com.kpi.tendersystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DaoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return new UserPrincipal(userService.getByUsername(username).get());
        } catch (RuntimeException e) {
            throw new UsernameNotFoundException("User: " + username + " doesn't exist");
        }
    }
}

package me.igrade.security.service;

import lombok.RequiredArgsConstructor;
import me.igrade.user.model.UserRole;
import me.igrade.user.service.impl.UserServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserServiceImpl userService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<me.igrade.user.model.User> optionalUser = userService.getUserByEmail(email);


        if(optionalUser.isPresent()){
            final me.igrade.user.model.User user = optionalUser.get();

            return new User(user.getEmail(),
                    user.getPassword(),
                    user.isEnabled(),
                    true,
                    true,
                    true,
                    getAuthorities(user.getRole())
            );
        }
        throw new UsernameNotFoundException("Email is not correct!");
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserRole userRole){
        return userRole.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName())).collect(Collectors.toList());
    }
}

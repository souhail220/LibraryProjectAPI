package com.library.libraryprojectapi.authentication.service;

import com.library.libraryprojectapi.authentication.models.UserPrincipal;
import com.library.libraryprojectapi.data.entities.User;
import com.library.libraryprojectapi.repositories.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new UserPrincipal(user);
        }

        throw new UsernameNotFoundException("User not found with authenticator: " + email);
    }
}

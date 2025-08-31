package com.fumology.carrental.configuration.Security;

import com.fumology.carrental.database.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CarRentalUserDetailService implements UserDetailsService {

    private static final String NOT_FOUND_MESSAGE = "Email not found";

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CarRentalUserDetails(
                userRepository.findByEmailIgnoreCase(email).orElseThrow(
                        () -> new UsernameNotFoundException(NOT_FOUND_MESSAGE)
                )
        );
    }
}

package com.lesya.booking1.security;

import com.lesya.booking1.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
// EN: Loads user from database and converts it into Spring Security format.
// UA: Завантажує користувача з БД та конвертує його у формат Spring Security.
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // EN: Fetch user from DB or throw exception.
        // UA: Шукаємо користувача в БД за email, або кидаємо виняток.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // EN: Map custom User entity to Spring Security's standard User object.
        // UA: Мапимо нашу сутність User у стандартний об'єкт User від Spring Security.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                )
        );
    }
}

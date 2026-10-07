package com.wayrapass.security;
import com.wayrapass.repository.UserRepository; import lombok.RequiredArgsConstructor; import org.springframework.security.core.userdetails.*; import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor public class CustomUserDetailsService implements UserDetailsService { private final UserRepository users; public UserDetails loadUserByUsername(String email){return UserPrincipal.from(users.findByEmailIgnoreCase(email).orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado")));} }

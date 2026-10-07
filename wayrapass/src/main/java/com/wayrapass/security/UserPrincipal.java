package com.wayrapass.security;
import com.wayrapass.model.User; import lombok.Getter; import org.springframework.security.core.*; import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.security.core.userdetails.UserDetails; import java.util.*;
@Getter public class UserPrincipal implements UserDetails {
 private final Long id; private final String username; private final String password; private final boolean enabled; private final boolean accountNonLocked; private final Collection<? extends GrantedAuthority> authorities;
 private UserPrincipal(User u){id=u.getId();username=u.getEmail();password=u.getPasswordHash();enabled=u.isEnabled();accountNonLocked=u.getLockedUntil()==null||!u.getLockedUntil().isAfter(java.time.Instant.now());authorities=List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole().name()));}
 public static UserPrincipal from(User u){return new UserPrincipal(u);} public boolean isAccountNonExpired(){return true;} public boolean isCredentialsNonExpired(){return true;}
}

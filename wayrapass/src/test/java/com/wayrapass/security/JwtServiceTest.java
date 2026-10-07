package com.wayrapass.security;
import com.wayrapass.model.*;import org.junit.jupiter.api.Test;import java.util.Base64;import static org.junit.jupiter.api.Assertions.*;
class JwtServiceTest{
 @Test void createsAndValidatesSignedToken(){String secret=Base64.getEncoder().encodeToString("a-development-secret-that-is-at-least-thirty-two-bytes".getBytes());JwtService jwt=new JwtService(secret,60_000);User u=new User();u.setId(7L);u.setEmail("student@example.com");u.setPasswordHash("encoded");u.setRole(Role.STUDENT);u.setEnabled(true);UserPrincipal p=UserPrincipal.from(u);String token=jwt.generate(p);assertEquals("student@example.com",jwt.username(token));assertTrue(jwt.valid(token,p));}
}

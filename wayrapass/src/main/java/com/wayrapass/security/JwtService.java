package com.wayrapass.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import org.springframework.security.core.userdetails.UserDetails; import javax.crypto.SecretKey; import java.nio.charset.StandardCharsets; import java.util.*;
@Service public class JwtService {
 private final SecretKey key; private final long expirationMs;
 public JwtService(@Value("${jwt.secret}") String secret,@Value("${jwt.expiration-ms}") long expirationMs){byte[] raw;try{raw=Base64.getDecoder().decode(secret);}catch(IllegalArgumentException e){raw=secret.getBytes(StandardCharsets.UTF_8);}if(raw.length<32)throw new IllegalArgumentException("JWT_SECRET debe contener al menos 256 bits");this.key=Keys.hmacShaKeyFor(raw);this.expirationMs=expirationMs;}
 public String generate(UserPrincipal p){var now=new Date();return Jwts.builder().subject(p.getUsername()).claim("uid",p.getId()).issuedAt(now).expiration(new Date(now.getTime()+expirationMs)).signWith(key).compact();}
 public String username(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
 public boolean valid(String token,UserDetails u){return username(token).equalsIgnoreCase(u.getUsername())&&Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().after(new Date());}
}

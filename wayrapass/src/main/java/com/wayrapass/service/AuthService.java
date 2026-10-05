package com.wayrapass.service;

import com.wayrapass.dto.request.LoginRequestDTO;
import com.wayrapass.dto.request.UserRequestDTO;
import com.wayrapass.dto.response.AuthResponseDTO;
import com.wayrapass.model.Role;
import com.wayrapass.model.User;
import com.wayrapass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(noRollbackFor = ResponseStatusException.class)
public class AuthService {

    private static final int PASSWORD_ITERATIONS = 310_000;
    private static final int PASSWORD_SALT_LENGTH = 16;
    private static final int PASSWORD_KEY_LENGTH = 256;
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOCK_DURATION_SECONDS = 15 * 60;
    private static final String PASSWORD_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String JWT_ALGORITHM = "HmacSHA256";
    private static final Pattern SUBJECT_PATTERN = Pattern.compile("\"sub\":\"([1-9][0-9]*)\"");
    private static final Pattern EXPIRATION_PATTERN = Pattern.compile("\"exp\":([0-9]+)");
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final byte[] jwtSecret;
    private final long jwtExpirationMs;

    public AuthService(
            UserRepository userRepository,
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration-ms}") long jwtExpirationMs
    ) {
        this.userRepository = userRepository;
        this.jwtSecret = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (this.jwtSecret.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 bytes.");
        }
        if (jwtExpirationMs <= 0) {
            throw new IllegalArgumentException("jwt.expiration-ms debe ser mayor que cero.");
        }
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public AuthResponseDTO register(UserRequestDTO request) {
        String email = normalizeEmail(request.email());
        String phone = request.phone().trim();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado.");
        }
        if (userRepository.existsByPhone(phone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El teléfono ya está registrado.");
        }
        if (request.role() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede registrar una cuenta administradora.");
        }

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPasswordHash(hashPassword(request.password()));
        user.setPhone(phone);
        user.setRole(request.role() == null ? Role.STUDENT : request.role());
        user.setEnabled(true);
        userRepository.save(user);
        return createAuthResponse(user);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmailIgnoreCase(normalizeEmail(request.email()))
                .orElseThrow(() -> invalidCredentials());
        Instant now = Instant.now();
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "La cuenta está temporalmente bloqueada.");
        }
        if (!user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cuenta está deshabilitada.");
        }

        if (!verifyPassword(request.password(), user.getPasswordHash())) {
            int failedAttempts = user.getFailedLoginAttempts() + 1;
            if (failedAttempts >= MAX_LOGIN_ATTEMPTS) {
                user.setFailedLoginAttempts(0);
                user.setLockedUntil(now.plusSeconds(LOCK_DURATION_SECONDS));
                userRepository.save(user);
                throw new ResponseStatusException(HttpStatus.LOCKED, "Demasiados intentos. La cuenta está bloqueada por 15 minutos.");
            }
            user.setFailedLoginAttempts(failedAttempts);
            userRepository.save(user);
            throw invalidCredentials();
        }

        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        return createAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public User requireAuthenticatedUser(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw unauthorizedToken();
        }

        String[] tokenParts = authorizationHeader.substring(7).trim().split("\\.", -1);
        if (tokenParts.length != 3) {
            throw unauthorizedToken();
        }

        try {
            String header = new String(Base64.getUrlDecoder().decode(tokenParts[0]), StandardCharsets.UTF_8);
            if (!"{\"alg\":\"HS256\",\"typ\":\"JWT\"}".equals(header)) {
                throw unauthorizedToken();
            }

            String signedContent = tokenParts[0] + "." + tokenParts[1];
            byte[] providedSignature = Base64.getUrlDecoder().decode(tokenParts[2]);
            if (!MessageDigest.isEqual(sign(signedContent), providedSignature)) {
                throw unauthorizedToken();
            }

            String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]), StandardCharsets.UTF_8);
            Matcher subjectMatcher = SUBJECT_PATTERN.matcher(payload);
            Matcher expirationMatcher = EXPIRATION_PATTERN.matcher(payload);
            if (!subjectMatcher.find() || !expirationMatcher.find()) {
                throw unauthorizedToken();
            }

            long userId = Long.parseLong(subjectMatcher.group(1));
            long expiration = Long.parseLong(expirationMatcher.group(1));
            if (expiration <= Instant.now().getEpochSecond()) {
                throw unauthorizedToken();
            }

            User user = userRepository.findById(userId).orElseThrow(AuthService::unauthorizedToken);
            if (!user.isEnabled()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cuenta está deshabilitada.");
            }
            return user;
        } catch (IllegalArgumentException exception) {
            throw unauthorizedToken();
        }
    }

    private AuthResponseDTO createAuthResponse(User user) {
        AuthResponseDTO.UserResponseDTO userResponse = new AuthResponseDTO.UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
        return new AuthResponseDTO(createToken(user), "Bearer", userResponse);
    }

    private String createToken(User user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(jwtExpirationMs);
        String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64Url(
                "{\"sub\":\"" + user.getId()
                        + "\",\"role\":\"" + user.getRole().name()
                        + "\",\"iat\":" + issuedAt.getEpochSecond()
                        + ",\"exp\":" + expiresAt.getEpochSecond() + "}"
        );
        String unsignedToken = header + "." + payload;
        return unsignedToken + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(sign(unsignedToken));
    }

    private byte[] sign(String value) {
        try {
            Mac mac = Mac.getInstance(JWT_ALGORITHM);
            mac.init(new SecretKeySpec(jwtSecret, JWT_ALGORITHM));
            return mac.doFinal(value.getBytes(StandardCharsets.US_ASCII));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("No se pudo firmar el token de autenticación.", exception);
        }
    }

    private String hashPassword(String password) {
        byte[] salt = new byte[PASSWORD_SALT_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        byte[] hash = derivePassword(password, salt, PASSWORD_ITERATIONS);
        return "pbkdf2$" + PASSWORD_ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    private boolean verifyPassword(String password, String encodedPassword) {
        String[] parts = encodedPassword.split("\\$", -1);
        if (parts.length != 4 || !"pbkdf2".equals(parts[0])) {
            throw new IllegalStateException("El formato de hash de contraseña almacenado no es válido.");
        }
        try {
            int iterations = Integer.parseInt(parts[1]);
            if (iterations < 100_000 || iterations > 2_000_000) {
                throw new IllegalStateException("El número de iteraciones del hash almacenado no es válido.");
            }
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);
            byte[] actualHash = derivePassword(password, salt, iterations);
            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("El hash de contraseña almacenado no es válido.", exception);
        }
    }

    private byte[] derivePassword(String password, byte[] salt, int iterations) {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, PASSWORD_KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PASSWORD_ALGORITHM);
            return factory.generateSecret(keySpec).getEncoded();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("No se pudo procesar la contraseña.", exception);
        } finally {
            keySpec.clearPassword();
        }
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String base64Url(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos.");
    }

    private static ResponseStatusException unauthorizedToken() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token de autenticación no es válido.");
    }
}

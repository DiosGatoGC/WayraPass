package com.wayrapass.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity @Table(name="password_reset_tokens") @Getter @Setter @NoArgsConstructor
public class PasswordResetToken {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="user_id", nullable=false) private User user;
    @Column(name="token_hash", nullable=false, unique=true, length=255) private String tokenHash;
    @Column(name="expires_at", nullable=false) private Instant expiresAt;
    @Column(name="used_at") private Instant usedAt;
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
    @PrePersist void create(){ if(createdAt==null) createdAt=Instant.now(); }
}

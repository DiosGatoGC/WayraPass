package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="vehicles") @Getter @Setter @NoArgsConstructor
public class Vehicle {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=20) private String plate; @Column(length=60) private String brand; @Column(length=60) private String model;
 @Column(nullable=false) private int capacity; @Column(name="qr_token",nullable=false,unique=true,length=120) private String qrToken;
 @Enumerated(EnumType.STRING) @Column(name="authorization_status",nullable=false,length=20) private AuthorizationStatus authorizationStatus=AuthorizationStatus.PENDING;
 @Enumerated(EnumType.STRING) @Column(name="operational_status",nullable=false,length=20) private VehicleOperationalStatus operationalStatus=VehicleOperationalStatus.AVAILABLE;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="updated_at",nullable=false) private Instant updatedAt;
 @PrePersist void create(){var n=Instant.now();createdAt=n;updatedAt=n;} @PreUpdate void update(){updatedAt=Instant.now();}
}

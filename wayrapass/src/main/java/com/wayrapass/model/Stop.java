package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal; import java.time.Instant;
@Entity @Table(name="stops") @Getter @Setter @NoArgsConstructor
public class Stop {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=150) private String name; @Column(length=250) private String address; @Column(length=100) private String district;
 @Column(precision=9,scale=6) private BigDecimal latitude; @Column(precision=9,scale=6) private BigDecimal longitude;
 @Column(nullable=false) private boolean active=true; @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
 @PrePersist void create(){if(createdAt==null)createdAt=Instant.now();}
}

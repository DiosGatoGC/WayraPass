package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal; import java.time.Instant;
@Entity @Table(name="trip_locations") @Getter @Setter @NoArgsConstructor
public class TripLocation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="trip_id",nullable=false) private Trip trip;
 @Column(nullable=false,precision=9,scale=6) private BigDecimal latitude; @Column(nullable=false,precision=9,scale=6) private BigDecimal longitude;
 @Column(name="recorded_at",nullable=false,updatable=false) private Instant recordedAt;
 @PrePersist void create(){if(recordedAt==null)recordedAt=Instant.now();}
}

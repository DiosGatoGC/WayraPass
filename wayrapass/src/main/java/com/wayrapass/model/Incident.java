package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="incidents") @Getter @Setter @NoArgsConstructor
public class Incident {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="trip_id",nullable=false) private Trip trip;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="driver_id",nullable=false) private Driver driver;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private IncidentType type;
 @Column(length=1000) private String description; @Column(name="delay_minutes",nullable=false) private int delayMinutes;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private IncidentPriority priority=IncidentPriority.NORMAL;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private IncidentStatus status=IncidentStatus.OPEN;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="resolved_at") private Instant resolvedAt;
 @PrePersist void create(){if(createdAt==null)createdAt=Instant.now();}
}

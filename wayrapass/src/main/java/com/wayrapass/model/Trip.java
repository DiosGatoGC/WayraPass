package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="trips") @Getter @Setter @NoArgsConstructor
public class Trip {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="route_id",nullable=false) private Route route;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="route_schedule_id") private RouteSchedule routeSchedule;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="driver_id") private Driver driver;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="vehicle_id") private Vehicle vehicle;
 @Column(name="scheduled_start_at",nullable=false) private Instant scheduledStartAt; @Column(name="scheduled_end_at") private Instant scheduledEndAt;
 @Column(name="actual_start_at") private Instant actualStartAt; @Column(name="actual_end_at") private Instant actualEndAt;
 @Column(name="estimated_arrival_at") private Instant estimatedArrivalAt;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private TripStatus status=TripStatus.SCHEDULED;
 @Column(name="was_reassigned",nullable=false) private boolean wasReassigned;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="updated_at",nullable=false) private Instant updatedAt;
 @PrePersist void create(){var n=Instant.now();createdAt=n;updatedAt=n;} @PreUpdate void update(){updatedAt=Instant.now();}
}

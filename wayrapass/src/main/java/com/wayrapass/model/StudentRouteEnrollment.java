package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.*;
@Entity @Table(name="student_route_enrollments") @Getter @Setter @NoArgsConstructor
public class StudentRouteEnrollment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="student_id",nullable=false) private Student student;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="route_id",nullable=false) private Route route;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="stop_id",nullable=false) private Stop stop;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private EnrollmentStatus status=EnrollmentStatus.ACTIVE;
 @Column(name="valid_from",nullable=false) private LocalDate validFrom; @Column(name="valid_until") private LocalDate validUntil;
 @Column(name="suspended_from") private LocalDate suspendedFrom; @Column(name="suspended_until") private LocalDate suspendedUntil;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="updated_at",nullable=false) private Instant updatedAt;
 @PrePersist void create(){var n=Instant.now();if(validFrom==null)validFrom=LocalDate.now();createdAt=n;updatedAt=n;} @PreUpdate void update(){updatedAt=Instant.now();}
}

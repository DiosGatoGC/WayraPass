package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="student_family_links", uniqueConstraints=@UniqueConstraint(columnNames={"student_id","family_user_id"})) @Getter @Setter @NoArgsConstructor
public class StudentFamilyLink {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="student_id",nullable=false) private Student student;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="family_user_id",nullable=false) private User familyUser;
 @Column(length=50) private String relationship;
 @Column(nullable=false) private boolean authorized=true;
 @Column(name="linked_at",nullable=false,updatable=false) private Instant linkedAt;
 @Column(name="revoked_at") private Instant revokedAt;
 @Column(name="notify_boarding",nullable=false) private boolean notifyBoarding=true;
 @Column(name="notify_arrival",nullable=false) private boolean notifyArrival=true;
 @Column(name="notify_delay",nullable=false) private boolean notifyDelay=true;
 @Column(name="notify_vehicle_change",nullable=false) private boolean notifyVehicleChange=true;
 @Column(name="notify_incident",nullable=false) private boolean notifyIncident=true;
 @PrePersist void create(){if(linkedAt==null) linkedAt=Instant.now();}
}

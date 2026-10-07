package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="notifications") @Getter @Setter @NoArgsConstructor
public class Notification {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="recipient_user_id",nullable=false) private User recipient;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="trip_id") private Trip trip; @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="incident_id") private Incident incident;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private NotificationType type;
 @Column(length=150) private String title; @Column(nullable=false,length=1000) private String message;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="read_at") private Instant readAt;
 @PrePersist void create(){if(createdAt==null)createdAt=Instant.now();}
}

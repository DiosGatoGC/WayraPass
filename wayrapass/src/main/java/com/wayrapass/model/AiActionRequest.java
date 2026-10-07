package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="ai_action_requests") @Getter @Setter @NoArgsConstructor
public class AiActionRequest {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="requested_by_user_id",nullable=false) private User requestedBy;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="confirmed_by_user_id") private User confirmedBy;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="trip_id",nullable=false) private Trip trip;
 @Enumerated(EnumType.STRING) @Column(name="action_type",nullable=false,length=30) private AiActionType actionType;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="proposed_driver_id") private Driver proposedDriver;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="proposed_vehicle_id") private Vehicle proposedVehicle;
 @Column(name="request_text",nullable=false,length=1000) private String requestText; @Column(name="ai_explanation",length=1500) private String aiExplanation;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private AiActionStatus status=AiActionStatus.PENDING;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="confirmed_at") private Instant confirmedAt; @Column(name="executed_at") private Instant executedAt;
 @PrePersist void create(){if(createdAt==null)createdAt=Instant.now();}
}

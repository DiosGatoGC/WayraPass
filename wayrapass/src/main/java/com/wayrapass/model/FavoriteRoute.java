package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="favorite_routes") @IdClass(FavoriteRouteId.class) @Getter @Setter @NoArgsConstructor
public class FavoriteRoute {
 @Id @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="student_id",nullable=false) private Student student;
 @Id @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="route_id",nullable=false) private Route route;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
 @PrePersist void create(){if(createdAt==null)createdAt=Instant.now();}
}

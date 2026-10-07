package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="boardings",uniqueConstraints=@UniqueConstraint(columnNames={"trip_id","student_id"})) @Getter @Setter @NoArgsConstructor
public class Boarding {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="trip_id",nullable=false) private Trip trip;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="student_id",nullable=false) private Student student;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="stop_id",nullable=false) private Stop stop;
 @Column(name="boarded_at",nullable=false,updatable=false) private Instant boardedAt;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private BoardingStatus status=BoardingStatus.CONFIRMED;
 @PrePersist void create(){if(boardedAt==null)boardedAt=Instant.now();}
}

package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*; import java.time.*;
@Entity @Table(name="route_schedules",uniqueConstraints=@UniqueConstraint(columnNames={"route_id","day_of_week","departure_time"})) @Getter @Setter @NoArgsConstructor
public class RouteSchedule {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="route_id",nullable=false) private Route route;
 @Enumerated(EnumType.STRING) @Column(name="day_of_week",nullable=false,length=10) private DayOfWeek dayOfWeek;
 @Column(name="departure_time",nullable=false) private LocalTime departureTime; @Column(name="estimated_arrival_time") private LocalTime estimatedArrivalTime;
 @Column(nullable=false) private boolean active=true;
}

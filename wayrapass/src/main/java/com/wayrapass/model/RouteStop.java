package com.wayrapass.model;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="route_stops",uniqueConstraints={@UniqueConstraint(columnNames={"route_id","stop_id"}),@UniqueConstraint(columnNames={"route_id","stop_order"})}) @Getter @Setter @NoArgsConstructor
public class RouteStop {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="route_id",nullable=false) private Route route;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="stop_id",nullable=false) private Stop stop;
 @Column(name="stop_order",nullable=false) private int stopOrder;
 @Column(name="estimated_minutes_from_start",nullable=false) private int estimatedMinutesFromStart;
 @Column(nullable=false) private boolean active=true;
}

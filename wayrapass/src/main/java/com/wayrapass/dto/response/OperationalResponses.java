package com.wayrapass.dto.response;
import com.wayrapass.model.*; import java.math.BigDecimal; import java.time.*;
public final class OperationalResponses {private OperationalResponses(){}
 public record FamilyCode(String code,Instant expiresAt){} public record Enrollment(Long id,Long studentId,Long routeId,Long stopId,EnrollmentStatus status,LocalDate validFrom,LocalDate validUntil){}
 public record Trip(Long id,Long routeId,Long driverId,Long vehicleId,Instant scheduledStartAt,Instant scheduledEndAt,Instant estimatedArrivalAt,TripStatus status,boolean wasReassigned){}
 public record Location(Long id,Long tripId,BigDecimal latitude,BigDecimal longitude,Instant recordedAt){} public record Boarding(Long id,Long tripId,Long studentId,Long stopId,Instant boardedAt){}
 public record Incident(Long id,Long tripId,IncidentType type,int delayMinutes,IncidentPriority priority,IncidentStatus status,Instant createdAt){}
 public record Notification(Long id,NotificationType type,String title,String message,Instant createdAt,Instant readAt){}
 public record AiAction(Long id,Long tripId,AiActionType actionType,AiActionStatus status,Long proposedDriverId,Long proposedVehicleId){}
}

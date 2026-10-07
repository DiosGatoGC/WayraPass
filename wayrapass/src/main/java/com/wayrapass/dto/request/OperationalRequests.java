package com.wayrapass.dto.request;
import com.wayrapass.model.*; import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.time.Instant;
public final class OperationalRequests {private OperationalRequests(){}
 public record FamilyCode(@NotBlank @Size(max=100) String code,@Size(max=50) String relationship){}
 public record Enrollment(@NotNull Long routeId,@NotNull Long stopId){}
 public record Trip(@NotNull Long routeId,Long routeScheduleId,Long driverId,Long vehicleId,@NotNull @FutureOrPresent Instant scheduledStartAt,@NotNull @Future Instant scheduledEndAt){}
 public record Reassignment(Long driverId,Long vehicleId){}
 public record TripStatusUpdate(@NotNull TripStatus status){}
 public record Location(@NotNull @DecimalMin("-90") @DecimalMax("90") BigDecimal latitude,@NotNull @DecimalMin("-180") @DecimalMax("180") BigDecimal longitude){}
 public record Boarding(@NotNull Long tripId,@NotNull Long stopId,@NotBlank String vehicleQrToken){}
 public record Incident(@NotNull Long tripId,@NotNull IncidentType type,@Size(max=1000) String description,@PositiveOrZero int delayMinutes,@NotNull IncidentPriority priority){}
 public record AiProposal(@NotNull Long tripId,@NotNull AiActionType actionType,Long proposedDriverId,Long proposedVehicleId,@NotBlank @Size(max=1000) String requestText,@Size(max=1500) String aiExplanation){}
}

package com.wayrapass.controller;
import com.wayrapass.dto.request.OperationalRequests;import com.wayrapass.dto.response.OperationalResponses;import com.wayrapass.service.OperationalService;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequiredArgsConstructor public class OperationalController{private final OperationalService s;
 @GetMapping("/api/trips") @PreAuthorize("hasRole('COORDINATOR')") public List<OperationalResponses.Trip> trips(){return s.allTrips();}
 @GetMapping("/api/trips/me") @PreAuthorize("hasRole('DRIVER')") public List<OperationalResponses.Trip> myTrips(){return s.myDriverTrips();}
 @PostMapping("/api/trips") @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasRole('COORDINATOR')") public OperationalResponses.Trip trip(@Valid @RequestBody OperationalRequests.Trip r){return s.createTrip(r);}
 @PutMapping("/api/trips/{id}/reassignment") @PreAuthorize("hasRole('COORDINATOR')") public OperationalResponses.Trip reassign(@PathVariable Long id,@RequestBody OperationalRequests.Reassignment r){return s.reassign(id,r);}
 @PutMapping("/api/trips/{id}/status") @PreAuthorize("hasAnyRole('COORDINATOR','DRIVER')") public OperationalResponses.Trip status(@PathVariable Long id,@Valid @RequestBody OperationalRequests.TripStatusUpdate r){return s.status(id,r.status());}
 @PostMapping("/api/trips/{id}/locations") @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasRole('DRIVER')") public OperationalResponses.Location location(@PathVariable Long id,@Valid @RequestBody OperationalRequests.Location r){return s.location(id,r);}
 @GetMapping("/api/trips/{id}/locations/latest") public OperationalResponses.Location latest(@PathVariable Long id){return s.latestLocation(id);}
 @PostMapping("/api/boardings") @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasRole('STUDENT')") public OperationalResponses.Boarding board(@Valid @RequestBody OperationalRequests.Boarding r){return s.board(r);}
 @PostMapping("/api/incidents") @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasRole('DRIVER')") public OperationalResponses.Incident incident(@Valid @RequestBody OperationalRequests.Incident r){return s.incident(r);}
 @GetMapping("/api/notifications/me") public List<OperationalResponses.Notification> notifications(){return s.myNotifications();}
 @PostMapping("/api/ai-action-requests") @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasRole('COORDINATOR')") public OperationalResponses.AiAction propose(@Valid @RequestBody OperationalRequests.AiProposal r){return s.propose(r);}
 @PostMapping("/api/ai-action-requests/{id}/confirm") @PreAuthorize("hasRole('COORDINATOR')") public OperationalResponses.AiAction confirm(@PathVariable Long id){return s.confirm(id);}
}

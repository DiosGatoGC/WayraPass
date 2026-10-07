package com.wayrapass.controller;
import com.wayrapass.dto.request.DriverRequest; import com.wayrapass.dto.response.DriverResponse; import com.wayrapass.service.DriverService; import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/drivers") @RequiredArgsConstructor public class DriverController {
 private final DriverService service;
 @GetMapping @PreAuthorize("hasRole('COORDINATOR')") public List<DriverResponse> all(){return service.all();}
 @GetMapping("/{id}") @PreAuthorize("hasRole('COORDINATOR')") public DriverResponse one(@PathVariable Long id){return service.one(id);}
 @GetMapping("/me") @PreAuthorize("hasRole('DRIVER')") public DriverResponse me(){return service.me();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasRole('COORDINATOR')") public DriverResponse create(@Valid @RequestBody DriverRequest r){return service.create(r);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('COORDINATOR')") public DriverResponse update(@PathVariable Long id,@Valid @RequestBody DriverRequest r){return service.update(id,r);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasRole('COORDINATOR')") public void delete(@PathVariable Long id){service.delete(id);}
}

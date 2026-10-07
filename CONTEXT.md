# WayraPass - Project Context

## Project

WayraPass is a web platform for managing transportation services for higher-education students in Lima.

The backend is implemented using:

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Spring Security
- JWT
- Maven

The existing Spring Boot module is located in:

`wayrapass/`

The PostgreSQL schema is located in:

`database/wayrapass_db.sql`

## User roles

WayraPass has four roles:

### STUDENT
Can interact with routes, stops, enrollment, favorite routes, trips and boarding according to authorization rules.

### FAMILY
Represents a parent, guardian or authorized family member.

Family users may access information only for students with whom they have an authorized relationship through `student_family_links`.

### DRIVER
Represents a driver.

Drivers have additional profile information in the `drivers` table and may interact only with trips/resources assigned to them.

### COORDINATOR
Administrative and operational role.

Coordinators manage routes, stops, students, drivers, vehicles, trips and resource assignments/reassignments.

## Registration

Public registration is allowed only for:

- STUDENT
- FAMILY

DRIVER and COORDINATOR accounts must be provisioned or enabled administratively.

Registering a STUDENT requires atomically creating:

`users`
+
`students`

A FAMILY registration initially creates a user. Student-family association is performed later through the linking workflow.

## Authentication

Authentication uses email + password.

Passwords must use BCrypt.

Authentication must use Spring Security and JWT.

The system stores:

- `failed_login_attempts`
- `locked_until`

After 3 consecutive invalid login attempts, the account is temporarily locked for 5 minutes.

## Password recovery

Password recovery uses:

`password_reset_tokens`

Only a hash of a recovery token should be persisted.

Recovery tokens:

- expire;
- may be used only once;
- must not change user role or profile data.

## Main domain

The PostgreSQL database contains these tables:

1. users
2. password_reset_tokens
3. institutions
4. students
5. drivers
6. student_family_links
7. routes
8. stops
9. route_stops
10. route_schedules
11. student_route_enrollments
12. favorite_routes
13. vehicles
14. trips
15. boardings
16. incidents
17. trip_locations
18. notifications
19. ai_action_requests

Do not infer schema details from this document. Read `database/wayrapass_db.sql` for exact definitions.

## Important business rules

### Access by role
Users may access only functionality appropriate for their role.

### Trip location
During an active trip, location may be periodically recorded.

For the MVP, coordinates may come from a simulator.

The latest known location must remain available if no newer coordinates arrive.

### ETA
Estimated arrival information may use scheduled timing, trip progress and reported incidents.

### Boarding
A boarding is valid only when:

- the student belongs to the relevant trip;
- the scanned vehicle corresponds to the vehicle assigned to the trip;
- the student has not already boarded that same trip.

### Family linking
Student-family access requires an authorized link.

The linking flow uses a temporary code generated for the student and subject to expiration.

### Driver and vehicle assignment
A driver or vehicle may be assigned only when authorized and operationally available.

The system must prevent the same driver or vehicle from being assigned to overlapping trips.

### Incidents
Drivers may report incidents only for trips assigned to them.

An incident must identify at least its type and affected trip.

Incidents that prevent continuing the service should be identifiable as high-priority situations requiring operational intervention.

### Reassignment
Driver or vehicle reassignment may use only valid, authorized and available resources.

Once confirmed, the trip assignment must be updated and affected users should be notified when applicable.

### WayrIA
`ai_action_requests` stores critical actions proposed through WayrIA.

WayrIA may propose reassignment, but a critical modification must not be executed without explicit confirmation from a COORDINATOR.

Full LLM/Spring AI integration is outside the current backend implementation scope.

## Current development objective

The immediate objective is to integrate the Software Engineering Week 4, Week 5 and Week 6 laboratory concepts into the real WayraPass backend.

The backend should be ready for manual Postman verification after implementation.

Postman evidence is intentionally performed separately by the user.

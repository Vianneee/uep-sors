# SORS Backend - Spring Boot API

## Overview
This is the backend API for the Student Organization Showcase and Registration System (SORS). It handles application submissions with strict registration window validation (FR-2 & FR-3).

## Architecture

### Core Features
- **Registration Window Validation**: Prevents submissions outside designated periods
- **Duplicate Application Prevention**: Ensures one student per organization
- **Comprehensive Error Handling**: Clear error messages for validation failures
- **CORS Enabled**: Frontend can communicate from different origins

### Project Structure
```
backend/
├── src/main/java/com/uep/sors/
│   ├── config/
│   │   └── DataInitializer.java       # Initialize sample data
│   ├── controller/
│   │   └── ApplicationController.java # API endpoints
│   ├── dto/
│   │   ├── ApplicationSubmissionDTO.java
│   │   └── ApiResponse.java
│   ├── entity/
│   │   ├── Application.java
│   │   ├── ApplicationStatus.java
│   │   └── RegistrationPeriod.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── RegistrationClosedException.java
│   │   └── DuplicateApplicationException.java
│   ├── repository/
│   │   ├── ApplicationRepository.java
│   │   └── RegistrationPeriodRepository.java
│   ├── service/
│   │   └── ApplicationService.java
│   └── SorsBackendApplication.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## API Endpoints

### 1. Submit Application (FR-2 & FR-3)
**POST** `/api/applications`

Submits a new application with automatic registration window validation.

**Request Body:**
```json
{
  "organizationId": 1,
  "studentNumber": "123456",
  "fullName": "Juan Dela Cruz",
  "email": "juan@example.com",
  "contactNumber": "09123456789",
  "program": "Computer Science",
  "yearLevel": 2,
  "reasonForJoining": "I'm passionate about technology and want to contribute to the CS community...",
  "skills": "Python, Web Development, Problem Solving...",
  "previousExperience": "Member of ACM Club in high school",
  "facebookLink": "https://facebook.com/juan.delacruz"
}
```

**Validation Rules:**
- Student number must be exactly 6 digits
- Contact number must be exactly 11 digits
- Year level must be 1-4
- Reason and skills must be 10-2000 characters
- Email must be valid format

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Application submitted successfully",
  "data": {
    "id": 1,
    "organizationId": 1,
    "studentNumber": "123456",
    "fullName": "Juan Dela Cruz",
    "email": "juan@example.com",
    "status": "PENDING",
    "submittedAt": "2026-05-15T10:30:00"
  },
  "timestamp": "2026-05-15T10:30:00"
}
```

**Error Responses:**

*Registration Closed (400 Bad Request):*
```json
{
  "success": false,
  "message": "Registration is currently closed. Registration period: 2026-04-01T00:00 to 2026-04-15T23:59",
  "timestamp": "2026-05-15T10:30:00"
}
```

*Duplicate Application (409 Conflict):*
```json
{
  "success": false,
  "message": "Student already has an active application for this organization",
  "timestamp": "2026-05-15T10:30:00"
}
```

*Validation Error (400 Bad Request):*
```json
{
  "success": false,
  "message": "Validation failed: contactNumber: Contact number must be 11 digits, yearLevel: Year level must be between 1 and 4",
  "timestamp": "2026-05-15T10:30:00"
}
```

### 2. Get Application by ID
**GET** `/api/applications/{id}`

### 3. Get Applications by Organization (FR-6)
**GET** `/api/applications/organization/{organizationId}`

Admin endpoint to view all applicants for a specific organization.

### 4. Get Applications by Student
**GET** `/api/applications/student/{studentNumber}`

### 5. Delete Application (FR-7)
**DELETE** `/api/applications/{id}`

Admin endpoint to remove an applicant.

## Registration Window Logic

The backend checks the `RegistrationPeriod` table:

```java
boolean isOpen() {
    LocalDateTime now = LocalDateTime.now();
    return isActive && now.isAfter(startDate) && now.isBefore(endDate.plusSeconds(1));
}
```

**Example Data (initialized on startup):**
- Organization 1: May 1 - May 30, 2026 ✅ OPEN
- Organization 2: May 1 - May 30, 2026 ✅ OPEN  
- Organization 3: Apr 1 - Apr 15, 2026 ❌ CLOSED

## Build & Run

### Prerequisites
- Java 17 or later
- Maven 3.8+
- PostgreSQL 12+ (or H2 for development)

### Development (H2 Database)
```bash
# Build
cd backend
mvn clean package

# Run
mvn spring-boot:run
```

Server will start at: `http://localhost:8080`

H2 Console: `http://localhost:8080/h2-console`

### Production (PostgreSQL)

1. Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sors_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

2. Create database:
```sql
CREATE DATABASE sors_db;
```

3. Build and run:
```bash
mvn clean package
mvn spring-boot:run
```

## Database Schema

### registration_periods
- `id` (Long, PK)
- `organization_id` (Long, FK)
- `start_date` (LocalDateTime)
- `end_date` (LocalDateTime)
- `is_active` (Boolean)
- `description` (String)
- `created_at` (LocalDateTime, auto-set)
- `updated_at` (LocalDateTime, auto-set)

### applications
- `id` (Long, PK)
- `organization_id` (Long, FK)
- `student_number` (String, unique per org)
- `full_name` (String)
- `email` (String)
- `contact_number` (String)
- `program` (String)
- `year_level` (Integer)
- `reason_for_joining` (Text)
- `skills` (Text)
- `previous_experience` (Text)
- `facebook_link` (String)
- `status` (Enum: PENDING, APPROVED, REJECTED, WITHDRAWN)
- `submitted_at` (LocalDateTime, auto-set)
- `reviewed_at` (LocalDateTime)
- `review_notes` (String)

## Implementation Notes

1. **Registration Validation (FR-3)**: All submission attempts are validated against the current registration period stored in the database. The check happens at the service layer.

2. **Duplicate Prevention**: Uses composite query on `organization_id + student_number` to prevent multiple applications from the same student.

3. **CORS Configuration**: Allows requests from any origin for development. Restrict in production.

4. **Logging**: All operations are logged at INFO/DEBUG level to track application submissions.

5. **Transactions**: Application submission is wrapped in `@Transactional` for data consistency.

## Testing the API

### Using cURL:

```bash
# Submit an application (will fail if registration closed)
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" \
  -d '{
    "organizationId": 1,
    "studentNumber": "123456",
    "fullName": "Test Student",
    "email": "test@example.com",
    "contactNumber": "09123456789",
    "program": "Computer Science",
    "yearLevel": 2,
    "reasonForJoining": "I want to join because I love this organization.",
    "skills": "Leadership, communication, problem-solving",
    "facebookLink": "https://facebook.com/test"
  }'

# Get all applications for organization 1
curl http://localhost:8080/api/applications/organization/1

# Get applications for a student
curl http://localhost:8080/api/applications/student/123456

# Delete an application
curl -X DELETE http://localhost:8080/api/applications/1
```

## Next Steps

1. Frontend integration: Connect `register.html` to POST `/api/applications`
2. Database setup: Configure PostgreSQL for production
3. Authentication: Add JWT-based authentication
4. Admin dashboard: Build admin interface for reviewing applications

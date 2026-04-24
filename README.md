# Smart Campus API

## Overview
This project is a RESTful API for managing a smart campus system.  
It allows management of rooms, sensors, and sensor readings.

The API is built using Java and JAX-RS and runs on a local server.

---

## How to Run

1. Open the project in NetBeans  
2. Run the project  
3. The server will start at:  
http://localhost:8080/api/v1  

---

## API Endpoints

### Rooms

#### Get all rooms  
GET /api/v1/rooms  

#### Create room  
POST /api/v1/rooms  

Example:
curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"ENG-101\",\"name\":\"Engineering Room\",\"capacity\":30}"

#### Delete room  
DELETE /api/v1/rooms/{id}  

---

### Sensors

#### Get all sensors  
GET /api/v1/sensors  

#### Filter sensors  
GET /api/v1/sensors?type=Temperature  

#### Create sensor  
POST /api/v1/sensors  

Example:
curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-001\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":22.5,\"roomId\":\"ENG-101\"}"

---

### Sensor Readings

#### Get readings  
GET /api/v1/sensors/{id}/readings  

#### Add reading  
POST /api/v1/sensors/{id}/readings  

Example:
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings -H "Content-Type: application/json" -d "{\"id\":\"R1\",\"timestamp\":1713312000000,\"value\":24.8}"

---

## Error Handling

The API returns structured JSON errors.

### 409 Conflict  
Room cannot be deleted if it has sensors:  
{"error":"Room Conflict","message":"Cannot delete room: sensors still assigned"}  

### 422 Unprocessable Entity  
Invalid room reference:  
{"error":"Linked Resource Not Found","message":"Room does not exist"}  

### 403 Forbidden  
Sensor in maintenance:  
{"error":"Sensor Unavailable","message":"Sensor is under maintenance"}  

### 500 Internal Server Error  
{"error":"Internal Server Error","message":"An unexpected error occurred"}  

---

## Design Decisions

- RESTful structure using resources (rooms, sensors, readings)  
- Nested resources used for sensor readings  
- In-memory data storage using collections  
- Exception mappers used for error handling  

---

## Logging

A logging filter is used to log incoming requests and outgoing responses.

---

## Conclusion

This API demonstrates a complete RESTful backend with CRUD operations, relationships, validation, and error handling.

## Coursework Questions

### 1. Why use 422 instead of 404?

A 422 Unprocessable Entity is used when the request is valid but contains incorrect data.  
For example, when creating a sensor with a roomId that does not exist, the request format is correct, but the data is invalid.

A 404 Not Found is used when the requested resource itself does not exist.  
Therefore, 422 is more appropriate for validation errors.

---

### 2. Why should stack traces not be exposed?

Stack traces contain sensitive internal information such as class names, file paths, and system structure.  
Exposing this information can create security risks, as attackers can use it to understand the system and exploit vulnerabilities.

Therefore, APIs should return clean, structured error messages instead.

---

### 3. Why use filters for logging?

Filters allow logging to be handled in one central place rather than repeating logging code in every resource method.  
This improves code organisation, reduces duplication, and makes the system easier to maintain.

---

### 4. How does the API follow REST principles?

The API follows REST principles by:
- Using resource-based endpoints such as /rooms and /sensors  
- Using standard HTTP methods like GET, POST, and DELETE  
- Returning JSON responses  
- Using proper HTTP status codes for success and errors  

---

### 5. What are nested resources and why are they used?

Nested resources are used to represent relationships between entities.  
For example, sensor readings are accessed using /sensors/{id}/readings.

This structure clearly shows that readings belong to a specific sensor and improves API organisation.
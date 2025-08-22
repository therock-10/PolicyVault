    // src/main/java/org/godigit/policyvault/exception/GlobalExceptionHandler.java
    package org.godigit.policyvault.exception;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.bind.annotation.ExceptionHandler;

    import java.time.Instant;
    import java.util.HashMap;
    import java.util.Map;

    @ControllerAdvice
    public class GlobalExceptionHandler {

        private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", Instant.now());
            body.put("status", status.value());
            body.put("error", status.getReasonPhrase());
            body.put("message", message);
            return new ResponseEntity<>(body, status);
        }

        @ExceptionHandler(PolicyNotFoundException.class)
        public ResponseEntity<Object> handlePolicyNotFound(PolicyNotFoundException ex) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        @ExceptionHandler(UnauthorizedActionException.class)
        public ResponseEntity<Object> handleUnauthorized(UnauthorizedActionException ex) {
            return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
        }

        @ExceptionHandler(DuplicatePolicyException.class)
        public ResponseEntity<Object> handleDuplicatePolicy(DuplicatePolicyException ex) {
            return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
        }

        @ExceptionHandler(InvalidRequestException.class)
        public ResponseEntity<Object> handleInvalidRequest(InvalidRequestException ex) {
            return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        @ExceptionHandler(Exception.class) // fallback for unexpected errors
        public ResponseEntity<Object> handleGeneral(Exception ex) {
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "NO ENTRY Due To =  " + ex.getMessage());
        }
    }

// package com.example.EmployeeManagement.exception;


// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;

// @ControllerAdvice
// public class GlobalExceptionHandler {
//     @ExceptionHandler({ResourceNotFoundException.class})
//     public ResponseEntity<Object> handleStudentNotFoundException(ResourceNotFoundException exception) {
//         return ResponseEntity
//                 .status(HttpStatus.NOT_FOUND)
//                 .body(exception.getMessage());
//     }
//     @ExceptionHandler({BadRequestException.class})
//     public ResponseEntity<Object> handleStudentAlreadyExistsException(BadRequestException exception) {
//         return ResponseEntity
//                 .status(HttpStatus.BAD_REQUEST)
//                 .body(exception.getMessage());
//     }
//     @ExceptionHandler({RuntimeException.class})
//     public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
//         return ResponseEntity
//                 .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                 .body(exception.getMessage());
//     }
// }
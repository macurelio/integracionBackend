package com.challenge.users.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para capturar todos los errores de la aplicación y transformarlos
 * en mensajes de error JSON estándar con el formato {"mensaje": "..."}.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Formato estándar de respuesta de error según lo requerido por el desafío.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String mensaje;
    }

    /**
     * Maneja excepciones de lógica de negocio personalizadas (RuntimeExceptions).
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error("Error de validación de negocio: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de validación JSR-303 (por ejemplo, @NotEmpty).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        log.error("Error de validación de solicitud: {}", errorMessage);
        return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja cualquier otro error inesperado del sistema.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Error inesperado del sistema: ", e);
        return new ResponseEntity<>(new ErrorResponse("Ocurrió un error inesperado: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

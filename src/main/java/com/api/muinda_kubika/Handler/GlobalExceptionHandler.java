package com.api.muinda_kubika.Handler;

import com.api.muinda_kubika.Exceptions.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
            .getFieldErrors()
            .forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
            );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(
        IllegalArgumentException ex
    ) {
        return ResponseEntity.badRequest().body(
            Map.of("erro", ex.getMessage())
        );
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<String> handleEmailExists(
        EmailAlreadyExistException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handleRoleNotFound(RoleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ex.getMessage()
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceeded(
        MaxUploadSizeExceededException ex
    ) {
        return ResponseEntity.status(HttpStatusCode.valueOf(413)).body(
            Map.of("erro", "Arquivo excede o tamanho máximo permitido (25MB).")
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            "Erro interno: " + ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(
        InvalidCredentialsException ex
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<String> handleInvalidRefreshToken(
        InvalidRefreshTokenException ex
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ex.getMessage()
        );
    }

    @ExceptionHandler(InactiveRoleException.class)
    public ResponseEntity<Object> handleInactiveRoleException(
        InactiveRoleException ex
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            new ErrorResponse("ERRO_ROLE", ex.getMessage())
        );
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<Object> handleInactiveRoleException(
        InsufficientAuthenticationException ex
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            new ErrorResponse("ERRO_ROLE", ex.getMessage())
        );
    }

    @ExceptionHandler(RoleNameAlreayExistException.class)
    public ResponseEntity<String> handleRoleExists(
        RoleNameAlreayExistException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ex.getMessage()
        );
    }

    @ExceptionHandler(ProfileAlreadyExistsException.class)
    public ResponseEntity<String> handleProfileAlreadyExists(
        ProfileAlreadyExistsException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ProfileAlreadyActiveException.class)
    public ResponseEntity<String> handleProfileAlreadyActive(
        ProfileAlreadyActiveException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ProfileAlreadyRejectedException.class)
    public ResponseEntity<String> handleProfileAlreadyRejected(
        ProfileAlreadyRejectedException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ProfileApprovalNotFoundException.class)
    public ResponseEntity<String> handleProfileApprovalNotFound(
        ProfileApprovalNotFoundException ex
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ex.getMessage()
        );
    }

    @ExceptionHandler(InsufficientApprovalPermissionException.class)
    public ResponseEntity<String> handleInsufficientApprovalPermission(
        InsufficientApprovalPermissionException ex
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ex.getMessage()
        );
    }

    @ExceptionHandler(ProfileApprovalInvalidStateException.class)
    public ResponseEntity<String> handleProfileApprovalInvalidState(
        ProfileApprovalInvalidStateException ex
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ex.getMessage()
        );
    }

    @ExceptionHandler(ProfileApprovalAlreadyPendingException.class)
    public ResponseEntity<String> handleProfileApprovalAlreadyPendig(
        ProfileApprovalAlreadyPendingException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DocumentoAnalisePendenteException.class)
    public ResponseEntity<String> handleDocumentoAnalisePendente(
        DocumentoAnalisePendenteException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    static class ErrorResponse {

        public String code;
        public String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    // @ExceptionHandler(RuntimeException.class)
    // public ResponseEntity<String> handleRuntimeExceptions(RuntimeException ex) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    // }
}

package sskf.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sskf.exception.SSKFException;
import sskf.model.BaseStatusError;
import sskf.model.response.ImportedMasterStatusError;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> buildResponseEntity(Exception e) {
        log.error("Exception!", e);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(SSKFException.class)
    public ResponseEntity<?> checkSSKFExceptionExceptionHandler(SSKFException exception) {
        log.error("Exception!", exception);
        BaseStatusError baseStatusError = new BaseStatusError(exception.getMessageCode(), exception.getMessage());
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, baseStatusError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Exception!", exception);
        String errorCode = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(exception.getMessage());
        BaseStatusError baseStatusError = new BaseStatusError(HttpStatus.BAD_REQUEST.toString(), errorCode);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, baseStatusError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authenticationExceptionHandler(AuthenticationException exception) {
        log.error("Exception!", exception);
        BaseStatusError baseStatusError = new BaseStatusError(HttpStatus.UNAUTHORIZED.toString(), exception.getMessage());
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, baseStatusError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(BadCredentialsException exception) {
        log.error("Exception!", exception);
        BaseStatusError baseStatusError = new BaseStatusError(HttpStatus.UNAUTHORIZED.toString(), exception.getMessage());
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, baseStatusError);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> getRequestStatusExceptionHandler(RuntimeException e) {
        log.error("RuntimeException!", e);
        ImportedMasterStatusError importedMasterStatusError = new ImportedMasterStatusError();
        importedMasterStatusError.setMessage(e.getMessage());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("request_status", "400");

        return ResponseEntity.badRequest().headers(responseHeaders).body(importedMasterStatusError);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, BaseStatusError baseStatusError) {
        return new ResponseEntity<>(baseStatusError, status);
    }
}

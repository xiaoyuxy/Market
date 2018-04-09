package marketplace;

import marketplace.service.auth.AuthenticationException;
import marketplace.service.auth.AuthorizationException;
import marketplace.service.infra.RateLimitException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author xiaoyuliang
 */
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { AuthenticationException.class, AuthorizationException.class})
    protected ResponseEntity<Object> handleAuthenticationAndAuthorization(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Unauthorized ";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = { RateLimitException.class})
    protected ResponseEntity<Object> handleRateLimitException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Rate limit exceeded ";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}
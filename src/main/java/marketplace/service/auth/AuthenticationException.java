package marketplace.service.auth;

/**
 * @author xiaoyuliang
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}

package marketplace.service.auth;

/**
 * @author xiaoyuliang
 */
public interface AuthenticationService{
    /**
     * Check the Authentication of the owner
     * @param apiKey the owner's api-key
     * @return the userid
     */
    String authenticate(String apiKey);
}

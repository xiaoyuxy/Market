package marketplace.service.infra;

/**
 * A simple rate-limit service interface that only rate limit by api-key
 *
 * @author xiaoyuliang
 */
public interface RateLimitService {
    void accept(String apiKey);
}

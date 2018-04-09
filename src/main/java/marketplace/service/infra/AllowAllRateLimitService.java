package marketplace.service.infra;

/**
 * A rate limit service that doesn't do any rate limiting.
 *
 * @author xiaoyuliang
 */
public class AllowAllRateLimitService implements RateLimitService {

    @Override
    public boolean accept(String userId) {
        return true;
    }
}

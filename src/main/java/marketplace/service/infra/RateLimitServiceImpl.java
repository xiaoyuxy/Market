package marketplace.service.infra;

import com.google.common.util.concurrent.RateLimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * A naive yet expensive rate limit implementation.
 * TODO: replace this with a industry widely used rate limiter like
 * @author xiaoyuliang
 */
public class RateLimitServiceImpl implements RateLimitService {
    private final Map<String, RateLimiter> rateLimiterMap;

    public RateLimitServiceImpl() {
        this.rateLimiterMap = new HashMap<>();
    }

    @Override
    public boolean accept(String apiKey) {
        return false;
    }
}

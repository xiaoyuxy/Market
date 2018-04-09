package marketplace.service.infra;

import com.google.common.util.concurrent.RateLimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * A naive blocking rate limit implementation.
 *
 * @author xiaoyuliang
 */
public class RateLimitServiceImpl implements RateLimitService {
    private static final double REQ_PER_SEC = 2.0; // we allow 2 request per second for each api-key
    private final Map<String, RateLimiter> rateLimiterMap;

    public RateLimitServiceImpl() {
        this.rateLimiterMap = new HashMap<>();
    }

    @Override
    public void accept(String apiKey) {
        RateLimiter rateLimiter = rateLimiterMap.get(apiKey);
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(REQ_PER_SEC);
            rateLimiterMap.put(apiKey, rateLimiter);
        }
        if (!rateLimiter.tryAcquire()) {
            throw new RateLimitException();
        }
    }
}

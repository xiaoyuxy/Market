package marketplace.service.infra;

import org.springframework.stereotype.Service;

/**
 * A rate limit service that doesn't do any rate limiting.
 *
 * @author xiaoyuliang
 */
@Service("RateLimitService")
public class AllowAllRateLimitService implements RateLimitService {

    public AllowAllRateLimitService() {
    }

    @Override
    public void accept(String userId) {
    }
}

package marketplace.service.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * A fake service that sends msg to server console.
 *
 * @author xiaoyuliang
 */
@Service("UserNotificationService")
public class FakeNotificationServiceImpl implements UserNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(FakeNotificationServiceImpl.class);

    public FakeNotificationServiceImpl() {
    }

    @Override
    public void send(String userId, String msg) {
        logger.info("Sent user information for user {}, msg {}", userId, msg);
    }
}

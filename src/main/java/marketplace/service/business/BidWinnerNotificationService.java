package marketplace.service.business;

/**
 * @author xiaoyuliang
 */
public interface BidWinnerNotificationService {
    /**
     *  start the notification service
     */
    void run();

    /**
     *interface for other services to notify BidWinnerService an update to project happened.
     */
    void notifyUpdate();
}

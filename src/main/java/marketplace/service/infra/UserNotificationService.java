package marketplace.service.infra;

/**
 * @author xiaoyuliang
 */
public interface UserNotificationService {
    /**
     * Send the winner info to the project owener and the buyer
     * @param userId project owner id or buyer id
     * @param msg winner msg
     */
    void send(String userId, String msg);
}

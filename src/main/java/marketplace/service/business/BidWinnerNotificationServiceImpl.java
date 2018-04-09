package marketplace.service.business;

import marketplace.model.Bid;
import marketplace.model.Project;
import marketplace.service.infra.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author xiaoyuliang
 */
@Service("BidWinnerNotificationService")
public class BidWinnerNotificationServiceImpl implements BidWinnerNotificationService {

    private final ProjectService projectService;
    private final BidService bidService;
    private final UserNotificationService userNotificationService;
    private final Timer timer;
    private TimerTask currentTimertask;

    @Autowired
    public BidWinnerNotificationServiceImpl(ProjectService projectService, BidService bidService,
                                            UserNotificationService userNotificationService) {
        timer = new Timer();
        this.projectService = projectService;
        this.bidService = bidService;
        this.userNotificationService = userNotificationService;
    }

    @Override
    public void run() {
        refreshScheudler();
    }

    private void refreshScheudler() {
        if (currentTimertask != null) {
            currentTimertask.cancel();
        }
        Project nextProject = projectService.getNextExpiringProject();
        if (nextProject != null) {
            Date expireDate = nextProject.getExpireTime();
            currentTimertask = new TimerTask() {
                @Override
                public void run() {
                    Bid bid = bidService.getBidWinnerForProject(nextProject.getUUID());
                    if (bid == null) {
                        return;
                    }
                    userNotificationService.send(bid.getOwnerId(), "Your bid" + bid.toString() + "won the project" + nextProject.toString());
                    userNotificationService.send(nextProject.getOwnerId(), "Your project's bid winner is {}" + bid.toString());
                    refreshScheudler();
                }
            };
            timer.schedule(currentTimertask, expireDate);
        }
    }

    @Override
    public void notifyUpdate() {
        refreshScheudler();
    }
}

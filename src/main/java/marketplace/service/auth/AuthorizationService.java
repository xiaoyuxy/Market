package marketplace.service.auth;

import marketplace.model.Project;

import java.util.UUID;

/**
 * @author xiaoyuliang
 */
public interface AuthorizationService {
    /**
     * Check the authorization of the user
     * @param projectId the projectid that user want to use
     * @param userId the userid
     * @return return true if this user has the authorizaion, else  false;
     */
    boolean authorize(UUID projectId, String userId);
}

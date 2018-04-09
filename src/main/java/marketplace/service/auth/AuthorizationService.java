package marketplace.service.auth;

import marketplace.model.Project;

import java.util.UUID;

/**
 * @author xiaoyuliang
 */
public interface AuthorizationService {
    boolean authorize(UUID projectId, String userId);
}

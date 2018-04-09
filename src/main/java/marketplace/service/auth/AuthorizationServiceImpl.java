package marketplace.service.auth;

import marketplace.datastore.ProjectDataStore;
import marketplace.model.Project;

import java.util.UUID;

/**
 * @author xiaoyuliang
 */
public class AuthorizationServiceImpl implements AuthorizationService {

    private final ProjectDataStore projectDataStore;

    public AuthorizationServiceImpl(ProjectDataStore projectDataStore) {
        this.projectDataStore = projectDataStore;
    }

    @Override
    public boolean authorize(UUID projectId, String userId) {
        Project project = projectDataStore.getProjectById(projectId);
        if (project == null || !project.getOwnerId().equals(userId)) {
            return false;
        }
        return true;
    }
}

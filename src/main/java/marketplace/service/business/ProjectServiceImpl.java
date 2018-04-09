package marketplace.service.business;

import marketplace.datastore.ProjectDataStore;
import marketplace.model.Project;
import marketplace.service.auth.AuthorizationException;
import marketplace.service.auth.AuthorizationService;
import marketplace.service.auth.AuthorizationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Created by xiaoyuliang on 4/5/18.
 */
@Service("ProjectService")
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDataStore projectDataStore;
    private final AuthorizationService authorizationService;

    @Autowired
    public ProjectServiceImpl(ProjectDataStore projectDataStore) {
        this.projectDataStore = projectDataStore;
        this.authorizationService = new AuthorizationServiceImpl(projectDataStore);
    }

    @Override
    public UUID createProject(Project project, String ownerId) {
        if (project.getUUID() != null) {
            throw new IllegalArgumentException("Project to be created should not have id.");
        }
        if (project.getOwnerId()!= null && !project.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("owner id doesn't match");
        }
        UUID id = UUID.randomUUID();
        Project persistedProject = Project.createProject(project, id, ownerId);
        projectDataStore.saveProject(persistedProject);

        return id;
    }

    @Override
    public Project getProjectById(UUID id) {
        return projectDataStore.getProjectById(id);
    }

    @Override
    public List<Project> getProjectByUserId(String userId) {
        return projectDataStore.getProjectByUserId(userId);
    }
    @Override
    public Project updateProject(UUID projectId, Project project, String userId) {
        if(getProjectById(projectId) == null) {
            throw new IllegalArgumentException("Unknown project id.");
        }

        if (!authorizationService.authorize(projectId, userId)) {
            throw new AuthorizationException();
        }

        Project updatedProject = Project.createProject(project, projectId, userId);
        projectDataStore.saveProject(updatedProject);
        return updatedProject;
    }

    @Override
    public void deleteProject(UUID projectId, String userId) {
        if(getProjectById(projectId) == null) {
            throw new IllegalArgumentException("Unknown project id.");
        }

        if (!authorizationService.authorize(projectId, userId)) {
            throw new AuthorizationException();
        }

        projectDataStore.delete(projectId);

    }

    @Override
    public List<Project> getAllProjects() {
        return projectDataStore.getAllProjects();
    }

    // This is an expensive implementation
    @Override
    public Project getNextExpiringProject() {
        List<Project> allProjects = getAllProjects();

        if (allProjects.size() > 0) {
            allProjects.sort(Comparator.comparing(Project::getExpireTime));
            return allProjects.get(0);
        } else {
            return null;
        }
    }
}

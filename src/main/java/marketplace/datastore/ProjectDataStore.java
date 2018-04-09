package marketplace.datastore;

import marketplace.model.Project;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Data persistence interface for {@link Project}.
 *
 * @author Xiaoyu Liang
 */
public interface ProjectDataStore {

    /**
     * Persist a new project
     *
     * @param project the new project to be persisted
     */
    void saveProject(Project project);

    /**
     * Get a project from projectid
     *
     * @param projectId the project id to be fetched
     * @return the project for given project if it exist, otherwise null
     */
    Project getProjectById(UUID projectId);

    List<Project> getAllProjects();

    void delete(UUID projectId);

    List<Project> getProjectByUserId(String userId);
}

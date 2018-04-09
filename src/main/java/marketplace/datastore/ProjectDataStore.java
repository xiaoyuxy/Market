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
     * Get a project using projectid
     *
     * @param projectId the project id to be fetched
     * @return the project for given project if it exist, otherwise null
     */
    Project getProjectById(UUID projectId);

    /**
     * Get all the existing project
     * @return all the projects
     */
    List<Project> getAllProjects();

    /**
     * Delete a project using the projectid
     * @param projectId the project id to be deleted
     */
    void delete(UUID projectId);

    /**
     * Get projects using the userid
     * @param userId the user id to be fetched
     * @return all the projects belong to this user
     */
    List<Project> getProjectByUserId(String userId);
}

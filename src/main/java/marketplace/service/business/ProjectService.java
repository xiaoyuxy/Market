package marketplace.service.business;

import marketplace.model.Project;

import java.util.List;
import java.util.UUID;

/**
 * Created by xiaoyuliang on 4/5/18.
 */
public interface ProjectService {
    /**
     * Create a new Project
     * @param project The detail of the project
     * @param ownerId The owner of the project
     * @return projectid of the project that is created
     */
    UUID createProject(Project project, String ownerId);

    /**
     * Get a project using projectid
     *
     * @param id the project id to be fetched
     * @return the project for given project if it exist, otherwise null
     */
    Project getProjectById(UUID id);

    /**
     * Get projects using the userid
     * @param userId the user id to be fetched
     * @return all the projects belong to this user
     */
    List<Project> getProjectByUserId(String userId);

    /**
     * Update a project
     * @param id Project UUID
     * @param project project detail to be updated
     * @param userId Owner of the project
     * @return
     */
    Project updateProject(UUID id, Project project, String userId);

    /**
     * Delete a project
     * @param id the projecid to be deleted
     * @param userId the owner of the action
     */
    void deleteProject(UUID id, String userId);

    /**
     * Get all the existing project
     * @return all the projects
     */
    List<Project> getAllProjects();

    /**
     * Get the next Project that will expire
     * @return the next expired project
     */
    Project getNextExpiringProject();
}

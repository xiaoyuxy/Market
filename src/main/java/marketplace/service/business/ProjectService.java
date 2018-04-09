package marketplace.service.business;

import marketplace.model.Project;

import java.util.List;
import java.util.UUID;

/**
 * Created by xiaoyuliang on 4/5/18.
 */
public interface ProjectService {

    UUID createProject(Project project, String ownerId);

    Project getProjectById(UUID id);

    List<Project> getProjectByUserId(String userId);

    Project updateProject(UUID id, Project project, String userId);

    void deleteProject(UUID id, String userId);

    List<Project> getAllProjects();
}

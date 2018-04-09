package marketplace.datastore;

import marketplace.model.Project;

import javax.rmi.PortableRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An in-memory {@link ProjectDataStore} for demo and testing
 *
 * @author Xiaoyu Liang
 */
public class InMemoryProjectDataStore implements ProjectDataStore {

    private final Map<UUID, Project> idToProjectMap;
    private final Map<String, List<Project>> userToProjectMap;

    public InMemoryProjectDataStore() {
        idToProjectMap = new ConcurrentHashMap<>();
        userToProjectMap = new ConcurrentHashMap<>();
    }

    @Override
    public void saveProject(Project project) {
        String ownerId = project.getOwnerId();
        idToProjectMap.put(project.getUUID(), project);
        List<Project> projectsForUser = userToProjectMap.getOrDefault(ownerId, new ArrayList<>());
        projectsForUser.add(project);
        userToProjectMap.put(ownerId, projectsForUser);
    }

    @Override
    public List<Project> getProjectByUserId(String userId) {
        return userToProjectMap.getOrDefault(userId, null);
    }

    @Override
    public Project getProjectById(UUID projectId) {
        return idToProjectMap.getOrDefault(projectId, null);
    }

    @Override
    public List<Project> getAllProjects() {
        return new ArrayList<>(idToProjectMap.values());
    }

    @Override
    public void delete(UUID projectId) {
        Project project = idToProjectMap.get(projectId);
        List<Project> projectsForUser = userToProjectMap.get(project.getOwnerId());
        int indexToRemove = 0;
        for (Project p : projectsForUser) {
            if (p.getUUID().equals(projectId)) {
                break;
            }
            indexToRemove++;
        }
        projectsForUser.remove(indexToRemove);
        idToProjectMap.remove(projectId);
    }
}

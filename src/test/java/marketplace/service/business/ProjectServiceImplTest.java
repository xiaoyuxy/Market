package marketplace.service.business;

import marketplace.datastore.InMemoryProjectDataStore;
import marketplace.datastore.ProjectDataStore;
import marketplace.model.Project;
import marketplace.service.auth.AuthorizationException;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author xiaoyuliang
 */

public class ProjectServiceImplTest {
    private static final Date TEST_DATE1 = new Date(1600000000);
    private static final Date TEST_DATE2 = new Date(1600000000);

    private final ProjectDataStore projectDataStore;
    private final ProjectServiceImpl projectService;
    public ProjectServiceImplTest() {
        this.projectDataStore = new InMemoryProjectDataStore();
        this.projectService = new ProjectServiceImpl(projectDataStore);
    }
    @Test
    public void testCreateProjectAndGet() throws Exception {
        Project testProject1 = new Project(null, null,"test1", "", 100.0, TEST_DATE1);
        Project testProject2 = new Project(null, null,"test2", "", 101.0, TEST_DATE2);
        Project testProject3 = new Project(null, null,"test2", "", 103.0, TEST_DATE1);

        // create project
        UUID project1Id = projectService.createProject(testProject1, "user1");
        UUID project2Id = projectService.createProject(testProject2, "user2");
        UUID project3Id = projectService.createProject(testProject3, "user2");

        // get project by uuid
        Project project1 = projectService.getProjectById(project1Id);
        assertThat(project1.getName(), Matchers.equalTo("test1"));
        assertThat(project1.getUUID(), Matchers.equalTo(project1Id));
        assertThat(project1.getOwnerId(), Matchers.equalTo("user1"));
        assertThat(project1.getBudget(), Matchers.equalTo(100.0));

        Project project2 = projectService.getProjectById(project2Id);
        assertThat(project2.getName(), Matchers.equalTo("test2"));
        assertThat(project2.getUUID(), Matchers.equalTo(project2Id));
        assertThat(project2.getOwnerId(), Matchers.equalTo("user2"));
        assertThat(project2.getBudget(), Matchers.equalTo(101.0));

        Project project3 = projectService.getProjectById(project3Id);
        assertThat(project3.getName(), Matchers.equalTo("test2"));
        assertThat(project3.getUUID(), Matchers.equalTo(project3Id));
        assertThat(project3.getOwnerId(), Matchers.equalTo("user2"));
        assertThat(project3.getBudget(), Matchers.equalTo(103.0));

        // get project by user id
        List<Project> projectsForUser = projectService.getProjectByUserId("user2");
        assertThat(projectsForUser, Matchers.containsInAnyOrder(project2, project3));

        // get all project
        List<Project> projects = projectService.getAllProjects();
        assertThat(projects, Matchers.containsInAnyOrder(project1, project2, project3));
    }

    @Test
    public void testUpdateProject() throws Exception {
        Project testProject = new Project(null, null,"test", "", 100.0, TEST_DATE1);
        UUID projectId = projectService.createProject(testProject, "user");
        // update th testProject with updated, change the budget
        Project updated = new Project(null, null,"test", "", 121.0, TEST_DATE1);

        Project updatedProject1 = projectService.updateProject(projectId, updated, "user");
        assertThat(updatedProject1.getBudget(), Matchers.equalTo(121.0));

    }

    @Test
    public void testDeleteProject() throws Exception {
        Project testProject = new Project(null, null,"test", "", 100.0, TEST_DATE1);
        UUID projectId = projectService.createProject(testProject, "user");
        projectService.deleteProject(projectId, "user");
        Project project = projectService.getProjectById(projectId);
        assertThat(project, Matchers.nullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotCreateProjectForOthers() throws Exception{
        Project testProject = new Project(null, "user1","test", "", 100.0, TEST_DATE1);
        projectService.createProject(testProject, "user2");
    }
    @Test(expected = AuthorizationException.class)
    public void testUserCannotUpdateProjectForOthers() {
        Project testProject = new Project(null, null,"test", "", 100.0, TEST_DATE1);
        UUID projectid = projectService.createProject(testProject, "user1");
        projectService.updateProject(projectid, testProject, "user2");
    }

    @Test(expected = AuthorizationException.class)
    public void testUserCannotDeleteProjectForOthers() {
        Project testProject = new Project(null, null,"test", "", 100.0, TEST_DATE1);
        UUID projectid = projectService.createProject(testProject, "user1");
        projectService.deleteProject(projectid, "user2");
    }
}

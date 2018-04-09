package marketplace.datastore;

import marketplace.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

/**
 * @author xiaoyuliang
 */
//@Repository("DbProjectDataStore")
public class DbProjectDataStore implements ProjectDataStore {

    private static final Logger log = LoggerFactory.getLogger(DbProjectDataStore.class);

    JdbcTemplate jdbcTemplate;

    public DbProjectDataStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveProject(Project project) {
    }

    @Override
    public Project getProjectById(UUID projectId) {
        return null;
    }

    @Override
    public List<Project> getAllProjects() {
        return null;
    }

    @Override
    public void delete(UUID projectId) {

    }

    @Override
    public List<Project> getProjectByUserId(String userId) {
        return null;
    }
}

package marketplace.controller;

import marketplace.model.Project;
import marketplace.service.*;
import marketplace.service.auth.AuthenticationService;
import marketplace.service.auth.FakeAuthenticationService;
import marketplace.service.business.BidWinnerNotificationService;
import marketplace.service.business.ProjectService;
import marketplace.service.infra.RateLimitService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

/**
 * @author Xiaoyu Liang
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;
    private final RateLimitService rateLimitService;
    private final BidWinnerNotificationService bidWinnerNotificationService;
    private final AuthenticationService authenticationService;

    @Autowired
    public ProjectController(JdbcTemplate jdbcTemplate, ProjectService projectService,
                             RateLimitService rateLimitService,
                             BidWinnerNotificationService bidWinnerNotificationService) {
        this.projectService = projectService;
        this.rateLimitService = rateLimitService;
        this.bidWinnerNotificationService = bidWinnerNotificationService;
        bidWinnerNotificationService.run();
        this.authenticationService = new FakeAuthenticationService();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<?> createProject(@RequestBody Project project, @RequestHeader("user-api-key") String apiKey, UriComponentsBuilder ucBuilder) {
        logger.info("Create project: {}, owner: {}", project, apiKey);
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);

        if (!InputValidationService.validateProjectForCreate(project)) {
            logger.warn("Unable to create this project. Project not valid {}", project);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        UUID projectId = projectService.createProject(project, userId);
        bidWinnerNotificationService.notifyUpdate();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/project/{projectId}").buildAndExpand(projectId.toString()).toUri());

        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<Project>> getAllProjects(@RequestHeader("user-api-key") String apiKey) {
        logger.info("Get all the projects");
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);
        List<Project> projects = projectService.getAllProjects();

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @RequestMapping(value = "/{projectid}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Project> getProjectById(@PathVariable("projectid") String projectid,
                                                  @RequestHeader("user-api-key") String apiKey) {
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);

        if (!InputValidationService.validateUUID(projectid)) {
            logger.error("Invalid input uuid {}", projectid);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        logger.info("Get the project with project id {}", projectid);
        Project project = projectService.getProjectById(UUID.fromString(projectid));

        if (project == null) {
            logger.error("Unable to find project with id {}", projectid);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(project, HttpStatus.OK);
    }
    @RequestMapping(value = "/user/{ownerId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<Project>> getProjectByUser(@PathVariable("ownerId") String ownerId,
                                                          @RequestHeader("user-api-key") String apiKey) {
        logger.info("Get the project with user id {}", ownerId);
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);

        List<Project> project = projectService.getProjectByUserId(ownerId);

        if (project == null) {
            logger.error("Unable to find project with user id {}", ownerId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(project, HttpStatus.OK);
    }


    @RequestMapping(value = "/{projectid}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Project> updateProjectById(@PathVariable("projectid") String projectid, @RequestBody Project project,
                                                     @RequestHeader("user-api-key") String apiKey) {
        logger.info("Update the project info : {}", projectid);
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);

        Project updatedProject = projectService.updateProject(UUID.fromString(projectid), project, userId);
        bidWinnerNotificationService.notifyUpdate();
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @RequestMapping(value = "/{projectid}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> deleteProject(@PathVariable("projectid") String projectid,
                                           @RequestHeader("user-api-key") String apiKey) {
        logger.info("Delete the project: {}", projectid);
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);

        projectService.deleteProject(UUID.fromString(projectid), userId);
        bidWinnerNotificationService.notifyUpdate();
        return new ResponseEntity<Project>(HttpStatus.OK);
    }
}

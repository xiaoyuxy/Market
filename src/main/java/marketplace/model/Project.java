package marketplace.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Project entity.
 * This class is immutable.
 *
 * @author Xiaoyu Liang
 */
public class Project {
    /**
     * id and ownerId should not be JsonProperty because they should not be set/updated by json body
     */
    @JsonIgnore
    private final UUID id;
    @JsonIgnore
    private final String ownerId;

    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("budget")
    private double budget;
    //TODO: consider time zone
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date expireTime;

    public Project() {
        id = null;
        ownerId = null;
    }

    public Project(UUID id, String ownerId, String name, String description, double budget, Date expireTime) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.expireTime = expireTime;
    }

    @JsonGetter("uuid")
    public UUID getUUID() {
        return id;
    }

    @JsonGetter("owner")
    public String getOwnerId() {
        return ownerId;
    }

    @JsonIgnore
    public boolean isValidProject() {
        return name != null && budget >= 0 && expireTime != null;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", budget=" + budget +
                ", expireTime=" + expireTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Double.compare(project.budget, budget) == 0 &&
                Objects.equals(ownerId, project.ownerId) &&
                Objects.equals(name, project.name) &&
                Objects.equals(description, project.description) &&
                Objects.equals(expireTime, project.expireTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, name, description, budget, expireTime);
    }

    static public Project createProject(Project project, UUID id, String ownerId) {
        return new Project(id, ownerId, project.name, project.description, project.budget,
                project.expireTime);
    }

    @JsonIgnore
    public double getBudget() {
        return budget;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }
}

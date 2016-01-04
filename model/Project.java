package the_projects.model;

/**
 * The class counting the amount of project cubes of a course on a room.
 */
public class Project {

    /**
     * The course linked to the project.
     */
    private Course course;

    /**
     * The amount of project cubes.
     */
    private int projectAmount;

    /**
     * Constructor with parameters of Project.
     *
     * @param course the course linked to the project.
     */
    public Project(Course course) {
        this.course = course;
        this.projectAmount = 0;
    }

    /**
     * Gets the course linked to the project.
     *
     * @return the course linked to the project.
     */
    public Course getCourse() {
        return this.course;
    }

    /**
     * Gets the amount of project cubes on a room.
     *
     * @return the amount of project cubes on the room.
     */
    public int getProjectAmount() {
        return this.projectAmount;
    }

    /**
     * Sets the amount of project cubes on a room.
     *
     * @param amount the new amount of project cubes.
     */
    public void setProjectAmount(int amount) {
        this.projectAmount = amount;
    }

}

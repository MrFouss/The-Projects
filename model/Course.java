package the_projects.model;

/**
 * Class representing a course.
 */
public class Course {

    /**
     * Name of the course.
     */
    private String name;

    /**
     * Remaining amount of project cubes available in the game.
     */
    private int remainingProjectAmount;

    /**
     * Completion status.
     */
    private Boolean completed;

    /**
     * Eradication status.
     */
    private Boolean eradicated;


    /**
     * Constructor of Course, with a name parameter.
     *
     * @param name the name of the course.
     */
    public Course(String name) {
        this.name = name;
        this.remainingProjectAmount = 24;
        this.completed = false;
        this.eradicated = false;
    }

    /**
     * Gets the name of the course.
     *
     * @return the name of the course.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if whether or not the course is completed.
     *
     * @return the status of completion of the course.
     */
    public Boolean isCompleted() {
        return this.completed;
    }

    /**
     * Sets the course as completed.
     */
    public void setCompleted() {
        this.completed = true;
    }

    /**
     * Checks if whether or not the course is eradicated.
     *
     * @return the status of eradication of the course.
     */
    public Boolean isEradicated() {
        return this.eradicated;
    }

    /**
     * Sets the course as eradicated.
     */
    public void setEradicated() {
        this.eradicated = true;
    }

    /**
     * Gets the remaining amount of project cubes available for the game.
     *
     * @return the remaining amount of project cubes available.
     */
    public int getRemainingProjectAmount() {
        return this.remainingProjectAmount;
    }

    /**
     * Sets the remaining amount of project cubes available for the game.
     *
     * @param amount the new remaining amount of project cubes available.
     */
    public void setRemainingProjectAmount(int amount) {
        this.remainingProjectAmount = amount;
    }

}
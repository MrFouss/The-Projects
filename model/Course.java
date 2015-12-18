package the_projects.model;

/**
 * 
 */
public class Course {

    /**
     * Default constructor
     */
    public Course() {
    }

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Boolean completed = false;

    /**
     * 
     */
    private Boolean eradicated = false;

    /**
     * 
     */
    private int remainingProjectAmount = 24;


    /**
     * @param name
     */
    public void Course(String name) {
        // TODO implement here
    }

    /**
     * @return
     */
    public String getName() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public Boolean isCompleted() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void setCompleted() {
        // TODO implement here
    }

    /**
     * @return
     */
    public Boolean isEradicated() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void setEradicated() {
        // TODO implement here
    }

    /**
     * 
     */
    public class Project {

        /**
         * Default constructor
         */
        public Project() {
        }

        /**
         * 
         */
        private int projectAmount = 0;

        /**
         * 
         */
        private Course course;

        /**
         * @param course
         */
        public void Project(Course course) {
            // TODO implement here
        }

        /**
         * @return
         */
        public int getProjectAmount() {
            // TODO implement here
            return 0;
        }

        /**
         * @param amount 
         * @return
         */
        public void setProjectAmount(int amount) {
            // TODO implement here
        }

    }

}
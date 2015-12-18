package the_projects.model;

import java.util.*;

/**
 * 
 */
public class Room {

    /**
     * Default constructor
     */
    public Room() {
    }

    /**
     * 
     */
    private Boolean labRoom = false;


    /**
     *
     */
    private Course.Project[] projectTab;


    /**
     * 
     */
    private Set<Room> neighbours;

    /**
     * 
     */
    private Course course;

    /**
     * @param course 
     * @param courses
     */
    public void Room(Course course, List<Course> courses) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Course getCourse() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Boolean toggleLabRoom() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Boolean isLabRoom() {
        // TODO implement here
        return null;
    }

    /**
     * @param course
     * @return
     */
    public Course.Project getProject(Course course) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Room> getReachableRooms() {
        // TODO implement here
        return null;
    }

}
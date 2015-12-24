package the_projects.model;

import java.util.*;

/**
 * Class representing each room of the game.
 */
public class Room {

    /**
     * The name of the room.
     */
    // TODO add to the class diagram
    private String name;

    /**
     * A boolean attribute used to say if the room is a lab room or not.
     */
    private Boolean labRoom;

    /**
     * The course affected to the room.
     */
    private Course course;

    /**
     * The list of projects on this room.
     */
    private Project[] projectTab;


    /**
     * The list of adjacent rooms to this one.
     */
    private LinkedList<Room> neighbours;

    /**
     * Constructor with parameters.
     *
     * @param course The course affected to the room.
     * @param coursesInGame The list of courses used to create the Project objects.
     */
    // TODO update the class diagram
    public Room(String name, Course course, Course[] coursesInGame) {
        this.name = name;
        this.labRoom = false;
        this.course = course;
        this.neighbours = new LinkedList<>();

        // initialization of projectTab
        this.projectTab = new Project[4];
        for(int i = 0; i < 4; ++i) {
            projectTab[i] = new Project(coursesInGame[i]);
        }
    }

    /**
     * Gets the name of the room.
     *
     * @return the name of the room.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if whether or not the room is a lab room.
     *
     * @return the labRoom state.
     */
    public Boolean isLabRoom() {
        return this.labRoom;
    }

    /**
     * Toggles the labRoom state.
     *
     * @return the new labRoom state.
     */
    public Boolean toggleLabRoom() {
        this.labRoom = !this.labRoom;
        return this.labRoom;
    }

    /**
     * Gets the course affected to the room.
     *
     * @return the course affected to the room.
     */
    public Course getCourse() {
        return this.course;
    }

    /**
     * Gets the Project linked to course.
     *
     * @param course the course linked to a project.
     * @return the project linked to the course.
     */
    public Project getProject(Course course) {
        for(int i = 0; i < 4; ++i) {
            if(this.projectTab[i].getCourse() == course) {
                return this.projectTab[i];
            }
        }
        return null;
    }

    /**
     * Gets the list of rooms adjacent to this one.
     *
     * @return the list of rooms adjacent to this one.
     */
    // TODO update the class diagram
    public LinkedList<Room> getNeighbours() {
        return this.neighbours;
    }

}
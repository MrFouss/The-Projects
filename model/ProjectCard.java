package the_projects.model;

/**
 * 
 */
// TODO add javadoc
public class ProjectCard implements Card {

    /**
     *
     */
    // TODO add javadoc
    private Room room;

    /**
     * Constructor with a Room parameter
     *
     * @param room a room to link to the card.
     */
    public ProjectCard(Room room) {
        this.room = room;
    }

    /**
     * @return the card's room
     */
    // TODO add javadoc
    public Room getRoom() {
        return this.room;
    }

}
package the_projects.model;

/**
 * 
 */
// TODO add javadoc
public class RoomCard implements PlayerCard {

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
    public RoomCard(Room room) {
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
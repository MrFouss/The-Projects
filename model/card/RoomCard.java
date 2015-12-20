package the_projects.model.card;

import the_projects.model.Room;

/**
 * Implementation of the PlayerCard interface.
 */
public class RoomCard implements PlayerCard {

    /**
     * Room object linked to the card.
     */
    private Room room;

    /**
     * Constructor with a Room parameter.
     *
     * @param room a room to link to the card.
     */
    public RoomCard(Room room) {
        this.room = room;
    }

    /**
     * Gets the Room object linked to the card.
     *
     * @return the card's room.
     */
    public Room getRoom() {
        return this.room;
    }

}
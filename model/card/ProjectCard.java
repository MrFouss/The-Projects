package the_projects.model.card;

import the_projects.model.Room;

/**
 * Implementation of the Card interface, the only kind of card composing the Project Deck.
 */
public class ProjectCard implements Card {

    /**
     * Room object linked to the card.
     */
    private Room room;

    /**
     * Constructor with a Room parameter.
     *
     * @param room a room to link to the card.
     */
    public ProjectCard(Room room) {
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
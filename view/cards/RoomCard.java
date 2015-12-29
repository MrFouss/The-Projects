package the_projects.view.cards;


import javafx.scene.layout.Pane;
import the_projects.view.Room;

/**
 * Room version of the cards
 */
public class RoomCard extends Card {
    /**
     * Simplified Constructor of Card
     * @param pane parent Pane
     * @param room corresponding room
     */
    public RoomCard(Pane pane, Room room, Owner owner) {
        super(pane, room.getUV(), room.getName(), room.getColor(), owner);
    }
}

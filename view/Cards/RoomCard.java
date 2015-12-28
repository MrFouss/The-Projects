package the_projects.view.Cards;


import javafx.scene.layout.Pane;
import the_projects.view.Cards.Card;
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
    public RoomCard(Pane pane, Room room) {
        super(pane, room.getUV(), room.getName(), room.getColor());
    }
}

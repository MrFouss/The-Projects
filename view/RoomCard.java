package the_projects.view;


import javafx.scene.layout.Pane;

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

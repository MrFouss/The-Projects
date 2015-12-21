package the_projects.view;


import javafx.scene.layout.Pane;

/**
 * Room version of the cards
 */
public class RoomCard extends Card {
    /**
     * Simplified Constructor of Card
     * @param scene parent scene
     * @param room corresponding room
     */
    public RoomCard(Pane scene, Room room) {
        super(scene, room.getUV(), room.getName(), room.getColor());
    }
}

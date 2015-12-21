package the_projects.view;

import javafx.scene.Scene;

/**
 * Room version of the cards
 */
public class RoomCard extends Card {
    /**
     * Simplified Constructor of Card
     * @param scene parent scene
     * @param room corresponding room
     * @param relativeX x position
     * @param relativeY y position
     */
    public RoomCard(Scene scene, Room room, double relativeX, double relativeY) {
        super(scene, room.getUV(), room.getName(), room.getColor(), relativeX, relativeY);
    }
}

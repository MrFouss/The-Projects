package the_projects.view;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * The representation of a player, a shape appearing in the room he is.
 */
public class Pawn {
    private Shape shape;
    private Room actualRoom;
    private Player player;

    /**
     * The main constructor
     * @param player the player associated the pawn
     */
    public Pawn(Player player) {
        this.player = player;
        Circle circle = new Circle(10,10,5, Player.roleToColor(player.getRole()));
        Polygon triangle= new Polygon(circle.getCenterX(), circle.getCenterY() - circle.getRadius(), circle.getCenterX() + circle.getRadius(), circle.getCenterY() + circle.getRadius()*3, circle.getCenterX() - circle.getRadius(), circle.getCenterY() + circle.getRadius()*3);
        shape = Shape.union(circle, triangle);
        shape.setFill(Player.roleToColor(player.getRole()));
        Board.setHoverStrokeChange(shape, Player.roleToColor(player.getRole()));
        shape.setStrokeWidth(2);
    }

    /**
     * Access to the Shape object
     * @return the Shape of the Pawn
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Method to call when changing the position of a Pawn
     * @param destination the room to which the Pawn is moved
     * @return the Room in which the pawn was
     */
    public Point2D setRoom(Room destination) {
        Point2D ancientCoords = new Point2D(0,0);
        if (actualRoom != null)
            ancientCoords = actualRoom.delPawn(this);
        actualRoom = destination;
        return  ancientCoords;
    }

    /**
     * Getter of the Player associated to the Pawn
     * @return the Player associated to the Pawn
     */
    public Player getPlayer() {
        return player;
    }
}

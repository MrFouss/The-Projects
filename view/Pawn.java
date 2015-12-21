package the_projects.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * The representation of a player, a shape appearing in the room he is.
 */
public class Pawn {
    private Shape shape;
    private Room actualRoom;

    /**
     * The main constructor
     * @param color the color of the pawn
     */
    public Pawn(Color color) {
        Circle circle = new Circle(10,10,5, color);
        Polygon triangle= new Polygon(circle.getCenterX(), circle.getCenterY() - circle.getRadius(), circle.getCenterX() + circle.getRadius(), circle.getCenterY() + circle.getRadius()*3, circle.getCenterX() - circle.getRadius(), circle.getCenterY() + circle.getRadius()*3);
        shape = Shape.union(circle, triangle);
        shape.setFill(color);
        Board.setHoverStrokeChange(shape, color);
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
    public Room setRoom(Room destination) {
        if (actualRoom != null)
            actualRoom.delPawn(this);
        Room ancientRoom = actualRoom;
        actualRoom = destination;
        return  ancientRoom;
    }
}

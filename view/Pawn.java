package the_projects.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import the_projects.model.Role;

/**
 * The representation of a player, a shape appearing in the room he is.
 */
public class Pawn {
    private Shape shape;
    private Room actualRoom;

    /**
     * The main constructor
     * @param role the role associated the pawn
     */
    public Pawn(Role role) {
        Circle circle = new Circle(10,10,5, roleToColor(role));
        Polygon triangle= new Polygon(circle.getCenterX(), circle.getCenterY() - circle.getRadius(), circle.getCenterX() + circle.getRadius(), circle.getCenterY() + circle.getRadius()*3, circle.getCenterX() - circle.getRadius(), circle.getCenterY() + circle.getRadius()*3);
        shape = Shape.union(circle, triangle);
        shape.setFill(roleToColor(role));
        Board.setHoverStrokeChange(shape, roleToColor(role));
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

    /**
     * Method associating a role to a color
     * @param role the role to associate
     * @return the color associated
     */
    private Color roleToColor(Role role) {
        switch (role) {
            case TRELLO_ADEPT :
                return Color.PURPLE;
            case GROUP_LEADER :
                return Color.RED;
            case MENTOR :
                return Color.ORANGE;
            case INSTALLER :
                return Color.WHITE;
            case COFFEE_MAKER :
                return Color.SADDLEBROWN;
            case DAOUID :
                return Color.YELLOW;
            case HACKER :
                return Color.GREEN;
            default :
                return Color.GRAY;
        }
    }
}

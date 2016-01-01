package the_projects.view;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * The representation of a player, a shape appearing in the room he is.
 */
public class Pawn implements Clickable{
    private final Shape shape;
    private Room actualRoom;
    private final Player player;
    private final Color color;
    private ChangeListener hoverListener;

    /**
     * The main constructor
     * @param player the player associated the pawn
     */
    public Pawn(Player player) {
        this.player = player;
        color = player.getRole().roleToColor();
        Circle circle = new Circle(10,10,5, color);
        Polygon triangle= new Polygon(circle.getCenterX(), circle.getCenterY() - circle.getRadius(), circle.getCenterX() + circle.getRadius(), circle.getCenterY() + circle.getRadius()*3, circle.getCenterX() - circle.getRadius(), circle.getCenterY() + circle.getRadius()*3);
        shape = Shape.union(circle, triangle);
        shape.setFill(player.getRole().roleToColor());
        shape.setStroke(color.deriveColor(0, 1, .5, 1));
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

    @Override
    public void setClickable(boolean clickable, View view) {
        if (clickable) {
            hoverListener = Board.setHoverStrokeChange(shape, color);
            shape.setOnMouseClicked(event -> view.firePawnCLicked(player.getRole()));
        }else {
            if (hoverListener != null) {
                //noinspection unchecked
                shape.hoverProperty().removeListener(hoverListener);

            }
            shape.setStroke(color.deriveColor(0, 1, .5, 1));
            shape.setStrokeWidth(2);
            shape.setOnMouseClicked(null);
        }
    }

    @Override
    public void resetFill() {
        shape.setFill(player.getRole().roleToColor());
    }
}

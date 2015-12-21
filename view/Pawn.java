package the_projects.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * Created by th0re on 19/12/15.
 * TODO complete
 */
public class Pawn {
    private Shape shape;
    private Room actualRoom;

    public Pawn(Color color) {
        Circle circle = new Circle(10,10,5, color);
        Polygon triangle= new Polygon(circle.getCenterX(), circle.getCenterY() - circle.getRadius(), circle.getCenterX() + circle.getRadius(), circle.getCenterY() + circle.getRadius()*3, circle.getCenterX() - circle.getRadius(), circle.getCenterY() + circle.getRadius()*3);
        shape = Shape.union(circle, triangle);
        shape.setFill(color);
    }

    public Shape getShape() {
        return shape;
    }

    public Room setRoom(Room room) {
        if (actualRoom != null)
            actualRoom.delPawn(this);
        Room ancientRoom = actualRoom;
        actualRoom = room;
        return  ancientRoom;
    }
}

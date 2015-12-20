package the_projects.view;


import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * TODO complete
 */
public class Corridor extends Path {

    public Corridor (Room room1, Room room2, Coord... coords) {
        getElements().add(new MoveTo(room1.getPos().getX(), room1.getPos().getY()));
        if (coords != null) {

        }
        getElements().add(new LineTo(room2.getPos().getX(), room2.getPos().getY()));
        setFill(Color.BLACK);
        setStrokeWidth(2);
    }

}
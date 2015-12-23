package the_projects.view;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;

/**
 * TODO complete
 */
public class Corridor extends Path {

    public Corridor (Room room1, Room room2, ArrayList<Point2D> coords) {
        getElements().add(new MoveTo(room1.getPos().getX(), room1.getPos().getY()));
        if (!coords.isEmpty()) {
            coords.stream().forEach((coord) -> getElements().add(new LineTo(coord.getX(), coord.getY())));
        }
        getElements().add(new LineTo(room2.getPos().getX(), room2.getPos().getY()));
        setStroke(Color.GRAY);
        setStrokeWidth(4);
    }

    public Corridor (Room room1, Room room2) {
        this(room1, room2, new ArrayList<>());
    }

}
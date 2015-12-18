package the_projects.view;


import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * TODO implement
 */
public class Building extends Shape {

    private Color color;

    public Building(Color c) {
        color = c;
    }


    @Override
    public com.sun.javafx.geom.Shape impl_configShape() {
        return null;
    }
}
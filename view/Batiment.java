package the_projects.view;


import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * TODO implement
 */
public class Batiment extends Shape {

    private Color color;

    public Batiment(Color c) {
        color = c;
    }


    @Override
    public com.sun.javafx.geom.Shape impl_configShape() {
        return null;
    }
}
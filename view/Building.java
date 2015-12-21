package the_projects.view;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;
import java.util.Arrays;


/**
 * The main building, defining their shape and color
 */
public class Building {

    private Shape shape;
    private Color color;

    /**
     * Constructor of a Building
     * @param color the color of the building
     * @param shapes the different shapes composing the building
     */
    public Building(Color color, Shape ... shapes) {

        if (shapes.length > 1) {
            shape = Arrays.asList(shapes).stream().reduce(Shape::union).get();
        }else{
            shape = shapes[0];
        }
        this.color = color;
        shape.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))));
        shape.setStroke(color.deriveColor(0,1,.5,1));
        shape.setStrokeWidth(3);
    }

    /**
     * Getter for the color of the building
     * @return the Color of the Building
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter of the Shape of the building
     * @return the Shape of the Building
     */
    public Shape getShape() {
        return shape;
    }
}

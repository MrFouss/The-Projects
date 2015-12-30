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
class Building {

    private final Shape shape;
    private final Color color;
    private final String UV;

    /**
     * Constructor of a Building
     * @param color the color of the building
     * @param UV the name of the corresponding UV
     * @param shapes the different shapes composing the building
     */
    public Building(Color color, String UV, Shape ... shapes) {
        this.UV = UV;
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

    /**
     * Getter of the UV of the building
     * @return the UV of the Building
     */
    public String getUV() {
        return UV;
    }
}

package the_projects.view;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;
import java.util.Arrays;


/**
 * TODO complete
 */
public class Building {

    private Shape shape;
    private Color color;

    public Building(Color color, Shape ... s) {

        if (s.length > 1) {
            shape = Arrays.asList(s).stream().reduce(Shape::union).get();
        }else{
            shape = s[0];
        }
        setColor(color);
        shape.setStrokeWidth(3);
    }


    public void setColor(Color c) {
        color = c;
        shape.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))));
        shape.setStroke(color.deriveColor(0,1,.5,1));
    }

    public Color getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }
}

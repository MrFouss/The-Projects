package the_projects.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import java.util.Arrays;


/**
 * TODO complete
 */
public class Building {

    private Shape shape;
    private Color color;

    public Building(Color color, Shape ... s) {
        this.color = color;
        if (s.length > 1) {
            shape = Arrays.asList(s).stream().reduce(Shape::union).get();
        }else{
            shape = s[0];
        }
        shape.setFill(color);
        shape.setStroke(color.darker().darker());
        shape.setStrokeWidth(3);
        shape.hoverProperty().addListener((e) -> {
            if(shape.isHover()) {
                shape.setStroke(Color.BLACK);
            }else{
                shape.setStroke(color.darker().darker());
            }
        });
    }


    public void setColor(Color c) {
        color = c;
        shape.setFill(color);
        shape.setStroke(color.darker().darker());
    }

    public Shape getShape() {
        return shape;
    }
}

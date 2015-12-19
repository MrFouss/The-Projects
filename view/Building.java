package the_projects.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * TODO complete
 */
public class Building {

    private Shape shape;
    private Color color;

    public Building(Circle circle, Rectangle rectangle, Color color) {
        this.color = color;
        shape = Shape.union(circle, rectangle);
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

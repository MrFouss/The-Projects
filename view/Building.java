package the_projects.view;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * TODO complete
 */
public class Building {
    private Rectangle rectangle;
    private Circle circle;
    private Color color;

    public Building(Circle circle, Rectangle rectangle, Color color) {
        this.circle = circle;
        this.rectangle = rectangle;
        this.color = color;
    }

    public Rectangle getRectangle() {
        rectangle.setFill(color);
        return rectangle;
    }

    public Circle getCircle() {
        circle.setFill(color);
        return circle;
    }
}

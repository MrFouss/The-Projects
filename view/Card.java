package the_projects.view;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * TODO implement
 */
public class Card extends StackPane {

    public Card(Scene scene, String title, String text, Color color, double relativeX, double relativeY) {

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(color);

        rectangle.setWidth(scene.getWidth() * 9 / 100.);
        rectangle.setHeight(scene.getHeight() * 22 / 100.);

        setLayoutX(scene.getWidth() * relativeX);
        setLayoutY(scene.getHeight() * relativeY);
        getChildren().add(rectangle);

        Label label = new Label(title);
        label.setFont(new Font(15));
        label.setTextFill(color.deriveColor(0, 1, 8, 1));
        label.setMouseTransparent(true);
        getChildren().add(label);
        setAlignment(label, Pos.TOP_CENTER);

        label = new Label(text);
        label.setFont(new Font(15));
        label.setTextFill(color.deriveColor(0, 1, 8, 1));
        label.setMouseTransparent(true);
        getChildren().add(label);

        Board.setHoverStrokeChange(rectangle, color);
    }

}
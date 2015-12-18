package the_projects.view;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * TODO complete
 */
public class Deck extends StackPane {
    Color color;
    String text;
    Rectangle rectangle;

    public Deck(Scene scene,String t, Color c, double relativeX, double relativeY) {
        color = c;
        rectangle = new Rectangle();
        rectangle.setFill(color);
        text = t;

        rectangle.widthProperty().bind(scene.widthProperty().multiply(9/100.));
        rectangle.heightProperty().bind(scene.heightProperty().multiply(22/100.));

        layoutXProperty().bind(scene.widthProperty().multiply(relativeX));
        layoutYProperty().bind(scene.heightProperty().multiply(relativeY));
        getChildren().add(rectangle);

        Text label = new Text(text);
        label.setFont(new Font(15));
        label.setFill(Color.GREENYELLOW);

        getChildren().add(label);
    }


}
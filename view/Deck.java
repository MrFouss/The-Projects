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

    public Deck(Scene scene,String t, Color c, double relativeX, double relativeY, boolean horizontal) {
        color = c;
        rectangle = new Rectangle();
        rectangle.setFill(color);
        text = t;


        rectangle.setWidth(scene.getWidth()*(horizontal ? 22*9/16 : 9)/100.);
        rectangle.setHeight(scene.getHeight()*(horizontal ? 9*16/9 : 22)/100.);

        setLayoutX(scene.getWidth()*relativeX);
        setLayoutY(scene.getHeight()*relativeY);
        getChildren().add(rectangle);

        Text label = new Text(text);
        label.setFont(new Font(15));
        label.setFill(Color.GREENYELLOW);

        getChildren().add(label);
    }


}
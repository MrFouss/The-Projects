package the_projects.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * TODO make fancier
 */
public class Card extends StackPane {

    public Card(Pane pane, String title, String text, Color color) {

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(color);

        rectangle.setWidth(pane.getWidth() * 9 / 100.);
        rectangle.setHeight(pane.getHeight() * 22 / 100.);

        setLayoutX(0);
        setLayoutY(0);
        getChildren().add(rectangle);

        Label label = new Label(title);
        label.setFont(new Font(15));
        label.setTextFill(color.deriveColor(0, 1, 8, 1));
        label.setMouseTransparent(true);
        getChildren().add(label);
        setAlignment(label, Pos.TOP_CENTER);
        setMargin(label, new Insets(pane.getHeight()/100,0,0,0));

        label = new Label(text);
        label.setFont(new Font(15));
        label.setTextFill(color.deriveColor(0, 1, 8, 1));
        label.setMouseTransparent(true);
        getChildren().add(label);

        Board.setHoverStrokeChange(rectangle, color);
    }

}
package the_projects.view.Cards;


import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import the_projects.view.Board;
import the_projects.view.Clickable;
import the_projects.view.View;

/**
 * TODO make fancier
 */
public class Card extends StackPane implements Clickable {
    private Rectangle rectangle;
    private Color color;
    private String title;
    private String text;
    private ChangeListener hoverListener;
    private Owner owner;

    public Card(Pane pane, String title, String text, Color color, Owner owner) {
        this.color = color;
        this.title = title;
        this.text = text;

        rectangle = new Rectangle();
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

        this.owner = owner;
    }

    @Override
    public void setClickable(boolean clickable, View view) {
        if (clickable) {
            hoverListener = Board.setHoverStrokeChange(rectangle, color);
            rectangle.setOnMouseClicked(event -> view.fireCardClicked(title, text));
        }else {
            rectangle.hoverProperty().removeListener(hoverListener);
            rectangle.setStrokeWidth(0);
            rectangle.setOnMouseClicked(null);
        }
    }

    @Override
    public void resetFill() {
        rectangle.setFill(color);
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
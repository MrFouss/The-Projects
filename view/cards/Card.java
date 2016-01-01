package the_projects.view.cards;


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
 * The representation of a card for the View
 */
public class Card extends StackPane implements Clickable {
    private final Rectangle rectangle;
    private final Color color;
    private final String title;
    private final String text;
    private ChangeListener hoverListener;
    private Owner owner;
    final Label textLabel;

    /**
     * Constructor of a card
     * @param pane the Pane on which the card appear
     * @param title the title of the card, written on top
     * @param text the text of the card, written in the middle
     * @param color the main color of the card
     * @param owner the owner of the card
     */
    Card(Pane pane, String title, String text, Color color, Owner owner) {
        this.color = color;
        this.title = title;
        this.text = text;

        rectangle = new Rectangle();
        rectangle.setFill(color);

        rectangle.setWidth(pane.getWidth() * 9 / 100.);
        rectangle.setHeight(pane.getHeight() * 22 / 100.);

        getChildren().add(rectangle);

        Label label = new Label(title);
        label.setFont(new Font(15));
        label.setTextFill(color.deriveColor(0, 1, 8, 1));
        label.setMouseTransparent(true);
        getChildren().add(label);
        setAlignment(label, Pos.TOP_CENTER);
        setMargin(label, new Insets(pane.getHeight()/100,0,0,0));

        textLabel = new Label(text);
        textLabel.setFont(new Font(15));
        textLabel.setTextFill(color.deriveColor(0, 1, 8, 1));
        textLabel.setMouseTransparent(true);
        getChildren().add(textLabel);

        this.owner = owner;
    }

    @Override
    public void setClickable(boolean clickable, View view) {
        if (clickable) {
            hoverListener = Board.setHoverStrokeChange(rectangle, color);
            rectangle.setOnMouseClicked(event -> view.fireCardClicked(title, text));
        }else {
            if (hoverListener != null)
                //noinspection unchecked
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

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
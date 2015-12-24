package the_projects.view;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * The place where card are drawn/discarded
 */
public class Deck extends StackPane {
    private boolean horizontal;

    /**
     * complete constructor
     * @param scene the scene in which the Deck appear
     * @param text the text on the Deck
     * @param color the color of the Deck
     * @param relativeX the relative x position of the Deck
     * @param relativeY the relative y position of the Deck
     * @param horizontal the horizontality of the Deck
     */
    public Deck (Scene scene,String text, Color color, double relativeX, double relativeY, boolean horizontal) {

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))));

        this.horizontal = horizontal;
        rectangle.setWidth(scene.getWidth()*(horizontal ? 22*9/16 : 9)/100.);
        rectangle.setHeight(scene.getHeight()*(horizontal ? 9*16/9 : 22)/100.);

        setLayoutX(scene.getWidth()*relativeX);
        setLayoutY(scene.getHeight()*relativeY);
        getChildren().add(rectangle);

        Label label = new Label(text);
        label.setFont(new Font(15));
        label.setTextFill(color.deriveColor(0,1,8,1));
        label.setMouseTransparent(true);


        getChildren().add(label);

        Board.setHoverStrokeChange(rectangle, color);
    }

    /**
     * Default Constructor for vertical Decks
     * @param scene the scene in which the Deck appear
     * @param text the text on the Deck
     * @param color the color of the Deck
     * @param relativeX the relative x position of the Deck
     * @param relativeY the relative y position of the Deck
     */
    public Deck (Scene scene,String text, Color color, double relativeX, double relativeY) {
        this(scene, text, color, relativeX, relativeY, false);
    }

    /**
     * Getter of horizontality
     * @return true if horizontal
     */
    public boolean isHorizontal() {
        return horizontal;
    }
}
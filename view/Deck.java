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
 * TODO complete
 */
public class Deck extends StackPane {

    public Deck (Scene scene,String text, Color color, double relativeX, double relativeY, boolean horizontal) {

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))));


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

}
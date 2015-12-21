package the_projects.view;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * TODO complete
 */
public class Deck extends StackPane {

    private Rectangle rectangle;

    public Deck(Scene scene,String text, Color color, double relativeX, double relativeY, boolean horizontal) {

        rectangle = new Rectangle();
        rectangle.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))));


        rectangle.setWidth(scene.getWidth()*(horizontal ? 22*9/16 : 9)/100.);
        rectangle.setHeight(scene.getHeight()*(horizontal ? 9*16/9 : 22)/100.);

        setLayoutX(scene.getWidth()*relativeX);
        setLayoutY(scene.getHeight()*relativeY);
        getChildren().add(rectangle);

        Text label = new Text(text);
        label.setFont(new Font(15));
        label.setFill(color.deriveColor(0,1,8,1));
        label.setMouseTransparent(true);


        getChildren().add(label);

        Board.setHoverStrokeChange(rectangle, color);
    }

}
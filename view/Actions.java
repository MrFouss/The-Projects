package the_projects.view;


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

/**
 * TODO complete
 */
public class Actions extends BorderPane {

    public Actions(Scene parent, Player... players) {
        Color color = Color.DARKSLATEBLUE;
        setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))), new CornerRadii(20), new Insets(0))));



        setLayoutX(parent.getWidth()*50/100.);
        setLayoutY(parent.getHeight()*65/100.);
        setPrefSize(parent.getWidth()*48/100.,parent.getHeight()*32/100.);

        VBox buttons = new VBox();
        setLeft(buttons);

        //TODO add buttons

        VBox center = new VBox();
        setRight(center);

        for (Player player : players) {
            //TODO add player infos
        }



    }
}
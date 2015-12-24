package the_projects.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Text;


/**
 * TODO implement
 */
public class PropagationGauge extends Pane {

    private int lvl;

    public PropagationGauge(Scene scene, double posX, double posY) {
        Color color = Color.FORESTGREEN;
        setLayoutX(scene.getWidth()*posX);
        setLayoutY(scene.getHeight()*posY);
        setPrefSize(scene.getWidth()*19/100., scene.getHeight()*10/100.);
        setMaxSize(scene.getWidth()*19/100., scene.getHeight()*10/100.);

        Text title = new Text("Jauge d'urgence");
        title.setFill(color.brighter().brighter());
        title.setLayoutX(getPrefWidth()/3);
        title.setLayoutY(15);
        getChildren().add(title);

        setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1,1)), new Stop(1, color.deriveColor(0,1,.5,1))), new CornerRadii(15), new Insets(0))));

        double line1 = 40, line2 = line1 + 30, r = 14;
        Shape backGauge = new Circle(50, line2, r, color);
        for (int i = 1; i < 7; i++) {
            backGauge = Shape.union(backGauge, new Circle(50 + i*35, i%2 == 0 ? line2 : line1, r, color));
            Path path = new Path(new MoveTo(50 + (i-1)*35,i%2 == 0 ? line1 : line2), new LineTo(50 + i*35, i%2 == 0 ? line2 : line1));
            path.setStrokeWidth(7);
            backGauge = Shape.union(backGauge, path);
        }


        backGauge.setFill(Color.DARKRED);
        getChildren().addAll(backGauge);
    }





}
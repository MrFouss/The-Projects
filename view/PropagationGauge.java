package the_projects.view;

import javafx.animation.PathTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;


/**
 * Representation of the Propagation Gauge
 */
public class PropagationGauge extends Pane implements Gauge {

    private int lvl;
    private Circle actual;

    /**
     * The Constructor of the PropagationGauge
     * @param scene the scene in which the PropagationGauge is placed
     * @param posX the relative to the scene x position
     * @param posY the relative to the scene y position
     */
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
        backGauge.setFill(new LinearGradient(0,0.5,1,.5,true,CycleMethod.NO_CYCLE,new Stop(0,Color.GREEN), new Stop(.5,Color.ORANGE), new Stop(1,Color.RED)));
        getChildren().add(backGauge);
        for (int i = 0; i < 7; i++) {
            String nbCards;
            if (i < 3) {
                nbCards = "2";
            } else if (i < 5) {
                nbCards = "3";
            } else {
                nbCards = "4";
            }
            Text text = new Text(50 + i * 35 - 4, (i % 2 == 0 ? line2 : line1) + 4, nbCards);
            getChildren().add(text);
        }

        lvl = 1;
        actual = new Circle(50, line2, r, Color.TRANSPARENT);
        actual.setStroke(Color.BLUE);
        actual.setStrokeWidth(4);
        getChildren().add(actual);
    }

    /**
     * Method to increase of 1 unit the Propagation Gauge
     */
    public void increase() {
        if (lvl < 7) {
            double line1 = 40, line2 = line1 + 30, r = 14;
            Path path = new Path(new MoveTo(50 + (lvl-1)*35,lvl%2 == 0 ? line1 : line2), new LineTo(50 + lvl*35, lvl%2 == 0 ? line2 : line1));
            (new PathTransition(Duration.millis(1000),path,actual)).play();

            lvl++;
        }

    }


}
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
 * Representation of the Outbreaks Gauge
 */
public class OutbreaksGauge extends Pane implements Gauge {

    private int lvl;
    private final Circle actual;

    /**
     * The Constructor of the OutbreaksGauge
     * @param scene the scene in which the OutbreaksGauge is placed
     * @param posX the relative to the scene x position
     * @param posY the relative to the scene y position
     */
    public OutbreaksGauge(Scene scene, double posX, double posY) {
        Color color = Color.FORESTGREEN;
        setLayoutX(scene.getWidth()*posX);
        setLayoutY(scene.getHeight()*posY);
        setPrefSize(scene.getWidth()*9/100., scene.getHeight()*39.5/100.);
        setMaxSize(scene.getWidth()*9/100., scene.getHeight()*39.5/100.);

        setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1,1)), new Stop(1, color.deriveColor(0,1,.5,1))), new CornerRadii(15), new Insets(0))));


        Text title = new Text("Jauge de Burn-Out");
        title.setFill(color.brighter().brighter());
        title.setLayoutX(getPrefWidth()/10);
        title.setLayoutY(15);
        getChildren().add(title);

        double column1 = 42, column2 = column1 + 60, r = 17;
        Shape backGauge = new Circle(column1, 50, r, color);
        for (int i = 1; i < 9; i++) {
            backGauge = Shape.union(backGauge, new Circle(i%2 == 0 ? column1 : column2, 50 + i*35, r, color));
            Path path = new Path(new MoveTo(i%2 == 0 ? column2 : column1, 50 + (i-1)*35), new LineTo(i%2 == 0 ? column1 : column2, 50 + i*35));
            path.setStrokeWidth(7);
            backGauge = Shape.union(backGauge, path);
        }
        backGauge.setFill(new LinearGradient(0.5,0,.5,1,true,CycleMethod.NO_CYCLE,new Stop(0,Color.GREEN), new Stop(.5,Color.ORANGE), new Stop(1,Color.RED)));
        getChildren().add(backGauge);
        for (int i = 0; i < 8; i++) {
            Text text = new Text( (i % 2 == 0 ? column1 : column2) - 4, 50 + i * 35 + 4, Integer.toString(i));
            getChildren().add(text);
        }
        Text text = new Text( column1 - 4, 50 + 8 * 35 + 4, "P");
        getChildren().add(text);

        lvl = 1;
        actual = new Circle(column1, 50, r, Color.TRANSPARENT);
        actual.setStroke(Color.BLUE);
        actual.setStrokeWidth(4);
        getChildren().add(actual);
    }

    /**
     * Method to increase of 1 unit the Outbreaks Gauge
     */
    public void increase(View view) {
        if (lvl < 9) {
            double column1 = 42, column2 = column1 + 60;
            Path path = new Path(new MoveTo(lvl%2 == 0 ? column2 : column1, 50 + (lvl-1)*35), new LineTo(lvl%2 == 0 ? column1 : column2, 50 + lvl*35));
            PathTransition pathTransition = new PathTransition(Duration.millis(1000),path,actual);
            pathTransition.setOnFinished(event -> view.fireOutbreakFinished());
            pathTransition.play();
            lvl++;
        }

    }
}

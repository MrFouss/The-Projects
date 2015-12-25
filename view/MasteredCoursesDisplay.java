package the_projects.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Representation of the toMastered states
 */
public class MasteredCoursesDisplay extends Pane {
    private Timeline[] toMastered;
    private Timeline[] toEradicated;

    /**
     * Complete constructor
     * @param scene the scene where the Pane appear
     * @param buildings the list of the different courses
     * @param posX the relative x position on the scene
     * @param posY the relative y position on the scene
     */
    public MasteredCoursesDisplay(Scene scene, Building[] buildings, double posX, double posY) {
        Color color = Color.INDIGO;
        setLayoutX(scene.getWidth()*posX);
        setLayoutY(scene.getHeight()*posY);
        setPrefSize(scene.getWidth()*14.5/100., scene.getHeight()*13.5/100.);
        setMaxSize(scene.getWidth()*14.5/100., scene.getHeight()*13.5/100.);

        Text title = new Text("État des UVs :\n Maîtrisée(V),\nÉradiquée(X).");
        title.setFill(color.brighter().brighter());
        title.setLayoutX(getPrefWidth()/3.2);
        title.setLayoutY(15);
        getChildren().add(title);

        toMastered = new Timeline[4];
        toEradicated = new Timeline[4];

        setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(1, color.deriveColor(0,1,1,1)), new Stop(0, color.deriveColor(0,1,.5,1))), new CornerRadii(15), new Insets(0))));

        for (int i = 0; i < 4; ++i) {
            Color courseColor = buildings[i].getColor();
            Circle circle = new Circle(45 + i*50, 90, 17, new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, courseColor.deriveColor(0,1,1,1)), new Stop(1, courseColor.deriveColor(0,1,.5,1))));
            Text text = new Text(30 + i*50, 70, buildings[i].getUV());
            text.setFill(buildings[i].getColor());

            MoveTo moveTo1 = new MoveTo(45 + i*50, 82);
            LineTo lineTo1 = new LineTo(45 - 8 + i*50, 99);
            MoveTo moveTo2 = new MoveTo(45 + i*50, 82);
            LineTo lineTo2 = new LineTo(45 + 8 + i*50, 99);
            Path path = new Path(moveTo1, lineTo1, moveTo2, lineTo2);
            path.setStrokeWidth(2);

            getChildren().addAll(circle, text, path);
            toMastered[i] = new Timeline(new KeyFrame(Duration.millis(1000),
                    new KeyValue(moveTo1.xProperty(), 45 - 8 + i*50),
                    new KeyValue(moveTo2.xProperty(), 45 + 8 + i*50),
                    new KeyValue(lineTo1.xProperty(), 45 + i*50),
                    new KeyValue(lineTo2.xProperty(), 45 + i*50)
                    ));
            toEradicated[i] = new Timeline(new KeyFrame(Duration.millis(1000),
                    new KeyValue(moveTo1.xProperty(), 45 - 8 + i*50),
                    new KeyValue(moveTo2.xProperty(), 45 + 8 + i*50),
                    new KeyValue(lineTo1.xProperty(), 45 + 8 + i*50),
                    new KeyValue(lineTo2.xProperty(), 45 - 8 + i*50)
            ));
        }
    }

    /**
     * Method to make a course mastered
     * @param projectIndex the index of the course
     */
    public void toMastered(int projectIndex) {
        if (projectIndex < 4 && projectIndex >= 0) {
            toMastered[projectIndex].play();
        }
    }

    /**
     * Method to make a course eradicated
     * @param projectIndex the index of the course
     */
    public void toEradicated(int projectIndex) {
        if (projectIndex < 4 && projectIndex >= 0) {
            toEradicated[projectIndex].play();
        }
    }
}

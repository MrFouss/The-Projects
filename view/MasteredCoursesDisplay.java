package the_projects.view;

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
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

/**
 * Representation of the courses states
 */
public class MasteredCoursesDisplay extends Pane {
    private Path[] courses;

    public MasteredCoursesDisplay(Scene scene, Building[] buildings, double posX, double posY) {
        Color color = Color.INDIGO;
        setLayoutX(scene.getWidth()*posX);
        setLayoutY(scene.getHeight()*posY);
        setPrefSize(scene.getWidth()*14.5/100., scene.getHeight()*13.5/100.);
        setMaxSize(scene.getWidth()*14.5/100., scene.getHeight()*13.5/100.);

        Text title = new Text("État des UVs :\n Maîtrisée(V),\n Éradiquée(X).");
        title.setFill(color.brighter().brighter());
        title.setLayoutX(getPrefWidth()/4);
        title.setLayoutY(15);
        getChildren().add(title);

        setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1,1)), new Stop(1, color.deriveColor(0,1,.5,1))), new CornerRadii(15), new Insets(0))));

    }
}

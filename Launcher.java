package the_projects;

import javafx.application.Application;
import javafx.stage.Stage;
import the_projects.view.View;

/**
 * Launcher of the game
 */
public class Launcher extends Application{
    private View stage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = new View();
        stage.show();
    }
}

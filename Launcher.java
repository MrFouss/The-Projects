package the_projects;

import javafx.application.Application;
import javafx.stage.Stage;
import the_projects.controller.Controller;
import the_projects.view.View;

/**
 * Launcher of the game
 */
public class Launcher extends Application{
    public static void main(String[] args) {
        //starts the Application
        launch(args);
        //kills all threads when the main window is closed
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) {
        View stage = new View();
        Controller controller = new Controller(stage);
        controller.start();
        stage.show();
    }
}

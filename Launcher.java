package the_projects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import the_projects.model.Role;
import the_projects.view.Player;
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
        stage.setMinHeight(450);
        stage.setMinWidth(800);
        stage.setMaximized(true);
        stage.show();


        //TODO remove next line
        stage.setBoard("AG44", "MI41", "SI20", "LO43", new Player("TOTO", Role.TRELLO_ADEPT), new Player("TATA", Role.DAOUID), new Player("TUTU", Role.GROUP_LEADER), new Player("TITI", Role.HACKER));


    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }
}

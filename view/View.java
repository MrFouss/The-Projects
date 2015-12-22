package the_projects.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import the_projects.model.Role;

/**
 *  TODO complete
 */
public class View extends Application {
    private MainMenu mainMenu;
    private Board board;

    @Override
    public void start(Stage primaryStage) {
        board = new Board(new Group(), "AG44", "MI41", "SI20", "LO43", Role.COFFEE_MAKER, Role.DAOUID, Role.GROUP_LEADER, Role.HACKER);
        primaryStage.setScene(board);

        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(800);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
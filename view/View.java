package the_projects.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

/**
 *  TODO complete
 */
public class View extends Application {
    private MainMenu mainMenu;
    private Board board;

    @Override
    public void start(Stage primaryStage) {
        board = new Board(new Group(), 5);
        primaryStage.setScene(board);

        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(800);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
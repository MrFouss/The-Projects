package the_projects.view;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * TODO implement
 */
public class MainMenu extends Scene {


    public MainMenu(Group root) {
        super(root);
        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);
        setFill(Color.GRAY);
        gridPane.add(new TextField("Joueur 1"),0,0);
    }
}
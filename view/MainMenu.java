package the_projects.view;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import the_projects.model.Role;

import java.util.LinkedList;

/**
 * TODO implement
 */
public class MainMenu extends Scene {


    private int difficulty;
    private LinkedList<Role> roles;
    private LinkedList<String> playerNames;
    private LinkedList<String> UVNames;

    public MainMenu(Group root, View view) {
        super(root);
        difficulty = 0;
        roles = new LinkedList<>();
        playerNames = new LinkedList<>();
        UVNames = new LinkedList<>();

        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);
        setFill(Color.GRAY);
        gridPane.add(new TextField("Joueur 1"),0,0);
        MyButton confirmButton = new MyButton("Confirmer");
        confirmButton.setOnMouseClicked(event -> view.fireSettingValidationButtonCLicked());
        gridPane.add(confirmButton, 0, 1);

    }

    public int getDifficulty() {
        return difficulty;
    }

    public LinkedList<Role> getRoles() {
        return roles;
    }

    public LinkedList<String> getPlayerNames() {
        return playerNames;
    }

    public LinkedList<String> getUVNames() {
        return UVNames;
    }
}
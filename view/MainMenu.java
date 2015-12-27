package the_projects.view;


import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import the_projects.model.Role;

import java.util.LinkedList;

/**
 * TODO complete
 */
public class MainMenu extends Scene {


    private ScrollBar difficulty;
    private TextField[] courses;
    private TextField[] playersFields;
    private ChoiceBox<String>[] rolesBoxes;

    public MainMenu(Group root, View view) {
        super(root);


        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);
        setFill(Color.GRAY);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.add(new Text("Nom des UVs :"), 0, 0);
        courses = new TextField[4];
        for (int i = 0; i < 4; ++i) {
            courses[i] = new TextField("UV" + Integer.toString(i));
            gridPane.add(courses[i], i+1, 0);
        }

        //Roles
        String[] roleNames = new String[Role.values().length];
        int index = 0;
        for (Role role : Role.values())
            roleNames[index++] = role.roleToName();

        playersFields = new TextField[4];
        rolesBoxes = new ChoiceBox[4];

        gridPane.add(new Text("Nom du Joueur 1 :"), 1, 1);
        playersFields[0] = new TextField("Joueur 1");
        gridPane.add(playersFields[0],2,1);
        gridPane.add(new Text("Rôle du Joueur 1 :"), 3, 1);
        rolesBoxes[0] = new ChoiceBox<>();
        rolesBoxes[0].getItems().addAll(roleNames);
        rolesBoxes[0].setValue(roleNames[1]);
        gridPane.add(rolesBoxes[0],4,1);

        gridPane.add(new Text("Nom du Joueur 2 :"), 1, 2);
        playersFields[1] = new TextField("Joueur 2");
        gridPane.add(playersFields[1],2,2);
        gridPane.add(new Text("Rôle du Joueur 1 :"), 3, 2);
        rolesBoxes[1] = new ChoiceBox<>();
        rolesBoxes[1].getItems().addAll(roleNames);
        rolesBoxes[1].setValue(roleNames[2]);
        gridPane.add(rolesBoxes[1],4,2);

        gridPane.add(new Text("Nom du Joueur 3 :"), 1, 3);
        playersFields[2] = new TextField("Joueur 3");
        gridPane.add(playersFields[2],2,3);
        gridPane.add(new Text("Rôle du Joueur 3 :"), 3, 3);
        rolesBoxes[2] = new ChoiceBox<>();
        rolesBoxes[2].getItems().addAll(roleNames);
        rolesBoxes[2].setValue(roleNames[3]);
        gridPane.add(rolesBoxes[2],4,3);

        gridPane.add(new Text("Nom du Joueur 4 :"), 1, 4);
        playersFields[3] = new TextField("Joueur 4");
        gridPane.add(playersFields[3],2,4);
        gridPane.add(new Text("Rôle du Joueur 4 :"), 3, 4);
        rolesBoxes[3] = new ChoiceBox<>();
        rolesBoxes[3].getItems().addAll(roleNames);
        rolesBoxes[3].setValue(roleNames[4]);
        gridPane.add(rolesBoxes[3],4,4);

        CheckBox checkBox2 = new CheckBox("Activer ?");
        checkBox2.selectedProperty().addListener(observable -> {
            playersFields[3].setDisable(!checkBox2.isSelected());
            rolesBoxes[3].setDisable(!checkBox2.isSelected());
        });
        checkBox2.setSelected(true);
        gridPane.add(checkBox2,0,4);

        CheckBox checkBox1 = new CheckBox("Activer ?");
        checkBox1.selectedProperty().addListener(observable -> {
            playersFields[2].setDisable(!checkBox1.isSelected());
            rolesBoxes[2].setDisable(!checkBox1.isSelected());
            checkBox2.setSelected(false);
            checkBox2.setDisable(!checkBox1.isSelected());
        });
        checkBox1.setSelected(true);
        gridPane.add(checkBox1,0,3);

        gridPane.add(new Text("Difficulté :"), 0, 5);
        difficulty = new ScrollBar();
        difficulty.setMax(0);
        difficulty.setMax(3);
        difficulty.setValue(0);
        gridPane.add(difficulty,1,5);
        Text difficultyText = new Text(Integer.toString((int)(difficulty.getValue()+.5)));
        difficulty.valueProperty().addListener(observable -> difficultyText.setText(Integer.toString((int)(difficulty.getValue()+.5))));
        gridPane.add(difficultyText,2,5);



        MyButton confirmButton = new MyButton("Confirmer");
        confirmButton.setOnMouseClicked(event -> view.fireSettingValidationButtonClicked());
        gridPane.add(confirmButton, 4, 9);

        gridPane.add(new Text("Decription des rôles :"), 0, 6);
        int i = 1, j = 6;
        for (Role role : Role.values()) {
            Label roleLabel = new Label(role.roleToDescription());
            Color color = role.roleToColor();
            roleLabel.setTextFill(color.brighter().brighter());
            roleLabel.setBackground(new Background(new BackgroundFill(color.deriveColor(0,1,.5,.5), new CornerRadii(5), new Insets(0))));
            gridPane.add(roleLabel, i, j);
            ++i;
            if (i > 4) {
                i = 1;
                ++j;
            }
        }
    }

    public int getDifficulty() {
        return (int)(difficulty.getValue()+.5);
    }

    public LinkedList<Role> getRoles() {
        LinkedList<Role> roles = new LinkedList<>();
        for (ChoiceBox<String> roleBox : rolesBoxes) {
            if (!roleBox.isDisable()) {
                roles.add(Role.nameToRole(roleBox.getValue()));
            }
        }
        return roles;
    }

    public LinkedList<String> getPlayerNames() {
        LinkedList<String> playerNames = new LinkedList<>();
        for (TextField playerField : playersFields) {
            if (!playerField.isDisable()) {
                playerNames.add(playerField.getText());
            }
        }
        return playerNames;
    }

    public LinkedList<String> getUVNames() {
        LinkedList<String> UVNames = new LinkedList<>();
        for (TextField course : courses) {
            UVNames.add(course.getText());
        }
        return UVNames;
    }


}
package the_projects.view;


import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * TODO complete
 */
public class Room extends StackPane {
    private Color color;
    private String name;
    private Circle circle;
    private int[] projectCubes;
    private Pawn[] pawns;
    private int maxPawns;
    private ArrayList<Corridor> corridors;

    public Room(Color color, String name, double xPos, double yPos, int nbPlayers) {
        this.color = color;
        this.name = name;
        circle = new Circle(10, 10, 20);
        circle.setFill(color);

        getChildren().add(circle);

        setLayoutX(xPos);
        setLayoutY(yPos);

        Text text = new Text(this.name);
        text.setFont(new Font(12));
        text.setFill(color.deriveColor(100,1,2,1));
        text.setMouseTransparent(true);
        getChildren().add(text);

        maxPawns = nbPlayers;
        projectCubes = new int[maxPawns];
        pawns = new Pawn[5];

        Board.setHoverStrokeChange(circle);

        corridors = new ArrayList<>();
    }

    public Coord addPawn(Pawn pawn) {
        //TODO implement
        return new Coord(0, 0);
    }

    public void delPawn(Pawn pawn) {
        //TODO implement
    }

    public void addProjectCube(int project) {
        //TODO implement
    }

    public void delProjectCube(int project) {
        //TODO implement
    }

    public void setTP() {
        //TODO implement
    }

    public Coord getPos() {
        return new Coord(getLayoutX() + circle.getRadius(), getLayoutY() + circle.getRadius());
    }

    public void addCorridor(Corridor c) {
        corridors.add(c);
    }
}
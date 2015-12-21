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
        circle = new Circle(10, 10, 25);
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
        for(Integer i = 0; i < 5; ++i) {
            pawns[i] = null;
        }

        Board.setHoverStrokeChange(circle);

        corridors = new ArrayList<>();
    }

    public Coord addPawn(Pawn pawn) {
        Coord coord;
        if (pawns[0] == null) {
            coord = new Coord(getPos().getX(), getPos().getY() + circle.getRadius()/2);
            pawns[0] = pawn;
        }else if (pawns[1] == null) {
            coord = new Coord(getPos().getX() - circle.getRadius()/3, getPos().getY() - circle.getRadius()/2);
            pawns[1] = pawn;
        }else if (pawns[2] == null) {
            coord = new Coord(getPos().getX() + circle.getRadius()/3, getPos().getY() - circle.getRadius()/2);
            pawns[2] = pawn;
        }else if (pawns[3] == null) {
            coord = new Coord(getPos().getX() - circle.getRadius()/2, getPos().getY() + circle.getRadius()/3);
            pawns[3] = pawn;
        }else if (pawns[4] == null) {
            coord = new Coord(getPos().getX() + circle.getRadius()/2, getPos().getY() + circle.getRadius()/3);
            pawns[4] = pawn;
        } else {
            coord = new Coord(0,0);
        }

        return coord;
    }

    public void delPawn(Pawn pawn) {
        for (int i = 0; i < 5; ++i) {
            if (pawns[i] == pawn) {
                pawns[i] = null;
            }
        }
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
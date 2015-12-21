package the_projects.view;


import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * TODO complete
 */
public class Room extends StackPane {
    private Text name;
    private Circle circle;
    private int[] projectCubes;
    private Pawn[] pawns;
    private int maxPawns;
    private ArrayList<Corridor> corridors;


    public Room(Color color, String name, double xPos, double yPos, int nbPlayers) {

        circle = new Circle(10, 10, 25);
        circle.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))));

        getChildren().add(circle);

        setLayoutX(xPos);
        setLayoutY(yPos);

        this.name = new Text(name);
        this.name.setFont(new Font(12));
        this.name.setFill(color.deriveColor(100,1,4,1));
        this.name.setMouseTransparent(true);
        getChildren().add(this.name);

        maxPawns = nbPlayers;
        projectCubes = new int[maxPawns];
        pawns = new Pawn[5];
        for(Integer i = 0; i < 5; ++i) {
            pawns[i] = null;
        }

        Board.setHoverStrokeChange(circle, color);

        corridors = new ArrayList<>();
    }

    public Coord addPawn(Pawn pawn) {
        Coord coord;
        if (pawns[0] == null) {
            coord = new Coord(getPos().getX(), getPos().getY() + circle.getRadius()/1.6);
            pawns[0] = pawn;
        }else if (pawns[1] == null) {
            coord = new Coord(getPos().getX() - circle.getRadius()/2.5, getPos().getY() - circle.getRadius()/1.5);
            pawns[1] = pawn;
        }else if (pawns[2] == null) {
            coord = new Coord(getPos().getX() + circle.getRadius()/2.5, getPos().getY() - circle.getRadius()/1.5);
            pawns[2] = pawn;
        }else if (pawns[3] == null) {
            coord = new Coord(getPos().getX() - circle.getRadius()/1.2, getPos().getY());
            pawns[3] = pawn;
        }else if (pawns[4] == null) {
            coord = new Coord(getPos().getX() + circle.getRadius()/1.2, getPos().getY());
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
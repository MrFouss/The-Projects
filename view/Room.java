package the_projects.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * TODO complete
 */
public class Room extends StackPane {
    private Color color;
    private Circle circle;
    private Rectangle[][] projectCubes;
    private int nbProjectCubes;
    private Pawn[] pawns;
    private ArrayList<Corridor> corridors;

    /**
     * TODO
     * @param color
     * @param name
     * @param xPos
     * @param yPos
     */
    public Room(Color color, String name, double xPos, double yPos) {
        this.color = color;
        circle = new Circle(10, 10, 25);
        circle.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1,1)), new Stop(1, color.deriveColor(0,1,.5,1))));

        getChildren().add(circle);

        setLayoutX(xPos);
        setLayoutY(yPos);

        Text text = new Text(name);
        text.setFont(new Font(13));
        text.setFill(color.deriveColor(360,1,8,1));
        text.setMouseTransparent(true);
        getChildren().add(text);


        projectCubes = new Rectangle[4][3];

        nbProjectCubes = 0;

        pawns = new Pawn[5];
        for(Integer i = 0; i < 5; ++i) {
            pawns[i] = null;
        }

        Board.setHoverStrokeChange(circle, color);

        corridors = new ArrayList<>();
    }

    /**
     * TODO
     * @param pawn
     * @return
     */
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
        coord.setX(coord.getX()+circle.getRadius()/15);
        return coord;
    }

    /**
     * TODO
     * @param pawn
     */
    public void delPawn(Pawn pawn) {
        for (int i = 0; i < 5; ++i) {
            if (pawns[i] == pawn) {
                pawns[i] = null;
            }
        }
    }

    /**
     * TODO
     * @param project
     * @param projectIndex
     */
    public void addProjectCube(Color project, int projectIndex) {
        int i = 0;
        while (projectCubes[projectIndex][i] != null && i < 2) {
            ++i;
        }
        if (projectCubes[projectIndex][i] == null) {
            projectCubes[projectIndex][i] = new Rectangle(circle.getRadius()/4, circle.getRadius()/4, project);
            projectCubes[projectIndex][i].setMouseTransparent(true);
            switch (i) {
                case 0 :
                    setAlignment(projectCubes[projectIndex][i], Pos.TOP_CENTER);
                    setMargin(projectCubes[projectIndex][i], new Insets(circle.getRadius()*(projectIndex/1.5)/4 + circle.getRadius()/15,0,0,0));
                    break;
                case 1 :
                    setAlignment(projectCubes[projectIndex][i], Pos.BOTTOM_RIGHT);
                    setMargin(projectCubes[projectIndex][i], new Insets(0,circle.getRadius()/2.6 + circle.getRadius()*(projectIndex/1.3)/8, circle.getRadius()/3.5 + circle.getRadius()*(projectIndex/1.3)/8,0));
                    break;
                case 2 :
                    setAlignment(projectCubes[projectIndex][i], Pos.BOTTOM_LEFT);
                    setMargin(projectCubes[projectIndex][i], new Insets(0, 0, circle.getRadius()/3.5 + circle.getRadius()*(projectIndex/1.3)/8, circle.getRadius()/2.6 + circle.getRadius()*(projectIndex/1.3)/8));
                    break;
                default:
                    break;
            }
            getChildren().add(projectCubes[projectIndex][i]);
            ++nbProjectCubes;

            circle.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1 - 0.3*nbProjectCubes,1)), new Stop(1, color.deriveColor(0,1,.5,1))));
        }
    }

    /**
     * TODO
     * @param projectIndex
     */
    public void delProjectCube(int projectIndex) {
        int i = 2;
        while (i > 0 && projectCubes[projectIndex][i] == null) {
            --i;
        }
        if (projectCubes[projectIndex][i] != null) {
            getChildren().remove(projectCubes[projectIndex][i]);
            projectCubes[projectIndex][i] = null;

            --nbProjectCubes;

            circle.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1 - 0.3*nbProjectCubes,1)), new Stop(1, color.deriveColor(0,1,.5,1))));

        }
    }

    /**
     * TODO
     */
    public void setTP() {
        //TODO implement
    }

    /**
     * TODO
     * @return
     */
    public Coord getPos() {
        return new Coord(getLayoutX() + circle.getRadius(), getLayoutY() + circle.getRadius());
    }

    /**
     * TODO
     * @param c
     */
    public void addCorridor(Corridor c) {
        corridors.add(c);
    }

    /**
     * TODO
     * @return
     */
    public ArrayList<Corridor> getCorridors() {
        return corridors;
    }
}
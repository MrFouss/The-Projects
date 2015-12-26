package the_projects.view;


import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * The nodes of the games, the rooms where the Projects are done
 */
public class Room extends StackPane implements Clickable {
    private Color color;
    private String UV;
    private String name;
    private Circle circle;
    private Shape roomShape;
    private Rectangle[][] projectCubes;
    private int nbProjectCubes;
    private Pawn[] pawns;
    private ArrayList<Corridor> corridors;
    private ChangeListener hoverListener;

    /**
     * The constructor of the Rooms
     * @param color the color of the room
     * @param name the name of the room
     * @param UV the name of the main corresponding UV
     * @param xPos the position of the room on the x axis
     * @param yPos the position of the room on the y axis
     */
    public Room(Color color, String name, String UV, double xPos, double yPos) {
        this.color = color;
        circle = new Circle(10, 10, 25);
        roomShape = circle;
        circle.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1,1)), new Stop(1, color.deriveColor(0,1,.5,1))));
        getChildren().add(circle);

        setLayoutX(xPos);
        setLayoutY(yPos);

        this.name = name;

        Label label = new Label(name);
        label.setFont(new Font(14));
        label.setTextFill(color.deriveColor(0,1,8,1));
        label.setMouseTransparent(true);
        getChildren().add(label);

        this.UV = UV;

        projectCubes = new Rectangle[4][3];

        nbProjectCubes = 0;

        pawns = new Pawn[4];
        for(Integer i = 0; i < 4; ++i) {
            pawns[i] = null;
        }

        corridors = new ArrayList<>();
    }

    /**
     * Method to call when moving a pawn to a room to know the good position
     * @param pawn The pawn moving
     * @return the new coordinates to give to the pawn
     */
    public Point2D addPawn(Pawn pawn) {
        Point2D coord;
        if (pawns[0] == null) {
            coord = new Point2D(getPos().getX() - circle.getRadius()/2.5, getPos().getY() - circle.getRadius()/1.5);
            pawns[0] = pawn;
        }else if (pawns[1] == null) {
            coord = new Point2D(getPos().getX() + circle.getRadius()/2.5, getPos().getY() - circle.getRadius()/1.5);
            pawns[1] = pawn;
        }else if (pawns[2] == null) {
            coord = new Point2D(getPos().getX() - circle.getRadius()/1.2, getPos().getY());
            pawns[2] = pawn;
        }else if (pawns[3] == null) {
            coord = new Point2D(getPos().getX() + circle.getRadius()/1.2, getPos().getY());
            pawns[3] = pawn;
        } else {
            coord = new Point2D(0,0);
        }
        coord.add(circle.getRadius()/15,0);
        return coord;
    }

    /**
     * Method to call when moving a pawn from a room
     * @param pawn the pawn to remove
     */
    public Point2D delPawn(Pawn pawn) {
        for (int i = 0; i < 4; ++i) {
            if (pawns[i] == pawn) {
                pawns[i] = null;
                switch (i) {
                    case 0:
                        return new Point2D(getPos().getX() - circle.getRadius()/2.5, getPos().getY() - circle.getRadius()/1.5);
                    case 1:
                        return new Point2D(getPos().getX() + circle.getRadius()/2.5, getPos().getY() - circle.getRadius()/1.5);
                    case 2:
                        return new Point2D(getPos().getX() - circle.getRadius()/1.2, getPos().getY());
                    case 3:
                        return new Point2D(getPos().getX() + circle.getRadius()/1.2, getPos().getY());
                    default:
                        break;
                }
            }
        }
        return new Point2D(0,0);
    }

    /**
     * Method to add a Project cube to a room
     * @param color The color representing the project to add
     * @param projectIndex The number of the project over the 4 different projects types
     */
    public void addProjectCube(Color color, int projectIndex) {
        int i = 0;
        while (projectCubes[projectIndex][i] != null && i < 2) {
            ++i;
        }
        if (projectCubes[projectIndex][i] == null) {
            projectCubes[projectIndex][i] = new Rectangle(circle.getRadius()/4, circle.getRadius()/4, color);
            projectCubes[projectIndex][i].setMouseTransparent(true);
            switch (i) {
                case 0 :
                    setAlignment(projectCubes[projectIndex][i], Pos.TOP_CENTER);
                    setMargin(projectCubes[projectIndex][i], new Insets(circle.getRadius()*(projectIndex/1.5)/4 + circle.getRadius()/15,0,0,0));
                    break;
                case 1 :
                    setAlignment(projectCubes[projectIndex][i], Pos.BOTTOM_RIGHT);
                    setMargin(projectCubes[projectIndex][i], new Insets(0,circle.getRadius()/2.4 + circle.getRadius()*(projectIndex/1.1)/8, circle.getRadius()/3.5 + circle.getRadius()*(projectIndex/1.3)/8,0));
                    break;
                case 2 :
                    setAlignment(projectCubes[projectIndex][i], Pos.BOTTOM_LEFT);
                    setMargin(projectCubes[projectIndex][i], new Insets(0, 0, circle.getRadius()/3.5 + circle.getRadius()*(projectIndex/1.3)/8, circle.getRadius()/2.4 + circle.getRadius()*(projectIndex/1.1)/8));
                    break;
                default:
                    break;
            }
            getChildren().add(projectCubes[projectIndex][i]);
            ++nbProjectCubes;

            roomShape.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, this.color.deriveColor(0,1,1 - 0.3*nbProjectCubes,1)), new Stop(1, this.color.deriveColor(0,1,.5,1))));
        }
    }

    /**
     * Method removing a Project cube from a room
     * @param projectIndex The number of the project over the 4 different projects types
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

            roomShape.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1 - 0.3*nbProjectCubes,1)), new Stop(1, color.deriveColor(0,1,.5,1))));

        }
    }

    /**
     * Method transforming a simple room to a Lab room
     */
    public void setLab() {
        Rectangle rectangle = new Rectangle(circle.getRadius()*2, circle.getRadius()*2, color.brighter());
        getChildren().add(rectangle);
        rectangle.toBack();
        getChildren().remove(circle);

        roomShape = rectangle;
        roomShape.setFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1 - 0.3*nbProjectCubes,1)), new Stop(1, color.deriveColor(0,1,.5,1))));
    }

    /**
     * Method to get the center of a room
     * @return the position of the center of the Room
     */
    public Point2D getPos() {
        return new Point2D(getLayoutX() + circle.getRadius(), getLayoutY() + circle.getRadius());
    }

    /**
     * Getter of the color of the room
     * @return the color of the room
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter of the UV of the room
     * @return the UV of the room
     */
    public String getUV() {
        return UV;
    }

    /**
     * Getter of the name of the room
     * @return the name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Method to add a corridor to a room
     * @param c the corridor to add
     */
    public void addCorridor(Corridor c) {
        corridors.add(c);
    }

    /**
     * Method to get the corridors of a Room
     * @return an ArrayList of the corridors of the Room
     */
    public ArrayList<Corridor> getCorridors() {
        return corridors;
    }

    public Pawn[] getPawns() {
        return pawns;
    }

    @Override
    public void setClickable(boolean clickable, View view) {
        if (clickable) {
            hoverListener = Board.setHoverStrokeChange(roomShape, color);
            roomShape.setOnMouseClicked(event -> view.fireRoomClicked(name));
        }else {
            roomShape.hoverProperty().removeListener(hoverListener);
            roomShape.setStrokeWidth(0);
            roomShape.setOnMouseClicked(null);
        }
    }
}
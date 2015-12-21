package the_projects.view;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * TODO complete
 */
public class Board extends Scene {

    /**
     *
     */
    private Group group;
    private Building[] batiments;
    private HashMap<String, Room> rooms;
    private Pane pane;
    private ArrayList<Pawn> pawns;
    private Card selectedCard;


    public Board(Group root, int nbPlayers) {
        super(root, 1600, 900);

        setFill(new RadialGradient(0, 0, .5, .5, 1, true, CycleMethod.REPEAT, new Stop(0, Color.rgb(78,19,203)), new Stop(0.3, Color.rgb(106,41,156)), new Stop(1, Color.rgb(78,19,203))));

        group = root;
        pane = new Pane();
        group.getChildren().add(pane);

       // pane.setBackground(new Background());

        Label title = new Label("The Projects");
        title.setLayoutX(20);
        title.setLayoutY(40);
        title.setFont(new Font(40));
        title.setTextFill(Color.CHARTREUSE);
        pane.getChildren().add(title);

        float Base = 25;

        batiments = new Building[4];
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*1.5)/100.);
        rectangle.setHeight(getHeight()*((Base*1.05/2)*16/9.)/100.);
        rectangle.setX(getWidth()*(20/100.));
        rectangle.setY(getHeight()*(1/100.));
        batiments[0] = new Building(Color.BLUE, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base)/100.);
        rectangle.setHeight(getHeight()*((Base*3/4)*16/9.)/100.);
        rectangle.setX(getWidth()*(1/100.));
        rectangle.setY(getHeight()*(25/100.));
        batiments[1] = new Building(Color.GREEN, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*1.5)/100.);
        rectangle.setHeight(getHeight()*(((Base+3)/2*16/9.)/100.));
        rectangle.setX(getWidth()*(50/100.));
        rectangle.setY(getHeight()*(30/100.));
        Circle arrondi = new Circle();
        arrondi.setCenterX(rectangle.getX() + rectangle.getWidth()*(48.5/100.));
        arrondi.setCenterY(rectangle.getY() + rectangle.getHeight());
        arrondi.setRadius(rectangle.getWidth()*(12/100.));
        batiments[2] = new Building(Color.YELLOW, arrondi, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*5/8)/100.);
        rectangle.setHeight(getHeight()*((Base*8/10)*16/9.)/100.);
        rectangle.setX(getWidth()*(31/100.));
        rectangle.setY(getHeight()*(60/100.));
        arrondi = new Circle();
        arrondi.setCenterX(rectangle.getX() + rectangle.getWidth()*(50/100.));
        arrondi.setCenterY(rectangle.getY());
        arrondi.setRadius(rectangle.getWidth()*(50/100.));
        batiments[3] = new Building(Color.RED, arrondi, rectangle);

        for (Building s: batiments) {
            pane.getChildren().add(s.getShape());
        }

        pane.getChildren().addAll(
                new Deck(this, "Cartes\nProjet", Color.GRAY, 60/100., 1/100., true),
                new Deck(this, "Défausse\nCartes\nProjet", Color.GRAY, 75/100., 1/100., true),
                new Deck(this, "Cartes\nJoueur", Color.BLUE, 1/100., 75/100., false),
                new Deck(this, "Défausse\nCartes\nJoueur", Color.BLUE, 11/100., 75/100., false)
        );

        setRooms("the_projects/resources/rooms.csv");
        setCorridor("the_projects/resources/corridors.csv");

        pawns = new ArrayList<>();

        pawns.add(new Pawn(Color.ALICEBLUE));
        pawns.add(new Pawn(Color.RED));
        pawns.add(new Pawn(Color.YELLOW));
        pawns.add(new Pawn(Color.ORANGE));
        pawns.add(new Pawn(Color.CYAN));

        for (Pawn pawn : pawns) {
            pane.getChildren().add(pawn.getShape());
            movePawn(pawn, rooms.get("B402"));
        }

        rooms.get("B402").addProjectCube(Color.BLUE, 0);
        rooms.get("B402").addProjectCube(Color.GREEN, 1);
        rooms.get("B402").addProjectCube(Color.YELLOW, 2);
        rooms.get("B402").addProjectCube(Color.RED, 3);
        rooms.get("B402").addProjectCube(Color.BLUE, 0);
        rooms.get("B402").addProjectCube(Color.GREEN, 1);
        rooms.get("B402").addProjectCube(Color.YELLOW, 2);
        rooms.get("B402").addProjectCube(Color.RED, 3);
        rooms.get("B402").addProjectCube(Color.BLUE, 0);
        rooms.get("B402").addProjectCube(Color.GREEN, 1);
        rooms.get("B402").addProjectCube(Color.YELLOW, 2);
        rooms.get("B402").addProjectCube(Color.RED, 3);


        rooms.get("B401").addProjectCube(Color.BLUE, 0);
        rooms.get("B403").addProjectCube(Color.GREEN, 1);
        rooms.get("B403").addProjectCube(Color.YELLOW, 2);
        rooms.get("B406").addProjectCube(Color.RED, 3);
        rooms.get("B406").addProjectCube(Color.RED, 3);
        rooms.get("B406").addProjectCube(Color.RED, 3);

        rooms.get("A208").addProjectCube(Color.BLUE, 0);
        rooms.get("A208").addProjectCube(Color.BLUE, 0);
        rooms.get("A208").addProjectCube(Color.BLUE, 0);
        rooms.get("A206").addProjectCube(Color.BLUE, 0);
        rooms.get("A206").addProjectCube(Color.BLUE, 0);
        rooms.get("A207").addProjectCube(Color.BLUE, 0);

        rooms.get("H010").addProjectCube(Color.GREEN, 1);
        rooms.get("H010").addProjectCube(Color.GREEN, 1);
        rooms.get("H010").addProjectCube(Color.GREEN, 1);
        rooms.get("H011").addProjectCube(Color.GREEN, 1);
        rooms.get("H011").addProjectCube(Color.GREEN, 1);
        rooms.get("H012").addProjectCube(Color.GREEN, 1);

        rooms.get("P108").addProjectCube(Color.YELLOW, 2);
        rooms.get("P108").addProjectCube(Color.YELLOW, 2);
        rooms.get("P108").addProjectCube(Color.YELLOW, 2);
        rooms.get("P106").addProjectCube(Color.YELLOW, 2);
        rooms.get("P106").addProjectCube(Color.YELLOW, 2);
        rooms.get("P107").addProjectCube(Color.YELLOW, 2);

        rooms.get("B404").addProjectCube(Color.RED, 3);
        rooms.get("B404").addProjectCube(Color.RED, 3);
        rooms.get("B404").addProjectCube(Color.RED, 3);
        rooms.get("B404").delProjectCube(3);
        rooms.get("B404").delProjectCube(3);
        rooms.get("B404").delProjectCube(3);

        Scale scale = new Scale(1,1,0,0);
        scale.xProperty().bind(widthProperty().divide(getWidth()));
        scale.yProperty().bind(heightProperty().divide(getHeight()));
        pane.getTransforms().add(scale);

    }

    public static void setHoverStrokeChange(Shape shape, Color color) {
        shape.setStrokeWidth(3);
        shape.setStroke(color.deriveColor(0,1,.5,1));
        shape.hoverProperty().addListener((e) -> {
            if(shape.isHover()) {
                shape.setStroke(color.deriveColor(100,1,4,1));
            }else{
                shape.setStroke(color.deriveColor(0,1,.5,1));
            }
        });
    }

    public void setRooms(String path) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            List<String> newRoomsList = Files.readAllLines(Paths.get(classLoader.getResource(path).getPath()));
            rooms = new HashMap<>();
            Room newRoom;
            for (String s : newRoomsList) {
                String[] vars = s.split(",");
                newRoom = new Room(batiments[Integer.parseInt(vars[0])].getColor().darker(), vars[1], Double.parseDouble(vars[2])*1600/100, Double.parseDouble(vars[3])*900/100);
                pane.getChildren().add(newRoom);
                rooms.put(vars[1], newRoom);
            }
        }
        catch (NullPointerException e) {
            System.out.println("Room file not found(pointer)");
        }
        catch (IOException e) {
            System.out.println("Room file not found");
        }
    }

    public void setCorridor(String path) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            List<String> newCorridorsList = Files.readAllLines(Paths.get(classLoader.getResource(path).getPath()));
            Corridor newCorridor;
            for (String s : newCorridorsList) {
                String[] vars = s.split(",");
                if(vars.length < 3) {
                    newCorridor = new Corridor(rooms.get(vars[0]), rooms.get(vars[1]));

                } else {
                    ArrayList<Coord> coords = new ArrayList<>();

                    for (int i = 2; i < vars.length; i+=2) {
                        coords.add(new Coord(Double.parseDouble(vars[i])*1600/100., Double.parseDouble(vars[i+1])*900/100));
                    }

                    newCorridor = new Corridor(rooms.get(vars[0]), rooms.get(vars[1]), coords);
                }
                pane.getChildren().add(newCorridor);
            }
            rooms.forEach((k,v) -> v.toFront());
        }
        catch (NullPointerException e) {
            System.out.println("Corridor file not found(pointer)");
        }
        catch (IOException e) {
            System.out.println("Corridor file not found");
        }
    }

    public void movePawn(Pawn pawn, Room dest) {
        Coord destCoords = dest.addPawn(pawn);
        Path path = new Path(new MoveTo(pawn.getShape().getLayoutX(), pawn.getShape().getLayoutY()), new LineTo(destCoords.getX(), destCoords.getY()));

        Room startRoom = pawn.setRoom(dest);
        if (startRoom != null) {
            startRoom.delPawn(pawn);
        }
        PathTransition pathTransition = new PathTransition(Duration.seconds(1),path,pawn.getShape());
        pathTransition.playFromStart();

    }
}
package the_projects.view;

import javafx.animation.PathTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import the_projects.model.Role;

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
    private Shape selectedObject;
    private Deck[] decks;
    private StackPane playerDiscard;


    public Board(Group root, Role... roles) {
        super(root, 1600, 900);
        Color background = Color.SLATEBLUE;
        setFill(new RadialGradient(0, 0, .5, .5, 1.5, true, CycleMethod.REPEAT, new Stop(0, background), new Stop(.5, background.deriveColor(0,1,.5,1)), new Stop(1, background)));

        group = root;
        pane = new Pane();
        group.getChildren().add(pane);
        pane.getChildren().add(new Rectangle(getWidth(),getHeight(),0,0));



        Label title = new Label("The Projects");
        title.setLayoutX(20);
        title.setLayoutY(40);
        title.setFont(new Font(40));
        title.setTextFill(background.deriveColor(180,1,1,1));
        pane.getChildren().add(title);

        float Base = 25;

        batiments = new Building[4];
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*1.5)/100.);
        rectangle.setHeight(getHeight()*((Base*1.05/2)*16/9.)/100.);
        rectangle.setX(getWidth()*(20/100.));
        rectangle.setY(getHeight()*(1/100.));
        batiments[0] = new Building(Color.STEELBLUE,"AG44", rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base)/100.);
        rectangle.setHeight(getHeight()*((Base*3/4)*16/9.)/100.);
        rectangle.setX(getWidth()*(1/100.));
        rectangle.setY(getHeight()*(25/100.));
        batiments[1] = new Building(Color.WHITE, "MI41", rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*1.5)/100.);
        rectangle.setHeight(getHeight()*(((Base+3)/2*16/9.)/100.));
        rectangle.setX(getWidth()*(50/100.));
        rectangle.setY(getHeight()*(30/100.));
        Circle arrondi = new Circle();
        arrondi.setCenterX(rectangle.getX() + rectangle.getWidth()*(48.5/100.));
        arrondi.setCenterY(rectangle.getY() + rectangle.getHeight());
        arrondi.setRadius(rectangle.getWidth()*(12/100.));
        batiments[2] = new Building(Color.GOLD, "SI20", arrondi, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*5/8)/100.);
        rectangle.setHeight(getHeight()*((Base*8/10)*16/9.)/100.);
        rectangle.setX(getWidth()*(31/100.));
        rectangle.setY(getHeight()*(60/100.));
        arrondi = new Circle();
        arrondi.setCenterX(rectangle.getX() + rectangle.getWidth()*(50/100.));
        arrondi.setCenterY(rectangle.getY());
        arrondi.setRadius(rectangle.getWidth()*(50/100.));
        batiments[3] = new Building(Color.FIREBRICK, "LO43", arrondi, rectangle);

        for (Building s: batiments) {
            pane.getChildren().add(s.getShape());
        }

        decks = new Deck[4];
        decks[0] = new Deck(this, "Cartes\nProjet", Color.FORESTGREEN, 60/100., 1/100., true);
        decks[1] = new Deck(this, "Défausse\nCartes\nProjet", Color.FORESTGREEN, 75/100., 1/100., true);
        decks[2] = new Deck(this, "Cartes\nJoueur", Color.INDIGO, 1/100., 75/100., false);
        decks[3] = new Deck(this, "Défausse\nCartes\nJoueur", Color.INDIGO, 11/100., 75/100., false);

        playerDiscard = decks[3];
        Arrays.asList(decks).stream().forEach(pane.getChildren()::add);


        setRooms("the_projects/resources/rooms.csv");
        setCorridor("the_projects/resources/corridors.csv");

        pawns = new ArrayList<>();

        rooms.get("B402").setTP();

        Arrays.asList(roles).stream().forEach(role -> pawns.add(new Pawn(role)));


        for (Pawn pawn : pawns) {
            pane.getChildren().add(pawn.getShape());
            movePawn(pawn, rooms.get("B402"));
        }

        //start of test lines
        rooms.get("P101").setTP();
        rooms.get("A201").setTP();
        rooms.get("H010").setTP();

        rooms.get("B402").addProjectCube(projectIndexToColor(0), 0);
        rooms.get("B402").addProjectCube(projectIndexToColor(1), 1);
        rooms.get("B402").addProjectCube(projectIndexToColor(2), 2);
        rooms.get("B402").addProjectCube(projectIndexToColor(3), 3);
        rooms.get("B402").addProjectCube(projectIndexToColor(0), 0);
        rooms.get("B402").addProjectCube(projectIndexToColor(1), 1);
        rooms.get("B402").addProjectCube(projectIndexToColor(2), 2);
        rooms.get("B402").addProjectCube(projectIndexToColor(3), 3);
        rooms.get("B402").addProjectCube(projectIndexToColor(0), 0);
        rooms.get("B402").addProjectCube(projectIndexToColor(1), 1);
        rooms.get("B402").addProjectCube(projectIndexToColor(2), 2);
        rooms.get("B402").addProjectCube(projectIndexToColor(3), 3);


        rooms.get("B401").addProjectCube(projectIndexToColor(0), 0);
        rooms.get("B403").addProjectCube(projectIndexToColor(1), 1);
        rooms.get("B403").addProjectCube(projectIndexToColor(2), 2);
        rooms.get("B406").addProjectCube(projectIndexToColor(3), 3);
        rooms.get("B406").addProjectCube(projectIndexToColor(3), 3);
        rooms.get("B406").addProjectCube(projectIndexToColor(3), 3);

        rooms.get("A208").addProjectCube(projectIndexToColor(0), 0);
        rooms.get("A208").addProjectCube(projectIndexToColor(0), 0);
        rooms.get("A208").addProjectCube(projectIndexToColor(0), 0);
        rooms.get("A206").addProjectCube(projectIndexToColor(0), 0);
        rooms.get("A206").addProjectCube(projectIndexToColor(0), 0);
        rooms.get("A207").addProjectCube(projectIndexToColor(0), 0);

        rooms.get("H010").addProjectCube(projectIndexToColor(1), 1);
        rooms.get("H010").addProjectCube(projectIndexToColor(1), 1);
        rooms.get("H010").addProjectCube(projectIndexToColor(1), 1);
        rooms.get("H011").addProjectCube(projectIndexToColor(1), 1);
        rooms.get("H011").addProjectCube(projectIndexToColor(1), 1);
        rooms.get("H012").addProjectCube(projectIndexToColor(1), 1);

        rooms.get("P108").addProjectCube(projectIndexToColor(2), 2);
        rooms.get("P108").addProjectCube(projectIndexToColor(2), 2);
        rooms.get("P108").addProjectCube(projectIndexToColor(2), 2);
        rooms.get("P106").addProjectCube(projectIndexToColor(2), 2);
        rooms.get("P106").addProjectCube(projectIndexToColor(2), 2);
        rooms.get("P107").addProjectCube(projectIndexToColor(2), 2);

        rooms.get("B404").addProjectCube(projectIndexToColor(3), 3);
        rooms.get("B404").addProjectCube(projectIndexToColor(3), 3);
        rooms.get("B404").addProjectCube(projectIndexToColor(3), 3);
        rooms.get("B404").delProjectCube(3);
        rooms.get("B404").delProjectCube(3);
        rooms.get("B404").delProjectCube(3);
        //end of test lines


        decks[2].setOnMouseClicked(e -> drawPlayerCards(new RoomCard(pane, rooms.get("B402")),new RoomCard(pane, rooms.get("P108"))));


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
            //noinspection ConstantConditions
            List<String> newRoomsList = Files.readAllLines(Paths.get(classLoader.getResource(path).getPath()));
            rooms = new HashMap<>();
            Room newRoom;
            for (String s : newRoomsList) {
                String[] vars = s.split(",");
                newRoom = new Room(batiments[Integer.parseInt(vars[0])].getColor().darker(), vars[1], batiments[Integer.parseInt(vars[0])].getUV(), Double.parseDouble(vars[2])*1600/100, Double.parseDouble(vars[3])*900/100);
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
            //noinspection ConstantConditions
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
        pathTransition.play();

    }

    public Color projectIndexToColor(int projectIndex) {
        return batiments[projectIndex].getColor();
    }

    public void drawPlayerCards(Card card1, Card card2) {
        Path path1 = new Path(new MoveTo(decks[2].getLayoutX() + decks[2].getWidth()/2, decks[2].getLayoutY() + decks[2].getHeight()/2), new LineTo(getWidth()/3 - decks[2].getWidth()/2, getHeight()/2 - decks[2].getHeight()/2));
        Path path2 = new Path(new MoveTo(decks[2].getLayoutX() + decks[2].getWidth()/2, decks[2].getLayoutY() + decks[2].getHeight()/2), new LineTo(getWidth()*2/3 - decks[2].getWidth()/2, getHeight()/2 - decks[2].getHeight()/2));



        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.BLACK.deriveColor(0,1,1,.5));
        rectangle.setWidth(pane.getWidth());
        rectangle.setHeight(pane.getHeight());
        pane.getChildren().addAll(rectangle, card1, card2);

        PathTransition pathTransition = new PathTransition(Duration.seconds(1),path1,card1);
        pathTransition.play();
        pathTransition = new PathTransition(Duration.seconds(1),path2,card2);
        pathTransition.play();

        rectangle.setOnMouseClicked(e -> {
            discardPlayerCard(card1);
            discardPlayerCard(card2);
            pane.getChildren().remove(rectangle);
        });

    }

    public void discardPlayerCard(Card card) {
        Path path = new Path(new MoveTo(card.localToScene(0,0).getX(), card.localToScene(0,0).getY()), new LineTo(decks[3].getLayoutX() + decks[3].getWidth()/2, decks[3].getLayoutY() + decks[3].getHeight()/2));


        PathTransition pathTransition = new PathTransition(Duration.seconds(1),path,card);
        pathTransition.setOnFinished(e -> {
            pane.getChildren().remove(playerDiscard);
            playerDiscard = card;
        });

        pathTransition.play();
    }

    public void drawProjectCards(Card card1, Card card2) {
        //TODO implement
    }

}
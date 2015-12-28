package the_projects.view;

import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
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
import the_projects.model.card.Event;
import the_projects.view.Cards.Card;
import the_projects.view.Cards.EventCard;
import the_projects.view.Cards.PartyCard;
import the_projects.view.Cards.RoomCard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * The main scene of the game, where the action takes place
 * TODO complete
 */
public class Board extends Scene {

    private View view;
    private Building[] batiments;
    private HashMap<String, Room> rooms;
    private Pane pane;
    private ArrayList<Pawn> pawns;
    private Deck[] decks;
    private StackPane playerDiscard;
    private StackPane projectDiscard;
    private PropagationGauge propagationGauge;
    private OutbreaksGauge outbreaksGauge;
    private MasteredCoursesDisplay masteredCoursesDisplay;
    private MyButton giveUp;
    private ArrayList<Card> displayedCards;
    private Rectangle hidingRectangle;

    /**
     * TODO doc when finished
     */
    public Board(Group root, View view, String UV1, String UV2, String UV3, String UV4, Player... players) {
        super(root, 1600, 900);

        this.view = view;

        //creating the group and pane organizing the scene
        pane = new Pane();
        root.getChildren().add(pane);

        //setting the background
        Color background = Color.SLATEBLUE;
        setFill(new RadialGradient(0, 0, .5, .5, 1.5, true, CycleMethod.REPEAT, new Stop(0, background), new Stop(.5, background.deriveColor(0,1,.5,1)), new Stop(1, background)));
        Label title = new Label("The Projects");
        title.setLayoutX(20);
        title.setLayoutY(40);
        title.setFont(new Font(40));
        title.setTextFill(background.deriveColor(180,1,1,1));
        pane.getChildren().add(title);


        //Shaping of the buildings
        float Base = 25;

        batiments = new Building[4];
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*1.5)/100.);
        rectangle.setHeight(getHeight()*((Base*1.05/2)*16/9.)/100.);
        rectangle.setX(getWidth()*(20/100.));
        rectangle.setY(getHeight()*(1/100.));
        batiments[0] = new Building(Color.STEELBLUE,UV1, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base)/100.);
        rectangle.setHeight(getHeight()*((Base*3/4)*16/9.)/100.);
        rectangle.setX(getWidth()*(1/100.));
        rectangle.setY(getHeight()*(25/100.));
        batiments[1] = new Building(Color.WHITE, UV2, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*1.5)/100.);
        rectangle.setHeight(getHeight()*(((Base+3)/2*16/9.)/100.));
        rectangle.setX(getWidth()*(50/100.));
        rectangle.setY(getHeight()*(30/100.));
        Circle arrondi = new Circle();
        arrondi.setCenterX(rectangle.getX() + rectangle.getWidth()*(48.5/100.));
        arrondi.setCenterY(rectangle.getY() + rectangle.getHeight());
        arrondi.setRadius(rectangle.getWidth()*(12/100.));
        batiments[2] = new Building(Color.GOLD, UV3, arrondi, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*5/8)/100.);
        rectangle.setHeight(getHeight()*((Base*8/10)*16/9.)/100.);
        rectangle.setX(getWidth()*(31/100.));
        rectangle.setY(getHeight()*(60/100.));
        arrondi = new Circle();
        arrondi.setCenterX(rectangle.getX() + rectangle.getWidth()*(50/100.));
        arrondi.setCenterY(rectangle.getY());
        arrondi.setRadius(rectangle.getWidth()*(50/100.));
        batiments[3] = new Building(Color.FIREBRICK, UV4, arrondi, rectangle);

        for (Building s: batiments) {
            pane.getChildren().add(s.getShape());
        }

        //Creation of the decks
        decks = new Deck[4];
        decks[0] = new Deck(this, "Cartes\nProjet", Color.FORESTGREEN, 60/100., 1/100., true);
        decks[1] = new Deck(this, "Défausse\nCartes\nProjet", Color.FORESTGREEN, 75/100., 1/100., true);
        decks[2] = new Deck(this, "Cartes\nJoueur", Color.INDIGO, 1/100., 75/100.);
        decks[3] = new Deck(this, "Défausse\nCartes\nJoueur", Color.INDIGO, 12.5/100., 75/100.);

        projectDiscard = decks[1];
        playerDiscard = decks[3];
        Arrays.asList(decks).stream().forEach(pane.getChildren()::add);

        //Creation of the rooms and corridors
        setRooms("/src/the_projects/resources/rooms.csv");
        setCorridors("/src/the_projects/resources/corridors.csv");


        //Creating the pawns
        pawns = new ArrayList<>();
        Arrays.asList(players).stream().forEach(player -> pawns.add(new Pawn(player)));

        for (Pawn pawn : pawns) {
            pane.getChildren().add(pawn.getShape());
            movePawn(pawn.getPlayer().getRole(), "B402");
        }

        Actions actions = new Actions(this, view,pane, players);
        pane.getChildren().add(actions);


        propagationGauge = new PropagationGauge(this, 68.5/100., 18.5/100.);
        pane.getChildren().add(propagationGauge);

        outbreaksGauge = new OutbreaksGauge(this, 89/100., 1/100.);
        pane.getChildren().add(outbreaksGauge);

        masteredCoursesDisplay = new MasteredCoursesDisplay(this, batiments, 1/100., 60/100.);
        pane.getChildren().add(masteredCoursesDisplay);

        giveUp = new MyButton("Abandonner ?");
        giveUp.setLayoutX(600);
        giveUp.setLayoutY(300);
        giveUp.setOnMouseClicked(event -> view.fireGiveUpButtonClicked());
        pane.getChildren().add(giveUp);

        displayedCards = new ArrayList<>();

        hidingRectangle = new Rectangle();
        rectangle.setFill(Color.BLACK.deriveColor(0,1,1,.5));
        rectangle.heightProperty().bind(pane.heightProperty());
        rectangle.widthProperty().bind(pane.widthProperty());

        //making the board proportional to the window
        pane.setMaxSize(getWidth(),getHeight());
        Scale scale = new Scale(1,1,0,0);
        scale.xProperty().bind(widthProperty().divide(getWidth()));
        scale.yProperty().bind(heightProperty().divide(getHeight()));
        pane.getTransforms().add(scale);

    }


    /**
     * Method making the stroke of a shape change color when hovering it
     * @param shape the shape to which we want to apply the method
     * @param color the color of the shape
     */
    public static ChangeListener setHoverStrokeChange(Shape shape, Color color) {
        shape.setStrokeWidth(3);
        shape.setStroke(color.deriveColor(0, 1, .5, 1));
        ChangeListener changeListener = (observable, oldValue, newValue) -> {
            if (shape.isHover()) {
                shape.setStroke(color.deriveColor(100, 1, 1.2, 1));
            } else {
                shape.setStroke(color.deriveColor(0, 1, .5, 1));
            }
        };
        shape.hoverProperty().addListener(changeListener);
        return changeListener;
    }

    /**
     * Method to read the position and place the rooms on the board from a text file
     * @param path the path to the text file
     */
    public void setRooms(String path) {
        try {
            List<String> newRoomsList = Files.readAllLines(Paths.get(System.getProperty("user.dir") + path));
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
            rooms = new HashMap<>();
        }
        catch (IOException e) {
            System.out.println("Room file not found");
            rooms = new HashMap<>();
        }
    }

    /**
     * Method to read the position and place the corridors on the board from a text file
     * @param path the path to the text file
     */
    public void setCorridors(String path) {
        try {
            List<String> newCorridorsList = Files.readAllLines(Paths.get(System.getProperty("user.dir") + path));
            Corridor newCorridor;
            for (String s : newCorridorsList) {
                String[] vars = s.split(",");
                if(vars.length < 3) {
                    newCorridor = new Corridor(rooms.get(vars[0]), rooms.get(vars[1]));

                } else {
                    ArrayList<Point2D> Point2Ds = new ArrayList<>();

                    for (int i = 2; i < vars.length; i+=2) {
                        Point2Ds.add(new Point2D(Double.parseDouble(vars[i])*1600/100., Double.parseDouble(vars[i+1])*900/100));
                    }

                    newCorridor = new Corridor(rooms.get(vars[0]), rooms.get(vars[1]), Point2Ds);
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

    /**
     * Method to move a pawn to a room
     * @param pawn the pawn to move
     * @param dest the room welcoming the pawn
     */
    public PathTransition movePawn(Pawn pawn, Room dest) {
        if (dest != null) {
            Point2D destPoint2Ds = dest.addPawn(pawn);
            Point2D startPoint2Ds = pawn.setRoom(dest);

            Path path = new Path(new MoveTo(startPoint2Ds.getX(), startPoint2Ds.getY()), new LineTo(destPoint2Ds.getX(), destPoint2Ds.getY()));


            return new PathTransition(Duration.seconds(0.8), path, pawn.getShape());
        }else{
            return new PathTransition();
        }
    }

    /**
     * Method to move a pawn along some rooms
     * @param role the role corresponding to the pawn
     * @param rooms the names of the rooms on the pawns way
     */
    public void movePawn(Role role, String... rooms) {
        Pawn pawn = null;
        for (Pawn p : pawns) {
            if (p.getPlayer().getRole() == role) {
                pawn = p;
            }
        }
        if (pawn != null && rooms.length > 0) {
            PathTransition[] transitions = new PathTransition[rooms.length];
            int i = 0;
            for (String name : rooms) {
                transitions[i++] = movePawn(pawn, this.rooms.get(name));
            }
            for (i = 1; i < transitions.length; ++i) {
                final int j = i;
                transitions[i-1].setOnFinished(event -> transitions[j].play());
            }
            transitions[0].play();
        }
    }

    /**
     * Method to get the color of a project
     * @param projectIndex the index of the project
     * @return the color of the project
     */
    public Color projectIndexToColor(int projectIndex) {
        return batiments[projectIndex].getColor();
    }

    /**
     * Method to add a project to a room given its name and the type of project
     * @param roomName the name of the room
     * @param projectIndex the index of the project
     */
    public void addProjectToRoom(String roomName, int projectIndex) {
        rooms.get(roomName).addProjectCube(projectIndexToColor(projectIndex), projectIndex);
    }

    /**
     * Method to remove a project from a room given its name and the type of project
     * @param roomName the name of the room
     * @param projectIndex the index of the project
     */
    public void removeProjectFromRoom(String roomName, int projectIndex) {
        rooms.get(roomName).delProjectCube(projectIndex);
    }

    /**
     * Method to display the movement of a card to a Deck
     * @param card the card to move
     * @param deck the destination deck
     */
    public PathTransition moveToDeck(Card card, Deck deck) {
        Path path = new Path(new MoveTo(card.localToParent(0,0).getX() + card.getWidth()/2, card.localToParent(0,0).getY() + card.getHeight()/2), new LineTo(deck.getLayoutX() + deck.getWidth()/2, deck.getLayoutY() + deck.getHeight()/2));

        card.toFront();
        if (deck.isHorizontal()) {
            RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), card);
            rotateTransition.setByAngle(-90);
            rotateTransition.play();
        }

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), card);
        scaleTransition.setToX(deck.isHorizontal()? deck.getScaleY() : deck.getScaleX());
        scaleTransition.setToY(deck.isHorizontal()? deck.getScaleX() : deck.getScaleY());
        scaleTransition.play();

        card.setOnMouseClicked(null);
        card.setClickable(false, view);

        return new PathTransition(Duration.seconds(.5),path,card);
    }

    /**
     * Method to display the movement of some cards from a Deck
     * @param deck the deck from where the cards come
     * @param cards the cards to move
     */
    public void moveFromDeck(boolean clickable, Deck deck, Card... cards) {
        double i = (6 - cards.length)/2., j = 1;
        if (cards.length > 6) {
            i = (6 - cards.length/2)/2.;
        }
        for (Card card : cards) {
            Path path;
            if (cards.length < 6) {
                path = new Path(new MoveTo(deck.getLayoutX() + deck.getWidth() / 2, deck.getLayoutY() + deck.getHeight() / 2), new LineTo(pane.getWidth() * (++i) / 8, pane.getHeight() / 2));
            }else{
                path = new Path(new MoveTo(deck.getLayoutX() + deck.getWidth() / 2, deck.getLayoutY() + deck.getHeight() / 2), new LineTo(pane.getWidth() * (++i) / 8, pane.getHeight()* j / 3));
                if (i == (6 - cards.length/2)/2. + 5) {
                    ++j;
                    i = (6 - cards.length/2)/2.;
                }
            }
            PathTransition pathTransition = new PathTransition(Duration.millis(500),path,card);
            pathTransition.play();

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), card);
            scaleTransition.setToX(decks[2].getScaleX());
            scaleTransition.setToY(decks[2].getScaleY());
            scaleTransition.play();

            if (deck.isHorizontal()) {
                card.setRotate(-90);
                RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), card);
                rotateTransition.setByAngle(90);
                rotateTransition.play();
            }

            displayedCards.add(card);
        }
        putInFront(clickable, (Card[])displayedCards.toArray());
    }

    /**
     * Method to display the drawing of two cards from the player deck
     * @param card1 first card to draw
     * @param card2 second card to draw
     */
    public void drawPlayerCards(Card card1, Card card2) {

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.BLACK.deriveColor(0,1,1,.5));
        rectangle.setWidth(pane.getWidth());
        rectangle.setHeight(pane.getHeight());
        pane.getChildren().addAll(rectangle, card1, card2);

        rectangle.setOnMouseClicked(e -> {
            discardPlayerCard(card1);
            discardPlayerCard(card2);
            pane.getChildren().remove(rectangle);
        });
        moveFromDeck(false, decks[2], card1, card2);
    }

    /**
     * Method to discard a card to the player discard
     * @param card the card to display
     */
    public void discardPlayerCard(Card card) {

        PathTransition pathTransition = moveToDeck(card, decks[3]);
        pathTransition.setOnFinished(e -> {
            pane.getChildren().remove(playerDiscard);
            playerDiscard = card;
        });
        pathTransition.play();
    }

    /**
     *  Method to display the drawing of cards from the project deck
     * @param cards the cards to draw
     */
    public void drawProjectCards(Card... cards) {
        pane.getChildren().addAll(cards);
        moveFromDeck(false, decks[0], cards);
    }

    /**
     * Method to discard a card to the project discard
     * @param card the card to display
     */
    public void discardProjectCard(Card card) {

        PathTransition pathTransition = moveToDeck(card, decks[1]);
        pathTransition.setOnFinished(e -> {
            pane.getChildren().remove(projectDiscard);
            projectDiscard = card;
        });
        pathTransition.play();
    }

    /**
     * Method to transform a room to a Lab room
     * @param roomName the name of the room
     */
    public void setRoomToLab(String roomName) {
        rooms.get(roomName).setLab();
    }

    /**
     * Method to increase the propagation gauge
     */
    public void increasePropagationGauge() {
        propagationGauge.increase();
    }

    /**
     * Method to increase the burn-out gauge
     */
    public void increaseBurnOutGauge() {
        outbreaksGauge.increase();
    }

    /**
     * Method to make a course mastered
     * @param projectIndex the index of the course
     */
    public void toMastered(int projectIndex) {
        masteredCoursesDisplay.toMastered(projectIndex);
    }

    /**
     * Method to make a course eradicated
     * @param projectIndex the index of the course
     */
    public void toEradicated(int projectIndex) {
        masteredCoursesDisplay.toEradicated(projectIndex);
    }


    /**
     * Method to draw two room cards from the player deck
     *
     * @param roomCard1 the name of the room of the first room card to draw
     * @param roomCard2 the name of the room of the second room card to draw
     */
    public void drawPlayerCards(String roomCard1, String roomCard2) {
        drawPlayerCards(new RoomCard(pane, rooms.get(roomCard1)), new RoomCard(pane, rooms.get(roomCard2)));
    }

    /**
     * Method to draw a room card and an event card from the player deck
     *
     * @param roomCard  the name of the room of the room card to draw
     * @param eventCard the event of the event card to draw
     */
    public void drawPlayerCards(String roomCard, Event eventCard) {
        drawPlayerCards(new RoomCard(pane, rooms.get(roomCard)), new EventCard(pane, eventCard));
    }

    /**
     * Method to draw two event cards from the player deck
     *
     * @param eventCard1 the event of the first event card to draw
     * @param eventCard2 the event of the second event card to draw
     */
    public void drawPlayerCards(Event eventCard1, Event eventCard2) {
        drawPlayerCards(new EventCard(pane, eventCard1), new EventCard(pane, eventCard2));

    }

    /**
     * Method to draw a room card and a student party card from the player deck
     *
     * @param roomCard the name of the room of the room card to draw
     */
    public void drawPlayerCards(String roomCard) {
        drawPlayerCards(new RoomCard(pane, rooms.get(roomCard)), new PartyCard(pane));
    }

    /**
     * Method to draw an event card and a student party card from the player deck
     *
     * @param eventCard the event of the event card to draw
     */
    public void drawPlayerCards(Event eventCard) {
        drawPlayerCards(new EventCard(pane, eventCard), new PartyCard(pane));
    }

    /**
     * Method to draw two student party cards from the player deck
     */
    public void drawPlayerCards() {
        drawPlayerCards(new PartyCard(pane), new PartyCard(pane));
    }

    /**
     * Method to discard a room card to the player discard
     * @param roomCard the name of the room of the room card
     */
    public void discardPlayerCard(String roomCard) {
        //TODO implement
    }

    /**
     * Method to discard an event card to the player discard
     * @param eventCard the event of the event card
     */
    public void discardPlayerCard(Event eventCard) {
        //TODO implement
    }

    /**
     * Method to make a partyCard disappear
     */
    public void discardPartyCard() {
        //TODO implement
    }

    /**
     * Method to draw project cards from the projects deck
     * @param roomNames the names of the projects to draw
     */
    public void drawProjectCards(String... roomNames) {
        //TODO implement
    }

    /**
     * Method to discard a project card to the project discard
     * @param roomName the name of the room of the project
     */
    public void discardProjectCard(String roomName) {
        //TODO implement
    }

    /**
     * Method to display the reachable rooms
     * @param rooms the name of the reachable rooms with the number of actions needed to reach them
     */
    public void reachableRooms(HashMap<String, Integer> rooms) {
        ArrayList<Room> roomTab = new ArrayList<>();
        rooms.forEach((name, dist) -> roomTab.add(this.rooms.get(name)));
        putInFront(true, roomTab.toArray(new Room[roomTab.size()]));
        rooms.forEach((name, dist) -> {
            this.rooms.get(name).setFill(Color.GREEN.deriveColor(0,1,1./dist,1));
        });
    }

    /**
     * Method to give a room card to a player
     * @param roomName the name of the room of the room card
     * @param role the role of the player
     */
    public void giveCardToPlayer(String roomName, Role role) {
        //TODO implement
    }

    /**
     * Method to give an event card to a player
     * @param event the event of the event card
     * @param role the role of the player
     */
    public void giveCardToPlayer(Event event, Role role) {
        //TODO implement
    }

    /**
     * Method to take a room card from a player
     * @param roomName the name of the room of the room card
     * @param role the role of the player
     */
    public void discardCardFromPlayer(String roomName, Role role) {
        //TODO implement
    }

    /**
     * Method to take an event card from a player
     * @param event the event of the event card
     * @param role the role of the player
     */
    public void discardCardFromPlayer(Event event, Role role) {
        //TODO implement
    }

    /**
     * Method to draw event cards from the player discard
     * @param events the events of the event cards to draw
     */
    public void drawEventCardsFromDiscard(Event... events) {
        //TODO implement
    }

    /**
     * Method to put elements in front and make only them clickable
     * @param clickable if true, the elements will be clickable
     * @param nodes the list of elements to put in front
     */
    public void putInFront(boolean clickable, Node... nodes) {
        hidingRectangle = new Rectangle();
        hidingRectangle.setFill(Color.BLACK.deriveColor(0,1,1,.6));
        hidingRectangle.setWidth(pane.getWidth());
        hidingRectangle.setHeight(pane.getHeight());
        pane.getChildren().add(hidingRectangle);
        for (Node node : nodes) {
            node.toFront();
            if (node.getClass() == Room.class) {
                for ( Pawn pawn : ((Room)node).getPawns()) {
                    if(pawn != null)
                        pawn.getShape().toFront();
                }
            }
            if (clickable) {
                try {
                    ((Clickable)node).setClickable(true,view);
                }catch (Exception ignore) {}
            }
        }

        hidingRectangle.setOnMouseClicked(e -> clean());
    }

    public void displayCardsOfPlayer(Role role) {
        //TODO implement
    }

    public void clean() {

        pane.getChildren().remove(hidingRectangle);
        pane.getChildren().stream().filter(node -> node instanceof Clickable).forEach(node -> {
            try {
                ((Clickable) node).setClickable(false, view);
                ((Clickable) node).resetFill();
            } catch (Exception ignore) {}
        });
    }
}
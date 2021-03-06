package the_projects.view;

import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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
import the_projects.view.cards.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * The main scene of the game, where the action takes place
 */
public class Board extends Scene {

    private final View view;
    private final Building[] batiments;
    private HashMap<String, Room> rooms;
    private final Pane pane;
    private final Actions actions;
    private final ArrayList<Pawn> pawns;
    private final ArrayList<StackPane> decks;
    private final PropagationGauge propagationGauge;
    private final OutbreaksGauge outbreaksGauge;
    private final MasteredCoursesDisplay masteredCoursesDisplay;
    private final MyButton giveUp;
    private LinkedList<Card> displayedCards;
    private Rectangle hidingRectangle;
    private final Player[] players;
    private Player currentPlayer;
    private final Label actionPoints;

    /**
     * The constructor of a Board
     * @param root the group in to which the Board belongs
     * @param view the View making the Board
     * @param UV1 the name of the first course
     * @param UV2 the name of the second course
     * @param UV3 the name of the third course
     * @param UV4 the name of the fourth course
     * @param players the name of the players
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
        decks = new ArrayList<>(5);
        decks.add(0, new Deck(this, "Cartes\nProjet", Color.FORESTGREEN, 60/100., 1/100., true));
        decks.add(1, new Deck(this, "Défausse\nCartes\nProjet", Color.FORESTGREEN, 75/100., 1/100., true));
        pane.getChildren().add(new Deck(this, "Défausse\nCartes\nProjet", Color.FORESTGREEN, 75/100., 1/100., true));
        decks.add(2, new Deck(this, "Cartes\nJoueur", Color.INDIGO, 1/100., 75/100.));
        decks.add(3, new Deck(this, "Défausse\nCartes\nJoueur", Color.INDIGO, 12.5/100., 75/100.));
        //hackerDeck
        decks.add(4, new Deck(this, "", Color.TRANSPARENT, 53/100., 80/100.));
        decks.get(4).setScaleX(0.1);
        decks.get(4).setScaleY(0.1);

        pane.getChildren().addAll(decks);

        //Creation of the rooms and corridors
        setRooms();
        setCorridors();


        //Creating the pawns
        this.players = players;

        pawns = new ArrayList<>();
        Arrays.asList(players).stream().forEach(player -> pawns.add(new Pawn(player)));

        for (Pawn pawn : pawns) {
            pane.getChildren().add(pawn.getShape());
            movePawn(pawn.getPlayer().getRole(), "B402");
        }

        actions = new Actions(this, view,pane, players);
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

        displayedCards = new LinkedList<>();

        hidingRectangle = new Rectangle();
        rectangle.setFill(Color.BLACK.deriveColor(0,1,1,.5));
        rectangle.heightProperty().bind(pane.heightProperty());
        rectangle.widthProperty().bind(pane.widthProperty());

        actionPoints = new Label("Points d'actions restants : 0");
        actionPoints.setBackground(new Background(new BackgroundFill(Color.WHITE.deriveColor(0,1,.9,.5), new CornerRadii(10), new Insets(-5))));
        actionPoints.setLayoutX(getWidth()*85.5/100.);
        actionPoints.setLayoutY(getHeight()*62/100.);
        pane.getChildren().add(actionPoints);

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
        //noinspection unchecked
        shape.hoverProperty().addListener(changeListener);
        return changeListener;
    }



    /**
     * Method to read the position and place the rooms on the board from a text file
     */
    private void setRooms() {
        try {
            List<String> newRoomsList = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/the_projects/resources/rooms.csv"));
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
     */
    private void setCorridors() {
        try {
            List<String> newCorridorsList = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/the_projects/resources/corridors.csv"));
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
    private PathTransition movePawn(Pawn pawn, Room dest) {
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
            transitions[transitions.length-1].setOnFinished(event -> view.fireMovePawnFinished());
            transitions[0].play();
        }
    }

    /**
     * Method to get the color of a project
     * @param projectIndex the index of the project
     * @return the color of the project
     */
    private Color projectIndexToColor(int projectIndex) {
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
        propagationGauge.increase(view);
    }

    /**
     * Method to increase the burn-out gauge
     */
    public void increaseBurnOutGauge() {
        outbreaksGauge.increase(view);
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
     * Method to display the reachable rooms
     * @param rooms the name of the reachable rooms with the number of actions needed to reach them
     */
    public void reachableRooms(HashMap<String, Integer> rooms) {
        ArrayList<Room> roomTab = new ArrayList<>();
        rooms.forEach((name, dist) -> roomTab.add(this.rooms.get(name)));
        putInFront(true, roomTab.toArray(new Room[roomTab.size()]));
        rooms.forEach((name, dist) -> this.rooms.get(name).setFill(Color.GREEN.deriveColor(0,1,1./dist,1)));
    }


    /**
     * Method to put elements in front and make only them clickable
     * @param clickable if true, the elements will be clickable
     * @param nodes the list of elements to put in front
     */
    private void putInFront(boolean clickable, Node... nodes) {
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
            if (node instanceof Clickable) {
                try {
                    ((Clickable)node).setClickable(clickable,view);
                }catch (Exception ignore) {}
            }
        }

        hidingRectangle.setOnMouseClicked(e -> clean());
    }

    /**
     * Method to return the board to a clean state (only buttons and player hands can be clicked)
     */
    public void clean() {
        discardCards();
        pane.getChildren().stream().filter(node -> node instanceof Clickable && !(node instanceof Card)).forEach(node -> {
            try {
                ((Clickable) node).setClickable(false, view);
                ((Clickable) node).resetFill();
            } catch (Exception ignore) {}
        });
        pawns.forEach(pawn -> {
            pawn.getShape().toFront();
            pawn.setClickable(false, view);
        });
        pane.getChildren().remove(hidingRectangle);
        view.fireCleared();
    }

    /**
     * Method to convert an Owner to its Deck
     * @param owner the Owner
     * @return the Deck
     */
    private StackPane ownerToDeck(Owner owner) {
        switch (owner) {
            case PLAYER1:
                return players[0].getHandDeck();
            case PLAYER2:
                return players[1].getHandDeck();
            case PLAYER3:
                return players[2].getHandDeck();
            case PLAYER4:
                return players[3].getHandDeck();
            case PLAYER_DECK:
                return decks.get(2);
            case PLAYER_DISCARD:
                return decks.get(3);
            case PROJECT_DECK:
                return decks.get(0);
            case PROJECT_DISCARD:
                return decks.get(1);
            case HACKER:
                return decks.get(4);
            default:
                return new Deck(this,"",Color.TRANSPARENT,0,0);
        }
    }

    /**
     * Method to display the movement of some cards from a Deck
     * @param deck the deck from where the cards come
     * @param cards the cards to move
     */
    private void moveFromDeck(boolean clickable, StackPane deck, boolean horizontal, Card... cards) {
        double i = (6 - cards.length)/2., j = 1;
        if (cards.length > 6) {
            i = (6 - cards.length/2)/2.;
        }
        PathTransition lastTransition = new PathTransition();
        for (Card card : cards) {
            Path path;
            if (cards.length < 6) {
                path = new Path(new MoveTo(deck.localToParent(deck.getWidth()/2.,deck.getHeight()/2.).getX(), deck.localToParent(deck.getWidth()/2.,deck.getHeight()/2.).getY()), new LineTo(pane.getWidth() * (++i) / 8, pane.getHeight() / 2));
            }else{
                path = new Path(new MoveTo(deck.localToParent(deck.getWidth()/2.,deck.getHeight()/2.).getX(), deck.localToParent(deck.getWidth()/2.,deck.getHeight()/2.).getY()), new LineTo(pane.getWidth() * (++i) / 8, pane.getHeight()* j / 3));
                if (i == (6 - cards.length/2)/2. + 5) {
                    ++j;
                    i = (6 - cards.length/2)/2.;
                }
            }
            PathTransition pathTransition = new PathTransition(Duration.millis(500),path,card);
            pathTransition.play();
            lastTransition = pathTransition;

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), card);
            scaleTransition.setToX(decks.get(2).getScaleX());
            scaleTransition.setToY(decks.get(2).getScaleY());
            scaleTransition.play();

            if (horizontal) {
                card.setRotate(-90);
                RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), card);
                rotateTransition.setByAngle(90);
                rotateTransition.play();
            }

            displayedCards.add(card);
        }

        lastTransition.setOnFinished(event -> view.fireCardToCenterFinished());

        putInFront(clickable, displayedCards.toArray(new Card[displayedCards.size()]));
    }

    /**
     * Method to display the movement of a card to a Deck
     * @param card the card to move
     */
    private PathTransition moveToDeck(Card card) {
        StackPane deck = ownerToDeck(card.getOwner());
        Path path = new Path(new MoveTo(card.localToParent(0,0).getX() + card.getWidth()/2, card.localToParent(0,0).getY() + card.getHeight()/2),
                new LineTo(deck.localToParent(deck.getWidth()/2.,deck.getHeight()/2.).getX(), deck.localToParent(deck.getWidth()/2.,deck.getHeight()/2.).getY()));

        boolean horizontal = card.getOwner() == Owner.PROJECT_DECK || card.getOwner() == Owner.PROJECT_DISCARD;

        card.toFront();
        if (horizontal) {
            RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), card);
            rotateTransition.setByAngle(-90);
            rotateTransition.play();
        }

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), card);
        scaleTransition.setToX(horizontal ? deck.getScaleY() : deck.getScaleX());
        scaleTransition.setToY(horizontal ? deck.getScaleX() : deck.getScaleY());
        scaleTransition.play();

        card.setClickable(false, view);

        return new PathTransition(Duration.seconds(.5),path,card);
    }

    /**
     * Method to draw some cards
     * @param actualOwner from where the cards are drawn
     * @param newOwner to where the cards will be discarded
     * @param clickable true if the cards have to be clickable when displayed
     * @param roomNamesOfRoomCards the list of names of the room cards to draw
     * @param eventsOfEventCards the list of events of the event cards to draw
     * @param numberOfPartyCards the number of party cards to draw
     */
    public void drawCards(Owner actualOwner, Owner newOwner, boolean clickable, ArrayList<String> roomNamesOfRoomCards, ArrayList<Event> eventsOfEventCards, int numberOfPartyCards) {
        if (roomNamesOfRoomCards == null)
            roomNamesOfRoomCards = new ArrayList<>();
        if (eventsOfEventCards == null)
            eventsOfEventCards = new ArrayList<>();


        ArrayList<Card> cards = new ArrayList<>();
        if (actualOwner == Owner.PLAYER1 ) {
            roomNamesOfRoomCards.forEach(roomName -> {
                if (players[0].have(roomName)) {
                    cards.add(players[0].take(roomName));
                }
            });
            eventsOfEventCards.forEach(event -> {
                if (players[0].have(event)) {
                    cards.add(players[0].take(event));
                }
            });
        } else if (actualOwner == Owner.PLAYER2) {
            roomNamesOfRoomCards.forEach(roomName -> {
                if (players[1].have(roomName)) {
                    cards.add(players[1].take(roomName));
                }
            });
            eventsOfEventCards.forEach(event -> {
                if (players[1].have(event)) {
                    cards.add(players[1].take(event));
                }
            });
        } else if (actualOwner == Owner.PLAYER3) {
            roomNamesOfRoomCards.forEach(roomName -> {
                if (players[2].have(roomName)) {
                    cards.add(players[2].take(roomName));
                }
            });
            eventsOfEventCards.forEach(event -> {
                if (players[2].have(event)) {
                    cards.add(players[2].take(event));
                }
            });
        } else if (actualOwner == Owner.PLAYER4) {
            roomNamesOfRoomCards.forEach(roomName -> {
                if (players[3].have(roomName)) {
                    cards.add(players[3].take(roomName));
                }
            });
            eventsOfEventCards.forEach(event -> {
                if (players[3].have(event)) {
                    cards.add(players[3].take(event));
                }
            });
        }else {
            roomNamesOfRoomCards.forEach(roomName -> cards.add(new RoomCard(pane, rooms.get(roomName), newOwner)));
            eventsOfEventCards.forEach(event -> cards.add(new EventCard(pane, event, newOwner)));
            for (int i = 0; i < numberOfPartyCards; ++i)
                cards.add(new PartyCard(pane, Owner.PLAYER_DISCARD));
            pane.getChildren().addAll(cards);
        }
        cards.forEach(card -> {
            card.setScaleX(ownerToDeck(actualOwner).getScaleX());
            card.setScaleY(ownerToDeck(actualOwner).getScaleY());
            if (!(card instanceof PartyCard))
                card.setOwner(newOwner);
        });
        if (cards.size() == 1) {
            switch (actualOwner) {
                case PLAYER1:
                    if (players[0].getHand().size() > 0) {
                        players[0].setHandCard(players[0].getHand().peekLast(), view);
                    }
                    break;
                case PLAYER2:
                    if (players[1].getHand().size() > 0) {
                        players[1].setHandCard(players[1].getHand().peekLast(), view);
                    }
                    break;
                case PLAYER3:
                    if (players[2].getHand().size() > 0) {
                        players[2].setHandCard(players[2].getHand().peekLast(), view);
                    }
                    break;
                case PLAYER4:
                    if (players[3].getHand().size() > 0) {
                        players[3].setHandCard(players[3].getHand().peekLast(), view);
                    }
                    break;

            }
        }

        moveFromDeck(clickable, ownerToDeck(actualOwner), actualOwner == Owner.PROJECT_DECK || actualOwner == Owner.PROJECT_DISCARD, cards.toArray(new Card[cards.size()]));

        if (actualOwner == Owner.PROJECT_DISCARD) {
            pane.getChildren().remove(decks.get(1));
        }
    }

    /**
     * Method to give all displayed cards to their owners
     */
    public void discardCards() {
        if (displayedCards.size() > 0) {
            Card lastCard = displayedCards.peekLast();
            displayedCards.stream().forEachOrdered(card -> {
                PathTransition pathTransition = moveToDeck(card);
                pathTransition.setOnFinished(event -> {
                    if (card.getOwner() == Owner.HACKER) {
                        card.toBack();
                    }

                    switch (card.getOwner()) {
                        case PLAYER_DECK:
                            pane.getChildren().remove(card);
                            break;
                        case PLAYER_DISCARD:
                            pane.getChildren().remove(decks.get(3));
                            decks.set(3, card);
                            break;
                        case PROJECT_DECK:
                            pane.getChildren().remove(card);
                            break;
                        case PROJECT_DISCARD:
                            pane.getChildren().remove(decks.get(1));
                            decks.set(1, card);
                            break;
                        case HACKER:
                            pane.getChildren().remove(decks.get(4));
                            decks.set(4, card);
                            break;
                        case PLAYER1:
                            players[0].addCard(card);
                            players[0].setHandCard(card,view);
                            break;
                        case PLAYER2:
                            players[1].addCard(card);
                            players[1].setHandCard(card,view);
                            break;
                        case PLAYER3:
                            players[2].addCard(card);
                            players[2].setHandCard(card,view);
                            break;
                        case PLAYER4:
                            players[3].addCard(card);
                            players[3].setHandCard(card,view);
                            break;
                        default:
                            break;
                    }
                    if (card == lastCard)
                        view.fireCardToDeckFinished();
                });
                pathTransition.play();
            });
            displayedCards.clear();

        }
    }


    /**
     * Method to give a particular displayed room card to a particular owner
     * @param newOwner the new owner of the displayed card
     * @param roomNameOfRoomCard the name of the room card
     */
    public void changeOwnerOfDisplayedCard(Owner newOwner, String roomNameOfRoomCard) {
        displayedCards.stream().filter(card -> roomNameOfRoomCard.equals(card.getText())).forEach(card -> card.setOwner(newOwner));
    }

    /**
     * Method to give a particular displayed event card to a particular owner
     * @param newOwner the new owner of the displayed card
     * @param eventOfEventCard the name of the event card
     */
    public void changeOwnerOfDisplayedCard(Owner newOwner, Event eventOfEventCard) {
        displayedCards.stream().filter(card -> eventOfEventCard.equals(Event.nameToEvent(card.getTitle()))).forEach(card -> card.setOwner(newOwner));
    }


    /**
     * Method to notify a card has been clicked from its name
     * @param title the title of the clicked card
     */
    public void titleToFireCardClicked(String title, String text) {
        if (rooms.containsKey(text) || Event.nameToEvent(title) != null) {
            for (Player player : players) {
                if (player != null && player.have(text)) {
                    ArrayList<String> rooms = new ArrayList<>();
                    ArrayList<Event> events = new ArrayList<>();
                    Owner owner = player.getHand().peek().getOwner();
                    player.getHand().forEach(card -> {
                        if (this.rooms.containsKey(card.getText()))
                            rooms.add(card.getText());
                        else if (Event.nameToEvent(card.getTitle()) != null)
                            events.add(Event.nameToEvent(card.getTitle()));
                    });
                    drawCards(owner, owner, false, rooms, events, 0);
                    return;
                }
            }
            if (rooms.containsKey(text))
                view.fireRoomCardClicked(text);
            else
                view.fireEventCardClicked(Event.nameToEvent(title));
        }
    }

    /**
     * Method to disable or enable the moves button
     * @param disabled if true, the button will be disabled
     */
    public void setMovesButtonDisabled(boolean disabled) {
        actions.setMovesDisabled(disabled);
    }

    /**
     * Method to disable or enable the projects button
     * @param disabled if true, the button will be disabled
     */
    public void setProjectsButtonDisabled(boolean disabled) {
        actions.setProjectsDisabled(disabled);
    }

    /**
     * Method to disable or enable the share button
     * @param disabled if true, the button will be disabled
     */
    public void setShareButtonDisabled(boolean disabled) {
        actions.setShareDisabled(disabled);
    }

    /**
     * Method to disable or enable the card button
     * @param disabled if true, the button will be disabled
     */
    public void setCardButtonDisabled(boolean disabled) {
        actions.setCardDisabled(disabled);
    }

    /**
     * Method to disable or enable the master button
     * @param disabled if true, the button will be disabled
     */
    public void setMasterButtonDisabled(boolean disabled) {
        actions.setMasterDisabled(disabled);
    }

    /**
     * Method to disable or enable the lab room button
     * @param disabled if true, the button will be disabled
     */
    public void setLabRoomButtonDisabled(boolean disabled) {
        actions.setLabRoomDisabled(disabled);
    }

    /**
     * Method to disable or enable the hacker button
     * @param disabled if true, the button will be disabled
     */
    public void setHackerButtonDisabled(boolean disabled) {
        actions.setHackerDisabled(disabled);
    }

    /**
     * Method to disable or enable the end button
     * @param disabled if true, the button will be disabled
     */
    public void setEndButtonButtonDisabled(boolean disabled) {
        actions.setEndButtonDisabled(disabled);
    }

    /**
     * Method to make a Pawn clickable or not
     * @param clickable true to make the pawn clickable
     * @param role the role of the pawn to change
     */
    public void makePawnClickable(boolean clickable, Role role) {
        pawns.forEach(pawn -> {
            if (pawn.getPlayer().getRole() == role)
                pawn.setClickable(clickable, view);
        });
    }

    /**
     * Method to make a list of Pawns clickable
     * @param roles the list of the roles of the Pawns
     */
    public void makePawnsClickable(LinkedList<Role> roles) {
        ArrayList<Shape> pawnsNode = new ArrayList<>();
        pawns.stream().filter(pawn1 -> roles.contains(pawn1.getPlayer().getRole())).forEach(pawn2 -> pawnsNode.add(pawn2.getShape()));
        pawns.forEach(pawn -> pawn.setClickable(true, view));
        putInFront(true, pawnsNode.toArray(new Node[pawnsNode.size()]));
    }

    /**
     * Method to make all pawns clickable
     */
    public void makePawnsClickable() {
        Shape[] pawnsNode = new Shape[pawns.size()];
        for (int i = 0; i < pawns.size(); ++i) {
            pawnsNode[i] = pawns.get(i).getShape();
        }
        pawns.forEach(pawn -> pawn.setClickable(true, view));
        putInFront(true, pawnsNode);
    }

    /**
     * Method to change the current player
     * @param role the role of the new current player
     */
    public void setCurrentPlayer(Role role) {
        if (currentPlayer != null) {
            currentPlayer.setCurrent(false);
        }
        for (Player player : players) {
            if (player.getRole() == role) {
                currentPlayer = player;
                currentPlayer.setCurrent(true);
                return;
            }
        }
    }

    /**
     * Method to change the amount of current action points available
     * @param actionPoints the new amount of action points
     */
    public void setActionsPoints(int actionPoints) {
        this.actionPoints.setText("Points d'actions restants : " + actionPoints);
    }
}
package the_projects.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import the_projects.model.Role;
import the_projects.view.Cards.Card;

import java.util.HashSet;

/**
 * The representation of a player
 */
public class Player {
    private String name;
    private Role role;
    private Pawn pawn;
    private HashSet<Card> hand;
    private StackPane handDeck;
    private Pane pane;
    private Color color;

    /**
     * Constructor without cards
     * @param name the name of the player
     * @param role the role of the player
     */
    public Player(String name, Role role) {
        this.name = name;
        this.role = role;
        color = role.roleToColor();
        hand = new HashSet<>();
        handDeck = null;
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1,.25)), new Stop(1, color.deriveColor(0,1,.5,.25))), new CornerRadii(5), new Insets(0))));

        Label description = new Label(role.roleToDescription());
        description.setTextFill(role.roleToColor().brighter());
        pane.getChildren().add(description);
        description = new Label(name);
        description.setTextFill(role.roleToColor().brighter());
        description.setLayoutX(350);
        description.setLayoutY(20);
        pane.getChildren().add(description);


        pawn = new Pawn(this);
    }

    /**
     * Getter for the name
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the role
     * @return the role of the player
     */
    public Role getRole() {
        return role;
    }

    /**
     * Getter for the hand
     * @return the cards of the player in a HashSet
     */
    public HashSet<Card> getHand() {
        return hand;
    }


    /**
     * Method to give a card to a player
     * @param card the card given to the player
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Method to take a card from a player
     * @param card the card taken from the player
     */
    public void removeCard(Card card) {
        hand.remove(card);
    }

    /**
     * Getter for the Deck of the player
     * @return the Deck of the player
     */
    public StackPane getHandDeck() {
        return handDeck;
    }

    /**
     * Setter for the Deck of the player
     * @param handDeck the new Deck of the player
     */
    public void setHandDeck(StackPane handDeck) {
        this.handDeck = handDeck;
    }

    /**
     * Getter for the Pane of the player
     * @return the Pane of the player
     */
    public Pane getPane() {
        return pane;
    }

    /**
     * Getter for the Pawn of the player
     * @return the Pawn of the player
     */
    public Pawn getPawn() {
        return pawn;
    }

    /**
     * Getter for the Color of the player
     * @return the Color of the player
     */
    public Color getColor() {
        return color;
    }

}

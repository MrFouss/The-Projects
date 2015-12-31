package the_projects.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import the_projects.model.Role;
import the_projects.model.card.Event;
import the_projects.view.cards.Card;

import java.util.LinkedList;

/**
 * The representation of a player
 */
public class Player {
    private final Label nameLabel;
    private final String name;
    private final Role role;
    private final Pawn pawn;
    private LinkedList<Card> hand;
    private StackPane handDeck;
    private Card handCard;
    private final Pane pane;
    private final Color color;

    /**
     * Constructor without cards
     * @param name the name of the player
     * @param role the role of the player
     */
    public Player(String name, Role role) {
        this.name = name;
        this.role = role;
        color = role.roleToColor();
        hand = new LinkedList<>();
        handDeck = null;
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1,.25)), new Stop(1, color.deriveColor(0,1,.5,.25))), new CornerRadii(5), new Insets(0))));

        Label description = new Label(role.roleToDescription());
        description.setTextFill(role.roleToColor().brighter());
        pane.getChildren().add(description);
        nameLabel = new Label(name);
        nameLabel.setTextFill(role.roleToColor().brighter());
        nameLabel.setLayoutX(350);
        nameLabel.setLayoutY(20);
        pane.getChildren().add(nameLabel);


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
    public LinkedList<Card> getHand() {
        return hand;
    }

    /**
     * Setter for the hand
     * @param hand the new hand
     */
    public void setHand(LinkedList<Card> hand) {
        this.hand = hand;
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

    public void setHandCard(Card card, View view) {
        if (handCard != null)
            handCard.setClickable(false, view);
        handCard = card;
        if (handCard != null) {
            handCard.setClickable(true, view);
            handCard.toFront();
        }
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

    /**
     * Method to check if a player have a roomCard
     * @param roomName the name of the room corresponding to the room card
     * @return true if the player have the card in his hand
     */
    public boolean have(String roomName) {
        for (Card card : hand) {
            if (card.getText().equals(roomName)) {
                return true;
            }
        }
        return false;
    }

    public boolean have(Event event) {
        for (Card card : hand) {
            if (card.getTitle().equals(event.eventToName())) {
                return true;
            }
        }
        return false;
    }

    public Card take(String roomName) {
        for (Card card : hand) {
            if (card.getText().equals(roomName)) {
                hand.remove(card);
                return card;
            }
        }
        return null;
    }

    public Card take(Event event) {
        for (Card card : hand) {
            if (card.getTitle().equals(event.eventToName())) {
                hand.remove(card);
                return card;
            }
        }
        return null;
    }

    public void setCurrent(boolean current) {
        if (current) {
            nameLabel.setBackground(new Background(new BackgroundFill(color.deriveColor(0,1,.5,.8), new CornerRadii(10), new Insets(-5))));
        }else {
            nameLabel.setBackground(null);
        }
    }
}

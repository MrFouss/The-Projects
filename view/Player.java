package the_projects.view;

import the_projects.model.Role;

import java.util.Collections;
import java.util.HashSet;

/**
 * The representation of a player as a Role and a name
 */
public class Player {
    private String name;
    private Role role;
    private HashSet<Card> hand;
    private Deck handDeck;

    /**
     * Default constructor
     */
    public Player() {
        name = "player";
        role = Role.TRELLO_ADEPT;
        hand = new HashSet<>();
        handDeck = null;
    }

    /**
     * Constructor without cards
     * @param name the name of the player
     * @param role the role of the player
     */
    public Player(String name, Role role) {
        this.name = name;
        this.role = role;
        hand = new HashSet<>();
        handDeck = null;
    }

    /**
     * Complete constructor
     * @param name the name of the player
     * @param role the role of the player
     * @param deck the place to put the cards of the player
     * @param hand the cards the player have
     */
    public Player(String name, Role role, Deck deck, Card... hand ) {
        this(name, role);
        Collections.addAll(this.hand, hand);
        handDeck = deck;
    }

    /**
     * Getter for the name
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name
     * @param name the name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the role
     * @return the role of the player
     */
    public Role getRole() {
        return role;
    }

    /**
     * Setter for the role
     * @param role the role of the player
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Getter for the hand
     * @return the cards of the player in a HashSet
     */
    public HashSet<Card> getHand() {
        return hand;
    }

    /**
     * Setter for the hand
     * @param hand the cards of the player in a HashSet
     */
    public void setHand(HashSet<Card> hand) {
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
    public Deck getHandDeck() {
        return handDeck;
    }

    /**
     * Setter for the Deck of the player
     * @param handDeck the new Deck of the player
     */
    public void setHandDeck(Deck handDeck) {
        this.handDeck = handDeck;
    }
}

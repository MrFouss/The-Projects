package the_projects.model;

import the_projects.model.card.CardDeck;
import the_projects.model.card.EventCard;
import the_projects.model.card.PlayerCard;

/**
 * The representation of the PhD students.
 */
public class PhDStudent {

    /**
     * The name of the PhD Student.
     */
    private String name;

    /**
     * The role played by the PhD student.
     */
    private Role role;

    /**
     * The hand of cards.
     */
    private CardDeck<PlayerCard> cardHand;

    /**
     * The position where the student is.
     */
    private Room position;

    /**
     * The card stored by the student (only used by the TRELLO_ADEPT)
     */
    private EventCard extraEventCard;

    /**
     * Constructor with parameters.
     *
     * @param name the name of the student.
     * @param role the specialization of the PhD student.
     */
    public PhDStudent(String name, Role role) {
        this.name = name;
        this.role = role;
        this.cardHand = new CardDeck<>();
        this.position = null;
        this.extraEventCard = null;
    }

    /**
     * Gets the name of the student.
     *
     * @return the name of the student.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the role associated to the student.
     *
     * @return the role associated to the student.
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Gets the hand of cards of the student.
     *
     * @return the hand of cards.
     */
    public CardDeck getCards() {
        return this.cardHand;
    }

    /**
     * Gets the position of the student.
     *
     * @return the position of the student.
     */
    // TODO add to the class diagram
    public Room getPosition() {
        return this.position;
    }

    /**
     * Sets the position of the student.
     *
     * @param position the new position of the student.
     */
    // TODO add to the class diagram
    public void setPosition(Room position) {
        this.position = position;
    }

    /**
     * Gets the event card of the TRELLO_ADEPT.
     *
     * @return the event card of the TRELLO_ADEPT.
     */
    public EventCard getExtraEventCard() {
        return this.extraEventCard;
    }

    /**
     * Sets the event card of the TRELLO_ADEPT.
     *
     * @param card the event card of the TRELLO_ADEPT.
     */
    // TODO update the class diagram
    public void setExtraEventCard(EventCard card) {
        this.extraEventCard = card;
    }

}
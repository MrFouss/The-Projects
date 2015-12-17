package the_projects.model;

/**
 * 
 */
public class PhDStudent {

    /**
     * Default constructor
     */
    public PhDStudent() {
    }

    /**
     * 
     */
    private String name;


    /**
     * 
     */
    private CardDeck cardHand;

    /**
     * 
     */
    private Room position;

    /**
     * 
     */
    private EventCard extraEventCard;

    /**
     * 
     */
    private Role role;

    /**
     * @param name 
     * @param role
     */
    public void PhDStudent(String name, Role role) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Role getRole() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public CardDeck<PlayerCard> getCards() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public EventCard getExtraEventCard() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public EventCard setExtraEventCard() {
        // TODO implement here
        return null;
    }

}
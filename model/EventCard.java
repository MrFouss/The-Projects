package the_projects.model;

/**
 * 
 */
// TODO add javadoc
public class EventCard implements PlayerCard {

    /**
     *
     */
    // TODO add javadoc
    private Event type;

    /**
     * Default constructor
     */
    public EventCard() {
        this.type = null;
    }

    /**
     * @param event the event linked to the card.
     */
    // TODO add javadoc
    public EventCard(Event event) {
        this.type = event;
    }

    /**
     * @return the type of event linked to the card.
     */
    // TODO add javadoc
    public Event getEvent() {
        return this.type;
    }

}
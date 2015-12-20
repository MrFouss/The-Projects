package the_projects.model.card;

/**
 * Implementation of PlayerCard, containing an event.
 */
public class EventCard implements PlayerCard {

    /**
     * Element of the Event enumeration.
     */
    private Event type;

    /**
     * Constructor with an Event parameter.
     *
     * @param event the event linked to the card.
     */
    public EventCard(Event event) {
        this.type = event;
    }

    /**
     * Gets the event linked to the card.
     *
     * @return the type of event linked to the card.
     */
    public Event getEvent() {
        return this.type;
    }

}
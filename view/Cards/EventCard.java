package the_projects.view.Cards;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import the_projects.model.card.Event;
import the_projects.view.Cards.Card;

/**
 * Event version of the cards
 */
public class EventCard extends Card {
    /**
     * Simplified Constructor of Card
     * @param pane parent Pane
     * @param event corresponding event
     */
    public EventCard(Pane pane, Event event) {
        super(pane, event.eventToName(), event.eventToDescription(), Color.YELLOWGREEN);
    }


}

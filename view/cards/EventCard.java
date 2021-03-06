package the_projects.view.cards;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import the_projects.model.card.Event;

/**
 * Event version of the cards
 */
public class EventCard extends Card {
    /**
     * Simplified Constructor of Card
     * @param pane parent Pane
     * @param event corresponding event
     */
    public EventCard(Pane pane, Event event, Owner owner) {
        super(pane, event.eventToName(), event.eventToDescription(), Color.DARKORANGE.darker(), owner);

        textLabel.setFont(new Font(13));
    }


}

package the_projects.view.Cards;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import the_projects.view.Cards.Card;

/**
 * Party version of the cards
 */
public class PartyCard extends Card {
    /**
     * Simplified Constructor of Card
     * @param pane parent Pane
     */
    public PartyCard(Pane pane) {
        super(pane, "Soirée Étudiante",
                "1) Examens : \n" +
                        "la jauge d’urgence\n" +
                        "augmente d’un cran .\n" +
                        "2) Stress : \n" +
                        "la carte du dessous\n" +
                        "du paquet projet est\n" +
                        "piochée, on met 3 \n" +
                        "projets sur la salle\n" +
                        "correspondante.\n" +
                        "3) Intensification : \n" +
                        "les cartes de la défausse\n" +
                        "projet sont mélangées \n" +
                        "et placées sur le dessus\n" +
                        "du paquet projets.",
                Color.GREEN);
    }
}

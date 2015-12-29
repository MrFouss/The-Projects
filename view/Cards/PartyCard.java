package the_projects.view.Cards;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Party version of the cards
 */
public class PartyCard extends Card {
    /**
     * Simplified Constructor of Card
     * @param pane parent Pane
     */
    public PartyCard(Pane pane, Owner owner) {
        super(pane, "Soirée Étudiante",
                "1) Examens : la jauge\n " +
                        "d’urgence augmente \n" +
                        "d’un cran .\n" +
                        "2) Stress : la carte du dessous\n" +
                        "du paquet projet est piochée,\n" +
                        " on met 3 projets sur la salle\n" +
                        "correspondante.\n" +
                        "3) Intensification : les cartes\n" +
                        " de la défausse projet sont\n" +
                        " mélangées  et placées sur \n" +
                        "le dessus du paquet projets.\n",
                Color.GREEN, owner);
        textLabel.setFont(new Font(9));
    }
}

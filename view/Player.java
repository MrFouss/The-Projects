package the_projects.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import the_projects.model.Role;

import java.util.HashSet;

/**
 * The representation of a player
 */
public class Player {
    private String name;
    private Role role;
    private Pawn pawn;
    private HashSet<Card> hand;
    private StackPane handDeck;
    private Pane pane;
    private Color color;

    /**
     * Constructor without cards
     * @param name the name of the player
     * @param role the role of the player
     */
    public Player(String name, Role role) {
        this.name = name;
        this.role = role;
        color = roleToColor(role);
        hand = new HashSet<>();
        handDeck = null;
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0,1,1,.25)), new Stop(1, color.deriveColor(0,1,.5,.25))), new CornerRadii(5), new Insets(0))));

        Label description = new Label(roleToDescription(role));
        description.setTextFill(roleToColor(role).brighter());
        pane.getChildren().add(description);
        description = new Label(name);
        description.setTextFill(roleToColor(role).brighter());
        description.setLayoutX(350);
        description.setLayoutY(20);
        pane.getChildren().add(description);


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
    public HashSet<Card> getHand() {
        return hand;
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
     * Method associating a role to a color
     * @param role the role to associate
     * @return the color associated
     */
    public static Color roleToColor(Role role) {
        switch (role) {
            case TRELLO_ADEPT :
                return Color.PURPLE;
            case GROUP_LEADER :
                return Color.RED;
            case MENTOR :
                return Color.ORANGE;
            case INSTALLER :
                return Color.WHITE;
            case COFFEE_MAKER :
                return Color.SADDLEBROWN;
            case DAOUID :
                return Color.YELLOW;
            case HACKER :
                return Color.GREEN;
            default :
                return Color.GRAY;
        }
    }

    /**
     * Method associating a role its a description
     * @param role the role to associate
     * @return the description associated
     */
    public static String roleToDescription(Role role) {
        switch (role) {
            case TRELLO_ADEPT :
                return "L’Adepte de Trello : pour une action, il peut donner" +
                        "\nn’importe quelle carte de Salle à un autre joueur" +
                        "\nsitué dans la même salle. La carte échangée n’a pas" +
                        "\nà correspondre à la salle sur laquelle sont les pions.";
            case GROUP_LEADER :
                return "Le Leader : avec une action, le Leader peut déplacer \n" +
                        "le pion d’un autre joueur comme si c’était le sien, \n" +
                        "ou déplacer n’importe quel pion vers une salle où se \n" +
                        "trouve déjà un autre pion.";
            case MENTOR :
                return "Mentor: pour une action Rendre un projet, il retire\n" +
                        "tous les projets d’une salle. Si une UV est\n" +
                        "masterisée, il retire automatiquement tous les\n" +
                        "cubes celle ci des salles qu'il visite.";
            case INSTALLER :
                return "L’Installer : pour une action, il peut construire \n" +
                        "une salle de TP dans sa salle actuelle sans \n" +
                        "défausser de carte, ou se déplacer d’une salle de TP \n" +
                        "vers n’importe quelle autre salle de son choix.";
            case COFFEE_MAKER :
                return "L’Addict au Café : l’Addict au Café empêche le \n" +
                        "placement de cubes de projet ou l’apparition de \n" +
                        "burn-out dans la salle où il est et dans les salles \n" +
                        "adjacentes.";
            case DAOUID :
                return "Daouid : il n’a besoin que de 4 (et non 5) cartes \n" +
                        "Salle de la même UV pour masteriser une UV.";
            case HACKER :
                return "Le Hacker : pour une action, il peut choisir une \n" +
                        "carte Évènement dans la défausse. Il la met de côté \n" +
                        "(1 à la fois) et peut la jouer quand il veut, avant \n" +
                        "de la retirer du jeu.";
            default :
                return "You shouldn't see this, please make a bug report(non-existent role description)";
        }
    }
}

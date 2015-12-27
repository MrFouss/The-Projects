package the_projects.model;

import javafx.scene.paint.Color;

/**
 * The enumeration containing all PhD Student's roles.
 */
public enum Role {
    TRELLO_ADEPT,
    GROUP_LEADER,
    MENTOR,
    INSTALLER,
    COFFEE_MAKER,
    DAOUID,
    HACKER;

    /**
     * Method associating a role to a color
     * @return the color associated
     */
    public Color roleToColor() {
        switch (this) {
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
     * @return the description associated
     */
    public String roleToDescription() {
        switch (this) {
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

    /**
     * Method to associate a name to its role
     * @param name the name to associate
     * @return the associated role
     */
    public static Role nameToRole(String name) {
        switch (name) {
            case "Adepte de Trello":
                return Role.TRELLO_ADEPT;
            case "Leader":
                return Role.GROUP_LEADER;
            case "Mentor":
                return Role.MENTOR;
            case "Installer":
                return Role.INSTALLER;
            case "Addict au Café":
                return Role.COFFEE_MAKER;
            case "Daouid":
                return Role.DAOUID;
            case "Hacker":
                return Role.HACKER;
            default:
                return null;
        }
    }
}
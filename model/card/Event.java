package the_projects.model.card;

/**
 * Enumeration containing the different types of event.
 */
public enum Event {
    TIMEOUT,
    GRANT_IN_AID,
    ON_DEMAND_TELEPORTATION,
    ENERGY_DRINK,
    HOLIDAYS;

    /**
     * Method to associate an Event to its name
     * @return its name
     */
    public String eventToName() {
        switch (this) {
            case TIMEOUT:
                return "Temps mort";
            case GRANT_IN_AID:
                return "Subvention \nde l'état";
            case ON_DEMAND_TELEPORTATION:
                return "Téléportation \nà la demande";
            case ENERGY_DRINK:
                return "Boisson \nénérgisante";
            case HOLIDAYS:
                return "Vacances";
            default:
                return "";
        }
    }

    /**
     * Method to associate a name to its event.
     *
     * @param name the name to associate
     * @return the associated event
     */
    public static Event nameToEvent(String name) {
        for (Event event : Event.values()) {
            if (event.eventToName().equals(name))
                return event;
        }
        return null;
    }


    /**
     * Method to associate an Event to its description
     * @return its description
     */
    public String eventToDescription() {
        switch (this) {
            case TIMEOUT:
                return "Ne faites pas \n" +
                        "la prochaine phase \n" +
                        "de propagation.";
            case GRANT_IN_AID:
                return "Placez gratuitement \n" +
                        "une salle de TP sur \n" +
                        "la salle de votre \n" +
                        "choix.";
            case ON_DEMAND_TELEPORTATION:
                return "Déplacez un pion \n" +
                        "quelconque sur \n" +
                        "la salle de votre \n" +
                        "choix.";
            case ENERGY_DRINK:
                return "Gagnez 2 actions \n" +
                        "supplémentaires \n" +
                        "ce tour-ci.";
            case HOLIDAYS:
                return "Une seule carte \n" +
                        "Projet est tirée \n" +
                        "durant la phase \n" +
                        "de propagation \n" +
                        "jusqu’au début\n" +
                        "du prochain tour \n" +
                        "du joueur à qui \n" +
                        "est assignée \n" +
                        "cette carte.";
            default:
                return "";
        }
    }
}
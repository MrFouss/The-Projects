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
    public String eventToName() {//TODO check names
        switch (this) {
            case TIMEOUT:
                return "Temps mort";
            case GRANT_IN_AID:
                return "Subvention de l'état";
            case ON_DEMAND_TELEPORTATION:
                return "Téléportation à la demande";
            case ENERGY_DRINK:
                return "Boisson énérgisante";
            case HOLIDAYS:
                return "Vacances";
            default:
                return "";
        }
    }

    /**
     * Method to associate an Event to its description
     * @return its description
     */
    public String eventToDescription() {//TODO check descriptions
        switch (this) {
            case TIMEOUT:
                return "La prochaine phase de \n" +
                        "propagation est ignorée";
            case GRANT_IN_AID:
                return "Construisez une salle \n" +
                        "de TP où vous voulez";
            case ON_DEMAND_TELEPORTATION:
                return "Téléporez vous où \n" +
                        "vous voulez";
            case ENERGY_DRINK:
                return "Gagnez 2 actions \n" +
                        "supplémentaires \n" +
                        "pour ce tour";
            case HOLIDAYS:
                return "Jusqu'à votre prochain\n" +
                        "tour, une seule carte\n" +
                        "de projet est tirée \n" +
                        "pendant la phasse de \n" +
                        "propagation.";
            default:
                return "";
        }
    }
}
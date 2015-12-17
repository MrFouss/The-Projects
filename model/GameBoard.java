package the_projects.model;

import java.util.*;

/**
 * 
 */
public class GameBoard {

    /**
     * Default constructor
     */
    public GameBoard() {
    }

    /**
     * 
     */
    private int emergencyGauge;

    /**
     * 
     */
    private int burnOutGauge;

    /**
     * 
     */
    private int labRoomAmount;


    /**
     * 
     */
    private CardDeck projectDeck;

    /**
     * 
     */
    private CardDeck projectDiscard;

    /**
     * 
     */
    private CardDeck playerDeck;

    /**
     * 
     */
    private CardDeck playerDiscard;

    /**
     * 
     */
    private PhDStudent currentPlayer;

    /**
     * 
     */
    private Room[] roomList;

    /**
     * 
     */
    private PhDStudent playerList;

    /**
     * 
     */
    private Course[] courseTab;

    /**
     * @param players 
     * @param courses
     */
    public void GameBoard(List<String> players, List<String> courses) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<PhDStudent> getPlayers() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public PhDStudent getCurrentPlayer() {
        // TODO implement here
        return null;
    }

    /**
     * 
     */
    public void nextPlayer() {
        // TODO implement here
    }

    /**
     * @return
     */
    public CardDeck<ProjectCard> getProjectDeck() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public CardDeck<ProjectCard> getProjectDiscard() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public CardDeck<PlayerCard> getPlayerDeck() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public CardDeck<PlayerCard> getPlayerDiscard() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Course> getCourses() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Room> getRooms() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Room> getLabRooms() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getEmergencyValue() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int getBurnOutValue() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public void increaseBurnOutGauge() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void increaseEmergencyGauge() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getLabRoomAmount() {
        // TODO implement here
        return 0;
    }

    /**
     * @param amount 
     * @return
     */
    public void setLabRoomAmount(int amount) {
        // TODO implement here
        return null;
    }

}
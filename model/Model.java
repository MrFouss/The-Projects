package the_projects.model;

import the_projects.model.card.CardDeck;
import the_projects.model.card.PlayerCard;
import the_projects.model.card.ProjectCard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * The class managing the whole game board.
 */
public class Model {

    /**
     * The scale used to compute the emergency level.
     */
    private int emergencyGauge;

    /**
     * The scale used to compute the burn-out level.
     */
    private int burnOutGauge;

    // TODO update the class diagram (-labRoomAmount)

    /**
     * The project deck.
     */
    private CardDeck<ProjectCard> projectDeck;

    /**
     * The discard pile of the project deck.
     */
    private CardDeck<ProjectCard> projectDiscard;

    /**
     * The player deck.
     */
    private CardDeck<PlayerCard> playerDeck;

    /**
     * The discard pile of the player deck.
     */
    private CardDeck<PlayerCard> playerDiscard;

    /**
     * An iterator on the current player.
     */
    private ListIterator<PhDStudent> currentPlayer;

    /**
     * The list of students.
     */
    private LinkedList<PhDStudent> playerList;

    /**
     * The list of rooms.
     */
    // TODO update the class diagram
    private Room[] roomTab;

    /**
     * The array containing all courses.
     */
    private Course[] courseTab;

    /**
     * Constructor with the list of the players' names and of the courses' names.
     *
     * @param players the names of the players.
     * @param roles the roles of the players.
     * @param courses the names of the courses.
     * @param difficulty the level of difficulty.
     */
    public Model(LinkedList<String> players, LinkedList<Role> roles, LinkedList<String> courses, int difficulty) {
        // TODO implement here
    }

    /**
     * Gets the list of players.
     *
     * @return the list of players.
     */
    public LinkedList<PhDStudent> getPlayers() {
        return this.playerList;
    }

    /**
     * Gets the current player.
     *
     * @return the current player.
     */
    public PhDStudent getCurrentPlayer() {
        return this.playerList.get(this.currentPlayer.nextIndex());
    }

    /**
     * Move the iterator currentPlayer to the next player.
     */
    public void nextPlayer() {
        if(!this.currentPlayer.hasNext()){
            this.currentPlayer = this.playerList.listIterator(0);
        } else {
            this.currentPlayer.next();
        }
    }

    /**
     * Gets the project deck.
     *
     * @return the project deck.
     */
    // TODO update the class diagram
    public CardDeck getProjectDeck() {
        return this.projectDeck;
    }

    /**
     * Gets the discard pile of the project deck.
     *
     * @return the discard pile of the project deck.
     */
    // TODO update the class diagram
    public CardDeck getProjectDiscard() {
        return this.projectDiscard;
    }

    /**
     * Gets the player deck.
     *
     * @return the player deck.
     */
    // TODO update the class diagram
    public CardDeck getPlayerDeck() {
        return this.playerDeck;
    }

    /**
     * Gets the discard pile of the player deck.
     *
     * @return the discard pile of the player deck.
     */
    // TODO update the class diagram
    public CardDeck getPlayerDiscard() {
        return this.playerDiscard;
    }

    /**
     * Gets the array containing all courses.
     *
     * @return the array containing all courses.
     */
    public Course[] getCourses() {
        return this.courseTab;
    }

    /**
     * Gets the array containing all rooms.
     *
     * @return the array containing all rooms.
     */
    public Room[] getRooms() {
        return this.roomTab;
    }

    /**
     * Gets a list containing all lab rooms.
     *
     * @return the list containing all lab rooms.
     */
    public LinkedList<Room> getLabRooms() {
        LinkedList<Room> labRoomList = new LinkedList<>();
        for (Room room : this.getRooms()) {
            if (room.isLabRoom()) {
                labRoomList.add(room);
            }
        }
        return labRoomList;
    }

    /**
     * Gets the value of the emergency gauge.
     *
     * @return the value of the emergency gauge.
     */
    public int getEmergencyValue() {
        return this.emergencyGauge;
    }

    /**
     * Gets the value of the burn-out gauge.
     *
     * @return the value of the burn-out gauge.
     */
    public int getBurnOutValue() {
        return this.burnOutGauge;
    }

    /**
     * Increases the value of the burn-out gauge.
     */
    public void increaseBurnOutGauge() {
        this.burnOutGauge++;
    }

    /**
     * Increases the value of the emergency gauge.
     */
    public void increaseEmergencyGauge() {
        this.emergencyGauge++;
    }

    /**
     * Gets the amount of lab rooms.
     *
     * @return the amount of lab rooms.
     */
    public int getLabRoomAmount() {
        return this.getLabRooms().size();
    }

    // TODO update the class diagram (-setLabRoomAmount)

    /**
     * Gets the list of rooms reachable by a player, considering its remaining actions.
     *
     * @param playerRole the role of the moving player.
     * @param remainingMoves the amount of actions remaining.
     * @return the hash map containing each room reachable linked to the amount of actions needed to go to the room.
     */
    // TODO add to the class diagram
    public HashMap<String, Integer> reachableRooms(Role playerRole, int remainingMoves) {
        LinkedList<Room> stack = new LinkedList<>();
        HashMap<String, Integer> map = new HashMap<>();

        String sourceName;

        for(PhDStudent student : this.getPlayers()) {
            if(student.getRole().equals(playerRole)) {
                sourceName = student.getPosition().getName();

                map.put(sourceName, 0);
                stack.push(student.getPosition());

                if(this.getCurrentPlayer().getRole() == Role.GROUP_LEADER) {
                    for(PhDStudent otherStudent : this.getPlayers()) {
                        if(!otherStudent.equals(student)) {
                            map.put(otherStudent.getPosition().getName(), 1);
                            stack.push(otherStudent.getPosition());
                        }
                    }
                }
            }
        }

        while(!stack.isEmpty()) {
            Room tmp = stack.pop();
            // if the neighbours of tmp are reachable
            if(map.get(tmp.getName()) + 1 <= remainingMoves) {
                for(Room neighbour : tmp.getNeighbours()) {
                    // if a shortest path from source has been found or if neighbour is not yet in
                    if((!map.containsKey(neighbour.getName())) || (map.get(tmp.getName()) + 1 < map.get(neighbour.getName()))) {
                        // if neighbour is already inside map
                        if(map.containsKey(neighbour.getName())) {
                            map.remove(neighbour.getName());
                        }
                        map.put(neighbour.getName(), map.get(tmp.getName()) + 1);
                        stack.push(neighbour);
                    }
                }
            }
        }

        return map;
    }

    // TODO javadoc
    public LinkedList<String> shortestPath(String start, String end) {
        return null;
    }

    /**
     * Adds a new lab room to the game.
     *
     * @param newRoom the name of the new lab room.
     */
    public void addLabRoom(String newRoom) {
        for(Room room : this.roomTab) {
            if(room.getName().equals(newRoom)) {
                for(Room labRoom : this.getLabRooms()) {
                    room.getNeighbours().add(labRoom);
                    labRoom.getNeighbours().add(room);
                }
                room.toggleLabRoom();
            }
        }
    }

}
package the_projects.model;

import the_projects.model.card.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * The class managing the whole game board.
 */
public class Model {

    /**
     * The scale used to compute the emergency level.
     * Can go from 0 to 6 (0-2 -> 2 ; 3-4 -> 3 ; 5-6 -> 4).
     */
    private int emergencyGauge;

    /**
     * The scale used to compute the burn-out level.
     * Can go from 0 to 8, if there are 8 burn-outs, the game is over.
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
     * @param courseNames the names of the courses.
     * @param difficulty the level of difficulty.
     */
    public Model(HashMap<String, Role> players, LinkedList<String> courseNames, int difficulty) {

        this.emergencyGauge = 0;
        this.burnOutGauge = 0;

        this.projectDiscard = new CardDeck<>();
        this.playerDiscard = new CardDeck<>();

        this.courseTab = new Course[4];
        int i = 0;
        for(String courseName : courseNames) {
            courseTab[i++] = new Course(courseName);
        }

        // Adds each room to roomTab
        this.roomTab = new Room[48];
        try {
            List<String> newRoomsList = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/the_projects/resources/rooms.csv"));
            i = 0;
            for (String line : newRoomsList) {
                String[] vars = line.split(",");
                roomTab[i] = new Room(vars[1], courseTab[Integer.parseInt(vars[0])], courseTab);
                if(vars[1].equals("B402")) {
                    roomTab[i].toggleLabRoom();
                }
                ++i;
            }
        }
        catch (NullPointerException e) {
            System.out.println("rooms.csv file not found (pointer)");
        }
        catch (IOException e) {
            System.out.println("rooms.csv file not found");
        }

        // Adds the neighbours to each room
        try {
            List<String> neighboursList = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/the_projects/resources/corridors.csv"));
            HashMap<String, Room> mapRooms = new HashMap<>();
            for(Room room : roomTab) {
                mapRooms.put(room.getName(), room);
            }
            for (String line : neighboursList) {
                String[] vars = line.split(",");
                mapRooms.get(vars[0]).getNeighbours().add(mapRooms.get(vars[1]));
                mapRooms.get(vars[1]).getNeighbours().add(mapRooms.get(vars[0]));
            }
        }
        catch (NullPointerException e) {
            System.out.println("rooms.csv file not found (pointer)");
        }
        catch (IOException e) {
            System.out.println("rooms.csv file not found");
        }

        this.projectDeck = new CardDeck<>();
        this.playerDeck = new CardDeck<>();

        for(i = 0 ; i < 48 ; ++i) {
            this.projectDeck.addCard(new ProjectCard(roomTab[i]));
            this.playerDeck.addCard(new RoomCard(roomTab[i]));
        }

        for(Event event : Event.values()) {
            this.playerDeck.addCard(new EventCard(event));
        }

        this.projectDeck.shuffle();
        this.playerDeck.shuffle();

        int deckSize = this.playerDeck.getSize();
        CardDeck<PlayerCard> finalDeck = new CardDeck<>();
        for(i = 0 ; i < 3+difficulty ; ++i) {
            CardDeck<PlayerCard> tmpDeck = new CardDeck<>();
            for(int j = 0 ; j <= deckSize/(4+difficulty) ; ++j) {
                tmpDeck.addCard(this.playerDeck.drawFirst());
            }
            tmpDeck.addCard(new PartyCard());
            tmpDeck.shuffle();
            finalDeck.addCardsOnTop(tmpDeck);
        }
        this.playerDeck.addCard(new PartyCard());
        this.playerDeck.shuffle();
        finalDeck.addCardsOnTop(this.playerDeck);
        this.playerDeck = finalDeck;

        this.playerList = new LinkedList<>();
        for(String name : players.keySet()) {
            PhDStudent tmpStudent;
            if(players.get(name) == Role.RANDOM) {
                Role tmpRole;
                Boolean isInside;
                do {
                    tmpRole = Role.random();
                    isInside = false;
                    for(PhDStudent student : this.playerList) {
                        if (student.getRole() == tmpRole) {
                            isInside = true;
                        }
                    }
                } while(isInside);
                tmpStudent = new PhDStudent(name, tmpRole);
            } else {
                tmpStudent = new PhDStudent(name, players.get(name));
            }

            for(Room room : roomTab) {
                if(room.getName().equals("B402")) {
                    tmpStudent.setPosition(room);
                }
            }

            this.playerList.add(tmpStudent);
        }

        this.currentPlayer = this.playerList.listIterator();
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
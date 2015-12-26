package the_projects.view;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import the_projects.controller.ErrorEvent;
import the_projects.controller.ViewListener;
import the_projects.model.Course;
import the_projects.model.Model;
import the_projects.model.PhDStudent;
import the_projects.model.Role;
import the_projects.model.card.Event;

import java.util.HashMap;
import java.util.HashSet;

/**
 *  TODO complete
 */
public class View extends Application {
    private Model model;
    private MainMenu mainMenu;
    private Board board;
    private Stage stage;
    private final HashSet<ViewListener> listeners = new HashSet<>();

    public View() {
        mainMenu = new MainMenu(new Group());
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setMinHeight(450);
        stage.setMinWidth(800);
        stage.setMaximized(true);
        stage.show();
        //TODO remove next line
        setBoard("AG44", "MI41", "SI20", "LO43", new Player("TOTO", Role.TRELLO_ADEPT), new Player("TATA", Role.DAOUID), new Player("TUTU", Role.GROUP_LEADER), new Player("TITI", Role.HACKER));
    }

    /**
     * Method to generate a new board
     */
    public void displayGameBoard(Model model){
        this.model = model;
        Course[] courses = model.getCourses();
        Player[] players = new Player[model.getPlayers().size()];
        int i = 0;
        for (PhDStudent phDStudent : model.getPlayers())
                players[i++] = new Player(phDStudent.getName(), phDStudent.getRole());
        setBoard(courses[0].getName(), courses[0].getName(), courses[0].getName(), courses[0].getName(), players);
    }

    /**
     * Method to generate a new board
     *
     * @param UV1     the name of the first UV
     * @param UV2     the name of the second UV
     * @param UV3     the name of the third UV
     * @param UV4     the name of the fourth UV
     * @param players the list of the players (please use the format "new Player(String name, Role role)"
     */
    private void setBoard(String UV1, String UV2, String UV3, String UV4, Player... players) {
        board = new Board(new Group(), this, UV1, UV2, UV3, UV4, players);
        stage.setScene(board);
    }

    /**
     * Method too add a viewListener
     * @param listener the view listener to add
     */
    public void addListener(ViewListener listener) {
        listeners.add(listener);
    }

    /**
     * Method to display a new settings menu
     */
    public void displaySetting() {
        stage.setScene(mainMenu);
    }

    public void displayInvalidSetting(ErrorEvent errorEvent) {
        //TODO implement
    }

    /**
     * Method to display the reachable rooms
     * @param rooms the name of the reachable rooms with the number of actions needed to reach them
     */
    public void displayReachablePlaces(HashMap<String,Integer> rooms) {
        board.reachableRooms(rooms);
    }

    /**
     * Method to move a pawn along some rooms
     *
     * @param role  the role corresponding to the pawn
     * @param rooms the names of the rooms on the pawns way
     */
    public void displayMovePawn(Role role, String... rooms) {
        board.movePawn(role, rooms);
    }

    /**
     * Method to transform a room to a Lab room
     *
     * @param roomName the name of the room
     */
    public void displaySetRoomToLab(String roomName) {
        board.setRoomToLab(roomName);
    }

    /**
     * Method to add a project to a room given its name and the type of project
     *
     * @param roomName     the name of the room
     * @param projectIndex the index of the project [0-3]
     */
    public void displayAddProjectToRoom(String roomName, int projectIndex) {
        board.addProjectToRoom(roomName, projectIndex);
    }

    /**
     * Method to remove a project from a room given its name and the type of project
     *
     * @param roomName     the name of the room
     * @param projectIndex the index of the project
     */
    public void displayRemoveProjectFromRoom(String roomName, int projectIndex) {
        board.removeProjectFromRoom(roomName, projectIndex);
    }

    /**
     * Method to increase the propagation gauge
     */
    public void displayIncreasePropagationGauge() {
        board.increasePropagationGauge();
    }

    /**
     * Method to increase the burn-out gauge
     */
    public void displayIncreaseBurnOutGauge() {
        board.increaseBurnOutGauge();
    }

    /**
     * Method to make a course mastered
     * @param projectIndex the index of the course
     */
    public void displayToMastered(int projectIndex) {
        board.toMastered(projectIndex);
    }

    /**
     * Method to make a course eradicated
     * @param projectIndex the index of the course
     */
    public void displayToEradicated(int projectIndex) {
        board.toEradicated(projectIndex);
    }

    /**
     * Set a course to mastered state
     * @param courseIndex the index of the course
     */
    public void displayCourseMastered(int courseIndex) {
        board.setCourseMastered(courseIndex);
    }

    /**
     * Set a course to Eradicated state
     * @param courseIndex the index of the course
     */
    public void displayCourseEradicated(int courseIndex) {
        board.setCourseEradicated(courseIndex);
    }

    /**
     * Method to draw two room cards from the player deck
     *
     * @param roomCard1 the name of the room of the first room card to draw
     * @param roomCard2 the name of the room of the second room card to draw
     */
    public void displayDrawPlayerCards(String roomCard1, String roomCard2) {
        board.drawPlayerCards(roomCard1, roomCard2);
    }

    /**
     * Method to draw a room card and an event card from the player deck
     *
     * @param roomCard  the name of the room of the room card to draw
     * @param eventCard the event of the event card to draw
     */
    public void displayDrawPlayerCards(String roomCard, Event eventCard) {
        board.drawPlayerCards(roomCard, eventCard);
    }

    /**
     * Method to draw two event cards from the player deck
     *
     * @param eventCard1 the event of the first event card to draw
     * @param eventCard2 the event of the second event card to draw
     */
    public void displayDrawPlayerCards(Event eventCard1, Event eventCard2) {
        board.drawPlayerCards(eventCard1, eventCard2);
    }

    /**
     * Method to draw a room card and a student party card from the player deck
     *
     * @param roomCard the name of the room of the room card to draw
     */
    public void displayDrawPlayerCards(String roomCard) {
        board.drawPlayerCards(roomCard);
    }

    /**
     * Method to draw an event card and a student party card from the player deck
     *
     * @param eventCard the event of the event card to draw
     */
    public void displayDrawPlayerCards(Event eventCard) {
        board.drawPlayerCards(eventCard);
    }

    /**
     * Method to draw two student party cards from the player deck
     */
    public void displayDrawPlayerCards() {
        board.drawPlayerCards();
    }
    /**
     * Method to discard a room card to the player discard
     * @param roomCard the name of the room of the room card
     */
    public void displayDiscardPlayerCard(String roomCard) {
        board.discardPlayerCard(roomCard);
    }

    /**
     * Method to discard an event card to the player discard
     * @param eventCard the event of the event card
     */
    public void displayDiscardPlayerCard(Event eventCard) {
        board.discardPlayerCard(eventCard);
    }

    /**
     * Method to make a partyCard disappear
     */
    public void displayDiscardPartyCard() {
        board.discardPartyCard();
    }

    /**
     * Method to draw project cards from the projects deck
     * @param roomNames the names of the projects to draw
     */
    public void displayDrawProjectCards(String... roomNames) {
        board.drawProjectCards(roomNames);
    }

    /**
     * Method to discard a project card to the project discard
     * @param roomName the name of the room of the project
     */
    public void displayDiscardProjectCard(String roomName) {
        board.discardProjectCard(roomName);
    }

    /**
     * Method to give a room card to a player
     * @param roomName the name of the room of the room card
     * @param role the role of the player
     */
    public void displayGiveCardToPlayer(String roomName, Role role) {
        board.giveCardToPlayer(roomName, role);
    }

    /**
     * Method to give an event card to a player
     * @param event the event of the event card
     * @param role the role of the player
     */
    public void displayGiveCardToPlayer(Event event, Role role) {
        board.giveCardToPlayer(event, role);
    }

    /**
     * Method to take a room card from a player
     * @param roomName the name of the room of the room card
     * @param role the role of the player
     */
    public void displayDiscardCardFromPlayer(String roomName, Role role) {
        board.discardCardFromPlayer(roomName, role);
    }

    /**
     * Method to take an event card from a player
     * @param event the event of the event card
     * @param role the role of the player
     */
    public void displayDiscardCardFromPlayer(Event event, Role role) {
        board.discardCardFromPlayer(event, role);
    }

    /**
     * Method to draw event cards from the player discard
     * @param events the events of the event cards to draw
     */
    public void displayEventCardsFromDiscard(Event... events) {
        board.drawEventCardsFromDiscard(events);
    }


    public void firePlaceClicked(String name) {//TODO use
        listeners.forEach(event->event.placeClicked(name));
    }

    public void firePawnCLicked(Role role) {//TODO use
        listeners.forEach(event->event.pawnCLicked(role));
    }

    public void fireSettingValidationButtonCLicked() {//TODO use
        listeners.forEach(ViewListener::settingValidationButtonCLicked);
    }

    public void fireMoveButtonClicked() {
        listeners.forEach(ViewListener::moveButtonClicked);
        //TODO erase this test line
        displayIncreasePropagationGauge();
    }

    public void fireRemoveProjectButtonClicked() {
        listeners.forEach(ViewListener::removeProjectButtonClicked);
        //TODO erase this test line
        displayIncreaseBurnOutGauge();
    }

    public void fireShareKnowledgeButtonCLicked() {
        listeners.forEach(ViewListener::shareKnowledgeButtonCLicked);
        //TODO erase these test lines
        displayToMastered(0);
        displayToMastered(1);
    }

    public void fireUseCardButtonClicked() {
        listeners.forEach(ViewListener::useCardButtonClicked);
        //TODO erase this test line
        displayToEradicated(0);
    }

    public void fireMasterButtonCliked() {
        listeners.forEach(ViewListener::masterButtonCliked);
    }

    public void fireBuildTPButtonClicked() {
        listeners.forEach(ViewListener::buildTPButtonClicked);
    }

    public void fireHackButtonCliked() {
        listeners.forEach(ViewListener::hackButtonCliked);
    }

    public void fireEndOfStageButtonClicked() {
        listeners.forEach(ViewListener::endOfStageButtonClicked);
    }
}

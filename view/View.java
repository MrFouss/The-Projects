package the_projects.view;


import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import the_projects.controller.ErrorEvent;
import the_projects.controller.ViewListener;
import the_projects.model.Course;
import the_projects.model.Model;
import the_projects.model.PhDStudent;
import the_projects.model.Role;
import the_projects.model.card.Event;
import the_projects.view.Cards.Owner;

import java.util.*;

/**
 *  TODO complete
 */
public class View extends Stage {
    private Model model;
    private MainMenu mainMenu;
    private Board board;
    private final HashSet<ViewListener> listeners = new HashSet<>();

    public View() {
        setTitle("The Projects");
        setMinHeight(450);
        setMinWidth(800);
        mainMenu = new MainMenu(new Group(), this);
    }


    /**
     * Method to generate a new board
     */
    public void displayGameBoard(Model model){
        this.model = model;
        Course[] courses = this.model.getCourses();
        Player[] players = new Player[this.model.getPlayers().size()];
        int i = 0;
        for (PhDStudent phDStudent : this.model.getPlayers())
                players[i++] = new Player(phDStudent.getName(), phDStudent.getRole());
        Platform.runLater(() -> setBoard(courses[0].getName(), courses[1].getName(), courses[2].getName(), courses[3].getName(), players));
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
    public void setBoard(String UV1, String UV2, String UV3, String UV4, Player... players) {
        setHeight(getWidth()*9/16);
        board = new Board(new Group(), this, UV1, UV2, UV3, UV4, players);
        setScene(board);
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
        Platform.runLater(() -> setSettings());
    }

    private void setSettings() {
        setScene(mainMenu);
        setHeight(50);
    }


    /**
     * Method to display the reachable rooms
     * @param rooms the name of the reachable rooms with the number of actions needed to reach them
     */
    public void displayReachableRooms(HashMap<String,Integer> rooms) {
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
        board.toMastered(courseIndex);
    }

    /**
     * Set a course to Eradicated state
     * @param courseIndex the index of the course
     */
    public void displayCourseEradicated(int courseIndex) {
        board.toEradicated(courseIndex);
    }

    public void fireRoomClicked(String name) {
        listeners.forEach(listener -> listener.placeClicked(name));
    }

    public void firePawnCLicked(Role role) {//TODO use
        listeners.forEach(event->event.pawnClicked(role));
    }

    public void fireMoveButtonClicked() {
        listeners.forEach(ViewListener::moveButtonClicked);
    }

    public void fireRemoveProjectButtonClicked() {
        listeners.forEach(ViewListener::removeProjectButtonClicked);
    }

    public void fireShareKnowledgeButtonCLicked() {
        listeners.forEach(ViewListener::shareKnowledgeButtonClicked);
    }

    public void fireUseCardButtonClicked() {
        listeners.forEach(ViewListener::useCardButtonClicked);
    }

    public void fireMasterButtonClicked() {
        listeners.forEach(ViewListener::masterButtonClicked);
    }

    public void fireBuildTPButtonClicked() {
        listeners.forEach(ViewListener::buildTPButtonClicked);
    }

    public void fireHackButtonClicked() {
        listeners.forEach(ViewListener::hackButtonClicked);
    }

    public void fireEndOfStageButtonClicked() {
        listeners.forEach(ViewListener::endOfStageButtonClicked);
    }

    public void fireCardClicked(String title) {
        board.titleToFireCardClicked(title);
    }

    public void fireRoomCardClicked(String roomName) {
        listeners.forEach(event -> event.roomCardClicked(roomName));
    }

    public void fireEventCardClicked(Event event) {
        listeners.forEach(e -> e.eventCardClicked(event));
    }

    protected void fireGiveUpButtonClicked() {
        listeners.forEach(ViewListener::giveUpButtonClicked);
    }

    public void displayInfoMessage(String s) {
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle("The-Projects");
        popup.setHeaderText(null);
        popup.setContentText(s);

        popup.showAndWait();
    }


    public LinkedList<String> getPlayerNames() {
        return mainMenu.getPlayerNames();
    }

    public LinkedList<String> getUVNames() {
        return mainMenu.getUVNames();
    }

    public int getDifficulty() {
        return mainMenu.getDifficulty();
    }

    public LinkedList<Role> getRoles() {
        return mainMenu.getRoles();
    }

    public void clean() {
        board.clean();
    }


    public void displayValidationMessage(String s) {
        Alert message = new Alert(Alert.AlertType.CONFIRMATION);
        message.setTitle("The-Projects");
        message.setHeaderText(null);
        message.setContentText(s);
        Optional<ButtonType> choice = message.showAndWait();
        if (choice.get() == ButtonType.OK) {
            listeners.forEach(ViewListener::YesButtonClicked);
        }else {
            listeners.forEach(ViewListener::NoButtonClicked);
        }
    }

    public void displayMessage(String s) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("The-Projects");
        message.setHeaderText(null);
        message.setContentText(s);
        message.show();
    }

    public void fireSettingValidationButtonClicked() {
        listeners.forEach(ViewListener::settingValidationButtonClicked);
    }

    public void displayDrawCards(Owner actualOwner, Owner newOwner, boolean clickable, ArrayList<String> roomNamesOfRoomCards, ArrayList<Event> eventsOfEventCards, int numberOfPartyCards) {
        board.drawCards(actualOwner, newOwner, clickable, roomNamesOfRoomCards, eventsOfEventCards, numberOfPartyCards);
    }
    public void displayDiscardCards() {
        board.discardCards();
    }
    public void displayDiscardCard(Owner newOwner, String roomNameOfRoomCard) {
        board.discardCard(newOwner, roomNameOfRoomCard);
    }
    public void displayDiscardCard(Owner newOwner, Event eventOfEventCard) {
        board.discardCard(newOwner, eventOfEventCard);
    }


}

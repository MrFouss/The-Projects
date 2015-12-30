package the_projects.view;


import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import the_projects.controller.ViewListener;
import the_projects.model.Course;
import the_projects.model.Model;
import the_projects.model.PhDStudent;
import the_projects.model.Role;
import the_projects.model.card.Event;
import the_projects.view.cards.Owner;

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
        setBoard(courses[0].getName(), courses[1].getName(), courses[2].getName(), courses[3].getName(), players);
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
        Platform.runLater(() -> board = new Board(new Group(), this, UV1, UV2, UV3, UV4, players));
        Platform.runLater(() -> setScene(board));
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
        Platform.runLater(() -> setScene(mainMenu));
        setHeight(50);
    }


    /**
     * Method to display the reachable rooms
     * @param rooms the name of the reachable rooms with the number of actions needed to reach them
     */
    public void displayReachableRooms(HashMap<String,Integer> rooms) {
        Platform.runLater(() -> board.reachableRooms(rooms));
    }

    /**
     * Method to move a pawn along some rooms
     *
     * @param role  the role corresponding to the pawn
     * @param rooms the names of the rooms on the pawns way
     */
    public void displayMovePawn(Role role, String... rooms) {
        Platform.runLater(() -> board.movePawn(role, rooms));
    }

    /**
     * Method to transform a room to a Lab room
     *
     * @param roomName the name of the room
     */
    public void displaySetRoomToLab(String roomName) {
        Platform.runLater(() -> board.setRoomToLab(roomName));
    }

    /**
     * Method to add a project to a room given its name and the type of project
     *
     * @param roomName     the name of the room
     * @param projectIndex the index of the project [0-3]
     */
    public void displayAddProjectToRoom(String roomName, int projectIndex) {
        Platform.runLater(() -> board.addProjectToRoom(roomName, projectIndex));
    }

    /**
     * Method to remove a project from a room given its name and the type of project
     *
     * @param roomName     the name of the room
     * @param projectIndex the index of the project
     */
    public void displayRemoveProjectFromRoom(String roomName, int projectIndex) {
        Platform.runLater(() -> board.removeProjectFromRoom(roomName, projectIndex));
    }

    /**
     * Method to increase the propagation gauge
     */
    public void displayIncreasePropagationGauge() {
        Platform.runLater(() -> board.increasePropagationGauge());
    }

    /**
     * Method to increase the burn-out gauge
     */
    public void displayIncreaseBurnOutGauge() {
        Platform.runLater(() -> board.increaseBurnOutGauge());
    }

    /**
     * Method to make a course mastered
     * @param projectIndex the index of the course
     */
    public void displayToMastered(int projectIndex) {
        Platform.runLater(() -> board.toMastered(projectIndex));
    }

    /**
     * Method to make a course eradicated
     * @param projectIndex the index of the course
     */
    public void displayToEradicated(int projectIndex) {
        Platform.runLater(() -> board.toEradicated(projectIndex));
    }

    /**
     * Set a course to mastered state
     * @param courseIndex the index of the course
     */
    public void displayCourseMastered(int courseIndex) {
        Platform.runLater(() -> board.toMastered(courseIndex));
    }

    /**
     * Set a course to Eradicated state
     * @param courseIndex the index of the course
     */
    public void displayCourseEradicated(int courseIndex) {
        Platform.runLater(() -> board.toEradicated(courseIndex));
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

    public void fireCardClicked(String title, String text) {
        Platform.runLater(() -> board.titleToFireCardClicked(title, text));
    }

    public void fireRoomCardClicked(String roomName) {
        listeners.forEach(event -> event.roomCardClicked(roomName));
    }

    public void fireEventCardClicked(Event event) {
        listeners.forEach(e -> e.eventCardClicked(event));
    }

    public void fireGiveUpButtonClicked() {
        listeners.forEach(ViewListener::giveUpButtonClicked);
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
        Platform.runLater(() -> board.clean());
    }

    public void displayInfoMessage(String s) {
        Platform.runLater(() -> {
            Alert popup = new Alert(Alert.AlertType.INFORMATION);
            popup.setTitle("The-Projects");
            popup.setHeaderText(null);
            popup.setContentText(s);

            popup.showAndWait();
        });
    }

    public void displayValidationMessage(String s) {
        Platform.runLater(() -> {
            Alert message = new Alert(Alert.AlertType.CONFIRMATION);
            message.setTitle("The-Projects");
            message.setHeaderText(null);
            message.setContentText(s);
            Optional<ButtonType> choice = message.showAndWait();
            if (choice.get() == ButtonType.OK) {
                listeners.forEach(ViewListener::YesButtonClicked);
            } else {
                listeners.forEach(ViewListener::NoButtonClicked);
            }
        });
    }

    public void displayMessage(String s) {
        Platform.runLater(() -> {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setTitle("The-Projects");
            message.setHeaderText(null);
            message.setContentText(s);
            message.show();
        });
    }

    public void fireSettingValidationButtonClicked() {
        listeners.forEach(ViewListener::settingValidationButtonClicked);
    }

    public void displayDrawCards(Owner actualOwner, Owner newOwner, boolean clickable, ArrayList<String> roomNamesOfRoomCards, ArrayList<Event> eventsOfEventCards, int numberOfPartyCards) {
        Platform.runLater(() -> board.drawCards(actualOwner, newOwner, clickable, roomNamesOfRoomCards, eventsOfEventCards, numberOfPartyCards));
    }

    public void displayDiscardCards() {
        Platform.runLater(() -> board.discardCards());
    }

    public void displayDiscardCard(Owner newOwner, String roomNameOfRoomCard) {
        Platform.runLater(() -> board.discardCard(newOwner, roomNameOfRoomCard));
    }

    public void displayDiscardCard(Owner newOwner, Event eventOfEventCard) {
        Platform.runLater(() -> board.discardCard(newOwner, eventOfEventCard));
    }

    public void setMovesButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setMovesButtonDisabled(disabled));
    }

    public void setProjectsButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setProjectsButtonDisabled(disabled));
    }

    public void setShareButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setShareButtonDisabled(disabled) );
    }

    public void setCardButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setCardButtonDisabled(disabled) );
    }

    public void setMasterButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setMasterButtonDisabled(disabled) );
    }

    public void setLabRoomButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setLabRoomButtonDisabled(disabled) );
    }

    public void setHackerButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setHackerButtonDisabled(disabled) );
    }

    public void setEndButtonButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setEndButtonButtonDisabled(disabled) );
    }

    /**
     * Method to make a Pawn clickable or not
     * @param clickable true to make the pawn clickable
     * @param role the role of the pawn to change
     */
    public void makePawnClickable(boolean clickable, Role role) {
        board.makePawnClickable(clickable, role);
    }

    /**
     * Method to change the current player
     * @param role the role of the new current player
     */
    public void displayCurrentPlayer(Role role) {
        //TODO
    }

    /**
     * Method to display the number of remaining action points
     * @param actionPoints the number of remaining action points
     */
    public void displayActionsPoints(int actionPoints) {
        //TODO
    }
}

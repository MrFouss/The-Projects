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
 *  The Stage of the Game
 */
public class View extends Stage {
    private final MainMenu mainMenu;
    private Board board;
    private final HashSet<ViewListener> listeners = new HashSet<>();

    /**
     * Constructor of a new View
     */
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
        Course[] courses = model.getCourses();
        Player[] players = new Player[model.getPlayers().size()];
        int i = 0;
        for (PhDStudent phDStudent : model.getPlayers())
                players[i++] = new Player(phDStudent.getName(), phDStudent.getRole());
        setBoard(courses[0].getName(), courses[1].getName(), courses[2].getName(), courses[3].getName(), players);
        Platform.runLater(() -> setMaximized(true));
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
        Platform.runLater(this::setSettings);
    }

    private void setSettings() {
        Platform.runLater(() -> setScene(mainMenu));
        setHeight(50);
        setMaximized(false); //Why doesn't it works :'(
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

    /**
     * Method to call when a room is clicked
     * @param name the name of the room
     */
    public void fireRoomClicked(String name) {
        listeners.forEach(listener -> listener.placeClicked(name));
    }

    /**
     * Method to call when a Pawn is clicked
     * @param role the role of the Pawn
     */
    public void firePawnCLicked(Role role) {
        listeners.forEach(event->event.pawnClicked(role));
    }

    /**
     * Method to call when the moves button is clicked
     */
    public void fireMoveButtonClicked() {
        listeners.forEach(ViewListener::moveButtonClicked);
    }

    /**
     * Method to call when the projects button is clicked
     */
    public void fireRemoveProjectButtonClicked() {
        listeners.forEach(ViewListener::removeProjectButtonClicked);
    }

    /**
     * Method to call when the share button is clicked
     */
    public void fireShareKnowledgeButtonCLicked() {
        listeners.forEach(ViewListener::shareKnowledgeButtonClicked);
    }

    /**
     * Method to call when the card button is clicked
     */
    public void fireUseCardButtonClicked() {
        listeners.forEach(ViewListener::useCardButtonClicked);
    }

    /**
     * Method to call when the master button is clicked
     */
    public void fireMasterButtonClicked() {
        listeners.forEach(ViewListener::masterButtonClicked);
    }

    /**
     * Method to call when the lab room button is clicked
     */
    public void fireBuildTPButtonClicked() {
        listeners.forEach(ViewListener::buildTPButtonClicked);
    }

    /**
     * Method to call when the hacker button is clicked
     */
    public void fireHackButtonClicked() {
        listeners.forEach(ViewListener::hackButtonClicked);
    }

    /**
     * Method to call when the end button is clicked
     */
    public void fireEndOfStageButtonClicked() {
        listeners.forEach(ViewListener::endOfStageButtonClicked);
    }

    /**
     * Method to call when a card is clicked
     * @param title the title of the card
     * @param text the text of the card
     */
    public void fireCardClicked(String title, String text) {
        Platform.runLater(() -> board.titleToFireCardClicked(title, text));
    }

    /**
     * Method to call when a RoomCard is clicked
     * @param roomName the name of the room
     */
    public void fireRoomCardClicked(String roomName) {
        listeners.forEach(event -> event.roomCardClicked(roomName));
    }

    /**
     * Method to call when an EventCard is clicked
     * @param event the event of the card
     */
    public void fireEventCardClicked(Event event) {
        listeners.forEach(e -> e.eventCardClicked(event));
    }

    /**
     * Method to call when the give up button is clicked
     */
    public void fireGiveUpButtonClicked() {
        listeners.forEach(ViewListener::giveUpButtonClicked);
    }

    /**
     * Getter for the player names
     * @return the names of the players
     */
    public LinkedList<String> getPlayerNames() {
        return mainMenu.getPlayerNames();
    }

    /**
     * Getter of the names of the courses
     * @return the names of the courses
     */
    public LinkedList<String> getUVNames() {
        return mainMenu.getUVNames();
    }

    /**
     * Getter for the difficulty of the game
     * @return the chosen difficulty
     */
    public int getDifficulty() {
        return mainMenu.getDifficulty();
    }

    /**
     * Getter for the chosen roles
     * @return the chosen roles
     */
    public LinkedList<Role> getRoles() {
        return mainMenu.getRoles();
    }

    /**
     * Method to return the board to a clean state (only buttons and player hands can be clicked)
     */
    public void clean() {
        Platform.runLater(() -> board.clean());
    }

    /**
     * Method to display an informative message in a pop up
     * @param s the message to display
     */
    public void displayInfoMessage(String s) {
        Platform.runLater(() -> {
            Alert popup = new Alert(Alert.AlertType.INFORMATION);
            popup.setTitle("The-Projects");
            popup.setHeaderText(null);
            popup.setContentText(s);

            popup.showAndWait();
        });
    }

    /**
     * Method to display a validation message in a pop up
     * @param s the message to display
     */
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

    /**
     * Method to display a message in a pop up
     * @param s the message to display
     */
    public void displayMessage(String s) {
        Platform.runLater(() -> {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setTitle("The-Projects");
            message.setHeaderText(null);
            message.setContentText(s);
            message.show();
        });
    }

    /**
     * Method to call when the validation button has been clicked
     */
    public void fireSettingValidationButtonClicked() {
        listeners.forEach(ViewListener::settingValidationButtonClicked);
    }

    /**
     * Method to draw cards
     * @param actualOwner the Owner from where the cards are drawn
     * @param newOwner the Owner to which the cards will be discarded
     * @param clickable if true, the drawn cards will be clickable
     * @param roomNamesOfRoomCards the list of the names of the room cards to draw
     * @param eventsOfEventCards the events of the event cards to draw
     * @param numberOfPartyCards the number of Party cards to draw
     */
    public void displayDrawCards(Owner actualOwner, Owner newOwner, boolean clickable, ArrayList<String> roomNamesOfRoomCards, ArrayList<Event> eventsOfEventCards, int numberOfPartyCards) {
        Platform.runLater(() -> board.drawCards(actualOwner, newOwner, clickable, roomNamesOfRoomCards, eventsOfEventCards, numberOfPartyCards));
    }

    /**
     * Method to discard the displayed cards
     */
    public void displayDiscardCards() {
        Platform.runLater(() -> board.discardCards());
    }

    /**
     * Method to change the Owner to which a displayed room card will be discarded
     * @param newOwner the new Owner
     * @param roomNameOfRoomCard the name of the room of the room card
     */
    public void displayChangeOwnerOfDisplayedCard(Owner newOwner, String roomNameOfRoomCard) {
        Platform.runLater(() -> board.changeOwnerOfDisplayedCard(newOwner, roomNameOfRoomCard));
    }

    /**
     * Method to change the Owner to which a displayed room card will be discarded
     * @param newOwner the new Owner
     * @param eventOfEventCard the event of the card
     */
    public void displayChangeOwnerOfDisplayedCard(Owner newOwner, Event eventOfEventCard) {
        Platform.runLater(() -> board.changeOwnerOfDisplayedCard(newOwner, eventOfEventCard));
    }

    /**
     * Method to disable or enable the moves button
     * @param disabled if true, the button will be disabled
     */
    public void setMovesButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setMovesButtonDisabled(disabled));
    }

    /**
     * Method to disable or enable the projects button
     * @param disabled if true, the button will be disabled
     */
    public void setProjectsButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setProjectsButtonDisabled(disabled));
    }

    /**
     * Method to disable or enable the share button
     * @param disabled if true, the button will be disabled
     */
    public void setShareButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setShareButtonDisabled(disabled) );
    }

    /**
     * Method to disable or enable the card button
     * @param disabled if true, the button will be disabled
     */
    public void setCardButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setCardButtonDisabled(disabled) );
    }

    /**
     * Method to disable or enable the master button
     * @param disabled if true, the button will be disabled
     */
    public void setMasterButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setMasterButtonDisabled(disabled) );
    }

    /**
     * Method to disable or enable the lab room button
     * @param disabled if true, the button will be disabled
     */
    public void setLabRoomButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setLabRoomButtonDisabled(disabled) );
    }

    /**
     * Method to disable or enable the hacker button
     * @param disabled if true, the button will be disabled
     */
    public void setHackerButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setHackerButtonDisabled(disabled) );
    }

    /**
     * Method to disable or enable the end button
     * @param disabled if true, the button will be disabled
     */
    public void setEndButtonButtonDisabled(boolean disabled) {
        Platform.runLater(() -> board.setEndButtonButtonDisabled(disabled) );
    }

    /**
     * Method to make a Pawn clickable or not
     * @param clickable true to make the pawn clickable
     * @param role the role of the pawn to change
     */
    public void makePawnClickable(boolean clickable, Role role) {
        Platform.runLater(() -> board.makePawnClickable(clickable, role));
    }

    /**
     * Method to make a list of Pawns clickable
     * @param roles the list of the roles of the Pawns
     */
    public void makePawnsClickable(LinkedList<Role> roles) {
        board.makePawnsClickable(roles);
    }

    /**
     * Method to make all pawns clickable
     */
    public void makePawnsClickable() {
        Platform.runLater(() -> board.makePawnsClickable());
    }

    /**
     * Method to change the current player
     * @param role the role of the new current player
     */
    public void displayCurrentPlayer(Role role) {
        Platform.runLater(() -> board.setCurrentPlayer(role));
    }

    /**
     * Method to display the number of remaining action points
     * @param actionPoints the number of remaining action points
     */
    public void displayActionsPoints(int actionPoints) {
        Platform.runLater(() -> board.setActionsPoints(actionPoints));
    }

    /**
     * Method to call when the animation of the cards going to the center of the screen is finished
     */
    public void fireCardToCenterFinished() {
        listeners.forEach(ViewListener::cardToCenterFinished);
    }

    /**
     * Method to call when the animation of  the cards going to the Decks is finished
     */
    public void fireCardToDeckFinished() {
        listeners.forEach(ViewListener::cardToDeckFinished);
    }

    /**
     * Method to call when the Board has been cleaned
     */
    public void fireCleared() {
        listeners.forEach(ViewListener::cleared);
    }

    /**
     * Method to call when the animation of a moving Pawn is finished
     */
    public void fireMovePawnFinished() {
        listeners.forEach(ViewListener::movePawnFinished);
    }

    /**
     * Method to call when the animation of an increase of the propagation gauge is finished
     */
    public void firePropagationFinished() {
        listeners.forEach(ViewListener::propagationFinished);
    }

    /**
     * Method to call when the animation of an increase of the outbreak gauge is finished
     */
    public void fireOutbreakFinished() {
        listeners.forEach(ViewListener::outbreakFinished);
    }
}

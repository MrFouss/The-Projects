package the_projects;

import the_projects.controller.Controller;
import the_projects.view.View;

/**
 * Launcher of the game
 */
public final class Launcher {
	
    public static void main(String[] args) {
    	View stage = new View();
    	Controller controller = new Controller(stage);
        stage.addListener(controller);
        //stage.launch();
        controller.start();
    }

    /*
    @Override
    public void start(Stage primaryStage) {
        stage.setMinHeight(450);
        stage.setMinWidth(800);
        stage.setMaximized(true);
        stage.show();
        controller.start();

        //TODO remove next line
        stage.setBoard("AG44", "MI41", "SI20", "LO43", new Player("TOTO", Role.TRELLO_ADEPT), new Player("TATA", Role.DAOUID), new Player("TUTU", Role.GROUP_LEADER), new Player("TITI", Role.HACKER));
    }
    */
}

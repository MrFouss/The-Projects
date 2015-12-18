package the_projects.model;

import the_projects.view.View;

/**
 * 
 */
public class Game {

    /**
     * Default constructor
     */
    public Game() {
    }

    /**
     * 
     */
    private GameBoard board;


    public static void main(String[] args) {
        View view = new View();
        view.launch(args);
    }

}
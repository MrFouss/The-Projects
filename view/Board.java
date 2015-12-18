package the_projects.view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;



/**
 * TODO complete
 */
public class Board extends Scene {

    /**
     *
     */
    private Group group;
    private Building[] batiments;
    private Card selectedCard;


    public Board(Group root) {
        super(root, 900, 600);

        setFill(Color.PURPLE);

        group = root;
        float Base = 20;

        batiments = new Building[4];
        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(widthProperty().multiply((Base*3/2)/100.));
        rectangle.heightProperty().bind(heightProperty().multiply(((Base/2)*16/9.)/100.));
        rectangle.xProperty().bind(widthProperty().multiply(22/100.));
        rectangle.yProperty().bind(heightProperty().multiply(1/100.));
        batiments[0] = new Building(new Circle(), rectangle, Color.BLUE);
        rectangle = new Rectangle();
        rectangle.widthProperty().bind(widthProperty().multiply(Base/100.));
        rectangle.heightProperty().bind(heightProperty().multiply(((Base*3/4)*16/9.)/100.));
        rectangle.xProperty().bind(widthProperty().multiply(1/100.));
        rectangle.yProperty().bind(heightProperty().multiply(25/100.));
        batiments[1] = new Building(new Circle(), rectangle, Color.gray(.5));
        rectangle = new Rectangle();
        rectangle.widthProperty().bind(widthProperty().multiply((Base*1.5)/100.));
        rectangle.heightProperty().bind(heightProperty().multiply(((Base/2)*16/9.)/100.));
        rectangle.xProperty().bind(widthProperty().multiply(40/100.));
        rectangle.yProperty().bind(heightProperty().multiply(30/100.));
        Circle arrondi = new Circle();
        arrondi.centerXProperty().bind(rectangle.xProperty().add(rectangle.widthProperty().multiply(45/100.)));
        arrondi.centerYProperty().bind(rectangle.yProperty().add(rectangle.heightProperty()));
        arrondi.radiusProperty().bind(rectangle.widthProperty().multiply(10/100.));
        batiments[2] = new Building(arrondi, rectangle, Color.YELLOW);
        rectangle = new Rectangle();
        rectangle.widthProperty().bind(widthProperty().multiply((Base*5/8)/100.));
        rectangle.heightProperty().bind(heightProperty().multiply(((Base)*16/9.)/100.));
        rectangle.xProperty().bind(widthProperty().multiply(25/100.));
        rectangle.yProperty().bind(heightProperty().multiply(60/100.));
        arrondi = new Circle();
        arrondi.centerXProperty().bind(rectangle.xProperty().add(rectangle.widthProperty().multiply(50/100.)));
        arrondi.centerYProperty().bind(rectangle.yProperty());
        arrondi.radiusProperty().bind(rectangle.widthProperty().multiply(50/100.));
        batiments[3] = new Building(arrondi, rectangle, Color.RED);

        for (Building s: batiments) {
            group.getChildren().add(s.getCircle());
            group.getChildren().add(s.getRectangle());
        }

        group.getChildren().addAll(
                new Deck(this, "Cartes\nProjet", Color.GREEN, 60/100., 1/100., true),
                new Deck(this, "Défausse\nCartes\nProjet", Color.GREEN, 75/100., 1/100., true),
                new Deck(this, "Cartes\nJoueur", Color.BLUE, 1/100., 60/100., false),
                new Deck(this, "Défausse\nCartes\nJoueur", Color.BLUE, 11/100., 60/100., false)
                );
    }
}
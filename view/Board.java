package the_projects.view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;


/**
 * TODO complete
 */
public class Board extends Scene {

    /**
     *
     */
    private Group group;
    private Building[] batiments;
    private Pane pane;
    private Card selectedCard;


    public Board(Group root) {
        super(root, 900, 600);

        setFill(Color.PURPLE);

        group = root;
        pane = new Pane();
        group.getChildren().add(pane);

        float Base = 20;

        batiments = new Building[4];
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*3/2)/100.);
        rectangle.setHeight(getHeight()*((Base/2)*16/9.)/100.);
        rectangle.xProperty().bind(widthProperty().multiply(22/100.));
        rectangle.yProperty().bind(heightProperty().multiply(1/100.));
        batiments[0] = new Building(new Circle(), rectangle, Color.BLUE);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*Base/100.);
        rectangle.setHeight(getHeight()*((Base*3/4)*16/9.)/100.);
        rectangle.xProperty().bind(widthProperty().multiply(1/100.));
        rectangle.yProperty().bind(heightProperty().multiply(25/100.));
        batiments[1] = new Building(new Circle(), rectangle, Color.gray(.5));

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*1.5)/100.);
        rectangle.setHeight(getHeight()*((Base/2)*16/9.)/100.);
        rectangle.xProperty().bind(widthProperty().multiply(40/100.));
        rectangle.yProperty().bind(heightProperty().multiply(30/100.));
        Circle arrondi = new Circle();
        arrondi.centerXProperty().bind(rectangle.xProperty().add(rectangle.widthProperty().multiply(45/100.)));
        arrondi.centerYProperty().bind(rectangle.yProperty().add(rectangle.heightProperty()));
        arrondi.radiusProperty().bind(rectangle.widthProperty().multiply(10/100.));
        batiments[2] = new Building(arrondi, rectangle, Color.YELLOW);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*5/8)/100.);
        rectangle.setHeight(getHeight()*((Base)*16/9.)/100.);
        rectangle.xProperty().bind(widthProperty().multiply(25/100.));
        rectangle.yProperty().bind(heightProperty().multiply(60/100.));
        arrondi = new Circle();
        arrondi.centerXProperty().bind(rectangle.xProperty().add(rectangle.widthProperty().multiply(50/100.)));
        arrondi.centerYProperty().bind(rectangle.yProperty());
        arrondi.radiusProperty().bind(rectangle.widthProperty().multiply(50/100.));
        batiments[3] = new Building(arrondi, rectangle, Color.RED);

        for (Building s: batiments) {
            pane.getChildren().add(s.getShape());
        }

        pane.getChildren().addAll(
                new Deck(this, "Cartes\nProjet", Color.GREEN, 60/100., 1/100., true),
                new Deck(this, "Défausse\nCartes\nProjet", Color.GREEN, 75/100., 1/100., true),
                new Deck(this, "Cartes\nJoueur", Color.BLUE, 1/100., 60/100., false),
                new Deck(this, "Défausse\nCartes\nJoueur", Color.BLUE, 11/100., 60/100., false)
        );

        Scale scale = new Scale(1,1,0,0);
        scale.xProperty().bind(widthProperty().divide(getWidth()));
        scale.yProperty().bind(heightProperty().divide(getHeight()));
        pane.getTransforms().add(scale);


    }
}
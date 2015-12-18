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

        batiments = new Building[4];
        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(widthProperty().divide(2.5));
        rectangle.heightProperty().bind(heightProperty().divide(5));
        rectangle.xProperty().bind(widthProperty().divide(4.5));
        rectangle.yProperty().bind(heightProperty().divide(50));
        batiments[0] = new Building(new Circle(), rectangle, Color.BLUE);
        rectangle = new Rectangle();
        rectangle.widthProperty().bind(widthProperty().multiply(0.27));
        rectangle.heightProperty().bind(heightProperty().multiply(0.35));
        rectangle.xProperty().bind(widthProperty().divide(50));
        rectangle.yProperty().bind(heightProperty().divide(3.25));
        batiments[1] = new Building(new Circle(), rectangle, Color.gray(.5));
        rectangle = new Rectangle();
        rectangle.widthProperty().bind(widthProperty().divide(2.5));
        rectangle.heightProperty().bind(heightProperty().divide(5));
        rectangle.xProperty().bind(widthProperty().divide(1.75));
        rectangle.yProperty().bind(heightProperty().divide(3.25));
        Circle arrondi = new Circle();
        arrondi.centerXProperty().bind(rectangle.xProperty().add(rectangle.widthProperty().multiply(.6)));
        arrondi.centerYProperty().bind(rectangle.yProperty().add(rectangle.heightProperty()));
        arrondi.radiusProperty().bind(rectangle.widthProperty().divide(10));
        batiments[2] = new Building(arrondi, rectangle, Color.YELLOW);
        rectangle = new Rectangle();
        rectangle.widthProperty().bind(widthProperty().divide(6));
        rectangle.heightProperty().bind(heightProperty().divide(2.5));
        rectangle.xProperty().bind(widthProperty().divide(2.8));
        rectangle.yProperty().bind(heightProperty().divide(1.75));
        arrondi = new Circle();
        arrondi.centerXProperty().bind(rectangle.xProperty().add(rectangle.widthProperty().divide(2)));
        arrondi.centerYProperty().bind(rectangle.yProperty());
        arrondi.radiusProperty().bind(rectangle.widthProperty().divide(2));
        batiments[3] = new Building(arrondi, rectangle, Color.RED);

        for (Building s: batiments) {
            group.getChildren().add(s.getCircle());
            group.getChildren().add(s.getRectangle());
        }
    }
}
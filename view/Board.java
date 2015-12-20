package the_projects.view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


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


    public Board(Group root, int nbPlayers) {
        super(root, 1600, 900);

        setFill(Color.PURPLE);

        group = root;
        pane = new Pane();
        group.getChildren().add(pane);

        float Base = 25;

        batiments = new Building[4];
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*1.5)/100.);
        rectangle.setHeight(getHeight()*((Base/2)*16/9.)/100.);
        rectangle.setX(getWidth()*(20/100.));
        rectangle.setY(getHeight()*(1/100.));
        batiments[0] = new Building(Color.BLUE, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base)/100.);
        rectangle.setHeight(getHeight()*((Base*3/4)*16/9.)/100.);
        rectangle.setX(getWidth()*(1/100.));
        rectangle.setY(getHeight()*(25/100.));
        batiments[1] = new Building(Color.GREEN, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*1.5)/100.);
        rectangle.setHeight(getHeight()*(((Base+1)/2*16/9.)/100.));
        rectangle.setX(getWidth()*(50/100.));
        rectangle.setY(getHeight()*(30/100.));
        Circle arrondi = new Circle();
        arrondi.setCenterX(rectangle.getX() + rectangle.getWidth()*(48.5/100.));
        arrondi.setCenterY(rectangle.getY() + rectangle.getHeight());
        arrondi.setRadius(rectangle.getWidth()*(12/100.));
        batiments[2] = new Building(Color.YELLOW, arrondi, rectangle);

        rectangle = new Rectangle();
        rectangle.setWidth(getWidth()*(Base*5/8)/100.);
        rectangle.setHeight(getHeight()*((Base*8/10)*16/9.)/100.);
        rectangle.setX(getWidth()*(31/100.));
        rectangle.setY(getHeight()*(60/100.));
        arrondi = new Circle();
        arrondi.setCenterX(rectangle.getX() + rectangle.getWidth()*(50/100.));
        arrondi.setCenterY(rectangle.getY());
        arrondi.setRadius(rectangle.getWidth()*(50/100.));
        batiments[3] = new Building(Color.RED, arrondi, rectangle);

        for (Building s: batiments) {
            pane.getChildren().add(s.getShape());
        }

        pane.getChildren().addAll(
                new Deck(this, "Cartes\nProjet", Color.GRAY, 60/100., 1/100., true),
                new Deck(this, "Défausse\nCartes\nProjet", Color.GRAY, 75/100., 1/100., true),
                new Deck(this, "Cartes\nJoueur", Color.BLUE, 1/100., 60/100., false),
                new Deck(this, "Défausse\nCartes\nJoueur", Color.BLUE, 11/100., 60/100., false)
        );

        setRooms("the_projects/resources/rooms.cvs", nbPlayers);

        Scale scale = new Scale(1,1,0,0);
        scale.xProperty().bind(widthProperty().divide(getWidth()));
        scale.yProperty().bind(heightProperty().divide(getHeight()));
        pane.getTransforms().add(scale);

    }

    public static void setHoverListener(Shape shape) {
        shape.setStrokeWidth(3);
        shape.setStroke(((Color)shape.getFill()).deriveColor(0,1,.5,1));
        shape.hoverProperty().addListener((e) -> {
            if(shape.isHover()) {
                shape.setStroke(((Color)(shape.getStroke())).deriveColor(100,1,4,1));
            }else{
                shape.setStroke(((Color)shape.getFill()).deriveColor(0,1,.5,1));
            }
        });
    }

    public void setRooms(String path, int nbPlayers) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            List<String> rooms = Files.readAllLines(Paths.get(classLoader.getResource(path).getPath()));
            for (String s : rooms) {
                String[] vars = s.split(",");
                pane.getChildren().add(new Room(batiments[Integer.parseInt(vars[0])].getColor().darker(), vars[1], Double.parseDouble(vars[2])*1600/100, Double.parseDouble(vars[3])*900/100, nbPlayers));
            }
        }
        catch (NullPointerException e) {
            System.out.println("Room file not found(pointer)");
        }
        catch (IOException e) {
            System.out.println("Room file not found");
        }
    }
}
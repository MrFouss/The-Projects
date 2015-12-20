package the_projects.view;

/**
 * Created by th0re on 19/12/15.
 * Class to give coordinates as return type
 */
public class Coord {
    private double x, y;

    public Coord() {
    }

    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}

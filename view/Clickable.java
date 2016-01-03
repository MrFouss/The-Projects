package the_projects.view;

/**
 * Interface for node that can be clicked when needed
 */
public interface Clickable {
    /**
     * Method to make a object clickable or not
     * @param clickable if true, the object will be made clickable
     * @param view the View to which the object must declare when it has been clicked
     */
    void setClickable(boolean clickable, View view);

    /**
     * Method to reset the original color of an object
     */
    void resetFill();
}

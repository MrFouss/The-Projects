package the_projects.view;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

/**
 * TODO implement
 */
public class MyButton extends Button {

    public MyButton(String text) {
        super(text);
        setTextFill(Color.BLUEVIOLET);
        Color color = Color.ORANGE;
        setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))), new CornerRadii(20), new Insets(0))));
        setBorder(new Border(new BorderStroke(color.deriveColor(0,1,.5,1), BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
        hoverProperty().addListener(e-> {
            if (!isFocused()) {
                if (isHover()) {
                    setBorder(new Border(new BorderStroke(color.deriveColor(90, 1, .5, 1), BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                } else {
                    setBorder(new Border(new BorderStroke(color.deriveColor(0, 1, .5, 1), BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                }
            }
        });
        focusedProperty().addListener(e-> {
            if (isFocused()) {
                setBorder(new Border(new BorderStroke(color.deriveColor(-90, 1, .5, 1), BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
            } else {
                setBorder(new Border(new BorderStroke(color.deriveColor(0, 1, .5, 1), BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
            }
        });

        setMaxWidth(Double.POSITIVE_INFINITY);
        setCursor(Cursor.HAND);
    }



}
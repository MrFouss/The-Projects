package the_projects.view;


import javafx.geometry.Insets;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Scale;

/**
 * TODO complete
 */
public class Actions extends BorderPane {

    public Actions(Board board, Pane pane, Player... players) {
        Color color = Color.DARKSLATEBLUE;
        setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))), new CornerRadii(20), new Insets(0))));



        setLayoutX(board.getWidth()*50/100.);
        setLayoutY(board.getHeight()*65/100.);
        setPrefSize(board.getWidth()*48/100.,board.getHeight()*32/100.);

        VBox buttons = new VBox(board.getHeight()*.5/100.);
        setLeft(buttons);
        setMargin(buttons, new Insets(board.getHeight()*1/100.));

        buttons.setFillWidth(true);

        MyButton testButton = new MyButton("Déplacer un pion");
        Tooltip tooltip = new Tooltip("Pour le coût d'une action, vous pouvez :" +
                "\nCourir dans une salle connectée." +
                "\nPrendre une navette TP express d'une salle de TP à une autre." +
                "\nDéplacer un pion vers un autre si vous êtes le leader." +
                "\n(Vous pouvez cliquer directement sur un pion pour le déplacer)");
        testButton.setTooltip(tooltip);
        buttons.getChildren().add(testButton);
        testButton = new MyButton("Rendre un ou plusieurs projets");
        tooltip = new Tooltip("Permet de rendre un projet dans la salle où vous êtes." +
                "\nSi l'UV à été masterisée ou si vous êtes le Mentor, vous rendez tous les projets de l'UV." +
                "\nSi il y a des projets d'UVs differentes dans la salle, l'UV est choisie au hasard.");
        testButton.setTooltip(tooltip);
        buttons.getChildren().add(testButton);
        testButton = new MyButton("Partager des connaissances");
        tooltip = new Tooltip("Vous permet de donner ou recevoir la carte de la salle dans laquelle vous êtes." +
                "\nSi vous êtes l'Adepte de Trello, vous pouvez donner n'importe quelle carte");
        testButton.setTooltip(tooltip);
        buttons.getChildren().add(testButton);
        testButton = new MyButton("Utiliser une carte");
        tooltip = new Tooltip("Vous pouvez utlisez une carte pour :" +
                "\nVous téléporter vers où depuis une salle dont vous avez la carte" +
                "\nLancez l'évènement décrit par une carte évènement");
        testButton.setTooltip(tooltip);
        buttons.getChildren().add(testButton);
        testButton = new MyButton("Utiliser plusieurs cartes");
        tooltip = new Tooltip("Vous pouvez utlisez 5 (ou 4 si vous ètes Daouid) carte de salle d'une UV pour la masteriser" +
                "\nCependant vous devez être dans une salle de TP");
        testButton.setTooltip(tooltip);
        buttons.getChildren().add(testButton);
        testButton = new MyButton("Construire une salle de TP");
        tooltip = new Tooltip("Vous pouvez transformer la salle où vous êtes en salle de TP en vous défaussant de la carte de Salle correspondante" +
                "\nSi vous êtes l'Installer, vous n'avez pas besoin de cette carte");
        testButton.setTooltip(tooltip);
        buttons.getChildren().add(testButton);
        testButton = new MyButton("Hacker");
        tooltip = new Tooltip("Si vous êtes le Hacker, vous pouvez retirer une carte d'èvenement de la défausse pour la mettre en réserve." +
                "\nVous pourrez ensuite la consulter et l'utiliser quand vous voulez gratuitement puis la retirer du jeu.");
        testButton.setTooltip(tooltip);
        buttons.getChildren().add(testButton);
        testButton = new MyButton("Fin de phase");
        tooltip = new Tooltip("Termine la phase, vous fait piocher des cartes et lance la phase de propagation");
        testButton.setTooltip(tooltip);
        buttons.getChildren().add(testButton);

        VBox right = new VBox();
        setRight(right);
        right.maxWidthProperty().bind(widthProperty());
        setMargin(right, new Insets(board.getHeight()*1/100.));

        int i = 0;
        for (Player player : players) {
            right.getChildren().add(player.getPane());
            Deck playerDeck = new Deck(board, "", Color.TRANSPARENT, 94/100., (66.5 + (i++)*31./players.length)/100.);
            Scale scale = new Scale(.3,.3,0,0);
            playerDeck.getTransforms().add(scale);


            pane.getChildren().add(playerDeck);

            player.getPane().setPrefHeight(getPrefHeight()/players.length);
            player.getPane().setPrefWidth(getPrefWidth()/1.5);
            player.setHandDeck(playerDeck);

        }
    }
}
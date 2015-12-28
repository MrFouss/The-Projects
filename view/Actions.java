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
 * TODO complete + javadoc
 */
public class Actions extends BorderPane {
    private MyButton moves;
    private MyButton projects;
    private MyButton share;
    private MyButton card;
    private MyButton master;
    private MyButton labRoom;
    private MyButton hacker;
    private MyButton endButton;


    public Actions(Board board, View view, Pane pane, Player... players) {
        Color color = Color.DARKSLATEBLUE;
        setBackground(new Background(new BackgroundFill(new RadialGradient(0, 0, .5, .5, .8, true, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, color.deriveColor(0,1,.5,1))), new CornerRadii(20), new Insets(0))));



        setLayoutX(board.getWidth()*50/100.);
        setLayoutY(board.getHeight()*65/100.);
        setPrefSize(board.getWidth()*48/100.,board.getHeight()*32/100.);

        VBox buttons = new VBox(board.getHeight()*.5/100.);
        setLeft(buttons);
        setMargin(buttons, new Insets(board.getHeight()*1/100.));

        buttons.setFillWidth(true);

        moves = new MyButton("Déplacer un pion");
        Tooltip tooltip = new Tooltip("Pour le coût d'une action, vous pouvez :" +
                "\nCourir dans une salle connectée." +
                "\nPrendre une navette TP express d'une salle de TP à une autre." +
                "\nDéplacer un pion vers un autre si vous êtes le leader." +
                "\n(Vous pouvez cliquer directement sur un pion pour le déplacer)");
        moves.setTooltip(tooltip);
        moves.setOnMouseClicked(event -> view.fireMoveButtonClicked());
        buttons.getChildren().add(moves);

        projects = new MyButton("Rendre un ou plusieurs projets");
        tooltip = new Tooltip("Permet de rendre un projet dans la salle où vous êtes." +
                "\nSi l'UV à été masterisée ou si vous êtes le Mentor, vous rendez tous les projets de l'UV." +
                "\nSi il y a des projects d'UVs differentes dans la salle, l'UV est choisie au hasard.");
        projects.setTooltip(tooltip);
        projects.setOnMouseClicked(event -> view.fireRemoveProjectButtonClicked());
        buttons.getChildren().add(projects);

        share = new MyButton("Partager des connaissances");
        tooltip = new Tooltip("Vous permet de donner ou recevoir la carte de la salle dans laquelle vous êtes." +
                "\nSi vous êtes l'Adepte de Trello, vous pouvez donner n'importe quelle carte");
        share.setTooltip(tooltip);
        share.setOnMouseClicked(event -> view.fireShareKnowledgeButtonCLicked());
        buttons.getChildren().add(share);

        card = new MyButton("Utiliser une carte");
        tooltip = new Tooltip("Vous pouvez utlisez une carte pour :" +
                "\nVous téléporter vers où depuis une salle dont vous avez la carte" +
                "\nLancez l'évènement décrit par une carte évènement");
        card.setTooltip(tooltip);
        card.setOnMouseClicked(event -> view.fireUseCardButtonClicked());
        buttons.getChildren().add(card);

        master = new MyButton("Masteriser une UV");
        tooltip = new Tooltip("Vous pouvez utlisez 5 (ou 4 si vous ètes Daouid) carte de salle d'une UV pour la masteriser" +
                "\nCependant vous devez être dans une salle de TP");
        master.setTooltip(tooltip);
        master.setOnMouseClicked(event -> view.fireMasterButtonClicked());
        buttons.getChildren().add(master);

        labRoom = new MyButton("Construire une salle de TP");
        tooltip = new Tooltip("Vous pouvez transformer la salle où vous êtes en salle de TP en vous défaussant de la carte de Salle correspondante" +
                "\nSi vous êtes l'Installer, vous n'avez pas besoin de cette carte");
        labRoom.setTooltip(tooltip);
        labRoom.setOnMouseClicked(event -> view.fireBuildTPButtonClicked());
        buttons.getChildren().add(labRoom);

        hacker = new MyButton("Hacker");
        tooltip = new Tooltip("Si vous êtes le Hacker, vous pouvez retirer une carte d'èvenement de la défausse pour la mettre en réserve." +
                "\nVous pourrez ensuite la consulter et l'utiliser quand vous voulez gratuitement puis la retirer du jeu.");
        hacker.setTooltip(tooltip);
        hacker.setOnMouseClicked(event -> view.fireHackButtonClicked());
        buttons.getChildren().add(hacker);

        endButton = new MyButton("Fin de phase");
        tooltip = new Tooltip("Termine la phase, vous fait piocher des cartes et lance la phase de propagation");
        endButton.setTooltip(tooltip);
        endButton.setOnMouseClicked(event -> view.fireEndOfStageButtonClicked());
        buttons.getChildren().add(endButton);

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
            player.setHandDeck(playerDeck, view);

        }
    }

    public void setMovesDisabled(boolean disabled) {
        moves.setDisable(disabled);
    }

    public void setProjectsDisabled(boolean disabled) {
        projects.setDisable(disabled);
    }

    public void setShareDisabled(boolean disabled) {
        share.setDisable(disabled);
    }

    public void setCardDisabled(boolean disabled) {
        card.setDisable(disabled);
    }

    public void setMasterDisabled(boolean disabled) {
        master.setDisable(disabled);
    }

    public void setLabRoomDisabled(boolean disabled) {
        labRoom.setDisable(disabled);
    }

    public void setHackerDisabled(boolean disabled) {
        hacker.setDisable(disabled);
    }

    public void setEndButtonDisabled(boolean disabled) {
        endButton.setDisable(disabled);
    }
}
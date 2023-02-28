package rps.gui.controller;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rps.bll.game.GameManager;
import rps.bll.game.Move;
import rps.bll.game.Result;
import rps.bll.game.ResultType;
import rps.bll.player.IPlayer;
import rps.bll.player.Player;
import rps.bll.player.PlayerType;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 *
 * @author smsj
 */
public class GameViewController implements Initializable {
    @FXML private ImageView imageGameIcon, humanMove, botMove;
    @FXML private Label labelAIMove, labelWinner, labelYourMove, labelPlayerName, labelAIName, labelGameRound;
    @FXML private VBox humanMoveVbox, botMoveVBox, menuBarVBox, mainContainer;
    @FXML private HBox centerGraphicHBox, statisticsHBox, playButtonsHBox;
    @FXML private Scene scene;
    private final Image rockSymbol = new Image("/rps/gui/view/icons/rock-hand.png");
    IPlayer human;
    IPlayer bot;
    GameManager gm;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bot = new Player(getRandomBotName(), PlayerType.AI);
        labelAIName.setText(bot.getPlayerName() + "'s move:");

        labelAIMove.setText("");
        labelYourMove.setText("");
        labelWinner.setText("");
        labelGameRound.setText("0");
    }

    private void bindSizes(){
        centerGraphicHBox.prefHeightProperty().bind(scene.heightProperty().subtract(playButtonsHBox.heightProperty())
                .subtract(statisticsHBox.heightProperty().getValue()));
        centerGraphicHBox.prefWidthProperty().bind(scene.widthProperty().subtract(menuBarVBox.widthProperty()));

        humanMoveVbox.prefWidthProperty().bind((centerGraphicHBox.prefWidthProperty()).divide(2));
        humanMoveVbox.prefHeightProperty().bind((centerGraphicHBox.prefHeightProperty()));

        humanMove.fitWidthProperty().bind(humanMoveVbox.prefWidthProperty());
        humanMove.fitHeightProperty().bind(humanMoveVbox.prefHeightProperty());
        humanMove.setImage(rockSymbol);

        //botMoveVBox.prefWidthProperty().bind((centerGraphicHBox.prefWidthProperty()).divide(2));
        //botMoveVBox.prefWidthProperty().bind((centerGraphicHBox.prefHeightProperty()));

        botMove.fitWidthProperty().bind(humanMove.fitWidthProperty());
        botMove.fitHeightProperty().bind(humanMove.fitHeightProperty());
        botMove.setImage(rockSymbol);
    }

    @FXML
    private void clickRock(ActionEvent actionEvent){
        Result result = gm.playRound(Move.Rock);
        updateLabels(result);
    }

    @FXML
    private void clickPaper(ActionEvent actionEvent){
        Result result = gm.playRound(Move.Paper);
        updateLabels(result);
    }

    @FXML
    private void clickScissors(ActionEvent actionEvent){
        Result result = gm.playRound(Move.Scissor);
        updateLabels(result);
    }

    @FXML
    private void clickMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/rps/gui/view/MainMenuView.fxml"));
        stage.setTitle("Rock, Paper, Scissors");
        stage.setScene(new Scene(root));
        stage.show();

        Node node = (Node) actionEvent.getSource();
        node.getScene().getWindow().hide();
    }

    @FXML
    private void clickRestart(ActionEvent actionEvent) {
        gm = new GameManager(human, bot);
        labelAIMove.setText("");
        labelYourMove.setText("");
        labelWinner.setText("");
        labelGameRound.setText("0");

        System.out.println("Center height: " + centerGraphicHBox.getHeight());
        System.out.println("Center width: " +centerGraphicHBox.getWidth());
        System.out.println("Statistics height: " + statisticsHBox.getHeight());
        System.out.println("Play buttons height: " + playButtonsHBox.getHeight());
        System.out.println("Scene height: " + scene.getHeight());

        System.out.println(centerGraphicHBox.maxHeightProperty().getValue());
    }

    public void setHuman(IPlayer human) {
        this.human = human;
        gm = new GameManager(this.human, bot);
        labelPlayerName.setText(human.getPlayerName() + "'s move:");
    }

    private String getRandomBotName() {
        String[] botNames = new String[] {
                "R2D2",
                "Mr. Data",
                "3PO",
                "Bender",
                "Marvin the Paranoid Android",
                "Bishop",
                "Robot B-9",
                "HAL"
        };
        int randomNumber = new Random().nextInt(botNames.length - 1);
        return botNames[randomNumber];
    }

    private void updateLabels(Result result){
        if (result.getType() == ResultType.Tie){
            labelWinner.setText("Tie");
            labelYourMove.setText(result.getWinnerMove().toString());
            labelAIMove.setText(result.getLoserMove().toString());
        } else{
            if (result.getWinnerPlayer().getPlayerName().equals(human.getPlayerName())){
                labelYourMove.setText(result.getWinnerMove().toString());
                labelWinner.setText(result.getWinnerPlayer().getPlayerName());
                labelAIMove.setText(result.getLoserMove().toString());
            }
            else {
                labelYourMove.setText(result.getLoserMove().toString());
                labelWinner.setText(result.getWinnerPlayer().getPlayerName());
                labelAIMove.setText(result.getWinnerMove().toString());
            }
        }
        labelGameRound.setText(String.valueOf(result.getRoundNumber()));
    }
    @FXML
    public void clickStats(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Stats:");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/rps/gui/view/StatsView.fxml"));
        Parent root = loader.load();
        GameStatsController controller = loader.getController();
        controller.setGameManager(gm);
        System.out.println("GameViewController: " + gm);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        bindSizes();
    }
}

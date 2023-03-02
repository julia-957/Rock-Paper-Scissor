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
import rps.bll.game.*;
import rps.bll.player.IPlayer;
import rps.bll.player.Player;
import rps.bll.player.PlayerType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 *
 * @author smsj
 */
public class GameViewController implements Initializable {
    @FXML private ImageView imageGameIcon, humanMove, botMove;
    @FXML private Label labelAIMove, labelWinner, labelYourMove, labelPlayerName, labelAIName, labelGameRound, labelGamesWon, labelTies, labelBotWins;
    @FXML private VBox humanMoveVbox, botMoveVBox, menuBarVBox, mainContainer;
    @FXML private HBox centerGraphicHBox, statisticsHBox, playButtonsHBox;
    @FXML private Scene scene;
    private final Image rockSymbol = new Image("/rps/gui/view/icons/robotWithBubble.png");
    private final Image wilhelm = new Image("/rps/gui/view/icons/wilhelm.png");
    private IPlayer human;
    private IPlayer bot;
    private GameManager gm;
    private GameStatsController gameStatsController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bot = new Player("Wi1h31m", PlayerType.AI);
        imageGameIcon.setImage(wilhelm);
        labelAIName.setText(bot.getPlayerName() + "'s move:");

        labelAIMove.setText("");
        labelYourMove.setText("");
        labelWinner.setText("");
        labelGameRound.setText("0");
        labelGamesWon.setText("0");
        labelTies.setText("0 %");
        labelBotWins.setText("0 %");
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

        botMoveVBox.prefWidthProperty().bind((centerGraphicHBox.prefWidthProperty()).divide(2));
        botMoveVBox.prefWidthProperty().bind((centerGraphicHBox.prefHeightProperty()));

        botMove.fitWidthProperty().bind(humanMove.fitWidthProperty());
        botMove.fitHeightProperty().bind(humanMove.fitHeightProperty());
        botMove.setImage(rockSymbol);
    }

    @FXML
    private void clickRock(ActionEvent actionEvent){
        playRound(Move.Rock);
    }
    @FXML
    private void clickPaper(ActionEvent actionEvent){
        playRound(Move.Paper);
    }
    @FXML
    private void clickScissors(ActionEvent actionEvent){
        playRound(Move.Scissor);
    }
    private void playRound(Move move){
        Result res = gm.playRound(move);
        updateLabels(res);
        updatePercentages();
        callUpdateMarkov();
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
        callUpdateMarkov();
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
        gameStatsController = loader.getController();
        gameStatsController.setGameManager(gm);
        //System.out.println("GameViewController: " + gm);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnHiding(event -> gameStatsController = null);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        bindSizes();
    }

    private void callUpdateMarkov(){
        if (gameStatsController != null)
            gameStatsController.updateMatrix();
    }

    private void updatePercentages(){
        labelGamesWon.setText(getHumanWinPercentage());
        labelTies.setText(getTiePercentage());
        labelBotWins.setText(getBotWinPercentage());
    }

    private String getBotWinPercentage() {
        gm.getGameState().getHistoricResults();
        double botWins = 0;
        for (Result r: gm.getGameState().getHistoricResults()){
            if(r.getWinnerPlayer().getPlayerType().equals(PlayerType.AI) && r.getType().equals(ResultType.Win))
                botWins++;
        }

        double winPercentage = (botWins/gm.getGameState().getHistoricResults().size())*100;

        return (((int) winPercentage) + " %");
    }

    private String getHumanWinPercentage() {
        gm.getGameState().getHistoricResults();
        double humanWins = 0;
        for (Result r: gm.getGameState().getHistoricResults()){
            if(r.getWinnerPlayer().getPlayerType().equals(PlayerType.Human) && r.getType().equals(ResultType.Win))
                humanWins++;
        }
        double winPercentage = (humanWins/gm.getGameState().getHistoricResults().size())*100;
        
        return (((int) winPercentage) + "");
    }

    private String getTiePercentage(){
        gm.getGameState().getHistoricResults();
        double ties = 0;
        for (Result r: gm.getGameState().getHistoricResults()){
            if(r.getType().equals(ResultType.Tie)){
                ties++;
            }
        }
        double tiePercentage = (ties/gm.getGameState().getHistoricResults().size()*100);
        return (((int) tiePercentage) + " %");
    }
}

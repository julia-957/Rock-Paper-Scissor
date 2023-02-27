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
import javafx.scene.image.ImageView;

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
    @FXML
    private ImageView imageGameIcon;
    @FXML
    private Label labelAIMove, labelWinner, labelYourMove, labelPlayerName, labelAIName;

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
        gm = new GameManager(human, bot);
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
    private void clickRestart(ActionEvent event) {
    }

    public void setHuman(IPlayer human) {
        this.human = human;
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
            labelPlayerName.setText(result.getWinnerPlayer().getPlayerName());
            labelAIName.setText(result.getWinnerPlayer().getPlayerName());
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
    }
}

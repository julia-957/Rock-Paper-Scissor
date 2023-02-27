package rps.gui.controller;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import rps.bll.game.GameManager;
import rps.bll.game.Move;
import rps.bll.player.IPlayer;
import rps.bll.player.Player;
import rps.bll.player.PlayerType;

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
    private Label labelAIMove, labelWinner, labelYourMove;

    IPlayer human;
    IPlayer bot;
    GameManager ge;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bot = new Player(getRandomBotName(), PlayerType.AI);
        ge = new GameManager(human, bot);
    }

    public void clickRock(ActionEvent actionEvent){
        ge.playRound(Move.Rock);
    }

    public void clickPaper(ActionEvent actionEvent){
        ge.playRound(Move.Paper);
    }

    public void clickScissors(ActionEvent actionEvent){
        ge.playRound(Move.Scissor);
    }

    @FXML
    private void clickMainMenu(ActionEvent event) {

    }

    @FXML
    private void clickRestart(ActionEvent event) {

    }

    public void setHuman(IPlayer human) {
        this.human = human;
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
}

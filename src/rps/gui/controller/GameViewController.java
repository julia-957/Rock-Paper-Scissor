package rps.gui.controller;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void clickMainMenu(ActionEvent event) {

    }

    @FXML
    void clickPaper(ActionEvent event) {

    }

    @FXML
    void clickRestart(ActionEvent event) {

    }

    @FXML
    void clickRock(ActionEvent event) {

    }

    @FXML
    void clickScissors(ActionEvent event) {

    }
}

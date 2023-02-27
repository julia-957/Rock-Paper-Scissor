package rps.gui.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import rps.bll.player.IPlayer;
import rps.bll.player.Player;
import rps.bll.player.PlayerType;

import java.io.IOException;

public class MainMenuController {
    @FXML private MFXButton btnStartGame;
    @FXML private MFXTextField txtPlayerName;

    @FXML
    void btnStartAction(ActionEvent actionEvent) throws IOException {
        if (!txtPlayerName.getText().isEmpty()){
            IPlayer human = new Player(txtPlayerName.getText().trim(), PlayerType.Human);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/rps/gui/view/GameView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Rock, Paper, Scissors");
            stage.setScene(new Scene(fxmlLoader.load()));
            GameViewController gameViewController = fxmlLoader.getController();
            gameViewController.setHuman(human);
            stage.show();

            Node node = (Node) actionEvent.getSource();
            node.getScene().getWindow().hide();
        }
        else {
            new Alert(Alert.AlertType.ERROR, "Player name cannot be empty!").show();
        }
    }
}

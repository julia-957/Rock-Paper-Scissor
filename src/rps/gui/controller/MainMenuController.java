package rps.gui.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import rps.bll.player.IPlayer;
import rps.bll.player.Player;
import rps.bll.player.PlayerType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML private MFXButton btnStartGame;
    @FXML private MFXTextField txtPlayerName;

    @FXML
    void btnStartAction(Event event) throws IOException {
        if (!txtPlayerName.getText().isEmpty()){
            IPlayer human = new Player(txtPlayerName.getText().trim(), PlayerType.Human);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/rps/gui/view/GameView.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Rock, Paper, Scissors");
            stage.setScene(scene);
            GameViewController gameViewController = fxmlLoader.getController();
            gameViewController.setScene(scene);
            gameViewController.setHuman(human);
            stage.show();

            Node node = (Node) event.getSource();
            node.getScene().getWindow().hide();
        }
        else {
            new Alert(Alert.AlertType.ERROR, "Player name cannot be empty!").show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtPlayerName.setOnKeyReleased(event -> {
            if(event.getCode()== KeyCode.ENTER) {
                try {
                    btnStartAction(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }
}

package rps.gui.controller;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import rps.bll.game.GameManager;
import rps.bll.game.Result;
import rps.bll.player.IPlayer;
import rps.bll.player.Player;
import rps.bll.player.PlayerType;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

public class GameStatsController implements Initializable {
    @FXML
    private MFXTableView<Result> table;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label RRscore, RPscore, RSscore, PRscore, PPscore, PSscore, SRscore, SPscore, SSscore;
    private Label[][] scores;
    private GameManager gm;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            SetupTable();
            updateMatrix();
        });
        scores = new Label[][]{{RRscore, RPscore, RSscore}, {PRscore, PPscore, PSscore}, {SRscore, SPscore, SSscore}};
    }
    public void setGameManager(GameManager gm){
        this.gm = gm;
    }

    private void SetupTable(){
        MFXTableColumn<Result> roundNumberColumn = new MFXTableColumn<>("RoundNumber", true, Comparator.comparing(Result::getRoundNumber));
        MFXTableColumn<Result> winnerColumn = new MFXTableColumn<>("Winner", true,
            Comparator.comparing(result -> {
                IPlayer winner = result.getWinnerPlayer();
                return winner == null ? "" : winner.getPlayerName();
            })
        );
        MFXTableColumn<Result> winnerMoveColumn = new MFXTableColumn<>("WinnerMove", true, Comparator.comparing(Result::getWinnerMove));
        MFXTableColumn<Result> loserColumn = new MFXTableColumn<>("Loser", true,
            Comparator.comparing(result -> {
                IPlayer loser = result.getLoserPlayer();
                return loser == null ? "" : loser.getPlayerName();
            })
        );
        MFXTableColumn<Result> loserMoveColumn = new MFXTableColumn<>("LoserMove", true, Comparator.comparing(Result::getLoserMove));

        roundNumberColumn.setRowCellFactory(result -> new MFXTableRowCell<>(Result::getRoundNumber));
        winnerColumn.setRowCellFactory(result -> new MFXTableRowCell<>(result1 -> {
            IPlayer winner = result.getWinnerPlayer();
            return winner == null ? "" : winner.getPlayerName();
        }));
        winnerMoveColumn.setRowCellFactory(result -> new MFXTableRowCell<>(Result::getWinnerMove));
        loserColumn.setRowCellFactory(result -> new MFXTableRowCell<>(result1 -> {
            IPlayer loser = result.getLoserPlayer();
            return loser == null ? "" : loser.getPlayerName();
        }));
        loserMoveColumn.setRowCellFactory(result -> new MFXTableRowCell<>(Result::getLoserMove));

        table.getTableColumns().addAll(roundNumberColumn, winnerColumn, winnerMoveColumn, loserColumn, loserMoveColumn);
        //System.out.println("GameStatsController.SetupTable() gm = " + gm);
        if (gm != null)
            table.setItems(FXCollections.observableArrayList(gm.getGameState().getHistoricResults())); //TODO: change to observable list in game manager
    }

    public void updateMatrix(){
        ArrayList<Result> results = (ArrayList<Result>) gm.getGameState().getHistoricResults();
        if (results.size() > 0) {
            Player bot = (Player) ((results.get(results.size() - 1).getWinnerPlayer().getPlayerType() == PlayerType.AI) ? results.get(results.size() - 1).getWinnerPlayer() : results.get(results.size() - 1).getLoserPlayer());
            int[][] matrix = bot.getBotAI().getMarkovMatrix();

            for (int i = 0; i < 3; i++) { //column
                for (int j = 0; j < 3; j++) {
                    scores[i][j].setText(String.valueOf(matrix[i][j]));
                }
            }
        }
    }
}

package rps.gui.controller;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import rps.bll.game.GameManager;
import rps.bll.game.Result;
import rps.bll.player.IPlayer;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class GameStatsController implements Initializable {
    @FXML
    private MFXTableView<Result> table;
    private GameManager gm;
    @Override

    public void initialize(URL location, ResourceBundle resources) {
        SetupTable();
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
        //table.setItems((ObservableList<Result>) gm.getGameState().getHistoricResults());
    }
}

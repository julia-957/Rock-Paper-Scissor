package rps.bll.player;

import rps.bll.game.Move;
import rps.bll.game.Result;

import java.util.ArrayList;
import java.util.Arrays;

public class MarkovChain {
    private int[][] matrix = new int[3][3];
    private Result result;
    private Move nextMove;

    public Move getNextMove(ArrayList<Result> results){
        result = results.get(results.size()-1);
        Move humanMove = result.getLoserMove();
        if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
            humanMove = result.getWinnerMove();

        int nextIndex = 0;
        for (int i =0; i < Move.values().length-1; i++){
            int prevIndex = humanMove.ordinal();

            if (matrix[prevIndex][i] > matrix[prevIndex][nextIndex])
                nextIndex = i;
        }
        nextMove = Move.values()[nextIndex];
        return nextMove;
    }

    public void updateMarkovChain(ArrayList<Result> results) {
        Result previous = results.get(results.size() - 2);
        Move previousMove = getHumanMove(previous);
        result = results.get(results.size() - 1);
        Move humanMove = getHumanMove(result);

        matrix[previousMove.ordinal()][humanMove.ordinal()]++;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    private Move getHumanMove(Result result){
        Move humanMove = result.getLoserMove();
        if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
            humanMove = result.getWinnerMove();
        return humanMove;
    }
}

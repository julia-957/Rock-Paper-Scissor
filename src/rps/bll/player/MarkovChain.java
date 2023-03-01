package rps.bll.player;

import rps.bll.game.Move;
import rps.bll.game.Result;

import java.util.ArrayList;

public class MarkovChain {
    private int[][] matrix = new int[3][3];
    private double[][] probability = new double[][]{{1.0/3,1.0/3,1.0/3},{1.0/3, 1.0/3, 1.0/3},{1.0/3, 1.0/3, 1.0/3}};
    private Result result;
    private Move nextMove;

    public Move getNextMove(ArrayList<Result> results){
        result = results.get(results.size()-1);
        Move humanMove = result.getLoserMove();
        if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
            humanMove = result.getWinnerMove();

        int nextIndex = 0;
        for (int i =0; i < Move.values().length; i++){
            int prevIndex = humanMove.ordinal();

            if (matrix[prevIndex][i] > matrix[prevIndex][nextIndex])
                nextIndex = i;
        }
        nextMove = Move.values()[nextIndex];
        return nextMove;
    }

    public void updateMarkovChain(ArrayList<Result> results) {
        if (results.size() > 1) {
            Result previous = results.get(results.size() - 2);
            Move previousMove = previous.getLoserMove();
            if (previous.getWinnerPlayer().getPlayerType() == PlayerType.Human)
                previousMove = previous.getWinnerMove();

            result = results.get(results.size() - 1);
            Move humanMove = result.getLoserMove();
            if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
                humanMove = result.getWinnerMove();

            matrix[previousMove.ordinal()][humanMove.ordinal()]++;

            for (int i = 0; i < 3; i++){
                int totalMoves = 0;
                for (int j = 0; j < 1; j++){
                    totalMoves += probability[i][j];
                    //System.out.print(probability[i][j] + " ");
                }
                probability[i][0] = 1.0/3;
                probability[i][1] = 1.0/3;
                probability[i][2] = 1.0/3;
                System.out.print(probability[i][0] + " ");
                System.out.print(probability[i][1] + " ");
                System.out.print(probability[i][2] + " ");
                System.out.print("\n");
            }
            System.out.println();
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }
}

package rps.bll.player;

import rps.bll.game.Move;
import rps.bll.game.Result;

import java.util.ArrayList;
import java.util.Arrays;

public class MarkovChain {
    private int[][] matrix = new int[3][3];
    private double[][] probability = new double[][]{{1.0/3,1.0/3,1.0/3},{1.0/3, 1.0/3, 1.0/3},{1.0/3, 1.0/3, 1.0/3}};
    private double[] predicted = new double[3];
    private Result result;
    private int rock = 1;
    private int paper = 2;
    private int scissors = 3;
    private Move computerMove, humanMove, humanPreviousMove;

    public MarkovChain(){
        //matrix [humanMove][botMove]
        matrix[rock-1][rock-1] = 0;
        matrix[rock-1][paper-1] = 1;
        matrix[rock-1][scissors-1] = -1;
        matrix[paper-1][rock-1] = -1;
        matrix[paper-1][paper-1] = 0;
        matrix[paper-1][scissors-1] = 1;
        matrix[scissors-1][rock-1] = 1;
        matrix[scissors-1][paper-1] = -1;
        matrix[scissors-1][scissors-1] = 0;
    }

    public Move getNextMove(ArrayList<Result> results){
        result = results.get(results.size()-1);
        Result prevResult = results.get(results.size()-2);

        humanMove = result.getLoserMove();
        if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
            humanMove = result.getWinnerMove();

        humanPreviousMove = result.getLoserMove();
        if (prevResult.getWinnerPlayer().getPlayerType() == PlayerType.Human)
            humanPreviousMove = result.getWinnerMove();

        // update trans
        int humanMoveIndex = humanMove.ordinal();
        int previousHumanMoveIndex = humanPreviousMove.ordinal();
        predicted[0] = matrix[humanMoveIndex][0] * probability[previousHumanMoveIndex][0];
        predicted[1] = matrix[humanMoveIndex][1] * probability[previousHumanMoveIndex][1];
        predicted[2] = matrix[humanMoveIndex][2] * probability[previousHumanMoveIndex][2];

        int computerMoveIndex = 0;
        for ( int i = 1; i < predicted.length; i++ ) {
            if (predicted[i] > predicted[computerMoveIndex]) computerMoveIndex = i;
        }

        computerMove = Move.values()[computerMoveIndex];
        probability[previousHumanMoveIndex][computerMoveIndex] += 1.0;
        probability[previousHumanMoveIndex] = normalize(probability[previousHumanMoveIndex]);

        return computerMove;
    }

    public double[][] getMatrix() {
        return probability;
    }

    private double[] normalize(double[] array) {
        double sum = Arrays.stream(array).sum();
        System.out.println(sum);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i] / sum;
            System.out.println(array[i]);
        }
        return array;
    }
}

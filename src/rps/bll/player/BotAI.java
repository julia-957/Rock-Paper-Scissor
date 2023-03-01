package rps.bll.player;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rps.bll.game.Move;
import rps.bll.game.Result;
import rps.bll.game.ResultType;

import java.io.IOException;
import java.util.*;

public class BotAI {
    private Result result;
    private final Move[] moves = {Move.Scissor, Move.Paper, Move.Rock};
    private TreeNode rockNode = new TreeNode(Move.Rock, 1, 0);
    private TreeNode paperNode = new TreeNode(Move.Paper, 1, 0);
    private TreeNode scissorsNode = new TreeNode(Move.Scissor, 1, 0);
    private HashMap<Move, TreeNode> mainNodes;
    private final TreeNode parentNode = new TreeNode(null, 0, 0);
    private MarkovChain markovChain = new MarkovChain();

    public BotAI() {
        mainNodes = new HashMap<>();
        mainNodes.put(Move.Rock, rockNode);
        mainNodes.put(Move.Paper, paperNode);
        mainNodes.put(Move.Scissor, scissorsNode);

        parentNode.getChildren().add(rockNode);
        parentNode.getChildren().add(paperNode);
        parentNode.getChildren().add(scissorsNode);
    }

    public Move botBasic(ArrayList<Result> results){
        if (results.size() > 0)
            result = results.get(results.size()-1);
        if(result == null){
            return botRandom();
        }
        if(result.getType() == ResultType.Win) {
         boolean humanWinner = result.getWinnerPlayer().getPlayerType() == PlayerType.Human;
         if ((result.getWinnerMove() == Move.Rock && humanWinner) || (result.getLoserMove() == Move.Scissor && !humanWinner)){
             return Move.Paper;
         }
         if ((result.getWinnerMove() == Move.Scissor && humanWinner) || (result.getLoserMove() == Move.Paper && !humanWinner)){
             return Move.Rock;
         }
         if ((result.getWinnerMove() == Move.Paper && humanWinner) || (result.getLoserMove() == Move.Rock && !humanWinner)){
             return Move.Scissor;
         }
        }
        if(result.getType()== ResultType.Tie){
         if(result.getWinnerMove() == Move.Rock){
             return Move.Scissor;
         }
         if(result.getWinnerMove() == Move.Scissor){
             return Move.Paper;
         }
         if(result.getWinnerMove() == Move.Paper){
             return Move.Rock;
         }
        }
     return null;
    }

    public Move botTreePattern(ArrayList<Result> results){
        return Move.Paper;
    }


    private Move botRandom(){
        return moves[getRandomNumber()];
    }

    private int getRandomNumber(){
        Random random = new Random();
        return random.nextInt(3);
    }

    public void updateTree(ArrayList<Result> results) {
        int amount = Math.min(results.size(), 7);
        if (amount > 0) {
            result = results.get(results.size() - 1);

            //Determine which move was human
            Move humanMove = result.getLoserMove();
            if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
                humanMove = result.getWinnerMove();

            TreeNode parent = mainNodes.get(humanMove);    //Get the main node
            for (int i = 1; i < amount + 1; i++) {
                humanMove = result.getLoserMove();
                if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
                    humanMove = result.getWinnerMove();

                if (parent.getChild(humanMove) == null)
                    parent.getChildren().add(new TreeNode(humanMove, i + 1, 1));
                else
                    parent = parent.getChild(humanMove);
            }
        }
    }

    public Move botMarkovChain(ArrayList<Result> results){
        markovChain.updateMarkovChain(results);
        Move chosenMove = moves[getRandomNumber()];
        if (results.size() > 0){
            Move predicted = markovChain.getNextMove(results);
            if (predicted == Move.Paper)
                chosenMove = Move.Scissor;
            else if (predicted == Move.Rock)
                chosenMove = Move.Paper;
            else
                chosenMove = Move.Rock;
        }
        return chosenMove;
    }

    public int[][] getMarkovMatrix(){
        return markovChain.getMatrix();
    }

    public void updateMarkovChain(ArrayList<Result> results){
        markovChain.updateMarkovChain(results);
    }
}

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

    public Move botWilhelm(ArrayList<Result> results){
        Move chosenMove = botRandom();
        if (results.size() > 0 && results.size() < 3)
            chosenMove = botBasic(results);
        else if (results.size() > 3) {
            if (getHumanMove(results.get(results.size()-3)) == getHumanMove(results.get(results.size()-2)) &&
                        getHumanMove(results.get(results.size()-2)) == getHumanMove(results.get(results.size()-1)))
                    chosenMove = returnCounterMove(getHumanMove(results.get(results.size()-1)));
            else
                chosenMove = botMarkovChain(results);
            }
        return chosenMove;
    }

    private Move botBasic(ArrayList<Result> results){
        System.out.println("Bot basic!");
        result = results.get(results.size()-1);

        Move chosenMove = null;
        if(result.getType() == ResultType.Win) {
         boolean humanWinner = result.getWinnerPlayer().getPlayerType() == PlayerType.Human;
         if ((result.getWinnerMove() == Move.Rock && humanWinner) || (result.getLoserMove() == Move.Scissor && !humanWinner)){
             chosenMove = Move.Paper;
         }
         if ((result.getWinnerMove() == Move.Scissor && humanWinner) || (result.getLoserMove() == Move.Paper && !humanWinner)){
             chosenMove = Move.Rock;
         }
         if ((result.getWinnerMove() == Move.Paper && humanWinner) || (result.getLoserMove() == Move.Rock && !humanWinner)){
             chosenMove = Move.Scissor;
         }
        }

        if(result.getType()== ResultType.Tie)
            chosenMove = returnCounterMove(result.getWinnerMove());
     return chosenMove;
    }

    private Move botRandom(){
        return moves[getRandomNumber()];
    }

    private int getRandomNumber(){
        Random random = new Random();
        return random.nextInt(3);
    }


    private Move botMarkovChain(ArrayList<Result> results){
        markovChain.updateMarkovChain(results);
        Move chosenMove = returnCounterMove(markovChain.getNextMove(results));
        return chosenMove;
    }

    private Move botSpam(ArrayList<Result> results){
        return returnCounterMove(getHumanMove(results.get(results.size()-1)));
    }

    public int[][] getMarkovMatrix(){
        return markovChain.getMatrix();
    }

    private Move getHumanMove(Result result){
        Move humanMove = result.getLoserMove();
        if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
            humanMove = result.getWinnerMove();
        return humanMove;
    }

    private Move returnCounterMove(Move move){
        if (move == Move.Rock)
            return Move.Paper;
        else if (move == Move.Paper)
            return Move.Scissor;
        else
            return Move.Rock;
    }
}

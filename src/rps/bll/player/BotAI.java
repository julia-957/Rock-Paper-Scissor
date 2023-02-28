package rps.bll.player;

import rps.bll.game.Move;
import rps.bll.game.Result;
import rps.bll.game.ResultType;

import java.util.*;

public class BotAI {
    private Result result;
    private final Move[] moves = {Move.Scissor, Move.Paper, Move.Rock};
    private TreeNode rockNode = new TreeNode(Move.Rock, 1, 0);
    private TreeNode paperNode = new TreeNode(Move.Paper, 1, 0);
    private TreeNode scissorsNode = new TreeNode(Move.Scissor, 1, 0);
    private HashMap<Move, TreeNode> mainNodes;
    private final TreeNode parentNode = new TreeNode(null, 0, 0);

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
            return moves[getRandomNumber()];
        }

        if(result.getType() == ResultType.Win) {
         if (result.getWinnerMove() == Move.Rock && result.getWinnerPlayer().getPlayerType() == PlayerType.Human) {
             return Move.Paper;
         }
         if (result.getWinnerMove() == Move.Scissor && result.getWinnerPlayer().getPlayerType() == PlayerType.Human) {
             return Move.Rock;
         }
         if (result.getWinnerMove() == Move.Paper && result.getWinnerPlayer().getPlayerType() == PlayerType.Human) {
             return Move.Scissor;
         }


         if (result.getLoserMove() == Move.Scissor && result.getLoserPlayer().getPlayerType() == PlayerType.Human) {
             return Move.Paper;
         }
         if (result.getLoserMove() == Move.Paper && result.getLoserPlayer().getPlayerType() == PlayerType.Human) {
             return Move.Rock;
         }
         if (result.getLoserMove() == Move.Rock && result.getLoserPlayer().getPlayerType() == PlayerType.Human) {
             return Move.Scissor;
         }
        }


        if(result.getType()== ResultType.Tie) {
         if(result.getWinnerMove() == Move.Rock && result.getWinnerPlayer().getPlayerType() == PlayerType.Human){
             return Move.Scissor;
         }
         if(result.getWinnerMove() == Move.Scissor && result.getWinnerPlayer().getPlayerType() == PlayerType.Human){
             return Move.Paper;
         }
         if(result.getWinnerMove() == Move.Paper && result.getWinnerPlayer().getPlayerType() == PlayerType.Human){
             return Move.Rock;
         }
        }

     return moves[getRandomNumber()];
    }

    public Move botTreePattern(ArrayList<Result> results){
        return Move.Paper;
    }


    public Move botRandom(){
        return moves[getRandomNumber()];
    }

    private int getRandomNumber(){
        Random random = new Random();
        return random.nextInt(3);
    }

    public void updateTree(ArrayList<Result> results) {
        int amount = Math.min(results.size(), 7);
        if (amount > 0) {
            result = results.get(results.size()-1);

            Move humanMove = result.getLoserMove();
            if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
                humanMove = result.getWinnerMove();

            TreeNode parent = mainNodes.get(humanMove);
            for (int i = 1; i < amount; i++){
                humanMove = result.getLoserMove();
                if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
                    humanMove = result.getWinnerMove();
                if (parent.getChild(humanMove) == null)
                    parent.getChildren().add(new TreeNode(humanMove, i+1, 1));
                else
                    parent = parent.getChild(humanMove);
            }
        }
    }
}

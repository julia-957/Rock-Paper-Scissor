package rps.bll.player;

import rps.bll.game.Move;
import rps.bll.game.Result;
import rps.bll.game.ResultType;

import java.util.Random;

public class BotAI {

    Result result = new Result();
    Move[] moves = {Move.Scissor, Move.Paper, Move.Rock};

    public Move botBasic(){

        if(result.getType() == null){
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

    public Move botRandom(){
        return moves[getRandomNumber()];
    }

    private int getRandomNumber(){
        Random random = new Random();
        return random.nextInt(3);
    }
}

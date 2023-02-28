package rps.bll.player;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
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
    private List<TreeNode> allNodes = new ArrayList<>();
    private HashMap<Integer, HBox> levelHBox = new HashMap<>();
    private MarkovChain markovChain = new MarkovChain();
    @FXML private VBox vbox;
    private Stage stage;

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

    private int getRandomNumber(){
        Random random = new Random();
        return random.nextInt(3);
    }

    public void updateTree(ArrayList<Result> results) {
        int amount = Math.min(results.size(), 7);
        if (amount > 0) {
            result = results.get(results.size()-1);

            //Determine which move was human
            Move humanMove = result.getLoserMove();
            if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
                humanMove = result.getWinnerMove();

            TreeNode parent = mainNodes.get(humanMove);    //Get the main node
            for (int i = 1; i < amount+1; i++){
                humanMove = result.getLoserMove();
                if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human)
                    humanMove = result.getWinnerMove();

                if (parent.getChild(humanMove) == null)
                    parent.getChildren().add(new TreeNode(humanMove, i+1, 1));
                else
                    parent = parent.getChild(humanMove);
            }
            //createVisibleNode();
        }
        /*
        try {
            visualizeMap();
        } catch (IOException e){
            e.printStackTrace();
        }

         */
    }

    private void visualizeMap() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/rps/gui/view/Tree.fxml"));
            vbox = fxmlLoader.load();
            Scene scene = new Scene(vbox);
            stage = new Stage();
            stage.setScene(scene);
        }catch (IOException e){
            e.printStackTrace();
        }
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().clear();
        vbox.getChildren().add(new HBox(new Button("Parent")));
        vbox.getChildren().add(new HBox(10, new Button(rockNode.getMove().toString()),
                new Button(paperNode.getMove().toString()), new Button(scissorsNode.getMove().toString())));
        stage.show();
    }

    private void createVisibleNode(){
        for (int i = 1; i <= levelHBox.size(); i++){
            HBox hbox = levelHBox.get(i);
            int level = i;
            List<TreeNode> children = allNodes.stream().filter(node -> node.getLevel() == level).toList();
            for (TreeNode treeNode: children){
                hbox.getChildren().add(new Button(treeNode.getMove().toString()));
            }
            vbox.getChildren().add(hbox);
        }
    }

    public Move botMarkovChain(ArrayList<Result> results){
        if (results.size() > 0){
            markovChain.updateMarkovChain(results);
            Move predicted = markovChain.getNextMove(results);
            if (predicted == Move.Paper)
                return Move.Scissor;
            else if (predicted == Move.Rock)
                return Move.Paper;
            else
                return Move.Rock;
        }
        else
            return moves[getRandomNumber()];
    }

    public int[][] getMarkovMatrix(){
        return markovChain.getMatrix();
    }
}

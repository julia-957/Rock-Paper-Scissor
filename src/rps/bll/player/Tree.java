package rps.bll.player;

import rps.bll.game.Move;

public class Tree {
    private TreeNode root;
    private int maxDepth;
    public Tree(int maxDepth){
        root = new TreeNode(Move.NULL, 0, 1);
        this.maxDepth = maxDepth;
    }
    public int getMaxDepth(){
        return maxDepth;
    }
    public TreeNode getRoot(){
        return root;
    }
}

package rps.bll.player;

import rps.bll.game.Move;

import java.util.LinkedList;
import java.util.List;

public class TreeNode {
    private Move move;
    private int level;
    private int usedCounter;
    private List<TreeNode> children;

    public TreeNode(Move move, int level, int usedCounter) {
        this.move = move;
        this.level = level;
        this.usedCounter = usedCounter;
        this.children = new LinkedList<TreeNode>();
    }

    public Move getMove() {
        return move;
    }

    public int getLevel() {
        return level;
    }

    public int getUsedCounter() {
        return usedCounter;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setUsedCounter(int usedCounter) {
        this.usedCounter = usedCounter;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public TreeNode getChild(Move move){
        return children.stream().filter(m -> m.getMove() == move).findFirst().orElse(null);
    }
    public TreeNode addChild(Move move, int level){
        if (this.level == level)
            return null;
        var child = getChild(move);
        if(child != null){
            child = getChild(move);
            child.setUsedCounter(child.getUsedCounter() + 1);
            return child;
        }
        child = new TreeNode(move, this.level + 1, 1);
        children.add(child);
        return child;
    }
}

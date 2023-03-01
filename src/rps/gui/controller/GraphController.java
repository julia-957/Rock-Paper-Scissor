package rps.gui.controller;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GraphController {
    private String previousMove;
    private Graph<String, String> graph;
    private SmartPlacementStrategy strategy;
    private SmartGraphPanel<String, String> graphView;
    public GraphController() {
        this.graph = new GraphEdgeList<>();
        /*
        graph.insertVertex("Rock");
        graph.insertVertex("Paper");
        graph.insertVertex("Scissors");

        graph.insertEdge("Rock", "Scissors", "Rock beats Scissors");
        graph.insertEdge("Paper", "Rock", "Paper beats Rock");
        graph.insertEdge("Paper", "Scissors", "Scissors beats Paper");

        graph.insertEdge("Scissors", "Scissors", "Scissors ties Scissors");
        graph.insertEdge("Rock", "Rock", "Rock ties Rock");
        graph.insertEdge("Paper", "Paper", "Paper ties Paper");
         */
        this.strategy = new SmartCircularSortedPlacementStrategy();
        this.graphView = new SmartGraphPanel<>(graph, strategy);
    }

    public SmartGraphPanel<String, String> getGraphView() {
        return graphView;
    }
    public void updateGraph(String move) {
        if (previousMove == null) {
            graph.insertVertex(move);
            previousMove = move;
        } else {
            graph.insertVertex(move);
            graph.insertEdge(previousMove, move, previousMove + " beats " + move);
            previousMove = move;
        }
        graphView.update();
    }
}

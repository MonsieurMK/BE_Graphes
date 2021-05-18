package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected void initialisation() {
        final ShortestPathData data = getInputData();

        // Initialisation
        ArrayList<LabelStar> labels = new ArrayList<>();
        LabelStar label;

        for (Node node : this.getInputData().getGraph().getNodes()) {
            if (node.equals(this.getInputData().getOrigin())) {
                label = new LabelStar(node, false, 0, null, calculerCout(node));
                this.getTas().insert(label);
                this.getLabelsNode().put(node, label);
                this.notifyOriginProcessed(node);
            } else {
                label = new LabelStar(node, false, Double.POSITIVE_INFINITY, null, calculerCout(node));
                this.getLabelsNode().put(node, label);
            }
            labels.add(label);
        }
    }

    private double calculerCout(Node node) {
        switch (this.data.getMode()) {
            case TIME:
                double maxSpeed;
                if (this.data.getMaximumSpeed() == GraphStatistics.NO_MAXIMUM_SPEED) {
                    maxSpeed = this.data.getGraph().getGraphInformation().getMaximumSpeed();
                } else {
                    maxSpeed = this.data.getMaximumSpeed();
                }
                return 3.6 * node.getPoint().distanceTo(this.getInputData().getDestination().getPoint())/maxSpeed;
            default:
                return node.getPoint().distanceTo(this.getInputData().getDestination().getPoint());
        }
    }

}

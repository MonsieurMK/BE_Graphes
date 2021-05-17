package org.insa.graphs.algorithm.shortestpath;

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
                label = new LabelStar(node, false, 0, null, this.calculDistance(node, this.getInputData().getDestination()));
                this.getTas().insert(label);
                this.getLabelsNode().put(node, label);
                this.notifyOriginProcessed(node);
            } else {
                label = new LabelStar(node, false, Double.POSITIVE_INFINITY, null, this.calculDistance(node, this.getInputData().getDestination()));
                this.getLabelsNode().put(node, label);
            }
            labels.add(label);
        }
    }

    private double calculDistance(Node origine, Node destination) {
        double lat_a, lat_b, lon_a, lon_b;
        lat_a = origine.getPoint().getLatitude();
        lon_a = origine.getPoint().getLongitude();
        lat_b = origine.getPoint().getLatitude();
        lon_b = origine.getPoint().getLongitude();
        return Math.sqrt(Math.pow(lat_b - lat_a, 2) + Math.pow(lon_b - lon_a, 2));
    }

}

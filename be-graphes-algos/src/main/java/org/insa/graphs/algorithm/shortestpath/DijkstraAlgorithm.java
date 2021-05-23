package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    private BinaryHeap tas;
    private HashMap<Node, Label> labelsNode;

    public BinaryHeap getTas() {
        return tas;
    }

    public HashMap<Node, Label> getLabelsNode() {
        return labelsNode;
    }

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        this.labelsNode = new HashMap<Node, Label>();
        this.tas = new BinaryHeap();
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();

        this.initialisation();

        // Iterations
        ArrayList<Arc> arcs = new ArrayList<>();
        Arc succ_dest = null;
        Node x;
        Label label = (Label) tas.findMin();
        x = label.getSommetCourant();
        while (!tas.isEmpty() && !x.equals(data.getDestination())) {
            label = (Label) tas.deleteMin();
            label.setMarque(true);
            x = label.getSommetCourant();
            this.notifyNodeMarked(x);
            Label labY;
            for (Arc successeur : x.getSuccessors()) {
                if (!data.isAllowed(successeur)) {
                    continue;
                }
                labY = labelsNode.get(successeur.getDestination());
                if (!labY.isMarque()) {
                    if (labY.getCout() > label.getCout() + data.getCost(successeur)) {
                        labY.setCout(label.getCout() + data.getCost(successeur));
                        tas.insert(labY);
                        labY.setPere(successeur);
                        this.notifyNodeReached(successeur.getDestination());
                    }
                }
                arcs.add(successeur);
            }
        }
        this.notifyDestinationReached(data.getDestination());

        // Creation chemin
        ArrayList<Arc> arcs_solution = new ArrayList<>();
        Arc successeur_courant = labelsNode.get(data.getDestination()).getPere();
        if (successeur_courant == null) {
            // il n'y a pas de chemin
            return new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        }
        arcs_solution.add(successeur_courant);
        Node node_courant = successeur_courant.getOrigin();
        while (successeur_courant != null) {
            successeur_courant = labelsNode.get(node_courant).getPere();
            if (successeur_courant != null) {
                node_courant = successeur_courant.getOrigin();
                arcs_solution.add(successeur_courant);
            }
        }

        Collections.reverse(arcs_solution);

        ShortestPathSolution solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(data.getGraph(), arcs_solution));

        return solution;
    }

    protected void initialisation() {
        final ShortestPathData data = getInputData();

        ArrayList<Label> labels = new ArrayList<>();
        Label label;

        for (Node node : data.getGraph().getNodes()) {
            if (node.equals(data.getOrigin())) {
                label = new Label(node, false, 0, null);
                tas.insert(label);
                labelsNode.put(node, label);
                this.notifyOriginProcessed(node);
            } else {
                label = new Label(node, false, Double.POSITIVE_INFINITY, null);
                labelsNode.put(node, label);
            }
            labels.add(label);
        }
    }

}

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

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        HashMap<Node, Label> labelsNode = new HashMap<Node, Label>();

        //this.getInputData().getMode();

        BinaryHeap tas = new BinaryHeap();

        // Initialisation
        ArrayList<Label> labels = new ArrayList<>();
        Label label;

        for (Node node : this.getInputData().getGraph().getNodes()) {
            if (node.equals(this.getInputData().getOrigin())) {
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

        // Iterations
        int nb_succ = 0;
        ArrayList<Arc> arcs = new ArrayList<>();
        Arc succ_dest = null;
        Node x;
        label = (Label) tas.findMin();
        x = label.getSommetCourant();
        while (!tas.isEmpty() && !x.equals(this.getInputData().getDestination())) {
            label = (Label) tas.deleteMin();
            label.setMarque(true);
            x = label.getSommetCourant();
            this.notifyNodeMarked(x);
            Label labY;
            nb_succ = 0;
            for (Arc successeur : x.getSuccessors()) {
                if (!this.getInputData().isAllowed(successeur)) {
                    continue;
                }
                nb_succ++;
                labY = labelsNode.get(successeur.getDestination());
                if (!labY.isMarque()) {
                    if (labY.getCout() > label.getCout() + successeur.getLength()) {
                        labY.setCout(label.getCout() + successeur.getLength());
                        tas.insert(labY);
                        labY.setPere(successeur);
                        this.notifyNodeReached(successeur.getDestination());
                    }
                }
                arcs.add(successeur);
            }
        }
        this.notifyDestinationReached(this.getInputData().getDestination());

        // Creation chemin
        ArrayList<Arc> arcs_solution = new ArrayList<>();
        Arc successeur_courant = labelsNode.get(this.getInputData().getDestination()).getPere();
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

        ShortestPathSolution solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(this.getInputData().getGraph(), arcs_solution));

        return solution;
    }

}

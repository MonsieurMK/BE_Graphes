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

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();

        BinaryHeap tas = new BinaryHeap();

        // Initialisation
        ArrayList<Label> labels = new ArrayList<>();
        Label label;
        Label labelDest = null;

        for (Node node : this.getInputData().getGraph().getNodes()) {
            if (node.equals(this.getInputData().getOrigin())) {
                label = new Label(node, false, 0, null);
                tas.insert(label);
            } else if (node.equals((this.getInputData().getDestination()))) {
                labelDest = new Label(node, false, Double.POSITIVE_INFINITY, null);
                label = labelDest;
            } else {
                label = new Label(node, false, Double.POSITIVE_INFINITY, null);
            }
            labels.add(label);
        }

        // Iterations
        ArrayList<Arc> arcs = new ArrayList<>();
        Arc succ_dest = null;
        Node x;
        label = (Label) tas.findMin();
        x = label.getSommetCourant();
        while (!tas.isEmpty() && !x.equals(this.getInputData().getDestination())) {
            label = (Label) tas.deleteMin();
            label.setMarque(true);
            x = label.getSommetCourant();
            Label labY;
            for (Arc successeur : x.getSuccessors()) {
                labY = this.getLabelNode(labels, successeur.getDestination());
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

        // Creation chemin
        ArrayList<Arc> arcs_solution = new ArrayList<>();
        //Arc successeur_courant = getLabelNode(labels, this.getInputData().getDestination()).getPere();
        Arc successeur_courant = labelDest.getPere();
        arcs_solution.add(successeur_courant);
        Node node_courant = successeur_courant.getOrigin();
        while (successeur_courant != null) {
            successeur_courant = getLabelNode(labels, node_courant).getPere();
            if (successeur_courant != null) {
                node_courant = successeur_courant.getOrigin();
                arcs_solution.add(successeur_courant);
            }
        }

        Collections.reverse(arcs_solution);

        ShortestPathSolution solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(this.getInputData().getGraph(), arcs_solution));

        return solution;
    }

    private Label getLabelNode(ArrayList<Label> labels, Node node) {
        for (Label label_courant : labels) {
            if (label_courant.getSommetCourant().equals(node)) {
                return label_courant;
            }
        }
        return null;
    }

}

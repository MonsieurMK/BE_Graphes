package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.ArrayList;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        // TODO:

        BinaryHeap tas = new BinaryHeap();

        // Initialisation
        ArrayList<Label> labels = new ArrayList<>();
        Label label;
        Label labelDest = null;

        for (Node node : this.getInputData().getGraph().getNodes()) {
            if (node.equals(this.getInputData().getOrigin())) {
                label = new Label(node, false, 0, null);
                tas.insert(label);
            } else if (node.equals(this.getInputData().getDestination())) {
                labelDest = new Label(node, false, 0, null);
                label = labelDest;
            } else {
                label = new Label(node, false, Double.POSITIVE_INFINITY, null);
            }
            labels.add(label);
        }

        // Iterations
        ArrayList<Arc> arcs = new ArrayList<>();
        Arc succ_dest = null;
        boolean fin = false;
        Node x;
        label = (Label) tas.findMin();
        x = label.getSommetCourant();
        while (!x.equals(this.getInputData().getDestination())) {
            label = (Label) tas.deleteMin();
            label.setMarque(true);
            x = label.getSommetCourant();
            Label labY;
            for (Arc successeur : x.getSuccessors()) {
                for (Label label_successeur : labels) {
                    if (label_successeur.getSommetCourant().equals(successeur.getDestination())) {
                        labY = label_successeur;
                        if (!labY.isMarque()) {
                            if (labY.getCout() > label.getCout() + successeur.getLength()) {
                                labY.setCout(label.getCout() + successeur.getLength());
                                //tas.insert(labY);
                                labY.setPere(successeur);
                            }
                        }
                        arcs.add(successeur);
                        if (x.equals(this.getInputData().getDestination())) {
                            succ_dest = successeur;
                        }
                    }
                }
            }

            // verif si tous sommets marqués
            // TODO optimiser
            fin = true;
            for (Label label_courant : labels) {
                if (!label_courant.isMarque()) {
                    fin = false;
                }
            }
        }
        // Creation chemin
        // TODO
        ArrayList<Arc> arcs_solution = new ArrayList<>();
        Arc successeur_courant = succ_dest;
        Node node_courant = successeur_courant.getOrigin();
        while (successeur_courant != null) {
            arcs.add(successeur_courant);
            for (Label label_courant : labels) {
                if (label_courant.getPere().equals(successeur_courant)) {
                    node_courant = label_courant.getSommetCourant();
                }
            }
            for (Label label_courant : labels) {
                if (label_courant.getPere().getDestination().equals(node_courant)) {
                    successeur_courant = label_courant.getPere();
                }
            }
        }

        ShortestPathSolution solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(this.getInputData().getGraph(), arcs));

        return solution;
    }

}

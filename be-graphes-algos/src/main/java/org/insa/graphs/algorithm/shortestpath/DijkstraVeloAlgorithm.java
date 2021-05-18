package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DijkstraVeloAlgorithm extends ShortestPathAlgorithm {

    public static final double CAR_ROAD_MULTIPLIER = 900000.0D;

    private BinaryHeap tas;
    private HashMap<Node, LabelVelo> labelsNode;

    public BinaryHeap getTas() {
        return tas;
    }

    public HashMap<Node, LabelVelo> getLabelsNode() {
        return labelsNode;
    }

    public DijkstraVeloAlgorithm(ShortestPathData data) {
        super(data);
        this.labelsNode = new HashMap<Node, LabelVelo>();
        this.tas = new BinaryHeap();
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();

        this.initialisation();

        // Iterations
        int nb_succ = 0;
        ArrayList<Arc> arcs = new ArrayList<>();
        Arc succ_dest = null;
        Node x;
        LabelVelo label = (LabelVelo) tas.findMin();
        x = label.getSommetCourant();
        while (!tas.isEmpty() && !x.equals(data.getDestination())) {
            label = (LabelVelo) tas.deleteMin();
            label.setMarque(true);
            x = label.getSommetCourant();
            this.notifyNodeMarked(x);
            LabelVelo labY;
            nb_succ = 0;
            for (Arc successeur : x.getSuccessors()) {
                if (!data.isAllowed(successeur)) {
                    continue;
                }
                nb_succ++;
                labY = labelsNode.get(successeur.getDestination());
                if (!labY.isMarque()) {
                    if (labY.getTotalCost() > this.calculerCout(label, successeur)) {
                        labY.setCout(this.calculerCout(label, successeur));
                        labY.setPere(successeur);
                        if(successeur.getRoadInformation().getAccessRestrictions().getRestrictionFor(AccessRestrictions.AccessMode.MOTORCAR) == AccessRestrictions.AccessRestriction.ALLOWED) {
                            labY.setSafeMultiplier(CAR_ROAD_MULTIPLIER);
                        } else {
                            labY.setSafeMultiplier(1.0D);
                        }
                        tas.insert(labY);
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

    private double calculerCout(LabelVelo label, Arc successeur) {
        if (successeur.getRoadInformation().getAccessRestrictions().getRestrictionFor(AccessRestrictions.AccessMode.MOTORCAR) == AccessRestrictions.AccessRestriction.ALLOWED) {
            return label.getTotalCost() + (successeur.getLength() * CAR_ROAD_MULTIPLIER);
        } else {
            return label.getTotalCost() + (successeur.getLength() * 1.0D);
        }
    }

    protected void initialisation() {
        final ShortestPathData data = getInputData();

        ArrayList<LabelVelo> labels = new ArrayList<>();
        LabelVelo label;

        for (Node node : data.getGraph().getNodes()) {
            if (node.equals(data.getOrigin())) {
                label = new LabelVelo(node, false, 0, null, 1.0D); // ou 0 ?
                tas.insert(label);
                labelsNode.put(node, label);
                this.notifyOriginProcessed(node);
            } else {
                label = new LabelVelo(node, false, Double.POSITIVE_INFINITY, null, CAR_ROAD_MULTIPLIER); // ou 0 ?
                labelsNode.put(node, label);
            }
            labels.add(label);
        }
    }
}

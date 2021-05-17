package org.insa.graphs.model;

public class LabelStar extends Label {

    private double coutDest;

    public LabelStar(Node sommetCourant, boolean marque, double cout, Arc pere, double coutDest) {
        super(sommetCourant, marque, cout, pere);
        this.coutDest = coutDest;
    }

    public double getCoutDest() {
        return coutDest;
    }

    public void setCoutDest(double coutDest) {
        this.coutDest = coutDest;
    }

    @Override
    public double getTotalCost() {
        return super.getTotalCost() + this.coutDest;
    }
}

package org.insa.graphs.model;

public class LabelVelo extends Label {
    private double safeMultiplier;

    public LabelVelo(Node sommetCourant, boolean marque, double cout, Arc pere, double safeMultiplier) {
        super(sommetCourant, marque, cout, pere);
        this.safeMultiplier = safeMultiplier;
    }

    public double getSafeMultiplier() {
        return safeMultiplier;
    }

    public void setSafeMultiplier(double safeMultiplier) {
        this.safeMultiplier = safeMultiplier;
    }
}

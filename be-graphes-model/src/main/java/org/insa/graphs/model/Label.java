package org.insa.graphs.model;

public class Label {
    private Node sommetCourant;
    private boolean marque;
    private double cout;
    private Node pere;

    public Label(Node sommetCourant, boolean marque, double cout, Node pere) {
        this.sommetCourant = sommetCourant;
        this.marque = marque;
        this.cout = cout;
        this.pere = pere;
    }

    public double getCout() {
        return cout;
    }

    public void setSommetCourant(Node sommetCourant) {
        this.sommetCourant = sommetCourant;
    }
}

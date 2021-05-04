package org.insa.graphs.model;

public class Label implements Comparable {
    private Node sommetCourant;
    private boolean marque;
    private double cout;
    private Arc pere;

    public Label(Node sommetCourant, boolean marque, double cout, Arc pere) {
        this.sommetCourant = sommetCourant;
        this.marque = marque;
        this.cout = cout;
        this.pere = pere;
    }

    public double getCout() {
        return cout;
    }

    public Node getSommetCourant() {
        return this.sommetCourant;
    }

    public void setSommetCourant(Node sommetCourant) {
        this.sommetCourant = sommetCourant;
    }

    public boolean isMarque() {
        return marque;
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public void setPere(Arc pere) {
        this.pere = pere;
    }

    public Arc getPere() {
        return this.pere;
    }

    @Override
    public int compareTo(Object o) {
        return (Double.compare(this.cout, ((Label) o).cout));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        } else if (o instanceof Label) {
            Label l = (Label) o;
            return (this.sommetCourant.equals(l.sommetCourant) && this.marque == l.marque && this.cout == l.cout && this.pere.equals(l.pere));
        }
        return false;
    }
}

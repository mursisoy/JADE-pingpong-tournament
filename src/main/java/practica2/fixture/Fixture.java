package practica2.fixture;

import jade.content.Concept;
import jade.content.onto.OntologyException;
import jade.core.AID;

import java.util.Objects;

public class Fixture<T extends Object> implements Concept {

    private T firstRegistration;
    private T secondRegistration;

    private int firstPoints;
    private int secondPoints;
    public Fixture(){}
    public Fixture(T firstRegistration, T secondRegistration) throws FixtureException {
        if (firstRegistration == secondRegistration) throw new FixtureException("Registration cannot be the same");
        this.firstRegistration = firstRegistration;
        this.secondRegistration = secondRegistration;
    }

    public T getFirstRegistration() {
        return firstRegistration;
    }

    public boolean isInvolved(T id){
        return firstRegistration.equals(id) || secondRegistration.equals(id);
    }

    public boolean isServer(T id) {
        return firstRegistration.equals(id);
    }

    public T getSecondRegistration() {
        return secondRegistration;
    }

    public void setPoints(T id, int points) {
        if (isServer(id)) {
            this.firstPoints = points;
        } else {
            this.secondPoints = points;
        }
    }

    public void setFirstRegistration(T firstRegistration) {
        this.firstRegistration = firstRegistration;
    }

    public void setSecondPoints(int secondPoints) {
        this.secondPoints = secondPoints;
    }

    public void setFirstPoints(int firstPoints) {
        this.firstPoints = firstPoints;
    }

    public void setSecondRegistration(T secondRegistration) {
        this.secondRegistration = secondRegistration;
    }

    public int getFirstPoints() {
        return firstPoints;
    }

    public int getSecondPoints() {
        return secondPoints;
    }

    public T getWinner() {
        if (firstPoints > secondPoints) {
            return firstRegistration;
        }
        if (secondPoints > firstPoints) {
            return secondRegistration;
        }
        return null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fixture)) return false;
        Fixture<?> fixture = (Fixture<?>) o;
        return Objects.equals(firstRegistration, fixture.firstRegistration) && Objects.equals(secondRegistration, fixture.secondRegistration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstRegistration, secondRegistration, firstPoints, secondPoints);
    }
}
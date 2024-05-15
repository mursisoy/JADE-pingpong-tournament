package practica2.ontology;

import jade.content.Predicate;
import jade.core.AID;
import practica2.fixture.Fixture;

public class TournamentResult implements Predicate {

    private Fixture<AID> result;

    public TournamentResult(Fixture<AID> result) {
        this.result = result;
    }

    public Fixture<AID> getResult() {
        return result;
    }
}

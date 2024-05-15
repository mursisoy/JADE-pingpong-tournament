package practica2.ontology;

import jade.content.Predicate;
import jade.core.AID;
import practica2.fixture.Fixture;
import practica2.player.FixtureActionResponder;

public class FixtureResult implements Predicate {

    private Fixture<AID> fixture;

    public FixtureResult(){}
    public FixtureResult(Fixture<AID> fixture) {
        this.fixture = fixture;
    }

    public void setFixture(Fixture<AID> fixture) {
        this.fixture = fixture;
    }

    public Fixture<AID> getFixture() {
        return fixture;
    }

}

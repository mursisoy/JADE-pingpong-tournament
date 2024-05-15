package practica2.ontology;

import jade.content.onto.*;
import jade.content.schema.*;
import practica2.fixture.Fixture;

public class PingPongOntology extends Ontology implements PingPongVocabulary{
    public static final String ONTOLOGY_NAME = "PINGPONG_ONTOLOGY";
    public static final String HIT = "Hit";
    public static final String MISS = "Miss";
    public static final String CALL4PLAYERS = "Call4Players";

    public static final String REGISTER_ACTION = "Register";
    public static final String REGISTER_PLAYER = "player";

    public static final String FIXTURE_ACTION = "FixtureRequest";

    public static final String FIXTURE_RECEIVER = "fixtureServer";
    public static final String FIXTURE_SERVER = "fixtureReceiver";

    public static final String FIXTURE_RESULT = "FixtureResult";
    public static final String FIXTURE = "fixture";


    // The singleton instance of this ontology
    private static Ontology theInstance = new PingPongOntology();

    // This is the method to access the singleton music shop ontology object
    public static Ontology getInstance() {
        return theInstance;
    }

    // Private constructor
    private PingPongOntology() {
        // The music shop ontology extends the basic ontology
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            add(new ConceptSchema(FIXTURE), Fixture.class);
            ConceptSchema f = (ConceptSchema) getSchema(FIXTURE);
            f.add("firstRegistration", (ConceptSchema) getSchema(BasicOntology.AID));
            f.add("secondRegistration", (ConceptSchema) getSchema(BasicOntology.AID));
            f.add("firstPoints", (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            f.add("secondPoints", (PrimitiveSchema) getSchema(BasicOntology.INTEGER));


            add(new PredicateSchema(HIT), Hit.class);
            add(new PredicateSchema(MISS), Miss.class);
            add(new PredicateSchema(CALL4PLAYERS), Call4Players.class);
            add(new PredicateSchema(FIXTURE_RESULT), FixtureResult.class);
            add(new AgentActionSchema(REGISTER_ACTION), RegisterAction.class);
            add(new AgentActionSchema(FIXTURE_ACTION), FixtureAction.class);

            PredicateSchema fr= (PredicateSchema) getSchema(FIXTURE_RESULT);
            fr.add(FIXTURE, (ConceptSchema) getSchema(FIXTURE), 1);

            AgentActionSchema rs = (AgentActionSchema) getSchema(REGISTER_ACTION);
            rs.add(REGISTER_PLAYER, (ConceptSchema) getSchema(BasicOntology.AID),1);

            AgentActionSchema fs = (AgentActionSchema) getSchema(FIXTURE_ACTION);
            fs.add(FIXTURE, (ConceptSchema) getSchema(FIXTURE), 1);
        } catch (OntologyException oe) {
            oe.printStackTrace();
        }
    }
}

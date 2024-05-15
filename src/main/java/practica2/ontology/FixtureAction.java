package practica2.ontology;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.MessageTemplate;
import practica2.fixture.Fixture;

public class FixtureAction implements AgentAction {
//    private AID fixtureServer;
//    private AID fixtureReceiver;

    private Fixture<AID> fixture;

    public FixtureAction(){}
    public FixtureAction(Fixture<AID> fixture){
        this.fixture = fixture;
    }

    public Fixture<AID> getFixture() {
        return this.fixture;
    }
    public void setFixture(Fixture<AID> fixture) {
        this.fixture = fixture;
    }
//    public FixtureAction(AID fixtureServer, AID fixtureReceiver) throws OntologyException {
//        if (fixtureServer == fixtureReceiver) {
//            throw new OntologyException("Server and receiver cannot be the same");
//        }
//        this.fixtureServer = fixtureServer;
//        this.fixtureReceiver = fixtureReceiver;
//    }
//    public AID getFixtureReceiver() {
//        return fixtureReceiver;
//    }
//
//    public AID getFixtureServer() {
//        return fixtureServer;
//    }

//    public void setFixtureReceiver(AID fixtureReceiver) throws OntologyException {
//        if (this.fixtureServer == fixtureReceiver) {
//            throw new OntologyException("Server and receiver cannot be the same");
//        }
//        this.fixtureReceiver = fixtureReceiver;
//    }
//    public void setFixtureServer(AID fixtureServer) throws OntologyException {
//        if (fixtureServer == this.fixtureReceiver) {
//            throw new OntologyException("Server and receiver cannot be the same");
//        }
//        this.fixtureServer = fixtureServer;
//    }

    public static MessageTemplate getMessageTemplate(ContentManager cm){
        return new MessageTemplate((MessageTemplate.MatchExpression) msg -> {
            try {
                System.out.println(msg.toString());
                ContentElement ce = (ContentElement) cm.extractContent(msg);
                Action a = (Action) ce;
                return a.getAction() instanceof FixtureAction;
            } catch (Exception e) {
                // Handle content extraction exception
                e.printStackTrace();
                return false;
            }
        });
    }
}

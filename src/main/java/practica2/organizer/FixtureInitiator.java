package practica2.organizer;

import jade.content.ContentElement;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import practica2.fixture.Fixture;
import practica2.ontology.FixtureAction;
import practica2.ontology.FixtureResult;
import practica2.ontology.RegisterAction;

import java.util.ArrayList;
import java.util.Vector;

public class FixtureInitiator extends AchieveREInitiator {

    private Tournament<AID> tournament;
    private Organizer myAgent;
    public FixtureInitiator(Organizer a, ACLMessage msg, Tournament<AID> tournament) {
        super(a, msg);
        this.myAgent = a;
        this.tournament =tournament;

    }
    protected void handleAgree(ACLMessage agree)
    {
        System.out.printf("%s: %s agrees on fixture\n", this.myAgent.getName(), agree.getSender().getName());
    }

    protected void handleRefuse(ACLMessage refuse)
    {
        System.out.printf("%s: %s refuses on fixture\n", this.myAgent.getName(), refuse.getSender().getName());
    }

    protected void handleNotUnderstood(ACLMessage notUnderstood)
    {
        System.out.printf("%s: %s don't undestand what's going on\n", this.myAgent.getName(), notUnderstood.getSender().getName());
    }

    @Override
    protected void handleAllResponses(Vector responses) {
        System.out.printf("Handle all responses\n");
        for ( Object response:responses ){
            ACLMessage reply = (ACLMessage) response;
            System.out.printf("%s\n", reply.toString());
        }
    }
    protected void handleAllResultNotifications(Vector resultNotifications) {
        System.out.printf("Handle all results\n");
        Fixture<AID> fixtureCheck = null;
        boolean allSame = true;
        for ( Object result:resultNotifications ){
            try {
                ContentElement ce = (ContentElement) myAgent.getContentManager().extractContent( (ACLMessage) result);
                if (ce instanceof FixtureResult) {
                    Fixture f = ((FixtureResult) ce).getFixture();
                    if (fixtureCheck == null) {
                        fixtureCheck = f;
                    } else {
                        allSame &= fixtureCheck.equals(f);
                    }
                }
            } catch (Codec.CodecException e) {
                throw new RuntimeException(e);
            } catch (OntologyException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Fixture winner: " + fixtureCheck.getWinner());
        tournament.addPoints(fixtureCheck.getWinner(), 1);
        myAgent.showTournamentInfo();
    }

    protected void handleFailure(ACLMessage fallo)
    {
        if (fallo.getSender().equals(myAgent.getAMS())) {
            System.out.println("Alguna de las centrales de bomberos no existe");
        }
        else
        {
            System.out.println("Fallo en central de bomberos " + fallo.getSender().getName()
                    + ": " + fallo.getContent().substring(1, fallo.getContent().length()-1));
        }
    }

    @Override
    public int onEnd() {
        return super.onEnd();
    }
}

package practica2.organizer;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import practica2.fixture.Fixture;
import practica2.ontology.FixtureAction;
import practica2.ontology.PingPongOntology;
import practica2.ontology.FixtureResult;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RoundMapBehaviour extends ParallelBehaviour {

    protected Organizer myAgent;

    private Codec codec = new SLCodec();
    private Ontology ontology = PingPongOntology.getInstance();

    private List<practica2.fixture.Fixture<AID>> round;

    private Tournament<AID> tournament;

    public RoundMapBehaviour(Organizer a, Tournament<AID> tournament) {
        super(a,0);
        this.myAgent = a;
        this.tournament = tournament;
//        ParallelBehaviour pb = new ParallelBehaviour();
        for (Fixture<AID> f : tournament.getRound(tournament.getCurrentRound())) {
            try {
//                FixtureAction fa = new FixtureAction(f.getFirstRegistration(), f.getSecondRegistration());
                FixtureAction fa = new FixtureAction(f);
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontology.getName());
                msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

                Action action = new Action(myAgent.getAID(), fa);
                this.myAgent.getContentManager().fillContent(msg, action);
                msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

                msg.addReceiver(f.getFirstRegistration());
                msg.addReceiver(f.getSecondRegistration());
                this.addSubBehaviour(new FixtureInitiator(this.myAgent, msg, tournament));
            } catch (Codec.CodecException ce) {
                ce.printStackTrace();
            } catch (OntologyException oe) {
                oe.printStackTrace();
            }
        }
    }

    @Override
    public int onEnd() {
        System.out.println("End RoundMap");
        myAgent.prepareNextRound();
        return super.onEnd();
    }
}

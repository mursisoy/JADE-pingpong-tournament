package practica2.organizer;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import practica2.ontology.Call4Players;
import practica2.ontology.PingPongOntology;
import practica2.ontology.PingPongVocabulary;

import java.util.Date;

public class Call4PlayersBehaviour extends OneShotBehaviour implements PingPongVocabulary{

    protected Organizer myAgent;
    private Codec codec = new SLCodec();
    private Ontology ontology = PingPongOntology.getInstance();

    private int numOfPlayers;

    public Call4PlayersBehaviour(Organizer a, int numOfPlayers) {
        super(a);
        myAgent = a;
        this.numOfPlayers = numOfPlayers;
    }
    @Override
    public void action() {
        ServiceDescription s = new ServiceDescription();
        s.setType(PLAYER);

        DFAgentDescription d = new DFAgentDescription();
        d.addLanguages(codec.getName());
        d.addServices(s);

        try {
            DFAgentDescription[] result = DFService.search(myAgent, d);
            if (result.length <= 0) {
                System.out.println("No players available.");
            } else {
                System.out.println("Looking for players....");
                // Creamos el mensaje CFP(Call For Proposal) cumplimentando sus par�metros
                ACLMessage cfpMessage = new ACLMessage(ACLMessage.CFP);
                cfpMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
                cfpMessage.setLanguage(codec.getName());
                cfpMessage.setOntology(ontology.getName());

//                AbsPredicate predicate = new AbsPredicate(PingPongOntology.CALL4PLAYERS);
//                Register r = new Register();
//                Action a = new Action(myAgent.getAID(), r);
                myAgent.getContentManager().fillContent(cfpMessage, new Call4Players());
                for (DFAgentDescription player: result) {
                    cfpMessage.addReceiver(player.getName());
                }
                cfpMessage.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
                myAgent.addBehaviour(new Call4PlayersInitiator(myAgent, cfpMessage, numOfPlayers));
            }
        }catch (Codec.CodecException ce) {
            ce.printStackTrace();
        } catch (OntologyException oe) {
            oe.printStackTrace();
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}

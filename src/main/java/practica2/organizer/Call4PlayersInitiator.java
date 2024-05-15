package practica2.organizer;


import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import practica2.ontology.RegisterAction;

import java.util.Vector;

public class Call4PlayersInitiator extends ContractNetInitiator {

    protected Organizer myAgent;

    private int numOfPlayers;

    Call4PlayersInitiator(Organizer a, ACLMessage m, int numOfPlayers){
        super(a, m);
        this.numOfPlayers = numOfPlayers;
        this.myAgent = a;
    }

    protected void handlePropose(ACLMessage propose, Vector acceptances) {
        System.out.printf("%s: Accept proposal from %s.\n", this.myAgent.getLocalName(), propose.getSender().getLocalName());
        ACLMessage reply = propose.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        try {
            ContentElement ce = (ContentElement) myAgent.getContentManager().extractContent(propose);
            if (ce instanceof Action) {
                Action a = (Action) ce;
                if (a.getAction() instanceof RegisterAction) {
                    RegisterAction r = (RegisterAction) a.getAction();
                    if (numOfPlayers > 0) {
                        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        System.out.printf("%s: Aceptando solicitud a %s .\n", myAgent.getLocalName(), r.getPlayer());
                        numOfPlayers--;
                    } else {
                        reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                        System.out.printf("%s: Rechazando solicitud a %s .\n", myAgent.getLocalName(), r.getPlayer());
                    }
                }
            }
        }catch (Codec.CodecException fe) {
            System.err.println("FIPAException in fill/extract Msgcontent:" + fe.getMessage());
        }catch (OntologyException oe) {
            oe.printStackTrace();
        }
        acceptances.add(reply);
    }

    protected void handleRefuse(ACLMessage refuse) {
        System.out.printf("%s: Refuse from %s.\n", this.myAgent.getLocalName(), refuse.getSender().getLocalName());
        System.out.printf("%s\n",refuse);
    }

    protected void handleInform(ACLMessage inform) {
        System.out.printf("%s: Inform done from %s.\n", this.myAgent.getLocalName(), inform.getSender().getLocalName());
        try {
            ContentElement ce = (ContentElement) myAgent.getContentManager().extractContent(inform);
            if (ce instanceof Action) {
                Action a = (Action) ce;
                if (a.getAction() instanceof RegisterAction) {
                    RegisterAction r = (RegisterAction) a.getAction();
                    System.out.printf("%s: Registrando a %s .\n", myAgent.getLocalName(), r.getPlayer());
                    try {
                        this.myAgent.registerPlayer(r.getPlayer());
                    } catch (Exception e){
                        System.err.println(e.getMessage());
                    }
                }
            }
        }catch (Codec.CodecException fe) {
            System.err.println("FIPAException in fill/extract Msgcontent:" + fe.getMessage());
        }catch (OntologyException oe) {
            oe.printStackTrace();
        }
    }

    protected void handleOutOfSequence(ACLMessage notUnderstood) {
        System.out.printf("TROLOLOLO");
    }

    protected void handleNotUnderstood(ACLMessage notUnderstood) {
    }

    protected void handleAllResponses(Vector responses, Vector acceptances) {
//        System.out.printf("Handle all responses\n");
//        for ( Object response:responses ){
//            ACLMessage reply = (ACLMessage) response;
//            System.out.printf("%s\n", reply.toString());
//        }
    }

    public int onEnd() {
        System.out.printf("%s: Contract-Net finished (%d).\n", myAgent.getLocalName(), this.getLastExitValue());
        myAgent.beginTournament();
        return this.getLastExitValue();
    }

}

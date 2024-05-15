package practica2.player;


import jade.content.lang.Codec;
import jade.content.ContentElement;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import practica2.ontology.*;

public class Call4PlayersResponder extends ContractNetResponder {

    protected Player myAgent;

    Call4PlayersResponder(Player a, MessageTemplate template){
        super(a, template);
        this.myAgent = a;
    }

    protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException{

        System.out.printf("%s: Llamamiento de jugadores recibido desde %s.\n", this.myAgent.getLocalName(), cfp.getSender().getLocalName());
        try {
            ContentElement ce = (ContentElement) myAgent.getContentManager().extractContent(cfp);
            if (ce instanceof Call4Players) {

                    if (Math.random() > 0.2) {
                        System.out.printf("%s: Preparando registro.\n", myAgent.getLocalName());
                        ACLMessage propose = cfp.createReply();
                        propose.setPerformative(ACLMessage.PROPOSE);
                        RegisterAction r = new RegisterAction();
                        r.setPlayer(myAgent.getAID());
                        Action a = new Action(myAgent.getAID(), r);
                        myAgent.getContentManager().fillContent(propose, a);
                        return propose;
                    } else {
                        System.out.printf("%s: Rechazo participar.\n", myAgent.getLocalName());
                        ACLMessage propose = cfp.createReply();
                        propose.setPerformative(ACLMessage.REFUSE);
                        return propose;
                    }
            }
        }catch (Codec.CodecException fe) {
            System.err.println("FIPAException in fill/extract Msgcontent:" + fe.getMessage());
        }catch (OntologyException oe) {
            oe.printStackTrace();
        }
        throw new RefuseException("Not able to propose such proposal");
    }
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        try {
            ACLMessage inform = accept.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            ContentElement ce = (ContentElement) myAgent.getContentManager().extractContent(propose);
            myAgent.getContentManager().fillContent(inform,ce);
            myAgent.beginTournament();
            Behaviour b = this.deregisterState("Dummy-Final");
            this.registerLastState(b,"Dummy-Final");
            return inform;
        }catch (Codec.CodecException fe) {
            System.err.println("FIPAException in fill/extract Msgcontent:" + fe.getMessage());
        }catch (OntologyException oe) {
            oe.printStackTrace();
        }

        throw new FailureException("Not able to propose such proposal");
    }

    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
    }

    @Override
    public int onEnd() {
        System.out.printf("Call4Players(%s) responder finished\n", myAgent.getLocalName());
        return super.onEnd();
    }
}

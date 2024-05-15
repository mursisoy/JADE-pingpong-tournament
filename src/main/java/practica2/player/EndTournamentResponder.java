package practica2.player;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ProposeResponder;

public class EndTournamentResponder extends ProposeResponder {

    private Player myAgent;
    public EndTournamentResponder(Player a, MessageTemplate mt) {
        super(a, mt);
        this.myAgent = a;
    }

    protected ACLMessage prepareResponse(ACLMessage propose) {
        System.out.println(propose);
        this.myAgent.endTournament();
        return propose.createReply();
    }
}

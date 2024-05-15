package practica2.player;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetResponder;
import jade.proto.ProposeResponder;
import practica2.ontology.FixtureAction;
import practica2.ontology.PingPongOntology;
import session3.CabinaTelefonica_Comportamiento;

public class TournamentBehaviour extends ParallelBehaviour {

    private Codec codec = new SLCodec();
    private Ontology ontology = PingPongOntology.getInstance();

    private Player myAgent;

    public TournamentBehaviour(Player a) {
        super(a, 1);
        this.myAgent = a;
        System.out.printf("Tournament for player %s  begins\n", a.getLocalName());
        MessageTemplate template = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        template = MessageTemplate.and(template, MessageTemplate.MatchLanguage(codec.getName()));
        template = MessageTemplate.and(template, MessageTemplate.MatchOntology(ontology.getName()));
        template = MessageTemplate.and(template, FixtureAction.getMessageTemplate(this.myAgent.getContentManager()));
        this.addSubBehaviour(new FixtureActionResponder(this.myAgent, template));
    }

    @Override
    public int onEnd() {
        System.out.println("TorunmanetBeheaviour ends");
        this.myAgent.endTournament();
        return super.onEnd();
    }
}

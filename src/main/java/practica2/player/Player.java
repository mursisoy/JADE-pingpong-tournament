package practica2.player;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import practica2.ontology.Call4Players;
import practica2.ontology.FixtureAction;
import practica2.ontology.PingPongOntology;
import practica2.ontology.PingPongVocabulary;

public class Player extends Agent implements PingPongVocabulary {
    protected double fitness = 10;
    private Codec codec = new SLCodec();
    private Ontology ontology = PingPongOntology.getInstance();

    private Behaviour wait4FixtureBehaviour;

    DFAgentDescription dfd;

    protected void setup(){
        this.fitness = Math.random() * 5 + 5;
        System.out.printf("Hola, soy el jugador %s en forma %f/10\n", getLocalName(), fitness);

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);

        ServiceDescription sd = new ServiceDescription();
        sd.setType(PLAYER);
        sd.setName(getName());
        dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addLanguages(codec.getName());
        dfd.addServices(sd);

        this.setAvailable();
    }

    protected void registerInDF(){
        try {
            DFAgentDescription[] dfds = DFService.search(this, dfd);
            if (dfds.length > 0 ) {
                DFService.deregister(this, dfd);
            }
            DFService.register(this, dfd);
            System.out.println(this.getLocalName() + " is ready.");
        }
        catch (Exception ex) {
            System.out.println("Failed registering with DF! Shutting down...");
            ex.printStackTrace();
            this.doDelete();
        }
    }
    protected void deregisterFromDF(){
        try {
            DFService.deregister(this);
            System.out.println("Deregistering with DF");
        } catch (Exception e) {}
    }


    public void setAvailable(){
//        SequentialBehaviour sb = new SequentialBehaviour();
        this.registerInDF();
        MessageTemplate template = ContractNetResponder
                .createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        template = MessageTemplate.and(template, MessageTemplate.MatchLanguage(codec.getName()));
        template = MessageTemplate.and(template, MessageTemplate.MatchOntology(ontology.getName()));
        template = MessageTemplate.and(template, Call4Players.getMessageTemplate(this.getContentManager()));
        addBehaviour(new Call4PlayersResponder(this, template));
    }

    public void beginTournament(){
        this.deregisterFromDF();
        addBehaviour(new TournamentBehaviour(this));
    }

    public void endTournament() {
        this.setAvailable();
    }

    protected void takeDown(){
        try { DFService.deregister(this); }
        catch (Exception e) {}
        System.out.println("El agente " + getLocalName() + " muere");
    }

    public double getFitness() {
        return fitness;
    }
}

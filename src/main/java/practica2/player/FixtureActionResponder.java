package practica2.player;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import practica2.fixture.Fixture;
import practica2.fixture.FixtureException;
import practica2.ontology.FixtureAction;
import practica2.ontology.FixtureResult;
import practica2.ontology.PingPongOntology;

public class FixtureActionResponder extends AchieveREResponder {

    //
    protected Player myAgent;

    private TournamentBehaviour tb;

    public FixtureActionResponder(Player a, MessageTemplate mt) {
        super(a, mt);
        Behaviour b = new FixtureActionBehaviour(a);
        this.registerPrepareResultNotification(b);
        this.myAgent = a;
    }

    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        System.out.printf("%s: Received a Request\n", myAgent.getLocalName());
        System.out.printf("%s\n", request.toString());
        ACLMessage agree = request.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        try {
            Action ce = (Action) myAgent.getContentManager().extractContent(request);
            FixtureAction fa = (FixtureAction) ce.getAction();

//            if ( !this.myAgent.getAID().getName().equals(fa.getFixtureServer().getName()) &&
//                    !this.myAgent.getAID().getName().equals(fa.getFixtureReceiver().getName())) {
//                throw new RefuseException("I am not involved in this fixture");
//            }
            if (!fa.getFixture().isInvolved(this.myAgent.getAID())) {
                throw new RefuseException("I am not involved in this fixture");
            }

        } catch (Codec.CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        return agree;
    }

    public class FixtureActionBehaviour extends SimpleBehaviour {

        protected double stamina = 10;
        protected double fitness = 10;
        protected int hits = 0;
        protected boolean serve;

        protected AID opponent;

        protected boolean done = false;

        private Codec codec = new SLCodec();
        private Ontology ontology = PingPongOntology.getInstance();

        protected Player myAgent;

        public FixtureActionBehaviour(Player a) {
            super(a);
            this.myAgent = a;
        }

        protected void hit() throws FailureException {
            myAgent.doWait(100);
            // https://en.wikipedia.org/wiki/Exponential_distribution#Random_variate_generation
            double accuracy = (10+Math.log(Math.random()))/10;
            ACLMessage ball = new ACLMessage(ACLMessage.FAILURE);
            ball.addReceiver(opponent);
            if ((this.stamina > 0 && accuracy > 0.7)  || this.serve) {
                this.hits++;
                this.stamina = this.stamina - (Math.exp(-this.fitness/this.hits));
                System.out.printf("%s: hits %02d\tstamina %.2f\taccuracy %.2f\n", myAgent.getLocalName(), this.hits, this.stamina,accuracy);
                ball.setPerformative(ACLMessage.CONFIRM);
                this.myAgent.send(ball);
            } else {
                this.myAgent.send(ball);
                throw new FailureException("Missed hit");
            }
        }

        public void action() {
            DataStore ds = this.getDataStore();
            AchieveREResponder fsm = (AchieveREResponder) this.getParent();
            ACLMessage response = (ACLMessage) ds.get(fsm.RESPONSE_KEY);
            ACLMessage request = (ACLMessage) ds.get(fsm.REQUEST_KEY);

            try {
                Action ce = (Action) myAgent.getContentManager().extractContent(request);
                FixtureAction fa = (FixtureAction) ce.getAction();
                if (fa.getFixture().isServer(this.myAgent.getAID())) {
                    this.opponent = fa.getFixture().getSecondRegistration();
                    if (this.hits == 0) {
                        this.serve = true;
                    }
                } else {
                    this.opponent = fa.getFixture().getFirstRegistration();
                }


                ACLMessage resNotification = null;

                // Serve
                if (this.serve) {
                    try {
                        this.hit();
                    } catch (FailureException e) {
                    }
                    this.serve = false;
                }

                ACLMessage ball;
                ball = myAgent.blockingReceive();
                if (ball.getPerformative() == ACLMessage.CONFIRM) {
                    try {
                        this.hit();
                    } catch (FailureException e) {
                        this.done = true;
                        resNotification = request.createReply();
                        resNotification.setPerformative(ACLMessage.INFORM);
                        fa.getFixture().setPoints(myAgent.getAID(), 0);
                        fa.getFixture().setPoints(this.opponent, 1);
                        FixtureResult fr = new FixtureResult(fa.getFixture());
                        myAgent.getContentManager().fillContent(resNotification, fr);
                        ds.put(fsm.RESULT_NOTIFICATION_KEY, resNotification);
                    }
                } else {
                    System.out.println("Agent " + myAgent.getLocalName() + " wins");
                    this.done = true;
                    resNotification = request.createReply();
                    resNotification.setPerformative(ACLMessage.INFORM);
                    fa.getFixture().setPoints(myAgent.getAID(), 1);
                    fa.getFixture().setPoints(this.opponent, 0);
                    FixtureResult fr = new FixtureResult(fa.getFixture());
                    myAgent.getContentManager().fillContent(resNotification, fr);
                    ds.put(fsm.RESULT_NOTIFICATION_KEY, resNotification);
                }
            } catch (Codec.CodecException e) {
                throw new RuntimeException(e);
            } catch (OntologyException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public void reset() {
            super.reset();
            this.stamina = 10;
            this.hits = 0;
            this.serve = false;
            this.opponent = null;
            this.done = false;
        }

        @Override
        public final boolean done() {
            return this.done;
        }
    }

}

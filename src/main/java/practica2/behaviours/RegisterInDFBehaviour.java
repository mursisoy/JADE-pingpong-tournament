package practica2.behaviours;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import practica2.ontology.PingPongVocabulary;

public class RegisterInDFBehaviour extends OneShotBehaviour implements PingPongVocabulary {

    private String serviceType;

    private Codec codec = new SLCodec();


    public RegisterInDFBehaviour(Agent a, String serviceType){
        super(a);
        this.serviceType = serviceType;
    }

    @Override
    public void action() {
        ServiceDescription sd = new ServiceDescription();
        sd.setType(this.serviceType);
        sd.setName(myAgent.getName());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());
        dfd.addLanguages(codec.getName());
        dfd.addServices(sd);
        try {
            DFAgentDescription[] dfds = DFService.search(myAgent, dfd);
            if (dfds.length > 0 ) {
                DFService.deregister(myAgent, dfd);
            }
            DFService.register(myAgent, dfd);
            System.out.println(myAgent.getLocalName() + " is ready.");
        }
        catch (Exception ex) {
            System.out.println("Failed registering with DF! Shutting down...");
            ex.printStackTrace();
            myAgent.doDelete();
        }
    }
}

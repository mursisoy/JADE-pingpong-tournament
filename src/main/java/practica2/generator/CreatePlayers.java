package practica2.generator;

import jade.core.*;
import jade.core.Runtime;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.Register;
import jade.domain.FIPAException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import practica2.organizer.Organizer;
import practica2.organizer.Tournament;
import practica2.player.Player;

public class CreatePlayers extends OneShotBehaviour {

    private int numberOfPlayers;
    private String playerClass;
    CreatePlayers(PlayerGenerator a, int numberOfPlayers, String playerClass) {
        super(a);
        this.numberOfPlayers = numberOfPlayers;
        this.playerClass = playerClass;
    }

    @Override
    public void action() {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.CONTAINER_NAME, "Players");
        ContainerController cc = rt.createAgentContainer(p);
        for (int i = 1; i <= this.numberOfPlayers; i++) {
            try{
            AgentController agentController = cc.createNewAgent(String.format("Player %d", i),"practica2.player.Player", null);
            agentController.start();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

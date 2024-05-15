package practica2.ontology;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.core.AID;
import jade.lang.acl.MessageTemplate;

public class RegisterAction implements AgentAction {
    private AID player;
    public RegisterAction(){
    }
    public RegisterAction(AID player){
        this.player = player;
    }
    public AID getPlayer() {
        return player;
    }
    public void setPlayer(AID id) {
        player = id;
    }

    public static MessageTemplate getMessageTemplate(ContentManager cm){
        return new MessageTemplate((MessageTemplate.MatchExpression) msg -> {
            System.out.printf("Check template: %s",msg);
            System.out.println(cm.toString());
            try {
                // Extract content as Action from ACLMessage
                ContentElement ce = (ContentElement) cm.extractContent(msg);
                // Check if the content is an instance of the specified concept in your ontology
                return ce instanceof RegisterAction;
            } catch (Exception e) {
                // Handle content extraction exception
                e.printStackTrace();
                return false;
            }
        });
    }
}

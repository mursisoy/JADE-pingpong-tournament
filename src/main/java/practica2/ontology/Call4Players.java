package practica2.ontology;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class Call4Players implements Predicate {
    public Call4Players(){}

    public static MessageTemplate getMessageTemplate(ContentManager cm){
        return new MessageTemplate((MessageTemplate.MatchExpression) msg -> {
            System.out.printf("Check template: %s",msg);
            System.out.println(cm.toString());
            try {
                // Extract content as Action from ACLMessage
                ContentElement ce = (ContentElement) cm.extractContent(msg);
                // Check if the content is an instance of the specified concept in your ontology
                return ce instanceof Call4Players;
            } catch (Exception e) {
                // Handle content extraction exception
                e.printStackTrace();
                return false;
            }
        });
    }
}

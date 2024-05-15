package practica2.generator;

import jade.core.Agent;

import javax.swing.*;

public class PlayerGenerator extends Agent{

    private JFrame frame;

    protected void setup() {
        this.showCreatePlayersForm();
    }

    public void showCreatePlayersForm() {
        frame = new JFrame("Create players");
        CreatePlayersForm of = new CreatePlayersForm(this);
        frame.setContentPane(of.getPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void createPlayers(int numberOfPlayers) {
        addBehaviour(new CreatePlayers(this, numberOfPlayers, "practica2.player.Player"));
        frame.dispose();
        this.doDelete();
    }
}

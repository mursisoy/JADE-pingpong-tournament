package practica2.organizer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrganizerForm {
    private JPanel panel;
    private JTextField tournamentName;
    private JTextField numberOfPlayers;
    private JButton createTournamentButton;

    public OrganizerForm(Organizer o) {
        createTournamentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int iNumberOfPlayers = 0;
                try {
                   iNumberOfPlayers = Integer.parseInt(numberOfPlayers.getText());
                } catch (NumberFormatException ex) {}
                if ( iNumberOfPlayers < 4 || iNumberOfPlayers > 20 ) {
                    JOptionPane.showMessageDialog(panel,
                            "Number of players must be between 10 and 20.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                o.createTournament(tournamentName.getText(), iNumberOfPlayers);
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}

package practica2.organizer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TournamentForm {
    private JLabel roundNumber;
    private JTable roundTable;
    private JTable classificationTable;
    private JPanel panel;
    private JButton playRoundButton;

    public TournamentForm(Organizer o) {

        playRoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                o.playRound();
                playRoundButton.setEnabled(false);
            }
        });

        DefaultTableModel classificationTableModel = new DefaultTableModel();
        classificationTable.setModel(classificationTableModel);
        classificationTableModel.addColumn("Player");
        classificationTableModel.addColumn("Points");

        DefaultTableModel roundTableModel = new DefaultTableModel();
        roundTable.setModel(roundTableModel);
        roundTableModel.addColumn("Player 1");
        roundTableModel.addColumn("Player 2");

    }

    public void setRoundNumber(String roundNumber) {
        this.roundNumber.setText(roundNumber);
    }

    public DefaultTableModel getClassificationTableModel() {
        return (DefaultTableModel)classificationTable.getModel();
    }

    public DefaultTableModel getRoundTableModel() {
        return (DefaultTableModel)roundTable.getModel();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void enablePlayRoundButton(){
        playRoundButton.setEnabled(true);
    }
}
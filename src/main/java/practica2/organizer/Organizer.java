package practica2.organizer;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.*;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;

import jade.wrapper.AgentContainer;
import practica2.behaviours.RegisterInDFBehaviour;
import practica2.fixture.Fixture;
import practica2.ontology.PingPongOntology;
import practica2.ontology.PingPongVocabulary;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Organizer extends Agent implements PingPongVocabulary {

    private List<AID> players;

    private Tournament tournament;
    private Codec codec = new SLCodec();
    private Ontology ontology = PingPongOntology.getInstance();

    private AgentContainer championshipContainerController;

    private JFrame frame;

    JDialog dialog = new JDialog();

    TournamentForm tournamentInfo;
    protected void setup() {
        this.showOrganizerForm();
    }

    public void showOrganizerForm() {
        frame = new JFrame("OrganizerForm");
        OrganizerForm of = new OrganizerForm(this);
        frame.setContentPane(of.getPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void createTournament(String tournamentName, int numberOfPlayers) {


        JOptionPane optionPane = new JOptionPane("Creating tournament....",
                JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

        frame.setContentPane(optionPane);


        // Register language and ontology
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);

        this.tournament = new Tournament(tournamentName);

        // Set this agent main behaviour
        SequentialBehaviour sb = new SequentialBehaviour();
        sb.addSubBehaviour(new RegisterInDFBehaviour(this, ORGANIZER));
        sb.addSubBehaviour(new WakerBehaviour(this, 500) {
        });
        sb.addSubBehaviour(new Call4PlayersBehaviour(this, numberOfPlayers));
        addBehaviour(sb);
        tournamentInfo = new TournamentForm(this);
    }

    public void beginTournament() {
        try {
            this.tournament.beginTournament();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.frame,
                    e.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.showTournamentInfo();
    }

    public void showTournamentInfo() {

        Stream<Map.Entry<AID, Integer>> classification = this.tournament.getClassification();


        frame.setContentPane(tournamentInfo.getPanel());
        frame.pack();

        tournamentInfo.setRoundNumber(String.format("%d/%d", this.tournament.getCurrentRound()+1, this.tournament.getRounds()));

        List<Fixture<AID>> round = tournament.getRound(this.tournament.getCurrentRound());
        tournamentInfo.getRoundTableModel().setRowCount(0);
        for (Fixture<AID> f : round) {
            tournamentInfo.getRoundTableModel().addRow(new Object[]{f.getFirstRegistration().getLocalName(), f.getSecondRegistration().getLocalName()});
        }
        tournamentInfo.getClassificationTableModel().setRowCount(0);
        classification.forEach((Map.Entry<AID, Integer> entry) -> {
            tournamentInfo.getClassificationTableModel().addRow(new Object[]{((AID) entry.getKey()).getLocalName(), entry.getValue()});
        });

    }

    public void registerPlayer(AID aid) throws Exception {
        if (this.tournament != null) {
            this.tournament.registerPlayer(aid);
        } else {
            throw new Exception("Tournament not created");
        }
    }

    public void playRound() {
//        List<Fixture<AID>> round = tournament.getRound(this.tournament.getCurrentRound());
        addBehaviour(new RoundMapBehaviour(this, tournament));
    }

    public void prepareNextRound (){
        this.tournament.nextRound();
        if (this.tournament.getCurrentRound() == this.tournament.getRounds()) {
            this.endTournament();
        } else {
            this.showTournamentInfo();
            tournamentInfo.enablePlayRoundButton();
        }
    }

    public void endTournament(){
        EndTournamentForm etf = new EndTournamentForm();
        List<Map.Entry<AID, Integer>> pole = (List<Map.Entry<AID, Integer>>) this.tournament.getClassification().collect(Collectors.toList());
        etf.getGold().setText(String.format("%s (%d points)",pole.get(0).getKey().getLocalName(), pole.get(0).getValue()));
        etf.getSilver().setText(String.format("%s (%d points)",pole.get(1).getKey().getLocalName(), pole.get(1).getValue()));
        etf.getBronze().setText(String.format("%s (%d points)",pole.get(2).getKey().getLocalName(), pole.get(2).getValue()));
        frame.setContentPane(etf.getPanel());
        frame.pack();
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception e) {
        }
        System.out.println("El agente " + getLocalName() + " muere");
    }


}

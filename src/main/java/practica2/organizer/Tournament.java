package practica2.organizer;

import jade.core.AID;
import practica2.fixture.Fixture;
import practica2.fixture.FixtureException;
import practica2.fixture.FixtureGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tournament<T extends Object> {

    protected String name;
    protected ConcurrentHashMap<AID, Integer> classification;

    protected List<List<Fixture<AID>>> rounds;

    protected boolean registrationOpen;

    protected int currentRound;

    public Tournament(String name) {
        this.classification = new ConcurrentHashMap<>();
        this.registrationOpen = true;
        this.currentRound = 0;
    }

    public void beginTournament() throws Exception {
        if (this.registrationOpen) {
            this.registrationOpen = false;
            this.generateFixtures();
        } else {
            throw new Exception("RegistrationsS are closed, tournament already running");
        }
    }

    public void registerPlayer(AID player) throws Exception {
        if (this.registrationOpen) {
            this.classification.put(player, 0);
        } else {
            throw new Exception("Registration are closed");
        }
    }

    protected void generateFixtures() throws FixtureException {
        FixtureGenerator<AID> fixtureGenerator = new FixtureGenerator();
        this.rounds = fixtureGenerator.getFixtures(this.classification.keySet().stream().collect(Collectors.toList()),true);
    }

    protected int getRounds(){
        return this.rounds.size();
    }

    protected List<Fixture<AID>> getRound(int round){
        return this.rounds.get(round);
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void nextRound() {
        currentRound = currentRound + 1;
    }

    public void addPoints(AID player, Integer points) {
        this.classification.compute(
                player, (k,v) -> v + points
        );
    }

    public Stream<Map.Entry<AID, Integer>> getClassification() {
        return this.classification.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));
    }
}

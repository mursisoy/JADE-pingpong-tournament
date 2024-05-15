package practica2.fixture;

import java.util.LinkedList;
import java.util.List;

// From https://github.com/codeecho/fixture-generator
public class FixtureGenerator<T extends Object> {

    public List<List<Fixture<T>>> getFixtures(List<T> teams, boolean includeReverseFixtures) throws FixtureException {

        int numberOfRegistration = teams.size();

        // If odd number of teams add a "ghost".
        boolean ghost = false;
        if (numberOfRegistration % 2 != 0) {
            numberOfRegistration++;
            ghost = true;
        }

        // Generate the fixtures using the cyclic algorithm.
        int totalRounds = numberOfRegistration - 1;
        int matchesPerRound = numberOfRegistration / 2;
        List<List<Fixture<T>>> rounds = new LinkedList<List<Fixture<T>>>();

        for (int round = 0; round < totalRounds; round++) {
            List<Fixture<T>> fixtures = new LinkedList<Fixture<T>>();
            for (int match = 0; match < matchesPerRound; match++) {
                int first = (round + match) % (numberOfRegistration - 1);
                int second = (numberOfRegistration - 1 - match + round) % (numberOfRegistration - 1);
                // Last team stays in the same place while the others
                // rotate around it.
                if (match == 0) {
                    second = numberOfRegistration - 1;
                }
                try {
                    fixtures.add(new Fixture<T>(teams.get(first), teams.get(second)));
                }catch (IndexOutOfBoundsException e){}
            }
            rounds.add(fixtures);
        }

        // Interleave so that first and second games are fairly evenly dispersed.
        List<List<Fixture<T>>> interleaved = new LinkedList<List<Fixture<T>>>();

        int evn = 0;
        int odd = (numberOfRegistration / 2);
        for (int i = 0; i < rounds.size(); i++) {
            if (i % 2 == 0) {
                interleaved.add(rounds.get(evn++));
            } else {
                interleaved.add(rounds.get(odd++));
            }
        }

        rounds = interleaved;

        // Last team can't be away for every game so flip them
        // to home on odd rounds.
        for (int roundNumber = 0; roundNumber < rounds.size(); roundNumber++) {
            if (roundNumber % 2 == 1) {
                Fixture fixture = rounds.get(roundNumber).get(0);
                rounds.get(roundNumber).set(0, new Fixture(fixture.getSecondRegistration(), fixture.getFirstRegistration()));
            }
        }

        if (includeReverseFixtures) {
            List<List<Fixture<T>>> reverseFixtures = new LinkedList<List<Fixture<T>>>();
            for (List<Fixture<T>> round : rounds) {
                List<Fixture<T>> reverseRound = new LinkedList<Fixture<T>>();
                for (Fixture<T> fixture : round) {
                    reverseRound.add(new Fixture<T>(fixture.getSecondRegistration(), fixture.getFirstRegistration()));
                }
                reverseFixtures.add(reverseRound);
            }
            rounds.addAll(reverseFixtures);
        }

        return rounds;
    }
}

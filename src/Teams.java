import java.util.ArrayList;
import java.util.Collections;

public class Teams {
    private Main main;
    public Teams(Main main){
        this.main = main;
    }
    public boolean teamStandingsAlreadyCalculated;
    int currentLeague;
    int currentLeagueBowlerSize;
    ArrayList<Bowlers.Bowler> bowlers;
    ArrayList<Leagues.League> leagues;
    ArrayList<Integer> teamsArray;
    boolean teamsArrayAlreadyCreated;
    public void init(){
        currentLeague = main.leaguesScript.currentLeague;
        bowlers = main.bowlersScript.bowlers;
        leagues = main.leaguesScript.leagues;
        teamsArray = main.standingsScript.teamsArray;
        teamsArrayAlreadyCreated = main.standingsScript.teamsArrayAlreadyCreated;
    }
    public static class Team{
        String name;
        int teamId;
        int leagueAffiliation;
        int wins;
        int losses;
        int ties;
        int currentOpposition;
        public Team(String name, int teamId, int leagueAffiliation){
            this.name = name;
            this.teamId = teamId;
            this.leagueAffiliation = leagueAffiliation;
        }
    }
    ArrayList<Team> teams = new ArrayList<>();
    public void calculateTeamStandings(){
        for (int j = 0; j < bowlers.size(); j++){
            if (bowlers.get(j).leagueAffiliation == currentLeague){
                currentLeagueBowlerSize = j + 1;
            }
        }
        int currentTeamScore;
        int opposingTeamScore;
        if (!teamStandingsAlreadyCalculated){ //only calculate once per week to avoid doubling team standings
            if (!teamsArrayAlreadyCreated) { //prevent shuffling twice
                for (int k = 0; k < teams.size(); k++){
                    teamsArray.add(k);
                }
                if (leagues.get(currentLeague).currentWeek >= 1) {
                    Collections.shuffle(teamsArray);
                }
                teamsArrayAlreadyCreated = true;
            }
            for (int i = 0; i < teams.size(); i += 2){
                currentTeamScore = 0;
                opposingTeamScore = 0;
                if (teams.get(i).leagueAffiliation == currentLeague){
                    int team1 = teamsArray.get(i);
                    int team2 = -1;
                    try {
                        team2 = teamsArray.get(i + 1);
                    }
                    catch (IndexOutOfBoundsException e){
                        team2 = -1;
                    }
                    if (teams.get(i).currentOpposition == -1){ //vacant teams
                        int teamBowlerSize = 0;
                        for (int k = 0; k < bowlers.size(); k++){
                            if (bowlers.get(k).leagueAffiliation == currentLeague && bowlers.get(k).teamId - 1 == i){
                                teamBowlerSize = k;
                            }
                        }
                        if (currentTeamScore >= (200 * leagues.get(currentLeague).gamesPerWeek * teamBowlerSize)){ //if facing vacant, team must get more than 200/game/person (with hdcp for hdcp leagues)
                            teams.get(i).wins++;
                        }
                        else{
                            teams.get(i).losses++;
                        }
                    }
                    else if (i > teams.get(i).currentOpposition) {
                        for (int j = 0; j < bowlers.size(); j++) {
                            if (bowlers.get(j).leagueAffiliation == currentLeague) {
                                if (bowlers.get(j).teamId - 1 == team1) {
                                    currentTeamScore += bowlers.get(j).currentWeekTotal + (bowlers.get(j).hdcp * leagues.get(currentLeague).gamesPerWeek);
                                }
                                if (bowlers.get(j).teamId - 1 == team2) {
                                    opposingTeamScore += bowlers.get(j).currentWeekTotal + (bowlers.get(j).hdcp * leagues.get(currentLeague).gamesPerWeek);
                                }
                            }
                        }
                        System.out.println("Team: " + (team1 + 1) + " score: " + currentTeamScore + " opponent score: " + opposingTeamScore);
                        if (currentTeamScore > opposingTeamScore) {
                            teams.get(team1).wins++;
                            try {
                                teams.get(team2).losses++;
                            }
                            catch (IndexOutOfBoundsException e){
                                //do nothing if vacant team
                            }
                        }
                        if (currentTeamScore < opposingTeamScore) {
                            teams.get(team1).losses++;
                            try {
                                teams.get(team2).wins++;
                            }
                            catch (IndexOutOfBoundsException e){}
                        }
                        if (currentTeamScore == opposingTeamScore) {
                            teams.get(team1).ties++;
                            try {
                                teams.get(team2).ties++;
                            }
                            catch (IndexOutOfBoundsException e){}
                        }
                    }
                }
            }
            if (main.bowlersScript.gamesEntered == leagues.get(currentLeague).gamesPerWeek && main.bowlersScript.currentBowler == currentLeagueBowlerSize) {
                teamStandingsAlreadyCalculated = true;
            }
        }
    }
}
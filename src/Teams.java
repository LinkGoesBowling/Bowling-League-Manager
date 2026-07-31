import java.util.ArrayList;
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
    public void init(){
        currentLeague = main.leaguesScript.currentLeague;
        bowlers = main.bowlersScript.bowlers;
        leagues = main.leaguesScript.leagues;
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
            for (int i = 0; i < teams.size(); i++){
                currentTeamScore = 0;
                opposingTeamScore = 0;
                if (teams.get(i).leagueAffiliation == currentLeague){
                    if (teams.get(i).currentOpposition == -1){
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
                                if (bowlers.get(j).teamId - 1 == i) {
                                    currentTeamScore += bowlers.get(j).currentWeekTotal + (bowlers.get(j).hdcp * leagues.get(currentLeague).gamesPerWeek);
                                }
                                if (bowlers.get(j).teamId == teams.get(i).currentOpposition) {
                                    opposingTeamScore += bowlers.get(j).currentWeekTotal + (bowlers.get(j).hdcp * leagues.get(currentLeague).gamesPerWeek);
                                }
                            }
                        }
                        System.out.println("Team: " + (i + 1) + " score: " + currentTeamScore + " opponent score: " + opposingTeamScore);
                        if (currentTeamScore > opposingTeamScore) {
                            teams.get(i).wins++;
                            teams.get(teams.get(i).currentOpposition).losses++;
                        }
                        if (currentTeamScore < opposingTeamScore) {
                            teams.get(i).losses++;
                            teams.get(teams.get(i).currentOpposition).wins++;
                        }
                        if (currentTeamScore == opposingTeamScore) {
                            teams.get(i).ties++;
                            teams.get(teams.get(i).currentOpposition).ties++;
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

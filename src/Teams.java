import java.util.ArrayList;

public class Teams {
    private Main main;
    public Teams(Main main){
        this.main = main;
    }
    public boolean teamStandingsAlreadyCalculated;
    int currentLeague;
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
            this.wins = wins;
            this.losses = losses;
            this.ties = ties;
            this.currentOpposition = currentOpposition;
        }
    }
    ArrayList<Team> teams = new ArrayList<>();
    public void calculateTeamStandings(){
        int currentTeamScore = 0;
        int opposingTeamScore = 0;
        if (teamStandingsAlreadyCalculated == false){ //only calculate once per week to avoid doubling team standings
            for (int i = 0; i < teams.size(); i++){
                currentTeamScore = 0;
                opposingTeamScore = 0;
                if (teams.get(i).leagueAffiliation == currentLeague){
                    for (int j = 0; j < main.currentLeagueBowlerSize; j++){
                        if (bowlers.get(j).leagueAffiliation == currentLeague) {
                            if (bowlers.get(j).teamId - 1 == i) {
                                currentTeamScore += bowlers.get(j).currentWeekTotal + (bowlers.get(j).hdcp * leagues.get(currentLeague).gamesPerWeek);
                            }
                            if (bowlers.get(j).teamId - 1 == teams.get(i).currentOpposition) {
                                opposingTeamScore += bowlers.get(j).currentWeekTotal + (bowlers.get(j).hdcp * leagues.get(currentLeague).gamesPerWeek);
                            }
                        }
                    }
                    try {
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
                    catch (IndexOutOfBoundsException e){
                        int teamBowlerSize = 0;
                        for (int k = 0; k < bowlers.size(); k++){
                            if (bowlers.get(k).leagueAffiliation == currentLeague && bowlers.get(k).teamId - 1== i){
                                teamBowlerSize++;
                            }
                        }
                        if (currentTeamScore >= (200 * leagues.get(currentLeague).gamesPerWeek * teamBowlerSize)){ //if facing vacant, team must get more than 200/game/person (with hdcp for hdcp leagues)
                            teams.get(i).wins++;
                        }
                        else{
                            teams.get(i).losses++;
                        }
                    }
                }
            }
            if (main.bowlersScript.gamesEntered == leagues.get(currentLeague).gamesPerWeek && main.bowlersScript.currentBowler == main.currentLeagueBowlerSize) {
                teamStandingsAlreadyCalculated = true;
            }
        }
    }
}

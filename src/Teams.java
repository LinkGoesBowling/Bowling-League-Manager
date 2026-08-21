import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

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
    ArrayList<WeeklyGames> weeklyGames = new ArrayList<>();
    boolean teamsArrayAlreadyCreated;
    public void init(){
        currentLeague = main.leaguesScript.currentLeague;
        bowlers = main.bowlersScript.bowlers;
        leagues = main.leaguesScript.leagues;
        teamsArray = main.standingsScript.teamsArray;
        teamsArrayAlreadyCreated = main.standingsScript.teamsArrayAlreadyCreated;
    }
    public static class WeeklyGames{
        int team;
        int leagueAffiliation;
        int score;
        int game;
        public WeeklyGames(int team, int leagueAffiliation, int score, int game){
            this.team = team;
            this.leagueAffiliation = leagueAffiliation;
            this.score = score;
            this.game = game;
        }
    }
    public static class Team{
        String name;
        int teamId;
        int leagueAffiliation;
        int wins;
        int losses;
        int ties;
        int currentOpposition;
        int currentScore;
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
                if (teams.get(i).leagueAffiliation == currentLeague) {
                    int team1 = teamsArray.get(i);
                    int team2;
                    try {
                        team2 = teamsArray.get(i + 1);
                    } catch (IndexOutOfBoundsException e) {
                        team2 = -1;
                    }
                    if (team2 == -1) { //vacant teams
                        int teamBowlerSize = 0;
                        for (int k = 0; k < bowlers.size(); k++) {
                            if (bowlers.get(k).leagueAffiliation == currentLeague && bowlers.get(k).teamId - 1 == i) {
                                teamBowlerSize++;
                            }
                        }
                        if (currentTeamScore >= (205 * leagues.get(currentLeague).gamesPerWeek * teamBowlerSize)) { //if facing vacant, team must get more than 204/game/person (with hdcp for hdcp leagues)
                            teams.get(i).wins += 2;
                        } else {
                            teams.get(i).losses += 2;
                        }
                    } else {
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
                            teams.get(team1).wins += 2; //adds 2 wins for total pins
                            try {
                                teams.get(team2).losses += 2;
                            } catch (IndexOutOfBoundsException e) {
                                //do nothing if vacant team
                            }
                        }
                        if (currentTeamScore < opposingTeamScore) {
                            teams.get(team1).losses += 2;
                            try {
                                teams.get(team2).wins += 2;
                            } catch (IndexOutOfBoundsException e) {
                            }
                        }
                        if (currentTeamScore == opposingTeamScore) {
                            teams.get(team1).ties += 2;
                            try {
                                teams.get(team2).ties += 2;
                            }
                            catch (IndexOutOfBoundsException e) {
                            }
                        }
                    }
                    for (int m = 0; m < leagues.get(currentLeague).gamesPerWeek; m++) {
                        for (int k = 0; k < teams.size(); k += 2) {
                            int teamOne = teamsArray.get(k);
                            int teamTwo = teamsArray.get(k + 1);
                            int teamOneScore = 0;
                            int teamTwoScore = 0;
                            try {
                                for (int l = 0; l < (weeklyGames.size()); l++) {
                                    if (weeklyGames.get(l).team - 1 == teamOne && weeklyGames.get(l).game  == m) { //game - 1
                                        teamOneScore += weeklyGames.get(l).score;
                                    }
                                    if (weeklyGames.get(l).team - 1 == teamTwo && weeklyGames.get(l).game == m){ //game - 1
                                        teamTwoScore += weeklyGames.get(l).score;
                                    }
                                }
                                System.out.println(
                                        "Game " + (m + 1) +
                                                ": Team " + (teamOne + 1) + " = " + teamOneScore +
                                                " vs Team " + (teamTwo + 1) + " = " + teamTwoScore
                                ); //test
                                if (teamOneScore > teamTwoScore){
                                    teams.get(teamOne).wins++;
                                    teams.get(teamTwo).losses++;
                                }
                                if (teamOneScore < teamTwoScore){
                                    teams.get(teamOne).losses++;
                                    teams.get(teamTwo).wins++;
                                }
                                if (teamOneScore == teamTwoScore){
                                    teams.get(teamOne).ties++;
                                    teams.get(teamTwo).ties++;
                                }
                            }
                            catch (IndexOutOfBoundsException e) {}
                        }
                    }
                }
            }
            if (main.bowlersScript.gamesEntered == leagues.get(currentLeague).gamesPerWeek && main.bowlersScript.currentBowler == currentLeagueBowlerSize) {
                teamStandingsAlreadyCalculated = true;
            }
        }
    }
    public void renameTeams(){
        System.out.println("Which team do you want to change? Type its number or type a non-number to exit:");
        Scanner reader = new Scanner(System.in);
        int chosenTeam;
        for (int i = 0; i < teams.size(); i++){
            System.out.println((i + 1) + ": " + teams.get(i).name);
        }
        try {
            int input = reader.nextInt();
            if (input >= 0 && input <= bowlers.size()) {
                chosenTeam = (input - 1);
            }
            else{
                main.userChoice();
                return;
            }
        }
        catch (InputMismatchException e){
            main.userChoice();
            return;
        }
        reader.nextLine();
        System.out.println("Chosen Team: " + (chosenTeam + 1));
        System.out.println("Type your desired team name or type ? to exit:");
        String input = reader.nextLine();
        if (input.equals("?")){
            main.userChoice();
            return;
        }
        teams.get(chosenTeam).name = input;
    }
}

import java.util.ArrayList;
import java.util.Collections;
public class Standings {
    private Main main;
    ArrayList<Integer> teamsArray;
    ArrayList<Teams.Team> teams;
    ArrayList<Leagues.League> leagues;
    int currentLeague;
    boolean teamsArrayAlreadyCreated;
    public boolean teamsArrayAlreadyShuffled = false;
    public Standings(Main main){
        this.main = main;
    }
    public void init() {
        teamsArray = new ArrayList<Integer>();
        teams = main.teamsScript.teams;
        currentLeague = main.leaguesScript.currentLeague;
        leagues = main.leaguesScript.leagues;
    }
    public void generateMatchups(){
        if (!teamsArrayAlreadyCreated) { //prevent shuffling twice
            for (int k = 0; k < teams.size(); k++){
                teamsArray.add(k);
            }
            if (!teamsArrayAlreadyShuffled) {
                Collections.shuffle(teamsArray);
            }
            teamsArrayAlreadyCreated = true;
        }
        System.out.println("Matchups:");
        if (main.leaguesScript.leagues.get(main.leaguesScript.currentLeague).currentWeek == 0) { //for first week, team opp. is in order (ex. 1 vs. 2, 3 vs. 4, etc.)
            for (int i = 0; i < teams.size(); i += 2){
                try {
                    teams.get(i).currentOpposition = i + 1;
                    teams.get(i + 1).currentOpposition = i;
                    System.out.println(teams.get(i).name + " against " + (teams.get(i + 1).name));
                }
                catch (IndexOutOfBoundsException e){ //set opposition to -1 (vacant team) in case of uneven number of teams
                    teams.get(i).currentOpposition = -1;
                    System.out.println(teams.get(i).name + " against Vacant");
                }
            }
        }
        else{ //randomly generate matchups after 1st week
            for (int j = 0; j < teams.size(); j += 2){
                int team1 = teamsArray.get(j);
                if (j + 1 >= teamsArray.size()) {
                    teams.get(team1).currentOpposition = -1;
                    System.out.println(teams.get(team1).name + " against Vacant");
                    continue;
                }
                int team2 = teamsArray.get(j + 1);
                teams.get(team1).currentOpposition = teams.get(team2).teamId;
                teams.get(team2).currentOpposition = teams.get(team1).teamId;
                System.out.println(teams.get(team1).name + " against " + (teams.get(team2).name));
            }
        }
    }
    public void printStandingsSheet(){
        System.out.println(leagues.get(currentLeague).name.toUpperCase() + " Week " + (leagues.get(currentLeague).currentWeek + 1));
        System.out.println("Team Standings:");
        teams.sort((a, b) -> Integer.compare(b.wins, a.wins));
        for (int i = 0; i < teams.size(); i++){
            System.out.println(teams.get(i).name + " Wins: " + teams.get(i).wins + " Losses: " + teams.get(i).losses + " Ties: " + teams.get(i).ties);
        }
        generateMatchups();
        System.out.println("Season Stat Leaders");
        System.out.println("Males: ");
        System.out.println("Average: ");
        main.bowlersScript.listBowlers("M", 3, "avg");
        System.out.println("High Game: ");
        main.bowlersScript.listBowlers("M", 3, "highGame");
        System.out.println("High Series: ");
        main.bowlersScript.listBowlers("M", 3, "highSeries");
        System.out.println("Females: ");
        System.out.println("Average: ");
        main.bowlersScript.listBowlers("F", 3, "avg");
        System.out.println("High Game: ");
        main.bowlersScript.listBowlers("F", 3, "highGame");
        System.out.println("High Series: ");
        main.bowlersScript.listBowlers("F", 3, "highSeries");
    }
}

import java.util.Scanner;
public class Main {
    Bowlers bowlersScript;
    Leagues leaguesScript;
    Standings standingsScript;
    Teams teamsScript;
    public Main(){
        bowlersScript = new Bowlers(this);
        leaguesScript = new Leagues(this);
        standingsScript = new Standings(this);
        teamsScript = new Teams(this);
        bowlersScript.init();
        leaguesScript.init();
        standingsScript.init();
        teamsScript.init();
    }
    void main() {
        if (leaguesScript.leagues.isEmpty()){ //condition not useful for now, but will be useful when there is save data
            leaguesScript.addNewLeague();
        }
        while (true){
            userChoice();
        }
    }
    public static int currentLeague = 0;
    public int currentLeagueBowlerSize;
    public void userChoice(){
        for (int i = 0; i < bowlersScript.bowlers.size(); i++){
            if (bowlersScript.bowlers.get(i).leagueAffiliation == currentLeague){
                currentLeagueBowlerSize = i + 1;
            }
        }
        Scanner reader = new Scanner(System.in);
        System.out.println("Current league: " + leaguesScript.leagues.get(currentLeague).name);
        System.out.println("Week " + (leaguesScript.leagues.get(currentLeague).currentWeek + 1));
        System.out.println("What do you want to do?");
        System.out.println("Type B to add new bowlers");
        System.out.println("Type G to add this week's games");
        System.out.println("Type A to list bowler ranks by average");
        System.out.println("Type M to list male bowler ranks by average");
        System.out.println("Type F to list female bowler ranks by average");
        System.out.println("Type N to create a new league");
        System.out.println("Type S to switch leagues");
        System.out.println("Type W to switch to next week");
        System.out.println("Type P to print standings sheet");
        System.out.println("Type T to list matchups");
        System.out.println("Type R to rename teams");
        System.out.println("Type E to edit bowlers");
        String choice = reader.next();
        if (choice.toUpperCase().equals("B")){
            bowlersScript.addNewBowlers();
        }
        if (choice.toUpperCase().equals("G")){
            bowlersScript.addGames();
        }
        if (choice.toUpperCase().equals("A")){
            bowlersScript.listBowlers("all", currentLeagueBowlerSize, "avg");
        }
        if (choice.toUpperCase().equals("M")){
            bowlersScript.listBowlers("M", currentLeagueBowlerSize, "avg");
        }
        if (choice.toUpperCase().equals("F")){
            bowlersScript.listBowlers("F", currentLeagueBowlerSize, "avg");
        }
        if (choice.toUpperCase().equals("N")){
            leaguesScript.addNewLeague();
        }
        if (choice.toUpperCase().equals("S")){
            leaguesScript.switchLeagues();
        }
        if (choice.toUpperCase().equals("W")){
            leaguesScript.leagues.get(currentLeague).currentWeek++;
            bowlersScript.currentBowler = 0;
            for (int i = 0; i < currentLeagueBowlerSize; i++){
                if (bowlersScript.bowlers.get(i).leagueAffiliation == currentLeague) {
                    bowlersScript.bowlers.get(i).currentWeekTotal = 0;
                }
            }
            standingsScript.teamsArrayAlreadyCreated = false;
            standingsScript.teamsArray.clear();
            userChoice();
        }
        if (choice.toUpperCase().equals("P")){
            standingsScript.printStandingsSheet();
        }
        if (choice.toUpperCase().equals("T")){
            standingsScript.generateMatchups();
        }
        if (choice.toUpperCase().equals("R")){
            teamsScript.renameTeams();
        }
        if (choice.toUpperCase().equals("E")){
            bowlersScript.editBowlers();
        }
    }
}

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Bowlers {
    private Main main;
    int currentLeague;
    ArrayList<Teams.Team> teams;
    ArrayList<Leagues.League> leagues;
    public int currentLeagueBowlerSize;
    public Bowlers(Main main){
        this.main = main;
    }
    public void init(){
        currentLeague = main.leaguesScript.currentLeague;
        teams = main.teamsScript.teams;
        leagues = main.leaguesScript.leagues;
        for (int i = 0; i < bowlers.size(); i++){
            currentLeagueBowlerSize = i + 1;
        }
    }
    int currentBowler;
    int gamesEntered;
    public class Bowler {
        String name;
        double pins;
        double highGame;
        double highSeries;
        double gameCount;
        String gender;
        double avg;
        double hdcp;
        int teamId;
        int leagueAffiliation;
        int highHandicapGame;
        int highHandicapSeries;
        double currentWeekTotal;

        public Bowler(String name, double pins, double gameCount, String gender, int teamId, int leagueAffiliation) {
            this.name = name;
            this.pins = pins;
            this.highGame = highGame;
            this.highSeries = highSeries;
            this.gameCount = gameCount;
            this.gender = gender;
            this.teamId = teamId;
            this.leagueAffiliation = leagueAffiliation;
            this.highHandicapGame = highHandicapGame;
            this.highHandicapSeries = highHandicapSeries;
            this.currentWeekTotal = currentWeekTotal;
        }
    }
    ArrayList<Bowlers.Bowler> bowlers = new ArrayList<>();
    public void calculateAvgAndHdcp() {
        for (int i = 0; i < currentLeagueBowlerSize; i++) {
            if (bowlers.get(i).gameCount == 0){
                bowlers.get(i).avg = 0;
            }
            else {
                bowlers.get(i).avg = bowlers.get(i).pins / bowlers.get(i).gameCount; //used to rank bowlers accurately
            }
            if (bowlers.get(i).avg >= leagues.get(currentLeague).baseScore) { //no negative handicaps
                bowlers.get(i).hdcp = 0;
            }
        }
    }
    public void addNewBowlers() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter bowler's name: ");
        String nameInput = reader.nextLine();
        System.out.println("Enter bowler's gender(M/F): ");
        String genderInput = reader.next().toUpperCase();
        if ((genderInput.toUpperCase().equals("M")) || (genderInput.toUpperCase().equals("F"))) {
            System.out.println("Enter bowler's team number: ");
            int teamInput = reader.nextInt();
            System.out.println("Are these details correct? Y/N:");
            System.out.println("Name: " + nameInput);
            System.out.println("Gender: " + genderInput);
            System.out.println("Team: " + teamInput);
            String confirm = reader.next();
            if (confirm.toUpperCase().equals("Y")) {
                bowlers.add(new Bowler(nameInput, 0d, 0, genderInput, teamInput, currentLeague));
                boolean teamAlreadyExists = false;
                for (int i = 0; i < teams.size(); i++){ //check if team already exists
                    if (teams.get(i).teamId == teamInput && teams.get(i).leagueAffiliation == currentLeague){
                        teamAlreadyExists = true;
                    }
                }
                if (teamAlreadyExists == false){
                    teams.add(new Teams.Team("Team " + teamInput, teamInput, currentLeague));
                    System.out.println("Team " + teamInput + " created");
                }
                System.out.println("Bowler successfully added");
                System.out.println("Add another bowler? Y/N");
                String confirm2 = reader.next();
                if (confirm2.toUpperCase().equals("Y")) {
                    addNewBowlers();
                }
                if (confirm2.toUpperCase().equals("N")) {
                    addGames();
                }
                if (!confirm2.toUpperCase().equals("Y") && !confirm2.toUpperCase().equals("N")){
                    System.out.println("Invalid input");
                    addNewBowlers();
                }
            }
            if (confirm.toUpperCase().equals("N")){
                addNewBowlers();
            }
            if (!confirm.toUpperCase().equals("Y") && !confirm.toUpperCase().equals("N")){
                System.out.println("Invalid input");
                addNewBowlers();
            }
        }
        else{
            System.out.println("Not a valid gender!");
            addNewBowlers();
        }
    }
    public void addGames(){
        for (int i = 0; i < bowlers.size(); i++){
            if (bowlers.get(i).leagueAffiliation == currentLeague){
                currentLeagueBowlerSize++;
            }
        }
        Scanner reader = new Scanner(System.in);
        int currentGame = 0;
        int seriesTotal = 0;
        for (int i = currentBowler; i < currentLeagueBowlerSize; i++){
            System.out.println(bowlers.get(i).name);
            for (int j = gamesEntered; j < leagues.get(currentLeague).gamesPerWeek; j++){
                while (true) {
                    System.out.println("Enter game " + (j + 1) + "'s scratch score (hdcp will be added for team standings) or type A for missed games:");
                    try {
                        currentGame = reader.nextInt();
                        if (currentGame < 0 || currentGame > 300) { //only allow scores between 0-300
                            System.out.println("Score must be between 0-300");
                            currentGame = 0;
                            continue;
                        }
                        break;
                    }
                    catch (InputMismatchException e) { //any non-number advances game without adding games or pins
                        reader.next();
                        gamesEntered++;
                        if (gamesEntered == leagues.get(currentLeague).gamesPerWeek){
                            currentBowler++;
                            gamesEntered = 0;
                        }
                        addGames();
                        return;
                    }
                }
                gamesEntered++;
                bowlers.get(i).pins += currentGame;
                bowlers.get(i).gameCount++;
                if (currentGame > bowlers.get(i).highGame){
                    bowlers.get(i).highGame = currentGame;
                }
                seriesTotal += currentGame;
                if (gamesEntered == leagues.get(currentLeague).gamesPerWeek){
                    if (bowlers.get(i).highSeries < seriesTotal) {
                        bowlers.get(i).highSeries = seriesTotal;
                    }
                    bowlers.get(i).currentWeekTotal = seriesTotal;
                    seriesTotal = 0;
                    gamesEntered = 0;
                    currentBowler++;
                }
            }
        }
        main.teamsScript.calculateTeamStandings();
    }
    public void listBowlers(String gender, int numOfBowlers, String stat) {
        calculateAvgAndHdcp();
        for (int j = 0; j < currentLeagueBowlerSize; j++) {
            if (bowlers.get(j).leagueAffiliation == currentLeague) {
                if (gender.equals("all") || (gender.equals("M") && bowlers.get(j).gender.equals("M")) || (gender.equals("F") && bowlers.get(j).gender.equals("F"))) {
                    if (stat.equals("avg")) {
                        bowlers.sort((a, b) -> Double.compare(b.avg, a.avg)); //sort bowlers highest average to lowest
                    }
                    if (stat.equals("highGame")){
                        bowlers.sort((a, b) -> Double.compare(b.highGame, a.highGame));
                    }
                    if (stat.equals("highSeries")){
                        bowlers.sort((a, b) -> Double.compare(b.highSeries, a.highSeries));
                    }
                }
            }
        }
        int k = 0;
        for (int i = 0; i < currentLeagueBowlerSize && k < numOfBowlers; i++) { //list top 3 bowlers of selected gender
            calculateAvgAndHdcp();
            if (currentLeague == bowlers.get(i).leagueAffiliation) {
                if (gender.equals("all") || (gender.equals("F") && bowlers.get(i).gender.equals("F")) || (gender.equals("M") && bowlers.get(i).gender.equals("M"))) {
                    DecimalFormat removeTrailingZeros = new DecimalFormat("0.#");
                    if (stat == "avg"){
                        System.out.println(bowlers.get(i).name + " " +  removeTrailingZeros.format(bowlers.get(i).avg));
                    }
                    if (stat == "highGame"){
                        System.out.println(bowlers.get(i).name + " " + removeTrailingZeros.format(bowlers.get(i).highGame));
                    }
                    if (stat == "highSeries"){
                        System.out.println(bowlers.get(i).name + " " + removeTrailingZeros.format(bowlers.get(i).highSeries));
                    }
                    k++;
                }
            }
        }
    }
}

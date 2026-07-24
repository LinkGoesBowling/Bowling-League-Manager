import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
public class Script {
    public int gamesPerWeek = 0;
    public String leagueName = "";
    public int currentLeague = 0;
    public int currentBowler = 0;
    public int gamesEntered = 0;
    public double seriesTotal = 0;
    public double currentGame = 0;
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
        }
    }
    ArrayList<League> leagues = new ArrayList<>();
    public class League{
        String name;
        int weeks;
        int currentWeek;
        int baseScore;
        int percent;
        public League(String name, int weeks, int currentWeek, int baseScore, int percent){
            this.name = name;
            this.weeks = weeks;
            this.currentWeek = currentWeek;
            this.baseScore = baseScore;
            this.percent = percent;
        }
    }
    ArrayList<Bowler> bowlers = new ArrayList<>();
    public class Team{
        String name;
        int teamId;
        int leagueAffiliation;
        int wins;
        int losses;
        public Team(String name, int teamId, int leagueAffiliation){
            this.name = name;
            this.teamId = teamId;
            this.leagueAffiliation = leagueAffiliation;
            this.wins = wins;
            this.losses = losses;
        }
    }
    ArrayList<Team> teams = new ArrayList<>();
    public void userChoice(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Current league: " + leagues.get(currentLeague).name);
        System.out.println("Week " + leagues.get(currentLeague).currentWeek);
        System.out.println("What do you want to do?");
        System.out.println("Type B to add new bowlers");
        System.out.println("Type G to add this week's games");
        System.out.println("Type A to list bowler ranks by avergae");
        System.out.println("Type M to list male bowler ranks by average");
        System.out.println("Type F to list female bowler ranks by average");
        System.out.println("Type N to create a new league");
        System.out.println("Type S to switch leagues");
        System.out.println("Type W to switch to next week");
        System.out.println("Type P to print standings sheet");
        String choice = reader.next();
        if (choice.toUpperCase().equals("B")){
            addNewBowlers();
        }
        if (choice.toUpperCase().equals("G")){
            addGames();
        }
        if (choice.toUpperCase().equals("A")){
            listBowlers("all", bowlers.size(), "avg");
        }
        if (choice.toUpperCase().equals("M")){
            listBowlers("M", bowlers.size(), "avg");
        }
        if (choice.toUpperCase().equals("F")){
            listBowlers("F", bowlers.size(), "avg");
        }
        if (choice.toUpperCase().equals("N")){
            addNewLeague();
        }
        if (choice.toUpperCase().equals("S")){
            switchLeagues();
        }
        if (choice.toUpperCase().equals("W")){
            leagues.get(currentLeague).currentWeek++;
            currentBowler = 0;
            userChoice();
        }
        if (choice.toUpperCase().equals("P")){
            printStandingsSheet();
        }
    }
    public void addNewLeague(){
        Scanner reader = new Scanner(System.in);
        System.out.println("What would you like to name your league?");
        leagueName = reader.nextLine();
        System.out.println("How many games do you want per week?");
        gamesPerWeek = reader.nextInt();
        System.out.println("What do you want for your handicap percentage? (ex. 90) (Use 100 for scratch league and do not include %)");
        int percent = reader.nextInt();
        System.out.println("What do you want for your base score? (ex. 220) (Use 0 for scratch leagues)");
        int baseScore = reader.nextInt();
        leagues.add(new League (leagueName, gamesPerWeek, 1, baseScore, percent));
        System.out.println("League successfully added");
        for (int i = 0; i < leagues.size(); i++){ //switch to newly created league
            currentLeague = i;
        }
        addNewBowlers();
    }
    public void switchLeagues(){
        System.out.println("Which league do you want to switch to? Type league's number:");
        Scanner reader = new Scanner(System.in);
        for (int i = 0; i < leagues.size(); i++){
            System.out.println((i) + ": " + leagues.get(i).name);
        }
        currentLeague = reader.nextInt();
    }
    public void calculateAvgAndHdcp() {
        for (int i = 0; i < bowlers.size(); i++) {
            bowlers.get(i).avg = bowlers.get(i).pins / bowlers.get(i).gameCount; //used to rank bowlers accurately
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
                    teams.add(new Team("Team " + teamInput, teamInput, currentLeague));
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
        Scanner reader = new Scanner(System.in);
        currentGame = 0;
        for (int i = currentBowler; i < bowlers.size(); i++){
            System.out.println(bowlers.get(i).name);
            for (int j = gamesEntered; j < gamesPerWeek; j++){
                while (true) {
                    System.out.println("Enter game " + (j + 1) + "'s scratch score (hdcp will be added for team standings) or type A for missed games:");
                    try {
                        currentGame = reader.nextDouble();
                        if (currentGame < 0 || currentGame > 300) { //only allow scores between 0-300
                            System.out.println("Score must be between 0-300");
                            currentGame = 0;
                            continue;
                        }
                        break;
                    }
                    catch (InputMismatchException e) { //any non-number advances game without adding games or pins
                        gamesEntered++;
                        continue;
                    }
                }
                gamesEntered++;
                bowlers.get(i).pins += currentGame;
                bowlers.get(i).gameCount++;
                if (currentGame > bowlers.get(i).highGame){
                    bowlers.get(i).highGame = currentGame;
                }
                seriesTotal += currentGame;
                if (gamesEntered == gamesPerWeek){
                    if (bowlers.get(i).highSeries < seriesTotal) {
                        bowlers.get(i).highSeries = seriesTotal;
                    }
                    seriesTotal = 0;
                    gamesEntered = 0;
                    currentBowler++;
                }
            }
        }
    }
    public void listBowlers(String gender, int numOfBowlers, String stat) {
        calculateAvgAndHdcp();
        for (int j = 0; j < bowlers.size(); j++) {
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
        for (int i = 0; i < bowlers.size() && k < numOfBowlers; i++) { //list top 3 bowlers of selected gender
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
    public void printStandingsSheet(){
        System.out.println(leagues.get(currentLeague).name.toUpperCase() + " Week " + leagues.get(currentLeague).currentWeek);
        for (int i = 0; i < teams.size(); i++){
            if (teams.get(i).leagueAffiliation == currentLeague){
                //win-loss records not currently working
                System.out.println(teams.get(i).name + " Wins: " + teams.get(i).wins + " Losses: " + teams.get(i).losses);
            }
        }
        System.out.println("Season Stat Leaders");
        System.out.println("Males: ");
        System.out.println("Average: ");
        listBowlers("M", 3, "avg");
        System.out.println("High Game: ");
        listBowlers("M", 3, "highGame");
        System.out.println("High Series: ");
        listBowlers("M", 3, "highSeries");
        System.out.println("Females: ");
        System.out.println("Average: ");
        listBowlers("F", 3, "avg");
        System.out.println("High Game: ");
        listBowlers("F", 3, "highGame");
        System.out.println("High Series: ");
        listBowlers("F", 3, "highSeries");
    }
    public void main() {
        Script script = new Script();
        if (leagues.size() == 0){
            script.addNewLeague();
        }
        while (true){
            script.userChoice();
        }
    }
}

import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Collections;
public class Script {
    public int gamesPerWeek;
    public String leagueName;
    public int currentLeague;
    public int currentBowler;
    public int gamesEntered;
    public double seriesTotal;
    public double currentGame;
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
    ArrayList<Bowler> bowlers = new ArrayList<>();
    public class League{
        String name;
        int weeks;
        int currentWeek;
        int baseScore;
        int percent;
        public League(String name, int weeks, int baseScore, int percent){
            this.name = name;
            this.weeks = weeks;
            this.currentWeek = currentWeek;
            this.baseScore = baseScore;
            this.percent = percent;
        }
    }
    ArrayList<League> leagues = new ArrayList<>();
    public class Team{
        String name;
        int teamId;
        int leagueAffiliation;
        int wins;
        int losses;
        int currentOpposition;
        public Team(String name, int teamId, int leagueAffiliation){
            this.name = name;
            this.teamId = teamId;
            this.leagueAffiliation = leagueAffiliation;
            this.wins = wins;
            this.losses = losses;
            this.currentOpposition = currentOpposition;
        }
    }
    ArrayList<Team> teams = new ArrayList<>();
    public void userChoice(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Current league: " + leagues.get(currentLeague).name);
        System.out.println("Week " + (leagues.get(currentLeague).currentWeek + 1));
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
        System.out.println("Type T to list matchups");
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
        if (choice.toUpperCase().equals("T")){ //test generateLaneAssignments() method
            generateMatchups();
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
        leagues.add(new League (leagueName, gamesPerWeek, baseScore, percent));
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
                        reader.next();
                        gamesEntered++;
                        if (gamesEntered == gamesPerWeek){
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
                if (gamesEntered == gamesPerWeek){
                    if (bowlers.get(i).highSeries < seriesTotal) {
                        bowlers.get(i).highSeries = seriesTotal;
                    }
                    seriesTotal = 0;
                    gamesEntered = 0;
                    currentBowler++;
                    calculateTeamStandings();
                }
            }
        }
    }
    public void calculateTeamStandings(){
        //resume here
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
        generateMatchups();
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
    ArrayList<Integer> teamsArray = new ArrayList<Integer>();
    public void generateMatchups(){
        teamsArray.clear();
        for (int k = 0; k < teams.size(); k++){
            teamsArray.add(k);
        }
        Collections.shuffle(teamsArray);
        System.out.println("Matchups:");
        if (leagues.get(currentLeague).currentWeek == 0) { //for first week, team opp. is in order (ex. 1 vs. 2, 3 vs. 4, etc.)
            for (int i = 0; i < teams.size(); i += 2){
                    try {
                        teams.get(i).currentOpposition = teams.get(i).teamId + 1;
                        teams.get(i + 1).currentOpposition = teams.get(i + 1).teamId - 1;
                        System.out.println(teams.get(i).name + " against " + teams.get(teams.get(i).currentOpposition + 1).name);
                    }
                    catch (IndexOutOfBoundsException e){ //set opposition to -1 (vacant team) in case of uneven number of teams
                        teams.get(i).currentOpposition = -1;
                        System.out.println(teams.get(i).name + " against Vacant");
                    }
            }
        }
        else{
            for (int j = 0; j < teams.size(); j += 2){ //randomly generate matchups after first week
                try {
                    int team1 = teamsArray.get(j);
                    int team2 = teamsArray.get(j + 1);
                    teams.get(team1).currentOpposition = teams.get(team2).teamId;
                    teams.get(team2).currentOpposition = teams.get(team1).teamId;
                    System.out.println(teams.get(team1).name + " against " + teams.get(teams.get(j).currentOpposition + 1).name);
                }
                catch (IndexOutOfBoundsException e){ //set opposition to -1 (vacant team) in case of uneven number of teams
                    teams.get(j).currentOpposition = -1;
                    System.out.println(teams.get(j).name + " against Vacant");
                }
            }
        }
    }
    public void main() {
        Script script = new Script();
        if (leagues.size() == 0){ //condition not useful for now, but will be useful when there is save data
            script.addNewLeague();
        }
        while (true){
            script.userChoice();
        }
    }
}

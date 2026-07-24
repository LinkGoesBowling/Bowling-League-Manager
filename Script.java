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
        public Bowler(String name, double pins, int highGame, int highSeries, double gameCount, String gender, int teamId, int leagueAffiliation) {
            this.name = name;
            this.pins = pins;
            this.highGame = highGame;
            this.highSeries = highSeries;
            this.gameCount = gameCount;
            this.gender = gender;
            this.teamId = teamId;
            this.leagueAffiliation = leagueAffiliation;
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
    public void userChoice(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Current league: " + leagues.get(currentLeague).name);
        System.out.println("What do you want to do?");
        System.out.println("Type B to add new bowlers");
        System.out.println("Type G to add this week's games");
        System.out.println("Type A to list top 3 bowlers");
        System.out.println("Type M to list top 3 male bowlers");
        System.out.println("Type F to list top 3 female bowlers");
        System.out.println("Type N to create a new league");
        System.out.println("Type S to switch leagues");
        System.out.println("Type W to switch to next week");
        String choice = reader.next();
        if (choice.toUpperCase().equals("B")){
            addNewBowlers();
        }
        if (choice.toUpperCase().equals("G")){
            addGames();
        }
        if (choice.toUpperCase().equals("A")){
            listBowlers("all");
        }
        if (choice.toUpperCase().equals("M")){
            listBowlers("M");
        }
        if (choice.toUpperCase().equals("F")){
            listBowlers("F");
        }
        if (choice.toUpperCase().equals("N")){
            addNewLeague();
        }
        if (choice.toUpperCase().equals("S")){
            switchLeagues();
        }
        if (choice.toUpperCase().equals("W")){
            leagues.get(currentLeague).currentWeek++;
            userChoice();
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
        addNewBowlers();
    }
    public void switchLeagues(){
        System.out.println("Which league do you want to switch to? Type league's number:");
        Scanner reader = new Scanner(System.in);
        for (int i = 0; i < leagues.size(); i++){
            System.out.println((i) + ": " + leagues.get(i).name);
        }
        currentLeague = reader.nextInt();
        userChoice();
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
                bowlers.add(new Bowler(nameInput, 0d, 0, 0, 0d, genderInput, teamInput, currentLeague));
                System.out.println("Bowler successfully added");
                System.out.println("Add another bowler? Y/N");
                String confirm2 = reader.next();
                if (confirm2.toUpperCase().equals("Y")) {
                    addNewBowlers();
                }
                if (confirm2.toUpperCase().equals("N")) {
                    addGames();
                }
            }
            else{
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
        int seriesTotal = 0;
        double currentGame = 0;
        //i = currentBowler
        for (int i = 0; i < bowlers.size(); i++){
            System.out.println(bowlers.get(i).name);
            //j = gamesEntered
            for (int j = 0; j < gamesPerWeek; j++){
                System.out.println("Enter game " + (j + 1) + " or type A for missed games:");
                try {
                    currentGame = reader.nextDouble();
                    gamesEntered++;
                }
                catch (InputMismatchException e){
                    gamesEntered++;
                    addGames();
                }
                if (gamesEntered == gamesPerWeek){
                    currentBowler++;
                    addGames();
                }
            }
                if (currentGame > bowlers.get(i).highGame){
                    bowlers.get(i).highGame = currentGame;
                }
                seriesTotal += currentGame;
                if (i + 1 == gamesPerWeek){
                    if (seriesTotal > bowlers.get(i).highSeries){
                        bowlers.get(i).highSeries = currentGame;
                    }
                }
                bowlers.get(i).pins += currentGame;
                bowlers.get(i).gameCount++;
            }
            userChoice();
        }
    public void listBowlers(String gender) {
        calculateAvgAndHdcp();
        for (int j = 0; j < bowlers.size(); j++) {
            if (bowlers.get(j).leagueAffiliation == currentLeague) {
                if (gender.equals("all") || (gender.equals("M") && bowlers.get(j).gender.equals("M")) || (gender.equals("F") && bowlers.get(j).gender.equals("F"))) {
                    bowlers.sort((a, b) -> Double.compare(b.avg, a.avg)); //sort bowlers highest average to lowest
                }
            }
        }
        int k = 0;
        for (int i = 0; i < bowlers.size() && k < 3; i++) { //list top 3 bowlers of selected gender
            calculateAvgAndHdcp();
            if (currentLeague == bowlers.get(i).leagueAffiliation) {
                if (gender.equals("all") || (gender.equals("F") && bowlers.get(i).gender.equals("F")) || (gender.equals("M") && bowlers.get(i).gender.equals("M"))) {
                    DecimalFormat removeTrailingZeros = new DecimalFormat("0.#");
                    System.out.println(bowlers.get(i).name + ", avg: " + removeTrailingZeros.format(bowlers.get(i).avg));
                    k++;
                }
            }
        }
        userChoice();
    }
    public void main() {
        Script script = new Script();
        if (leagues.size() == 0){
            script.addNewLeague();
        }
        else{
            script.userChoice();
        }
    }
}

import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.ArrayList;
public class Script {
    public float baseScore = 220f; //handicap base score and percent
    public float percent = 90f;
    public int gamesPerWeek = 0;
    public class Bowler {
        String name;
        double pins;
        int highGame;
        int highSeries;
        double gameCount;
        String gender;
        double avg;
        double hdcp;
        double roundedAvg;
        int teamId;

        public Bowler(String name, double pins, int highGame, int highSeries, double gameCount, String gender, int teamId) {
            this.name = name;
            this.pins = pins;
            this.highGame = highGame;
            this.highSeries = highSeries;
            this.gameCount = gameCount;
            this.gender = gender;
            this.teamId = teamId;
        }
    }

    ArrayList<Bowler> bowlers = new ArrayList<>();
    public void userChoice(){
        Scanner reader = new Scanner(System.in);
        System.out.println("What do you want to do?");
        System.out.println("Type B to add new bowlers");
        System.out.println("Type G to add this week's games");
        System.out.println("Type A to list top 3 bowlers");
        System.out.println("Type M to list top 3 male bowlers");
        System.out.println("Type F to list top 3 female bowlers");
        String choice = reader.next();
        if (choice.toUpperCase().equals("B")){
            addNewBowlers();
        }
        if (choice.toUpperCase().equals("G")){
            askGamesPerWeek();
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
    }
    public void askGamesPerWeek(){
        if (gamesPerWeek == 0) {
            Scanner reader = new Scanner(System.in);
            System.out.println("How many games do you want per week?");
            gamesPerWeek = reader.nextInt();
        }
        addGames();
    }
    public void calculateAvgAndHdcp() {
        for (int i = 0; i < bowlers.size(); i++) {
            bowlers.get(i).roundedAvg = Math.floor(bowlers.get(i).pins / bowlers.get(i).gameCount); //used to show to user. bowling averages always round down
            System.out.println("Pins: " + bowlers.get(i).pins + ", Games: " + bowlers.get(i).gameCount); //show pins and games for debugging
            bowlers.get(i).avg = bowlers.get(i).pins / bowlers.get(i).gameCount; //used to rank bowlers accurately
            bowlers.get(i).hdcp = Math.floor((baseScore - bowlers.get(i).roundedAvg) * (percent / 100d));
            if (bowlers.get(i).avg >= baseScore) {
                bowlers.get(i).hdcp = 0;
            }
        }
    }

    public void addNewBowlers() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter bowler's name: ");
        String nameInput = reader.next();
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
                bowlers.add(new Bowler(nameInput, 0d, 0, 0, 0d, genderInput, teamInput));
                System.out.println("Bowler successfully added");
                System.out.println("Add another bowler? Y/N");
                String confirm2 = reader.next();
                if (confirm2.toUpperCase().equals("Y")) {
                    addNewBowlers();
                }
                if (confirm2.toUpperCase().equals("N")) {
                    userChoice();
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
        int currentGame = 0;
        for (int i = 0; i < bowlers.size(); i++){
            System.out.println(bowlers.get(i).name);
            for (int j = 0; j < gamesPerWeek; j++){
                System.out.println("Enter game " + (j + 1) + ":");
                currentGame = reader.nextInt();
                if (currentGame > bowlers.get(i).highGame){
                    bowlers.get(i).highGame = currentGame;
                    continue;
                }
                seriesTotal += currentGame;
                if (i == gamesPerWeek){
                    if (seriesTotal > bowlers.get(i).highSeries){
                        bowlers.get(i).highSeries = currentGame;
                        continue;
                    }
                }
                bowlers.get(i).pins += currentGame;
                bowlers.get(i).gameCount++;
            }
        }
        userChoice();
    }
    public void listBowlers(String gender) {
        calculateAvgAndHdcp();
        for (int j = 0; j < bowlers.size(); j++) {
            if (gender == "all" || (gender == "M" && bowlers.get(j).gender == "M") || (gender == "F" && bowlers.get(j).gender == "F")) {
                bowlers.sort((a, b) -> Double.compare(b.avg, a.avg)); //sort bowlers highest average to lowest
            }
        }
        int k = 0;
        for (int i = 0; i < bowlers.size() && k < 3; i++) { //list top 3 bowlers of selected gender
            calculateAvgAndHdcp();
            if (gender == "all" || (gender == "M" && bowlers.get(i).gender == "M") || (gender == "F" && bowlers.get(i).gender == "F")) {
                DecimalFormat format = new DecimalFormat("0.#"); //remove trailing 0's
                System.out.println(bowlers.get(i).name + ", avg: " + format.format(bowlers.get(i).roundedAvg));
                k++;
            }
        }
        userChoice();
    }

    public static void main(String[] args) {
        Script script = new Script();
        script.userChoice();
    }
}

import java.util.Arrays;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.ArrayList;
public class Script {
    public float baseScore = 220f; //handicap base score and percent
    public float percent = 90f;
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
    public void calculateAvgAndHdcp(){
        for (int i = 0; i < bowlers.size(); i++){
            bowlers.get(i).roundedAvg = Math.floor(bowlers.get(i).pins / bowlers.get(i).gameCount); //used to show to user. bowling averages always round down
            bowlers.get(i).avg = bowlers.get(i).pins / bowlers.get(i).gameCount; //used to rank bowlers accurately
            bowlers.get(i).hdcp = Math.floor((baseScore - bowlers.get(i).roundedAvg) * (percent / 100d));
            if (bowlers.get(i).avg >= baseScore){
                bowlers.get(i).hdcp = 0;
            }
        }
    }
    public void listBowlers(String gender) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter bowler's name: ");
        String nameInput = reader.next();
        System.out.println("Enter bowler's gender(M/F): ");
        String genderInput = reader.next();
        if ((genderInput.equals("M")) || (genderInput.equals("F"))){
            System.out.println("Enter bowler's team: ");
            int teamInput = reader.nextInt();
            System.out.println("Are these details correct?");
            System.out.println("Name: " + nameInput);
            System.out.println("Gender: " + genderInput);
            System.out.println("Team: " + teamInput);
            String confirm = reader.next();
            if (confirm.equals("Y")){
                Bowler newBowler = new Bowler(nameInput, 0d, 0, 0, 0d, genderInput, teamInput);
                System.out.println("Enter bowler's name: ");
                nameInput = reader.next();
                System.out.println("Enter bowler's gender(M/F): ");
                genderInput = reader.next();
                if ((genderInput.equals("M")) || (genderInput.equals("F"))){
                    System.out.println("Enter bowler's team: ");
                    teamInput = reader.nextInt();
                    System.out.println("Are these details correct?");
                    System.out.println("Name: " + nameInput);
                    System.out.println("Gender: " + genderInput);
                    System.out.println("Team: " + teamInput);
                    confirm = reader.next();
                    if (confirm.equals("Y")){
                        newBowler = new Bowler(nameInput, 0d, 0, 0, 0d, genderInput, teamInput);
                    }
                    else{
                        nameInput = "";
                        genderInput = "";
                        teamInput = 0;
                    }
                }
                else{
                    System.out.println("Not a valid gender!");
                }
            }
            else{
                nameInput = "";
                genderInput = "";
                teamInput = 0;
                System.out.println("Enter bowler's name: ");
                nameInput = reader.next();
                System.out.println("Enter bowler's gender(M/F): ");
                genderInput = reader.next();
                if ((genderInput.equals("M")) || (genderInput.equals("F"))){
                    System.out.println("Enter bowler's team: ");
                   teamInput = reader.nextInt();
                    System.out.println("Are these details correct?");
                    System.out.println("Name: " + nameInput);
                    System.out.println("Gender: " + genderInput);
                    System.out.println("Team: " + teamInput);
                    confirm = reader.next();
                    if (confirm.equals("Y")){
                        Bowler newBowler = new Bowler(nameInput, 0d, 0, 0, 0d, genderInput, teamInput);
                    }
                    else{
                        nameInput = "";
                        genderInput = "";
                        teamInput = 0;
                    }
                }
                else{
                    System.out.println("Not a valid gender!");
                }
            }
        }
        else{
            System.out.println("Not a valid gender!");
        }
        calculateAvgAndHdcp();
        for (int j = 0; j < bowlers.size(); j++) {
            if (gender == "all" || (gender == "M" && bowlers.get(j).gender == "M") || (gender == "F" && bowlers.get(j).gender == "F")) {
                bowlers.sort((a, b) -> Double.compare(b.avg, a.avg)); //sort bowlers highest average to lowest
            }
        }
        int k = 0;
        for (int i = 0; i < bowlers.size() && k < 3; i++) { //list top 3 bowlers of selected gender
            if (gender == "all" || (gender == "M" && bowlers.get(i).gender == "M") || (gender == "F" && bowlers.get(i).gender == "F")) {
                DecimalFormat format = new DecimalFormat("0.#"); //remove trailing 0's
                System.out.println(bowlers.get(i).name + " " + format.format(bowlers.get(i).roundedAvg));
                k++;
            }
        }
    }
    public static void main(String[] args) {
        Script script = new Script();
        script.listBowlers("all");
    }
}

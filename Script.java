import java.util.Arrays;
import java.text.DecimalFormat;
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
Bowler[] bowlers = {
        new Bowler("Link", 7312, 300, 735, 36, "M", 1),
        new Bowler("Nate", 6426, 280, 760, 30, "M", 1),
        new Bowler("Gianna", 6230, 279, 710, 33, "F", 2),
        new Bowler("Diego", 8280, 300, 813, 36, "M", 3),
        new Bowler ("Naomi", 7020, 289, 725, 36, "F", 2),
        new Bowler ("Emily", 8136, 300, 780, 36, "F", 3),
        new Bowler ("Zach", 6948, 268, 710, 36, "M", 4),
        new Bowler ("Julie", 4218, 220, 650, 30, "F", 4),
        new Bowler ("Miles", 7311, 299, 734, 36, "M", 5),
        new Bowler ("Braden", 7314, 300, 736, 36, "M", 5)
};
    public void calculateAvgAndHdcp(){
        for (int i = 0; i < bowlers.length; i++){
            bowlers[i].roundedAvg = Math.floor(bowlers[i].pins / bowlers[i].gameCount); //used to show to user. bowling averages always round down
            bowlers[i].avg = bowlers[i].pins / bowlers[i].gameCount; //used to rank bowlers accurately
            bowlers[i].hdcp = Math.floor((baseScore - bowlers[i].roundedAvg) * (percent / 100d));
            if (bowlers[i].avg >= baseScore){
                bowlers[i].hdcp = 0;
            }
        }
    }
    public void listBowlers(String gender) {
        calculateAvgAndHdcp();
        for (int j = 0; j < bowlers.length; j++) {
            if (gender == "all" || (gender == "M" && bowlers[j].gender == "M") || (gender == "F" && bowlers[j].gender == "F")) {
                Arrays.sort(bowlers, (a, b) -> Double.compare(b.avg, a.avg)); //sort bowlers highest average to lowest
            }
        }
        int k = 0;
        for (int i = 0; i < bowlers.length && k < 3; i++) { //list top 3 bowlers of selected gender
            if (gender == "all" || (gender == "M" && bowlers[i].gender == "M") || (gender == "F" && bowlers[i].gender == "F")) {
                DecimalFormat format = new DecimalFormat("0.#"); //remove trailing 0's
                System.out.println(bowlers[i].name + " " + format.format(bowlers[i].roundedAvg));
                k++;
            }
        }
    }
    public static void main(String[] args) {
        Script script = new Script();
        script.listBowlers("all");
    }
}

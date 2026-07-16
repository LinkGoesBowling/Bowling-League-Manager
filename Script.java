import java.util.Arrays;
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
        public Bowler(String name, double pins, int highGame, int highSeries, double gameCount, String gender) {
            this.name = name;
            this.pins = pins;
            this.highGame = highGame;
            this.highSeries = highSeries;
            this.gameCount = gameCount;
            this.gender = gender;
        }
}
Bowler[] bowlers = {
        new Bowler("Link", 7312, 300, 735, 36, "M"),
        new Bowler("Nate", 6426, 280, 760, 30, "M"),
        new Bowler("Gianna", 6230, 279, 710, 33, "F"),
        new Bowler("Diego", 8280, 300, 813, 36, "M"),
        new Bowler ("Naomi", 7020, 289, 725, 36, "F"),
        new Bowler ("Emily", 8136, 300, 780, 36, "F")
};
    public void calculateAvgAndHdcp(){
        for (int i = 0; i < bowlers.length; i++){
            bowlers[i].roundedAvg = Math.floor(bowlers[i].pins / bowlers[i].gameCount);
            bowlers[i].avg = bowlers[i].pins / bowlers[i].gameCount;
            bowlers[i].hdcp = Math.floor((baseScore - bowlers[i].roundedAvg) * (percent / 100d));
            if (bowlers[i].avg >= baseScore){
                bowlers[i].hdcp = 0;
            }
        }
    }
    public void listBowlers(String gender){
        calculateAvgAndHdcp();
        for (int j = 0; j < bowlers.length; j++) {
            if ((gender == "all") || (gender == "M" && bowlers[j].gender == "M") || (gender == "F" && bowlers[j].gender == "F")) {
                Arrays.sort(bowlers, (a, b) -> Double.compare(b.avg, a.avg));
            }
        }
        for (int i = 0; i < bowlers.length; i++){ //there is 2 loops because the 2nd one was to only list top 3, but it wasn't working
            if ((gender == "all") || (gender == "M" && bowlers[i].gender == "M") || (gender == "F" && bowlers[i].gender == "F")) {
                System.out.println(bowlers[i].name + " " + bowlers[i].roundedAvg + " " + bowlers[i].hdcp);
            }
        }
    }
    public static void main(String[] args) {
        Script script = new Script();
        script.listBowlers("all");
    }
}

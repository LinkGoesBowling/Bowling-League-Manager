import java.util.Arrays;
public class Script {
    public class Bowler {
        String name;
        int pins;
        int highGame;
        int highSeries;
        int gameCount;
        String gender;
        int avg;
        public Bowler(String name, int pins, int highGame, int highSeries, int gameCount, String gender) {
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
    public void getAvg(){
        for (int i = 0; i < bowlers.length; i++) {
            bowlers[i].avg = bowlers[i].pins / bowlers[i].gameCount;
        }
    }
    public void listBowlers(String gender){
        getAvg();
        for (int j = 0; j < bowlers.length; j++) {
            if ((gender == "all") || (gender == "M" && bowlers[j].gender == "M") || (gender == "F" && bowlers[j].gender == "F")) {
                Arrays.sort(bowlers, (a, b) -> Integer.compare(b.avg, a.avg));
            }
        }
        for (int i = 0; i < bowlers.length; i++){
            if ((gender == "all") || (gender == "M" && bowlers[i].gender == "M") || (gender == "F" && bowlers[i].gender == "F")) {
                System.out.println(bowlers[i].name + " " + bowlers[i].avg);
            }
        }
    }
    public static void main(String[] args) {
        Script script = new Script();
        script.listBowlers("all"); //change this for gender filtering
    }
}

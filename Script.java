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
        new Bowler("Diego", 8280, 300, 813, 36, "M")
};
    public void getAvg(){
        for (int i = 0; i < bowlers.length; i++) {
            bowlers[i].avg = bowlers[i].pins / bowlers[i].gameCount;
        }
    }
    public void listBowlers(){
        getAvg();
        Arrays.sort(bowlers, (a, b) -> Integer.compare(b.avg, a.avg));
        for (int i = 0; i < bowlers.length; i++){
            int avg = bowlers[i].pins / bowlers[i].gameCount;
            System.out.println(bowlers[i].name + " " + avg);
        }
        return;
    }
    public static void main(String[] args) {
        Script addGames = new Script();
        addGames.listBowlers();
    }
}

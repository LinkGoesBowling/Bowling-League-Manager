import java.util.ArrayList;
import java.util.Scanner;
public class Leagues {
    private Main main;
    public ArrayList<Leagues.League> leagues = new ArrayList<>();
    public Leagues(Main main){
        this.main = main;
    }
    public int currentLeague = 0;
    public class League{
        String name;
        int currentWeek;
        int baseScore;
        float percent;
        int gamesPerWeek;
        public League(String name, int gamesPerWeek, int baseScore, float percent){
            this.name = name;
            this.gamesPerWeek = gamesPerWeek;
            this.baseScore = baseScore;
            this.percent = percent;
        }
    }
    public void init(){
        leagues = main.leaguesScript.leagues;
    }
    public void addNewLeague(){
        Scanner reader = new Scanner(System.in);
        System.out.println("What would you like to name your league?");
        String leagueName = reader.nextLine();
        System.out.println("How many games do you want per week?");
        int gamesPerWeek = reader.nextInt();
        System.out.println("What do you want for your handicap percentage? (ex. 90) (Use 100 for scratch league and do not include %)");
        float percent = reader.nextInt();
        System.out.println("What do you want for your base score? (ex. 220) (Use 0 for scratch leagues)");
        int baseScore = reader.nextInt();
        leagues.add(new League (leagueName, gamesPerWeek, baseScore, percent));
        System.out.println("League successfully added");
        for (int i = 0; i < leagues.size(); i++){ //switch to newly created league
            currentLeague = i;
        }
        main.bowlersScript.addNewBowlers();
    }
    public void switchLeagues(){
        System.out.println("Which league do you want to switch to? Type league's number:");
        Scanner reader = new Scanner(System.in);
        for (int i = 0; i < leagues.size(); i++){
            System.out.println((i) + ": " + leagues.get(i).name);
        }
        currentLeague = reader.nextInt();
    }
}

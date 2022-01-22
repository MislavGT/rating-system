
package hr.pmf.project.rating;

import java.util.ArrayList;
import static java.util.Collections.list;
import org.javatuples.Pair;
import java.io.File;

public class Event {
    
    static{
        File lib = new File("src/jni/" + 
                System.mapLibraryName("JNI"));
        System.load(lib.getAbsolutePath());
    }
    
    public static final double BETA = 100;
    public static final double GAMMA = 100;
    public static final double RHO = 0.5;
    
    private final ArrayList<Pair<Player, Integer>> players;
    private ArrayList<Double> deltas;
    private ArrayList<Double> priorMeans;
    private ArrayList<Integer> placements;
    
    public Event(ArrayList<Pair<Player, Integer>> players){
        this.players = players;
        this.deltas = new ArrayList<Double>();
        this.priorMeans = new ArrayList<Double>();
        this.placements = new ArrayList<Integer>();
    }
    
    public void addPlayer(Player player, int rank){
        players.add(Pair.with(player, rank));
    }
    
    public void addPlayer(Player player){
        addPlayer(player, 1);
    }
    
    public ArrayList<Pair<Player, Integer>> getPlayers(){
        return players;
    }
    
    public void calculate(){
        for(Pair<Player, Integer> element1 : players){
            Player player1 = element1.getValue0();
            diffuse(player1);
            deltas.add(Math.sqrt(Math.pow(player1.getDeviation(), 2) 
                    + Math.pow(BETA, 2)));
            priorMeans.add(player1.getMean());
            placements.add(element1.getValue1());
        }
        for(Pair<Player, Integer> element2 : players){
            update(element2);
        }
        placements.clear();
        priorMeans.clear();
        deltas.clear();
    }
    
    public void diffuse(Player player){
        double k = 1 / (1 + Math.pow(GAMMA, 2) / 
               Math.pow(player.getDeviation(), 2));
        double w_G = Math.pow(k, RHO) * player.getD().get(0);
        double w_L = 0;
        for(int i = 0; i < player.getD().size(); i++){
            w_L += player.getD().get(i);
        }
        w_L *= (1 - Math.pow(k, RHO));
        player.getM().set(0, (w_G * player.getM().get(0) 
                + w_L * player.getMean()) / (w_G + w_L));
        player.getD().set(0, k * (w_G + w_L));
        for(int i = 1; i < player.getD().size(); i++){player.getD().
                set(i, Math.pow(k, 1 + RHO)*player.getD().get(i));
        }
        player.setDeviation(player.getDeviation()/Math.sqrt(k));
    }
    
    public void update(Pair<Player, Integer> player){
        double p = firstSolveZero(player.getValue1(), deltas, priorMeans, 
                placements, player.getValue0().getMean());
        player.getValue0().addM(p);
        player.getValue0().addD(1 / Math.sqrt(BETA));
        player.getValue0().setMean(secondSolveZero(player.getValue0().getM(),
                player.getValue0().getD(), player.getValue0().getMean(), BETA));
    }
    
    public native double firstSolveZero(int placement, 
            ArrayList<Double> _deltas, ArrayList<Double> _priorMeans, 
            ArrayList<Integer> _placements, double start);
    
    public native double secondSolveZero(ArrayList<Double> _m_list, 
            ArrayList<Double> _d_list, double start, double parameter);
            
    public static void main(String[] args){
        ArrayList<Double> m_list1 = new ArrayList<Double>();
        ArrayList<Double> d_list1 = new ArrayList<Double>();
        ArrayList<Double> m_list2 = new ArrayList<Double>();
        ArrayList<Double> d_list2 = new ArrayList<Double>();
        ArrayList<Double> m_list3 = new ArrayList<Double>();
        ArrayList<Double> d_list3 = new ArrayList<Double>();
        m_list1.add(1500d); m_list2.add(1500d); m_list3.add(1500d);
        d_list1.add(Math.pow(1/350d, 2)); d_list2.add(Math.pow(1/350d, 2));
        d_list3.add(Math.pow(1/350d, 2));
        Player Marko = new Player("001", 1500, 350, "Marko", m_list1, d_list1);
        Player Ivan = new Player("002", 1500, 350, "Ivan", m_list2, d_list2);
        Player Luka = new Player("003", 1500, 350, "Luka", m_list3, d_list3);
        Pair<Player, Integer> A = Pair.with(Marko, 1);
        Pair<Player, Integer> B = Pair.with(Ivan, 2);
        Pair<Player, Integer> C = Pair.with(Luka, 3);
        ArrayList<Pair<Player, Integer>> players = new ArrayList<Pair<Player, Integer>>();
        players.add(A); players.add(B); players.add(C);
        Event X = new Event(players);
        X.calculate();
        System.out.println(Marko.getMean());
        System.out.println(Ivan.getMean());
        System.out.println(Luka.getMean());
        System.out.println(Marko.getDeviation());
        System.out.println(Ivan.getDeviation());
        System.out.println(Luka.getDeviation());
        X.calculate();
        System.out.println(Marko.getMean());
        System.out.println(Ivan.getMean());
        System.out.println(Luka.getMean());
        System.out.println(Marko.getDeviation());
        System.out.println(Ivan.getDeviation());
        System.out.println(Luka.getDeviation());
    }
                
}


package hr.pmf.project.rating;

import java.util.ArrayList;
import static java.util.Collections.list;
import org.javatuples.Pair;
import java.io.File;
import java.util.Random;

public class Event {
    
    static{
        File lib = new File("src/jni/" + 
                System.mapLibraryName("JNI"));
        System.load(lib.getAbsolutePath());
    }
    
    public static final double BETA = 200;
    public static final double GAMMA = 80;
    public static final double RHO = 1;
    
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
            deltas.add(Math.sqrt(
        Math.pow(player1.getSigma(), 2) + (BETA * BETA)));
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
        double k = Math.pow(1 + (Math.pow(GAMMA, 2) / 
               Math.pow(player.getSigma(), 2)), -1);
        double w_G = Math.pow(k, RHO) * player.getD().get(0);
        double w_L = 0;
        for(int i = 0; i < player.getD().size(); i++){
            w_L += player.getD().get(i);
        }
        w_L *= (1 - Math.pow(k, RHO));
        player.getM().set(0, ((w_G * player.getM().get(0))
                + (w_L * player.getMean())) / (w_G + w_L));
        player.getD().set(0, k * (w_G + w_L));
        for(int i = 1; i < player.getD().size(); i++){player.getD().
                set(i, Math.pow(k, 1 + RHO)*player.getD().get(i));
        }
        player.setSigma(player.getSigma() / Math.sqrt(k));
        
    }
    
    public void update(Pair<Player, Integer> player){
        double p = firstSolveZero(player.getValue1(), deltas, priorMeans, 
                placements, player.getValue0().getMean());
        player.getValue0().addM(p);
        player.getValue0().addD(Math.pow(BETA, -2));
        player.getValue0().setMean(secondSolveZero(player.getValue0().getM(),
                player.getValue0().getD(), player.getValue0().getMean(), BETA));
        player.getValue0().setSigma(
                Math.pow(Math.pow(BETA, -2) + 
                        Math.pow(player.getValue0().getSigma(), -2), -0.5));
    }
    
    public native double firstSolveZero(int placement, 
            ArrayList<Double> _deltas, ArrayList<Double> _priorMeans, 
            ArrayList<Integer> _placements, double start);
    
    public native double secondSolveZero(ArrayList<Double> _m_list, 
            ArrayList<Double> _d_list, double start, double parameter);
    
    public static double cdf(double x){
        return 0.5 + 0.5 * Math.tanh(0.5 * Math.PI * x / Math.sqrt(3));
    }
            
    public static void main(String[] args){
        ArrayList<Double> m_list1 = new ArrayList<Double>();
        ArrayList<Double> d_list1 = new ArrayList<Double>();
        ArrayList<Double> m_list2 = new ArrayList<Double>();
        ArrayList<Double> d_list2 = new ArrayList<Double>();
        ArrayList<Double> m_list3 = new ArrayList<Double>();
        ArrayList<Double> d_list3 = new ArrayList<Double>();
        ArrayList<Double> m_list4 = new ArrayList<Double>();
        ArrayList<Double> d_list4 = new ArrayList<Double>();
        m_list1.add(1500d); m_list2.add(1500d); m_list3.add(1500d);
        m_list4.add(1500d);
        d_list1.add(Math.pow(1/350d, 2)); d_list2.add(Math.pow(1/350d, 2));
        d_list3.add(Math.pow(1/350d, 2)); d_list4.add(Math.pow(1/350d, 2));
        Player Marko = new Player("001", 1500, 350, "Marko", m_list1, d_list1);
        Player Ivan = new Player("002", 1500, 350, "Ivan", m_list2, d_list2);
        Player Luka = new Player("003", 1500, 350, "Luka", m_list3, d_list3);
        Player Petar = new Player("004", 1500, 350, "Petar", m_list4, d_list4);
        Pair<Player, Integer> A = Pair.with(Marko, 1);
        Pair<Player, Integer> B = Pair.with(Ivan, 2);
        Pair<Player, Integer> C = Pair.with(Luka, 3);
        Pair<Player, Integer> D = Pair.with(Petar, 4);
        ArrayList<Pair<Player, Integer>> players = new ArrayList<>();
        players.add(A); players.add(B); players.add(C); players.add(D);
        Event X = new Event(players);   
        Pair<Player, Integer> G = Pair.with(Marko, 2);
        Pair<Player, Integer> E = Pair.with(Ivan, 1);
        Pair<Player, Integer> F = Pair.with(Luka, 1);
        ArrayList<Pair<Player, Integer>> players2 = new ArrayList<>();
        players2.add(G); players2.add(E); players2.add(F);
        Event Y = new Event(players2);
        for(int i = 0; i < 100; i++){
            X.calculate();
        }
        System.out.println(Marko.getMean());
        System.out.println(Marko.getSigma());
        System.out.println(Ivan.getMean());
        System.out.println(Ivan.getSigma());
        System.out.println(Luka.getMean());
        System.out.println(Luka.getSigma());
        System.out.println(Petar.getMean());
        System.out.println(Petar.getSigma());
        double prob1 = cdf((Marko.getMean() - Ivan.getMean()) / 
                (Math.sqrt(2 * (BETA * BETA + 
                        Marko.getSigma() * Marko.getSigma()))));
        double prob2 = cdf((Marko.getMean() - Luka.getMean()) / 
                (Math.sqrt(2 * (BETA * BETA + 
                        Marko.getSigma() * Marko.getSigma()))));
        double prob3 = cdf((Marko.getMean() - Petar.getMean()) / 
                (Math.sqrt(2 * (BETA * BETA + 
                        Marko.getSigma() * Marko.getSigma()))));
        System.out.println(prob1);
        System.out.println(prob2);
        System.out.println(prob3);
        
    }
                
}

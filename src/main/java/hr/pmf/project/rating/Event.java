
package hr.pmf.project.rating;

import static hr.pmf.project.rating.PredictEvent.THREAD_POOL_SIZE;
import java.util.ArrayList;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

public class Event {
    
    static{
        File lib = new File("src/jni/" + 
                System.mapLibraryName("JNI"));
        System.load(lib.getAbsolutePath());
    }
    
    public static final double BETA = 200;
    public static final double GAMMA = 80;
    public static final double RHO = 1;
    public static final double SH = Math.sqrt(3) / Math.PI;
    public static final double MAX_LENGTH = 1000;
    
    private final ArrayList<Pair<Player, Integer>> players;
    private final ArrayList<Double> deltas;
    private final ArrayList<Double> priorMeans;
    private final ArrayList<Integer> placements;
    
    public Event(ArrayList<Pair<Player, Integer>> players){
        this.players = players;
        this.deltas = new ArrayList<>();
        this.priorMeans = new ArrayList<>();
        this.placements = new ArrayList<>();
    }
    
    public ArrayList<Double> getDeltas(){
        return deltas;
    }
    
    public ArrayList<Double> getPriorMeans(){
        return priorMeans;
    }
        
    public ArrayList<Integer> getPlaces(){
        return placements;
    }
    
    public void updateSql() throws ClassNotFoundException{
        JavaSqlite baza = new JavaSqlite();
        for(Pair<Player, Integer> p : players){
            try{
                if(Double.isNaN(p.getValue0().getMean())){
                    System.out.println("igrac je " + p.getValue0().getName() + " sigma " + p.getValue0().getSigma() + " mean " + 
                            p.getValue0().getMean());
                    System.out.println("adasdasfdasdasdasdasdasd");
                }
                baza.UpdatePlayer(p.getValue0());
            }catch(ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    public static double hyp(double x, double y){
        return Math.sqrt(x * x + y * y);
    }
        
    public int getSize(){
        return this.players.size();
    }
    
    public ArrayList<Integer> getPlacements(){
        ArrayList<Integer> placementList = new ArrayList<>();
        for(int i = 0; i <this.getSize(); i++){
            placementList.add(players.get(i).getValue1());
        }
        return placementList;
    }
    
    public ArrayList<Player> getPlayerList(){
        ArrayList<Player> playerList = new ArrayList<>();
        for(int i = 0; i < getSize(); i++){
            playerList.add(players.get(i).getValue0());
        }
        return playerList;
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
        CopyOnWriteArrayList<Triplet<Double, Double, Integer>> priors = 
        new CopyOnWriteArrayList<>();
        ArrayList<Future<Void>> tasks = new ArrayList<>();
        ExecutorService executorService = 
            Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        players.forEach((Pair<Player, Integer> element) -> {
            Future<Void> diffuse = executorService.submit(
                    new CalculationDiffuse(element, priors));
            tasks.add(diffuse);
        });
        tasks.forEach((Future<Void> ft) -> {
            try{
                ft.get();
            }catch(ExecutionException | InterruptedException e){
                
            }
        });
        
        executorService.shutdown();
        
        for(int i = 0; i < priors.size(); i++){
            placements.add(priors.get(i).getValue2());
            deltas.add(priors.get(i).getValue0());
            priorMeans.add(priors.get(i).getValue1());
        }
        
        players.forEach(element2 -> {
            update(element2);
        });
        
        placements.clear();
        priorMeans.clear();
        deltas.clear();
    }
    
    public void update(Pair<Player, Integer> player){
        double p = firstSolveZero(player.getValue1(), deltas, priorMeans, 
                placements, player.getValue0().getMean());
        player.getValue0().addM(p);
        player.getValue0().addD(Math.pow(player.getValue0().getSigma(), -2));
        player.getValue0().setMean(secondSolveZero(player.getValue0().getM(),
                player.getValue0().getD(), player.getValue0().getMean(), BETA));
        player.getValue0().setSigma(
                Math.pow(Math.pow(player.getValue0().getSigma(), -2) + 
                        Math.pow(player.getValue0().getSigma(), -2), -0.5));
        if(player.getValue0().getD().size() > MAX_LENGTH){
            player.getValue0().getD().remove(0);
            player.getValue0().getM().remove(0);
        }
    }
    
    public native double firstSolveZero(int placement, 
            ArrayList<Double> _deltas, ArrayList<Double> _priorMeans, 
            ArrayList<Integer> _placements, double start);
    
    public native double secondSolveZero(ArrayList<Double> _m_list, 
            ArrayList<Double> _d_list, double start, double parameter);
    
    public static double cdf(double x){
        return 0.5 + 0.5 * Math.tanh(0.5 * x / SH);
    }
    
    public static double cdfParameter(Player x, Player y){
        return (x.getMean() - y.getMean()) / 
                (Math.sqrt(x.getSigma() * x.getSigma() +
                y.getSigma() * y.getSigma() + 2 * BETA * BETA));
    }
            
    public static void main(String[] args){
        ArrayList<Double> m_list1 = new ArrayList<>();
        ArrayList<Double> d_list1 = new ArrayList<>();
        ArrayList<Double> m_list2 = new ArrayList<>();
        ArrayList<Double> d_list2 = new ArrayList<>();
        ArrayList<Double> m_list3 = new ArrayList<>();
        ArrayList<Double> d_list3 = new ArrayList<>();
        ArrayList<Double> m_list4 = new ArrayList<>();
        ArrayList<Double> d_list4 = new ArrayList<>();
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
        Pair<Player, Integer> G = Pair.with(Marko, 3);
        Pair<Player, Integer> E = Pair.with(Ivan, 2);
        Pair<Player, Integer> F = Pair.with(Luka, 1);
        ArrayList<Pair<Player, Integer>> players2 = new ArrayList<>();
        players2.add(G); players2.add(E); players2.add(F);
        Event Y = new Event(players2);
        double prob4;
        for(int i = 0; i < 3; i++){
                Y.calculate();
                System.out.println(Marko.getMean());
                System.out.println(Ivan.getMean());
                System.out.println(Luka.getMean());
        }
        for(int i = 0; i < 3; i++){
            X.calculate();
            System.out.println(Marko.getMean());
            System.out.println(Ivan.getMean());
            System.out.println(Luka.getMean());
            System.out.println(Petar.getMean());
        }
    }
                
}

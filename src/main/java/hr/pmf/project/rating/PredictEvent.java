/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.pmf.project.rating;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicIntegerArray;
import org.apache.commons.math3.distribution.LogisticDistribution;
import org.javatuples.Pair;
import java.math.RoundingMode;
import java.math.BigDecimal;

/**
 *
 * @author jurica
 */
public class PredictEvent {
    final static int ITER_CNT = 10_000;
    final static int THREAD_POOL_SIZE = 7;
    final int nPlayers;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<AtomicIntegerArray> rankings = null;
    HashMap<String, ArrayList<Double>> rankovi = null;
    
    public PredictEvent(ArrayList<Player> players){
        this.players = players;
        nPlayers = players.size();
    }
    
    private void setRankovi(ArrayList<ArrayList<Double>> freqs){
        rankovi = new HashMap<>();
        for(int i = 0; i < nPlayers; i++){
            rankovi.put(players.get(i).getId(), freqs.get(i));
        }
    }
    
    public HashMap<String, ArrayList<Double>> getRankovi(){
        return rankovi;
    }
    
    public void process(){
        ArrayList<ArrayList<Double>> scores = new ArrayList<>();
        for(int i = 0; i < nPlayers; i++){
            //za svakog igraca uzmem 10_000 sampleova iz logisticke distribucije
            double curMean = players.get(i).getMean();
            double curSigma = Event.hyp(players.get(i).getSigma(), Event.BETA) *
                    Event.SH;
           
            LogisticDistribution ld = new LogisticDistribution(curMean, curSigma);
            double[] curSample = ld.sample(ITER_CNT);
            ArrayList<Double> moja = new ArrayList<>();
            for(int j = 0; j < ITER_CNT; j++){
                moja.add(curSample[j]);
            }
            scores.add(moja);
        }
        
        rankings = new ArrayList<>(); //ovo je za visedretvenost
      
        for(int i = 0; i < nPlayers; i++){
            int pomoc[] = new int[nPlayers];
            for(int j = 0; j < nPlayers; j++) pomoc[j] = 0;
            AtomicIntegerArray ubaci = new AtomicIntegerArray(pomoc);
            rankings.add(ubaci);
        }
        
        List<Future<Void>> zadaci = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for(int game = 0; game < ITER_CNT; game++){
            ArrayList<Pair<Double, Integer>> al = new ArrayList<>();
            for(int j = 0; j < nPlayers; j++)
                al.add(new Pair(scores.get(j).get(game), j));
            //ovdje paralelno
            Future<Void> racunaj = executorService.submit(new CalculationJobPredict(al, rankings));
            zadaci.add(racunaj);
        }
        
        zadaci.forEach((Future<Void> ft) -> {
            try{
                ft.get();
            }catch(ExecutionException | InterruptedException e){
                
            }
        });
        
        executorService.shutdown();
     
    }
    public ArrayList<ArrayList<Double>> matr(){
        ArrayList<ArrayList<Double>> freq = new ArrayList<>();
        for(int i = 0; i < nPlayers; i++){
            ArrayList<Double> cur = new ArrayList<>();
            for(int j = 0; j < nPlayers; j++){
                cur.add(round(100*(double)rankings.get(i).get(j) / 
                        (double)ITER_CNT, 2));
            }
            freq.add(cur);
        }
        return freq;
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static void main(String args[]){
        ArrayList<Player> testna = new ArrayList<>();
        ArrayList<Double> emp = new ArrayList<>();
        testna.add(new Player("001", 3000d, 35d, "Marko", emp, emp));
        testna.add(new Player("002", 5000d, 35d, "Petar", emp, emp));
        testna.add(new Player("003", 6000d, 35d, "Ivan", emp, emp));
        testna.add(new Player("004", 6000d, 35d, "Luka", emp, emp));
        testna.add(new Player("005", 7000d, 35d, "Pero", emp, emp));
        testna.add(new Player("006", 7000d, 35d, "Stef", emp, emp));
        testna.add(new Player("007", 9000d, 35d, "Andrija", emp, emp));
        testna.add(new Player("008", 9000d, 35d, "Boris", emp, emp));
        testna.add(new Player("009", 9000d, 35d, "Milan", emp, emp));
        PredictEvent novi = new PredictEvent(testna);
        novi.process();
        HashMap<String, ArrayList<Double>> predvidi = novi.getRankovi();
        for(int i = 0; i < testna.size(); i++){
            System.out.println("igrac: " + testna.get(i).getId());
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            for(int j = 0; j < testna.size(); j++){
                System.out.print(df.format((Double)(predvidi.get(testna.get(i).getId()).get(j) * 100.0)) + "% ");
            }
            System.out.println();
        }
    }
}

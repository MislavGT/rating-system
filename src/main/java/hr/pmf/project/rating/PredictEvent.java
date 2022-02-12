/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.pmf.project.rating;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import org.apache.commons.math3.distribution.LogisticDistribution;
import org.javatuples.Pair;

/**
 *
 * @author jurica
 */
public class PredictEvent {
    final static int ITER_CNT = 10_000;
    final int nPlayers;
    private ArrayList<Player> players = new ArrayList<>();
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
    
    public void addPlayer(Player p){
        players.add(p);
    }
    
    public void process(){
        ArrayList<ArrayList<Double>> scores = new ArrayList<>();
        for(int i = 0; i < nPlayers; i++){
            //za svakog igraca uzmem 10_000 sampleova iz logisticke distribucije
            double curMean = players.get(i).getMean();
            double curSigma = players.get(i).getSigma();
           
            LogisticDistribution ld = new LogisticDistribution(curMean, curSigma);
            double[] curSample = ld.sample(ITER_CNT);
            ArrayList<Double> moja = new ArrayList<>();
            for(int j = 0; j < ITER_CNT; j++){
                moja.add(curSample[j]);
            }
            scores.add(moja);
        }
        
        ArrayList<ArrayList<Integer>> rankings = new ArrayList<>(); //matrica; (i, j) -> #i-ti igrac na mjestu j
        for(int i = 0; i < nPlayers; i++){
            ArrayList<Integer> tmp = new ArrayList<>();
            for(int j = 0; j < nPlayers; j++) tmp.add(0);
            rankings.add(tmp);
        }
        
        for(int game = 0; game < ITER_CNT; game++){
            ArrayList<Pair<Double, Integer>> al = new ArrayList<>();
            for(int j = 0; j < nPlayers; j++)
                al.add(new Pair(scores.get(j).get(game), j));
            al.sort(new Comparator<Pair<Double, Integer>>(){
                @Override
                public int compare(Pair<Double, Integer> p1, Pair<Double, Integer> p2) {
                    if((double)p1.getValue0() > (double)p2.getValue0())
                        return -1;
                    if((double)p1.getValue0() == (double)p2.getValue0())
                        return 0;
                    return 1;
                }
            });
            
            for(int j = 0; j < nPlayers; j++){
                int player = al.get(j).getValue1();
                int c = rankings.get(player).get(j);
                rankings.get(player).set(j, c + 1);
            }
        }
        /*
        //provjera: za svakog igraca ispisem kolko puta je bio na kojoj poziciji
        for(int i = 0; i < nPlayers; i++){
            System.out.println("igrac = " + players.get(i).getName());
            for(int j = 0; j < nPlayers; j++)
              System.out.println("pos = " + (j + 1) + ": #" + rankings.get(i).get(j));
        }
        */
        
        //u postocima
        ArrayList<ArrayList<Double>> freq = new ArrayList<>();
        for(int i = 0; i < nPlayers; i++){
            ArrayList<Double> cur = new ArrayList<>();
            for(int j = 0; j < nPlayers; j++){
                cur.add((double)rankings.get(i).get(j) / (double)ITER_CNT);
            }
            freq.add(cur);
        }
        
        this.setRankovi(freq);
        
    }
    
    public static void main(String args[]){
        System.out.println("tu sam");
        ArrayList<Player> testna = new ArrayList<>();
        ArrayList<Double> emp = new ArrayList<>();
        testna.add(new Player("001", 1500d, 10d, "Marko", emp, emp));
        testna.add(new Player("002", 2100d, 15d, "Petar", emp, emp));
        testna.add(new Player("003", 1212d, 12d, "Ivan", emp, emp));
        testna.add(new Player("004", 1710d, 8d, "Luka", emp, emp));
        testna.add(new Player("005", 1823d, 11d, "Pero", emp, emp));
        testna.add(new Player("006", 1900d, 205d, "Stef", emp, emp));
        testna.add(new Player("007", 1312d, 12.44d, "Andrija", emp, emp));
        testna.add(new Player("008", 1500d, 12.12d, "Boris", emp, emp));
        testna.add(new Player("009", 2400d, 22.23d, "Milan", emp, emp));
        PredictEvent novi = new PredictEvent(testna);
        novi.process();
        HashMap<String, ArrayList<Double>> predvidi = novi.getRankovi();
        for(int i = 0; i < 9; i++){
            System.out.println("igrac: " + testna.get(i).getId());
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            for(int j = 0; j < 9; j++){
                System.out.print(df.format((Double)(predvidi.get(testna.get(i).getId()).get(j) * 100.0)) + "% ");
            }
            System.out.println();
        }
    }
}

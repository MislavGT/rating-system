/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.pmf.project.rating;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicIntegerArray;
import org.javatuples.Pair;

/**
 *
 * @author jurica
 */
public class CalculationJobPredict implements Callable<Void>{
    final static int EPSILON = 1000;
    
    ArrayList<Pair<Double, Integer>> al = null;
    ArrayList<AtomicIntegerArray> rankings = null;
    public CalculationJobPredict(ArrayList<Pair<Double, Integer>> al, ArrayList<AtomicIntegerArray> rankings){
        this.al = al;
        this.rankings = rankings;
    }
    
    @Override
    public Void call() throws Exception {
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
        
        int nPlayers = al.size();
        
        /* for(int k = 0; k < nPlayers - 1; k++){
            if((al.get(k).getValue0() - al.get(k+1).getValue0()) < EPSILON){
                Pair<Double, Integer> replacement = Pair.with
                    (al.get(k+1).getValue0(), al.get(k).getValue1());
                al.set(k+1, replacement);
            }
        } */
        
        for(int j = 0; j < nPlayers; j++){
            int player = al.get(j).getValue1();
            rankings.get(player).getAndIncrement(j);
            //rankings.get(player).set(j, c + 1);
        }
        return null;
    }
    
}

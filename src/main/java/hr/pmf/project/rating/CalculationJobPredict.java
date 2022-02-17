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
import org.javatuples.Triplet;

/**
 *
 * @author jurica
 */
public class CalculationJobPredict implements Callable<Void>{
    
    ArrayList<Pair<Double, Integer>> al = null;
    ArrayList<AtomicIntegerArray> rankings = null;
    ArrayList<Triplet<Double, Integer, Integer>> standardized = new ArrayList<>();
    public CalculationJobPredict(ArrayList<Pair<Double, Integer>> al, ArrayList<AtomicIntegerArray> rankings){
        this.al = al;
        this.rankings = rankings;
    }
    
    @Override
    public Void call() throws Exception {
        al.sort((Pair<Double, Integer> p1, Pair<Double, Integer> p2) -> {
            if((double)p1.getValue0() > (double)p2.getValue0())
                return -1;
            if((double)p1.getValue0() == (double)p2.getValue0())
                return 0;
            return 1;
        });
        
        int nPlayers = al.size(); 
        
        standardized.add(new Triplet(al.get(0).getValue0(), al.get(0).getValue1(), 1));
        if(RatingGUIController.DRAW){
            for(int i = 1; i < nPlayers; i++){
                if(al.get(i-1).getValue0() - al.get(i).getValue0() 
                        < ParameterOptimizer.EPSILON){
                    standardized.add(new Triplet(al.get(i).getValue0(), 
                            al.get(i).getValue1(), 
                            standardized.get(i-1).getValue2()));
                }
                else{
                    standardized.add(new Triplet(al.get(i).getValue0(), 
                            al.get(i).getValue1(), i+1));
                }
            }
        }
        else{
            for(int i = 1; i < nPlayers; i++){
                standardized.add(new Triplet(al.get(i).getValue0(), 
                            al.get(i).getValue1(), i+1));
            }
        }
        
        
        
        for(int j = 0; j < nPlayers; j++){
            int player = al.get(j).getValue1();
            rankings.get(player).getAndIncrement(
                    standardized.get(j).getValue2()-1);
            //rankings.get(player).set(j, c + 1);
        }
        return null;
    }
    
}

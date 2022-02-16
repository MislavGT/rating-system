
package hr.pmf.project.rating;

import java.util.ArrayList;

public class PredictionFitness {
    
    private double fitness;
    private final ArrayList<ArrayList<Double>> matrica;
    private final ArrayList<Integer> placements;
    
    public PredictionFitness(Event event){
        PredictEvent prediction = new PredictEvent(event.getPlayerList());
        prediction.process();
        this.matrica = prediction.matr();
        this.placements = event.getPlacements();
    }
    
    public void calculateFitness(){
        fitness = 0;
        for(int i = 0; i < placements.size(); i++){
            fitness += matrica.get(i).get(placements.get(i)-1);
        }
    }
    
    public double getFitness(){
        this.calculateFitness();
        return fitness;
    }
}


package hr.pmf.project.rating;

import java.util.ArrayList;

public class ParameterOptimizer {
    public static double BETA = 200;
    public static double GAMMA = 80;
    public static double RHO = 1;
    public static double EPSILON = 35;
    public static double FITNESS = 0;
    
    public ParameterOptimizer(Event event){
        
        PredictionFitness X;
        ArrayList<Double> probs = new ArrayList<>();
        X = new PredictionFitness(event);
        probs.add(X.getFitness());
        }
}
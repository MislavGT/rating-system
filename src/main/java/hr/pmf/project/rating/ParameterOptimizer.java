
package hr.pmf.project.rating;

import java.util.ArrayList;

public class ParameterOptimizer {
    public static double BETA = 200;
    public static double GAMMA = 80;
    public static double RHO = 1;
    public static double EPSILON = 35;
    
    public ParameterOptimizer(Event event){
        
        PredictionFitness X;
        ArrayList<Double> probs = new ArrayList<>();
        BETA *= 0.9;
        GAMMA *= 0.9;
        EPSILON *= 0.9;
        X = new PredictionFitness(event);
        probs.add(X.getFitness());
        BETA *= 1.22;
        GAMMA *= 1.22;
        EPSILON *= 1.22;
        X = new PredictionFitness(event);
        probs.add(X.getFitness());
        if(probs.get(0) > probs.get(1)){
            BETA /= 1.22;
            GAMMA /= 1.22;
            EPSILON /= 1.22;
        }
    }
}

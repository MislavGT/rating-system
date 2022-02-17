
package hr.pmf.project.rating;

import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import org.javatuples.Pair;
import org.javatuples.Triplet;

public class CalculationDiffuse implements Callable<Void>{
    
    Pair<Player, Integer> element = null;
    CopyOnWriteArrayList<Triplet<Double, Double, Integer>> priors = null;
    
    public CalculationDiffuse(Pair<Player, Integer> element, 
            CopyOnWriteArrayList<Triplet<Double, Double, Integer>> priors){
        this.element = element;
        this.priors = priors;
    }
    
    @Override
    public Void call() throws Exception {
        
        Player player = element.getValue0();
        double k = 1 / (1 + (Math.pow(ParameterOptimizer.GAMMA, 2) / 
               Math.pow(player.getSigma(), 2)));
        double w_G = Math.pow(k, ParameterOptimizer.RHO) * player.getD().get(0);
        double w_L = 0;
        for(int i = 0; i < player.getD().size(); i++){
            w_L += player.getD().get(i);
        }
        w_L *= (1 - Math.pow(k, ParameterOptimizer.RHO));
        player.getM().set(0, ((w_G * player.getM().get(0))
                + (w_L * player.getMean())) / (w_G + w_L));
        player.getD().set(0, k * (w_G + w_L));
        for(int i = 1; i < player.getD().size(); i++){player.getD().
                set(i, Math.pow(k, 1 + ParameterOptimizer.RHO)*player.getD().get(i));
        }
        player.setSigma(player.getSigma() / Math.sqrt(k));
        
        priors.add(new Triplet(Event.hyp(player.getSigma(), 
                ParameterOptimizer.BETA),
                player.getMean(),
                element.getValue1()));
        
        return null;
    }
}

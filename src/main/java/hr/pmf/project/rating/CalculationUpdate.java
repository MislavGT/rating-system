/*
package hr.pmf.project.rating;

import static hr.pmf.project.rating.Event.MAX_LENGTH;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import org.javatuples.Pair;

public class CalculationUpdate implements Callable<Void>{
    
    static{
        File lib = new File("src/jni/" + 
                System.mapLibraryName("JNI"));
        System.load(lib.getAbsolutePath());
    }
    
    Pair<Player, Integer> player = null;
    Event event = null;

    CalculationUpdate(Pair<Player, Integer> player, Event event){
        this.player = player;
        this.event = event;
    }
    public native double firstSolveZero(int placement, 
            ArrayList<Double> _deltas, ArrayList<Double> _priorMeans, 
            ArrayList<Integer> _placements, double start);
    
    public native double secondSolveZero(ArrayList<Double> _m_list, 
            ArrayList<Double> _d_list, double start, double parameter);
    
    @Override
    public Void call() throws Exception {
        double p = firstSolveZero(player.getValue1(), event.getDeltas(), 
                event.getPriorMeans(), 
                event.getPlaces(), player.getValue0().getMean());
        player.getValue0().addM(p);
        player.getValue0().addD(Math.pow(player.getValue0().getSigma(), -2));
        player.getValue0().setMean(secondSolveZero(player.getValue0().getM(),
                player.getValue0().getD(), player.getValue0().getMean(), ParameterOptimizer.BETA));
        player.getValue0().setSigma(
                Math.pow(Math.pow(player.getValue0().getSigma(), -2) + 
                        Math.pow(player.getValue0().getSigma(), -2), -0.5));
        if(player.getValue0().getD().size() > MAX_LENGTH){
            player.getValue0().getD().remove(0);
            player.getValue0().getM().remove(0);
        }
        return null;
    }
}
    
*/
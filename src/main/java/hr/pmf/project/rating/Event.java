
package hr.pmf.project.rating;

import java.util.ArrayList;
import static java.util.Collections.list;

public class Event {
    
    public static final double BETA = 1;
    public static final double GAMMA = 1;
    public static final double RHO = 0.5;
    
    private final ArrayList<Player> players;
    
    public Event(ArrayList<Player> players){
        this.players = players;
    }
    
    public Event addPlayer(Player player){
        players.add(player);
        return this;
    }
    
    public ArrayList<Player> getPlayers(){
        return players;
    }
    
    public void calculate(){
        for(Player player : players){
            diffuse(player);
            player.setDelta(Math.sqrt(Math.pow(BETA, 2) + Math.pow(player.getDeviation(), 2)));
            /*update(player);*/
        }
    }
    
    public void diffuse(Player player){
        double k = 1 / (1 + Math.pow(GAMMA, 2) / Math.pow(player.getDeviation(), 2));
        double w_G = Math.pow(k, RHO) * player.d_list.get(0);
        double w_L = 0;
        for(int i = 0; i < player.d_list.size(); i++){
            w_L += player.d_list.get(i);
        }
        w_L *= (1 - Math.pow(k, RHO));
        player.m_list.set(0, (w_G * player.m_list.get(0) + 
                w_L * player.getMean()) / (w_G + w_L));
        player.d_list.set(0, k * (w_G + w_L));
        for(int i = 1; i < player.d_list.size(); i++){
            player.d_list.set(i, Math.pow(player.d_list.get(i), 1 + RHO));
        }
        double new_deviation = player.getDeviation()/Math.sqrt(k);
        /* updateDatabase^ */
    }
    
  /*  public void update(Player player){
        double p = firstSolveZero(player);
        player.m_list.add(p);
        player.d_list.add(1 / Math.sqrt(BETA));
        double new_mean = secondSolveZero(player);
        /* updateDatabase^ */
    }
    
    /*public double firstSolveZero(Player player){
        /* https://docs.scipy.org/doc/scipy/reference/generated/scipy.optimize.newton.html */
  /*      return solution
/*    }
    
    public double secondSolveZero(Player player){
        /* https://docs.scipy.org/doc/scipy/reference/generated/scipy.optimize.newton.html */
    /*    return solution
    }
                
             
  */  
/*}*/


package hr.pmf.project.rating;

import java.util.ArrayList;
import java.util.Arrays;


public class Player{

    private final String id;
    private double mean;
    private double deviation;
    private double delta;
    private final String name;
    public ArrayList<Double> m_list;
    public ArrayList<Double> d_list;


    public Player(String id, double mean, double deviation, String name,
            ArrayList<Double> m_list, ArrayList<Double> d_list){
        this.id = id;
        this.mean = mean;
        this.deviation = deviation;
        this.name = name;
        this.m_list = m_list;
        this.d_list = d_list;
    }

    public String getId(){
        return id;
    }
    
    public double getMean(){
        return mean;
    }
    
    public void setMean(double mean){
        this.mean = mean;
    }
    
    public double getDeviation(){
        return deviation;
    }
    
    public void setDeviation(double deviation){
        this.deviation = deviation;
    }
    
    public double getDelta(){
        return delta;
    }
    
    public void setDelta(double delta){
        this.delta = delta;
    }
    
    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;
        
        return !(getId() != null ? !getId().equals(player.getId()) : player.getId() != null);

    }

    @Override
    public int hashCode() {
        int result;
        result = getId() != null ? getId().hashCode() : 0;
        return result;
    }
}
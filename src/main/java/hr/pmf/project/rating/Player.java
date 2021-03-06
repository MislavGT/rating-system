
package hr.pmf.project.rating;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Player{

    private final String id;
    private double mean;
    private double sigma;
    private final String name;
    private ArrayList<Double> m_list;
    private ArrayList<Double> d_list;
    
    
    public Player(String id, double mean, double sigma, String name,
                    ArrayList<Double> m_list, ArrayList<Double> d_list){
        this.id = id;
        this.mean = mean;
        this.sigma = sigma;
        this.name = name;
        this.m_list = m_list;
        this.d_list = d_list;
    }
    
    public ArrayList<Double> getM(){
        return m_list;
    }

    public ArrayList<Double> getD(){
        return d_list;
    }
    
    public void addM(double x){
        m_list.add(x);
    }
    
    public void addD(double x){
        d_list.add(x);
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
    
    public double getSigma(){
        return sigma;
    }
    
    public void setSigma(double sigma){
        this.sigma = sigma;
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

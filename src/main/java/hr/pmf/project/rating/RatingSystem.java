/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.pmf.project.rating;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.javatuples.Pair;

/**
 *
 * @author jurica
 */
public class RatingSystem {
    final int eventType;
    /*
    public static Player makePlayer(String handle, String playerId){
        //radimo novog igraca i stavljamo ga u bazu
        Player ret = null;
        double _mean = 1500d, _sigma = 350d;
        ArrayList<Double> m_list = new ArrayList<Double>();
        m_list.add(1500d);
        ArrayList<Double> d_list = new ArrayList<Double>();
        d_list.add(1d / (350d * 350d));
        ret = new Player(handle, _mean, _sigma, handle, m_list, d_list);
        return ret;
    }
    */
    
    public RatingSystem(int eventType){
        this.eventType = eventType;
    }
    
    public void readEvent(String fileName) throws SQLException{
        File file = new File(fileName);
        JavaSqlite baza = new JavaSqlite();
        List<Player> sviIgraci = null;
        try{
            sviIgraci = baza.SelectPlayers("SELECT * FROM Player WHERE player_id > 0");
            if(sviIgraci == null){
                throw new SQLException();
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        try{
            Scanner reader = new Scanner(file);
            Event curEvent = null;
            int nPlayers = 0;
            while(reader.hasNextLine()){
                String tmp[] = reader.nextLine().split(" ");
                if(tmp == null)
                    continue;
                if(tmp[0].equals("_contest:")){
                    //dolazimo na novi contest, racunamo ratinge za stari
                    if(nPlayers > 0){
                        curEvent.calculate();
                        curEvent.updateSql();
                    }
                    nPlayers = 0;
                    curEvent = new Event(new ArrayList<Pair<Player, Integer>>());
                }else{
                    int ranked = Integer.parseInt(tmp[1]);
                    String playerId = tmp[0];
                    nPlayers++;
                    Player curPlayer = null;
                    for(Player p : sviIgraci){
                        if(p.getId() == playerId){
                            curPlayer = p;
                            break;
                        }
                    }
                    if(curPlayer != null)
                        curEvent.addPlayer(curPlayer, ranked);
                    else{
                        throw new SQLException("igrac nije u bazi");
                    }
                }
            }
            reader.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
    }
    
    public static void main(String args[]) throws SQLException{
        //ovo ne radi dok ne stavimo ljude u bazu (putem guia il rucno)
        new RatingSystem(1).readEvent("eventi2.txt");
    }
}

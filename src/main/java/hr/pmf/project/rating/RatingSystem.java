
package hr.pmf.project.rating;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.javatuples.Pair;


public class RatingSystem {
    final int eventType;
    //0 - codeforces
    //1 - tennis
    public RatingSystem(int eventType){
        this.eventType = eventType;
    }
    
    public static String separiraj(String s){
        String ret = "";
        int n = s.length();
        for(int i = 0; i < n; i++){
            if(i != 0 && s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')
                ret = ret + " ";
            ret = ret + s.charAt(i);
        }
        return ret;
    }
    
    public HashMap<String, Player> readEvent(File file) throws SQLException, ClassNotFoundException{
        //ucita evente, kalkulira dogadaje i mijenja rejtinge u bazi
        JavaSqlite baza = new JavaSqlite();
        List<Player> sviIgraci = null;
        HashMap<String, Player> mapa = null;
        //vratim mapu koja za string imePlayera sadrzi objekt player odgovarajuci
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
            mapa = new HashMap<>();
            if((int)sviIgraci.size() == 0){
                System.out.println("nema igraca...");
                return null;
            }
            for(int i = 0; i < (int)sviIgraci.size(); i++){
                mapa.put(sviIgraci.get(i).getName(), sviIgraci.get(i));
            }
            while(reader.hasNextLine()){
                String tmp[] = reader.nextLine().split(" ");
                if(tmp == null)
                    continue;
                if(tmp[0].equals("_contest:")){
                    //dolazimo na novi contest, racunamo ratinge za stari
                    if(nPlayers > 0){
                        /**
                        boolean flagFitness = Boolean.TRUE;
                        ArrayList<Player> playerList = curEvent.getPlayerList();
                        for(int i = 0; i < playerList.size(); i++){
                            if(playerList.get(i).getD().size() < 10){
                                flagFitness = Boolean.FALSE;
                            }
                        }
                        PredictionFitness fitnessP;
                        if(flagFitness){
                            fitnessP = new PredictionFitness(curEvent);
                            ParameterOptimizer.FITNESS += fitnessP.getFitness();
                        }
                        */
                        curEvent.calculate();
                        ArrayList<Player> promjene = curEvent.getPlayerList();
                        for(int j = 0; j < (int)promjene.size(); j++){
                            String ime = promjene.get(j).getName();
                            mapa.remove(ime);
                            mapa.put(ime, promjene.get(j));
                        }
                    }
                    nPlayers = 0;
                    curEvent = new Event(new ArrayList<>());
                }else{
                    int ranked = Integer.parseInt(tmp[1]);
                    if(eventType == 1){
                        tmp[0] = separiraj(tmp[0]);
                    }
                    String playerId = tmp[0];
                    nPlayers++;
                    Player curPlayer = null;
                    if(mapa.containsKey(playerId)){
                        curPlayer = mapa.get(playerId);
                    }
                    if(curPlayer != null)
                        curEvent.addPlayer(curPlayer, ranked);
                    else{
                        System.out.println("problem je sa" + playerId);
                        throw new SQLException("igrac nije u bazi");
                    }
                }
            }
            reader.close();
            for(Player p : sviIgraci){
                baza.UpdatePlayer(p);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(ParameterOptimizer.FITNESS);
        return mapa;
    }
    
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException{
        
        File file = new File("tenis.txt");
        if(file != null){
            HashMap<String, Player> tmp = new RatingSystem(1).readEvent(file);
        }
        
        /*
        JavaSqlite baza = new JavaSqlite();
        List<Player> tmp = baza.SelectPlayers("SELECT * FROM Player WHERE player_id > 0");
        if(tmp != null){
            for(int i = 0; i < (int)tmp.size(); i++){
                baza.DeletePlayer(tmp.get(i));
            }
        }
        */
    }
}

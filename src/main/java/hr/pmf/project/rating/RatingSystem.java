/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.pmf.project.rating;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.javatuples.Pair;

/**
 *
 * @author jurica
 */
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
    
    /*
    public static void izdvojiLjude() throws FileNotFoundException, IOException{
        File file = new File("tenis.txt");
        Scanner sc = new Scanner(file);
        HashSet<String> ljudi = new HashSet<>();
        while(sc.hasNextLine()){
            String tmp[] = sc.nextLine().split(" ");
            if(tmp[0].equals("_contest:"))
                continue;
            ljudi.add(separiraj(tmp[0]));
        }
        Iterator it = ljudi.iterator();
        String svi = "";
        while(it.hasNext()){
            String cur = it.next().toString();
            System.out.println(cur);
            svi += cur + "\n";
        }
        sc.close();
        FileWriter fw = new FileWriter("tenisaci.txt");
        fw.write(svi);
        fw.close();
    }
    */
    
    public List<Event> readEvent(File file) throws SQLException{
        //ucita evente, kalkulira dogadaje i mijenja rejtinge u bazi
        JavaSqlite baza = new JavaSqlite();
        List<Player> sviIgraci = null;
        List<Event> ret = new ArrayList<Event>();
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
                        ret.add(curEvent);
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
                    for(Player p : sviIgraci){
                        if(p.getName().equals(playerId)){
                            curPlayer = p;
                            break;
                        }
                    }
                    if(curPlayer != null)
                        curEvent.addPlayer(curPlayer, ranked);
                    else{
                        //System.out.println("problem je sa" + playerId);
                        throw new SQLException("igrac nije u bazi");
                    }
                }
            }
            reader.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        return ret;
    }
    
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException{
        //ovo ne radi dok ne stavimo ljude u bazu (putem guia il rucno)
        
        File file = new File("tenis.txt");
        if(file != null){
            List<Event> tmp = new RatingSystem(1).readEvent(file);
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
        
        //izdvojiLjude();
    }
}

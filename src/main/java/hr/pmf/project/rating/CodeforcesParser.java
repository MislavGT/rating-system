/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.pmf.project.rating;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jurica
 */
public class CodeforcesParser {
    static HashMap<String, Integer> appeared = null;  
    //za svaki handle pamtim koliko contesta ima u promatranima
    final static int POCETAK = 1600; //id prvog contesta koji analiziram
    final static int KRAJ = 1638;
    final static int RANK_LIMIT = 8000; //prvih 8000 po contestu
    final static int MIN_PARTICIPATIONS = 15; //minimalno sudjelovanja
    
    public static void uzmiUzorak(){
        //prodje po zadnjim rundama na cf-u i uzima ljude
        //oni koji su bili na barem 15 rundi ce biti zapamceni
        ArrayList<String> bitni = new ArrayList<>();
        appeared = new HashMap<>();
        for(int i = POCETAK; i <= KRAJ; i++){
            ArrayList<String> rankLista = new CodeforcesParser().skiniRezultate(i + "");
            if(rankLista == null || rankLista.size() != RANK_LIMIT){
                continue; //ovo je neki timski contest ili nebitni
            }
            for(String players : rankLista){
                int cnt = 0;
                if(appeared.containsKey(players)){
                    cnt = appeared.get(players);
                    appeared.remove(players);
                }
                appeared.put(players, cnt + 1);
            }
            //System.out.println(rankLista.size());
        }
        
        for(Map.Entry<String, Integer> entry : appeared.entrySet()){
           if(entry.getValue() > MIN_PARTICIPATIONS)
               bitni.add(entry.getKey());
        }
        
        System.out.println(bitni.size());
        for(String hendlovi : bitni){
            System.out.println(hendlovi);
        }
        
        try{
            FileWriter pisac = new FileWriter("output.txt");
            for(String hendlovi : bitni)
                pisac.write(hendlovi + "\n");
            pisac.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static String readUrl(String urlString){
        //pomocna fja za citanje s cf
        BufferedReader reader = null;
        String ret = null;
        try{
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            ret = buffer.toString();
            if(reader != null)
                reader.close();
        }catch(Exception e){
            return null;
        }
        
        return ret;
    }
    
    public static Player getPlayer(String handle){
        String urlinfo = "https://codeforces.com/api/user.info?handles=" + handle + ";";
        String code = null;
        for(int it = 0; it < 5; it++){
            code = readUrl(urlinfo);
            if(code != null) break;
        }
        if(code == null){
            //igrac ne postoji u bazi
            return null;
        }
        JSONObject obj = new JSONObject(code);
        JSONArray result = obj.getJSONArray("result");
        if(result.length() == 0)
            return null;
        double mean = 0d, sigma = 0d;
        JSONObject user = result.getJSONObject(0);
        int rating = user.getInt("rating");
        mean = Double.parseDouble(rating + "");
        ArrayList<Double> emp = new ArrayList<>();
        //ovdje radimo playera prvi put. Sto stavljam u ove liste?
        //biti ce bitan ID koji koristimo za svoju bazu - to npr. moze 
        //biti i ovaj handle? jer je na cfu jedinstven
        Player newPlayer = new Player(handle, mean, sigma, handle, emp, emp);
        newPlayer.setMean(mean);
        newPlayer.setSigma(0d);
        return newPlayer;
    }
    
    public static ArrayList<Integer> getRank(String handle){
        //ova funckija za igraca daje njegove rankove u svim promatranim contestima
        //ako na nekom nije bio, tamo stavljamo -1
        ArrayList<Integer> ret = new ArrayList<>();
        String code = null;
        while(code == null){
            code = readUrl("https://codeforces.com/api/user.rating?handle=" + handle);
        }
        JSONObject obj = new JSONObject(code);
        JSONArray result = obj.getJSONArray("result");
        int N = result.length();
        for(int i = 0; i < KRAJ - POCETAK + 1; i++){
            ret.add(-1);
        }
        for(int i = 0; i < N; i++){
            JSONObject tmp = result.getJSONObject(i);
            int tmpID = tmp.getInt("contestId");
            if(tmpID >= POCETAK && tmpID <= KRAJ){
                ret.set(tmpID - POCETAK, tmp.getInt("rank"));
            }
        }
        return ret;
    }
    
    public ArrayList<String> skiniRezultate(String contestId){
        //uzmi rezultate natjecanja
        String code = readUrl("https://codeforces.com/api/contest.standings?contestId=" + contestId +
                "&from=1&" + "&count=" + RANK_LIMIT + "&showUnofficial=false");
        if(code == null){
            return null;
        }
        ArrayList<String> ret = new ArrayList<>();
        JSONObject obj = new JSONObject(code);
        JSONObject result = obj.getJSONObject("result");
        JSONArray row = result.getJSONArray("rows");
        
        int N = row.length();
        for(int i = 0; i < N; i++){
            JSONObject person = row.getJSONObject(i);
            JSONObject party = person.getJSONObject("party");
            JSONArray members = party.getJSONArray("members");
            int k = (int)members.length();
            if(k > 1)
                return null; //ne zanimaju nas timska natjecanja
            String ime = "";
            for(int j = 0; j < k; j++){
                String handle = members.getJSONObject(j).getString("handle");
                ime += handle;
                if(j != k - 1) ime += "\n";
            }
            ret.add(ime);
            //System.out.println(ime);
        }
        
        return ret;
    }
    
    public static List<Event> makeEvents(){
        List<Event> eventi = new ArrayList<>();
        for(int i = 0; i < KRAJ - POCETAK + 1; i++)
            eventi.add(new Event(new ArrayList<Pair<Player, Integer>>()));
     
        String cijela = "";
        File datoteka = new File("output.txt"); 
        //u datoteci su svi ljudi koji imaju barem MIN_PARTICIPATION contesta
        int cnt = 0;
        //uzimam samo prvih 20 ljudi jer pre dugo traje
        try{
            Scanner sc = new Scanner(datoteka);
            while(sc.hasNextLine()){
                cnt++;
                String playerHandle = sc.nextLine();
                Player curUser = getPlayer(playerHandle);
                if(curUser == null)
                    System.out.println("nemoguce");
                ArrayList<Integer> mojiRankovi = getRank(playerHandle);
                if(mojiRankovi == null){
                    //ovo nebi smjelo
                    continue;
                }
                for(int i = POCETAK; i <= KRAJ; i++){
                    int ranked = mojiRankovi.get(i - POCETAK);
                    if(ranked == -1)
                        continue;
                    eventi.get(i - POCETAK).addPlayer(curUser, ranked);
                }
                if(cnt == 30)
                    break;
            } 
            sc.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
            
        for(int i = 0; i < eventi.size(); i++){
            Event e = eventi.get(i);
            ArrayList<Pair<Player, Integer>> tmp = e.getPlayers();
            if(tmp == null || tmp.size() == 0)
                continue;
            String van = "_contest: " + (i + POCETAK) + "\n";
            for(Pair<Player, Integer> p : tmp){
                van += p.getValue0().getId() + " " + p.getValue1() + "\n";
            }
            cijela += van;
        }
        
        try{
            FileWriter fw = new FileWriter("eventi2.txt");
            fw.write(cijela);
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        
        return eventi;
    }
    
    public static void main(String args[]){
        //new CodeforcesParser().skiniRezultate("1638");
        //getUser("tourist");
        //uzmiUzorak(); 
        makeEvents();
    }
}

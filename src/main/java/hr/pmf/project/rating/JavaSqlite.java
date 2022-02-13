/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.pmf.project.rating;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.ProcessBuilder.Redirect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import org.javatuples.Pair;


/**
 *
 * @author matij
 */
public class JavaSqlite {
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
    // TODO code application logic here
    JavaSqlite app = new JavaSqlite();
    ArrayList<Double> first = new ArrayList<Double>();
    first.add(2.0);
    first.add(3.0);
    first.add(4.0);
    first.add(5.0);
    ArrayList<Double> second = new ArrayList<Double>();
    second.add(1.2);
    second.add(1.3);
    second.add(1.8);


    Player plyr = new Player("59",2,3, "Dorian", first, second  );
    ArrayList<Pair<Player,Integer>> for_evt = new ArrayList<Pair<Player,Integer>>();
    for_evt.add(new Pair(plyr,10));
    Event evt = new Event(for_evt);
    //app.InsertPlayer(plyr);
    //app.InsertEvent(evt);
    List<Event> events = app.SelectEvents("SELECT * FROM Event");
    for (Event element : events)
    {
        for(Pair<Player, Integer> item: element.getPlayers())
            System.out.println(item.getValue0().getName());

    }
    System.out.println("prije player");
    List<Player> players = app.SelectPlayers("SELECT * FROM Player");
    for (Player element : players)
        System.out.println(element.getName());
    
    }
    
    
    public List<Player> SelectPlayers(String sql) throws ClassNotFoundException, SQLException
    {
        List<Player> players = new ArrayList<Player>();
        Statement stmt = null;
        try(Connection conn = connect())
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next())
            {
                GsonBuilder builder = new GsonBuilder(); 
                builder.setPrettyPrinting(); 
                Gson gson = builder.create();

                Double[] m_l = gson.fromJson(rs.getString("m_list"), Double[].class);

                ArrayList<Double> m_list = new ArrayList<>();
                for(double d : m_l) m_list.add(d);
                
                Double[] d_l = gson.fromJson(rs.getString("d_list"), Double[].class);
                ArrayList<Double> d_list = new ArrayList<>();
                for(double d : d_l) d_list.add(d);

                Player player = new Player(rs.getString("player_id"),rs.getDouble("mean"), rs.getDouble("sigma"),rs.getString("name"), m_list, d_list);

                
                players.add(player);
            }
            
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        stmt.close();
        return players;
    }
    
    public List<Event> SelectEvents(String sql) throws ClassNotFoundException, SQLException
    {
        List<Event> events = new ArrayList<Event>();
        Statement stmt = null;
        try(Connection conn = connect())
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next())
            {
                GsonBuilder builder = new GsonBuilder(); 
                builder.setPrettyPrinting(); 
                Gson gson = builder.create();
                
                Player[] plyrs = gson.fromJson(rs.getString("players"), Player[].class);
                int[] positions = gson.fromJson(rs.getString("place"), int[].class);
                ArrayList<Pair<Player, Integer>> lista = new ArrayList<Pair<Player, Integer>>();
                for(int i = 0; i < plyrs.length;i++)
                {
                    lista.add(new Pair(plyrs[i], positions[i]));
                }
                events.add(new Event(lista));
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        stmt.close();
        return events;
    }
    
    public void InsertPlayer(Player player) throws ClassNotFoundException, SQLException
    {
        Statement stmt = null;
        //System.out.println("Prije try");

        try(Connection conn = connect())
        {
            Gson gson = new Gson(); 
            stmt = conn.createStatement();
            String sql = "INSERT INTO Player( name, mean, sigma, m_list, d_list) VALUES( '" + player.getName()+"', " + player.getMean() + ", "+player.getSigma()+", '" + gson.toJson(player.getM()) +"', '" + gson.toJson(player.getD())+"');";
            stmt.executeQuery(sql);
            

        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        stmt.close();
        return;
    }
    
    public void InsertEvent(Event event) throws ClassNotFoundException, SQLException
    {
        Statement stmt = null;
        Player[] plyrs = new Player[event.getPlayers().size()];
        int[] positions = new int[event.getPlayers().size()];
        int i = 0;
        for(Pair<Player,Integer> item : event.getPlayers())
        {
            plyrs[i] = item.getValue0();
            positions[i] = item.getValue1();
        }
        try(Connection conn = connect())
        {
            Gson gson = new Gson();
            stmt = conn.createStatement();
            String sql = "INSERT INTO Event(players, place) VALUES('"+gson.toJson(plyrs)+"', '"+gson.toJson(positions)+"');";
            System.out.println(sql );
            stmt.executeQuery(sql);
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        stmt.close();
        return;
    }
     
        
    public Connection connect() throws ClassNotFoundException
    {
        String url = "jdbc:sqlite:ratingsystemDB.db";
        Connection conn = null;
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return conn;
    }
           
}

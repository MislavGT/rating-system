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


    Player plyr = new Player("51",2,3, "Karl", first, second  );
    ArrayList<Player> for_evt = new ArrayList<Player>();
    for_evt.add(plyr);
    Event evt = new Event(for_evt);
    System.out.println("Prije");
    //app.InsertPlayer(plyr);
    //app.InsertEvent(evt);
    List<Event> events = app.SelectEvents("SELECT * FROM Event");
    for (Event element : events)
    {
        for(Player item: element.getPlayers())
            System.out.println(item.getName());

    }
    System.out.println("Poslije");

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

                Player player = new Player(rs.getString("player_id"),rs.getDouble("mean"), rs.getDouble("deviation"),rs.getString("name"), m_list, d_list);

                
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
                ArrayList<Player> players = new ArrayList<Player>();
                for(Player d : plyrs) players.add(d);
                
                Event event = new Event(players);
                events.add(event);
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
            String sql = "INSERT INTO Player( name, mean, deviation,m_list, d_list) VALUES( '" + player.getName()+"', " + player.getMean() + ", " + player.getDeviation() + ", '" + gson.toJson(player.m_list) +"', '" + gson.toJson(player.d_list)+"');";
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
        try(Connection conn = connect())
        {
            Gson gson = new Gson();
            stmt = conn.createStatement();
            String sql = "INSERT INTO Event(players) VALUES('"+gson.toJson(event.getPlayers())+"');";
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

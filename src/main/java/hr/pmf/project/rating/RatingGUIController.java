/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.pmf.project.rating;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import hr.pmf.project.rating.Event;
import hr.pmf.project.rating.JavaSqlite;
import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import org.javatuples.Pair;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.concurrent.Task ;

/**
 * FXML Controller class
 *
 * @author matij
 */
public class RatingGUIController implements Initializable {

    @FXML
    private TextField brojIgraca;
    @FXML
    private TextField ID;
    @FXML
    private TextField place;
    @FXML
    private Button updateButton;
    @FXML
    private Button unesiButton;
    @FXML
    private Button gotovDogadajButton;
    @FXML
    private TableView<PlayerTable> tablica;
    @FXML
    public TableColumn<PlayerTable, String> ime;
    @FXML
    public TableColumn<PlayerTable, Double> rating;
    TableView tablicaCopy;
    
    FileChooser odabirDatoteka;
    ArrayList<Pair<Player,Integer>> players = new ArrayList<Pair<Player,Integer>>();
    Queue<Event> eventsInProgress = new LinkedList<>();
    ObservableList<PlayerTable> data = FXCollections.observableArrayList();
    JavaSqlite App;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            App = new JavaSqlite();
            try {
                // TODO
                App.DeleteAllPlayers();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RatingGUIController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(RatingGUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
            brojIgraca.setDisable(true);
            ID.setDisable(true);
            place.setDisable(true);
            updateButton.setDisable(true);
            unesiButton.setDisable(true);
            odabirDatoteka = new FileChooser();
            odabirDatoteka.setTitle("Otvori .txt datoteku");
            odabirDatoteka.getExtensionFilters().addAll(new ExtensionFilter("Sve tekstualne", "*.txt"));
            ime.setCellValueFactory(new PropertyValueFactory<>("Ime"));
            rating.setCellValueFactory(new PropertyValueFactory<>("Rating"));
            
            tablica.setItems(data);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RatingGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void dogadajHandler(ActionEvent event) {
        brojIgraca.setDisable(false);
        ID.setDisable(false);
        place.setDisable(false);
        unesiButton.setDisable(false);
        
    }
    boolean check = true;
    int brojac = 0;
    @FXML
    private void unesiHandler(ActionEvent event) throws ClassNotFoundException, SQLException {
        boolean integerCheck = true;
        int placeNum = 0;
        int idNum = 0;
        int brIgraca = 0;
        try{        
            placeNum = Integer.parseInt(place.getText());
            idNum = Integer.parseInt(ID.getText());
            brIgraca = Integer.parseInt(brojIgraca.getText());
        }
        catch(Exception e)
        {
            integerCheck = false;
        }
        if(integerCheck && placeNum > 0 && idNum > 0 && brIgraca > 0)
        {
            if(check)
                brojac = brIgraca;
        
            List<Player> playerWithId = App.SelectPlayers("SELECT * FROM Player WHERE player_id = " + idNum + ";");
            System.out.println("Ime: " + playerWithId.get(0).getName() + ", mjesto: " + placeNum);
            players.add(new Pair(playerWithId.get(0),placeNum));
            check = false;
            brojac--;
            ID.deleteText(0,ID.getLength());
            place.deleteText(0,place.getLength());
            for(Pair<Player, Integer> item : players )
                System.out.println("Ime: "+ item.getValue0().getName() + ", mjesto: " + item.getValue1());
        
            if(brojac == 0)
            {
                brojIgraca.deleteText(0, brojIgraca.getLength());
                brojIgraca.setDisable(true);
                ID.setDisable(true);
                place.setDisable(true);
                unesiButton.setDisable(true);

                updateButton.setDisable(false);
                Event evnt = new Event(players);
                eventsInProgress.add(evnt);
                App.InsertEvent(evnt);
                players.clear();
                check = true;
            }
        }
        else
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Krivi unos podataka");
            alert.setContentText("Broj igrača, ID i mjesto moraju biti prirodni brojevi");
            brojIgraca.setDisable(true);
            ID.setDisable(true);
            place.setDisable(true);
            updateButton.setDisable(true);
            unesiButton.setDisable(true);
            ID.deleteText(0,ID.getLength());
            place.deleteText(0,place.getLength());
            brojIgraca.deleteText(0, brojIgraca.getLength());
            alert.showAndWait();
        }
        
    }
    @FXML
    private void updateHandler(ActionEvent event) throws ClassNotFoundException
    {
        Event notProcessedEvent = eventsInProgress.peek();
        notProcessedEvent.calculate();
        notProcessedEvent.updateSql();
        for(Player plyr : notProcessedEvent.getPlayerList())
        {
            for(PlayerTable plTable : tablica.getItems())
            {
                if(plyr.getName().equals(plTable.getIme()))
                {
                        plTable.setRating(plyr.getMean());
                        break;
                }
            }
        }
        eventsInProgress.remove();
        if(eventsInProgress.size() == 0)
            updateButton.setDisable(true);
            
        
    }
    
    @FXML
    private void gotovDogadajHandler(ActionEvent event) throws SQLException, ClassNotFoundException
    {
        File odabrana = odabirDatoteka.showOpenDialog (new Stage());
        RatingSystem RS = new RatingSystem(0);
        List<Event> lista = RS.readEvent(odabrana);
        System.out.println("Booooooook");
        for(Event evt : lista)
        {
            for(Player plyr : evt.getPlayerList())
            {
                for(PlayerTable plTable : tablica.getItems())
                {
                    if(plyr.getName().equals(plTable.getIme()) )
                    {
                        plTable.setRating(plyr.getMean());
                        break;
                    }
                }
            }
        }
        //tablica.setItems(tablica.getItems());
    }
    
    @FXML
    private void datasetHandler(ActionEvent event)
    {
        
        File odabrana = odabirDatoteka.showOpenDialog(new Stage()) ;
        Task zadatak = new Task<Void>(){
            @Override
            public Void call() throws Exception {
                try{
                    String cijela = "", linija = "";
                    Path p = Paths.get(odabrana.getAbsolutePath());
                    BufferedReader citac = Files.newBufferedReader(p, StandardCharsets.UTF_8);
                    while((linija = citac.readLine()) != null)
                    {
                        if ( isCancelled () ) { break ; }
                        cijela += linija + "\n";
                        ArrayList<Double> m_list = new ArrayList<Double>();
                        ArrayList<Double> d_list = new ArrayList<Double>();
                        m_list.add(1500.);
                        d_list.add(Double.valueOf(1)/Double.valueOf(350*350));
                        Player forDatabase = new Player("",1500.,350.,linija,m_list,d_list);
                        App.InsertPlayer(forDatabase);
                
                        data.add(new PlayerTable(linija, 1500.));
                    }
        
                }
                catch(Exception e){}
                return null;}
        };
        /*try{
            String cijela = "", linija = "";
            Path p = Paths.get(odabrana.getAbsolutePath());
            BufferedReader citac = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            while((linija = citac.readLine()) != null)
            {
                cijela += linija + "\n";
                ArrayList<Double> m_list = new ArrayList<Double>();
                ArrayList<Double> d_list = new ArrayList<Double>();
                m_list.add(1500.);
                d_list.add(Double.valueOf(1)/Double.valueOf(350*350));
                Player forDatabase = new Player("",1500.,350.,linija,m_list,d_list);
                App.InsertPlayer(forDatabase);
                
                data.add(new PlayerTable(linija, 1500.));
            }
        
        }
        catch(Exception e){}*/
    }
    
    public class PlayerTable{
        SimpleStringProperty Ime;
        SimpleDoubleProperty Rating;
        
        public PlayerTable(String n, Double m)
        {
            Ime = new SimpleStringProperty(n); 
            Rating = new SimpleDoubleProperty(m);;
        }
        public String getIme()
        {
            return Ime.get();
        }
        public void setIme(String ime)
        {
            Ime.set(ime);
        }
        public double getRating()
        {
            return Rating.get();
        }
        public void setRating(double rating)
        {
            Rating.set(rating);
        }
    
    }
    
}

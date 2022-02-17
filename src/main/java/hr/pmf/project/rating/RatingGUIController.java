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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javax.swing.text.PlainDocument;

/**
 * FXML Controller class
 *
 * @author matij
 */
public class RatingGUIController implements Initializable {
    
    @FXML
    private ProgressBar progressBar;
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
    ArrayList<Pair<Player,Integer>> players = new ArrayList<>();
    Queue<Event> eventsInProgress = new LinkedList<>();
    ObservableList<PlayerTable> data = FXCollections.observableArrayList();
    JavaSqlite App;
    HashMap<String, Player> stanjeIgraca = null;
    int dataType = 0;
    int cntPlayers; //u rucnom eventu
    ArrayList<Pair<Player, Integer>> rucniEvent;
    
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
            brojIgraca.setDisable(false);
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
            stanjeIgraca = new HashMap<>();
            
            tablica.setRowFactory(tv -> {;
                TableRow<PlayerTable> row = new TableRow<PlayerTable>();
                row.setOnMouseClicked(event -> {
                    if(event.getClickCount() == 2 && (!row.isEmpty())){
                        PlayerTable tmp = row.getItem();
                        Task<Void> zadatak = new Task<Void>(){
                            @Override
                            public Void call(){
                                Player odabraniPlayer = stanjeIgraca.get(tmp.getIme());
                                Chart dijagram = new Chart(tmp.getIme(), odabraniPlayer.getM());
                                dijagram.draw();
                                return null;
                            }
                        };
                        
                        new Thread(zadatak).start();
                        
                        //System.out.println("igrac je " + tmp.getIme());
                    }
                });
                return row;
            });
            
            rating.setCellFactory(tc -> new TableCell<PlayerTable, Double>(){
                @Override
                protected void updateItem(Double value, boolean empty){
                    super.updateItem(value, empty);
                    if(empty){
                        setText(null);
                    }else{
                        int tmp = value.intValue();
                        setText(String.format("%d", tmp));
                    }
                
                }
                
                
            });
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RatingGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void dogadajHandler(ActionEvent event) {
        if(stanjeIgraca == null){
            //nemoguc dogadaj, nemamo igrace u bazi!
            return;
        }
        try{
            cntPlayers = Integer.parseInt(brojIgraca.getText());
            brojIgraca.setText("");
            brojIgraca.setDisable(true);
            ID.setDisable(false);
            place.setDisable(false);
            unesiButton.setDisable(false);
            rucniEvent = new ArrayList<Pair<Player, Integer>>();
            gotovDogadajButton.setDisable(true);
        }catch(Exception e){
            //ovdje baciti upozorenje
            e.printStackTrace();
        }
    }
    
    @FXML
    private void unesiHandler(ActionEvent event) throws ClassNotFoundException, SQLException {
        try{        
            int placeNum = Integer.parseInt(place.getText());
            String igrac = ID.getText();
            if(!stanjeIgraca.containsKey(igrac)){
                //greska, mora biti u bazi, tj. mapi!
                return;
            }
            rucniEvent.add(new Pair(stanjeIgraca.get(igrac), placeNum));
            cntPlayers--;
        }
        catch(Exception e){
            //ovdje isto izbaciti prozor
            e.printStackTrace();
        }
        if(cntPlayers == 0){
            unesiButton.setDisable(true);
            updateButton.setDisable(false);
        }
        place.setText("");
        ID.setText("");
    }
    @FXML
    private void updateHandler(ActionEvent event) throws ClassNotFoundException{
        //ovdje je ustvari PREDIKCIJA
        ArrayList<Player> tmp = new ArrayList<>();
        for(int j = 0; j < (int)rucniEvent.size(); j++){
            tmp.add(rucniEvent.get(j).getValue0());
        }
        PredictionOutput.draw(tmp);
        Event noviEvent = new Event(rucniEvent);
        noviEvent.calculate();
        ArrayList<Player> promjene = noviEvent.getPlayerList();
        for(int i = 0; i < promjene.size(); i++){
            stanjeIgraca.remove(promjene.get(i).getName());
            stanjeIgraca.put(promjene.get(i).getName(), promjene.get(i));
        }
        
        noviEvent.updateSql();
        rucniEvent = null;
        updateButton.setDisable(true);
        ID.setDisable(false);
        refresajTablicu();
        gotovDogadajButton.setDisable(false);
    }
    
    public void refresajTablicu(){
        for(PlayerTable plTable : tablica.getItems()){
            Player novaVerzija = stanjeIgraca.get(plTable.getIme());
            plTable.setRating(novaVerzija.getMean());
        }
        tablica.refresh();
    }
    
    @FXML
    private void gotovDogadajHandler(ActionEvent event) throws SQLException, ClassNotFoundException
    {
        File odabrana = odabirDatoteka.showOpenDialog(new Stage());
        if(odabrana == null)
            return;
        String imeDatoteke = odabrana.getName();
        int flag = (imeDatoteke.charAt(0) == 't') ? 1 : 0; 
        Task<Void> zadatak = new Task<Void>(){
            @Override
            public Void call() throws SQLException, ClassNotFoundException{
                RatingSystem RS = new RatingSystem(flag);
                HashMap<String, Player> promjene = RS.readEvent(odabrana);
             //   System.out.println("Booooooook");
                for(PlayerTable plTable : tablica.getItems())
                {
                    Player novaVerzija = promjene.get(plTable.getIme());
                    plTable.setRating(novaVerzija.getMean());
                }
                tablica.refresh();
                for(Map.Entry<String, Player> entry : promjene.entrySet()){
                    if(stanjeIgraca.containsKey(entry.getKey()))
                        stanjeIgraca.remove(entry.getKey());
                    stanjeIgraca.put(entry.getKey(), entry.getValue());
                }
                updateProgress(1, 1);
                return null;
            }
        };
        progressBar.progressProperty().bind(zadatak.progressProperty());
        new Thread(zadatak).start();
    }
    
    @FXML
    private void datasetHandler(ActionEvent event) throws InterruptedException
    {
        File odabrana = odabirDatoteka.showOpenDialog(new Stage());
        if(odabrana == null)
            return;
        Task zadatak = new Task<Void>(){
            @Override
            public Void call() throws Exception {
                try{
                    int flag = (odabrana.getName().charAt(0) == 't') ? 1 : 0;
                    dataType = flag;
                    String cijela = "", linija = "";
                    Path p = Paths.get(odabrana.getAbsolutePath());
                    BufferedReader citac = Files.newBufferedReader(p, StandardCharsets.UTF_8);
                    while((linija = citac.readLine()) != null)
                    {
                        if (isCancelled()) { break ; }
                        cijela += linija + "\n";
                        ArrayList<Double> m_list = new ArrayList<Double>();
                        ArrayList<Double> d_list = new ArrayList<Double>();
                        m_list.add(1500.);
                        d_list.add(1.0 / (350d * 350d));
                        Player forDatabase = new Player(linija,1500.,350.,linija,m_list,d_list);
                        App.InsertPlayer(forDatabase);
                        if(stanjeIgraca.containsKey(linija))
                            stanjeIgraca.remove(linija);
                        stanjeIgraca.put(linija, forDatabase);
                        data.add(new PlayerTable(linija, 1500.));
                    }
                    updateProgress(1, 1);
                }
                catch(Exception e){
                    return null;
                }
                return null;
            }
        };
        progressBar.progressProperty().bind(zadatak.progressProperty());
        new Thread(zadatak).start();
    }
    
    public class PlayerTable{
        SimpleStringProperty Ime;
        SimpleDoubleProperty Rating;
        
        public PlayerTable(String n, Double m)
        {
            Ime = new SimpleStringProperty(n); 
            Rating = new SimpleDoubleProperty(m);
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

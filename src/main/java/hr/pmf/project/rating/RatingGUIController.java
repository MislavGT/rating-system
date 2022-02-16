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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    
    FileChooser odabirDatoteka;
    ArrayList<Pair<Player,Integer>> players = new ArrayList<Pair<Player,Integer>>();
    Queue<Event> eventsInProgress = new LinkedList<>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        brojIgraca.setDisable(true);
        ID.setDisable(true);
        place.setDisable(true);
        updateButton.setDisable(true);
        unesiButton.setDisable(true);
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
            JavaSqlite App = new JavaSqlite();
        
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
    private void updateHandler(ActionEvent event)
    {
        System.out.println("Book");
        /*Event notProcessedEvent = eventsInProgress.peek();
        for(Pair<Player,Integer> item: notProcessedEvent.getPlayers())
        {
            notProcessedEvent.update(item);
        }*/
    }
    @FXML
    private void datasetHandler(ActionEvent event)
    {
        File odabrana = odabirDatoteka.showOpenDialog(new Stage());
        /*try{
            String cijela = "", linija = "";
            Path p = Paths.get(odabrana.getAbsolutePath());
            BufferedReader citac = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            while((linija = citac.readLine()) != null)
            {
                cijela += linija + "\n";
            }
        
        }
        catch(Exception e){}*/
    }
    
}

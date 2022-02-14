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
import java.util.ArrayList;
import org.javatuples.Pair;
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
    
    ArrayList<Pair<Player,Integer>> players = new ArrayList<Pair<Player,Integer>>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        brojIgraca.setDisable(false);
        ID.setDisable(false);
        place.setDisable(false);
    }    

    @FXML
    private void dogadajHandler(ActionEvent event) {
        brojIgraca.setEditable(true);
        ID.setEditable(true);
        place.setEditable(true);
        
    }
    boolean check = true;
    int brojac = 0;
    @FXML
    private void unesiHandler(ActionEvent event) throws ClassNotFoundException, SQLException {
        if(check)
            brojac = Integer.parseInt(brojIgraca.getText());
        JavaSqlite App = new JavaSqlite();
        int placeNum = Integer.parseInt(place.getText());
        int idNum = Integer.parseInt(ID.getText());
        List<Player> playerWithId = App.SelectPlayers("SELECT * FROM Player WHERE player_id = " + idNum + ";");
        players.add(new Pair(playerWithId.get(0),placeNum));
        check = false;
        brojac--;
        ID.deleteText(0,ID.getLength());
        place.deleteText(0,place.getLength());
        if(brojac == 0)
        {
            brojIgraca.deleteText(0, brojIgraca.getLength());
            brojIgraca.setDisable(false);
            ID.setDisable(false);
            place.setDisable(false);
            Event evnt = new Event(players);
            App.InsertEvent(evnt);
            
        }
    }
    
}

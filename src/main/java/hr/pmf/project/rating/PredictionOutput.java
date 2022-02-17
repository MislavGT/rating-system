package hr.pmf.project.rating;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.util.stream.Collectors;

public class PredictionOutput{
    
    public static void draw(ArrayList<Player> players){
        final JFrame frame = new JFrame();
        frame.setTitle("Predictions");
        frame.setSize(500, 500);

        JPanel panel = new JPanel();

        JTextArea text = new JTextArea(10, 30);
        
        PredictEvent prediction = new PredictEvent(players);
        
        prediction.process();
        
        ArrayList<ArrayList<Double>> matr = new ArrayList<>();
        
        matr = prediction.matr();
        
        String line = new String();
        
        text.append("\t#\t");
        
        for(int i = 1; i < players.size() + 1; i++){
            text.append(String.valueOf(i) + "\t");
        }
        
        text.append("\n");
        
        for(int i = 0; i < players.size(); i++){
            text.append(players.get(i).getName() + "\t\t");
            line = matr.get(i).stream().map(Object::toString)
                        .collect(Collectors.joining("\t"));
            text.append(line + "\n");
        }
        text.append("\t");
        players.forEach(_item -> {
            text.append("\t" + "%");
        });
                
        panel.add(text);
        
        JScrollPane scroll = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        frame.add(scroll);
        frame.pack();
        frame.setVisible(true);

    }
    
    public static void main(String[] args){
        
        ArrayList<Player> test = new ArrayList<>();
        ArrayList<Double> emp = new ArrayList<>();
        
        test.add(new Player("001", 3000d, 35d, "Marko", emp, emp));
        test.add(new Player("002", 5000d, 35d, "Petar", emp, emp));
        test.add(new Player("003", 6000d, 35d, "Ivan", emp, emp));
        test.add(new Player("004", 6000d, 35d, "Luka", emp, emp));
        test.add(new Player("005", 7000d, 35d, "Pero", emp, emp));
        test.add(new Player("006", 7000d, 35d, "Stef", emp, emp));
        test.add(new Player("007", 9000d, 35d, "Andrija", emp, emp));
        test.add(new Player("008", 9000d, 35d, "Boris", emp, emp));
        test.add(new Player("009", 9000d, 35d, "Milan", emp, emp));
        test.add(new Player("001", 3000d, 35d, "Marko", emp, emp));
        test.add(new Player("002", 5000d, 35d, "Petar", emp, emp));
        test.add(new Player("003", 6000d, 35d, "Ivan", emp, emp));
        test.add(new Player("004", 6000d, 35d, "Luka", emp, emp));
        test.add(new Player("005", 7000d, 35d, "Pero", emp, emp));
        test.add(new Player("006", 7000d, 35d, "Stef", emp, emp));
        test.add(new Player("007", 9000d, 35d, "Andrija", emp, emp));
        test.add(new Player("008", 9000d, 35d, "Boris", emp, emp));
        test.add(new Player("009", 9000d, 35d, "Milan", emp, emp));
        test.add(new Player("001", 3000d, 35d, "Marko", emp, emp));
        test.add(new Player("002", 5000d, 35d, "Petar", emp, emp));
        test.add(new Player("003", 6000d, 35d, "Ivan", emp, emp));
        test.add(new Player("004", 6000d, 35d, "Luka", emp, emp));
        test.add(new Player("005", 7000d, 35d, "Pero", emp, emp));
        test.add(new Player("006", 7000d, 35d, "Stef", emp, emp));
        test.add(new Player("007", 9000d, 35d, "Andrija", emp, emp));
        test.add(new Player("008", 9000d, 35d, "Boris", emp, emp));
        test.add(new Player("009", 9000d, 35d, "Milan", emp, emp));

        PredictionOutput.draw(test);
    }
}
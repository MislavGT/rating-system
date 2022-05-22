package hr.pmf.project.rating;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PredictEventTest {
    
    public PredictEventTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    /**
     * Test of matr method, of class PredictEvent.
     */
    @Test
    public void testMatr() {
        System.out.println("matr");
        ArrayList<Player> testna = new ArrayList<>();
        ArrayList<Double> emp = new ArrayList<>();
        testna.add(new Player("001", 3000d, 35d, "Marko", emp, emp));
        testna.add(new Player("002", 5000d, 35d, "Petar", emp, emp));
        testna.add(new Player("003", 6000d, 35d, "Ivan", emp, emp));
        testna.add(new Player("004", 6000d, 35d, "Luka", emp, emp));
        testna.add(new Player("005", 7000d, 35d, "Pero", emp, emp));
        testna.add(new Player("006", 7000d, 35d, "Stef", emp, emp));
        testna.add(new Player("007", 9000d, 35d, "Andrija", emp, emp));
        testna.add(new Player("008", 9000d, 35d, "Boris", emp, emp));
        testna.add(new Player("009", 100000d, 35d, "Milan", emp, emp));
        PredictEvent novi = new PredictEvent(testna);
        novi.process();
        ArrayList<ArrayList<Double>> matrica;
        matrica = novi.matr();
        assertEquals(100, matrica.get(8).get(0));
    }
}

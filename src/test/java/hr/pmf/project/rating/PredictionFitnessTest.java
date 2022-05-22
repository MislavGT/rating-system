package hr.pmf.project.rating;

import java.util.ArrayList;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PredictionFitnessTest {
    
    public PredictionFitnessTest() {
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
     * Test of getFitness method, of class PredictionFitness.
     */
    @Test
    public void testGetFitness() {
        System.out.println("getFitness");
        ArrayList<Double> m_list1 = new ArrayList<>();
        ArrayList<Double> d_list1 = new ArrayList<>();
        ArrayList<Double> m_list2 = new ArrayList<>();
        ArrayList<Double> d_list2 = new ArrayList<>();
        ArrayList<Double> m_list3 = new ArrayList<>();
        ArrayList<Double> d_list3 = new ArrayList<>();
        ArrayList<Double> m_list4 = new ArrayList<>();
        ArrayList<Double> d_list4 = new ArrayList<>();
        m_list1.add(10000d); m_list2.add(5000d); m_list3.add(0d);
        m_list4.add(-5000d);
        d_list1.add(Math.pow(1/5d, 2)); d_list2.add(Math.pow(1/5d, 2));
        d_list3.add(Math.pow(1/5d, 2)); d_list4.add(Math.pow(1/5d, 2));
        Player Marko = new Player("001", 10000, 5, "Marko", m_list1, d_list1);
        Player Ivan = new Player("002", 5000, 5, "Ivan", m_list2, d_list2);
        Player Luka = new Player("003", 0, 5, "Luka", m_list3, d_list3);
        Player Petar = new Player("004", -5000, 5, "Petar", m_list4, d_list4);
        Pair<Player, Integer> A = Pair.with(Marko, 1);
        Pair<Player, Integer> B = Pair.with(Ivan, 2);
        Pair<Player, Integer> C = Pair.with(Luka, 3);
        Pair<Player, Integer> D = Pair.with(Petar, 4);
        ArrayList<Pair<Player, Integer>> players = new ArrayList<>();
        players.add(A); players.add(B); players.add(C); players.add(D);
        Event instance = new Event(players);
        PredictionFitness P = new PredictionFitness(instance);
        assertEquals(400, P.getFitness(), 10);

    }
    
}

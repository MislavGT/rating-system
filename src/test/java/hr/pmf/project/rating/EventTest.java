package hr.pmf.project.rating;

import java.lang.constant.ConstantDescs;
import java.util.ArrayList;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Mislav
 */
public class EventTest {
    
    public EventTest() {
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
     * Test of hyp method, of class Event.
     */
    @Test
    public void testHyp() {
        System.out.println("hyp");
        double x = 10;
        double y = 20;
        double expResult = 22.36;
        double result = Event.hyp(x, y);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of getSize method, of class Event.
     */
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        ArrayList<Double> m_list1 = new ArrayList<>();
        ArrayList<Double> d_list1 = new ArrayList<>();
        ArrayList<Double> m_list2 = new ArrayList<>();
        ArrayList<Double> d_list2 = new ArrayList<>();
        ArrayList<Double> m_list3 = new ArrayList<>();
        ArrayList<Double> d_list3 = new ArrayList<>();
        ArrayList<Double> m_list4 = new ArrayList<>();
        ArrayList<Double> d_list4 = new ArrayList<>();
        m_list1.add(1500d); m_list2.add(1500d); m_list3.add(1500d);
        m_list4.add(1500d);
        d_list1.add(Math.pow(1/350d, 2)); d_list2.add(Math.pow(1/350d, 2));
        d_list3.add(Math.pow(1/350d, 2)); d_list4.add(Math.pow(1/350d, 2));
        Player Marko = new Player("001", 1500, 350, "Marko", m_list1, d_list1);
        Player Ivan = new Player("002", 1500, 350, "Ivan", m_list2, d_list2);
        Player Luka = new Player("003", 1500, 350, "Luka", m_list3, d_list3);
        Player Petar = new Player("004", 1500, 350, "Petar", m_list4, d_list4);
        Pair<Player, Integer> A = Pair.with(Marko, 1);
        Pair<Player, Integer> B = Pair.with(Ivan, 2);
        Pair<Player, Integer> C = Pair.with(Luka, 3);
        Pair<Player, Integer> D = Pair.with(Petar, 4);
        ArrayList<Pair<Player, Integer>> players = new ArrayList<>();
        players.add(A); players.add(B); players.add(C); players.add(D);
        Event instance = new Event(players);
        int expResult = 4;
        int result = instance.getSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of calculate method, of class Event.
     */
    @Test
    public void testCalculate() {
        System.out.println("calculate");
        ArrayList<Double> m_list1 = new ArrayList<>();
        ArrayList<Double> d_list1 = new ArrayList<>();
        ArrayList<Double> m_list2 = new ArrayList<>();
        ArrayList<Double> d_list2 = new ArrayList<>();
        ArrayList<Double> m_list3 = new ArrayList<>();
        ArrayList<Double> d_list3 = new ArrayList<>();
        ArrayList<Double> m_list4 = new ArrayList<>();
        ArrayList<Double> d_list4 = new ArrayList<>();
        m_list1.add(1500d); m_list2.add(1500d); m_list3.add(1500d);
        m_list4.add(1500d);
        d_list1.add(Math.pow(1/350d, 2)); d_list2.add(Math.pow(1/350d, 2));
        d_list3.add(Math.pow(1/350d, 2)); d_list4.add(Math.pow(1/350d, 2));
        Player Marko = new Player("001", 1500, 350, "Marko", m_list1, d_list1);
        Player Ivan = new Player("002", 1500, 350, "Ivan", m_list2, d_list2);
        Player Luka = new Player("003", 1500, 350, "Luka", m_list3, d_list3);
        Player Petar = new Player("004", 1500, 350, "Petar", m_list4, d_list4);
        Pair<Player, Integer> A = Pair.with(Marko, 1);
        Pair<Player, Integer> B = Pair.with(Ivan, 2);
        Pair<Player, Integer> C = Pair.with(Luka, 3);
        Pair<Player, Integer> D = Pair.with(Petar, 4);
        ArrayList<Pair<Player, Integer>> players = new ArrayList<>();
        players.add(A); players.add(B); players.add(C); players.add(D);
        Event instance = new Event(players);
        instance.calculate();
        assertTrue(Marko.getMean() > Ivan.getMean() &&
                Ivan.getMean() > Luka.getMean() &&
                Luka.getMean() > Petar.getMean(), "True");
    }
    
    /**
     * Test of cdf method, of class Event.
     */
    @Test
    public void testCdf() {
        System.out.println("cdf");
        ArrayList<Double> m_list1 = new ArrayList<>();
        ArrayList<Double> d_list1 = new ArrayList<>();
        ArrayList<Double> m_list2 = new ArrayList<>();
        ArrayList<Double> d_list2 = new ArrayList<>();
        Player Marko = new Player("001", 2500, 0, "Marko", m_list1, d_list1);
        Player Ivan = new Player("002", 0, 0, "Ivan", m_list2, d_list2);
        double x = Event.cdfParameter(Marko, Ivan);
        double expResult = 1;
        double result = Event.cdf(x);
        assertEquals(expResult, result, 0.01);
    }
}

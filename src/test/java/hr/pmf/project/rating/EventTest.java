package hr.pmf.project.rating;

import java.util.ArrayList;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class EventTest {
    
    public EventTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
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
    @org.junit.jupiter.api.Test
    public void testHyp() {
        System.out.println("hyp");
        double x = 0.0;
        double y = 0.0;
        double expResult = 0.0;
        double result = Event.hyp(x, y);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPlayer method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testAddPlayer_Player_int() {
        System.out.println("addPlayer");
        Player player = null;
        int rank = 0;
        Event instance = null;
        instance.addPlayer(player, rank);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPlayer method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testAddPlayer_Player() {
        System.out.println("addPlayer");
        Player player = null;
        Event instance = null;
        instance.addPlayer(player);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPlayers method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testGetPlayers() {
        System.out.println("getPlayers");
        Event instance = null;
        ArrayList<Pair<Player, Integer>> expResult = null;
        ArrayList<Pair<Player, Integer>> result = instance.getPlayers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculate method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testCalculate() {
        System.out.println("calculate");
        Event instance = null;
        instance.calculate();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of diffuse method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testDiffuse() {
        System.out.println("diffuse");
        Player player = null;
        Event instance = null;
        instance.diffuse(player);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testUpdate() {
        System.out.println("update");
        Pair<Player, Integer> player = null;
        Event instance = null;
        instance.update(player);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of firstSolveZero method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testFirstSolveZero() {
        System.out.println("firstSolveZero");
        int placement = 0;
        ArrayList<Double> _deltas = null;
        ArrayList<Double> _priorMeans = null;
        ArrayList<Integer> _placements = null;
        double start = 0.0;
        Event instance = null;
        double expResult = 0.0;
        double result = instance.firstSolveZero(placement, _deltas, _priorMeans, _placements, start);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of secondSolveZero method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testSecondSolveZero() {
        System.out.println("secondSolveZero");
        ArrayList<Double> _m_list = null;
        ArrayList<Double> _d_list = null;
        double start = 0.0;
        double parameter = 0.0;
        Event instance = null;
        double expResult = 0.0;
        double result = instance.secondSolveZero(_m_list, _d_list, start, parameter);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cdf method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testCdf() {
        System.out.println("cdf");
        double x = 0.0;
        double expResult = 0.0;
        double result = Event.cdf(x);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cdfParameter method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testCdfParameter() {
        System.out.println("cdfParameter");
        Player x = null;
        Player y = null;
        double expResult = 0.0;
        double result = Event.cdfParameter(x, y);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class Event.
     */
    @org.junit.jupiter.api.Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Event.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

package hr.pmf.project.rating;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
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
public class JavaSqliteTest {
    
    public JavaSqliteTest() {
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
     * Test of SelectEvents method, of class JavaSqlite.
     */
    @Test
    public void testSelectEvents() throws Exception {
        System.out.println("SelectEvents");
        JavaSqlite app = new JavaSqlite();
        ArrayList<Double> first = new ArrayList<>();
        first.add(2.0);
        first.add(3.0);
        first.add(4.0);
        first.add(5.0);
        ArrayList<Double> second = new ArrayList<>();
        second.add(1.2);
        second.add(1.3);
        second.add(1.8);
        Player plyr = new Player("59",2,3, "Dorian", first, second  );
        ArrayList<Pair<Player,Integer>> for_evt = new ArrayList<>();
        for_evt.add(new Pair(plyr,10));
        Event evt = new Event(for_evt);
        List<Event> events = app.SelectEvents("SELECT * FROM Event");
        int result = events.size();
        int expResult = 0;
        assertEquals(expResult, result);
    }
}

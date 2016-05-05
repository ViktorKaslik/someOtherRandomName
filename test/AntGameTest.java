/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ant.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vikto
 */
public class AntGameTest {
    
    public AntGameTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void antTest() {
        Ant ant = new Ant(0,0);
        assertEquals(ant.getId(),0);
        assertEquals(ant.getColor(),0);
        assertEquals(ant.getDirection(),0);
        assertEquals(ant.getResting(),0);
        assertEquals(ant.getState(),0);
        assertFalse(ant.HasFood());
        
        ant.setCoord(12, 15);
        int[] temp = ant.getCoord();
        assertEquals(temp[0],12);
        assertEquals(temp[1],15);
        
        ant.setDirection(5);
        ant.setResting(12);
        ant.setState(99);
        ant.setHasFood(true);
        assertEquals(ant.getDirection(),5);
        assertEquals(ant.getResting(),12);
        assertEquals(ant.getState(),99);
        assertTrue(ant.HasFood());
    }
    
    @Test
    public void markerTest() {
        Marker mark = new Marker();
        
        assertEquals(mark.getFood(),0);
        assertEquals(mark.getMarker(0, 4),0);
        
        mark.setFood(11);
        mark.placeMarker(0, 4);
        assertEquals(mark.getFood(),11);
        assertEquals(mark.getMarker(0, 4),1);
        mark.removeMarker(0, 4);
        assertEquals(mark.getMarker(0, 4),0);
    }
    
    @Test
    public void modelGenTest() {
        modelGen mod = new modelGen();
        
        assertTrue(mod.isValidWorld(mod.getBoard()));
    }
    
    @Test
    public void gameModelTest(){
        GameModel gm = new GameModel();
        int temp = gm.flip(5);
        assertTrue((temp<5 && temp>=0));
        temp = gm.flip(5);
        assertTrue((temp<5 && temp>=0));
        temp = gm.flip(5);
        assertTrue((temp<5 && temp>=0));
        temp = gm.flip(5);
        assertTrue((temp<5 && temp>=0));
        temp = gm.flip(5);
        assertTrue((temp<5 && temp>=0));
        temp = gm.flip(5);
        assertTrue((temp<5 && temp>=0));
        temp = gm.flip(5);
        assertTrue((temp<5 && temp>=0));
        
        
    }
}

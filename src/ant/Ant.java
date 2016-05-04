/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

/**
 *
 * @author viktor
 */
public class Ant {
    private int id;
    private int color;
    private int state;
    private int resting;
    private int direction;
    private int xCoord;
    private int yCoord;
    private boolean hasFood;
    
    /**
     * Creates an ant
     * @param _id is id of the ant (cant be changed), type int
     * @param _color is the colour of the ant (red/black), type int (red = 0, black=1)
     */
    public Ant(int _id, int _color){
        id = _id;
        color = _color;
        state = 0;
        resting = 0;
        direction = 0;
        hasFood = false;
    }
    
    
    
    
    
    /************************************ getters/ setters *********************/
    /**
     * gets the ants ID
     * @return ID
     */
    public int getId() { return id;}
    
    /**
     * gets ants colour
     * @return colour
     */
    public int getColor() { return color; }
    
    /**
     * gets ants state
     * @return state
     */
    public int getState() { return state; }
    /**
     * sets ants state
     * @param s state
     */
    public void setState(int s) { state = s;}

    /**
     * gets ants resting 
     * @return resting
     */
    public int getResting() { return resting; }
    
    /**
     * sets ants resting to rest
     * @param rest new resting value
     */
    public void setResting(int rest) {resting = rest;}

    /**
     * gets the direction that the ant is facing
     * @return direction
     */
    public int getDirection() { return direction; }
    
    /**
     * sets the direction to dir
     * @param dir 
     */
    public void setDirection(int dir){direction=dir;}

    /**
     * gets the hasFood value
     * @return hasFood
     */
    public boolean HasFood() { return hasFood; }
    
    /**
     * sets has food to hf
     * @param hf 
     */
    public void setHasFood(boolean hf){hasFood = hf;}
    
    /**
     * gets an ants coordinates
     * @return int[]{x,y}
     */
    public int[] getCoord(){return new int[]{xCoord,yCoord};}
    
    /**
     * sets the ants position to x,y
     * @param x
     * @param y 
     */
    public void setCoord(int x,int y){xCoord = x; yCoord = y;}


    
    
        
}

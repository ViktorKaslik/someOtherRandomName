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
    public int getId() { return id;}

    public int getColor() { return color; }

    public int getState() { return state; }
    public void setState(int s) { state = s;}


    public int getResting() { return resting; }
    public void setResting(int rest) {resting = rest;}

    public int getDirection() { return direction; }
    public void setDirection(int dir){direction=dir;}

    public boolean HasFood() { return hasFood; }
    public void setHasFood(boolean hf){hasFood = hf;}
    
    /**
     * gets an ants coordinates
     * @return int[]{x,y}
     */
    public int[] getCoord(){return new int[]{xCoord,yCoord};}
    public void setCoord(int x,int y){xCoord = x; yCoord = y;}


    
    
        
}

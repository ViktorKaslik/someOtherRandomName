/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

/**
 *
 * @author vikto
 */
public class Marker {
     private int[] red;
     private int[] black;
     private int food;
     
    /**
    * There is a marker for every cell, 
    * the marker contains information about 
    * which markers have been placed on a cell
    * and quantity of food on a cell
    */
    public Marker(){
        food = 0;
        red = new int[5];
        black = new int[5];
        for(int i=0; i<5; i++){
            red[i]=0;
            black[i]=0;
        }
    }

    /**
     * gets the value of marker 'marker' for ants of colour 'colour'
     * @param colour
     * @param marker
     * @return 1 if marker is present else 0
     */
    public int getMarker(int colour, int marker){
        if(colour == 0){
            return red[marker];
        }else if(colour == 1){
            return black[marker] = 1;
        }
        return -1;
    }
    
    /**
     * sets marker 'marker' for ant of colour 'colour' to 1
     * @param colour
     * @param marker 
     */
    public void placeMarker(int colour, int marker) {
        if(colour == 0){
            red[marker] = 1;
        }else if(colour == 1){
            black[marker] = 1;
        }
    }

    /**
     * sets marker 'marker' for ant of colour 'colour' to 0
     * @param colour
     * @param marker 
     */
   public void removeMarker(int colour, int marker) {
        if(colour == 0){
            red[marker] = 0;
        }else if(colour == 1){
            black[marker] = 0;
        }
   }
   
   /**
    * gets the number of food 
    * @return 
    */
   public int getFood(){
       return food;
   }
   
   /**
    * sets number of food
    * @param x 
    */
   public void setFood(int x){
       food = x;
   }
   
   
}

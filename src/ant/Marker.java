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
class Marker {
     private int[] red;
     private int[] black;
     private int food;
     
   public Marker(){
       food = 0;
        red = new int[5];
        black = new int[5];
        for(int i=0; i<5; i++){
            red[i]=0;
            black[i]=0;
        }
    }

    public int getMarker(int colour, int marker){
        if(colour == 0){
            return red[marker];
        }else if(colour == 1){
            return black[marker] = 1;
        }
        return -1;
    }
    
    public void placeMarker(int colour, int marker) {
        if(colour == 0){
            red[marker] = 1;
        }else if(colour == 1){
            black[marker] = 1;
        }
    }

   public void removeMarker(int colour, int marker) {
        if(colour == 0){
            red[marker] = 0;
        }else if(colour == 1){
            black[marker] = 0;
        }
   }
   public int getFood(){
       return food;
   }
   
   public void setFood(int x){
       food = x;
   }
   
   
}

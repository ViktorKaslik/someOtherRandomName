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
public class Setup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        int[][] multi = new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1},
            {1, 4, 4, 4, 2, 2, 2, 2, 2, 4, 4, 1},
            {1, 4, 4, 2, 2, 2, 2, 2, 2, 4, 4, 1},
            {1, 4, 4, 2, 2, 2, 2, 2, 2, 2, 4, 1},
            {1, 4, 2, 2, 2, 2, 2, 2, 2, 2, 4, 1},
            {1, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 4, 2, 2, 2, 2, 2, 2, 2, 2, 4, 1},
            {1, 4, 4, 2, 2, 2, 2, 2, 2, 2, 4, 1},
            {1, 4, 4, 2, 2, 2, 2, 2, 2, 4, 4, 1},
            {1, 4, 4, 4, 2, 2, 2, 2, 2, 4, 4, 1},
            {1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1},
            {1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1},
            {1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1},
            {1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1},
            {1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1},
            {1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1},
            {1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1 , 1}
        };
        new GameModel();
        //Board b = new Board(multi,30);
    }
    
    
}

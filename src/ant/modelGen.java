package ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author viktor
 */
//150x150
//perimiter always rocky  #DONE
//2 anthills #DONE
//14 rocks (14 sets of ajoining rocky cells)
//11 food blobs ( a blob is a 5x5 square of food cells, each cell contains 5 food)  #DONE
//all elements can have orientation changed
public class modelGen {
    private int[][] boardModel;
    
    /**
     * is a 2nd constructor for if you want to load a world that 
     * has been produced in a text file
     * @param file 
     */
    public modelGen(File file) throws Exception{
        boardModel = loadWorld(file);
        if(!isValidWorld(boardModel)){//if world is not valid throw error
            throw new Exception("Invalid file");
        }
    }
    
    /**
     * Generates a random map according to set rules
     */
    public modelGen(){
        int x,y;
        x=150;//150
        y=150;//150
        boardModel = new int[y][x];
        for(int i = 0; i<y; i++){
            for(int ii =0; ii<x; ii++){
                if(ii==0 || i==0 || ii==x-1 || i==y-1)
                    boardModel[i][ii]= 1;
                else
                    boardModel[i][ii]= 4;
            }
        }
        
        //System.out.println("1");
        boardModel = antHills(boardModel,7);
        boardModel = food(boardModel);
        boardModel = rocks(boardModel);
        
        Ant a = new Ant(1,2);
        a.setCoord(7, 7);
        HashMap<Integer,Ant> ants = new HashMap<Integer,Ant>();
        ants.put(1,a);
        
        //Board b = new Board(boardModel,5,ants);//8
    }
    
    /**
     * gets the board model
     * @return boardModel
     */
    public int[][] getBoard(){return boardModel;}
    
    /**
     * places the 2 ant hills
     * @param matrix the 2d int array containing the world
     * @return the 2d int array with the ant hills placed on it
     */
    private int[][] antHills(int[][] matrix, int de){
        //top left must be min 2 away from L edge
        //top left must be min 7 away from R edge
        //top must be must 9 from bottom
        int width = matrix[1].length;
        int height = matrix.length;
        int antTLHor = antTLHor = randInt(4,width-9); //generates the horizontal index for anthill
        int antTLVer = randInt(2,height-11); //generates the vertical index for anthill
                
        //4-2 = 2 ant hills
        for(int col=2;col<4;col++){
            if(col == 3){
                do{
                    antTLHor = randInt(3,width-8); //generates the horizontal index for anthill
                    antTLVer = randInt(1,height-10); //generates the vertical index for anthill
                }while(!antIsClear(antTLHor,antTLVer,matrix,de));
            }

            for(int hor =antTLHor; hor < antTLHor+7; hor++){
                matrix[antTLVer][hor] = col;}
            int i=0;
            int[] HL = new int[]{antTLHor,antTLVer};
            while(i<de-1){
                HL = adjacent_cell(HL[0],HL[1],2);
                for(int hor = HL[0]; hor < HL[0]+8+i; hor++){
                    matrix[antTLVer+(i+1)][hor] = col;
                }
                i+=1;
            }
            i=0;
            while(i<de-1){
                HL = adjacent_cell(HL[0],HL[1],1);
                for(int hor = HL[0]; hor < HL[0]+(12-i); hor++){
                    matrix[antTLVer+(i+7)][hor] = col;
                }
                i+=1;
            }
        }

        return matrix;
    }
    
    /**
     * calculates the index of the adjacent cells
     * @param x co-ordinate 
     * @param y co-ordinate
     * @param dir direction of adjacent tile
     * @return an int array in the form [x,y]
     */
    public int[] adjacent_cell(int x, int y, int dir){ 
        
        if(y%2 == 0){
            if(dir == 0){ x=x+1; }
            if(dir == 1){ x=x; y=y+1; }
            if(dir == 2){ x=x-1; y=y+1; }
            if(dir == 3){ x=x-1; }
            if(dir == 4){ x=x-1; y=y-1; }
            if(dir == 5){ x=x; y=y-1; }
        }else{
            if(dir == 0){ x=x+1; }
            if(dir == 1){ x=x+1; y=y+1; }
            if(dir == 2){ x=x; y=y+1; }
            if(dir == 3){ x=x-1; }
            if(dir == 4){ x=x; y=y-1; }
            if(dir == 5){ x=x+1; y=y-1; }
        }
        
        return new int[]{x,y};
    }
    
    /**
     * checks that the board is clear for an anthill to be placed
     * @param x co-ordinate of top left
     * @param y co-ordinate of top left
     * @param matrix the 2d int array containing the world
     * @return boolean true if clear, false otherwise
     */
    private boolean antIsClear(int x, int y, int[][] matrix, int de){
        //increase to 7x7
        int w = boardModel[1].length;
        int h = boardModel.length;
        boolean pass= true;
        if(x-8 < 0 || x+8 > w){pass =false;}
        if(y-8 < 0 || y+8 > h){pass =false;}
        for(int yy=(y-2); yy<(y+((de*2)+4)); yy++){
            for(int xx = x-4; xx<x+(de+5); xx++){
                if(xx < 0 || xx > w-1 || yy < 0 || yy > h-1){pass =false;}
                //if(yy < 0 || yy > w){pass =false;}
                else if(matrix[yy][xx] == 2){
                    pass = false;
                }
            }
        }
        
        return pass;
    }
    
    /**
     * places 11 5x5 blocks of food randomly on the map where each cell contains 5 food
     * @param matrix the 2d int array containing the world
     * @return the 2d int array containing the world with the food added
     */
    private int[][] food(int[][] matrix){
        //11 blobs
        //5x5 block
        //food id=9
        int width = matrix[1].length;
        int height = matrix.length;
        int xIn = randInt(1,width-1);
        int yIn = randInt(1,height-1);
        
        for(int i=0; i<11; i++){
            while(foodIsClear(xIn,yIn,matrix) == false){
                xIn = randInt(1,width-1);
                yIn = randInt(1,height-1);
            }
            
            for(int yy=(yIn); yy<(yIn+5); yy++)
                for(int xx = xIn; xx<(xIn+5); xx++)
                    matrix[yy][xx] = 9;
        }
        
        return matrix;
    }
    
    /**
     * checks that the point on the map is clear, and can have food placed there
     * @param x co-ordinate of top left
     * @param y co-ordinate of top left
     * @param matrix the 2d int array containing the world
     * @return true if clear, false otherwise
     */
     private boolean foodIsClear(int x, int y, int[][] matrix) {///////////////////why is an exception somtimes thrown???
        boolean pass= true;
        int h = matrix.length;
        int w = matrix[1].length;
        if(y+7 > h-7 || y-3 < 2){ return false;}
        if(x+7 > w-7 || x-3 < 2){ return false;}
        try{
            for(int yy=(y-2); yy<(y+6); yy++){
                for(int xx = x-2; xx<(x+6); xx++){
                    if(matrix[yy][xx] != 4){ pass = false; }
                }
            }
        }catch(Exception e){}
        return pass;
    }
    
     /**
     * places 14 rocks randomly on the map
     * @param matrix the 2d int array containing the world
     * @return the 2d int array containing the world with the rocks added
     */
    private int[][] rocks(int[][] matrix){// This function is what throws the array out of bounds exception (add in fix i.e. if x||y on boundry dont -1 or +2 to the array)
        //rocks can be adjacent to other rocks but atleast 1 free space between them and other cells (bar ground)
        //a rock is a set of adjacent rock cells
        //if rock is adjacent to another rock dont increment/decrement rock counter
        int size = 4;
        int width = matrix[1].length;
        int height = matrix.length;
        int xIn = randInt(1,width-1);
        int yIn = randInt(1,height-1);
        int i=0;
        while(i < 14){
            //size = randInt(2,10);
            i++;
            do{
                xIn = randInt(1,width-1);
                yIn = randInt(1,height-1);
            }while(rockIsClear(xIn,yIn, matrix,size) == false);
            matrix[yIn][xIn]=1;
            /*if(!rockAdjacentToRock(xIn,yIn,size,matrix)){i++;} //Fix the rock over rock issue (sometimes a larger rock is placed directly over a smaller rock)
            //i+=1;
            //System.out.println(yIn + " "+xIn + " "+i);
            //size = randInt(4,15);
            for(int x=xIn; x<xIn+size; x++){
                for(int y=yIn; y<yIn+size; y++){
                    matrix[y][x]=1;
                }
            }*/
            //matrix[yIn][xIn] = 1;
        }
        
        return matrix;
    }
    
    /**
     * checks that the point on the map is clear, and can have rocks placed there
     * @param x co-ordinate of top left
     * @param y co-ordinate of top left
     * @param matrix the 2d int array containing the world
     * @return true if clear, false otherwise
     */
    private boolean rockIsClear(int x, int y, int[][] matrix,int size) {///////////////////why is an exception somtimes thrown???
        boolean pass= true;
        int h = matrix.length;
        int w = matrix[1].length;
        /*if(y+size+2 > h || y < 3){ return false;}
        if(x+size+2 > w || x < 3){ return false;}
        if(matrix[y][x] != 4){ return false;}
        
        for(int xx = x-3; xx<(x+size+5); xx++){
            for(int yy = y-3; yy<(y+size+5); yy++){
            //System.out.println(" "+y+" "+xx);
                if(yy >=h || xx>=w){pass=false; break;}
                if(matrix[yy][xx] == 2){ pass = false; }//red anthill
                if(matrix[yy][xx] == 3){ pass = false; }//black anthill
                if(matrix[yy][xx] > 4){ pass = false; } //food
                if(xx == x && yy == y){
                    if(matrix[yy][xx] == 1){pass=false;}
                }
            }
        }*/
        for(int xx = x-1; xx<=(x+1); xx++){
            for(int yy = y-1; yy<=(y+1); yy++){
                if(yy >=h || xx>=w){pass=false; break;}
                if(matrix[yy][xx] == 2){ pass = false; }//red anthill
                if(matrix[yy][xx] == 3){ pass = false; }//black anthill
                if(matrix[yy][xx] > 4){ pass = false; } //food
                
            }
        }
        return pass;
    }
    
    private boolean rockAdjacentToRock(int xIn, int yIn, int size, int[][] matrix){
        boolean pass= false;
        int h = matrix.length;
        int w = matrix[1].length;
        int[] nextTo = new int[]{0,0};
        for(int y = yIn; y<yIn+size; y++){
            for(int x = xIn; x<xIn+size; x++){
                /*for(int i =0; i<6; i++){
                    nextTo = adjacent_cell(xIn, yIn, i);
                    if(matrix[nextTo[0]][nextTo[1]] == 1){pass = true;}
                }*/
                /***************check whats sides are on x and y*/
                if(y== yIn){
                    nextTo = adjacent_cell(x, y, 4);
                    if(matrix[nextTo[0]][nextTo[1]] == 1){pass = true;}
                    nextTo = adjacent_cell(x, y, 5);
                    if(matrix[nextTo[0]][nextTo[1]] == 1){pass = true;}                }
                if(x == xIn){
                    nextTo = adjacent_cell(x, y, 2);
                    if(matrix[nextTo[0]][nextTo[1]] == 1){pass = true;}
                    nextTo = adjacent_cell(x, y,3);
                    if(matrix[nextTo[0]][nextTo[1]] == 1){pass = true;}
                }
                if(x==xIn+size){
                    nextTo = adjacent_cell(x, y, 0);
                    if(matrix[nextTo[0]][nextTo[1]] == 1){pass = true;}
                    nextTo = adjacent_cell(x, y, 1);
                    if(matrix[nextTo[0]][nextTo[1]] == 1){pass = true;}
                }
                if(y == yIn+size){
                    nextTo = adjacent_cell(x, y,1);
                    if(matrix[nextTo[0]][nextTo[1]] == 1){pass = true;}
                    nextTo = adjacent_cell(x, y,2);
                    if(matrix[nextTo[0]][nextTo[1]] == 1){pass = true;}
                }
            }
        }
        
        return pass;
    }
    
      private boolean isValidRock(int x, int y, int[][] matrix){
        boolean pass = true;  
            for(int i=0; i<6; i++){
                int[] adCell = adjacent_cell(x,y,i);
                if(matrix[adCell[1]][adCell[0]] != 1 || !(matrix[adCell[1]][adCell[0]] >= 4)){
                    pass = false;
                }/*
                if(matrix[adCell[1]][adCell[0]] >= 4){
                pass = true;
                }*/
            }
        return pass;
    }
    
    /////////////////////////////////////////////////////////////////////////////rock functions need redesigning ^^^^
    
    /**
     * produces a random int in range (min-max)
     * @param min value of int
     * @param max value of int
     * @return a random int in range (min,max)
     */
    private static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    /**
     * checks that world is valid
     * @param matrix 2D int array of world
     * @return true if valid else false
     */
    public boolean isValidWorld(int[][] matrix){
        boolean isValid = false;
        isValid = isBoarderValid(matrix);
        if(!isValid){return false;}
        isValid = isAntHillValid(matrix);
        if(!isValid){return false;}
        //count rocks
        
        return isValid;
    }
    
    /**
     * checks that the boarder of the map is made of rocks
     * @param matrix 2D int array of world
     * @return true if valid else false
     */
    private boolean isBoarderValid(int[][] matrix) {
        boolean pass = true;
        int height = matrix.length;
        int width = matrix[0].length;
        for(int y =0; y<height; y++){
            if(matrix[y][0] != 1 && matrix[y][width] != 1){
                return false;
            }
        }
        for(int x =0; x<height; x++){
            if(matrix[0][x] != 1 && matrix[height][x] != 1){
                return false;
            }
        }
        return pass;
    }
    
    /**
     * checks that there are 2 ant hills and that both are of correct size
     * @param matrix 2D int array of world
     * @return true if valid else false
     */
    public boolean isAntHillValid(int[][] matrix){
        int height = matrix.length;
        int width = matrix[0].length;
        int antHill=0;
        int dirt=0;
        int food = 0;
        int rock = 0;
        for(int y=1; y<height-1;y++){//1-->height-1 to remove borders
            for(int x=1; x<width-1; x++){
                if(matrix[y][x] == 2 || matrix[y][x] == 3){//is ant hill
                    antHill += 1;
                }
                if(isDirtAdjacentToHill(x,y,matrix)){//is dirt or food on dirt
                    dirt += 1;
                }
                if(matrix[y][x] > 5){//is a cell of 5 food
                    food = food + (matrix[y][x]-4);
                }
                if(matrix[y][x] == 1){
                    rock = rock+1;
                }
            }
        }
        System.out.println(rock);
        return (antHill == 254 && dirt == 338 && food == 1375  && rock == 14); //each ant hill should consist of 127 tiles
                                               //dirt surrounding an ant hill should = 42
    }
    
    /**
     * checks if a dirt cell is adjacent to the ant hills
     * @param x
     * @param y
     * @param matrix 2D int array of world
     * @return true if valid else false
     */
    private boolean isDirtAdjacentToHill(int x, int y, int[][] matrix){
        
        for(int dir=0; dir<6; dir++){
            int[] temp = adjacent_cell(x,y,dir);
            if(temp[1] < matrix.length-1 && temp[0] < matrix[0].length-1 && temp[1] > 0 && temp[0] > 0)
                if(matrix[temp[1]][temp[0]] == 2 || matrix[temp[1]][temp[0]] == 3){
                    return true;
                }
        }
        return false;
    }
    
    

     
    /**
     * loads and converts world files to be used in game
     * @param file
     * @return 2D int array of world
     */
    public int[][] loadWorld(File file){
        List<String> lines = new LinkedList<String>(); // create a new list

        try(BufferedReader in = new BufferedReader(new FileReader(file))){

            String line = in.readLine(); // read a line at a time
            while(line != null){ // loop till you have no more lines
                lines.add(line); // add the line to your list
                line = in.readLine(); // try to read another line
            }

        }catch(IOException e){
            System.out.println(e);
        }
        int x = Integer.parseInt(lines.get(0));//load x
        int y = Integer.parseInt(lines.get(1)); //load y
        int[][] matrix = new int[y][x]; //create empty 2D matrix
        
        for(int yy = 2; yy<lines.size(); yy++){
            String[] li = lines.get(yy).split(" ");
            for (int xx = 0; xx<li.length; xx++){
                if(li[xx].equals('#')){
                    matrix[yy-2][xx] = 1;
                }else if(li[xx].equals('.')){
                    matrix[yy-2][xx] = 4;
                }else if(li[xx].equals('+')){
                    matrix[yy-2][xx] = 2;
                }else if(li[xx].equals('-')){
                    matrix[yy-2][xx] = 3;
                }else if(li[xx].matches("\\d+")){//if is an int
                    matrix[yy-2][xx] = Integer.parseInt(li[xx])+4;
                }
            }
        }
        
        return matrix;
    }
    

    /**
     * prints the matrix out (used for debugging)
     * @param A 2D int array of world
     */
    private void print(int[][] A){
        int h=A.length;
        int w=A[1].length;
        
        for(int i = 0; i<h; i++){
            for(int ii =0; ii<w; ii++){
                System.out.print(A[i][ii]);
            }
            System.out.print("\n");
        }
    }

   
  

    
}

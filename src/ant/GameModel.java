/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

import java.io.File;
import static java.lang.Math.abs;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.Spring.height;
import static javax.swing.Spring.width;

/**
 *
 * @author vikto
 */
public class GameModel {
    private int[][] boardModel;
    private Marker[][] markerModel;
    private Board boardGui;
    private int tileSize;
    private HashMap<Integer,Ant> ants;
    private Brains brains;
    private int round;
    
    public GameModel(){
        tileSize = 5;
        round = 300000;
        ants = new HashMap<Integer,Ant>();
        modelGen boardGen = new modelGen();
        boardModel = boardGen.getBoard();
        antSetup();
        boardGui = new Board(boardModel,tileSize,ants); //draw board
        senseSetup();
        //refresh();
    }
    
    /**
     * loops through board left to right, top to bottom creating an ant when ever an anthill 
     * cell is located. The ants are given their ID's in the order in which they are created
     */
    public void antSetup(){
        //loop through board model L-->R T-->B
        int height = boardModel.length;
        int width = boardModel[1].length;
        int count = 0;
        for(int y = 0; y<height; y++){
            for(int x =0; x<width;x++){
                int cellVal = boardModel[y][x];
                
                if(cellVal == 2 || cellVal == 3){ //if cell = redAntHill
                    Ant tempAnt = new Ant(count,cellVal-1);
                    tempAnt.setCoord(x, y);
                    ants.put(count,tempAnt); //add  new ant to hashmap
                    count++;
                }
                
            }
        }
    }
    
    /**
     * Creates a MarkerBoard where each cell on the board corrosponds to a cell on the marker board
     */
    public void senseSetup(){
        int height = boardModel.length;
        int width = boardModel[1].length;
        markerModel = new Marker[height][width];
        for(int y = 0; y<height; y++){
            for(int x =0; x<width;x++){
                markerModel[y][x] = new Marker();
            }
        }
    }
    
    public void loadBrains(File redF, File blackF){
        try {
            brains = new Brains(redF,blackF);
        } catch (Exception ex) {
            Logger.getLogger(GameModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void playRounds(){
        while(round !=0){
            if(round % 50 == 0){
                refresh();
            }
            for(int i = 0; i<ants.size(); i++){
                //Ant ant = ants.get(i);
                step(i);
            }
            round--;
        }
        int winner = getWinner();
    }
    
    public int getWinner(){
        int red=0;
        int black = 0;
        int height = boardModel.length;
        int width = boardModel[1].length;
        
        for(int y = 0; y<height; y++){
            for(int x =0; x<width;x++){
                if(boardModel[y][x] == 2){
                    red = red+ markerModel[y][x].getFood();
                }
                if(boardModel[y][x] == 3){
                    black = black + markerModel[y][x].getFood();
                }
            }
        }
        if(red>black)
            return 0;
        else if(black > red)
            return 1;
        else
        return -1;
    }
    
    /**
     * calculates the index of the adjacent cells
     * @param x co-ordinate 
     * @param y co-ordinate
     * @param dir direction of adjacent tile
     * @return an int array in the form [x,y]
     */
    public int[] adjacent_cell(int x, int y, int dir){ // may make static???
        
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
     * gets the state of an ant
     * @param ant the Ant in which you want the state off
     * @return int, the ants state
     */
    public int State(Ant ant){return ant.getState();}
    
    public int colour(Ant ant){return ant.getColor();}
    
    public int resting(Ant ant){return ant.getResting();}
    
    public int direction(Ant ant){return ant.getDirection();}
    
    public boolean has_Food(Ant ant){return ant.HasFood();}
    
    public void set_state(Ant ant,int state){ant.setState(state);}
    
    public void set_resting(Ant ant, int rest){ant.setResting(rest);}
    
    public void set_direction(Ant ant, int dir){ ant.setDirection(dir);}
    
    public void set_has_food(Ant ant, boolean bool){ ant.setHasFood(bool);}
    
    /**
     * Changes the ants direction 
     * @param ant Ant to which you want to alter
     * @param lr the direction you want to turn ('l' or 'r')
     */
    public void turn(Ant ant, char lr){
        int dir = direction(ant);
        int newDir;
        if(lr == 'r'){ newDir = (dir+1)%6;}
        else{ newDir = (dir+5)%6; }
        ant.setDirection(newDir);
    }
    
    /**
     * gets the number of food particles at cell x,y
     * @param x coordinate 
     * @param y coordinate
     * @return number of food particles at specified cell
     */
    public int food_at(int x, int y){
        //tile 5 = 1 food 
        return boardModel[y][x]-4;
    }
    
    /**
     * sets the number of food particles at cell x,y
     * @param x coordinate
     * @param y coordinate
     * @param food the number of food particles 
     */
    public void set_food_at(int x, int y, int food){
        if(boardModel[y][x] == 2 && boardModel[y][x] == 3){
            boardModel[y][x] = food+4;
        }
        markerModel[y][x].setFood(food);
    }
    
    /**
     * returns true if cell at coordinates contains the anthill that match's the colour 
     * @param x coordinate
     * @param y coordinate
     * @param colour of ant hill
     * @return true if colour == anthill
     */
    public boolean anthill_at(int x, int y, int colour){
        return (boardModel[y][x] == colour);
    }
    
    /**
     * returns the number of ants of colour color surrounding the cell at coordinate x,y
     * @param x coordinate
     * @param y coordinate
     * @param color to count
     * @return The number of ants around the coordinates
     */
    public int adjacent_ants(int x, int y, int color){
        int inc = 0;
        for(int dir=0; dir<6; dir++){
            int[] cell = adjacent_cell(x,y,dir); // cell = [x,y]
            Ant AjAnt = isAntAt(cell[0],cell[1]);
            if(AjAnt != null){
                if(AjAnt.getColor() == color)
                    inc += 1;
            }
        }
        return inc;
    }
    
    /**
     * checks if an ant is at the coordinates x,y
     * @param x coordinate 
     * @param y coordinate
     * @return true if ant is present false otherwise
     */
    public Ant isAntAt(int x, int y){
        Ant ant;
        for (Map.Entry<Integer, Ant> entry : ants.entrySet())
        {
            ant = entry.getValue();
            int[] co = ant.getCoord();
            if(co[0] == x && co[1] == y){return ant;}
            
        }
        return null;
    }
    
    /**
     * checks if there is an ant of either colour in cell x,y
     * @param x coordinate
     * @param y coordinate
     * @return true if ant is present false otherwise
     */
    public boolean some_ant_is_at(int x, int y){
        return isAntAt(x,y) != null;
    }

    /**
     * finds the coordinate that the ant with id is in
     * @param id of Ant
     * @return coordinate of ant
     */   
    public int[] find_ant(int id){
        Ant ant = ants.get(id);
        return ant.getCoord();
    }
    
    /**
     * checks if cell is rocky 
     * @param x coordinate
     * @param y coordinate
     * @return true if cell is rocky false otherwise
     */
    public boolean rocky(int x, int y){
        return boardModel[y][x] == 1;
    }
    
    /**
     * checks if ant is alive
     * @param id of ant
     * @return true if ant is alive, false otherwise
     */
    public boolean ant_is_alive(int id){
        Ant ant = ants.get(id);
        return ant != null;
    }
    
    /**
     * checks if ant is surrounded, if it is then ant dies
     * @param ant to check
     */
    public boolean check_for_surround_ant_at(Ant ant){
        int[] coord = ant.getCoord();
        if(adjacent_ants(coord[0],coord[1], abs(ant.getColor()-1)) >=5){
            kill_ant(ant);
            return true;
        }
        return false;
    }
    
    /**
     * kills the ant passed to it
     * @param a Ant to be killed
     */
    public void kill_ant(Ant a){
        int[] coord = a.getCoord();
        ants.put(a.getId(), null); //kill ant
        set_food_at(coord[0],coord[1],food_at(coord[0],coord[1])+3); //adds 3 to food at coords
    }
    
    /**
     * makes ants move
     * @param id of ant to move
     */
    public void step(int id){
        if(ant_is_alive(id)){
            Ant ant = ants.get(id);
            if(!check_for_surround_ant_at(ant)){
                if(ant.getResting() > 0){
                    ant.setResting(ant.getResting()-1);
                }
                else{
                    //getInstruction
                    get_instruction(ant);
                }
            }
        }
    }
    
    /**
     * set ants coordinate's as x,y
     * @param x coordinate
     * @param y coordinate
     * @param ant  ant to be updated
     */
    public void set_ant_at(int x, int y ,Ant ant){
        ant.setCoord(x, y);
    }
    
    public void set_marker_at(int x, int y, int color, int marker){
        markerModel[y][x].placeMarker(color, marker);
    }
    
    public void clear_marker_at(int x, int y, int color, int marker){
        markerModel[y][x].removeMarker(color, marker);
    }
    
    public boolean check_marker_at(int x, int y, int color, int marker){
        if(markerModel[y][x].getMarker(color, marker) > 0)
            return true;
        return false;
    }
    
    public boolean check_any_marker_at(int x, int y, int color)
    {
        for(int i=0; i<5; i++){
            if(markerModel[y][x].getMarker(color, i) > 0)
                return true;
        }
        return false;
    }
    public void sense(Ant ant, int dir, int st1, int st2, int cond){
        int[] coord = ant.getCoord();
        if(dir == 0){// here
            coord = ant.getCoord();
        }else if(dir == 1){ //ahead
            coord = adjacent_cell(coord[0], coord[1],ant.getDirection());
        }else if(dir == 2){ //leftahead           
            int temp = (ant.getDirection()+5)%6; 
            coord = adjacent_cell(coord[0], coord[1],temp);
        }else if (dir == 3){//rightahead
            int temp = (ant.getDirection()+1)%6; 
            coord = adjacent_cell(coord[0], coord[1],temp);
        }
        /*conditions*/
        if(cond==0){//is ant friend
            Ant antAt = isAntAt(coord[0],coord[1]);
            if(antAt.getColor() == ant.getColor()){
                ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }else if(cond==1){//is ant foe
            Ant antAt = isAntAt(coord[0],coord[1]);
            if(antAt.getColor() != ant.getColor()){
                ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }else if(cond==2){//is ant friend with food
            Ant antAt = isAntAt(coord[0],coord[1]);
            if(antAt.getColor() == ant.getColor() && ant.HasFood() == true){
                ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }else if(cond==3){//is ant foe with food
            Ant antAt = isAntAt(coord[0],coord[1]);
            if(antAt.getColor() != ant.getColor() && ant.HasFood() == true){
                ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }else if(cond==4){//is food
            if(food_at(coord[0],coord[1]) > 0){
                 ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }else if(cond==5){//is rocky
            if(rocky(coord[0],coord[1]) == true){
                 ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }else if(cond >= 6 && cond <= 11){//is friend marker
            if(check_marker_at(coord[0],coord[1],ant.getColor(),cond-6)){
                ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }else if(cond == 12){//is foe marker
            //abs(ant.getColor()-1) flips the ants colour
            if(check_any_marker_at(coord[0],coord[1],abs(ant.getColor()-1))){
                ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }else if(cond == 13){//is home
            if(anthill_at(coord[0],coord[1],ant.getColor())){
                ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }else if(cond == 14){//is foe home
            if(anthill_at(coord[0],coord[1],abs(ant.getColor()-1))){
                ant.setState(st1);
            }else{
                ant.setState(st2);
            }
        }
    }
    
    public void move(Ant ant){
        int[] coord = ant.getCoord();
        int[] cellCoord = adjacent_cell(coord[0],coord[1],ant.getDirection());
        if(rocky(cellCoord[0],cellCoord[1]) == false){
            ant.setCoord(cellCoord[0],cellCoord[1]);
            ant.setResting(14);
        }
    }
    
    public int flip(int p){
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(p);
        
    }
    
    public void get_instruction(Ant ant){
        String[] instruction = brains.getInstruction(ant.getState(), ant.getColor());
        //process instruction and call relevent instructions
        //maybe convert string into int code on parsing
        //ant.setResting(ant.getResting()-1);
        switch(instruction[0]){
                case("sense"):
                    //Sense(dir, st1,st2,cond)
                    int dir=0;
                    if(instruction[1].equals("here")){
                        dir=0;
                    }else if(instruction[1].equals("ahead")){
                        dir=1;
                    }else if(instruction[1].equals("leftahead")){
                        dir=2;
                    }else if(instruction[1].equals("rightahead")){
                        dir=3;
                    }
                    
                    int st1 = Integer.parseInt(instruction[2]);
                    int st2 = Integer.parseInt(instruction[3]);
                    
                    int cond=0;
                    if(instruction[4].equals("friend")){
                        cond = 0;
                    }else if(instruction[4].equals("foe")){
                        cond = 1;
                    }else if(instruction[4].equals("friendwithfood")){
                        cond = 2;
                    }else if(instruction[4].equals("foewithfood")){
                        cond = 3;
                    }else if( instruction[4].equals("food")){
                        cond = 4;
                    }else if( instruction[4].equals("rock")){
                        cond = 5;
                    }else if(instruction[4].equals("marker_0")){
                        cond = 6;
                    }else if(instruction[4].equals("marker_1")){ 
                        cond = 7;
                    }else if(instruction[4].equals("marker_2")){
                        cond = 8;
                    }else if( instruction[4].equals("marker_3")){
                        cond = 9;
                    }else if( instruction[4].equals("marker_4")){
                        cond = 10;
                    }else if( instruction[4].equals("marker_5")){
                        cond = 11;
                    }else if(instruction[4].equals("foemarker")){
                        cond = 12;
                    }else if(instruction[4].equals("home")){
                        cond = 13;
                    }else if(instruction[4].equals("foehome")){
                        cond = 14;
                    }
                    sense(ant,dir,st1,st2,cond);
                    break;
                case("mark"):
                    //Mark(i,st)
                    int i = Integer.parseInt(instruction[1]);
                    int st = Integer.parseInt(instruction[2]);
                    int[] coord = ant.getCoord();
                    set_marker_at(coord[0],coord[1],ant.getColor(),i);
                    ant.setState(st);
                    break;
                case("unmark"): // using case fall through as syntax is identicle
                    //Unmark(i,st)
                    int ii = Integer.parseInt(instruction[1]);
                    int stt = Integer.parseInt(instruction[2]);
                    int[] coord1 = ant.getCoord();
                    clear_marker_at(coord1[0],coord1[1],ant.getColor(),ii);
                    ant.setState(stt);
                    break;
                case("pickup"):
                    //PickUp(st1,st2)
                    int[] co = ant.getCoord();
                    if(food_at(co[0],co[1])>0){
                        if(!ant.HasFood()){
                            set_food_at(co[0],co[1],food_at(co[0],co[1])-1);
                            ant.setHasFood(true);
                        }
                        ant.setState(Integer.parseInt(instruction[2]));
                    }else{ant.setState(Integer.parseInt(instruction[2]));}
                case("move"): 
                    //Move(st1,st2)
                    if(ant.getResting() == 0){
                        move(ant);
                        ant.setState(Integer.parseInt(instruction[1]));
                    }else{ant.setState(Integer.parseInt(instruction[2]));}
                    
                    break;
                
                case("drop"):
                    //Drop(st)
                    int[] coo = ant.getCoord();
                    if(ant.HasFood()){
                        set_food_at(coo[0],coo[1],food_at(coo[0],coo[1])+1);
                        ant.setHasFood(false);
                    }
                    ant.setState(Integer.parseInt(instruction[1]));
                    break;
                case("turn"):
                    //Turn(lr,st)
                    char lr;
                    if(instruction[1].equals("left")){lr = 'l';}
                    else{lr='r';}
                    turn(ant,lr);
                    ant.setState(Integer.parseInt(instruction[1]));
                    break;
                case("flip"):
                    //Flip(p,st1,st2)
                    int rand = flip(Integer.parseInt(instruction[1]));
                    if(rand == 0){ant.setState(Integer.parseInt(instruction[2]));
                    }else{ant.setState(Integer.parseInt(instruction[1]));}
                    break;
               
        }
    }
    /*
        sensed_cell(p:pos, d:dir, sd:sense_dir):pos
        
        cell_matches(pos,con,color):bool
        clear_ant_at(pos):void //dont belive this is required
        
    */


    /**
     * Updates and refreshes the board (GUI)
     */
    public void refresh(){
        boardGui.refresh(boardModel, tileSize, ants);
    }
}

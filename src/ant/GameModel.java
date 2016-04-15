/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

import static java.lang.Math.abs;
import java.util.HashMap;
import java.util.Map;
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
    
    public GameModel(){
        tileSize = 5;
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
        for(int y = 0; y<height; y++){
            for(int x =0; x<width;x++){
                markerModel[y][x] = new Marker();
            }
        }
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
        boardModel[y][x] = food+4;
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
    public void check_for_surround_ant_at(Ant ant){
        int[] coord = ant.getCoord();
        if(adjacent_ants(coord[0],coord[1], abs(ant.getColor()-1)) >=5){
            kill_ant(ant);
        }
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
            if(ant.getResting() > 0){
                ant.setResting(ant.getResting()-1);
            }
            else{
                //getInstruction
                /*switch get_instruction(color(a), state(a)) of
                    case Sense(sensedir, st1, st2, cond):
                    let p' = sensed_cell(p, direction(a), sensedir) in
                    let st = if cell_matches(p', cond, color(a)) then st1 else st2 in
                    set_state(a, st)
                    case Mark(i, st):
                    set_marker_at(p, color(a), i);
                    set_state(a, st)
                    case Unmark(i, st):
                    clear_marker_at(p, color(a), i);
                    set_state(a, st)
                    case PickUp(st1, st2):
                    if has_food(a) || food_at(p) = 0 then
                    set_state(a, st2)
                    else begin
                    set_food_at(p, food_at(p) - 1);
                    set_has_food(a, true);
                    set_state(a, st1)
                    end
                    case Drop(st):
                    if has_food(a) then begin
                    set_food_at(p, food_at(p) + 1);
                    set_has_food(a, false)
                    end;
                    set_state(a, st)
                    case Turn(lr, st):
                    set_direction(a, turn(lr, direction(a)));
                    set_state(a, st)
                    case Move(st1, st2):
                    let newp = adjacent_cell(p, direction(a)) in
                    if rocky(newp) || some_ant_is_at(newp) then
                    set_state(a, st2)
                    else begin
                    clear_ant_at(p);
                    set_ant_at(newp, a);
                    set_state(a, st1);
                    set_resting(a, 14);
                    check_for_surrounded_ants(newp)
                    end
                    case Flip(n, st1, st2):
                    let st = if randomintno = 0 then st1 else st2 in
                    set_state(a, st)*/
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
    /*
        sensed_cell(p:pos, d:dir, sd:sense_dir):pos
        
        cell_matches(pos,con,color):bool
        clear_ant_at(pos):void //dont belive this is required
        get_instruction(color,state):instruction //need antbrain model 1st
    */


    /**
     * Updates and refreshes the board (GUI)
     */
    public void refresh(){
        boardGui.refresh(boardModel, tileSize, ants);
    }
}

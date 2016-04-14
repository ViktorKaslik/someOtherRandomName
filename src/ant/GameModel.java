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
    private Board boardGui;
    private int tileSize;
    private HashMap<Integer,Ant> ants;
    
    public GameModel(){
        tileSize = 5;
        Ant a = new Ant(1,2);
        a.setCoord(7, 7);
        ants = new HashMap<Integer,Ant>();
        ants.put(1,a);
        
        
        modelGen boardGen = new modelGen();
        boardModel = boardGen.getBoard();
        
        boardGui = new Board(boardModel,tileSize,ants); //draw board
        
        Ant aa = new Ant(1,2);
        aa.setCoord(10, 8);
        ants.put(2,aa);
        refresh();
        
    }
    
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
    
    public int State(Ant ant){return ant.getState();}
    
    public int colour(Ant ant){return ant.getColor();}
    
    public int resting(Ant ant){return ant.getResting();}
    
    public int direction(Ant ant){return ant.getDirection();}
    
    public boolean has_Food(Ant ant){return ant.HasFood();}
    
    public void set_state(Ant ant,int state){ant.setState(state);}
    
    public void set_resting(Ant ant, int rest){ant.setResting(rest);}
    
    public void set_direction(Ant ant, int dir){ ant.setDirection(dir);}
    
    public void set_has_food(Ant ant, boolean bool){ ant.setHasFood(bool);}
    
    public void turn(Ant ant, char lr){
        int dir = direction(ant);
        int newDir;
        if(lr == 'r'){ newDir = (dir+1)%6;}
        else{ newDir = (dir+5)%6; }
        ant.setDirection(newDir);
    }
    
    public int food_at(int x, int y){
        //tile 5 = 1 food 
        return boardModel[y][x]-4;
    }
    
    public void set_food_at(int x, int y, int food){
        boardModel[y][x] = food+4;
    }
    
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
    
    public boolean some_ant_is_at(int x, int y){
        return isAntAt(x,y) != null;
    }

       
    public int[] find_ant(int id){
        Ant ant = ants.get(id);
        return ant.getCoord();
    }
    
    public boolean rocky(int x, int y){
        return boardModel[y][x] == 1;
    }
    
    public boolean ant_is_alive(int id){
        Ant ant = ants.get(id);
        return ant != null;
    }
    
    public void check_for_surround_ant_at(Ant ant){
        int[] coord = ant.getCoord();
        if(adjacent_ants(coord[0],coord[1], abs(ant.getColor()-1)) >=5){
            kill_ant(ant);
        }
    }
    
    public void kill_ant(Ant a){
        int[] coord = a.getCoord();
        ants.put(a.getId(), null); //kill ant
        set_food_at(coord[0],coord[1],food_at(coord[0],coord[1])+3); //adds 3 to food at coords
    }
    
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
    
    /*
        sensed_cell(p:pos, d:dir, sd:sense_dir):pos
        set_ant_at(pos,ant):void
        clear_ant_at(pos):void
        set_marker_at(pos,color,marker):void
        clear_marker_at(pos,colour,marker):void
        check_marker_at(pos, color,marker):void
        check_any_marker_at(pos,color):bool
        cell_matches(pos,con,color):bool
        get_instruction(color,state):instruction
    */


    /**
     * Updates and refreshes the board (GUI)
     */
    public void refresh(){
        boardGui.refresh(boardModel, tileSize, ants);
    }
}

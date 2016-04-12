/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

import static java.lang.Math.abs;
import java.util.HashMap;
import java.util.Map;

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
        //tile 5 =1 food
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
    step(id):void*/


    /**
     * Updates and refreshes the board (GUI)
     */
    public void refresh(){
        boardGui.refresh(boardModel, tileSize, ants);
    }
}

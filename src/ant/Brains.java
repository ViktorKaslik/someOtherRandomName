/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author vikto
 */
public class Brains {
    private String[][] red;
    private String[][] black;
    
    /**
     * checks brain files are well formed and converts them into FSA 
     * @param redF red brain file
     * @param blackF black brain file
     * @throws Exception if either of the files isn't well formed or file cant be read
     */
    public Brains(File redF, File blackF) throws Exception{
        for(int x=0; x<2; x++){
            File file;
            List<String> lines = new LinkedList<String>(); // create a new list
            
            if(x==0){file = redF;}
            else{file = blackF;}

            try(BufferedReader in = new BufferedReader(new FileReader(file))){

                String line = in.readLine(); // read a line at a time
                while(line != null){ // loop till you have no more lines
                    lines.add(line); // add the line to your list
                    line = in.readLine(); // try to read another line
                }

            }catch(IOException e){
                System.out.println(e);
            }
            /*init brain sizes*/
            if(x==0){red = new String[lines.size()][5];} //x_size = 5 as thats 
            else{black = new String[lines.size()][5];} //the size of largest instruction
            
            //pass lines list to function to parse then put into brains
            parseBrain(lines, x);
        }
        
    }
    
    /**
     * checks that file is well formed
     * @param lines list of lines from file 
     * @param ant ant to have the brain
     * @throws Exception 
     */
    private void parseBrain(List<String> lines, int ant) throws Exception{
        
        for(int x=0; x<lines.size(); x++){
            boolean isValid = true;
            //if(isValid == false){throw new Exception();}// break out of parser if failed
            String line = lines.get(x);
            String[] temp = line.split(";");//remove comment (as its unrequired)
            line = temp[0];
            //remove brackets and comma 
            line.replace("(", " ");
            line.replace(")", " ");
            line.replace(",", " ");
            line.replace("marker ","marker_"); // change marker i to marker_i
            /*remove the importance of caps (as they dont make much diff)*/
            line = line.toLowerCase(); 
            String[] words = line.split(" "); //split the instruction into 'parts'
            
            switch(words[0]){
                case("sense"):
                    //Sense(dir, st1,st2,cond)
                    if(words[1].equals("here") || words[1].equals("ahead") || words[1].equals("leftahead") || words[1].equals("rightahead")){
                        if(words[2].matches("\\d+")){ //check if is and int
                            if(words[3].matches("\\d+")){ //check if is and int
                                if(words[4].equals("friend") || words[4].equals("foe") || words[4].equals("friendwithfood") || words[4].equals("foewithfood")
                                    || words[4].equals("food")|| words[4].equals("rock")|| words[4].equals("marker_0") || words[4].equals("marker_1") || 
                                    words[4].equals("marker_2") || words[4].equals("marker_3") || words[4].equals("marker_4") || words[4].equals("marker_5")
                                    || words[4].equals("foemarker") ||  words[4].equals("home") ||  words[4].equals("foehome")){ 
                                    
                                    if(ant == 0){
                                        red[x][0]=words[0];
                                        red[x][1]=words[1];
                                        red[x][2]=words[2];
                                        red[x][3]=words[3];
                                        red[x][4]=words[4];
                                    }else{
                                        black[x][0]=words[0];
                                        black[x][1]=words[1];
                                        black[x][2]=words[2];
                                        black[x][3]=words[3];
                                        black[x][4]=words[4];
                                    }
                                    
                                }else{isValid = false;}
                            }else{isValid = false;}
                        }else{isValid = false;}
                    }else{isValid = false;}
                    break;
                case("mark"):
                case("unmark"): // using case fall through as syntax is identicle
                    //Mark(i,st)
                    if(Integer.parseInt(words[1]) <6 && Integer.parseInt(words[1]) >=0){
                        if(words[2].matches("\\d+")){
                            if(ant == 0){
                                red[x][0]=words[0];
                                red[x][1]=words[1];
                                red[x][2]=words[2];
                            }else{
                                black[x][0]=words[0];
                                black[x][1]=words[1];
                                black[x][2]=words[2];
                            }
                        }else{isValid = false;}
                    }else{isValid = false;}
                    break;
                case("pickup"):
                case("move"): // use fall through
                    //PickUp(st1,st2)
                    //Move(st1,st2)
                    if(words[1].matches("\\d+") && words[2].matches("\\d+")){
                        if(ant == 0){
                            red[x][0]=words[0];
                            red[x][1]=words[1];
                            red[x][2]=words[2];
                        }else{
                            black[x][0]=words[0];
                            black[x][1]=words[1];
                            black[x][2]=words[2];
                        }
                    }else{isValid = false;}
                    break;
                
                case("drop"):
                    //Drop(st)
                    if(words[1].matches("\\d+")){
                        if(ant == 0){
                            red[x][0]=words[0];
                            red[x][1]=words[1];
                        }else{
                            black[x][0]=words[0];
                            black[x][1]=words[1];
                        }
                    }else{isValid = false;}
                    break;
                case("turn"):
                    //Turn(lr,st)
                    if(words[1].equals("left") || words[1].equals("right")){
                        if(words[2].matches("\\d+")){
                            if(ant == 0){
                                red[x][0]=words[0];
                                red[x][1]=words[1];
                                red[x][2]=words[2];
                            }else{
                                black[x][0]=words[0];
                                black[x][1]=words[1];
                                black[x][2]=words[2];
                            }
                        }else{isValid = false;}
                    }else{isValid = false;}
                    break;
                case("flip"):
                    //Flip(p,st1,st2)
                    if(words[1].matches("\\d+")){
                        if(words[2].matches("\\d+") && words[3].matches("\\d+")){
                            if(ant == 0){
                                red[x][0]=words[0];
                                red[x][1]=words[1];
                                red[x][2]=words[2];
                                red[x][3]=words[3];
                            }else{
                                black[x][0]=words[0];
                                black[x][1]=words[1];
                                black[x][2]=words[2];
                                black[x][3]=words[3];
                            }
                        }else{isValid = false;}
                        
                    }else{isValid = false;}
                    break;
                default:
                    isValid = false;
                    
            if(isValid == false){throw new Exception();}// break out of parser if failed
               
            }
        }
    }
    
    /**
     * gets instruction
     * @param state of FSM to get
     * @param colour of ant
     * @return String array containing the instruction
     */
    public String[] getInstruction(int state, int colour){
        System.out.println("size: " + red.length);
        String[] instruction= null;
        if(colour == 0){
            if(state < red.length){instruction = red[state];}
        }else{
            if(state < black.length){instruction = black[state];}
        }
        //System.out.println(colour + instruction[0]);
        return instruction;
    }
    //end of class
}

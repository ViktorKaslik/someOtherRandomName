/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *
 * @author vikto
 */
public class BrainParser {
    public BrainParser(File brainFile){
        
    }
    
    public void readFile(File brainFile){
        try(BufferedReader br = new BufferedReader(new FileReader(brainFile))) {
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                String[] lineTemp = line.split(";",2); //split at comment
                //lineTemp[0]; contains instruction portion
            }
        }catch(Exception e){}
        
    }
}

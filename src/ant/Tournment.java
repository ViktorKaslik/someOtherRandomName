/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

/**
 *
 * @author vikto
 */
public class Tournment {
    final List<File> files;
    JFrame frame;
    JSpinner spinner;
    int numPlayers;
    JPanel p;
    
    
    public Tournment(){
        files = new LinkedList<File>();
    }
    
    public void drawFrame(){
        frame= new JFrame("Ant Game");
        frame.setSize(400,400);
        frame.setLayout(new BorderLayout());
        
        p=getNumPlayers();
        frame.add(p,BorderLayout.CENTER);
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        
    }
    
    public JPanel getNumPlayers(){
        JPanel pan = new JPanel();
        pan.setSize(500,500);
        BorderLayout layout = new BorderLayout();
        layout.setHgap(5);
        layout.setVgap(5);
        pan.setLayout(layout);
        pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        //spinner(initValue,Min,max,step)
        SpinnerNumberModel spM = new SpinnerNumberModel(2, 2, 20, 1);
        spinner = new JSpinner(spM);
        JButton but = new JButton("Submit");
        but.addActionListener(new NumPlayerButton());
        
        pan.add(spinner,BorderLayout.CENTER);
        pan.add(but,BorderLayout.SOUTH);

        return pan;
    }
    
     public JPanel getBrainFiles(){
        JPanel pan = new JPanel();
        pan.setSize(500,500);
        FlowLayout layout = new FlowLayout();
        layout.setHgap(5);
        layout.setVgap(5);
        pan.setLayout(layout);
        pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        
        //JButton but = new JButton("Open File");
        //final JFileChooser  fileDialog = new JFileChooser();
        pan.add(new JLabel("Please upload all the files"));
        
       final List<JFileChooser> filesDig = new LinkedList<JFileChooser>();
       final List<JButton> buttons = new LinkedList<JButton>();
       for(int x=0; x<numPlayers; x++){
            JFileChooser fileDialog = new JFileChooser();
            filesDig.add(fileDialog);
            JButton but = new JButton("Open File: "+(x+1));
            final int ii = x; 
            but.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int returnVal = filesDig.get(ii).showOpenDialog(frame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                       files.add(filesDig.get(ii).getSelectedFile());
                       String temp = e.getActionCommand();
                       temp = temp.replaceAll("[^\\d]", "");
                       buttons.get(Integer.parseInt(temp)-1).setText("Uploaded");
                    }     
                    if(files.size()==numPlayers){
                        frame.remove(p);
                        //p=;////////////////////;
                        frame.add(p,BorderLayout.CENTER);
                        frame.pack();
                    }
                }
            });
           //pan.add(fileDialog,BorderLayout.CENTER);
            buttons.add(but);
            pan.add(buttons.get(x));
        }

        return pan;
    }
    
    public JPanel getWorldFiles(){
        JPanel pan = new JPanel();
        pan.setSize(500,500);
        FlowLayout layout = new FlowLayout();
        layout.setHgap(5);
        layout.setVgap(5);
        pan.setLayout(layout);
        pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
      
        
        pan.add(new JLabel("Would you like load a tournment map"),BorderLayout.NORTH);
        
        
        final JFileChooser  fileDialog = new JFileChooser();
        JButton yesBut = new JButton("Yes");
        yesBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               int returnVal = fileDialog.showOpenDialog(frame);
               if (returnVal == JFileChooser.APPROVE_OPTION) {
                  java.io.File file = fileDialog.getSelectedFile();
                  frame.dispose();
                   try {
                       tournament(file);
                   } catch (Exception ex) {
                       Logger.getLogger(Tournment.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
                    
            }
        });
        
        JButton noBut = new JButton("No");
        noBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                tournament();
            }
        });
        pan.add(yesBut,BorderLayout.WEST);
        pan.add(noBut,BorderLayout.EAST);
        return pan;
    }
     
    public void tournament(File board) throws Exception{
        int[] score = new int[numPlayers];
        for(int x=0;x<numPlayers;x++){
            score[x]=0;
        }
        for(int red=0; red<numPlayers; red++){
            File redFile= files.get(red);
            for(int black=0; black<numPlayers; black++){
                if(red != black){ //if files arent the same
                    File blackFile= files.get(black);
                    GameModel gm = new GameModel(redFile, blackFile, board);
                    int winner = gm.getWinner();
                    if(winner ==0){
                        score[red]=score[red]+1;
                    }else{
                        score[black]=score[black]+1;
                    }
                }
            }
        }
        
        scores(score);
        
    }
    
    public void tournament(){
         int[] score = new int[numPlayers];
        for(int x=0;x<numPlayers;x++){
            score[x]=0;
        }
        for(int red=0; red<numPlayers; red++){
            File redFile= files.get(red);
            for(int black=0; black<numPlayers; black++){
                if(red != black){ //if files arent the same
                    File blackFile= files.get(black);
                    GameModel gm = new GameModel(redFile, blackFile);
                    int winner = gm.getWinner();
                    if(winner ==0){
                        score[red]=score[red]+1;
                    }else{
                        score[black]=score[black]+1;
                    }
                }
            }
        }
        
        scores(score);
    }
     
    public void scores(int[] score){
        frame= new JFrame("Scores");
        frame.setSize(400,400);
        frame.setLayout(new BorderLayout());
        
        p=new JPanel();
        for(int x=0;x<score.length;x++){
            String temp = "Player_" + (x+1) +": ";
            p.add(new JLabel(temp + score[x]));
        }
        frame.add(p,BorderLayout.CENTER);
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }
    
    class NumPlayerButton implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            numPlayers = (int) spinner.getValue();
            frame.remove(p);
            p=getBrainFiles();
            frame.add(p,BorderLayout.CENTER);
            frame.pack();
        } 
    }
}

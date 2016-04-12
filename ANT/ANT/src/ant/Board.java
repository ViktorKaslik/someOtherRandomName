/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author vikto
 */
class Board {
    private JFrame frame;
    private JPanel board;
    private BufferedImage img;
    private ImageIcon[] imgA;
    private ImageIcon[] antsIm;
    
    int height;
    int width;
    int imgW;
    double ratio;
    int imgH;
    int buff;
    int buffer;
    /**
     * Draws the board 
     * @param A a 2d int array representing the world
     * @param size size of tiles (1>= size <= 130)
     */
    public Board(int[][] A, int size, HashMap<Integer,Ant> ants) {
        if(size<1){size=1;}
        if(size>130){size = 130;}
        imgA = new ImageIcon[14];
        antsIm = new ImageIcon[5];
        readImg(size);
        //readAntImg(size);
        frame= new JFrame("Ant Game");
        
        height = A.length;
        width = A[1].length;
        imgW = size; 
        ratio = (44.0/60);
        imgH = (int) Math.ceil(ratio*size);
        buff= size/2;
        
        board = buildBoard(A, size, ants);
        
        //drawAnt(ant);
        
        frame.add(board);
        //frame.add(drawAnt(ant));
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        
    }
    
    public void refresh(int[][] A, int size, HashMap<Integer,Ant> ants){
        board = buildBoard(A, size, ants);
        frame.add(board);
        frame.pack();
    }
        
    /**
     * calculates the placing of tiles to create the board
     * @param boardModel a 2d int array that refers to the boards design 
     * @param size is an int that refers to the tile size
     * @return JPanel that contains the board
     */
    public JPanel buildBoard(int[][] boardModel, int size, HashMap<Integer,Ant> ants){
        
        JPanel pan = new JPanel();
        pan.setLayout(null); 
        
        Insets insets = pan.getInsets();
        for(int y = 0; y<height; y++){
            int[] temp = boardModel[y];
            if(y%2 != 0)
                buffer = buff;
            else
                buffer = 0;
            for(int x =0; x<width;x++){
                int w =(x*imgW)+buffer;
                int h =(y*imgH); //0*h?????????
                JLabel b1;
                Ant an = isAntAt(x,y,ants);
                int tile = temp[x];
                if(tile >= 13)//if theres more than 9 food place food9 tile
                    tile = 13;
                if(an == null){ b1 = getImg(tile);}
                else{b1 = getAntImg(an.getColor(),0);}
                Dimension dSize = b1.getPreferredSize();
                pan.add(b1);
                b1.setBounds(w + insets.left, h + insets.top, dSize.width, dSize.height);
            }
        }
        
        
        pan.setPreferredSize(new Dimension((width*imgW)+buff,(imgH*height)+(imgH/2)));
        
        return pan;
    }
    
    public Ant isAntAt(int x, int y, HashMap<Integer,Ant> ants){
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
     * builds the tile
     * @param j is the int that refers to a tile image
     * @return the tile
     */
    public JLabel getImg(int j){
        JLabel label=new JLabel();
        
        label = new JLabel(imgA[j]);
        label.setBorder(new EmptyBorder(0,0,0,0));
        return label;
    }
    
    public JLabel getAntImg(int j,int dir){
        JLabel label=new JLabel();
        
        label = new JLabel(imgA[j]);
        label.setBorder(new EmptyBorder(0,0,0,0));
        return label;
    }
    
    /**
     * Reads images into array
     * @param size alters the image sizes to equal size
     */
    public void readImg(int size){
       
        try {
            img = ImageIO.read(Setup.class.getResource("textures.png"));
            //ImageIcon icon = new ImageIcon(img2);
        } catch (IOException e) { }
        for(int x = 1; x<= 13;x++){
            int k = 0;
            if(x>1){
                k = 143*(x-1);
            }
            Image img2=img.getSubimage(0+k,0,143,164);
            ImageIcon imageIcon = new ImageIcon(img2);
            
            //imgA[x]= new ImageIcon(img2);
            imgA[x]= resize(imageIcon,size);
        }
    }
    
    /*public void readAntImg(int size){
    
    try {
    //make dynamic
    
    img = ImageIO.read(Setup.class.getResource("Ants.png"));
    //ImageIcon icon = new ImageIcon(img2);
} catch (IOException e) { }
for(int x = 1; x<= 4;x++){
int k = 0;
if(x>1){
k = 143*(x-1);
}
Image img2=img.getSubimage(0+k,0,143,164);
ImageIcon imageIcon = new ImageIcon(img2);

//imgA[x]= new ImageIcon(img2);
antsIm[x]= resize(imageIcon,size);
}
}*/
    
    /**
     * Alters the size of image icon
     * @param icon takes an ImageIcon 
     * @param size takes an int that relates to the desired size of the image
     * @return ImageIcon returns the resized image
     */
    public ImageIcon resize(ImageIcon icon,int size){
        int widthResize= size;
        int heightResize= size;
        Image imgResize = icon.getImage();
        BufferedImage buf = new BufferedImage(imgResize.getWidth(null), imgResize.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = buf.createGraphics();
        g.drawImage(imgResize, 0, 0, widthResize, heightResize, null);
        ImageIcon newIcon = new ImageIcon(buf);
        return newIcon;
    }
    
}

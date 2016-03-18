/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ant;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
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
    JFrame frame;
    BufferedImage img;
    ImageIcon[] imgA;
    
    /**
     * Draws the board 
     * @param A a 2d int array representing the world
     * @param size size of tiles (1>= size <= 130)
     */
    public Board(int[][] A, int size) {
        if(size<1){size=1;}
        if(size>130){size = 130;}
        imgA = new ImageIcon[14];
        readImg(size);
        frame= new JFrame("Ant Game");
                
        frame.add(buildBoard(A, size));
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        
    }
    
    /**
     * calculates the placing of tiles to create the board
     * @param boardModel a 2d int array that refers to the boards design 
     * @param size is an int that refers to the tile size
     * @return JPanel that contains the board
     */
    public JPanel buildBoard(int[][] boardModel, int size){
        JPanel pan = new JPanel();
        pan.setLayout(null); 
        
        int height = boardModel.length;
        if(height < 1){return null;}
        int width = boardModel[1].length;
        int imgW = size; //60;//141;
        double ratio = (44.0/60);
        //System.out.println(Math.ceil(ratio*size));
        int imgH = (int) Math.ceil(ratio*size);//44;//123;//
        Insets insets = pan.getInsets();
        int buff= size/2;//30;//70;
        int buffer;
        
        for(int y = 0; y<height;y++){
            int[] temp = boardModel[y];
            if(y%2 != 0)
                buffer = buff;
            else
                buffer = 0;
            for(int x =0; x<width;x++){
                int w =(x*imgW)+buffer;
                int h =(y*imgH); //0*h?????????
                JLabel b1 = getImg(temp[x]);
                Dimension dSize = b1.getPreferredSize();
                pan.add(b1);
                b1.setBounds(w + insets.left, h + insets.top, dSize.width, dSize.height);
            }
        }
        
        
        pan.setPreferredSize(new Dimension((width*imgW)+buff,(imgH*height)+(imgH/2)));
        
        return pan;
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
    
   
    
    /**
     * Reads images into array
     * @param size alters the image sizes to equal size
     */
    public void readImg(int size){
       
        try {
            //make dynamic
           /* img = ImageIO.read(new File("C:\\Users\\vikto\\Google Drive\\UNI\\Year 2\\"
                + "term 2\\Software Engineering\\Assignment\\ANT\\src\\ant\\textures.png"));*/
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
    
    /**
     * Alters the size of image icon
     * @param icon takes an ImageIcon 
     * @param size takes an int that relates to the desired size of the image
     * @return ImageIcon returns the resized image
     */
    public ImageIcon resize(ImageIcon icon,int size){
        int width= size;
        int height= size;
        Image img = icon.getImage();
        BufferedImage buf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = buf.createGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        ImageIcon newIcon = new ImageIcon(buf);
        return newIcon;
    }
    
}

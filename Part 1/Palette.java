import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;

public class Palette extends JPanel implements ActionListener
{
  
	Timer timer; //Swing timer that controls car spinning
	ImageIcon[] car1Images; //Array of sprite images
	
	int carPhase = 0;//Phase of car(Car at a specific angle)
	
	public Palette()
	{
      //Initalize swing timer
		timer = new Timer(100, this);
		
      //Initalize car images
		car1Images = new ImageIcon[16];
		
		String path = "car1\\";// Path of images
      
      //Fill car1Images with images from path directory
		for(int i = 0; i < car1Images.length; i++)
		{
			  String currentCar = path + String.valueOf(i) + ".png"; //full image path
			  
           //initalize image object to hold the current image
			  car1Images[i] =  new ImageIcon(currentCar);
		}
      
      //Start timer
      timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == timer)
		{
			//If timer tick
         
         //Rotate sprite by 22.5 degrees by changing index of current image
			carPhase++;//Change phase of car
         
         
			if(carPhase == 16)
			{
            //Reset image to original angle
			    carPhase = 0;
			}
         
         //Refresh graphics
			repaint();
		}
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //50 is the height and width of images being drawn to the panel
        int x = (getWidth() - 50)/2;
        int y = (getHeight() - 50)/2;
        
        car1Images[carPhase].paintIcon(this, g, x, y);
    }
}

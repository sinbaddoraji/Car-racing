import java.awt.*;
import java.awt.Stroke;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

   /**
    *
    * Author: SID 1651542
    */

public class Palette extends JPanel implements ActionListener, KeyListener
{
    Timer timer; //Swing timer that controls car spinning
    ImageIcon[] car1Images, car2Images; //Array of sprite images
        
    int[] carPhase;//Phase of car(Car at a specific angle)
    int[] carPosition; //array holding x and y value of cars
        
    int[] carLane; //Lane 1 -> inner lane, Lane 2 -> outer lane
        
    int[] displacementFactor; //Displacement factor of car 1 and 2
        
    Stroke dashedStroke;
        
    Label carSpeedLabel;
    
    Clip[] clips;
    
    boolean playSound;
    
    public Palette()
    {
        carPhase = new int[] {0, 0};
        carLane = new int[] {0, 0};
        carPosition = new int[]{430, 150, 430, 100}; //X, Y values of car 1 and 2
        displacementFactor = new int[] {0, 0}; //Displacement facor of car 1 and 2
               
        carSpeedLabel = new Label("              Car  Speed: 0");
        add(carSpeedLabel); 
                
        //Configure key listner
        addKeyListener(this);
        
        try
        {
            clips = new Clip[3];
            clips[0] = AudioSystem.getClip(); 
            clips[0].open(AudioSystem.getAudioInputStream(new File("Audio\\swerve.wav")));
            
            clips[1] = AudioSystem.getClip();
            clips[1].open(AudioSystem.getAudioInputStream(new File("Audio\\crash.wav")));
            
            clips[2] = AudioSystem.getClip();
            clips[2].open(AudioSystem.getAudioInputStream(new File("Audio\\drive.wav")));
        }
        catch(Exception e)
        {
            
        }
        
        playSound = true;
        
        //Initalize car images
        car1Images = new ImageIcon[16];
        car2Images = new ImageIcon[16];
      
        String path = "car1\\";// Path of car1 images
        String path2 = "car2\\";// Path of car2 images
        
        //Fill car1Images with images from path directory
      	for(int i = 0; i < car1Images.length; i++)
      	{
                  String currentCar1 = path + String.valueOf(i) + ".png"; //full image path
                  String currentCar2 = path2 + String.valueOf(i) + ".png"; //full image path
                  //initalize image object to hold the current image
                  car1Images[i] =  new ImageIcon(currentCar1);// Current sprite image of car 1
                  car2Images[i] =  new ImageIcon(currentCar2);//Current sprite image of car 2
      	}
                
        dashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 50, new float[]{30.6f, 0, 0}, 0);
        
        //Initalize swing timer
        timer = new Timer(100, this);
        timer.start();
    }
    
     void PlaySound(int index)  
     {
        if(!timer.isRunning()) return;
        if(!playSound) return;
        
        try
        {
            //Restart clip if reached end
            int frameLength = clips[index].getFrameLength();
            if(clips[index].getFramePosition() == frameLength)
                clips[index].setMicrosecondPosition(1);
            //Play clip
            clips[index].start();
        }
        catch (Exception exc)
        {
            
        }
    }
    
    void IncreaseCarSpeed(int car)
    {
        if (displacementFactor[car] < 10) 
            displacementFactor[car] += 1;
        
        UpdateDetails();
    }

    void ReduceCarSpeed(int car)
    {
        if (displacementFactor[car] > -10)
            displacementFactor[car] -= 1;
        
        UpdateDetails();
    }

    void AssertCarLane(int car)
    {
        int carX = carPosition[car == 0 ? 0 : 2];
        int carY = carPosition[car == 0 ? 1 : 3];
        
        if(carX <= 70 || carX >= 734|| carY <= 118 || carY >= 531)
            carLane[car] = 2;
        else
            carLane[car] = 1;
    }

    void ControlCarSpeed(int car, int lane)
    {
        int xLim0, xLim1, yLim1, yLim0; //Race track corners
        int carX; int carY; int disFac;// Car x, y values and displacement factor
        
        //index of sprite image
        int carDirection = this.carPhase[car];
        
        if(lane == 1)
        {
            xLim0 = 148; xLim1 = 649;
            yLim1 = 450; yLim0 = 200;
        }
        else
        {
            xLim0 = 97; xLim1 = 700;
            yLim1 = 502; yLim0 = 144;
        }

        carX = carPosition[car == 0 ? 0 : 2];
        carY = carPosition[car == 0 ? 1 : 3];
        disFac = displacementFactor[car];
        
        //Car is close to top right corner
        boolean nearTopR = carX > xLim1 - disFac * 5  && carX < xLim1;
        
        //Car is close to lower right corner
        boolean nearlowerR = carY > yLim1 - disFac * 5 
                && carY < yLim1 && carDirection == 4;
        
        //Car is close to top left corner
        boolean nearTopL = carY <= yLim0 + disFac * 5  
                && carY > yLim0 && carDirection == 12;
        
        //Car is close to lower left corner
        boolean nearlowerL = carX <= xLim0 + disFac * 5  && carX > xLim0 ;
            
        //Reduce car speed if near road corner
        if((nearTopR || nearlowerR || nearTopL || nearlowerL) && disFac > 4) 
            ReduceCarSpeed(car);
        
    }

    void MoveCar(int car)
    {
        int xDiff = 0, yDiff = 0;
        
        switch(carPhase[car])
        {
            case 0: case 8:
                 xDiff = displacementFactor[car];
                 if(carPhase[car] == 8)
                     xDiff *= -1; //Move car left
            break;
            
            case 1: case 2: case 3: case 5: case 6: case 7:
            case 9: case 10: case 11: case 13: case 14: case 15:
                xDiff = displacementFactor[car] / 2;
                yDiff = displacementFactor[car] / 2;
                
                //Move car left
                if (carPhase[car] >= 5 && carPhase[car] <= 11) xDiff *= -1;
                //Move car up
                if (carPhase[car] >= 9 && carPhase[car] <= 15) yDiff *= -1;
            break;
            
            case 4: case 12:
                yDiff = displacementFactor[car];
                
                if(carPhase[car] == 12) yDiff *= -1; //Move car up
            break;
        }
        //Compare car position and speed. Assert if car will crash
        handleCarCollision(car, xDiff, yDiff);
    }
    
    void handleCarCollision(int car, int xDiff, int yDiff)
    {
        int xIndex = car == 0 ? 0 : 2; //X index of car 
        int yIndex = car == 0 ? 1 : 3; //Y index of car
         
        var carX = carPosition[xIndex] + xDiff; //new x position of car
        var carY = carPosition[yIndex] + yDiff; //new y position of car
        
        //If car intersects with grass in the centre or attempts leaving race track 
        if(carX > 118 && carX < 682 && carY > 160 && carY < 487
                || carX < 40 || carX > 760 || carY < 90 || carY > 560)
        {
            displacementFactor[car] = 0; //Slow car speed down
            UpdateDetails();
            PlaySound(0); //Play swerve sound
        }
        else
        {
            Rectangle car1 = new Rectangle(carPosition[0],carPosition[1], 30, 20);
            Rectangle car2 = new Rectangle(carPosition[2],carPosition[3], 30, 20);
            //if cars crash
            if(car1.intersects(car2))
            {
                PlaySound(1); //Play crash sound
                timer.stop();
                
                JOptionPane.showMessageDialog(this,"Game over, cars crashed. Type 'R' on the game UI to restart");
            }
            else
            {
                //Move car normally
                carPosition[xIndex] = carX;
                carPosition[yIndex] = carY;
            }
            
        }
    }

    @Override
    public void keyPressed(KeyEvent e) 
    {
        try
        {
            if(!timer.isRunning() && e.getKeyCode() != KeyEvent.VK_R) return;
            switch (e.getKeyCode()) {
                //Key control for car given by the server
                 case KeyEvent.VK_UP:    IncreaseCarSpeed(StaticData.GetCarIndex()); break;
                 case KeyEvent.VK_DOWN:  ReduceCarSpeed(StaticData.GetCarIndex());   break;
                 case KeyEvent.VK_LEFT:  rotateLeft(StaticData.GetCarIndex());       break;
                 case KeyEvent.VK_RIGHT: rotateRight(StaticData.GetCarIndex());      break;
                case KeyEvent.VK_M: playSound = !playSound; break;
                
                case KeyEvent.VK_R: 
                  if(!timer.isRunning())
                  {
                      try 
                      {
                           //Send details to server
                            MessageSender.sendMessage("restart");
                      } catch (Exception ex) {}
                      Restart();
                  }
                break;
            }
            SendDetailsToServer();
        }
        catch(Exception et)
        {
            
        }
      
    }
    
    public void Restart()
    {
         carPosition = new int[]{430, 150, 430, 100}; //X, Y values of car 1 and 2
         carPhase = new int[] {0, 0};
         displacementFactor = new int[] {0, 0}; //Displacement facor of car 1 and 2
         timer.start();
    }
    
    void UpdateDetails()
    {
        String carStatus = "              Car  Speed: "+ displacementFactor[StaticData.GetCarIndex()] * 10 + "  ";
        carSpeedLabel.setText(carStatus);
    }

    public void MakeChanges(String[] str) 
    {
        //car carphase carX carY displacementFactor
        int car = Integer.valueOf(str[0]);
        if(car == StaticData.GetCarIndex()) return;
        
        carPhase[car] = Integer.valueOf(str[1]);
        carPosition[car == 0 ? 0 : 2] = Integer.valueOf(str[2]);
        carPosition[car == 0 ? 1 : 3] = Integer.valueOf(str[3]);
        displacementFactor[car] = Integer.valueOf(str[4]);
        
        //.SendDetailsToServer();
    }
    
    public void SendDetailsToServer()
    {
        int i = StaticData.GetCarIndex();
        var carX = String.valueOf(carPosition[i == 0 ? 0 : 2]); //x position of car
        var carY = String.valueOf(carPosition[i == 0 ? 1 : 3]); //y position of car

        String car = String.valueOf(i);
        String carDirection = String.valueOf(carPhase[i]);
        String disFactor = String.valueOf(displacementFactor[i]);
        String message = car + " " 
                        + carDirection + " " + carX + " " + carY + " "
                        + disFactor + " ";
            
        try 
        {
            //Send details to server
            MessageSender.sendMessage(message);
        } catch (Exception ex) {}
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Event handler for timer tick
        
        for(int i = 0; i < 2; i++)
        {
             ControlCarSpeed(i,carLane[i]); //Slow car down if near corner
            MoveCar(i); //Move car
            AssertCarLane(i); //Assert what lane car is on

            //Play car drving sound
            if(displacementFactor[i] > 0) PlaySound(2);
            //car carphase carX carY displacementFactor
        }
       
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) {}
	
    @Override
    public void paintComponent(Graphics gr) 
    {
        Graphics2D g = (Graphics2D) gr.create();
        
        super.paintComponent(g);
        
        g.setColor(Color.black);  g.drawRect(50, 100, 750, 500);  // outer edge
        g.setColor(Color.darkGray);  g.fillRect(50, 100, 750, 500);  // outer edge
        
        g.setColor(Color.darkGray);  g.fillRect( 100, 150, 650, 400 ); // Inner lane 
        
        g.setStroke(dashedStroke); 
        g.setColor(Color.white);  g.drawRect( 100, 150, 650, 400 ); // mid-lane marker 
        g.setStroke(new BasicStroke());
              
        g.setColor(Color.green);  g.fillRoundRect( 150, 200, 550, 300 , 14, 14); //Inner grass
 
        g.setColor(Color.white);  g.drawLine( 425, 100, 425, 200 ); // start line 
        
        car1Images[carPhase[0]].paintIcon(this, g, carPosition[0], carPosition[1]);
        car2Images[carPhase[1]].paintIcon(this, g, carPosition[2], carPosition[3]);
        
        
    }

    void rotateLeft(int car)
    {
        //Rotate car 22.5 degrees anti-clockwise
        carPhase[car]--;
        if(carPhase[car] == -1) carPhase[car] = 15;
        
    }

    void rotateRight(int car)
    {
        //Rotate car 22.5 degrees anti-clockwise
        carPhase[car]++;
        if(carPhase[car] == 16) carPhase[car] = 0;
    }

}

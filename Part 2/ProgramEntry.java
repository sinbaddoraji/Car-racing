import javax.swing.*;
   /**
    *
    * Author: SID 1651542
    */
public class ProgramEntry 
{
	public static void main(String[] args) 
	{    
          //Initalize form
		    Form mainForm = new Form();
      
          //Set form size 850 pixels by 650 pixels 
		    mainForm.setSize(850, 650);
          mainForm.setResizable(false);
      
          //Alow form to close on the pressing of "X"
		    mainForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      
          var g = new GameInfo(mainForm, true);
          g.show();
	}

}

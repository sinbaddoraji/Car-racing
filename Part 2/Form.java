import javax.swing.*;
   /**
    *
    * Author: SID 1651542
    */
public class Form extends JFrame 
{
	//Panel that will be used for all graphical work in the program
	Palette graphicsPalette;
	public Form()
	{
         graphicsPalette = new Palette();
         
         //initalize graohicsPalette
         graphicsPalette.setFocusable(true);
         
         //fill form with graphics palette
		   graphicsPalette.setSize(WIDTH, HEIGHT);
		   this.add(graphicsPalette);
         show();
	}
}



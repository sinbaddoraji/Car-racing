import javax.swing.*;

public class Form extends JFrame
{
	//Panel that will be used for all graphical work in the program
	Palette graphicsPalette;
	
	public Form()
	{
      //initalize graohicsPalette
		graphicsPalette = new Palette();
      //fill form with graphics palette
		graphicsPalette.setSize(WIDTH, HEIGHT);
		this.add(graphicsPalette);
	}
	
}

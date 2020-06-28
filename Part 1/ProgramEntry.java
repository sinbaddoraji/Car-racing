import javax.swing.*;

public class ProgramEntry 
{

	public static void main(String[] args) 
	{
      //Initalize form
		Form mainForm = new Form();
      
      //Set form size
		mainForm.setSize(200, 200);
		mainForm.setResizable(false);
      
      //Alow form to close on the pressing of "X"
		mainForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      
      //Display form
		mainForm.show();
	}

}

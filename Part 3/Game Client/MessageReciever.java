import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

class MessageReceiver implements Runnable 
{
    static byte buffer[]; //Message buffer

    MessageReceiver(DatagramSocket s) 
    {
        //Set up buffer
        buffer = new byte[1024];
    }
    
    public static String Recieve()
    {
        try 
        {
            //Initalize packet
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            //Recieve data from network into packet
            GameClient.socket.receive(packet);
            //Convert pack data to string
            String serverMessage = new String(packet.getData(), 0, packet.getLength());
            //Return recieved message
            return serverMessage;

        } catch (IOException e) {}
        
        return null;
    }
    
     private static boolean RespondToServer(String serverMessage) throws Exception
        {
            
        switch (serverMessage) {
            case "0":
                if(StaticData.GetCarIndex() != 0)
                    StaticData.SetCar(0);
                System.err.println("Allocated white car");
                StaticData.waitForRecieve = true;
                StaticData.GetForm();
                break;
            case "1":
                if(StaticData.GetCarIndex() != 1)
                    StaticData.SetCar(1);
                System.err.println("Allocated blue car");
                StaticData.GetForm();
                break;
            case "restart":
               StaticData.GetForm().Restart();
            break;
            case "exit":
                //Close Application. Server full(Already controling two cars)
                System.out.println("Server down");
                System.out.println("Server closing");
                StaticData.GetForm().dispose();
                System.exit(0);
                return false; //Stop running
            default:
                //Split server message
                String[] args = serverMessage.split(" ");
                //Use array created as details for new car details
                if(args != null && args.length == 5)
                    StaticData.GetForm().MakeChanges(args);
                break;
        }
                  
        return true;  //Continue running 
    }
     
    public static boolean RecieveAndRespond() throws Exception
    {
        //Recieve message from server
        String serverMessage = Recieve(); 
        //Display server message
        if(!serverMessage.isEmpty())
            System.out.println("Server: " + serverMessage);
        //return continue value
        return RespondToServer(serverMessage);
    }

    @Override
    public void run() {

        try 
        {
            //Recieve messages from server and respond indefinitly
            while (RecieveAndRespond()) {}
        } 
        catch (Exception ex) 
        {
            //Display error message
            System.err.println("Messages can no longer recieve messages");
        }
    }
}
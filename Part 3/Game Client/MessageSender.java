
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class MessageSender implements Runnable 
{
    public static boolean sendMessage(String s) throws Exception 
    {
        if(s.isEmpty()) return true; //Stop function if message is empty
           
        System.out.println("Client: " + s); //Display message to be sent
        
        byte buf[] = s.getBytes(); //Get bytes from string

        //Get address
        InetAddress address = InetAddress.getByName(GameClient.host);

        //Initalize packet
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, GameClient.port);

        //Send packet to destination
        GameClient.socket.send(packet);
        
        return !s.equals("exit"); //Keep sending kessages
    }

    @Override
    public void run() 
    {
        boolean connected = false;
        
        //Wait for client to succesfully send packet
        while(!connected)
        {
            try 
            {
                //Send greeting message
                sendMessage("Hello");
                //Set connection status to true
                connected = true;

            } catch (Exception e) {}
        }

        System.out.println("Ready to revieve from network");
        
        while (true) 
        {
            try 
            {
                boolean cont = sendMessage(StaticData.GetClientMessage()); //Send message to server
                //Close socket if sent message is exit
                if(!cont)break;
                //Pause process for a second
            } 
            catch(Exception e) {System.err.println(e); }
        }

    }

}

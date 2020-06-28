import java.net.*;

   /**
    *
    * Author: SID 1651542
    */

public final class GameClient
{
        static DatagramSocket socket;
        public static String host = "127.0.0.1"; //Address to be used for communication
        public static int port = 7334; //Address to be used for communication
        static MessageReceiver messageReciever; //Class handling recieving messages
        static Thread messageRecieverThread; //Thread dedicated to the message reciever
        
        static MessageSender messageSender; //Class handling sending messages to the server
        static Thread messageSenderThread; //Thread dedicated to the message sender
        
	public static void main(String[] args) throws Exception
	{    
            if(args.length > 0)
            {
                //Get port and host info if started with parameters
                try
                {
                    host = args[0];
                    port = Integer.valueOf(args[1]);
                }
                catch(NumberFormatException e)
                {
                    System.out.println(e.getMessage());
                }
            }
             //Set up socket
                socket = new DatagramSocket();
                System.out.println("Socket set up");
                //Set up class for recieving messages
                messageReciever = new MessageReceiver(socket);
                //Set up class for sending messages
                messageSender = new MessageSender();
                //Set up thread handling the message reciever
                messageRecieverThread = new Thread(messageReciever);
                //Set up thread handling the message sender
                messageSenderThread = new Thread(messageSender);
                //Start message reciever
                messageRecieverThread.start();
                System.out.println("Recieving thread Initalized");
                //Start message sender
                messageSenderThread.start();
                System.out.println("Sending thread Initalized");
	}
}

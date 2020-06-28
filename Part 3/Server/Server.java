import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread
{
    //Network port number
    public static int port = 7334;
    //Buffer size
    private final static int BUFFER = 1024;
    //Message buffer
    static byte[] buf = new byte[BUFFER];
    //Network socket
    private static DatagramSocket socket;
    //List of client addresses
    private static ArrayList<InetAddress> clientAddresses;
    //List of client ports
    private static ArrayList<Integer> clientPorts;
    //List of client ID's
    private static ArrayList<String> existingClients;

    public static void main(String args[]) 
    {
        try 
        {
            if(args.length > 0)
            {
                port = Integer.getInteger(args[0]);
            }
            //Set up server
            socket = new DatagramSocket(port);
            //Set up list of client addresses
            clientAddresses = new ArrayList();
            //Set up list of client ports
            clientPorts = new ArrayList();
            //Set up list of ID's refrencing clients
            existingClients = new ArrayList();
            //Run server
            RunServer();
        } 
        catch (IOException | InterruptedException ex) 
        {
            //Print out error message
            System.out.println(ex.getMessage());
        }

    }
    
    private static String[] GetClientMessage() throws IOException, InterruptedException
    {
        //Return array in format [Client id, client message]
        Arrays.fill(buf, (byte)0);
        
        //Initalize data packet
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        
        //Recieve client message from socket to packet
        socket.receive(packet);
        
        //Get client address
        InetAddress clientAddress = packet.getAddress();
        
        //Get client Port
        int clientPort = packet.getPort();
        
        //Client port
        String id = clientAddress.toString() + "," + clientPort;

        //Check if client is an old one
        if (!existingClients.contains(id)) 
        {
            //Add id to id list
            existingClients.add(id);

            //Add port to client port list
            clientPorts.add(clientPort);

            //Add address to address list
            clientAddresses.add(clientAddress);
            
            //Prompt server admin that client has been added
            System.out.println(id + " Joined the network");
            
            int intValue = existingClients.indexOf(id);
            String message = String.valueOf(intValue);
            
            if(intValue < 2)
            {
                //Send only to specific client
                SentToClient(id, message, false);
            }
            else
            {
                //Prompt client to exit
                SentToClient(id, "Exit", false);
            }
        }

        var clientMessage = new String(buf, buf.length).trim();

        if(!clientMessage.isEmpty())
        {
            System.out.println("Client: " + clientMessage);
            return new String[]{id, clientMessage};
        }
        else return null;
    }
    
    public static void SentToClient(String id, String message, boolean toAll) throws IOException, InterruptedException
    {
         if(message.isEmpty()) return;
         
         System.out.println("Server: " + message);

         byte[] data = (message).getBytes();
         for (int i=0; i < existingClients.size(); i++) 
         {
            boolean isClient = existingClients.get(i) == null 
                    ? id == null : existingClients.get(i).equals(id);
            
            if(isClient || toAll)
            {
                //If sender ID then reply
                InetAddress cl = clientAddresses.get(i);

                int cp = clientPorts.get(i);

                var packet = new DatagramPacket(data, data.length, cl, cp);

                socket.send(packet);
                break;
            }
          }
          
    }
    
    public static boolean GetAndRespondToClient() throws IOException, InterruptedException
    {
        String[] clientMessage = GetClientMessage();
        if(clientMessage == null) return true; 
        if(clientMessage[1].isEmpty()) return true;
        
        if (clientMessage[1].equals("exit"))
        {
            //Send only to specific client
            System.out.println("Client Controling car has left the server");
            System.out.println("Server is now shutting down ");
            SentToClient(existingClients.get(0), clientMessage[1], false);
            SentToClient(existingClients.get(1), clientMessage[1], false);
            return false;
        }
        else if (clientMessage[1].equals("restart"))
        {
            //Send only to specific client
            System.out.println("Restarting game");
            SentToClient(existingClients.get(0), clientMessage[1], false);
            SentToClient(existingClients.get(1), clientMessage[1], false);
            return false;
        }
        else
        {
            //car carphase carX carY displacementFactor
            String[] args = clientMessage[1].split(" ");
            if(args.length != 5) return true;
            
            int i;
            if(args[0].equals("0")) i = 1;
            else i = 0;
            SentToClient(existingClients.get(i), clientMessage[1], false);
             
        }
        return true;
    }
    
    public static void RunServer() throws IOException, InterruptedException 
    {
        System.out.println("Server Running");
        
        while (true) 
        {
            if(!GetAndRespondToClient()) break;

        }

    }


}
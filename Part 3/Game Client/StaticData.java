class StaticData 
{
    //Message from client
    private static String clientMessage = "hello";
    
    public static void SetClientMessage(String message)
    {
        clientMessage = message;
    }
    
    public static String GetClientMessage()
    {
        var output = clientMessage; //Retrieve message from memory
        clientMessage = "";//clear client message
        return output;//Return 
    }
    
    
    //Main form(Form containing game)
    private static Form mainForm;
    
    public static Form GetForm()
    {
        if (mainForm == null)
        {
            
            System.err.println("Assigned car: " + car);

            //Initalize form
            mainForm = new Form();

            
        }
        else return mainForm;
        return null;
    }
    //Car allocated to this instance
    private static int car = -1;
    
    public static void SetCar(int car)
    {
        if(StaticData.car == -1)
            StaticData.car = car;
    }
    
    public static int GetCarIndex()
    {
        return car;
    }
    
    public static boolean waitForRecieve;
    
}
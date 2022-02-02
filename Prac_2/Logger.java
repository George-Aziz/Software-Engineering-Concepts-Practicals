import java.io.*;

/*******************************************************************************************
 * Author: George aziz
 * Purpose: In charge of writing output to 'cron.log'. It does this in its own thread, but 
 * assumes that other threads will call the setMessage() in order to provide messages to log
 * Date Modified: 18/08/2021
 *******************************************************************************************/
public class Logger
{
    private Thread loggerThread = null;
    private String curMessage = null; 
    private Object monitor = new Object();

    //Sets the current message to log
    public void setMessage(String newMessage) throws InterruptedException
    {  
        synchronized(monitor)
        { 
            //Keep waiting while there is still a message
            while(curMessage != null)
            {
                monitor.wait();
            }
            curMessage = newMessage;
            monitor.notifyAll();
        } 
    }
    
    //Starts the logger thread
    public void start()
    {
        // Verify that 'Schedler thread' doesn't exist yet.
        if(loggerThread != null) { throw new IllegalStateException(); }

        //Start thread
        loggerThread = new Thread(loggerTask, "Logger-Thread");
        loggerThread.start();
    }
    
    //Ends the logger thread gracefully
    public void stop()
    {
        // Verify that 'Scheduler threead' DOES exist.
        if(loggerThread == null) { throw new IllegalStateException(); }

        loggerThread.interrupt();
        loggerThread = null;
    }

    //The logger task that will be run on a separate thread
    Runnable loggerTask = () ->
    {
        synchronized(monitor)
        {
            while(true)
            {
                try 
                {
                    //Keep waiting until there is a message
                    while(curMessage == null)
                    {
                        monitor.wait();
                    }

                    //Appends message to file names "cron.log"
                    try(PrintWriter writer = new PrintWriter(new FileWriter("cron.log", true)))
                    {
                        writer.println(curMessage);
                    }
                    curMessage = null; //Sets the message to null for a new message to be made
                    monitor.notifyAll(); //Notifies all threads that are waiting on monitor
                }
                catch(IOException ex)
                {
                    System.out.println("[ERROR] IOException occured with the Logger!");         
                }
                catch(InterruptedException ex)
                {
                    System.out.println("Logger thread ending...");
                    //If interrupted, just break the loop to end thread
                    break;
                }
            }
        }
    };
}

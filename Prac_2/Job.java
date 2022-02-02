import java.io.*;
import java.nio.Buffer;
import java.util.TimerTask;

/*****************************************************************************
 * Author: George aziz
 * Purpose: Represents a job (command) to be run, and performs the execution
 * Date Modified: 18/08/2021
 ****************************************************************************/
public class Job extends TimerTask implements Runnable
{
    private String command;
    private int delay;
    private Logger logger;
    private boolean started;

    //Job Constructor
    public Job(String command, int delay, Logger logger)
    {
        this.command = command;
        this.delay = delay;
        this.logger = logger;
        this.started = false;
    }
    
    //The task of a job
    @Override
    public void run() {
        try 
        {
            //Run the string command
            Process proc = Runtime.getRuntime().exec(command);
        
            //Arrange to capture the command's output, line by line
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(proc.getInputStream()));
            String line = reader.readLine();
            while(line != null)
            {
                output.append(line);
                output.append("\n");
                line = reader.readLine();
            }

            //After command's output, this means the command has finished
            logger.setMessage("[" + command + "] " + output.toString());
        }
        catch(IOException ex) //Can happen when the provided command is invalid
        {
            System.out.println("\n[ERROR] IOException occured with a Job! (Invalid command inputted - All other jobs will still continue working [Press 'X' to exit of cron])");
        }
        catch(InterruptedException ex)
        {
            System.out.println("[ERROR] InterruptedException occured with a Job!");
        }
    }

    //Accesor to the delay field
    public int getDelay()
    {
        return this.delay;
    }

    public boolean getStarted()
    {
        return this.started;
    }

    public void setStarted()
    {
        this.started = true;
    }
}

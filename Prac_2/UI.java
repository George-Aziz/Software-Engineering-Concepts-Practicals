import java.util.*;

/*****************************************************************************
 * Author: George aziz
 * Purpose: A simple console-based UI
 * Date Modified: 18/08/2021
 ****************************************************************************/
public class UI
{
    private Scheduler scheduler;
    private Logger logger;
    
    public UI(Scheduler scheduler, Logger logger)
    {
        this.scheduler = scheduler;
        this.logger = logger;
    }

    
    //Runs the console-based menu system (in the current thread)
    public void menu()
    {
        Scanner sc = new Scanner(System.in);
        char choice;
        try 
        {
            do
            {
                System.out.print("(n) New command, or (x) Exit? ");
                choice = (sc.nextLine().toLowerCase() + " ").charAt(0);
                
                if(choice == 'n')
                {
                    // Ask user for details of the new command
                    String command;
                    do
                    {
                        System.out.print("Enter command: ");
                        command = sc.nextLine();
                    }
                    while(command == null || command.length() == 0);
                    
                    int delay = 0;
                    do
                    {
                        try
                        {
                            System.out.print("How many seconds between executions? ");
                            delay = sc.nextInt();
                            sc.nextLine();
                        }
                        catch(InputMismatchException e) 
                        {
                            // Non-numeric input; continue loop.
                        }
                    }
                    while(delay <= 0);
                    
                    // Create and add new job to the scheduler
                    Job job = new Job(command, delay, logger);
                    scheduler.addJob(job);
                }
            }
            while(choice != 'x');
            
            // When the user wants to exit, we need to shut down the other threads and any other resources.
            sc.close();
            scheduler.stop();
            logger.stop();
        }
        catch(IllegalStateException ex)
        {
            //.. Scheduler/Logger Thread already exists and tried to open a new one
            System.out.println("[ERROR] IllegalStateException - Scheduler/Logger thread already exists");
        }
    }
}

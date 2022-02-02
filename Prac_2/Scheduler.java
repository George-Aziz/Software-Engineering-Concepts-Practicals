import java.util.*;

/*********************************************************************************
 * Author: George aziz
 * Purpose: Keeps track of all the jobs, and runs each one at the appropriate time
 * Date Modified: 18/08/2021
 *********************************************************************************/
public class Scheduler
{
    private List<Job> jobs = new ArrayList<>();
    private Thread schedulerThread = null;
    private Object mutex = new Object();
    private List<Timer> timers = new ArrayList<>();
    
    //Adds job to the list
    public void addJob(Job newJob)
    {
        synchronized(mutex)
        {
            jobs.add(newJob);
        }
    }
    
    //Starts the scheduler thread
    public void start()
    {
        // Verify that 'Schedler thread' doesn't exist yet.
        if(schedulerThread != null) { throw new IllegalStateException(); }

        //Start thread
        schedulerThread = new Thread(schedulerTask, "Scheduler-Thread");
        schedulerThread.start();
    }

    //Ends the scheduler thread gracefully
    public void stop()
    {
        // Verify that 'Scheduler thread' DOES exist.
        if(schedulerThread == null) { throw new IllegalStateException(); }

        //Ends all job tasks and then interrupts the scheduler thread
        for (Timer timer : timers) 
        {
            timer.cancel();
        }
        schedulerThread.interrupt();
        schedulerThread = null;
    }

    //The scheduler task that will be run on a separate thread
    Runnable schedulerTask = () ->
    {
        while(true)
        {
            try 
            {
                synchronized(mutex)
                {
                    for (Job job : jobs) 
                    {
                        try 
                        {
                            if(!job.getStarted()) //Only creates if a job hasn't started
                            {
                                job.setStarted(); //Sets started to true
                                Timer timer = new Timer(); //New timer thread
                                timers.add(timer); //Added to a list for later to be stopped
                                //Schedules job tasks at fixed delay rate
                                timer.scheduleAtFixedRate(job, 0, job.getDelay() * 1000);
                            }
                        }
                        catch(IllegalStateException ex)
                        {
                            //Nothing to do (Only happens if task already scheduled/cancelled)
                        }
                    } 
                }
                Thread.sleep(1000L);
            }
            catch(InterruptedException ex)
            {
                System.out.println("Scheduler thread ending...");
                //If interrupted, just break the loop to end thread
                break;
            }
        }
    };
}

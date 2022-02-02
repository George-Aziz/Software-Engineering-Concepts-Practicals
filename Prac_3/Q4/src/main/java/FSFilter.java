package edu.curtin.comp3003.filesearcher;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.*;

import javafx.application.Platform;

public class FSFilter 
{
    private String searchPath;
    private String searchTerm;
    private FSUserInterface ui;
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private static final String POISON = new String();

    public FSFilter(String searchPath, String searchTerm, FSUserInterface ui)
    {
        this.searchPath = searchPath;
        this.searchTerm = searchTerm;
        this.ui = ui;
    }

    Runnable filterTask = () ->
    {
        try 
        {
            while(true)
            {
                String fileStr = queue.take();
                if(fileStr == POISON)
                {
                    break; //End thread
                }
                else 
                {
                    // Check whether each file contains the search term, 
                    // and if so, add it to the list
                    if(fileStr.contains(searchTerm))
                    {
                        Platform.runLater(() ->  // Runnable to re-enter GUI thread
                        { 
                            ui.addResult(fileStr);
                        });
                    }
                }  
            }
        }
        catch(InterruptedException ex)
        {

        }
    };

    Runnable finderTask = () ->
    {
        try 
        {
            try
            {
                // Recurse through the directory tree
                Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>()
                {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    {
                        try 
                        {
                            //Adds all files visted and enqueue 
                            queue.put(file.toString()); 
                        }
                        catch(InterruptedException ex) { }
                        return FileVisitResult.CONTINUE; //Continues
                    }
                });
            }
            catch(IOException e)
            {
                Platform.runLater(() ->  // Runnable to re-enter GUI thread
                { 
                    // This error handling is a bit quick-and-dirty, but it will suffice here.
                    ui.showError(e.getClass().getName() + ": " + e.getMessage());
                    //throw new InterruptedException();
                });
            }
            finally
            {
                queue.put(POISON);
            }
        }
        catch(InterruptedException ex)
        {
            //..
        }
        
    };
}

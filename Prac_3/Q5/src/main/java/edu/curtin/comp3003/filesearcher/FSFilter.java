package edu.curtin.comp3003.filesearcher;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.concurrent.*;

import javafx.application.Platform;

/*************************************************************************************
 * Author: George Aziz
 * Purpose: Finds and filters all files to match the search term inputted by the user
 * Date Last Modified: 20/08/2021
 *************************************************************************************/
public class FSFilter 
{
    private String searchPath;
    private String searchTerm;
    private FSUserInterface ui;
    private ExecutorService es;

    //Class Constructor
    public FSFilter(String searchPath, String searchTerm, FSUserInterface ui)
    {
        this.searchPath = searchPath;
        this.searchTerm = searchTerm;
        this.ui = ui;
        es = Executors.newFixedThreadPool(3);
    }

    Runnable finderTask = () ->
    {
        try {
            // Recurse through the directory tree
            Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileStr = file.toString();

                    //Task gets submitted to an executor service to get queued
                    es.submit(() ->
                    {
                        //Goes through all lines in a file to find if it contains the searchTerm
                        String line;
                        try(BufferedReader br = new BufferedReader(new FileReader(fileStr))) {
                            while ((line = br.readLine()) != null) {
                                if (line.contains(searchTerm)) {
                                    Platform.runLater(() -> { ui.addResult(fileStr); });
                                    break; //If term is found, no need to continue searching
                                }
                            }
                        }
                        catch (IOException e)
                        {
                            //Ignore the file as if nothing happened
                        }
                    });
                    return FileVisitResult.CONTINUE; //Continues
                }
            });
        }
        catch(IOException e)
        {
            Platform.runLater(() -> { ui.showError(e.getClass().getName() + ": " + e.getMessage()); });
        }
        //Once the thread is complete, makes thread null so new thread can run also shuts down all exec services
        ui.endThread();
        es.shutdown();
    };
}

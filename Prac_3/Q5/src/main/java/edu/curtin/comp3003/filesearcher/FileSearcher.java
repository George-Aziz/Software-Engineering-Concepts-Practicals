package edu.curtin.comp3003.filesearcher;

import javafx.application.Application;
import javafx.stage.Stage;

/*************************************************************************************
 * Author: George Aziz
 * Purpose: Beginning of the program that starts the U.I Class
 * Date Last Modified: 20/08/2021
 * NOTE: This class has been provided by the unit to be used for this practical
 *************************************************************************************/
public class FileSearcher extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }
    
    @Override
    public void start(Stage stage)
    {
        new FSUserInterface().show(stage); //Starts the UI
    }
}

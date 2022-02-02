package edu.curtin.bustimetable;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/********************************************************************************
 * Author: George Aziz
 * Purpose: Entry point for the bus timetabling app
 * Date Last Modified: 04/10/2021
 * NOTE: This class has been provided by the unit to be used for this practical
 * It can be invoked with the command-line parameter --locale=<value>
 *******************************************************************************/
public class BusTimetableApp extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
            //Locale string that is passed from the console
            var localeString = getParameters().getNamed().get("locale");

            //English Locale as default unless said otherwise
            Locale loc = new Locale("en", "AU");
            ResourceBundle bundle = ResourceBundle.getBundle("bundle", loc);

            if (localeString != null) {
                //Current supported languages, if any new bundles get added in future can add in if statement
                if (localeString.equals("ar") || localeString.equals("en")) {
                    loc = new Locale(localeString);
                    bundle = ResourceBundle.getBundle("bundle", loc);
                } else {
                    System.out.println("ERROR: Locale not found!!!");
                }
            }
            //Setup for other objects to be used within program's execution
            var entries = FXCollections.<TimetableEntry>observableArrayList();
            FileIO fileIO = new FileIO();
            LoadSaveUI loadSaveUI = new LoadSaveUI(stage, entries, fileIO, bundle);
            AddUI addUI = new AddUI(entries, bundle);
            //Start of the main user interface screen
            new MainUI(stage, entries, loadSaveUI, addUI, loc, bundle).display();
    }
}

package edu.curtin.comp3003.filesearcher;

import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**************************************************************************************************************
 * Author: George Aziz
 * Purpose: A JavaFX-based user interface for the file searcher.
 * Date Last Modified: 20/08/2021
 * NOTE: This class has been provided by the unit to be used for this practical and has been slightly modified
 *************************************************************************************************************/
public class FSUserInterface
{
    private static final double SPACING = 8.0;
    private Text tally;
    private volatile Thread finderThread = null;

    // A list-like container used to keep track of search results.
    private ObservableList<String> searchResults;
    
    public FSUserInterface(){ }

    public void show(Stage stage)
    {
        stage.setTitle("File Searcher");
        
        FlowPane searchPanel = new FlowPane(SPACING, SPACING);
        TextField searchPathBox = new TextField("/usr");
        TextField searchTermBox = new TextField();
        Button searchButton = new Button("Search");
        
        searchPanel.getChildren().addAll(
            new Text("Path:"),
            searchPathBox,
            new Text("Search text:"),
            searchTermBox,
            searchButton);
            
        searchButton.setOnAction((event) ->
        {
            if(finderThread == null) { //Only runs if no finderThread is running
                FSFilter filter = new FSFilter(
                        searchPathBox.getText(),
                        searchTermBox.getText(),
                        this);

                finderThread = new Thread(filter.finderTask, "Finder-Thread");
                finderThread.start();
            }
            else { //Notifies user that there currently exists a task and to try again later
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Task currently running - Please wait and try again later", ButtonType.CLOSE);
                a.setResizable(true);
                a.showAndWait();
            }
        });
        
        ListView<String> resultsList = new ListView<>();
        searchResults = resultsList.getItems();
        
        FlowPane auxPanel = new FlowPane(SPACING, SPACING);
        auxPanel.setAlignment(Pos.BASELINE_RIGHT);
        tally = new Text();
        Button clearButton = new Button("Clear results");
        auxPanel.getChildren().addAll(tally, clearButton);
        
        clearButton.setOnAction((event) ->
        {
            searchResults.clear();
            tally.setText("");
        });
        
        BorderPane root = new BorderPane();
        BorderPane.setMargin(searchPanel, new Insets(SPACING));
        BorderPane.setMargin(resultsList, new Insets(0.0, SPACING, 0.0, SPACING));
        BorderPane.setMargin(auxPanel, new Insets(SPACING));
        root.setTop(searchPanel);
        root.setCenter(resultsList);
        root.setBottom(auxPanel);
        
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    //Add strings to list in GUI
    public void addResult(String result)
    {
        searchResults.add(result);
        tally.setText(Integer.toString(searchResults.size()) + " result(s) found");
    }

    //Shows error to user on GUI
    public void showError(String message)
    {
        Alert a = new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE);
        a.setResizable(true);
        a.showAndWait();
    }

    //Method that sets the finder thread to null and shuts down all threads running on executor service
    //Only happens onc finderThread ends to not get stuck when closing program
    public void endThread()
    {
        this.finderThread = null;
    }
}

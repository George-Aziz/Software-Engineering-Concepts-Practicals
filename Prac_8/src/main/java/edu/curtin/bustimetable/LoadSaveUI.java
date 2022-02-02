package edu.curtin.bustimetable;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ResourceBundle;


/*****************************************************************************************************
 * Author: George Aziz
 * Purpose: Controls those parts of the user interface relating to loading/saving timetable CSV files
 * Date Last Modified: 04/10/2021
 * NOTE: This class has been provided by the unit to be used for this practical
 ****************************************************************************************************/
public class LoadSaveUI
{
    private static final int SPACING = 8;
    
    private Stage stage;
    private ObservableList<TimetableEntry> entries;
    private FileIO fileIO;
    private FileChooser fileDialog = new FileChooser();
    private Dialog<String> encodingDialog;
    private ResourceBundle bundle;
    
    public LoadSaveUI(Stage stage, ObservableList<TimetableEntry> entries, FileIO fileIO, ResourceBundle bundle)
    {
        this.stage = stage;
        this.entries = entries;
        this.fileIO = fileIO;
        this.bundle = bundle;
    }

    // Internal method for displaying the encoding dialog and retrieving the name of the chosen encoding
    private String getEncoding()
    {
        if(encodingDialog == null)
        {
            var encodingComboBox = new ComboBox<String>();
            var content = new FlowPane();
            encodingDialog = new Dialog<>();
            encodingDialog.getDialogPane().setStyle("-fx-font-family: 'arial'");
            encodingDialog.setTitle(bundle.getString("encoding_dialog"));
            encodingDialog.getDialogPane().setContent(content);
            encodingDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);        
            encodingDialog.setResultConverter(
                btn -> (btn == ButtonType.OK) ? encodingComboBox.getValue() : null);
            
            content.setHgap(SPACING);
            content.getChildren().setAll(new Label(bundle.getString("encoding_label")), encodingComboBox);
            
            encodingComboBox.getItems().setAll("UTF-8", "UTF-16", "UTF-32");
            encodingComboBox.setValue("UTF-8");
        }        
        return encodingDialog.showAndWait().orElse(null);
    }

    // Asks the user to choose a file to load, then an encoding, then loads the file contents and updates the timetable
    public void load()
    {
        fileDialog.setTitle(bundle.getString("load_file_dialog"));

        File f = fileDialog.showOpenDialog(stage);
        if(f != null)
        {
            String encoding = getEncoding();
            if(encoding != null)
            {
                try
                {
                    Charset selectedEncoding = Charset.forName(encoding);
                    entries.setAll(fileIO.load(f, selectedEncoding));
                }
                catch(IOException | TimetableFormatException e)
                {
                    Alert error = new Alert(
                        Alert.AlertType.ERROR, 
                        String.format(bundle.getString("load_error"), e.getClass().getName(), e.getMessage()),
                        ButtonType.CLOSE
                    );
                    error.getDialogPane().setStyle("-fx-font-family: 'arial'");
                    error.showAndWait();
                }                
            }
        }
    }

    // Asks the user to choose a filename to save the timetable under, then an encoding, then saves the timetable
    // contents to the chosen file in the chosen encoding
    public void save()
    {
        fileDialog.setTitle(bundle.getString("save_file_dialog"));
        File f = fileDialog.showSaveDialog(stage);
        if(f != null)
        {
            String encoding = getEncoding();
            if(encoding != null)
            {
                try
                {
                    Charset selectedEncoding = Charset.forName(encoding);
                    fileIO.save(f, entries, selectedEncoding);
                }
                catch(IOException e)
                {
                    Alert error = new Alert(
                        Alert.AlertType.ERROR, 
                        String.format(bundle.getString("save_error"), e.getClass().getName(), e.getMessage()),
                        ButtonType.CLOSE
                    );
                    error.getDialogPane().setStyle("-fx-font-family: 'arial'");
                    error.showAndWait();
                }
            }
        }
    }
}

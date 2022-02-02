package edu.curtin.bustimetable;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.print.attribute.standard.MediaSize;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/***********************************************************************************************************************
 * Author: George Aziz
 * Purpose: Controls the main window, displaying the bus timetable contents, a toolbar (with 'load', 'save' and 'add'
 *          buttons and a search field) and a status bar that displays the current time
 * Date Last Modified: 13/10/2021
 * NOTE: This class has been provided by the unit to be used for this practical
 **********************************************************************************************************************/
public class MainUI
{
    private static final int SPACING = 8;
    
    private Stage stage;
    private ObservableList<TimetableEntry> entries;
    private LoadSaveUI loadSaveUI;
    private AddUI addUI;
    private TextField statusBar = new TextField();
    private ResourceBundle bundle;
    private Locale locale;
    
    public MainUI(Stage stage, ObservableList<TimetableEntry> entries, LoadSaveUI loadSaveUI, AddUI addUI, Locale locale, ResourceBundle bundle)
    {
        this.stage = stage;
        this.entries = entries;
        this.loadSaveUI = loadSaveUI;
        this.addUI = addUI;
        this.bundle = bundle;
        this.locale = locale;
    }
    
    public void display()
    {
        stage.setTitle(bundle.getString("app_title"));
        stage.setMinWidth(1000);
        stage.setWidth(1000);

        // Create toolbar and button event handlers
        var loadBtn = new Button(bundle.getString("load_btn"));
        var saveBtn = new Button(bundle.getString("save_btn"));
        var addBtn = new Button(bundle.getString("add_btn"));
        var filterText = new TextField();        
        filterText.setPromptText(bundle.getString("filter_text"));
        ToolBar toolBar = new ToolBar(
            loadBtn, saveBtn, addBtn, new Separator(), filterText);            
        loadBtn.setOnAction(event -> loadSaveUI.load());
        saveBtn.setOnAction(event -> loadSaveUI.save());
        addBtn.setOnAction(event -> addUI.addEntry());

        // Table and table data
        var entryTable = new TableView<TimetableEntry>();
        var filteredEntries = new FilteredList<>(entries);
        entryTable.setItems(filteredEntries);
        
        filterText.textProperty().addListener(
            (field, oldVal, newVal) ->
                filteredEntries.setPredicate(entry -> matches(entry, newVal))
        );
        
        // Table columns
        TableColumn<TimetableEntry,String> routeIdCol       = new TableColumn<>(bundle.getString("route_col"));
        TableColumn<TimetableEntry,String> fromCol          = new TableColumn<>(bundle.getString("from_col"));
        TableColumn<TimetableEntry,String> destinationCol   = new TableColumn<>(bundle.getString("destination_col"));
        TableColumn<TimetableEntry,String> departureTimeCol = new TableColumn<>(bundle.getString("departure_col"));
        TableColumn<TimetableEntry,String> arrivalTimeCol   = new TableColumn<>(bundle.getString("arrival_col"));
        TableColumn<TimetableEntry,String> durationCol      = new TableColumn<>(bundle.getString("duration_col"));
        
        entryTable.getColumns().setAll(List.of(routeIdCol, fromCol, destinationCol, departureTimeCol, arrivalTimeCol, durationCol));
        entryTable.prefWidthProperty().bind(stage.widthProperty());
        // Formatting table column values
        routeIdCol.setCellValueFactory( 
            (cell) -> new SimpleStringProperty(cell.getValue().getRouteId()) );
            
        fromCol.setCellValueFactory( 
            (cell) -> new SimpleStringProperty(cell.getValue().getFrom()) );
        
        destinationCol.setCellValueFactory(
            (cell) -> new SimpleStringProperty(cell.getValue().getDestination()) );
            
        departureTimeCol.setCellValueFactory(
            (cell) -> new SimpleStringProperty(getDepartureTimeString(cell.getValue())));
                        
        arrivalTimeCol.setCellValueFactory(
            (cell) -> new SimpleStringProperty(getArrivalTimeString(cell.getValue())));
            
        durationCol.setCellValueFactory(
            (cell) -> new SimpleStringProperty(String.valueOf(cell.getValue().getDuration().toMinutes())));
          
        // Table column widths.
        routeIdCol      .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.10));
        fromCol         .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.30));
        destinationCol  .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.30));
        departureTimeCol.prefWidthProperty().bind(entryTable.widthProperty().multiply(0.10));
        arrivalTimeCol  .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.10));
        durationCol     .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.10));

        // Status bar
        var exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> Platform.runLater(this::updateStatus), 0L, 1L, TimeUnit.SECONDS);
        stage.setOnHiding(event -> exec.shutdown());

        // Add the main parts of the UI to the window.
        var mainBox = new BorderPane();
        mainBox.setTop(toolBar);
        mainBox.setCenter(entryTable);
        mainBox.setBottom(statusBar);
        Scene scene = new Scene(mainBox);
        scene.getRoot().setStyle("-fx-font-family: 'arial'");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
    
    //Matches strings internationalised
    private boolean matches(TimetableEntry entry, String searchTerm)
    {
        String normGetFrom = Normalizer.normalize(entry.getFrom().toLowerCase(), Normalizer.Form.NFKC);
        String normSearchTerm = Normalizer.normalize(searchTerm.toLowerCase(), Normalizer.Form.NFKC);
        String normGetDestination = Normalizer.normalize(entry.getDestination().toLowerCase(), Normalizer.Form.NFKC);
        return normGetFrom.contains(normSearchTerm) || normGetDestination.contains(normSearchTerm);
    }

    //Provides departure time internationalised
    private String getDepartureTimeString(TimetableEntry entry)
    {
        LocalTime time =  LocalTime.parse(entry.getDepartureTime().toString());
        DateTimeFormatter dtf1 = DateTimeFormatter.
                ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(locale);
        String timeFormat = dtf1.format(time);
        return timeFormat;
    }

    //Calculates departure time with duration to provide internationalised arrival time
    private String getArrivalTimeString(TimetableEntry entry)
    {
        LocalTime time =  LocalTime.parse(entry.getDepartureTime().toString());
        Duration duration = Duration.parse(entry.getDuration().toString());
        time = time.plus(duration); //Adds duration onto departure time to get arrival time
        DateTimeFormatter dtf1 = DateTimeFormatter.
                ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(locale);
        String timeFormat = dtf1.format(time);
        return timeFormat;
    }

    //Provides current time localised
    private void updateStatus()
    {
        ZonedDateTime datetime = ZonedDateTime.now();
        DateTimeFormatter dtf1 = DateTimeFormatter.
                ofLocalizedTime(FormatStyle.LONG)
                .withLocale(locale);
        String dateTimeFormat = dtf1.format(datetime);
        statusBar.setText(dateTimeFormat);
    }
}

package application;

import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * class that will create and show the Edit Data window that
 * will allow users to manually edit a months worth of data for
 * a specified farm.
 * 
 * @author Adam
 *
 */
public class EditDataWindow extends AssistantWindow{

  private TableView<Row> table;
  private ObservableList<Row> saveList;
  private ObservableList<Row> list;
  private ComboBox<String> farmSelect;
  private ComboBox<String> yearSelect;
  private ComboBox<Month> monthSelect;
  private TextField editDay;
  private TextField editWeight;
  private SimpleStringProperty updateMsg;
  private SimpleStringProperty loadMsg;
  private SimpleStringProperty tableTitle;
  private boolean tableChanged = false;
  private GregorianCalendar date;
  
  /**
   * Constructor that initializes the screen for this window.
   * 
   * @param stage The stage that the scene will be displayed on.
   */
  public EditDataWindow(Stage stage) {

    Button back = new Button("Back");
    back.setOnAction(actionEvent -> {
      stage.setScene(old);
    });
    
    Label sceneTitle = new Label("Edit Data");
    
    //Stores the data for the table.
    saveList = FXCollections.observableArrayList();
    list = FXCollections.observableArrayList();
    
    //Selection tool for loading data.
    HBox tableID = buildTableID();
    
    Button loadButton = new Button("Load Table");
    loadButton.setOnAction(actionEvent -> {
      loadData();
    });
    
    Label loadStatus = new Label();
    loadMsg = new SimpleStringProperty();
    loadStatus.textProperty().bind(loadMsg);
    
    Label loadTitle = new Label();
    tableTitle = new SimpleStringProperty();
    loadTitle.textProperty().bind(tableTitle);
    
    buildTable();

    //Allows for data to be altered.
    HBox updatePrompt = buildUpdatePrompt();
    
    HBox top = new HBox(back, sceneTitle);
    HBox bottom = buildCancelSave();
    HBox loadBox = new HBox(loadButton, loadStatus);
    
    VBox root = new VBox(top, 
                         tableID, 
                         loadBox,
                         loadTitle,
                         table, 
                         updatePrompt, 
                         bottom);
    
    top.setSpacing(WINDOW_WIDTH / 3.0);
    bottom.setSpacing(WINDOW_WIDTH - 93.0);
    root.setSpacing(5.0);
    updatePrompt.setSpacing(5.0);
    loadBox.setSpacing(5.0);
    sceneTitle.setFont(new Font("System Regular", 30));
    
    scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
  }
  
  /**
   * Method that will build the control element that will allow
   * the user to manually add and change elements of the table.
   * 
   * @return The HBox with the necessary components
   */
  private HBox buildUpdatePrompt() {
    
    editDay = new TextField();
    editDay.setPrefWidth(80);
    
    editWeight = new TextField();
    editWeight.setPrefWidth(80);
    
    Button update = new Button("Update");
    Label updateStatus = new Label();
    updateMsg = new SimpleStringProperty("");
    updateStatus.textProperty().bind(updateMsg);
    update.setOnAction(actionEvent -> {
      updateEntry();;
    });
    
    return new HBox(new Label("Day: "), editDay, 
                    new Label("Weight: "), editWeight, 
                    update, updateStatus);
  }
  /**
   * Method that will build the input controls that will
   * allow the user to request the data to fill the table.
   * 
   * @return The assembled HBox containing the TableID components
   */
  private HBox buildTableID() {
    
    farmSelect = new ComboBox<String>(names);
    farmSelect.setEditable(true);
    farmSelect.setPromptText("Insert Name");
    
    yearSelect = new ComboBox<String>(years);
    yearSelect.setEditable(true);
    yearSelect.setPromptText("Insert Year");
    yearSelect.setPrefWidth(100);
    
    monthSelect = new ComboBox<Month>(MONTHS);
    monthSelect.setPromptText("Choose Month");
    
    return new HBox(new VBox(new Label("Farm ID"), farmSelect), 
                    new VBox(new Label("Year"), yearSelect), 
                    new VBox(new Label("Month"), monthSelect));
  }
  
  /**
   * Method that will load the data that will be displayed in the table,
   * will return if the entered data does not match the required form 
   * and will inform the user.
   */
  private void loadData() {
    
    String farmID = farmSelect.getValue();
    String yearInput = yearSelect.getValue();
    Month month = monthSelect.getValue();
    int year;
    
    if (farmID == null) {
      loadMsg.set("Must choose or input a Farm");
      return;
    }
    
    if (yearInput == null) {
      loadMsg.set("Must choose or input a year");
      return;
    }
    
    if (month == null) {
      loadMsg.set("Must choose a month");
      return;
    }
    
    try {
      year = Integer.parseInt(yearInput);
    } catch (NumberFormatException e) {
      loadMsg.set("Year must be an integer");
      return;
    }
    
    saveList.clear();
    date = new GregorianCalendar(year, month.getValue() - 1, 1);
    
    //Simulation of loading data into saveList
    for(int i = 1; i <= date.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
      date.set(Calendar.DAY_OF_MONTH, i);
      saveList.add(new Row(i, factory.get(farmID, date)));
    }
    
    list.clear();
    for(Row r : saveList) {
      list.add(r);
    }
    
    table.refresh();
    loadMsg.set("Data loaded");
    tableTitle.set(farmID + ", " + yearInput + ", " + month);
  }
  /**
   * Method that assembles the table to display the data.
   */
  private void buildTable() {
    table = new TableView<Row>();
    
    TableColumn<Row, Integer> dayCol = new TableColumn<Row, Integer>("Day");
    dayCol.setCellValueFactory(new PropertyValueFactory<Row, Integer>("rowValue"));
    
    TableColumn<Row, Integer> weightCol = new TableColumn<Row, Integer>("Weight");
    weightCol.setCellValueFactory(new PropertyValueFactory<Row, Integer>("weight"));
    table.setItems(list);
    
    table.getColumns().add(dayCol);
    table.getColumns().add(weightCol);
  }
  
  /**
   * Method that builds the cancel and save buttons for this window.
   * 
   * @return HBox containing the cancel and save button.
   */
  private HBox buildCancelSave() {
    
    Button cancel = new Button("Cancel");
    cancel.setOnAction(actionEvent -> {
      if(tableChanged) {
        list.clear();
        
        for(Row r : saveList)
          list.add(r);
        
        updateMsg.set("Data reverted");
        tableChanged = false;
      }
    });
    
    Button save = new Button("Save");
    save.setOnAction(actionEvent -> {
      if (tableChanged) {
        saveList.clear();
        
        for(Row r : list)
          saveList.add(r);
        
        updateMsg.set("Data saved");
        tableChanged = false;
      }
    });
    return new HBox(cancel, save);
  }
  
  /**
   * Method that updates an entry in the table as specified by
   * the respective text fields, will return if the entered
   * data does not match the required form and will inform the user.
   */
  private void updateEntry() {
    
    int changeDay;
    int newWeight;
    
    if (date == null) {
      updateMsg.set("Must load a table");
      return;
    }
    
    //Checks if input day is valid.
    try {  
      changeDay = Integer.parseInt(editDay.getText());
      int numDays = date.getActualMaximum(Calendar.DAY_OF_MONTH);
      if (changeDay <= 0)
        throw new NumberFormatException();
      if (changeDay > numDays) {
        updateMsg.set("Day cannot be greater than " + numDays);
        return;
      }
    } catch (NumberFormatException | NullPointerException e) {
      updateMsg.set("Day must be a positive integer");
      return;
    }
    
    //Checks if input weight is valid.
    try {
      newWeight = Integer.parseInt(editWeight.getText());
      if (newWeight < 0)
        throw new NullPointerException();
    } catch (NumberFormatException | NullPointerException e) {
      updateMsg.set("Weight must be a positive integer");
      return;
    }
    
    //Checks if update is required.
    if (list.get(changeDay - 1).getWeight() == newWeight) {
      updateMsg.set("Day " + changeDay + " is already set to " + newWeight);
    }
    
    //Updates the table.
    else {
      list.set(changeDay - 1, new Row(changeDay, newWeight));
      table.refresh();
      updateMsg.set("Day " + changeDay + " updated to " + newWeight);
      tableChanged = true;
    }
  }
  @Override
  public void showWindow(Stage stage, CheeseFactory factory) {
    super.showWindow(stage, factory);
    farmSelect.setItems(names);
    yearSelect.setItems(years);
  }
}

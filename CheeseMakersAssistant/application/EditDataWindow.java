package application;

import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.beans.property.SimpleIntegerProperty;
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
public class EditDataWindow{
  
  private static final int WINDOW_WIDTH = 600;
  private static final int WINDOW_HEIGHT = 500;
  
  private static final ObservableList<String> NAMES =
      FXCollections.observableArrayList("Farm 1", "Farm 2", "Farm 3", "Farm 4", "Farm 5");
  
  private static final ObservableList<String> YEARS = 
      FXCollections.observableArrayList("2016", "2017", "2018", "2019", "2020");

  private static final ObservableList<Month> MONTHS =
      FXCollections.observableArrayList(Month.values());
  
  Scene scene;
  Scene old;
  TableView<Row> table;
  ObservableList<Row> saveList;
  ObservableList<Row> list;
  ComboBox<String> farmSelect;
  ComboBox<String> yearSelect;
  ComboBox<Month> monthSelect;
  TextField editDay;
  TextField editWeight;
  SimpleStringProperty updateMsg;
  SimpleStringProperty loadMsg;
  SimpleStringProperty tableTitle;
  
  /**
   * Class that allows elements to be loaded into the table.
   * 
   * @author Adam
   *
   */
  public static class Row implements Comparable<Row>{
    
    GregorianCalendar date;
    SimpleIntegerProperty day;
    SimpleIntegerProperty weight;
    
    public Row(GregorianCalendar date, int weight) {
      this.date = date;
      this.day = new SimpleIntegerProperty(date.get(Calendar.DAY_OF_MONTH));
      this.weight = new SimpleIntegerProperty(weight);
    }
    public GregorianCalendar getDate() {
      return date;
    }
    
    public int getDay() {
      return day.get();
    }
      
    public int getWeight() {
      return weight.get();
    }
       
    public void setWeight(int newWeight) {
      this.weight.set(newWeight);;
    }
    @Override
    public int compareTo(Row other) {
      return this.getDay() - other.getDay();
    }
  }
  
  public EditDataWindow(Stage stage) {

    Button back = new Button("Back");
    back.setOnAction(actionEvent -> {
      stage.setScene(old);
    });
    
    Label sceneTitle = new Label("Edit Data");
    
    saveList = FXCollections.observableArrayList();
    list = FXCollections.observableArrayList();
    
    HBox tableID = buildTableID();
    
    Button generate = new Button("Load Table");
    generate.setOnAction(actionEvent -> {
      generateTable();
    });
    
    Label loadStatus = new Label();
    loadMsg = new SimpleStringProperty();
    loadStatus.textProperty().bind(loadMsg);
    
    Label loadTitle = new Label();
    tableTitle = new SimpleStringProperty();
    loadTitle.textProperty().bind(tableTitle);
    
    buildTable();

    HBox updatePrompt = buildUpdatePrompt();
    
    Button cancel = new Button("Cancel");
    cancel.setOnAction(actionEvent -> {
      list.clear();
      for(Row r : saveList)
        list.add(r);
    });
    
    Button save = new Button("Save");
    save.setOnAction(actionEvent -> {
      saveList.clear();
      for(Row r : list)
        saveList.add(r);
    });
    
    HBox top = new HBox(back, sceneTitle);
    HBox bottom = new HBox(cancel, save);
    VBox root = new VBox(top, 
                         tableID, 
                         new HBox(generate, loadStatus),
                         loadTitle,
                         table, 
                         updatePrompt, 
                         bottom);
    top.setSpacing(WINDOW_WIDTH / 3.0);
    bottom.setSpacing(WINDOW_WIDTH - 95.0);
    sceneTitle.setFont(new Font("System Regular", 30));
    
    scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
  }
  
  /**
   * Method that will allow this scene to be displayed.
   * 
   * @param stage that the scene will be displayed to.
   */
  public void showWindow(Stage stage) {
    old = stage.getScene();
    stage.setScene(scene);
  }
  
  /**
   * Mehod that will build the control element that will allow
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
    farmSelect = new ComboBox<String>(NAMES);
    farmSelect.setEditable(true);
    farmSelect.setPromptText("Insert Name");
    yearSelect = new ComboBox<String>(YEARS);
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
  private void generateTable() {
    
    String farmID = farmSelect.getValue();
    String yearInput = yearSelect.getValue();
    Month month = monthSelect.getValue();
    
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
    int year;
    try {
    year = Integer.parseInt(yearInput);
    } catch (NumberFormatException e) {
      loadMsg.set("Year must be an integer");
      return;
    }
    saveList.clear();
    saveList.addAll(
        new Row(new GregorianCalendar(year, month.getValue(), 1), 100),
        new Row(new GregorianCalendar(year, month.getValue(), 2), 500),
        new Row(new GregorianCalendar(year, month.getValue(), 9), 700)
        );
    
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
    dayCol.setCellValueFactory(new PropertyValueFactory<>("day"));
    dayCol.setEditable(true);
    
    TableColumn<Row, Integer> weightCol = new TableColumn<Row, Integer>("Weight");
    weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
    weightCol.setEditable(true);
    table.setItems(list);
    
    table.getColumns().addAll(dayCol, weightCol);
    table.setEditable(true);
  }
  /**
   * Method that udpates an entry in the table as specified by
   * the respective text fields, will return if the entered
   * data does not match the required form and will inform the user.
   */
  private void updateEntry() {
    int changeDay;
    int newWeight;
    GregorianCalendar date = null;
    try {
      date = list.get(0).getDate();
    } catch (IndexOutOfBoundsException e) {
      updateMsg.set("Must load a table");
      return;
    }
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
    
    try {
      newWeight = Integer.parseInt(editWeight.getText());
      if (newWeight < 0)
        throw new NullPointerException();
    } catch (NumberFormatException | NullPointerException e) {
      updateMsg.set("Weight must be a positive integer");
      return;
    }
    
    int i = 0;
    for(Row r : list) {
      if (r.getDay() == changeDay) {
        list.set(i, new Row(r.getDate(), newWeight));
        table.refresh();
        updateMsg.set("");
        return;
      }
      if (r.getDay() > changeDay)
        break;
      i++;
    }
      list.add(new Row(
          new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), changeDay), 
          newWeight));
      FXCollections.sort(list);
      table.refresh();
      updateMsg.set("");
  }
}

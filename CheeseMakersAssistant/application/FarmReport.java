package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Builds and shows the Farm report window, showing data for an individual
 * farm for a given year.
 * 
 * @author maddie henry, Adam Szulczewski
 *
 */
public class FarmReport extends AssistantWindow{
  
  private TableView<Row> table;
  private ObservableList<Row> list;
  private ComboBox<String> farmSelect;
  private ComboBox<String> yearSelect;
  private SimpleStringProperty tableTitle;
  private SimpleStringProperty loadMsg;

  /**
   * Create a FarmReport screen
   * @param stage the stage to use
   */
  FarmReport(Stage stage){
    
    list = FXCollections.observableArrayList();
    
    Button backButton = new Button("Back");
    backButton.setOnAction(actionEvent -> {
      stage.setScene(old);
    });
    
    Label sceneTitle = new Label("Farm Report");
    
    HBox tableID = buildTableID();
    
    Label loadStatus = new Label();
    loadMsg = new SimpleStringProperty();
    loadStatus.textProperty().bind(loadMsg);
    
    Label loadTitle = new Label();
    tableTitle = new SimpleStringProperty();
    loadTitle.textProperty().bind(tableTitle);
    
    buildTable();
    
    Button saveToFile = new Button("Save to File");
    saveToFile.setOnAction(actionEvent -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save File");
      File file = fileChooser.showSaveDialog(stage);
      if (file != null) {
        CSVWriter writer = null;
        try {
          writer = new CSVWriter(file);
          writer.writeRow(new String[]{
                  "month",
                  "weight",
                  "percent"
          });
          for(Row row : list) {
            writer.writeRow(new String[]{
                    row.getRowName(),
                    Integer.toString(row.getWeight()),
                    Integer.toString(row.getRowValue())
            });
          }
          writer.close();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    });
    
    HBox top = new HBox(backButton, sceneTitle);
    VBox root = new VBox(top,
            tableID,
            loadStatus,
            loadTitle,
            table,
            saveToFile
    );
    
    root.setSpacing(5.0);
    top.setSpacing(WINDOW_WIDTH / 3.0);
    sceneTitle.setFont(new Font("System Regular", 30));
    tableID.setSpacing(5.0);
    scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    
  }
  
  private void buildTable() {
    table = new TableView<Row>();
    
    TableColumn<Row, String> monthCol = new TableColumn<Row, String>("Month");
    monthCol.setCellValueFactory(new PropertyValueFactory<>("rowName"));
    
    TableColumn<Row, Integer> totalWeightCol = new TableColumn<Row, Integer>("Weight");
    totalWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
    
    TableColumn<Row, Integer> percentWeightCol = new TableColumn<Row, Integer>("Percent");
    percentWeightCol.setCellValueFactory(new PropertyValueFactory<>("rowValue"));
    TableColumn<Row, Integer> minWeightCol = new TableColumn<Row, Integer>("Min");
    minWeightCol.setCellValueFactory(new PropertyValueFactory<>("min"));
    TableColumn<Row, Integer> maxWeightCol = new TableColumn<Row, Integer>("Max");
    maxWeightCol.setCellValueFactory(new PropertyValueFactory<>("max"));
    TableColumn<Row, Integer> avgWeightCol = new TableColumn<Row, Integer>("Average");
    avgWeightCol.setCellValueFactory(new PropertyValueFactory<>("avg"));
    
    table.setItems(list);
    
    table.getColumns().addAll(monthCol, totalWeightCol, percentWeightCol, minWeightCol, maxWeightCol, avgWeightCol);
  }
  
  private HBox buildTableID() {
    farmSelect = new ComboBox<>(names);
    farmSelect.setPromptText("Name");
    farmSelect.valueProperty().addListener(observable -> {
      loadData();
    });
    yearSelect = new ComboBox<>(years);
    yearSelect.setPromptText("Year");
    yearSelect.setPrefWidth(100);
    yearSelect.valueProperty().addListener(observable -> {
      loadData();
    });

    
    return new HBox(
            new VBox(new Label("Farm ID"), farmSelect), 
            new VBox(new Label("Year"), yearSelect)
    );
  }
  
  /**
   * Method that will load the requested data for the table.
   */
  private void loadData() {
    
    int year;
    String farm = farmSelect.getValue();
    
    if (farm == null) {
      loadMsg.set("Must choose a farm");
      return;
    }
    
    try {
    year = Integer.parseInt(yearSelect.getValue());
    } catch (NumberFormatException e) {
      loadMsg.set("Must select a year");
      return;
    }
    list.clear();
    
    // Loads the data for each month into a row
    for(Month m : MONTHS) {
      
      GregorianCalendar startDate = new GregorianCalendar(year, m.getValue() - 1, 1);
      GregorianCalendar endDate = (GregorianCalendar) startDate.clone();
      endDate.roll(Calendar.DAY_OF_MONTH, -1);
      
      List<Integer> range = factory.getFarm(farm).forRange(startDate, endDate);
      int total = Stats.sum(range);
      int min = Stats.min(range.stream().map(x -> x).collect(Collectors.toList()));
      int max = Stats.max(range.stream().map(x -> x).collect(Collectors.toList()));
      int avg = Stats.avg(range.stream().map(x -> x).collect(Collectors.toList()));
      
      list.add(new Row(
                       m.toString().substring(0, 3), 
                       total, 
                       100 * total / factory.getTotal(startDate, endDate),min,max,avg));
    }
    table.refresh();
    loadMsg.set("Data loaded");
    List<Integer> weights = list.stream().map(x -> x.getWeight()).collect(Collectors.toList());
    String statsString = "Min: " + Stats.min(weights) + ", Avg: " + Stats.avg(weights) + ", Max: " + Stats.max(weights);
    tableTitle.set(farm + ", " + year + " | " + statsString);
  }
  
  /**
   * Method that will allow this scene to be displayed.
   * 
   * @param stage that the scene will be displayed to.
   * @param factory Reference to the CheeseFactory used by this program
   */
  @Override
  public void showWindow(Stage stage, CheeseFactory man) {
    super.showWindow(stage, man);
    farmSelect.setItems(names);
    yearSelect.setItems(years);
  }
}

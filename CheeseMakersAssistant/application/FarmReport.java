package application;

import java.time.Month;

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
  
  FarmReport(Stage stage){
    
    list = FXCollections.observableArrayList();
    
    Button backButton = new Button("Back");
    backButton.setOnAction(actionEvent -> {
      stage.setScene(old);
    });
    
    Label sceneTitle = new Label("Farm Report");
    
    HBox tableID = buildTableID();
    
    Button loadButton = new Button("Generate Report");
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
    
    HBox loadBox = new HBox(loadButton, loadStatus);
    HBox top = new HBox(backButton, sceneTitle);
    VBox root = new VBox(top,
                         tableID, 
                         loadBox,
                         loadTitle,
                         table);
    
    root.setSpacing(5.0);
    loadBox.setSpacing(5.0);
    top.setSpacing(WINDOW_WIDTH / 3.0);
    sceneTitle.setFont(new Font("System Regular", 30));
    tableID.setSpacing(5.0);
    scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    
  }
  
  private void buildTable() {
    table = new TableView<Row>();
    
    TableColumn<Row, String> monthCol = new TableColumn<Row, String>("Month");
    monthCol.setCellValueFactory(new PropertyValueFactory<>("rowName"));
    
    TableColumn<Row, Integer> totalWeightCol = new TableColumn<Row, Integer>("Total Weight");
    totalWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
    
    TableColumn<Row, Integer> percentWeightCol = new TableColumn<Row, Integer>("Percent Weight");
    percentWeightCol.setCellValueFactory(new PropertyValueFactory<>("rowValue"));
    
    table.setItems(list);
    
    table.getColumns().add(monthCol);
    table.getColumns().add(totalWeightCol);
    table.getColumns().add(percentWeightCol);
  }
  
  private HBox buildTableID() {
    farmSelect = new ComboBox<String>(NAMES);
    farmSelect.setEditable(true);
    farmSelect.setPromptText("Insert Name");
    yearSelect = new ComboBox<String>(YEARS);
    yearSelect.setEditable(true);
    yearSelect.setPromptText("Insert Year");
    yearSelect.setPrefWidth(100);

    
    return new HBox(new VBox(new Label("Farm ID"), farmSelect), 
                    new VBox(new Label("Year"), yearSelect));
  }
  
  /**
   * Method that will load the requested data for the table.
   */
  private void loadData() {
    
    int year;
    String farm = farmSelect.getValue();
    
    if (farm == null || farm.equals("")) {
      loadMsg.set("Must choose a farm");
      return;
    }
    
    try {
    year = Integer.parseInt(yearSelect.getValue());
    } catch (NumberFormatException e) {
      loadMsg.set("Year must be a positive integer");
      return;
    }
    list.clear();
    
    //Simulates loading data into list.
    int value = farm.hashCode() + year;
    for(Month m : MONTHS) {
      list.add(new Row(m, 1000 + value * m.getValue() % 1000, 45 + value * m.getValue() % 45));
    }
    table.refresh();
    loadMsg.set("Data loaded");
    tableTitle.set(farm + ", " + year);
  }
}

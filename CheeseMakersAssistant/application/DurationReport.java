package application;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Month;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DurationReport extends AssistantWindow{
  
  DurationReport(Stage stage){
    old = stage.getScene();
    BorderPane root = new BorderPane();
    Button backButton = new Button("Back");
    backButton.setOnAction(actionEvent -> {
      stage.setScene(old);
    });
    root.setTop(new Label("Duration Reports"));
    root.setLeft(backButton);

    final ToggleGroup group = new ToggleGroup();

    RadioButton rb1 = new RadioButton("Yearly");
    rb1.setToggleGroup(group);
    rb1.setSelected(true);

    RadioButton rb2 = new RadioButton("Monthly");
    rb2.setToggleGroup(group);

    RadioButton rb3 = new RadioButton("Date Range");
    rb3.setToggleGroup(group);
    
    HBox radioHBox = new HBox(rb1, rb2, rb3);
    
    ChoiceBox<String> yearChoiceBox = new ChoiceBox<String>(YEARS);
    HBox yearHBox = new HBox(new Label("Year"), yearChoiceBox);
    ChoiceBox<Month> monthChoiceBox = new ChoiceBox<Month>(MONTHS);
    HBox monthHBox = new HBox(new Label("Month"), monthChoiceBox);
    ChoiceBox<Integer> dayChoiceBox = new ChoiceBox<Integer>(FXCollections.observableList(IntStream.rangeClosed(1, 31).boxed().collect(Collectors.toList())));
    HBox dayHBox = new HBox(new Label("Day"), dayChoiceBox);

    TableView<Row> table = new TableView<Row>();
    
    TableColumn<Row, String> farmIdCol = new TableColumn<Row, String>("Farm ID");
    farmIdCol.setCellValueFactory(new PropertyValueFactory<>("rowName"));
    TableColumn<Row, Integer> totalWeightCol = new TableColumn<Row, Integer>("Total Weight");
    totalWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
    TableColumn<Row, Integer> percentWeightCol = new TableColumn<Row, Integer>("Percent Weight");
    percentWeightCol.setCellValueFactory(new PropertyValueFactory<>("rowValue"));
    
    table.getColumns().add(farmIdCol);
    table.getColumns().add(totalWeightCol);
    table.getColumns().add(percentWeightCol);
    
    table.setItems(FXCollections.observableArrayList(
            new Row("Farm ID 1", 10, 15),
            new Row("Farm ID 2", 20, 30),
            new Row("Farm ID 2", 40, 60)
    ));
    
    VBox vbox = new VBox(radioHBox, yearHBox, monthHBox, dayHBox, table);
    root.setCenter(vbox);
    scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
  }
}

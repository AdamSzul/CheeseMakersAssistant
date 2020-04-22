package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Builds and shows the Farm report window, showing data for an individual
 * farm for a given year.
 * 
 * @author maddie henry
 *
 */
public class FarmReport {

	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 500;

	/**
	 * Stores information for each row of data to be loaded into the table
	 *
	 */
	public static class Row {
		SimpleStringProperty farmId;
		SimpleIntegerProperty totalWeight;
		SimpleIntegerProperty percentWeight;

		public Row(String farmId, Integer totalWeight, Integer percentWeight) {
			this.farmId = new SimpleStringProperty(farmId);
			this.totalWeight = new SimpleIntegerProperty(totalWeight);
			this.percentWeight = new SimpleIntegerProperty(percentWeight);
		}

		public int getPercentWeight() {
			return percentWeight.get();
		}

		public int getTotalWeight() {
			return totalWeight.get();
		}

		public String getFarmId() {
			return farmId.get();
		}
	}

	FarmReport(Stage stage) {
		Scene old = stage.getScene();
		BorderPane root = new BorderPane();
		Button backButton = new Button("Back");
		backButton.setOnAction(actionEvent -> {
			stage.setScene(old);
		});
		root.setTop(new Label("Farm Report"));
		root.setLeft(backButton);

		final ToggleGroup group = new ToggleGroup();


		ChoiceBox<?> yearChoiceBox = new ChoiceBox<Object>(
				FXCollections.observableArrayList("2016", "2017", "2018", "2019", "2020"));
		HBox yearHBox = new HBox(new Label("Year"), yearChoiceBox);
		
		ChoiceBox<?> farmIDChoiceBox = new ChoiceBox<Object>(
				FXCollections.observableArrayList("FarmID 1", "FarmID 2", "FarmID 3", "FarmID 4", "FarmID 5"));
		HBox farmIDHBox = new HBox(new Label("Farm ID"), farmIDChoiceBox);

		
		TableView<application.FarmReport.Row> table = new TableView<Row>();

		TableColumn<application.FarmReport.Row, ?> monthCol = new TableColumn<Row, String>("Month");
		monthCol.setCellValueFactory(new PropertyValueFactory<>("farmId"));
		TableColumn<application.FarmReport.Row, ?> totalWeightCol = new TableColumn<Row, Integer>("Total Weight");
		totalWeightCol.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
		TableColumn<application.FarmReport.Row, ?> percentWeightCol = new TableColumn<Row, Integer>("Percent Weight");
		percentWeightCol.setCellValueFactory(new PropertyValueFactory<>("percentWeight"));
		table.getColumns().addAll(monthCol, totalWeightCol, percentWeightCol);

		table.setItems(FXCollections.observableArrayList(new Row("January", 10, 15), new Row("February", 20, 30),
				new Row("March", 40, 60), new Row("April", 20, 30), new Row("May", 20, 30),
				new Row("June", 20, 30), new Row("July", 20, 30), new Row("August", 20, 30), 
				new Row("September", 20, 30), new Row("October", 20, 30), new Row("November", 20, 30),
				new Row("December", 20, 30)));
		
		
		VBox vbox = new VBox(yearHBox, farmIDHBox, table);
		vbox.setSpacing(10);
		root.setCenter(vbox);

		stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
	}

}

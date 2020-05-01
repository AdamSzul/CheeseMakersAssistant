package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.Month;
import java.time.YearMonth;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The duration report
 *
 * @authors Michael, Adam
 */
public class DurationReport extends AssistantWindow {

  Report report = Report.RANGE;

  SimpleStringProperty loadMsg;
  SimpleStringProperty tableTitle;
  private TableView<Row> table;
  private ObservableList<Row> list;
  private ChoiceBox<String> yearChoiceBox;
  private ChoiceBox<Month> monthChoiceBox;
  private ChoiceBox<String> endYearChoiceBox;
  private ChoiceBox<Month> endMonthChoiceBox;
  private TextField dayStart;
  private TextField dayEnd;

  /**
   * Create duration report
   *
   * @param stage the stage
   */
  DurationReport(Stage stage) {

    list = FXCollections.observableArrayList();

    Button backButton = new Button("Back");
    backButton.setOnAction(actionEvent -> {
      stage.setScene(old);
    });


    Label title = new Label("Duration Report");
    title.setFont(new Font("System Regular", 30));
    HBox top = new HBox(backButton, title);

    final ToggleGroup group = new ToggleGroup();

    RadioButton rb1 = new RadioButton("Yearly");
    rb1.setToggleGroup(group);

    RadioButton rb2 = new RadioButton("Monthly");
    rb2.setToggleGroup(group);

    RadioButton rb3 = new RadioButton("Date Range");
    rb3.setToggleGroup(group);
    rb3.setSelected(true);


    HBox radioHBox = new HBox(rb1, rb2, rb3);

    yearChoiceBox = new ChoiceBox<String>(years);
    yearChoiceBox.valueProperty().addListener(observable -> {
      loadData();
    });
    HBox yearHBox = new HBox(new Label("Year"), yearChoiceBox);
    monthChoiceBox = new ChoiceBox<Month>(MONTHS);
    monthChoiceBox.valueProperty().addListener(observable -> {
      loadData();
    });
    HBox monthHBox = new HBox(new Label("Month"), monthChoiceBox);
    dayStart = new TextField();
    dayStart.setPrefWidth(40.0);
    dayStart.textProperty().addListener(observable -> {
      loadData();
    });

    HBox dayHBox = new HBox(new Label("Day"), dayStart);

    dayEnd = new TextField();
    dayEnd.setPrefWidth(40.0);
    dayEnd.textProperty().addListener(observable -> {
      loadData();
    });
    HBox endDayHBox = new HBox(new Label("Day"), dayEnd);

    endYearChoiceBox = new ChoiceBox<String>(years);
    endYearChoiceBox.valueProperty().addListener(observable -> {
      loadData();
    });
    HBox endYearHBox = new HBox(new Label("Year"), endYearChoiceBox);

    endMonthChoiceBox = new ChoiceBox<Month>(MONTHS);
    endMonthChoiceBox.valueProperty().addListener(observable -> {
      loadData();
    });
    HBox endMonthHBox = new HBox(new Label("Month"), endMonthChoiceBox);

    buildTable();

    Label startLabel = new Label("Start");
    Label endLabel = new Label("End");
    VBox startVBox = new VBox(startLabel, yearHBox, monthHBox, dayHBox);
    VBox endVBox = new VBox(endLabel, endYearHBox, endMonthHBox, endDayHBox);

    Label loadStatus = new Label();
    loadMsg = new SimpleStringProperty();
    loadStatus.textProperty().bind(loadMsg);

    Label loadTitle = new Label();
    tableTitle = new SimpleStringProperty();
    loadTitle.textProperty().bind(tableTitle);

    group.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
      switch (((RadioButton) newVal).getText()) {
        case "Yearly":
          monthHBox.setVisible(false);
          startLabel.setVisible(false);
          dayHBox.setVisible(false);
          endVBox.setVisible(false);
          report = Report.YEAR;
          break;
        case "Monthly":
          monthHBox.setVisible(true);
          startLabel.setVisible(false);
          dayHBox.setVisible(false);
          endVBox.setVisible(false);
          report = Report.MONTH;
          break;
        case "Date Range":
          monthHBox.setVisible(true);
          startLabel.setVisible(true);
          dayHBox.setVisible(true);
          endVBox.setVisible(true);
          report = Report.RANGE;
          break;
      }
      loadData();
    });

    Button saveToFile = new Button("Save to File");
    saveToFile.setOnAction(actionEvent -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save File");
      File file = fileChooser.showSaveDialog(stage);
      if (file != null) {
        /*try {
          factory.write(file); // FIX this
        } catch (IOException e) {
          e.printStackTrace();
        }*/
      }
    });

    VBox vbox = new VBox(
            radioHBox,
            new HBox(startVBox, endVBox),
            loadStatus,
            loadTitle,
            table
    );
    scene = new Scene(new VBox(top, vbox, saveToFile), WINDOW_WIDTH, WINDOW_HEIGHT);
  }

  /**
   * Construct the table
   */
  private void buildTable() {

    table = new TableView<Row>();

    TableColumn<Row, String> farmIdCol = new TableColumn<Row, String>("Farm ID");
    farmIdCol.setCellValueFactory(new PropertyValueFactory<>("rowName"));
    TableColumn<Row, Integer> totalWeightCol = new TableColumn<Row, Integer>("Weight");
    totalWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
    TableColumn<Row, Integer> percentWeightCol = new TableColumn<Row, Integer>("Percent");
    percentWeightCol.setCellValueFactory(new PropertyValueFactory<>("rowValue"));

    table.getColumns().add(farmIdCol);
    table.getColumns().add(totalWeightCol);
    table.getColumns().add(percentWeightCol);

    table.setItems(list);
  }

  /**
   * Load data into the table
   */
  private void loadData() {
    int startYear = 0;
    int endYear = 0;
    YearMonth endDate;
    YearMonth startDate;
    Month startMonth = Month.JANUARY;
    Month endMonth = Month.DECEMBER;
    int startDay = 1;
    int endDay = 31;
    switch (report) {
      case RANGE:
        try {
          startYear = Integer.parseInt(yearChoiceBox.getValue());
          endYear = Integer.parseInt(endYearChoiceBox.getValue());
          startDay = Integer.parseInt(dayStart.getText());
          endDay = Integer.parseInt(dayEnd.getText());
        } catch (NumberFormatException e) {
          loadMsg.set("Years and Days must be integers");
          return;
        }
        startMonth = monthChoiceBox.getValue();
        endMonth = endMonthChoiceBox.getValue();
        if (endMonth == null || startMonth == null) {
          loadMsg.set("Must select a start month and an end month");
          return;
        }
        startDate = YearMonth.of(startYear, startMonth.getValue());
        endDate = YearMonth.of(endYear, endMonth.getValue());

        if (startDay <= 0 || endDay <= 0) {
          loadMsg.set("Days must be positive integers");
          return;
        }

        if (startDay > startDate.lengthOfMonth()) {
          loadMsg.set("Start day cannot be greater than " + startDate.lengthOfMonth());
          return;
        }

        if (endDay > endDate.lengthOfMonth()) {
          loadMsg.set("End day cannot be greater than " + endDate.lengthOfMonth());
          return;
        }

        if (endYear < startYear) {
          loadMsg.set("End date must not come before start date");
          return;
        }
        if (endYear == startYear) {
          if (endMonth.getValue() < startMonth.getValue()) {
            loadMsg.set("End date must not come before start date");
            return;
          }
          if (endMonth.getValue() == startMonth.getValue()) {
            if (endDay < startDay) {
              loadMsg.set("End date must not come before start date");
              return;
            }
          }
        }

        tableTitle.set(String.format("Date Range Report from %d/%d/%d to %d/%d/%d",
                startDay, startMonth.getValue(), startYear,
                endDay, endMonth.getValue(), endYear));
        break;

      case MONTH:
        try {
          startYear = Integer.parseInt(yearChoiceBox.getValue());
          endYear = startYear;
        } catch (NumberFormatException e) {
          loadMsg.set("Year must be an integer");
          return;
        }
        startMonth = monthChoiceBox.getValue();
        if (startMonth == null) {
          loadMsg.set("Must select a month");
          return;
        }
        endMonth = startMonth;
        endDate = YearMonth.of(endYear, endMonth.getValue());
        endDay = endDate.lengthOfMonth();
        tableTitle.set("Monthly Report for " + startMonth + " " + startYear);
        break;

      case YEAR:
        try {
          startYear = Integer.parseInt(yearChoiceBox.getValue());
          endYear = startYear + 1;
        } catch (NumberFormatException e) {
          loadMsg.set("Year must be a positive integer");
          return;
        }
        tableTitle.set("Yearly Report for " + startYear);
    }

    GregorianCalendar start = new GregorianCalendar(startYear, startMonth.getValue() - 1, startDay);
    GregorianCalendar end = new GregorianCalendar(endYear, endMonth.getValue() - 1, endDay);
    list.clear();
    int total = factory.getTotal(start, end);
    for (String s : names) {
      int farmTotal = Stats.sum(factory.getFarm(s).forRange(start, end));
      list.add(new Row(s, farmTotal, 100 * farmTotal / total));
    }
    loadMsg.set("Data loaded");
    List<Integer> weights = list.stream().map(x -> x.getWeight()).collect(Collectors.toList());
    String statsString = "Min: " + Stats.min(weights) + ", Avg: " + Stats.avg(weights) + ", Max: " + Stats.max(weights);
    tableTitle.set(tableTitle.get() + " | " + statsString);
    table.refresh();
  }

  /**
   * Show the window
   *
   * @param stage   that the scene will be displayed to.
   * @param factory the factory
   */
  @Override
  public void showWindow(Stage stage, CheeseFactory factory) {
    super.showWindow(stage, factory);
    yearChoiceBox.setItems(years);
    endYearChoiceBox.setItems(years);
  }

  enum Report {YEAR, MONTH, RANGE}
}


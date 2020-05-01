package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author Michael
 */
public class Main extends Application {

  private static final int WINDOW_WIDTH = 600;
  private static final int WINDOW_HEIGHT = 500;
  private static final String APP_TITLE = "Milk Weights";

  private EditDataWindow editData;
  private DurationReport durationReport;
  private FarmReport farmReport;
  private CheeseFactory factory;

  /**
   * Main method
   *
   * @param args args
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Start method
   *
   * @param primaryStage stage
   */
  @Override
  public void start(Stage primaryStage) {

    factory = new CheeseFactory();

    editData = new EditDataWindow(primaryStage);
    durationReport = new DurationReport(primaryStage);
    farmReport = new FarmReport(primaryStage);

    Label title = new Label("Milk Weights");
    title.setFont(new Font("System Regular", 30));

    VBox vbox = new VBox();

    Button loadFromFileButton = new Button("Load from File");
    loadFromFileButton.setOnAction(actionEvent -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Load File");
      File file = fileChooser.showOpenDialog(primaryStage);
      if (file == null) {
        return;
      }
      try {
        //Reads data from file
        
        factory.read(file);
        AssistantWindow.setNames(factory.getNames());
        AssistantWindow.setYears(factory.getYears());
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Load Complete");
        a.setContentText("The file has been loaded.");
        a.show();
      } catch (Exception e) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error while Loading File");
        a.setContentText(e.getMessage());
        a.show();
      }
    });

    Button loadFromFolderButton = new Button("Load from Folder");
    loadFromFolderButton.setOnAction(actionEvent -> {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Load File");
      File directory = directoryChooser.showDialog(primaryStage);
      if (directory == null) {
        return;
      }
      try {
        for (File file : directory.listFiles()) {
          if (file.getName().endsWith(".csv")) {
            factory.read(file);
          }
        }

        AssistantWindow.setNames(factory.getNames());
        AssistantWindow.setYears(factory.getYears());
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Load Complete");
        a.setContentText("The folder had been loaded.");
        a.show();
      } catch (Exception e) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error while Loading Folder");
        a.setContentText(e.getMessage());
        a.show();
      }
    });

    Button editDataButton = new Button("Edit Data");
    editDataButton.setOnAction(actionEvent -> {
      editData.showWindow(primaryStage, factory);
    });

    Button farmReportButton = new Button("Farm Report");
    farmReportButton.setOnAction(actionEvent -> {
      farmReport.showWindow(primaryStage, factory);
    });

    Button durationReportButton = new Button("Duration Report");
    durationReportButton.setOnAction(actionEvent -> {
      durationReport.showWindow(primaryStage, factory);
    });

    Button exitWindowButton = new Button("Exit");
    exitWindowButton.setOnAction(actionEvent -> {
      Platform.exit();
    });

    vbox.getChildren().addAll(title, loadFromFileButton, loadFromFolderButton, editDataButton, farmReportButton, durationReportButton, exitWindowButton);
    for (Node node : vbox.getChildren()) {
      ((Control) node).setMinWidth(200);
    }
    vbox.setSpacing(1);

    //Formats the window
    Scene mainScene = new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT);
    vbox.setAlignment(Pos.CENTER);
    title.setAlignment(Pos.CENTER);

    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(mainScene);
    primaryStage.show();
    primaryStage.setResizable(false);
  }
}

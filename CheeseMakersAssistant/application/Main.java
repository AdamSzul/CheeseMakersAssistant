package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * @author Michael
 *
 */
public class Main extends Application {
  private List<String> args;
  
  private static final int WINDOW_WIDTH = 600;
  private static final int WINDOW_HEIGHT = 500;
  private static final String APP_TITLE = "Milk Weights";
  
  private EditDataWindow editData;
  private DurationReport durationReport;
  private FarmReport farmReport;
  private CheeseFactory factory;
  FileManager IOManager;

  /**
   * Start method
   * @param primaryStage stage
   */
  @Override
  public void start(Stage primaryStage) {
    
    factory = new CheeseFactory();
    IOManager = new FileManager(factory);
    BorderPane root = new BorderPane();

    editData = new EditDataWindow(primaryStage);
    durationReport = new DurationReport(primaryStage);
    farmReport = new FarmReport(primaryStage);
    
    root.setTop(new Label("Milk Weights"));

    VBox vbox = new VBox();
    
    Button loadFromFileButton = new Button("Load from File");
    loadFromFileButton.setOnAction(actionEvent -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Load File");
      File file = fileChooser.showOpenDialog(primaryStage);
      try {
        IOManager.read(file);
        AssistantWindow.setNames(factory.getNames());
        AssistantWindow.setYears(factory.getYears());
      } catch (FileNotFoundException e) {
        System.out.println("didn't find file");
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    
    Button editDataButton = new Button("Edit Data");
    editDataButton.setOnAction(actionEvent -> {
      editData.showWindow(primaryStage, factory, IOManager);
    });
    
    Button farmReportButton = new Button("Farm Report");
    farmReportButton.setOnAction(actionEvent -> {
      farmReport.showWindow(primaryStage, factory, IOManager);
    });
    
    Button durationReportButton = new Button("Duration Report");
    durationReportButton.setOnAction(actionEvent -> {
      durationReport.showWindow(primaryStage, factory, IOManager);
    });
    
    Button exitWindowButton = new Button("Exit");
    exitWindowButton.setOnAction(actionEvent -> {
    	Platform.exit();
    });
    
    vbox.getChildren().addAll(loadFromFileButton, editDataButton, farmReportButton, durationReportButton, exitWindowButton);
    root.setCenter(vbox);
    
    Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(mainScene);
    primaryStage.show();
    primaryStage.setResizable(false);
  }

  /**
   * Main method
   * @param args args
   */
  public static void main(String[] args) {
    launch(args);
  }
}

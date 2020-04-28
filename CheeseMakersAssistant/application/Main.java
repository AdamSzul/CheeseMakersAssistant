package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.application.Application;
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
  private Manager man;

  /**
   * Start method
   * @param primaryStage stage
   */
  @Override
  public void start(Stage primaryStage) {
    
    man = new Manager();
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
      fileChooser.showOpenDialog(primaryStage);
      try {
        CheeseReader reader = new CheeseReader(new File("test.txt"), man);
        reader.read();
        AssistantWindow.setNames(man.getNames());
        AssistantWindow.setYears(man.getYears());
      } catch (FileNotFoundException e) {
        System.out.println("didn't find file");
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    
    Button saveToFileButton = new Button("Save to File");
    saveToFileButton.setOnAction(actionEvent -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save File");
      fileChooser.showSaveDialog(primaryStage);
    });
    
    Button editDataButton = new Button("Edit Data");
    editDataButton.setOnAction(actionEvent -> {
      editData.showWindow(primaryStage, man);
    });
    
    Button farmReportButton = new Button("Farm Report");
    farmReportButton.setOnAction(actionEvent -> {
      farmReport.showWindow(primaryStage, man);
    });
    
    Button durationReportButton = new Button("Duration Report");
    durationReportButton.setOnAction(actionEvent -> {
      durationReport.showWindow(primaryStage, man);
    });
    
    Button exitWindowButton = new Button("Exit");
    exitWindowButton.setOnAction(actionEvent -> {
    	Platform.exit();
    });
    
    vbox.getChildren().addAll(loadFromFileButton, saveToFileButton, editDataButton, farmReportButton, durationReportButton, exitWindowButton);
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

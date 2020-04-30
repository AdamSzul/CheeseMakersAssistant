package application;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * @author Michael
 *
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
   * Start method
   * @param primaryStage stage
   */
  @Override
  public void start(Stage primaryStage) {
    
    factory = new CheeseFactory();
    BorderPane root = new BorderPane();

    editData = new EditDataWindow(primaryStage);
    durationReport = new DurationReport(primaryStage);
    farmReport = new FarmReport(primaryStage);
    
    Label title = new Label("Milk Weights");
    title.setFont(new Font("System Regular", 30));
    root.setTop(title);

    VBox vbox = new VBox();
    
    Button loadFromFileButton = new Button("Load from File");
    loadFromFileButton.setOnAction(actionEvent -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Load File");
      File file = fileChooser.showOpenDialog(primaryStage);
      
      try {
        factory.read(file);
        AssistantWindow.setNames(factory.getNames());
        AssistantWindow.setYears(factory.getYears());
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Load Complete");
        a.setContentText("The file had been loaded.");
        a.show();
      }catch (Exception e){
        e.printStackTrace();
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error while Loading File");
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

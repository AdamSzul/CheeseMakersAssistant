package application;

import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class is just a main method for testing purposes.
 * 
 * @author Adam
 *
 */
public class Main extends Application {
    // store any command-line arguments that were entered.
    // NOTE: this.getParameters().getRaw() will get these also
    private List<String> args;

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 500;
    private static final String APP_TITLE = "Hello World!";
    
    private EditDataWindow editData;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // save args example
        args = this.getParameters().getRaw();
        
        // Main layout is Border Pane example (top,left,center,right,bottom)
        editData = new EditDataWindow(primaryStage);
        BorderPane root = new BorderPane();

        // Add the vertical box to the center of the root pane
        root.setTop(new Label(APP_TITLE));
        Button next = new Button("Edit Data");
        next.setOnAction(actionEvent ->{
          editData.showWindow(primaryStage);
        });
        root.setCenter(next);
        Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Add the stuff and set the primary stage
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(mainScene);
            primaryStage.show();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
           launch(args);
    }
}
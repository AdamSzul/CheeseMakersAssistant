package application;

import java.time.Month;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Holds methods, inner classes, and variables that will be used
 * in the classes that will assemble their respective windows
 * 
 * @author Adam
 *
 */
public abstract class AssistantWindow {

  protected static final int WINDOW_WIDTH = 600;
  protected static final int WINDOW_HEIGHT = 500;
  
  protected static final ObservableList<String> NAMES =
      FXCollections.observableArrayList("Farm 1", "Farm 2", "Farm 3", "Farm 4", "Farm 5");
  
  protected static final ObservableList<String> YEARS = 
      FXCollections.observableArrayList("2016", "2017", "2018", "2019", "2020");
  
  protected static final ObservableList<Month> MONTHS =
      FXCollections.observableArrayList(Month.values());
  
  protected Scene scene;
  protected Scene old;
  
  /**
   * Class that allows elements to be loaded into the table.
   * 
   * @author Adam
   *
   */
  protected static class Row {
    
    SimpleIntegerProperty rowValue;
    SimpleStringProperty rowName;
    SimpleIntegerProperty weight;
    
    /**
     * Constructor for rows of the Edit Data table
     * 
     * @param day
     * @param weight
     */
    public Row(int day, int weight) {
      this.rowValue = new SimpleIntegerProperty(day);
      this.weight = new SimpleIntegerProperty(weight);
    }
    
    /**
     * Constructor for the rows of the Farm Report table
     * 
     * @param month
     * @param weight
     * @param percentWeight
     */
    public Row(Month month, int weight, int percentWeight) {
      this.rowName = new SimpleStringProperty(month.toString());
      this.weight = new SimpleIntegerProperty(weight);
      this.rowValue = new SimpleIntegerProperty(percentWeight);
    }
    
    /**
     * Constructor for the rows of the Duration Report table
     * 
     * @param farmId
     * @param totalWeight
     * @param percentWeight
     */
    public Row(String farmId, int totalWeight, int percentWeight) {
      this.rowName = new SimpleStringProperty(farmId);
      this.weight = new SimpleIntegerProperty(totalWeight);
      this.rowValue = new SimpleIntegerProperty(percentWeight);
    }
    
    public int getRowValue() {
      return rowValue.get();
    }
      
    public int getWeight() {
      return weight.get();
    }
    
    public String getRowName() {
      return rowName.get();
    }
       
    public void setWeight(int newWeight) {
      this.weight.set(newWeight);;
    }
  }
  
  /**
   * Method that will allow this scene to be displayed.
   * 
   * @param stage that the scene will be displayed to.
   */
  public void showWindow(Stage stage) {
    old = stage.getScene();
    stage.setScene(scene);
  }
}

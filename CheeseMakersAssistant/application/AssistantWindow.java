package application;

import java.time.Month;
import java.util.ArrayList;

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
  
  protected static ObservableList<String> names =
      FXCollections.observableArrayList();
  
  protected static ObservableList<String> years = 
      FXCollections.observableArrayList();
  
  protected static final ObservableList<Month> MONTHS =
      FXCollections.observableArrayList(Month.values());
  
  protected Scene scene;
  protected Scene old;
  protected CheeseFactory factory;
  
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
    SimpleIntegerProperty min;
    SimpleIntegerProperty max;
    SimpleIntegerProperty avg;
    
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
     * @param min
     * @param max
     * @param avg
     */
    public Row(Month month, int weight, int percentWeight,
               int min, int max, int avg) 
    {
      this.rowName = new SimpleStringProperty(month.toString());
      this.weight = new SimpleIntegerProperty(weight);
      this.rowValue = new SimpleIntegerProperty(percentWeight);
      this.rowValue = new SimpleIntegerProperty(min);
      this.rowValue = new SimpleIntegerProperty(max);
      this.rowValue = new SimpleIntegerProperty(avg);
    }
    
    /**
     * Constructor for the rows of the Duration Report table
     * 
     * @param farmId
     * @param totalWeight
     * @param percentWeight
     * @param min
     * @param max
     * @param avg
     */
    public Row(String farmId, int totalWeight, int percentWeight,
               int min, int max, int avg) 
    {
      this.rowName = new SimpleStringProperty(farmId);
      this.weight = new SimpleIntegerProperty(totalWeight);
      this.rowValue = new SimpleIntegerProperty(percentWeight);
      this.min = new SimpleIntegerProperty(min);
      this.max = new SimpleIntegerProperty(max);
      this.avg = new SimpleIntegerProperty(avg);
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
    
    public int getMin() {
      return min.get();
    }
    
    public int getMax() {
      return max.get();
    }
    
    public int getAvg() {
      return avg.get();
    }
       
    public void setWeight(int newWeight) {
      this.weight.set(newWeight);;
    }
  }
  
  /**
   * Method that will allow this window to be displayed.
   * 
   * @param stage that the scene will be displayed to.
   * @param factory Reference to the CheeseFactory used by this program
   */
  public void showWindow(Stage stage, CheeseFactory factory) {
    this.factory = factory;
    old = stage.getScene();
    stage.setScene(scene);
  }
  
  /**
   * Method that will store the Farm names for each window to use
   * 
   * @param names
   */
  static void setNames(ArrayList<String> names) {
    AssistantWindow.names = FXCollections.observableArrayList(names);
  }
  
  /**
   * Method that will store the years for each window to use
   * 
   * @param years
   */
  static void setYears(ArrayList<String> years) {
    AssistantWindow.years = FXCollections.observableArrayList(years);
  }
}

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class CheeseReader {

  Scanner scnr;
  Manager man;
  
  String name;
  GregorianCalendar date;
  int weight;
  
  
  CheeseReader(File file, Manager man) throws FileNotFoundException{
    scnr = new Scanner(file);
    this.man = man;
  }
  
  public void read() throws Exception {
    String line = scnr.nextLine();
    String[] order = line.split(",");
    int i = 1;
    while(scnr.hasNext()) {
      line = scnr.nextLine();
      String[] data = line.split(",");
      lineParse(order[0], data[0]);
      lineParse(order[1], data[1]);
      lineParse(order[2], data[2]);
      man.insert(name, date, weight);
    }
  }
  
  private void lineParse(String order, String data) throws Exception {
    
    switch(order) {
    
    case "date":
      String[] dateString = data.split("-");
      date = new GregorianCalendar(Integer.parseInt(dateString[0]),
                                   Integer.parseInt(dateString[1]) - 1,
                                   Integer.parseInt(dateString[2]));
      break;
    
    case "farm_id":
      name = data;
      break;
    
    case "weight":
      weight = Integer.parseInt(data);
      break;
    
    default:
      throw new Exception();
    }
  }
  
  public void write() {
    
  }
}

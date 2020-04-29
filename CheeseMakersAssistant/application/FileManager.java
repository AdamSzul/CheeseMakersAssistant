package application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

  private CheeseFactory factory;
  private LinkedList<InsertObject> list;
  
  private class InsertObject{
    private String name;
    private GregorianCalendar date;
    private int weight;
  }
  
  FileManager(CheeseFactory factory) {
    this.factory = factory;
    list = new LinkedList<InsertObject>();
  }
  
  public void read(File file) throws Exception {
    Scanner scnr = new Scanner(file);
    String line = scnr.nextLine();
    String[] order = line.split(",");
    while(scnr.hasNext()) {
      InsertObject insert = new InsertObject();
      list.add(insert);
      line = scnr.nextLine();
      String[] data = line.split(",");
      lineParse(order[0], data[0], insert);
      lineParse(order[1], data[1], insert);
      lineParse(order[2], data[2], insert);
    }
    scnr.close();
    
    for(InsertObject i : list) {
      factory.insert(i.name, i.date, i.weight);
    }
  }
  
  private void lineParse(String order, String data, InsertObject insert) throws Exception {
    
    switch(order) {
    
    case "date":
      String[] dateString = data.split("-");
      insert.date = new GregorianCalendar(Integer.parseInt(dateString[0]),
                                   Integer.parseInt(dateString[1]) - 1,
                                   Integer.parseInt(dateString[2]));
      break;
    
    case "farm_id":
      insert.name = data;
      break;
    
    case "weight":
      insert.weight = Integer.parseInt(data);
      break;
    
    default:
      throw new Exception();
    }
  }
  
  public void write() {
    
  }
  
  public void write(File file, String title, List<String[]> list) throws IOException {
    PrintWriter writer = new PrintWriter(file);
    writer.write(title);
    for(String[] s : list) {
      writer.print("\n" + s[0] + "," + s[1] + "," + s[2]);
    }
    writer.close();
  }
}

package application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CheeseFactory {
  HashMap<String, Farm> farmMap;
  ArrayList<String> farmNames;
  ArrayList<String> years;
  
  public CheeseFactory() {
    farmMap = new HashMap<String, Farm>();
    farmNames = new ArrayList<String>();
    years = new ArrayList<String>();
  }
  
  public void insert(String name, GregorianCalendar date, int weight) {
    if (!farmMap.containsKey(name)) {
      Farm farm = new Farm();
      farm.insert(date, weight);
      farmMap.put(name, farm);
      farmNames.add(name);
    }
    else {
      farmMap.get(name).insert(date, weight);
    }
    String year = Integer.toString(date.get(Calendar.YEAR));
    if (!years.contains(year)) {
      years.add(year);
    }
  }
  
  public int get(String name, GregorianCalendar date) {
    if (farmMap.containsKey(name))
      return farmMap.get(name).get(date);
    else
      return 0;
  }
  
  public ArrayList<String> getNames() {
    return farmNames;
  }
  
  public ArrayList<String> getYears() {
    return years;
  }
  
  public int getTotalForFarm(String farm, GregorianCalendar startDate, GregorianCalendar endDate) {
    int total = 0;
    GregorianCalendar date = (GregorianCalendar) startDate.clone();
    while(date.compareTo(endDate) <= 0) {
      total += get(farm, date);
      date.add(Calendar.DAY_OF_MONTH, 1);
    }
    return total;
  }
  
  public int getTotal(GregorianCalendar startDate, GregorianCalendar endDate) {
    int total = 0;
    for(String s : farmNames) {
      total += getTotalForFarm(s, startDate, endDate);
    }
    if (total == 0)
      total++;
    return total;
  }
  
  public List<String[]> saveToFile(GregorianCalendar date) {
    LinkedList<String[]> printList = new LinkedList<String[]>();
    for(String farmID : farmNames) {
      for(int i = date.getMaximum(Calendar.DAY_OF_MONTH); i > 0; i--) {
        String[] line = new String[3];
        printList.add(line);
        line[0] = Integer.toString(date.get(Calendar.YEAR)) + "-" + 
                  Integer.toString(date.get(Calendar.MONTH) + 1) + "-" +
                  Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        line[1] = farmID;
        line[2] = Integer.toString(get(farmID,date));
        date.roll(Calendar.DAY_OF_MONTH, 1);
      }
    }
    return printList;
  }
}

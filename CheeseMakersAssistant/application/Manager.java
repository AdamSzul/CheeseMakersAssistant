package application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Manager {
  HashMap<String, Farm> farmMap;
  ArrayList<String> farmNames;
  ArrayList<String> years;
  
  public Manager() {
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
}

package application;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Stores and provides access to data for all the milk deliveries for 
 * a single farm in a HashMap
 *
 */
public class Farm {
  HashMap<GregorianCalendar, Integer> shipments;
  
  public Farm() {
    shipments = new HashMap<>();
  }
  
  /**
  * sets the given weight at the given date, replaces previous weight if date already exists
  * 
  * @param date
  * @param weight
  */
  public void insert(GregorianCalendar date, int weight){
    shipments.put(date, weight);
  }
  
  /**
  * gets the weight for given date, returns 0 if date doesn't exist
  * 
  * @param date
  * @return weight
  */
  public int get(GregorianCalendar date) {
    if (shipments.containsKey(date))
      return shipments.get(date);
    else
      return 0;
  }
  
  /**
  * gets the list of weights within the given range
  * returns empty list if none within range or if farm is empty
  * 
  * @param start
  * @param end
  * @return weights in range
  */
  public List<Integer> forRange (GregorianCalendar start, GregorianCalendar end) {
    List<Integer> inRange = new ArrayList<Integer>();
    Set<GregorianCalendar> allDates = shipments.keySet();
		
      for (GregorianCalendar d : allDates) {
        if (d.compareTo(start) >= 0 && d.compareTo(end) <= 0) { // checks if in range
	  inRange.add(shipments.get(d));
        }
      }
		
    return inRange;
  }
  
  /**
  * gets the list of weights for the month
  * 
  * @param year
  * @param month
  * @return weights for month
  */
  public List<Integer> forMonth (int year, int month) {
    List<Integer> inRange = new ArrayList<>();
    Set<GregorianCalendar> allDates = shipments.keySet();
		
    for (GregorianCalendar d : allDates) {
      if (d.get(GregorianCalendar.MONTH) == month && d.get(GregorianCalendar.YEAR) == year) { // checks if in range
	      inRange.add(shipments.get(d));
      }
    }
		
    return inRange;
  }
  
  /**
  * gets the list of weights for the year
  * 
  * @param year
  * @return list for year
  */
  public List<Integer> forYear (int year) {
    List<Integer> inRange = new ArrayList<Integer>();
    Set<GregorianCalendar> allDates = shipments.keySet();
		
    for (GregorianCalendar d : allDates) {
      if (d.get(GregorianCalendar.YEAR) == year)
        inRange.add(shipments.get(d));
    }
		
     return inRange;
  }
}

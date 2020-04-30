package application;

import java.util.*;


/**
 * Stores and provides access to data for all the milk deliveries for 
 * a single farm in a HashMap
 *
 */
public class Farm {
  private HashMap<GregorianCalendar, Integer> shipments;
  
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
  
  public HashMap<GregorianCalendar, Integer> getShipments() {
    return shipments;
  }

  /**
   * gets the list of weights within the given range
   * returns empty list if none within range or if farm is empty
   *
   * @param startDate the start date
   * @param endDate the end date
   * @return weights in range
   */
  public List<Integer> forRange(GregorianCalendar startDate, GregorianCalendar endDate) {
    List<Integer> inRange = new ArrayList<>();
    GregorianCalendar date = (GregorianCalendar) startDate.clone();
    while(date.compareTo(endDate) <= 0) {
      inRange.add(get(date));
      date.add(Calendar.DAY_OF_MONTH, 1);
    }
    return inRange;
  }
}

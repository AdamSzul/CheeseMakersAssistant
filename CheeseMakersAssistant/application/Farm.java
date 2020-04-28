package application;

import java.util.GregorianCalendar;
import java.util.HashMap;

public class Farm {
  HashMap<GregorianCalendar, Integer> shipments;
  
  public Farm() {
    shipments = new HashMap<GregorianCalendar, Integer>();
  }
  
  public void insert(GregorianCalendar date, int weight){
    shipments.put(date, weight);
  }
  
  public int get(GregorianCalendar date) {
    if (shipments.containsKey(date))
      return shipments.get(date);
    else
      return 0;
  }
}

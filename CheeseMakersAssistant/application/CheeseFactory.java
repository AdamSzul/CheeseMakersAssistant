package application;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Represents a cheese factory
 */
public class CheeseFactory {
  private HashMap<String, Farm> farmMap; // A CheeseFactory farms identified by Strings
  private ArrayList<String> years; // Years with data

  /**
   * Construct a cheese
   */
  public CheeseFactory() {
    farmMap = new HashMap<>();
    years = new ArrayList<>();
  }

  /**
   * Insert a row into a cheese factory
   * @param farmId the farmId
   * @param date the date
   * @param weight the weight
   */
  public void insert(String farmId, GregorianCalendar date, int weight) {
    if (!farmMap.containsKey(farmId)) {
      Farm farm = new Farm();
      farm.insert(date, weight);
      farmMap.put(farmId, farm);
    }
    else {
      farmMap.get(farmId).insert(date, weight);
    }
    String year = Integer.toString(date.get(Calendar.YEAR));
    if (!years.contains(year)) {
      years.add(year);
    }
  }

  /**
   * Get the names
   * @return the names
   */
  public ArrayList<String> getNames() {
    return new ArrayList(farmMap.keySet());
  }

  /**
   * Get the years
   * @return the years
   */
  public ArrayList<String> getYears() {
    return years;
  }

  /**
   * Get the total in a range
   * @param startDate the start date
   * @param endDate the end date
   * @return the total
   */
  public int getTotal(GregorianCalendar startDate, GregorianCalendar endDate) {
    int total = 0;
    for(String s : getNames()) {
      total += Stats.sum(getFarm(s).forRange(startDate, endDate));
    }
    if (total == 0)
      total++;
    return total;
  }

  /**
   * Read from file
   * @param file the file to read from
   * @throws Exception if an error occurs
   */
  public void read(File file) throws Exception {
    Scanner scanner = new Scanner(file);
    String line = scanner.nextLine();
    String[] names = line.split(",");
    int farmIdIndex = -1;
    int dateIndex = -1;
    int weightIndex = -1;
    for(int i = 0; i < names.length; i++){
      switch (names[i]){
        case "date": dateIndex = i; break;
        case "farm_id": farmIdIndex = i; break;
        case "weight": weightIndex = i; break;
        default: break;
      }
    }
    if(farmIdIndex == -1 || dateIndex == -1 || weightIndex == -1){
      throw new Exception("Required columns not present");
    }
    while(scanner.hasNext()) {
      line = scanner.nextLine();
      String[] data = line.split(",");
      String farmId = data[farmIdIndex];
      String dataString = data[dateIndex];
      String[] dateParts = dataString.split("-");
      GregorianCalendar date = new GregorianCalendar(
              Integer.parseInt(dateParts[0]),
              Integer.parseInt(dateParts[1]) - 1,
              Integer.parseInt(dateParts[2])
      );
      int weight = Integer.parseInt(data[weightIndex]);
      insert(farmId, date, weight);
    }
    scanner.close();
  }

  /**
   * Write the data to a file
   * @param file the file to write to
   * @throws IOException if the file can not be written to
   */
  public void write(File file) throws IOException {
    CSVWriter writer = new CSVWriter(file);
    writer.writeRow(new String[]{
            "farm_id",
            "date",
            "weight"
    });
    for(Map.Entry<String, Farm> farmEntry : farmMap.entrySet()) {
      String farmId = farmEntry.getKey();
      Farm farm = farmEntry.getValue();
      for(Map.Entry<GregorianCalendar, Integer> weightEntry : farm.getShipments().entrySet()){
        GregorianCalendar date = weightEntry.getKey();
        int weight = weightEntry.getValue();
        String dateString = date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH);
        String weightString = String.valueOf(weight);
        writer.writeRow(new String[]{
                farmId,
                dateString,
                weightString
        });
      }
    }
    writer.close();
  }
  
  public Farm getFarm(String key){
    return farmMap.get(key);
  }
}

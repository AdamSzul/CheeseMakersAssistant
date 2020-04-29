package application;

import java.util.ArrayList;
import java.util.List;

/**
 * computes the statistics including min, max, avg, sum on lists
 * 
 * @author maddie
 *
 */
public class Stats {
	
	/**
	 * returns the minimum value in the list.
	 * 
	 * @param values
	 * @return min
	 */
	static public int min(List<Integer> values) {
		if (values.isEmpty())
			return 0;
		
		int min = values.get(0);
		
		for (int v : values) {
			if (v < min)
				min = v;
		}
		
		return min;
	}
	
	/**
	 * returns the average of the values in the list.
	 * 
	 * @param values
	 * @return avg
	 */
	static public int avg (List<Integer> values) {
		if (values.isEmpty())
			return 0;
		
		int numElements = values.size();
		int total = sum(values);
		
		return total / numElements;
	}
	
	/**
	 * returns the max value in the list
	 * 
	 * @param values
	 * @return max
	 */
	static public int max (List<Integer> values) {
		if (values.isEmpty())
			return 0;
		
		int max = values.get(0);
		
		for (int v : values) {
			if (v > max)
				max = v;
		}
		
		return max;
	}
	
	/**
	 * returns the sum of all values in the list
	 * 
	 * @param values
	 * @return sum
	 */
	static public int sum (List<Integer> values) {
		if (values.isEmpty())
			return 0;
		
		int sum = 0;
		
		for (int v : values)
			sum += v;
		
		return sum;
	}

	/**
	 * returns a list containing the percent value of the sum of the integers
	 * for each element in the given list, rounded up to the nearest whole number
	 * 
	 * returns an empty list if values is empty
	 * 
	 * @param values
	 * @return percents
	 */
	static public List<Integer> percents (List<Integer> values){
		List<Integer> percents = new ArrayList<Integer>();
		
		int sum = sum(percents);
		for (int v : values) {
			int percent = v / sum;
			percents.add(percent);
		}
		
		return percents;
			
	}
}

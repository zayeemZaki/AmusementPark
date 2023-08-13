/*Zayeem_Zaki
 * EECS 2500 HW4
 * In this project we simulate the recreation park with different rides and riders.
 */

//Rider class in which we make an object for rider and do all the calculations related to rider
import java.util.ArrayList;
import java.util.List;

public class Rider
{
    private String name; // the rider's name
    private List<String> rides; // the rides they rode on

    /**
     * Creates a Rider with the specified name and initializes
     * an empty list of rides ridden on by the rider.
     * @param name Name of the rider
     */
    public Rider(String name)
    {
        this.name = name;
        this.rides = new ArrayList<String>();
    }
    
    //gets the name of the rider
    public String getName() {
    	return name;
    }
    
    //remembers the name of rides the rider took
    public void rememberRide(String ride) {
    	rides.add(ride);
    }
    
    //gets the number of rides ridden by rider
    public int getRideCount() {
    	return rides.size();
    }
    
    //this method prints out name and number of rides rider rode as a string
    public String toString() {
    	return getName() + " rode " + getRideCount() + " rides ";
    }



}
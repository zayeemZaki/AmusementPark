
/*AmusementPrakDemo Class
contains main method which creates an object of amusement park, opens, closes the park 
and also print the daily recap. 
*/

public class AmusementParkDemo {

	public static void main(String[] args) {
		
		final int CLOSING_TIME = 18000;
		final int START_LEAVING_TIME = 900;
		final int STATUS_TIME = 600;
		
		AmusementPark park = new AmusementPark(CLOSING_TIME, START_LEAVING_TIME, STATUS_TIME);
		
	    
        park.open();

        park.close();
        
        System.out.println("Here's how the day at the park went:");
        
        park.printDailyRecap();
		
	}
	
}

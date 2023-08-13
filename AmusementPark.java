/*This class creates an object for amusement park and performs all the functions related to amusement park
 * including clockTick prinStatus
 * */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AmusementPark
{
    private final int VISITORS_ARRIVAL_TIME = 60; // Seconds between a group of visitors arriving
    private final int VISITORS_MAX_SIZE = 10; // Maximum number of visitors in each group

    private final int CAROUSEL_LOAD = 40; // riders per load
    private final int CAROUSEL_TIME = 360; // seconds between loads

    private final int COASTER_LOAD = 12;  // riders per load
    private final int COASTER_TIME = 45; // seconds between loads

    private final int CHAIRLIFT_LOAD = 2; // riders per load
    private final int CHAIRLIFT_TIME = 15; // seconds between loads

    private final int WALKING_LOAD = Integer.MAX_VALUE; // unlimited riders per load
    private final int WALKING_TIME = 240; // seconds to "walk" between rides


    private int time;              // current clock time (in seconds)
    private int closingTime;       // time the simulation ends
    private int checkWaitTimes;    // how often to report waiting times
    private int startLeaving;      // seconds before closing time when visitors start leaving
    private int stopArrivingTime;  // no visitors arrive after this time
    private int totalVisitors;     // total number of visitors who arrived
    private int totalRides;        // total number of visitor rides

    // Rides in the park
    private Ride coaster;
    private Ride chairlift;
    private Ride carousel;
    private Ride walking; // walking between rides counts as a "ride"

    // List of all available rides
    private List<Ride> rides;

    // Random number generator, used for number of arriving visitors
    private Random random;

    /**
     * Creates an AmusementPark instance.
     * @param closingTime The "closing time" is the duration of the simulation in seconds.
     * @param startLeaving Number of seconds before closing time visitors start leaving.
     * @param showWaitTimes How often to display wait time status.
     */
    public AmusementPark(int closingTime, int startLeaving, int showWaitTimes)
    {
        this.time = 0;
        this.closingTime = closingTime;
        this.startLeaving = startLeaving;
        this.checkWaitTimes = showWaitTimes;

        this.stopArrivingTime = (closingTime - time) / 2;
        this.totalVisitors = 0;
        this.totalRides = 0;
        this.random = new Random();

        // Set up the rides with their parameters
        this.coaster = new Ride("Coaster", COASTER_LOAD, COASTER_TIME);
        this.chairlift = new Ride("Chairlift", CHAIRLIFT_LOAD, CHAIRLIFT_TIME);
        this.carousel = new Ride("Carousel", CAROUSEL_LOAD, CAROUSEL_TIME);
        this.walking = new Ride("Walking", WALKING_LOAD, WALKING_TIME);

        this.rides = new ArrayList<Ride>();
        this.rides.add(coaster);
        this.rides.add(chairlift);
        this.rides.add(carousel);
    }


    /**
     * Simulates each second of the event clock. Each second, events that might occur
     * are visitors arriving at or leaving the park, rides being available with riders
     * exiting the ride and walking to another ride, and more riders entering a line.
     */
    private void clockTick()
    {
        if (time % checkWaitTimes == 0)
        {
            printStatus(getWaitTimeString());
        }

        // Every so often a number of visitors arrive and head for the shortest wait
        if (time < stopArrivingTime && time % VISITORS_ARRIVAL_TIME == 0)
        {
            int howMany = random.nextInt(VISITORS_MAX_SIZE) + 1;
            for (int i = 0; i < howMany; i++)
            {
                Rider newRider = new Rider("Visitor-" + (totalVisitors));
                walking.enterLine(newRider);
            }
            totalVisitors += howMany;
        }

        // Send walking riders to next ride or exit the park
        if (walking.available(time))
        {
            for (Rider rider : walking.exitRide())
            {
                // If it is before the time when people start leaving
                // go to another ride, otherwise don't (leave the park)
                if (time < closingTime - startLeaving)
                {
                    getRideWithShortestWait().enterLine(rider);
                }
                else
                {
                    totalRides += rider.getRideCount();
                    // Uncomment to display info about each visitor leaving the park
                    //this.printStatus("Departing " + rider);
                }
            }
        }

        // Unload rides that finished and send riders walking
        for (Ride ride : rides)
        {
            // This ride just finished this second so it is available.
            // Exit the last group of riders and start them walking.
            // Then, load the next group on this ride.
            if (ride.available(time))
            {
                List<Rider> finished = ride.exitRide();
                for (Rider rider : finished)
                {
                    rider.rememberRide(ride.getName()); // remember this ride
                    walking.enterLine(rider);
                }
                ride.loadRide(time);
            }
        }

        // Make any recent walkers start walking
        walking.loadRide(time);

        // Another second ticks by
        time++;
    }
    
    //open method which opens the park and ticks the clock till time is equal to closing time
    public void open() {

    	printStatus("park is open!");
        
    	while(time != closingTime) {
       		clockTick();
    	}
    	if (time == closingTime) {
    		printStatus("Park is closing...");
    	}
    	
    }
    
    //closes the park and tick clock after closing till everyone leaves
    public void close() {
    	
    	while (getVisitorsInParkCount()!=0) {
    		clockTick();
    	}
    	if(getVisitorsInParkCount() ==0) {
    		printStatus("Park is Closed!");
    	}
    }
    
    //this method gets the ride with shortest wait
    public Ride getRideWithShortestWait() {
    	
        int min = Integer.MAX_VALUE;     //sets shortest time to Max_Value
        Ride shortest = null;			//sets the shortest ride to null 
        
        //for loop used to loop through every ride
        for(Ride ride : rides) {
            int waitTime = ride.getWaitTime(); //gets the waitTime for each ride
            //if waitTime is less than minimum sets the shortest to ride and changes minimum to WaitTime
            if (waitTime < min) {
                shortest = ride; 
                min = waitTime;
            } 
            else if (waitTime == min && Math.random() < 0.5) // this method returns random ride as shortest ride;used only if waitTimes are equal
                shortest = ride;  
 
        }
        
        return shortest;
    }
    	
    	
    	
//    	System.out.println(chairlift.getWaitTime());
//    	if(coaster.getWaitTime() < chairlift.getWaitTime()) {
//    		
//    		if(coaster.getWaitTime() < carousel.getWaitTime())
//    			return coaster;
//    	}
//    	else if (chairlift.getWaitTime() < coaster.getWaitTime()) {
//    		if(chairlift.getWaitTime() < carousel.getWaitTime())
//    			return chairlift;
//    	}
//    	return carousel;
//    	
//    }
    

   //getWaitTimeString method returns the waitTime and peopleinQueue for each ride in a well mannered string
    public String getWaitTimeString() {
    	
    	return "[Coaster " + coaster.getWaitTime()/60 + " mins " + "(" + coaster.getTotalPeopleInQueue() + ")]" 
	           	 + "[Chairlift " + chairlift.getWaitTime()/60 + " mins " + "(" + chairlift.getTotalPeopleInQueue() + ")]"
	           	 + "[Carousel " + carousel.getWaitTime()/60 + " mins " + "(" + carousel.getTotalPeopleInQueue() + ")]"
	        	 + "[walking " + walking.getWaitTime()/60 + " mins " + "(" + walking.getTotalPeopleInQueue() + ")]"
	        	 + "[Park " + getVisitorsInParkCount() + " ]";   
    	
    }
    
    //gets the total number of visitors in park
    public int getVisitorsInParkCount() {
    	
    	return  coaster.getTotalPeopleInQueue() + chairlift.getTotalPeopleInQueue() + carousel.getTotalPeopleInQueue() + walking.getTotalPeopleInQueue() ;
    }
    
    //prints out the day of park, how many rides, riders ...
    public void printDailyRecap() {
    	
    	System.out.println("\n" +"The park had " + totalVisitors + " who rode " + totalRides + " rides averaging about " + totalRides/totalVisitors + " rides each" + "\n");
    	
    	System.out.println("The Coaster had " + coaster.getTotalRiderCount() + " riders in " + coaster.getTotalLoadCount() + " loads averaging " + 
    			coaster.getTotalRiderCount()/coaster.getTotalLoadCount() + " riders each" + "\n");
 
    	System.out.println("The Chairlift had " + chairlift.getTotalRiderCount() + " riders in " + chairlift.getTotalLoadCount() + " loads averaging " + 
    			chairlift.getTotalRiderCount()/chairlift.getTotalLoadCount() + " riders each" + "\n");
  
    	System.out.println("The carousel had " + carousel.getTotalRiderCount() + " riders in " + carousel.getTotalLoadCount() + " loads averaging " + 
    			carousel.getTotalRiderCount()/carousel.getTotalLoadCount() + " riders each" + "\n");
    	
    }
    
    //printStatus method used to print out the status of park 
    public void printStatus(String string) {
    	
        int CurrentTimeH  = time / 3600;
        int CurrentTimeM = (time % 3600) / 60;
        int CurrentTimeS = (time % 60);

        String statusMessage = String.format("%02d", CurrentTimeH) + ":" + String.format("%02d", CurrentTimeM) + ":" + String.format("%02d", CurrentTimeS)  + "  " + string + "\n";        
        System.out.println(statusMessage);

        
    }
  
}
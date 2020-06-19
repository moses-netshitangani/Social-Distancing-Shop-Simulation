package socialDistanceShopSampleSolution;

// An import I need 
import java.util.concurrent.Semaphore; 

//class to keep track of people inside and outside and left shop

 /* 
         I used a semaphore and the synchronized identifier to ensure thread safety.
         The cap semaphore ensures that customers dont exceed the maximum number of people
         allowed inside the shop at one time. Also makes sure customers enter the shop in the order
         in which they arrive, by specifying the fair argument to true. I also put in place some restrictions
         on the setter methods so as to prevent data races since these are shared variables.
*/
public class PeopleCounter {
   private int peopleOutSide; //counter for people arrived but not yet in the building
   private int peopleInside; //people inside the shop
   private int peopleLeft; //people left the shop
   private int maxPeople; //maximum for lockdown rules
   
   private Semaphore cap; 
	
   PeopleCounter(int max) {
      peopleOutSide = 0;
      peopleInside = 0;
      peopleLeft = 0;
      maxPeople = max;                                                // I changed maxPeople from 0 to max. This represents the max number of people inside
      cap = new Semaphore(maxPeople, true);        // To ensure that not more than maxPeople can be inside at one time 
   }
		
	//getter
   public int getWaiting() {
      return peopleOutSide;
   }

	//getter
   public int getInside() {
      return peopleInside;
   }
	
	//getter
   public int getTotal() {
      return (peopleOutSide+peopleInside+peopleLeft);
   }

	//getter
   public int getLeft() {
      return peopleLeft;
   }
	
	//getter
   public int getMax() {
      return maxPeople;
   }
	
	//getter
   /*
         Customers decrease the number of permits of the semaphore, until maxPeople is reached, after which 
         they enter one after another exits the shop
   */ 
   public void personArrived() {
      try
      {
         peopleOutSide++;
         cap.acquire();
      }
      catch(InterruptedException e)
      {
         e.printStackTrace();
      }
   }
	
	//update counters for a person entering the shop
   /*    
         Used synchronized to prevent data races with the two shared variables peopleOutSide and peopleInside
   */
   public synchronized void personEntered() {
      peopleOutSide--;
      peopleInside++;
   }

	//update counters for a person exiting the shop
   /* 
         Called the release function to increment the value in the semaphore - allow the next customer in
   */
   public void personLeft() {
      cap.release();
      peopleInside--;
      peopleLeft++;
   }

	//reset - not really used
   public synchronized void resetScore() {
      peopleInside = 0;
      peopleOutSide = 0;
      peopleLeft = 0;
   }
}

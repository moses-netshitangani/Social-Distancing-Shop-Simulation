package socialDistanceShopSampleSolution;
// Me
import java.util.concurrent.Semaphore; 

//class to keep track of people inside and outside and left shop
public class PeopleCounter {
   private int peopleOutSide; //counter for people arrived but not yet in the building
   private int peopleInside; //people inside the shop
   private int peopleLeft; //people left the shop
   private int maxPeople; //maximum for lockdown rules
   // Me
   private Semaphore cap; 
   private Semaphore waiting;
	
   PeopleCounter(int max) {
   // I changed this from a zero to max. This represents the max number of people inside
      peopleOutSide = 0;
      peopleInside = 0;
      peopleLeft = 0;
      maxPeople = max;
         // Me
      cap = new Semaphore(maxPeople);
      waiting = new Semaphore(1);
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
   // Me. Added synchronized to prevent data races when customers enter and leave
   // Used semaphore to restrict entrance of customers to one at a time 
   public synchronized void personArrived() throws InterruptedException {
      waiting.acquire();
      peopleOutSide++;
         
   }
	
	//update counters for a person entering the shop
   // I used a barrier semaphore to limit the number of customers that can enter
   public void personEntered() throws InterruptedException {
      waiting.release(); 
      cap.acquire();
      peopleOutSide--;
      peopleInside++;
   }

	//update counters for a person exiting the shop
   // called the release function to increment the value in the semaphore - let another customer in
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

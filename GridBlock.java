package socialDistanceShopSampleSolution;

import java.util.concurrent.atomic.*;
// GridBlock class to represent a block in the shop.

/*
      What I changed in this class was to check first if the isOccupied variable was true before
      setting it to true in the get() method. I then used an atomic variable isOccupied to allow at most, 
      a single thread to occupy a block.
      
      The getter methods were not synchronized since we wish to maintin liveliness of the threads.
*/

public class GridBlock {
   // Here I replaced the standard isOccupied variable with an atomic variable 
   private AtomicBoolean isOccupied; 
   private final boolean isExit; 
   private final boolean isCheckoutCounter;
   private int [] coords; // the coordinate of the block.
   private int ID; 
   public static int classCounter=0;
    
	
   GridBlock(boolean exitBlock, boolean checkoutBlock) throws InterruptedException {
      isExit=exitBlock;
      isCheckoutCounter=checkoutBlock;
      isOccupied = new AtomicBoolean(false); 
      ID=classCounter;
      classCounter++;
   }
	
   GridBlock(int x, int y, boolean exitBlock, boolean refreshBlock) throws InterruptedException {
      this(exitBlock,refreshBlock);
      coords = new int [] {x,y};
   }
	
	//getter
   public  int getX() {
      return coords[0];}  
	
	//getter
   public  int getY() {
      return coords[1];}
	
	//for customer to move to a block
   /*
         The change I made here was to check first if isOccupied is already true, before setting It to true.
   */
   public boolean get() {
      if(this.isOccupied.get()== true)
      {
         return false;
      }
      else
      {
         this.isOccupied.set(true);
         return true;
      }   
   }
		
	//for customer to leave a block
   /*
         Does not need further protection since any block can only be occupied at most by a single customer
   */
   public  void release() {
      this.isOccupied.set(false); 
   }
	
	//getter
   /*
         Does not need further protection since this is a getter, same applies to subsequent methods
   */
   public boolean occupied() {
      return isOccupied.get(); 
   }
	
	//getter
   public  boolean isExit() {
      return isExit;	
   }

	//getter
   public  boolean isCheckoutCounter() {
      return isCheckoutCounter;
   }
	
	//getter
   public int getID() {
      return this.ID;}
}

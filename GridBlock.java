package socialDistanceShopSampleSolution;

// Me
import java.util.concurrent.atomic.*; 

// GridBlock class to represent a block in the shop.

public class GridBlock {
   // private boolean isOccupied;
      // I commented out the isOccupied variable. Attempting to replace it with an AtomicBoolean variable
      private AtomicBoolean isOccupied; 
   private final boolean isExit; 
   private final boolean isCheckoutCounter;
   private int [] coords; // the coordinate of the block.
   private int ID; 
	
   public static int classCounter=0;
	
   GridBlock(boolean exitBlock, boolean checkoutBlock) throws InterruptedException {
      isExit=exitBlock;
      isCheckoutCounter=checkoutBlock;
      // isOccupied= false;
         // Me again with one line
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
   //Me. I think we should check first to see if isOccupied is already true before we set it to true.!!!!!!!!!!!!!!!!!!!!!!!!
   // I really may be on to something. Should head back to ShopGrid line 80 if forgot what i was doing  
   public boolean get() throws InterruptedException {
      // isOccupied=true;
      // return true;
         // Me again with all lines below
         if(this.isOccupied.get() == true){
            return false;}
         else{
            this.isOccupied.set(true);
            return true;
            }
         
   }
		
	//for customer to leave a block
   public  void release() {
      // isOccupied =false;
         //Me again
         isOccupied.set(false); 
   }
	
	//getter
   public boolean occupied() {
      // return isOccupied;
         // Me again
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

//M. M. Kuttel
//Class to represent the shop, as a grid of gridblocks 
 
package socialDistanceShopSampleSolution;

// An import I need
import java.util.concurrent.Semaphore;

/*
      Used a binary semaphore to restrict entry through the entrance block.
      Used a check variable to allow only those customers who called acquire() to also call release() and
      let other customers enter.
      The getters do not need protection and this helps with the livenness of the simulation.
*/

public class ShopGrid {
   private GridBlock [][] Blocks;
   private final int x;
   private final int y;
   public final int checkout_y;
   private final static int minX =5;//minimum x dimension
   private final static int minY =5;//minimum y dimension
   private Semaphore entry = new Semaphore(1);     // A semaphore to allow only a single customer to enter at a time
   private int check = -1;                                                   // A check variable I use to control which Customers can call release() on the semaphore 
	
	
   ShopGrid() throws InterruptedException {
      this.x=20;
      this.y=20;
      this.checkout_y=y-3;
      Blocks = new GridBlock[x][y];
      int [] [] dfltExit= {{10,10}};
      this.initGrid(dfltExit);
   }
	
   ShopGrid(int x, int y, int [][] exitBlocks,int maxPeople) throws InterruptedException {
      if (x<minX) x=minX; //minimum x
      if (y<minY) y=minY; //minimum x
      this.x=x;
      this.y=y;
      this.checkout_y=y-3;
      Blocks = new GridBlock[x][y];
      this.initGrid(exitBlocks);
   }
	
   private  void initGrid(int [][] exitBlocks) throws InterruptedException {
      for (int i=0;i<x;i++) {
         for (int j=0;j<y;j++) {
            boolean exit=false;
            boolean checkout=false;
            for (int e=0;e<exitBlocks.length;e++)
               if ((i==exitBlocks[e][0])&&(j==exitBlocks[e][1])) 
                  exit=true;
            if (j==(y-3)) {
               checkout=true; 
            }//checkout is hardcoded two rows before  the end of the shop
            Blocks[i][j]=new GridBlock(i,j,exit,checkout);
         }
      }
   }
	
	//get max X for grid
   public  int getMaxX() {
      return x;
   }
	
	//get max y  for grid
   public int getMaxY() {
      return y;
   }

   public GridBlock whereEntrance() throws Exception{ //hard coded entrance
      return Blocks[getMaxX()/2][0];
   }

	//is a position a valid grid position?
   public  boolean inGrid(int i, int j) {
      if ((i>=x) || (j>=y) ||(i<0) || (j<0)) 
         return false;
      return true;
   }
	
	//called by customer when entering shop 
   /*
         Customer acquires a lock upon entering (only one customer can enter) and sets check to 0 - meaning they now have 
         permission to call release() once on the entry semaphore, allowing another customer to enter.
   */
   public GridBlock enterShop() throws Exception  {
      entry.acquire();
      check = 0;
      GridBlock entrance = whereEntrance();
      return entrance;
   }
		
	//called when customer wants to move to a location in the shop
   /*
         Only customers who have just entered are allowed to call release() on the semaphore, once.
   */
   public GridBlock move(GridBlock currentBlock,int step_x, int step_y) throws Exception {  
   	//try to move in 
      int c_x= currentBlock.getX();
      int c_y= currentBlock.getY();
   	
      int new_x = c_x+step_x; //new block x coordinates
      int new_y = c_y+step_y; // new block y  coordinates 
   	
   	//restrict i an j to grid
      if (!inGrid(new_x,new_y)) {
      	//Invalid move to outside shop - ignore
         return currentBlock;
      }
   
      if ((new_x==currentBlock.getX())&&(new_y==currentBlock.getY())) //not actually moving
         return currentBlock;
   	 
      GridBlock newBlock = Blocks[new_x][new_y];
      
   	//Customer cannot be allowed to move back to the entrance block - temporarily blocks other customers from entering
      if(newBlock.getID() == whereEntrance().getID())
      {
         return currentBlock;
      }
      
      if (newBlock.get())  {  //get successful because block not occupied 
         if(check == 0 && currentBlock.getID() == whereEntrance().getID())
         {
            entry.release();         // Customer has to let another customer enter as well
            check = 1;
         }
         currentBlock.release(); //must release current block
      }
      else {
         newBlock=currentBlock;
      	  //Block occupied - giving up
      }
         
      return newBlock;
   } 
	
	//called by customer to exit the shop
   public void leaveShop(GridBlock currentBlock)   {
      currentBlock.release();
   }

}


	

	


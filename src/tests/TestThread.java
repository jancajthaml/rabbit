package tests;
import java.util.Set;

import java.util.Iterator;

import thread.Task;

public class TestThread
{
	
	private static final int TIMES	= 20000;
	  private static final int MAX		= 50000;
  
  public static void main(String[] argv) {
    // first, get the JIT going

	  testThread(false);
//	  testTask(false);
    

    // then, do real timings
    
	  testThread(true);
	//  testTask(true);
  }

  public static void testThread(boolean output) {
    long start = System.currentTimeMillis();

    if (output) {
      System.gc(); System.gc();
    }
    long before = Runtime.getRuntime().totalMemory() -
      Runtime.getRuntime().freeMemory();
  
    Runnable task = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < MAX; i++);
        }
    };
    
    for (int i = 0; i < TIMES; i++)
        new Thread(task).start();

    if (output) {
      System.gc(); System.gc();
      long after = Runtime.getRuntime().totalMemory() -
        Runtime.getRuntime().freeMemory();
      System.out.println("Memory before: " + before);
      System.out.println("Memory after: " + after);
      System.out.println("Memory usage: " + (after - before));
    }


    // remove

    

    if (output)
      System.out.println("TIME: " + (System.currentTimeMillis() - start));
  }


  public static void testTask(boolean output) {
    long start = System.currentTimeMillis();

    if (output) {
      System.gc(); System.gc();
    }
    long before = Runtime.getRuntime().totalMemory() -
      Runtime.getRuntime().freeMemory();
    
    
    for (int i = 0; i < TIMES; i++){
    	new Task() {
        	int i=0;

    		@Override
    		public void callback() {
    			i++;
    		}

    		@Override
    		public boolean next() {

    			return i==MAX;
    		}
        }.start();
    }
    
    if (output) {
      System.gc(); System.gc();
      long after = Runtime.getRuntime().totalMemory() -
        Runtime.getRuntime().freeMemory();
      System.out.println("Memory before: " + before);
      System.out.println("Memory after: " + after);
      System.out.println("Memory usage: " + (after - before));
    }
    

    if (output)
      System.out.println("TIME: " + (System.currentTimeMillis() - start));
  }

}
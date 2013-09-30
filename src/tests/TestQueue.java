package tests;
import java.util.Set;

import java.util.Iterator;

public class TestQueue
{
	
	private static final int TIMES	= 40000000;
	  private static final int MAX		= 5000000;
  
  public static void main(String[] argv) {
    // first, get the JIT going

	  test(false, new java.util.LinkedList<Integer>());
	  test(false, new struct.LinkedList<Integer>());
    

    // then, do real timings
    
	  test(true, new java.util.LinkedList<Integer>());
	  test(true, new struct.LinkedList<Integer>());
  }

  public static void test(boolean output, struct.LinkedList<Integer> set) {
    long start = System.currentTimeMillis();

    if (output) {
      System.gc(); System.gc();
    }
    long before = Runtime.getRuntime().totalMemory() -
      Runtime.getRuntime().freeMemory();
    
    // add
    for (int ix = 0; ix < TIMES; ix++)
      set.addLast(new Integer((int) Math.round(Math.random() * MAX)));

    if (output) {
      System.gc(); System.gc();
      long after = Runtime.getRuntime().totalMemory() -
        Runtime.getRuntime().freeMemory();
      System.out.println("Memory before: " + before);
      System.out.println("Memory after: " + after);
      System.out.println("Memory usage: " + (after - before));
    }

    // iterate
    Iterator it = set.iterator();
    while (it.hasNext()) {
      Integer number = (Integer) it.next();
    }

    // remove
    while(!set.isEmpty())
      set.removeFirst();
    

    if (output)
      System.out.println("TIME: " + (System.currentTimeMillis() - start));
  }


  public static void test(boolean output, java.util.LinkedList<Integer> set) {
    long start = System.currentTimeMillis();

    if (output) {
      System.gc(); System.gc();
    }
    long before = Runtime.getRuntime().totalMemory() -
      Runtime.getRuntime().freeMemory();
    
    // add
    for (int ix = 0; ix < TIMES; ix++){
      set.addLast(new Integer((int) Math.round(Math.random() * MAX)));
    }
    if (output) {
      System.gc(); System.gc();
      long after = Runtime.getRuntime().totalMemory() -
        Runtime.getRuntime().freeMemory();
      System.out.println("Memory before: " + before);
      System.out.println("Memory after: " + after);
      System.out.println("Memory usage: " + (after - before));
    }
    

    // iterate
    Iterator it = set.iterator();
    while (it.hasNext()) {
      Integer number = (Integer) it.next();
    }

    // remove
    while(!set.isEmpty()){
        set.removeFirst();
    }

    if (output)
      System.out.println("TIME: " + (System.currentTimeMillis() - start));
  }

}
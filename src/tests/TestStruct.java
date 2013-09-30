package tests;
import java.util.Set;

import java.util.Iterator;

public class TestStruct
{
	
  private static final int TIMES	= 51200000;
  private static final int MAX		= 500000;
  
  public static void main(String[] argv) {
    // first, get the JIT going
	warmup();

    // then, do real timings
    doTest(true);
    
  }
  
  static void warmup()
  {
	  doTest(false);
  }
  
  static void doTest(boolean b)
  {
	  test(b, new struct.LinkedList<Float>());
	  test(b, new struct.Stack<Float>());
	  test(b, new struct.Queue<Float>());
	  test(b, new struct.HashSet<Float>());
  }

  ///

  public static void test(boolean output, struct.HashSet<Float> set)
  {
    long start = System.currentTimeMillis();

    if (output) {
      System.gc(); System.gc();
    }
    long before = Runtime.getRuntime().totalMemory() -
      Runtime.getRuntime().freeMemory();
    
    // add
    for (int ix = 0; ix < TIMES; ix++)
      set.add(new Float(Math.round(Math.random() * MAX)));

    if (output)
    {
    	System.out.println("HashSet");
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
    	Float number = (Float) it.next();
    }

    // remove
    for (int ix = 0; ix < TIMES; ix++) {
    	Float number = new Float(Math.round(Math.random() * MAX));
      set.remove(number);
    }

    if (output)
      System.out.println("TIME: " + (System.currentTimeMillis() - start));
  }
  
  
  

  public static void test(boolean output, struct.LinkedList<Float> set)
  {
	  long start = System.currentTimeMillis();

	    if (output) {
	      System.gc(); System.gc();
	    }
	    long before = Runtime.getRuntime().totalMemory() -
	      Runtime.getRuntime().freeMemory();
	    
	    // add
	    for (int ix = 0; ix < TIMES; ix++)
	      set.add(new Float(ix));

	    if (output) {
	    	System.out.println("LinkedList");
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
	    	Float number = (Float) it.next();
	    }

	    // remove
	    for (int ix = 0; ix < set.size(); ix++) {
	    	
	      set.remove(ix);
	    }

	    if (output)
	      System.out.println("TIME: " + (System.currentTimeMillis() - start));
  }
  
  
  
  

  public static void test(boolean output, struct.Queue<Float> set)
  {
	  long start = System.currentTimeMillis();

	    if (output) {
	      System.gc(); System.gc();
	    }
	    long before = Runtime.getRuntime().totalMemory() -
	      Runtime.getRuntime().freeMemory();
	    
	    // add
	    for (int ix = 0; ix < TIMES; ix++)
	      set.enqueue(new Float(Math.round(Math.random() * MAX)));

	    if (output) {
	    	System.out.println("Queue");
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
	    	Float number = (Float) it.next();
	    }

	    // remove
	    while(!set.isEmpty())
	    	set.dequeue();

	    if (output)
	      System.out.println("TIME: " + (System.currentTimeMillis() - start));
  }
  
  

  public static void test(boolean output, struct.Stack<Float> set)
  {
	  long start = System.currentTimeMillis();

	    if (output) {
	      System.gc(); System.gc();
	    }
	    long before = Runtime.getRuntime().totalMemory() -
	      Runtime.getRuntime().freeMemory();
	    
	    // add
	    for (int ix = 0; ix < TIMES; ix++)
	      set.push(new Float(Math.round(Math.random() * MAX)));

	    if (output) {
	    	System.out.println("Stack");
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
	    	Float number = (Float) it.next();
	    }

	    // remove
	    while(!set.isEmpty())
	      set.pop();
	    

	    if (output)
	      System.out.println("TIME: " + (System.currentTimeMillis() - start));
  }
}
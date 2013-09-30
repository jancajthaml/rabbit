package util.sort.any;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Quick Sort (3-pivot) algorithm
 *
 * @author Jan Cajthaml
 */
public class SmartSort
{

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private SmartSort() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT ... elements)
	{
		sort(elements,0,elements.length-1);
	}
	
	/**
	 * sort given collection 
	 * @param num collection of elements
	 * @param left left bounds
	 * @param right right bounds
	 */
	private static <ELEMENT extends Comparable<ELEMENT>>void sort( ELEMENT [ ] elements, int left, int right )
    {
		
    }

	/**
	 * 
	 * @param e
	 * @param a
	 * @param b
	 */
	private static <ELEMENT extends Comparable<ELEMENT>> void swap(ELEMENT[] e, int a, int b)
	{
		ELEMENT T	= e[a]; 
		e[a]		= e[b];
		e[b]		= T;
	}
	
	  
	  static void search()
	  {
		  Random rand = new Random();
		  Integer[] array = new Integer[100000];
		  for(int i=0; i<array.length; i++)
			  array[i]=new Integer(rand.nextInt());
		
		  long time = System.nanoTime();
		 // System.out.println("Before sort");
		  //for(Integer i : array)
		  //System.out.print(i+", ");
		  
		  QuickSort3.sort(array);
		  System.out.println("duration GNOME: \t"+(System.nanoTime()-time));

		  //System.out.println("\nAfter sort");
		  Integer last=new Integer(Integer.MIN_VALUE);
		  for(Integer i : array)
		  {
			  if(i.compareTo(last)<0)	System.err.println("ALGORITHM DOESNT WORK "+i+" < "+last);
	//		  else			System.err.println("ALGORITHM OK "+i+" >= "+last);
			  last=i;
		  }
			//  System.out.print(i+", ");
		  
		  
	  }
	  
	  static void search2()
	  {
		  Random rand = new Random();
		  List<Integer> array = new LinkedList<Integer>();
		  for(int i=0; i<100000; i++)
			  array.add(new Integer(rand.nextInt()));
		
		  long time = System.nanoTime();
		 // System.out.println("Before sort");
		  //for(Integer i : array)
		  //System.out.print(i+", ");
		  
		  Collections.sort(array);
		  System.out.println("duration Native: \t"+(System.nanoTime()-time));

		  
		  //System.out.println("\nAfter sort");
		  Integer last=new Integer(Integer.MIN_VALUE);
		  for(Integer i : array)
		  {
			  if(i.compareTo(last)<0)	System.err.println("ALGORITHM DOESNT WORK "+i+" < "+last);
	//		  else			System.err.println("ALGORITHM OK "+i+" >= "+last);
			  last=i;
		  }
			//  System.out.print(i+", ");
		  
		  
	  }
	  public static void main(String[] args)
	  {
		  for(int j=0; j<100; j++){
			  System.out.println("\nNEXT");
		  for(int i=0; i<10; i++)
			  search();
		  System.out.println("\nNEXT");
		  for(int i=0; i<10; i++)
			  search2();
		  System.out.println("\nNEXT");
		  for(int i=0; i<10; i++)
			  search();
		  System.out.println("\nNEXT");
		  for(int i=0; i<10; i++)
			  search2();
		  }
	  }

}
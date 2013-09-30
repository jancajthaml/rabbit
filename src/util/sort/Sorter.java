package util.sort;

/**
 * Sortting algorithm base
 *
 * @author Jan Cajthaml
 */
public interface Sorter
{
	
	/**
	 * @param elements target collection to sort
	 */
	public <ELEMENT extends Comparable<ELEMENT>>void sort(ELEMENT ... elements);
	
}

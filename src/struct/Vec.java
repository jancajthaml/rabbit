package struct;

import util.calculations.ObjectToString;

/**
 * Lightweight Generic Vector implementation
 * 
 * @author Jan Cajthaml
 *
 * @param <ELEMENT>
 */
public class Vec<ELEMENT>
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private ELEMENT[] elements	= null;

	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new Vector from set of elements
	 * 
	 * @param e elements
	 */
	public Vec(ELEMENT ... e)
	{ Vec.this.elements = e; }
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Gets index value
	 * 
	 * @param i element index
	 * @return element
	 */
	public ELEMENT get(int i)
	{ return Vec.this.elements[i]; }
	
	/**
	 * Sets index value
	 * 
	 * @param i element index
	 * @param o element value
	 */
	public void set(int i, ELEMENT o)
	{ Vec.this.elements[i] = o; }
	
	/**
	 * Formats class like: Vec<ELEMENT>[a1, a2, ..., an]
	 */
	public String toString()
	{ return "Vec"+ObjectToString.genericArrayToString(Vec.this.elements); }
	
}
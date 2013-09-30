package util.search;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import struct.LinkedList;

/**
 * Boyer Moore search Algorithm implementation
 * 
 * @author Jan Cajthaml
 *
 */
public class BoyerMoore
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private String pattern	= "";
	private int style		= Pattern.LITERAL;
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * 
	 * @param pattern regex
	 */
	public void setPattern(String pattern)
	{ this.pattern = pattern; }

	/**
	 * 
	 * @param text pattern regex
	 * @param style Pattern type
	 */
	public void setPattern(String text, int style)
	{
		this.style = style;
		if(style==Pattern.CASE_INSENSITIVE) text = text.toLowerCase();
		setPattern(text);
	}
	
	/**
	 * Find matches in target text
	 * 
	 * @param text target string
	 * @return Vector of indexes of matches
	 */
	public LinkedList<Integer> match(String text)
	{
		if(style==Pattern.CASE_INSENSITIVE) text = text.toLowerCase();
		
		LinkedList<Integer> matches		= new LinkedList<Integer>();
		int m							= text.length();
		int n							= pattern.length();
		Map<Character, Integer> right	= preprocessForBadCharacterShift(pattern);	
		int at							= 0;
		
		while (at + (n - 1) < m)
		{
			for (int indexInPattern = n - 1; indexInPattern >= 0; indexInPattern--)
			{
				int indexInText	= at + indexInPattern;
				char x			= text.charAt(indexInText);
				char y			= pattern.charAt(indexInPattern);
				
				if (indexInText >= m)	break;
				if (x != y)
				{
					Integer r = right.get(x);
					
					if (r == null)	at = indexInText + 1;
					else
					{
						int shift	= indexInText - (at + r);
						at			+= shift > 0 ? shift : 1;
					}
					break;
				}
				else if (indexInPattern == 0)
				{
					matches.add(at);
					at++;
				}
			}
		}
		return matches;
	}
	
	/**
	 * 
	 * 
	 * @param pattern input match pattern
	 * @return Dictionary for character shift
	 */
	private static Map<Character, Integer> preprocessForBadCharacterShift(String pattern)
	{
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		
		for (int i = pattern.length() - 1; i >= 0; i--)
		{
			char c = pattern.charAt(i);
			if (!map.containsKey(c)) map.put(c, i);
		}
		return map;
	}
	
}
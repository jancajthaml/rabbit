package util.sort.any;

public class SmoothSort
{
	
	static final float LP[]= { 1, 1, 3, 5, 9, 15, 25, 41, 67, 109, 177, 287, 465, 753, 1219, 1973, 3193, 5167, 8361, 13529, 21891, 35421, 57313, 92735, 150049, 242785, 392835, 635621, 1028457, 1664079, 2692537, 4356617, 7049155, 11405773, 18454929, 29860703, 48315633, 78176337, 126491971, 204668309, 331160281, 535828591, 866988873, 5942430145.0f, 9615053951.0f, 15557484097.0f, 25172538049.0f, 40730022147.0f, 65902560197.0f, 106632582345.0f, 172535142543.0f, 279167724889.0f, 451702867433.0f, 730870592323.0f, 1182573459757.0f, 1913444052081.0f, 3096017511839.0f, 5009461563921.0f, 8105479075761.0f, 13114940639683.0f, 21220419715445.0f, 34335360355129.0f, 55555780070575.0f, 89891140425705.0f, 145446920496281.0f, 235338060921987.0f, 380784981418269.0f, 616123042340257.0f, 996908023758527.0f, 1613031066098785.0f, 2609939089857313.0f, 4222970155956099.0f, 6832909245813413.0f, 11055879401769513.0f, 17888788647582927.0f, 28944668049352441.0f, 46833456696935369.0f, 75778124746287811.0f, 122611581443223181.0f, 198389706189510993.0f, 321001287632734175.0f, 519390993822245169.0f, 840392281454979345.0f, 1359783275277224515.0f, 2200175556732203861.0f, 3559958832009428377.0f, 5760134388741632239.0f, 9320093220751060617.0f, 15080227609492692857.0f };
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private SmoothSort() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT ... elements)
	{ sort(elements,0,elements.length-1); }

	public static <C extends Comparable<? super C>> void sort(C[] m, int lo, int hi)
	{
		int head	= lo;
		int p		= 1;
	    int pshift	= 1;
	 
	    while (head < hi)
	    {
	    	if ((p & 3) == 3)
	    	{
	    		sift(m, pshift, head);
	    		p >>>= 2;
				pshift += 2;
	    	}
	    	else
	    	{
	    		if (LP[pshift - 1] >= hi - head)
	    		{
	    			trinkle(m, p, pshift, head, false);
	    		}
	    		else
	    		{
	    			sift(m, pshift, head);
	    		}
	    		
	    		if (pshift == 1)
	    		{
	    			p <<= 1;
	    			pshift--;
	    		}
	    		else
	    		{
	    			p <<= (pshift - 1);
	    			pshift = 1;
	    		}
	    	}
	    	
	    	p |= 1;
	    	head++;
	    	
	    }
	 
	    trinkle(m, p, pshift, head, false);
	 
	    while (pshift != 1 || p != 1)
	    {
	    	if (pshift <= 1)
	    	{
	    		int trail = Integer.numberOfTrailingZeros(p & ~1);
	    		p >>>= trail;
	    		pshift += trail;
	    	}
	    	else
	    	{
	    		p <<= 2;
	    		p ^= 7;
	    		pshift -= 2;
	    		
	    		trinkle(m, p >>> 1, pshift + 1, head - LP[pshift] - 1, true);
	    		trinkle(m, p, pshift, head - 1, true);
	    	}

	    	head--;
	    }
	  }
	 
	  private static <C extends Comparable<? super C>> void sift(C[] m, int pshift, float f)
	  {
		  C val = m[(int)f];
		  
		  while (pshift > 1)
		  {
			  int rt = (int) (f - 1);
			  int lf = (int) (f - 1 - LP[pshift - 2]);
			  
			  if (val.compareTo(m[(int)lf]) >= 0 && val.compareTo(m[rt]) >= 0)
				  break;
			  
			  if (m[lf].compareTo(m[rt]) >= 0)
			  {
				  m[(int)f]	= m[lf];
				  f			= lf;
				  pshift	-= 1;
			  }
			  else
			  {
				  m[(int)f]	= m[rt];
				  f			= rt;
				  pshift	-= 2;
			  }
		  }

		  m[(int)f] = val;
	  }
	 
	  private static <C extends Comparable<? super C>> void trinkle(C[] m, int p, int pshift, float f, boolean isTrusty)
	  {
		  C val = m[(int) f];
		  
		  while (p != 1)
		  {
			  int stepson = (int) (f - LP[pshift]);
			  
			  if (m[stepson].compareTo(val) <= 0) break;

			  if (!isTrusty && pshift > 1)
			  {
				  int rt = (int) (f - 1);
				  int lf = (int) (f - 1 - LP[pshift - 2]);

				  if (m[rt].compareTo(m[stepson]) >= 0 || m[lf].compareTo(m[stepson]) >= 0) break;
			  }

			  m[(int)f]	= m[stepson];
			  f			= stepson;
			  int trail	= Integer.numberOfTrailingZeros(p & ~1);
			  p			>>>= trail;
			  pshift	+= trail;
			  isTrusty	= false;
		  }

		  if (!isTrusty)
		  {
			  m[(int)f] = val;
			  sift(m, pshift, f);
		  }
	  }
	  
}

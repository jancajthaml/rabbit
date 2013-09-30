package struct;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * Faster Lightweight BigInteger implementation
 *
 * @author Jan Cajthaml
 */
public class RealInteger
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private int capacity	= 128;
	private BitSet buffer	= new BitSet(capacity);
	private BigInteger bi	= new BigInteger("0");
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //

	public RealInteger(int number)
	{ buffer = toBinary(number); }
	
	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Plus operation 
	 * 
	 * @param input number to add
	 */
	public void add(int input)
	{ add(buffer,toBinary(input)); }

	//FIXME use ATOMIC
	private BitSet toBinary(int input)
	{
		int size	= capacity>>3;
		byte[] conv	= new byte[size];
		BitSet bits	= new BitSet(capacity);
		
		for(int i=size-1; i>=0; i--)
		{
			conv[i] = (byte) (input & 0xFF );
			input >>= 8;
		}
		
		for (int i = 0; i < conv.length * 8; i++)
			if ((conv[i>>3] & (1 << (7 - (i % 8)))) > 0) bits.set(i, true);
		
		return bits;
	}
	
	private void add(BitSet a, BitSet b)
    {
        boolean carry = false;
        for (int i = capacity-1; i >=0 ; i--)
        {
            if (a.get(i) && b.get(i))
            {
            	a.set(i,carry);
            	carry = true;
            	continue;
            }
            
            else if (a.get(i) | b.get(i))
            {
                if (carry)
                {
                	a.set(i,false);
                    carry = true;
                    continue;
                }
                else
                {
                	a.set(i,true);
                    carry = false;
                    continue;
                }
            }
            
            else
            {
            	a.set(i,carry);
            	carry = false;
            	continue;
            }
        }
    }
	
	/**
	 * Internaly converts binary to integer and prints it to string
	 */
	//FIXME use atomic
	public String toString()
	{
		for(int i=0; i<buffer.size(); i++) if(buffer.get(i)) bi = bi.setBit(buffer.size()-i-1);
		return bi.toString();
	}

}
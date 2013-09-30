package lang;

import java.awt.Color;

/**
 * 
 * @author Jan Cajthaml
 * 
 * Library for atomic functions.
 * Future implementation would include native libraries build on top of ASSEMBLY language
 * 
 */
//FIXME Refator & integrate
public class Atomic
{
	
	// >----------[ attrs ]-------------------------------------------------------------------------------------< //

    static int uniqueId				= 0;
	private static char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    private static StringBuilder _	= new StringBuilder();	//pre-cached String buffer
    
	// >----------[ Byte ]-------------------------------------------------------------------------------------< //

    /**
     * Convert single integer to byte
     * 
     * @param n integer
     * @return byte
     */
    public static byte _byte(int n)
    { return (byte)n; }

    /**
     * Convert single integer to byte array
     * 
     * @param n integer
     * @return byte array
     */
    public static byte[] __byte(int n)
    { return new byte[] { (byte)(n >>> 24), (byte)(n >>> 16), (byte)(n >>> 8), (byte)n}; }

    // >----------[ Int ]-------------------------------------------------------------------------------------< //
    
    /**
     * Convert byte to integer
     * 
     * @param n byte
     * @return integer
     */
    public static int _int(byte n)
    { return n & 0xFF; }
    
    /**
     * Convert short to integer
     * 
     * @param n short
     * @return integer
     */
    public static int _int(short n)
    { return n & 0xFFFF; }

    /**
     * Convert String to integer (fast way using knowledge of ASCII chars)
     * 
     * @param n string
     * @return integer
     */
    public static int _int(String n)
    {
         boolean negative = false;
         int result	= 0,i = 0, len = n.length();

         if (len > 0)
         {
             char firstChar = n.charAt(0);
             if (firstChar < '0')
             {
                 if (firstChar == '-') negative = true;
                 i++;
             }
             while (i < len)
             {
                 result <<= 4;
                 result -= Character.digit((int)n.charAt(i++),16);
             }
         }
         return negative ? result : -result;
    }
    
    // >----------[ Signed Short ]-------------------------------------------------------------------------------------< //
    
    /**
     * Convert int to short
     * 
     * @param n int
     * @return short
     */
    public static short _short(int n)
    { return (short)n; }

    // >----------[ Binary String ]-------------------------------------------------------------------------------------< //
    
    /**
     * Convert byte to binary string
     * 
     * @param b byte
     * @return binary string
     */
    public static String _bit(byte b)
    { return new String(new char[]{HEX_CHARS[(0x80 & b)>>7],HEX_CHARS[(0x40 & b)>>6],HEX_CHARS[(0x20 & b)>>5],HEX_CHARS[(0x10 & b)>>4],HEX_CHARS[(0x08 & b)>>3],HEX_CHARS[(0x04 & b)>>2],HEX_CHARS[(0x02 & b)>>1],HEX_CHARS[0x01 & b]}); }
    
    /**
     * Convert byte array to binary string
     * 
     * @param data byte array
     * @return binary string
     */
    public static String _bit(byte ... data)
    {
    	_.setLength(0);
        for (byte b : data)	_.append(_bit(b));
        return _.toString();
    }
    
    // >----------[ Unsigned Hex ]-------------------------------------------------------------------------------------< //

    /**
     * Convert byte to unsigned hexadecimal string
     * 
     * @param data byte
     * @return hexadecimal string
     */
    public static String _hex(byte data)
    { return _hex(_int(data)); }

    // >----------[ Human readable unsigned Hex ]-------------------------------------------------------------------------------------< //
    
    /**
     * Convert byte array to unsigned hexadecimal string with spacing between hexadecimal numbers
     * 
     * @param data byte array
     * @param maxLength cut offset
     * @return hexadecimal string
     */
    public static String _hex(byte[] data, int maxLength)
    {
    	_.setLength(0);
    	for (int i = 0; i < data.length && i < maxLength; i++)	_.append(_hex(data[i] & 0xFF)+" ");
    	return _.toString();
    }

    /**
     * Convert integer to hexadecimal string
     * 
     * @param n integer
     * @return hexadecimal string
     */
    public static String _hex(int n)
    { return Integer.toHexString(n); }

    /**
     * Convert Color to standart hexadecimal value
     * 
     * @param c color
     * @return hexadecimal value
     */
	public static String _hex(Color c)
	{ return (c==null)?null:("#"+toBrowserHex(c.getRed())+toBrowserHex(c.getGreen())+toBrowserHex(c.getBlue())); }
	
	/**
	 * Convert Color RGB value to hexadecimal value
	 * 
	 * @param value color RGB value
	 * @return hexadecimal value
	 */
	private static String toBrowserHex(int value)
	{ return (value<16)?("0"+Integer.toHexString(value)):Integer.toHexString(value); }
    
    // >----------[ String ]-------------------------------------------------------------------------------------< //

	/**
	 * Convert byte array to readable string
	 * 
	 * @param data byte array
	 * @param maxLength cut offset
	 * @return byte array string
	 */
    public static String bytesToText(byte[] data, int maxLength)
    {
        String s = "";
        for (int i = 0; i < data.length && i < maxLength; i++)
        {
            if(_int(data[i]) < 32)	s = s + "[" + _int(data[i]) + "]";
            else						s = s + (char)data[i];
        }
        return s;
    }    

    // >----------[ String formatters ]-------------------------------------------------------------------------------------< //
    
    /**
	 * Convert byte array to readable string alternative version
	 * 
	 * @param data byte array
	 * @param length cut offset
	 * @return byte array string
	 */
    public static String $(byte[] data, int length)
    {
    	_.setLength(0);
        for (int i = 0; i <  data.length && i < length; i++)	_.append(_hex(_int(data[i])) + " ");
        return _.toString();
    }
    
    // >----------[ log(n) String expander  ]-------------------------------------------------------------------------------------< //

    /**
     * prefix number expander with "0" prefix
     * 
     * @param s number
     * @param length wanted length
     * @return prefixed string
     */
    public static String $(int s, int length)
    { return $(String.valueOf(s),length,"00000000000000000000"); }
    
    /**
     * prefix string expander with "0" prefix
     * 
     * @param s string
     * @param length wanted length
     * @return prefixed string
     */
    public static String $(String s, int length)
    { return $(s,length,"00000000000000000000"); }
    
    /**
     * generic prefix string expander
     *  
     * @param s string
     * @param length wanted length
     * @param template prefix
     * @return prefixed string
     */
    public static String $(String s, int length,String template)
    { return (length>=template.length())?$(s,length,template+""+template):((s.length() < length)?(template.substring(0,length-s.length())+s):s); }

    /**
     * simplistic id counter
     * 
     * @return next id
     */
	public static int uuid()
	{ return uniqueId++; }

}
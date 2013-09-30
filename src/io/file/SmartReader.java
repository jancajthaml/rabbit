package io.file;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.io.Reader;

/**
 * File Reader over SmartFile
 *
 * @author Jan Cajthaml
 */
public class SmartReader extends Reader
{

	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private InputStreamReader in	= null;
	private String encoding			= "";

	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new SmartReader over input stream
	 * 
	 * @param stream input stream
	 * 
	 * @throws IOException
	 */
	public SmartReader(InputStream stream) throws IOException
	{ this(stream, null); }
	
	/**
	 * Creates new SmartReader over input stream with given encoding
	 * 
	 * @param stream input stream
	 * @param encoding file encoding
	 * 
	 * @throws IOException
	 */
	public SmartReader(InputStream stream, String encoding) throws IOException
	{ SmartReader.this.prepare(stream, encoding); }

	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * closes stream
	 */
	public void close() throws IOException
	{ SmartReader.this.in.close(); }
	
	/**
	 * Get File encoding
	 * 
	 * @return file encoding
	 */
	public String getEncoding()
	{ return SmartReader.this.encoding; }
	
	/**
	 * Reads input Characters into a portion of an array.
	 */
	public int read(char[] buffer, int offset, int length) throws IOException
	{ return SmartReader.this.in.read(buffer, offset, length); }
	
	/**
	 * FIX encoding before reading data
	 * 
	 * @param stream input stream
	 * @param encoding file encoding
	 * 
	 * @throws IOException
	 */
	protected void prepare(InputStream stream, String encoding) throws IOException
	{
		PushbackInputStream pushBack = new PushbackInputStream(stream, 4);

		byte BOM[]	= new byte[4];
		int blank	= 0;
		int n		= pushBack.read(BOM, 0, BOM.length);

		if ((BOM[0]==(byte)0x00) && (BOM[1]==(byte)0x00) && (BOM[2]==(byte)0xFE) && (BOM[3]==(byte)0xFF))
		{
			SmartReader.this.encoding	= "UTF-32BE";
			blank						= n - 4;
		}
		else if (n==4 && (BOM[0]==(byte)0xFF) && (BOM[1]==(byte)0xFE) && (BOM[2]==(byte)0x00) && (BOM[3]==(byte)0x00))
		{
			SmartReader.this.encoding	= "UTF-32LE";
			blank						= n - 4;
		}
		else if ((BOM[0]==(byte)0xEF) && (BOM[1]==(byte)0xBB) && (BOM[2]==(byte)0xBF))
		{
			SmartReader.this.encoding	= "UTF-8";
			blank						= n - 3;
		}
		else if ((BOM[0]==(byte)0xFE) && (BOM[1] == (byte)0xFF))
		{
			SmartReader.this.encoding	= "UTF-16BE";
			blank						= n - 2;
		}
		else if ((BOM[0]==(byte)0xFF) && (BOM[1]== (byte)0xFE))
		{
			SmartReader.this.encoding	= "UTF-16LE";
			blank						= n - 2;
		}
		else
		{
			SmartReader.this.encoding	= encoding;
			blank						= n;
		}

		if (blank > 0)							pushBack.unread(BOM, (n - blank), blank);
		else if (blank < -1)					pushBack.unread(BOM, 0, 0);
		if (SmartReader.this.encoding == null)	SmartReader.this.encoding = (in = new InputStreamReader(pushBack)).getEncoding();
		else 									in = new InputStreamReader(pushBack, SmartReader.this.encoding);
	}
	
}
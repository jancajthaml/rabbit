package io.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * File Writer over SmartFile
 *
 * @author Jan Cajthaml
 */
public class SmartWriter extends Writer
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private OutputStreamWriter out;

	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new SmartWriter with given encoding and filename
	 * 
	 * @param name file name
	 * @param encoding file encoding
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public SmartWriter(String name, String encoding) throws UnsupportedEncodingException, IOException
	{ this(new FileOutputStream(name), encoding); }
	
	/**
	 * 
	 * @param file target file
	 * @param encoding file encoding
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public SmartWriter(File file, String encoding) throws UnsupportedEncodingException, IOException
	{ this(new FileOutputStream(file), encoding); }
	
	/**
	 * Creates new SmartWriter over output stream with given encoding
	 * 
	 * @param out output stream
	 * @param encoding file encoding
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public SmartWriter(OutputStream out, String encoding) throws UnsupportedEncodingException, IOException
	{ prepare(out, encoding); }

	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Closes the stream
	 */
	public void close() throws IOException
	{ SmartWriter.this.out.close(); }
	
	/**
	 * Flushes the stream.
	 */
	public void flush() throws IOException
	{ SmartWriter.this.out.flush(); }
	
	/**
	 * Get file encoding
	 * 
	 * @return encoding
	 */
	public String getEncoding()
	{ return SmartWriter.this.out.getEncoding(); }
	
	/**
	 * Writes l bytes from d buffer starting at p
	 */
	public void write(char[] d, int p, int l) throws IOException
	{ SmartWriter.this.out.write(d, p, l); }
	
	/**
	 * Writes single byte to stream
	 */
	public void write(int d) throws IOException
	{ SmartWriter.this.out.write(d); }
	
	/**
	 * Writes l bytes from d starting at p
	 */
	public void write(String d, int p, int l) throws IOException
	{ SmartWriter.this.out.write(d, p, l); }
	
	/**
	 * FIX encoding before writing data
	 * 
	 * @param stream output stream
	 * @param encoding file encoding
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private void prepare(OutputStream stream, String encoding) throws UnsupportedEncodingException, IOException
	{
		SmartWriter.this.out = new OutputStreamWriter(stream, encoding);
		if ("UTF-8".equals(encoding))
		{
			if (!(System.getProperty("UnicodeWriter.writeUtf8BOM")!=null && Boolean.valueOf(System.getProperty("UnicodeWriter.writeUtf8BOM")).equals(Boolean.FALSE)))
				stream.write(new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF }, 0, 3);
		}
		else if ("UTF-16LE".equals(encoding))								stream.write(new byte[] { (byte)0xFF, (byte)0xFE }, 0, 2);
		else if ("UTF-16BE".equals(encoding))								stream.write(new byte[] { (byte)0xFE, (byte)0xFF }, 0, 2);
		else if ("UTF-32LE".equals(encoding))								stream.write(new byte[] { (byte)0xFF, (byte)0xFE, (byte)0x00, (byte)0x00 }, 0, 4);
		else if ("UTF-32".equals(encoding)||"UTF-32BE".equals(encoding))	stream.write(new byte[] { (byte)0x00, (byte)0x00, (byte)0xFE, (byte)0xFF }, 0, 4);
	}

}
package io.file;

import java.io.Reader;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;

/**
 * Reader for Character streams 
 *
 * @author Jan Cajthaml
 */
public class DocumentReader extends Reader
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private long position		= 0;
	private long mark			= -1L;
	private Document document	= null;
	private Segment buffer		= new Segment();

	// >-------[ctors]---------------------------------------------------------------------------------------< //

	/**
	 * Creates a new synchronized Character reader that reads plain text dirrectly into Segment buffer.
	 * 
	 * @param document target Document
	 */
	public DocumentReader(Document document)
	{ DocumentReader.this.document = document; }
	
	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Closes the stream.
	 */
	public void close()
	{ }
	
	/**
	 * Marks the current position in the stream.
	 */
	public void mark(int NEVER_USED)
	{ DocumentReader.this.mark = DocumentReader.this.position; }
	
	/**
	 * Always returns true - always support mark() operation.
	 */
	public boolean markSupported()
	{ return true; }
	
	/**
	 * 
	 * @param position target position
	 */
	public void seek(long position)
	{ DocumentReader.this.position = Math.min(position, DocumentReader.this.document.getLength()); }
	
	/**
	 * Reads Characters into array.
	 */
	public int read(char array[])
	{ return read(array, 0, array.length); }

	/**
	 * Reads single Character.
	 */
	public int read()
	{
		if(DocumentReader.this.position>=DocumentReader.this.document.getLength()) return -1;
		try
		{
			DocumentReader.this.document.getText((int)position,1, buffer);
			DocumentReader.this.position++;
			return DocumentReader.this.buffer.array[DocumentReader.this.buffer.offset];
		}
		catch (BadLocationException e)
		{
			//never happens
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Reads input Characters into a portion of an array.
	 */
	public int read(char buffer[], int off, int len)
	{
		if(DocumentReader.this.position>=DocumentReader.this.document.getLength()) return -1;
		
		int																				k = len;
		if((DocumentReader.this.position+k)>=DocumentReader.this.document.getLength())	k = DocumentReader.this.document.getLength() - ((int)DocumentReader.this.position);
		if(off + k >= buffer.length)													k = buffer.length - off;
		
		try
		{
			DocumentReader.this.document.getText(((int)DocumentReader.this.position), k, DocumentReader.this.buffer);
			DocumentReader.this.position += k;
			System.arraycopy(DocumentReader.this.buffer.array,DocumentReader.this.buffer.offset,buffer,off,k);
			return k;
		}
		catch (BadLocationException e) { return -1; }
	}

	/**
	 * Always returns true - next read() is guaranteed not to block.
	 */
	public boolean ready()
	{ return true; }
	
	/**
	 * Resets the stream.
	 */
	public void reset()
	{
		if(DocumentReader.this.mark==-1)
		{
			DocumentReader.this.position = 0;
			return;
		}
		DocumentReader.this.position	= DocumentReader.this.mark;
		DocumentReader.this.mark		= -1;
	}
	
	/**
	 * Skippes number of Characters.
	 */
	public long skip(long size)
	{
		if ((DocumentReader.this.position + size) <= DocumentReader.this.document.getLength())
		{
			DocumentReader.this.position += size;
			return size;
		}
		
		long prev						= DocumentReader.this.position;
		DocumentReader.this.position	= DocumentReader.this.document.getLength();
		
		return DocumentReader.this.document.getLength() - prev;
	}
	
}
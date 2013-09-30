package io.file;

import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import lang.Destructable;
import struct.LinkedList;
import struct.Queue;
import util.FontUtils;
import util.GraphicUtils;

/**
 * Plain Text to RTF Convertor
 *
 * @author Jan Cajthaml
 */
public class RTF implements Transferable, Destructable
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private byte[] data							= null;
	private Reader reader						= null;
	private StringBuffer buffer					= null;
	private StringBuffer control				= null;
	private String fontName						= null;
	private boolean inControl					= false;
	private boolean last						= false;
	private boolean lastBold					= false;
	private boolean lastItalic					= false;
	private Queue<Font> fonts					= new Queue<Font>();
	private LinkedList<Color> colors			= new LinkedList<Color>();
	private StringBuffer document				= new StringBuffer();
	private int lastFontIndex					= 0;
	private int lastFGIndex						= 0;
	private int lastFontSize					= 0;
	private int size							= 0;
	private static final DataFlavor[] FLAVORS	= { new DataFlavor("text/rtf", "RTF"), DataFlavor.stringFlavor };

	// >-------[ctors]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new blank RTF convertor
	 */
	public RTF()
	{}
	
	/**
	 * Creates new RTF convertor with data
	 * 
	 * @param data plain text string
	 */
	public RTF(byte[] data)
	{ RTF.this.data = data; }

	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * @return an array of RTF data flavors.
	 */
	public DataFlavor[] getTransferDataFlavors()
	{ return (DataFlavor[])RTF.FLAVORS.clone();																																											}
	
	/**
	 * @return data to be transferred 
	 */
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{ return (flavor.equals(RTF.FLAVORS[0]))?(new ByteArrayInputStream(RTF.this.data==null ? new byte[0] : RTF.this.data)):((flavor.equals(RTF.FLAVORS[1]))?(RTF.this.data==null ? "" : plain(RTF.this.data)):null);	}
	
	/**
	 * @return true if data flavor is supported
	 * @param flavor the requested flavor for the data
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{ return (flavor.equals(RTF.FLAVORS[0]) || flavor.equals(RTF.FLAVORS[1]));																																			}

	/**
	 * Converts plain text to RTF text
	 * 
	 * @return RTF standartise string
	 * @throws IOException
	 */
	//FIXME fuse
	private String toRTF() throws IOException
	{
		int i = RTF.this.reader.read();
		if (i!='{') throw new IOException("Corrupted File");
		
		while ((i=RTF.this.reader.read())!=-1)
		{
			char ch = (char)i;
			switch (ch)
			{
				case '{':
					if (RTF.this.inControl && RTF.this.control.length()==0)
					{
						RTF.this.buffer.append('{');
						RTF.this.control.setLength(0);
						RTF.this.inControl = false;
					}
					else RTF.this.size++;
				break;
				case '}':
					if (RTF.this.inControl && RTF.this.control.length()==0)
					{
						RTF.this.buffer.append('}');
						RTF.this.control.setLength(0);
						RTF.this.inControl = false;
					}
					else RTF.this.size--;
				break;
				case '\\':
					if (size==0)
					{
						if (RTF.this.inControl)
						{
							if (RTF.this.control.length()==0)
							{
								RTF.this.buffer.append('\\');
								RTF.this.control.setLength(0);
								RTF.this.inControl = false;
							}
							else
							{
								String word = control.toString();
								if ("par".equals(word))			RTF.this.buffer.append('\n');
								else if ("tab".equals(word))	RTF.this.buffer.append('\t');
								RTF.this.control.setLength(0);
								RTF.this.inControl = false;	
							}
						}
						RTF.this.inControl = true;
					}
				break;
				case ' ':
					if (RTF.this.size==0)
					{
						if (RTF.this.inControl)
						{
							String word = RTF.this.control.toString();
							if ("par".equals(word))			RTF.this.buffer.append('\n');
							else if ("tab".equals(word))	RTF.this.buffer.append('\t');
							RTF.this.control.setLength(0);
							RTF.this.inControl = false;
						}
						else RTF.this.buffer.append(' ');
					}
				break;
				case '\r':
				case '\n':
					if (RTF.this.size==0)
					{
						if (RTF.this.inControl)
						{
							String word = RTF.this.control.toString();
							if ("par".equals(word))			RTF.this.buffer.append('\n');
							else if ("tab".equals(word))	RTF.this.buffer.append('\t');
							RTF.this.control.setLength(0);
							RTF.this.inControl = false;
						}
					}
				break;
				default:
					if (size==0)
					{
						if (inControl) control.append(ch);
						else buffer.append(ch);
					}
				break;
			}
		}
		return RTF.this.buffer.toString();
	}

	/**
	 * Obtains RTF text from plain text Reader 
	 * 
	 * @param r input Reader
	 * @return RTF string
	 * 
	 * @throws IOException
	 */
	private String plain(Reader r) throws IOException
	{
		try
		{
			RTF.this.reader		= r;
			RTF.this.buffer		= new StringBuffer();
			RTF.this.control	= new StringBuffer();
			RTF.this.size		= 0;
			RTF.this.inControl	= false;
			return toRTF();
		}
		finally { r.close(); }
	}
	
	/**
	 * Obtains RTF text from plain text data
	 * 
	 * @param data source string
	 * @return RTF string
	 * 
	 * @throws IOException
	 */
	public String plain(byte[] data) throws IOException
	{ return plain(new ByteArrayInputStream(data)); }
	
	/**
	 * Obtains RTF text from plain text File
	 * 
	 * @param file source file
	 * @return RTF string
	 * 
	 * @throws IOException
	 */
	public String plain(File file) throws IOException
	{ return plain(new BufferedReader(new FileReader(file))); }
	
	/**
	 * Obtains RTF text from plain text input stream
	 * 
	 * @param in source stream
	 * @return RTF string
	 * 
	 * @throws IOException
	 */
	public String plain(InputStream in) throws IOException
	{ return plain(new InputStreamReader(in, "US-ASCII")); }
	
	/**
	 * Obtains RTF text from plain text string
	 * 
	 * @param data source string
	 * @return RTF string
	 * 
	 * @throws IOException
	 */
	public String plain(String data) throws IOException
	{ return plain(new StringReader(data)); }
	
	/**
	 * Appends newline to RTF document
	 */
	public void newline()
	{
		document.append("\\par");
		document.append('\n');
		last = false;
	}
	
	/**
	 * Appends formatted text to RTF document 
	 * 
	 * @param text source
	 * @param f text font
	 * @param fg text foreground
	 * @param bg text background
	 */
	public void append(String text, Font f, Color fg, Color bg)
	{ append(text, f, fg, bg, false); }
	
	/**
	 * Appends formatted text to RTF document
	 * 
	 * @param text source
	 * @param f text font
	 * @param bg text background
	 * @param u is text underline?
	 */
	public void append(String text, Font f, Color bg, boolean u)
	{ append(text, f, null, bg, u, false); }
	
	/**
	 * Appends formatted text to RTF document
	 * 
	 * @param text source
	 * @param f text font
	 * @param fg text foreground
	 * @param bg text background
	 * @param u is text underline?
	 */
	public void append(String text, Font f, Color fg, Color bg, boolean u)
	{ append(text, f, fg, bg, u, true); }

	/**
	 * Appends formatted text to RTF document
	 * 
	 * @param text source
	 * @param f text font
	 * @param fg text foreground
	 * @param bg text background
	 * @param u is text underline?
	 * @param setFG ovveride parent foreground value? 
	 */
	public void append(String text, Font f, Color fg, Color bg, boolean u, boolean setFG)
	{
		if (text==null) return;
		
		int fontIndex = (f==null) ? 0 : (font(fonts, f)+1);
		
		if (fontIndex!=lastFontIndex)
		{
			document.append("\\f").append(fontIndex);
			lastFontIndex = fontIndex;
			last = true;
		}
		if (f!=null)
		{
			int screenRes = GraphicUtils.getScreenResolution();
			int fontSize =((screenRes!=72)?((int)Math.round(f.getSize2D()*screenRes/72.0)):(int)f.getSize2D());
			if (fontSize!=lastFontSize)
			{
				document.append("\\fs").append(fontSize);
				lastFontSize = fontSize;
				last = true;
			}
			if (f.isBold()!=lastBold)
			{
				document.append(lastBold ? "\\b0" : "\\b");
				lastBold = !lastBold;
				last = true;
			}
			if (f.isItalic()!=lastItalic)
			{
				document.append(lastItalic ? "\\i0" : "\\i");
				lastItalic = !lastItalic;
				last = true;
			}
		}
		else
		{
			int size = FontUtils.DEFAULT_FONT_SIZE;
			if (lastFontSize!=size)
			{
				document.append("\\fs").append(size);
				lastFontSize = size;
				last = true;
			}
			if (lastBold)
			{
				document.append("\\b0");
				lastBold = false;
				last = true;
			}
			if (lastItalic)
			{
				document.append("\\i0");
				lastItalic = false;
				last = true;
			}
		}
		if (u)
		{
			document.append("\\ul");
			last = true;
		}
		if (setFG)
		{
			int fgIndex = 0;
			if (fg!=null) fgIndex = index(colors, fg)+1;		
			if (fgIndex!=lastFGIndex)
			{
				document.append("\\cf").append(fgIndex);
				lastFGIndex = fgIndex;
				last = true;
			}
		}
		if (bg!=null)
		{
			int pos = index(colors, bg);
			document.append("\\highlight").append(pos+1);
			last = true;
		}
		if (last)
		{
			document.append(' ');
			last = false;
		}
		escape(document, text);
		if (bg!=null)
		{
			document.append("\\highlight0");
			last = true;
		}
		if (u)
		{
			document.append("\\ul0");
			last = true;
		}
	}

	/**
	 * Converts escape character to RTF format
	 * 
	 * @param buffer input buffer
	 * @param text source text
	 */
	private final void escape(StringBuffer buffer, String text)
	{
		int size = text.length();
		for (int i=0; i<size; i++)
		{
			char ch = text.charAt(i);
			switch (ch)
			{
				case '\t'	: buffer.append("\\tab"); while ((++i<size) && text.charAt(i)=='\t') buffer.append("\\tab"); buffer.append(' '); i--; break;
				case '\\'	:
				case '{'	:
				case '}'	: buffer.append('\\').append(ch); break;
				default		: buffer.append(ch); break;
			}
		}
	}

	/**
	 * Generate color table
	 * 
	 * @return RTF color table string
	 */
	private String color()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\\colortbl ;");
		for (int i=0; i<colors.size(); i++)
		{
			Color c = (Color)colors.get(i);
			buffer.append("\\red").append(c.getRed());
			buffer.append("\\green").append(c.getGreen());
			buffer.append("\\blue").append(c.getBlue());
			buffer.append(';');
		}
		buffer.append("}");
		return buffer.toString();
	}

	/**
	 * Generate font table
	 * 
	 * @return RTF font table string
	 */
	private String font()
	{
		StringBuffer buffer		= new StringBuffer();
		String monoFamilyName	= (fontName==null)?fontName=FontUtils.getMonospacedFontName():fontName;
		
		buffer.append("{\\fonttbl{\\f0\\fnil\\fcharset0 " + monoFamilyName + ";}");
		for (int i=0; i<fonts.size(); i++)
		{
			Font f = (Font)fonts.get(i);
			String familyName = f.getFamily();
			if (familyName.equals("Monospaced")) familyName = monoFamilyName;
			buffer.append("{\\f").append(i+1).append("\\fnil\\fcharset0 ");
			buffer.append(familyName).append(";}");
		}
		buffer.append('}');
		return buffer.toString();
	}
	
	/**
	 * 
	 * @param list Vector of Fonts
	 * @param font Font value
	 * @return Font position in RTF font table
	 */
	private static int font(Queue<Font> list, Font font)
	{
		String fontName = font.getFamily();
		int index=0;
		for (Font f : list)
		{
			if(f.getFamily().equals(fontName)) return index;
			index++;
		}
		list.enqueue(font);
		return list.size()-1;
	}

	/**
	 * 
	 * @param list Vector of Colors
	 * @param item Color value
	 * @return Color position in RTF color table
	 */
	private static int index(LinkedList<Color> list, Color item)
	{
		int pos = list.indexOf(item);
		if (pos==-1)
		{
			list.add(item);
			pos = list.size()-1;
		}
		return pos;
	}

	/**
	 * Creates clipart transferable
	 * 
	 * @return RTF string
	 */
	public String rtf()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang1033\\viewkind4\\uc\\pard\\f0\\fs20");
		buffer.append(font()).append('\n');
		buffer.append(color()).append('\n');
		buffer.append(document+"}");
		return buffer.toString();
	}

	/**
	 * rollback
	 */
	public void clear()
	{
		fonts.clear();
		colors.clear();
		document.setLength(0);
		
		lastFontIndex	= 0;
		lastFGIndex		= 0;
		last			= false;
		lastBold		= false;
		lastItalic		= false;
		lastFontSize	= FontUtils.DEFAULT_FONT_SIZE;
	}
	
	/**
	 * 
	 * @return data for transfer operation
	 */
	public Transferable getContents()
	{ return new RTF(rtf().getBytes()); }
	
	/**
	 * describtion of destructor work here
	 */
	public boolean $()
	{
		fonts.clear();
		colors.clear();
		document.setLength(0);
		
		if(reader!=null)
			try						{ reader.close();		}
			catch (IOException e)	{ e.printStackTrace();	}
		
		if(buffer!=null)
			buffer.setLength(0);
		
		if(control!=null)
			control.setLength(0);
		
		reader			= null;
		buffer			= null;
		control			= null;
		fontName		= null;
		return true;
	}
	
}
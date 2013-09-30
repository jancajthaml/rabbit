package io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import lang.Destructable;

/**
 * File with capability of working with either Local or Remote Files
 *
 * @author Jan Cajthaml
 */
public class SmartFile implements Comparable<SmartFile>, Destructable
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private boolean remote	= false;
	private String alias	= "";
	private File file		= null;
	private URL url			= null;
	private String path		= "";
	private String name		= "";
	private long timestamp	= 0L;
	private int flag		= 2;
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new Local SmartFile over File
	 * 
	 * @param file source File
	 */
	public SmartFile(File file)
	{
		try
		{
			SmartFile.this.remote	= false;
			SmartFile.this.file		= file.getCanonicalFile();
			SmartFile.this.path		= file.getAbsolutePath();
			SmartFile.this.name		= file.getName();
			SmartFile.this.alias	= SmartFile.this.name;
		}
		catch (IOException ioe) { SmartFile.this.file = file; }
	}

	/**
	 * Creates Remote SmartFile over URL
	 * 
	 * @param url source URL
	 */
	public SmartFile(URL url)
	{
		SmartFile.this.remote	= true;
		SmartFile.this.url		= url;
		SmartFile.this.path		= createFileFullPath();
		String name				= url.getPath();
		SmartFile.this.name		= (name.startsWith("/%2F/"))?(name.substring(4)):((name.startsWith("/"))?name.substring(1):name);
		SmartFile.this.alias	= SmartFile.this.name;
	}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Get input stream to File
	 * 
	 * @return InputStream of File
	 * @throws IOException
	 */
	@SuppressWarnings("resource") public InputStream inputStream() throws IOException
	{ return remote?(SmartFile.this.url.openStream()):new FileInputStream(SmartFile.this.file); }
	
	/**
	 * Get output stream from File
	 * 
	 * @return OutputStream for File
	 * @throws IOException
	 */
	@SuppressWarnings("resource") public OutputStream outputStream() throws IOException
	{ return remote?(SmartFile.this.url.openConnection().getOutputStream()):new FileOutputStream(SmartFile.this.file); }
	
	/**
	 * Determine whenever File has given extension
	 * 
	 * @param name File name
	 * @param ext File extension
	 * @return true if destination filename has extension
	 */
	public static boolean hasExtension(String name, String ext)
	{ return (cutExtension(name)+"."+ext).equalsIgnoreCase(name); }
	
	/**
	 * Slice extension from File name
	 * 
	 * @param name File name
	 * @return extension from File name
	 */
	public static String cutExtension(String name)
	{ return name.split("\\.(?=[^\\.]+$)")[0]; }
	
	/**
	 * create new SmartFile from string path
	 * 
	 * @param path File path
	 * @return blank SmartFile
	 */
	public static SmartFile create(String path)
	{ return new SmartFile(new File(path)); }
	
	/**
	 * create new SmartFile from File
	 *  
	 * @param file source File
	 * @return blank Local SmartFile
	 */
	public static SmartFile create(File file)
	{ return new SmartFile(file); }
	
	/**
	 * create new SmartFile from URL
	 * 
	 * @param url source URL
	 * @return blank Remote SmartFile
	 */
	public static SmartFile create(URL url)
	{ return ("file".equalsIgnoreCase(url.getProtocol()))?new SmartFile(new File(url.getPath())):new SmartFile(url); }
	
	/**
	 * 
	 * @return true if file is not Local
	 */
	public boolean isRemote()
	{ return !isLocal(); }
	
	/**
	 * 
	 * @return full path for Local/Remote File
	 */
	private String createFileFullPath()
	{ return SmartFile.this.url.toString().replaceFirst("://([^:]+)(?:.+)@", "://$1@"); }
	
	/**
	 * 
	 * @return true if File is Local
	 */
	public boolean isLocal()
	{ return SmartFile.this.remote?("file".equalsIgnoreCase(SmartFile.this.url.getProtocol())):true; }
	
	/**
	 * 
	 * @return true if file is Local and Exist
	 */
	public boolean isLocalAndExists()
	{ return SmartFile.this.remote?false:SmartFile.this.file.exists(); }
	
	/**
	 * 
	 * @return last modified timestamp
	 */
	public long getActualLastModified()
	{ return SmartFile.this.remote?0:SmartFile.this.file.lastModified(); }
	
	/**
	 * 
	 * @return last opened timestamp
	 */
	public long getActualLastOpened()
	{ return SmartFile.this.timestamp; }
	
	/**
	 * 
	 * @return file path
	 */
	public String getFileFullPath()
	{ return SmartFile.this.path; }
	
	/**
	 * 
	 * @return file name
	 */
	public String getFileName()
	{ return SmartFile.this.name; }
	
	/**
	 * Changes last opened timestamp for target File
	 * 
	 * @param timestamp
	 */
	public void setLastOpened(long timestamp)
	{ SmartFile.this.timestamp = timestamp; }
	
	/**
	 * 
	 * @return true if File cannot be edited
	 */
	public boolean isLocked()
	{ return !SmartFile.this.file.canWrite(); }
	
	/**
	 * Can File be Locked?
	 * 
	 * @return true is File can be locked
	 */
	public boolean canLock()
	{ return SmartFile.this.isLocalAndExists(); }
	
	/**
	 * get Localised File (File itself or its temporary copy)
	 * 
	 * @return Localised File
	 */
	public File getLocalFile()
	{ return SmartFile.this.file; }
	
	/**
	 * Sets Bookmark Alias for File
	 * 
	 * @param name alias name
	 */
	public void setAlias(String name)
	{ SmartFile.this.alias = name; }
	
	/**
	 * Get Alias for File
	 * 
	 * @return Bookmark Alias for File
	 */
	public String getAlias()
	{ return SmartFile.this.alias; }

	/**
	 * Get flag for File
	 * 
	 * @return Bookmark Flag
	 */
	public int getFlag()
	{ return this.flag; }
	
	/**
	 * Sets Bookmark Flag
	 * 
	 * @param flag file flag
	 */
	public void setFlag(int flag)
	{ this.flag = flag; }

	/**
	 * 
	 * @param other File
	 * @return comparation
	 */
	@Override public int compareTo(SmartFile other)
	{ return (this.timestamp>other.timestamp)?-1:((this.timestamp<other.timestamp)?1:0); }
	
	/**
	 * destructor description
	 * 
	 */
	public boolean $()
	{
		file	= null;
		url		= null;
		return true;
	}
	
}
package util;

import io.file.SmartFile;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

/**
 * Utility class for working with Files
 * 
 * @author Jan Cajthaml
 *
 */
public class FileUtils
{
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	private FileUtils()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Lockes file
	 * 
	 * @param file target File
	 */
	@SuppressWarnings("resource") public static void lock(File file)
	{
		try
		{
			FileChannel channel	= new RandomAccessFile(file, "rw").getChannel();
		    FileLock lock		= channel.lock(); 
		    try
		    {
		    	lock = channel.tryLock();
		    	file.setWritable(false);
		    }
		    catch (OverlappingFileLockException e){}
		    
		    lock.release(); 
		    channel.close();
		} 
		catch (Exception e){} 
	}

	/**
	 * Unlockes file
	 * 
	 * @param file target File
	 */
	@SuppressWarnings("resource") public static void unlock(File file)
	{
		try
		{
			FileChannel channel	= new RandomAccessFile(file, "rw").getChannel();
		    FileLock lock		= channel.lock(); 
		    try
		    {
		    	lock = channel.tryLock();
		    	file.setWritable(true);
		    }
		    catch (OverlappingFileLockException e){}
		    
		    lock.release(); 
		    channel.close();
		} 
		catch (Exception e){} 
	}

	/**
	 * Lockes SmartFile
	 * 
	 * @param smart target SmartFile
	 */
	public static void lock(SmartFile smart)
	{
		try								{ smart.getLocalFile().setWritable(false, true);	}
		catch(NullPointerException e)	{													}
		catch(SecurityException e)		{													}
		catch(Exception e)				{													}
	}
	
	/**
	 * Unlockes SmartFile
	 * 
	 * @param smart target SmartFile
	 */
	public static void unlock(SmartFile smart)
	{
		try								{ smart.getLocalFile().setWritable(true, true);		}
		catch(NullPointerException e)	{													}
		catch(SecurityException e)		{													}
		catch(Exception e)				{													}	
	}

	/**
	 * Check whenever File is locked
	 */
	public static boolean isLocked(SmartFile file)
	{
		if(file==null) throw new IllegalArgumentException("file does not Exists");
		return file.canLock() && file.isLocked();
	}
	
}
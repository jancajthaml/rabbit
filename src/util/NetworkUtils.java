package util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Network utility class
 * 
 * @author Jan Cajthaml
 *
 */
public class NetworkUtils
{
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	private NetworkUtils()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Checks whenever the destination url exists
	 * 
	 * @param url destination
	 * @return true if url exist
	 */
	public static boolean exists(String url)
	{
		try
		{
			if(url.startsWith("http"))	return existsHTTP(url);
			else						return existsURL(url);
		}
		catch (Exception e)
		{ return false;	}		
	}
	
	/**
	 * Checks if http address exists
	 * 
	 * @param name HTTP address name
	 * @return true if HTTP URL exists
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static boolean existsHTTP(String name) throws MalformedURLException, IOException
	{
		HttpURLConnection.setFollowRedirects(false);
	    HttpURLConnection huc = (HttpURLConnection)  (new URL(name)).openConnection();
	    huc.setRequestMethod("GET");
	    int response = huc.getResponseCode();
	    return (response == 200) || (response == 302);
	}
	
	/**
	 * Checks if url address exists
	 * 
	 * @param name URL address name
	 * @return true if URL exists
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static boolean existsURL(String name) throws MalformedURLException, IOException
	{
		new URL(name).openConnection();
		return true;
	}
	
}
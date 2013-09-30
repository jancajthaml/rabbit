package util.storage;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;

/**
 * Prefference Utility class
 * 
 * @author Jan Cajthaml
 *
 */
public class PrefferenceStorage
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	/**
	 * Platform dependent "Control" key
	 */
	public static final int CTRL								= Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	/**
	 * Current Theme name
	 */
	public static String STYLESHEET								= "default";
	public static boolean HOLD_INIT								= false;
	private static String theme									= "default";
	private static String path									= new File("").getAbsolutePath();
	private static final HashMap<String,Integer> keys			= new HashMap<String,Integer>();
	private static final HashMap<String,Integer[]> modifiers	= new HashMap<String,Integer[]>();
	private static final HashMap<String,Integer> integers		= new HashMap<String,Integer>();
	private static final HashMap<String,Boolean> bools			= new HashMap<String,Boolean>();
	private static final PrefferenceContentHandler handler		= new PrefferenceContentHandler();

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private PrefferenceStorage()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Sets bool property name
	 * 
	 * @param name boolean name
	 * @param b boolean value
	 */
	public static void setBool(String name, boolean b)
	{ if(bools.containsKey(name)) bools.put(name, b); }
	
	/**
	 * Sets int property name
	 * 
	 * @param name integer name
	 * @param i integer value
	 */
	public static void setInt(String name, int i)
	{ if(integers.containsKey(name)) integers.put(name, i); }
	
	/**
	 * Gets int property
	 * 
	 * @param name integer name
	 * @return int value
	 */
	public static int getInt(String name)
	{
		if(!integers.containsKey(name))
		{
			System.err.println("int "+name+" does not exists");
			return 0;
		}
		return integers.get(name);
	}
	
	/**
	 * Gets bool property
	 * 
	 * @param name bool name
	 * @return bool value
	 */
	public static boolean getBool(String name)
	{
		if(!bools.containsKey(name))
		{
			System.err.println("bool "+name+" does not exists");
			return false;
		}
		return  bools.get(name);
	}
	
	/**
	 * load and initialize
	 */
	public static void init()
	{
		load();
		setInt("tab.height",Math.max(10, Math.min(getInt("tab.height"),50)));
		setInt("header.height",getInt("tab.height")+10);
		UIManager.put("List.lockToPositionOnScroll", Boolean.FALSE);
	}
	
	/**
	 * Gets keystroke property
	 * 
	 * @param key keystroke name
	 * 
	 * @return keystroke value
	 */
	public static KeyStroke getKeyStroke(String key)
	{
		Integer[] mod = getModifiers(key);
		if((mod.length==0))		return KeyStroke.getKeyStroke((char)getMask(key));
		else if(mod.length>1)	return KeyStroke.getKeyStroke(getMask(key) , mod[0]|mod[1]);
		else					return KeyStroke.getKeyStroke(getMask(key) , mod[0]);
	}

	
	/**
	 * print all loaded Integers
	 */
	public static void dumpInt()
	{
												System.err.println("\n### Dumping INTEGERS ");
		for(String key : integers.keySet())		System.err.println(key+"\t --> \t"+getInt(key));
	}
	
	/**
	 * print all loaded KeyStrokes
	 */
	public static void dumpKey()
	{
												System.err.println("\n### Dumping KEYSTROKES ");
		for(String key : keys.keySet())			System.err.println(key+"\t --> \t"+getKeyStroke(key));
	}
	
	/**
	 * print all loaded Booleans
	 */
	public static void dumpBools()
	{
												System.err.println("\n### Dumping BOOLEANS ");
		for(String key : bools.keySet())		System.err.println(key+"\t --> \t"+getBool(key));
	}


	private static int decodeInt(String value)
	{
		try								{ return Integer.parseInt(value);	}
		catch(NumberFormatException e)	{ return 0;							}
	}
	
	private  static boolean decodeBool(String value)
	{
		return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t");
	}

	/**
	 * 
	 * @param name
	 * @return Key Modifiers
	 */
	private static Integer[] getModifiers(String name)
	{
		if(!modifiers.containsKey(name))	return new Integer[]{0};
		Integer[] i = modifiers.get(name);
		return (i.length==1 && i[0]==-1) ? new Integer[]{} : modifiers.get(name);
	}
	
	/**
	 * 
	 * @param name
	 * @return Key Mask
	 */
	private static int getMask(String name)
	{
		if(!keys.containsKey(name))
		{
			System.err.println(name+" pref mask not found");
			return KeyEvent.CHAR_UNDEFINED;
		}
		return keys.get(name);
	}

	/**
	 * load prefferences from target XML(s)
	 */
	private static void load()
	{
		String sourcecursor = path+"/../data/prefferences/"+theme+".xml";
		
		try
		{
			XMLReader parser = XMLReaderFactory.createXMLReader();
			parser.setContentHandler(handler);
			parser.parse(new InputSource(sourcecursor));
		}
		catch (Exception e) { e.printStackTrace(); }
		finally
		{
			handler.fillKeys(keys,modifiers);
			handler.fillBools(bools);
			handler.fillInts(integers);
		}

		Runtime.getRuntime().addShutdownHook(new Thread()
		{ public void run() { save(); } });
	}
	
	/**
	 * save prefferences to XML from which this prefference was loaded
	 */
	private static void save()
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(path+"/../data/prefferences/"+theme+".xml"));
			out.write(handler.xmlFor(keys,modifiers,integers,bools));
			out.close();
		}
		catch (Exception e)
		{ System.err.println("Error Saving prefferences: " + e.getMessage()); }
	}

    // >-------[Helper methods]---------------------------------------------------------------------------------------< //
	
    static int decodeKey(String value)
	{
		if(value.length()==1)			return value.toUpperCase().charAt(0);
		value=value.toLowerCase();
		if(value.equals("null"))		return -1;
		if(value.equals("ctrl"))		return PrefferenceStorage.CTRL;
		if(value.equals("home"))		return KeyEvent.VK_HOME;
		if(value.equals("left"))		return KeyEvent.VK_LEFT;
		if(value.equals("right"))		return KeyEvent.VK_RIGHT;
		if(value.equals("down"))		return KeyEvent.VK_DOWN;
		if(value.equals("up"))			return KeyEvent.VK_UP;
		if(value.equals("alt"))			return KeyEvent.ALT_DOWN_MASK;
		if(value.equals("enter"))		return KeyEvent.VK_ENTER;
		if(value.equals("end"))			return KeyEvent.VK_END;
		if(value.equals("tab"))			return KeyEvent.VK_TAB;
		if(value.equals("shift"))		return InputEvent.SHIFT_MASK;
		if(value.equals("slash")) 		return KeyEvent.VK_SLASH;
		if(value.equals("space")) 		return KeyEvent.VK_SPACE;
		if(value.equals("del")) 		return KeyEvent.VK_DELETE;
		if(value.equals("backspace"))	return KeyEvent.VK_BACK_SPACE;
		if(value.equals("minus"))		return KeyEvent.VK_MINUS;
		if(value.equals("plus"))		return KeyEvent.VK_PLUS;
		if(value.equals("div"))			return KeyEvent.VK_DIVIDE;
		if(value.equals("mult"))		return KeyEvent.VK_MULTIPLY;
		if(value.equals("open"))		return KeyEvent.VK_OPEN_BRACKET;
		if(value.equals("pageUp"))		return KeyEvent.VK_PAGE_UP;
		if(value.equals("insert"))		return KeyEvent.VK_INSERT;
		if(value.equals("pageDown"))	return KeyEvent.VK_PAGE_DOWN;
		return KeyEvent.CHAR_UNDEFINED;
	}
    
    static String encodeKey(int value)
	{
    	switch(value)
    	{
    		case -1							: return "null";
    		case KeyEvent.VK_HOME			: return "home";
    		case KeyEvent.VK_LEFT			: return "left";
    		case KeyEvent.VK_RIGHT			: return "right";
    		case KeyEvent.VK_DOWN			: return "down";
    		case KeyEvent.ALT_DOWN_MASK		: return "alt";
    		case KeyEvent.VK_UP				: return "up";
    		case KeyEvent.VK_ENTER			: return "enter";
    		case KeyEvent.VK_END			: return "end";
    		case KeyEvent.VK_TAB			: return "tab";
    		case InputEvent.SHIFT_MASK		: return "shift";
    		case KeyEvent.VK_SLASH			: return "slash";
    		case KeyEvent.VK_DELETE			: return "del";
    		case KeyEvent.VK_BACK_SPACE		: return "backspace";
    		case KeyEvent.VK_MINUS			: return "minus";
    		case KeyEvent.VK_PLUS			: return "plus";
    		case KeyEvent.VK_DIVIDE			: return "div";
    		case KeyEvent.VK_MULTIPLY		: return "mult";
    		case KeyEvent.VK_OPEN_BRACKET	: return "open";
    		case KeyEvent.VK_PAGE_UP		: return "pageUp";
    		case KeyEvent.VK_INSERT			: return "insert";
    		case KeyEvent.VK_SPACE			: return "space";
    		case KeyEvent.VK_PAGE_DOWN		: return "pageDown";
    		case KeyEvent.CHAR_UNDEFINED	: return "";
    		default 						: return (value==PrefferenceStorage.CTRL)?"ctrl":String.valueOf((char)value);
    	}
	}

    //FIXME refactor & indent
    private static class PrefferenceContentHandler implements ContentHandler
    {
	
    	String cursor			= "";
    	String namespace		= "";
    	String tabs				= "";
    	static final String TAB	= "\t";
    	StringBuffer buffer		= new StringBuffer();
    
    	//HELPER classes
    	Key _key		= null;
    	Int _int		= null;
    	Bool _bool		= null;
	
    	public void setDocumentLocator(Locator locator)
    	{}

    	public String xmlFor(HashMap<String, Integer> keys2, HashMap<String, Integer[]> modifiers, HashMap<String, Integer> integers, HashMap<String, Boolean> bools2)
    	{
    		this.keys.clear();
    		this.ints.clear();
    		this.bools.clear();

    		for(String k : keys2.keySet())
    		{
    			Key key		= new Key();
    			key.image	= k;
    			key.value	= encodeKeyStroke(modifiers.get(k),keys2.get(k));
    			keys.add(key);
    		}
    	
    		for(String i : integers.keySet())
    		{
    			Int in		= new Int();
    			in.image	= i;
    			in.value	= String.valueOf(integers.get(i));
    			ints.add(in);
    		}
    	
    		for(String b : bools2.keySet())
    		{
    			Bool bool	= new Bool();
    			bool.image	= b;
    			bool.value	= bools2.get(b)?"true":"false";
    			bools.add(bool);
    		}

    		return toXML();
    	}

    	private String encodeKeyStroke(Integer[] mods, Integer key)
    	{
    		if(mods==null)		return encodeKey(key);
    		if(mods.length==2)	return encodeKey(mods[0])+"|"+encodeKey(mods[1])+"+"+encodeKey(key);
    		if(mods.length==1)	return encodeKey(mods[0])+"+"+encodeKey(key);
    							return encodeKey(key);
    	}

    	public void fillInts(HashMap<String, Integer> map)
    	{
    		for(Int i : ints)		map.put(i.image, decodeInt(i.value));
    	}

    	public void fillBools(HashMap<String, Boolean> map)
    	{
    		for(Bool bool : bools)	map.put(bool.image, decodeBool(bool.value));		
    	}

    	public void fillKeys(HashMap<String, Integer> map, HashMap<String, Integer[]> modifiers)
    	{
    		for(Key key : keys)
    		{
    			String[] s = key.value.split("\\+");
    			if(s.length>1)
    			{
    				String[] l = s[0].split("\\|");
				
    				if(l.length>1)
    				{
    					Integer[] r = new Integer[l.length];
    					map.put(key.image, decodeKey(s[1].trim()));
    					for(int i=0; i<l.length; i++) r[i]=decodeKey(l[i].trim());
    					modifiers.put(key.image, r);
    				}
    				else
    				{
    					map.put(key.image, decodeKey(s[1].trim()));
    					modifiers.put(key.image, new Integer[]{decodeKey(s[0].trim())});
    				}
    			}
    			else map.put(key.image, decodeKey(key.value.trim()));
    		}
    	}

    	public void startDocument() throws SAXException
    	{
    		cursor = "/";
    		buffer.delete(0, buffer.length());
    	}
        
    	public void endDocument() throws SAXException
    	{}
    
    	private String toXML()
    	{
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<Theme>\n");
    						
    		for(Key key : keys)	
    			buffer.append("\t<Key image=\""+key.image+"\" value=\""+key.value+"\"/>\n");

    		buffer.append("\n");
    						
    		for(Int i : ints)	buffer.append("\t<Int image=\""+i.image+"\" value=\""+i.value+"\"/>\n");
    							buffer.append("\n");
    		
    		for(Bool bool : bools)	buffer.append("\t<Bool image=\""+bool.image+"\" value=\""+bool.value+"\"/>\n");
    								buffer.append("\n");
    	
    								buffer.append("\t<Skin value=\""+PrefferenceStorage.STYLESHEET+"\"/>\n");
    	
    								buffer.append("</Theme>");
    		return buffer.toString();
    	}
    
    	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
    	{
    		buffer.append(tabs+"<"+qName+namespace+">\n");

    		tabs += TAB;

    		if (namespace.length() > 0) namespace = "";
    		cursor += localName + "/";

    		if (cursor.equals("/Theme/Key/"))		_key		= new Key();
			if (cursor.equals("/Theme/Int/"))		_int		= new Int();
			if (cursor.equals("/Theme/Bool/"))		_bool		= new Bool();
		
			for (int i = 0; i < atts.getLength(); i++)
			{
				String name		= atts.getQName(i);
				String value	= atts.getValue(i);
			
				//FIXME ugly temporary fix
				if(cursor.equals("/Theme/Skin/") && name.equalsIgnoreCase("value"))	PrefferenceStorage.STYLESHEET=value;
			
				if(cursor.equals("/Theme/Key/") && name.equalsIgnoreCase("image"))		_key.image=value;
				if(cursor.equals("/Theme/Key/") && name.equalsIgnoreCase("value"))		_key.value=value;
			
				if(cursor.equals("/Theme/Bool/") && name.equalsIgnoreCase("image"))		_bool.image=value;
				if(cursor.equals("/Theme/Bool/") && name.equalsIgnoreCase("value"))		_bool.value=value;
			
				if(cursor.equals("/Theme/Int/") && name.equalsIgnoreCase("image"))		_int.image=value;
				if(cursor.equals("/Theme/Int/") && name.equalsIgnoreCase("value"))		_int.value=value;
			
				buffer.append(tabs+"<"+name+namespace+">\n"+tabs+value+namespace+"\n"+tabs+"<"+name+">"+namespace+"\n");
			}
    	}
    	
    	public void endElement(String uri, String localName, String qName) throws SAXException
    	{
    		tabs = tabs.substring(1);
    		buffer.append(tabs+"</"+qName+">\n");
    
    		if (cursor.equals("/Theme/Bool/"))					bools.add(_bool);
    		if (cursor.equals("/Theme/Int/"))					ints.add(_int);
    		if (cursor.equals("/Theme/Key/"))					keys.add(_key);
		
    		cursor = cursor.substring(0, cursor.lastIndexOf("/"));
    		cursor = cursor.substring(0, cursor.lastIndexOf("/") + 1);
    	}
        
    	public void characters(char[] ch, int start, int length) throws SAXException
    	{}
    
    	public void startPrefixMapping(String prefix, String uri) throws SAXException
    	{ namespace += (prefix.length() == 0)?(" xmlns=\"" + uri + "\""):(" xmlns:" + prefix + "=\"" + uri + "\""); }

    	public void endPrefixMapping(String prefix) throws SAXException
    	{}

    	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
    	{}
    	public void processingInstruction(String target, String data) throws SAXException
    	{}
    	
    	public void skippedEntity(String name) throws SAXException
    	{}
        
    	// >-------[Helper classes]---------------------------------------------------------------------------------------< //
	
    	LinkedList<Int> ints		= new LinkedList<Int>();
    	LinkedList<Bool> bools		= new LinkedList<Bool>();
    	LinkedList<Key> keys		= new LinkedList<Key>();
    
    	private class Int
    	{
    		String image				= "";
    		String value				= "";
    	}
    
    	private class Bool
    	{
    		String image				= "";
    		String value				= "";
    	}
    
    	private class Key
    	{
    		String image				= "";
    		String value				= "";
    	}
    }
    static
	{
		if(!HOLD_INIT)
		{
			init();
		}
	}
}
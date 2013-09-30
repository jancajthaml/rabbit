package util.storage;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import util.update.Watcher;

/**
 * Represents Stylesheet for Application
 * 
 * @author Jan Cajthaml
 *
 */
//FIXME refactor ColorTheme like other Storages
public class ColorTheme
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	protected static final DynamicColor NILC	= new DynamicColor("NIL");
	protected static final DynamicPaint NILP	= new DynamicPaint("NIL");
	private String name							= "";
	private HashMap<String,DynamicColor> colors	= new HashMap<String,DynamicColor>();
	private HashMap<String,DynamicPaint> paints	= new HashMap<String,DynamicPaint>();
	private HashMap<String,Boolean> bools		= new HashMap<String,Boolean>();
	private HashMap<String,String> icons		= new HashMap<String,String>();

	/**
	 * 
	 */
	private final DefaultHandler xmlReader = new DefaultHandler()
	{
		public void startElement(String uri, String localName,String qName, Attributes attrs) throws SAXException
		{
			if (qName.equalsIgnoreCase("Hex"))
			{
				String key="";
				String value="";
				String opacity = "255";
				int length = attrs.getLength();
				for (int i=0; i<length; i++)
				{
					if(attrs.getQName(i).equalsIgnoreCase("image")) key			= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("value")) value		= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("opacity")) opacity	= attrs.getValue(i);
				}
				Color c	= NILC.getColor();
				int o	= 255;
				
				try
				{
					c=Color.decode("0x"+value.trim().toUpperCase());
					o=Integer.parseInt(opacity);
				}
				catch(NumberFormatException nee){}
				
				create(key,new Color(c.getRed(),c.getGreen(),c.getBlue(),o));
			}
			if (qName.equalsIgnoreCase("Rgb"))
			{
				String key="";
				String value="";
				String opacity = "255";
				int length = attrs.getLength();
				for (int i=0; i<length; i++)
				{
					if(attrs.getQName(i).equalsIgnoreCase("image")) key			= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("value")) value		= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("opacity")) opacity	= attrs.getValue(i);
				}
				Color c	= NILC.getColor();
				int o	= 255;
				try
				{
					String[] s = value.trim().toUpperCase().split(",");
					c=new Color(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]));
					o=Integer.parseInt(opacity);
				}
				catch(NumberFormatException nee){}
				catch(IndexOutOfBoundsException e){}
				
				create(key,new Color(c.getRed(),c.getGreen(),c.getBlue(),o));
			}
			
			if (qName.equalsIgnoreCase("Gradient"))
			{
				String Start="";
				String Stop="";
				String BreakX="";
				String BreakY="";
				String key="";
				String loop = "";
				int length = attrs.getLength();
				for (int i=0; i<length; i++)
				{
					if(attrs.getQName(i).equalsIgnoreCase("image")) key		= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("start")) Start	= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("stop"))	Stop	= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("breakX")) BreakX	= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("breakY")) BreakY	= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("loop")) loop	= attrs.getValue(i);
				}
				Paint c = NILP.getPaint();
				try
				{
					try									{ c=new GradientPaint(0,0,Color.decode("0x"+Start.trim().toUpperCase()),Integer.parseInt(BreakX),Integer.parseInt(BreakY),Color.decode("0x"+Stop.trim().toUpperCase()),loop.equalsIgnoreCase("true"));	}
					catch(NumberFormatException nee)	{																																																		}
				}
				catch(NumberFormatException nee){}
				catch(IndexOutOfBoundsException e){}
				create(key,c);
			}
			if (qName.equalsIgnoreCase("Icon"))
			{
				String key="";
				String value="";
				int length = attrs.getLength();
				for (int i=0; i<length; i++)
				{
					if(attrs.getQName(i).equalsIgnoreCase("image")) key		= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("value")) value	= attrs.getValue(i);
				}
				create(key,value);
			}
			
			if (qName.equalsIgnoreCase("Bool"))
			{
				String key="";
				boolean value=false;
				int length = attrs.getLength();
				for (int i=0; i<length; i++)
				{
					if(attrs.getQName(i).equalsIgnoreCase("image")) key		= attrs.getValue(i);
					if(attrs.getQName(i).equalsIgnoreCase("value")) value	= attrs.getValue(i).equalsIgnoreCase("true");
				}
				create(key,value);
			}
		}

		public void endElement(String uri, String localName,String qName) throws SAXException{}
		public void characters(char ch[], int start, int length) throws SAXException{}
	};
	//FIXME optimise use something like static shared instance at SystemUtils
	private static String path							= new File("").getAbsolutePath();
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * 
	 * @param themeName
	 */
	protected ColorTheme(String themeName)
	{ load(this.name = themeName); }
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * 
	 * @param name
	 */
	protected void load(String name)
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			name=path+"/../data/themes/"+name+".xml";
			saxParser.parse(name, xmlReader);
		}
		catch (ParserConfigurationException e)	{ e.printStackTrace(); }
		catch (SAXException e)					{ e.printStackTrace(); }
		catch (IOException e)					{ e.printStackTrace(); }
	}
	

	/**
	 * 
	 * @param name
	 * @param c
	 */
	private void create(String name, Paint c)
	{
		DynamicPaint dc = new DynamicPaint(name);
		dc.setColor(c);
		paints.put(name, dc);
	}
	

	/**
	 * 
	 * @param name
	 * @param c
	 */
	private void create(String name, boolean bool)
	{ bools.put(name, bool); }
	
	/**
	 * 
	 * @param name
	 * @param c
	 */
	private void create(String name, Color c)
	{
		DynamicColor dc = new DynamicColor(name);
		dc.setColor(c);
		colors.put(name, dc);
	}
	
	/**
	 * 
	 * @param name
	 * @param c
	 */
	private void create(String name, String c)
	{ icons.put(name, c); }

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected DynamicColor getColor(String name)
	{
		if(colors.containsKey(name)) return colors.get(name);
		System.err.println(name+"\t\tCOLOR\tnot found in theme "+this.toString());
		return NILC;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected DynamicPaint getPaint(String name)
	{
		if(paints.containsKey(name)) return paints.get(name);
		System.err.println(name+"\t\tPAINT\tnot found in theme "+this.toString());
		return NILP;
	}
	
	/**
	 * 
	 * @param name
	 * @param c
	 */
	protected void setColor(String name, Color c)
	{
		if(!colors.containsKey(name)) return;
		colors.get(name).setColor(c);
	}
	
	/**
	 * 
	 * @return
	 */
	protected String[] toArray()
	{ return colors.keySet().toArray(new String[0]); }
	
	/**
	 * 
	 */
	public String toString()
	{ return this.name; }
	
	/**
	 * 
	 */
	protected void cascadeUpdate()
	{ for(DynamicColor color : colors.values()) color.demainUpdate(); }

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected Image getIcon(String name)
	{ return IconStorage.get(icons.get(name)); }

	private static class IconStorage
	{	
		//FIXME optimise
		private static String path							= new File("").getAbsolutePath()+"/../data/icons";		
		private static final HashMap<String,BufferedImage> icons	= new HashMap<String,BufferedImage>();

		public static BufferedImage get(String name)
		{ return (!IconStorage.icons.containsKey(name))?new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB):IconStorage.icons.get(name); }
		
		public static Image load(String name,String simplename)
		{
			loadIcon(simplename,IconStorage.path+"/"+name);	
			return IconStorage.icons.get(simplename);
		}
		
		private static void loadIcon(String simplename,String name)
		{
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			try						{ img = ImageIO.read(new File(name));	}
			catch (IOException e)	{										}
			IconStorage.icons.put(simplename, img);
		}

		static
		{
			load("popup.png"			, "popup"	);
			load("ribbon_shadow.png"	, "ribbonShadow"	);
			
			load("about.png"			, "about"			);
			load("all.png"				, "all"				);

			Image image				= ColorStorage.getSharedIcon("all");
			BufferedImage source	= new BufferedImage(image.getWidth(null), image.getHeight(null),Transparency.BITMASK);
			Graphics t				= source.createGraphics();
			t.drawImage(image, 0, 0, null);
			
			IconStorage.icons.put("add16", crop(source,0,0,16,16));
			IconStorage.icons.put("php16",crop(source,0,16,16,16));	
			IconStorage.icons.put("sql16",crop(source,0,32,16,16));
			IconStorage.icons.put("plain16",crop(source,0,48,16,16));
			IconStorage.icons.put("html16",crop(source,0,64,16,16));	
			IconStorage.icons.put("java16",crop(source,0,80,16,16));
			IconStorage.icons.put("loading16",crop(source,0,96,16,16));
			IconStorage.icons.put("resize16",crop(source,0,112,16,16));
			IconStorage.icons.put("task16",crop(source,0,128,16,16));
			IconStorage.icons.put("fatal16",crop(source,0,144,16,16));
			IconStorage.icons.put("beats16",crop(source,0,160,16,16));
			IconStorage.icons.put("closeON",crop(source,0,176,16,16));
			IconStorage.icons.put("foldExpandedUp",crop(source,0,192,16,16));
			IconStorage.icons.put("arrow",crop(source,0,208,16,16));
			IconStorage.icons.put("canon16",crop(source,16,0,16,16));
			IconStorage.icons.put("file16",crop(source,16,16,16,16));
			IconStorage.icons.put("warring16",crop(source,16,32,16,16));
			IconStorage.icons.put("lense16",crop(source,16,48,16,16));
			IconStorage.icons.put("apple16",crop(source,16,64,16,16));
			
			IconStorage.icons.put("breakpointBlack16",crop(source,16,112,16,16));
			IconStorage.icons.put("favorite16",crop(source,16,128,16,16));
			IconStorage.icons.put("breakpointWhite16",crop(source,16,144,16,16));
			IconStorage.icons.put("closeOFF",crop(source,16,160,16,16));
			IconStorage.icons.put("alert16",crop(source,16,176,16,16));
			IconStorage.icons.put("foldCollapsed",crop(source,16,192,16,16));
			IconStorage.icons.put("foldExpandedDown",crop(source,16,208,16,16));
			IconStorage.icons.put("stats20",crop(source,32,0,20,20));
			IconStorage.icons.put("remote20",crop(source,32,20,20,20));
			IconStorage.icons.put("save20",crop(source,32,40,20,20));
			IconStorage.icons.put("reload20",crop(source,32,60,20,20));
			IconStorage.icons.put("graph20",crop(source,32,80,20,20));
			IconStorage.icons.put("download20",crop(source,32,100,20,20));
			IconStorage.icons.put("upload20",crop(source,32,120,20,20));
			IconStorage.icons.put("about20",crop(source,52,0,20,20));
			IconStorage.icons.put("help20",crop(source,52,20,20,20));
			IconStorage.icons.put("unlocked20",crop(source,52,40,20,20));
			IconStorage.icons.put("locked20",crop(source,52,60,20,20));
			IconStorage.icons.put("local20",crop(source,52,80,20,20));
			IconStorage.icons.put("search20",crop(source,52,120,20,20));
			IconStorage.icons.put("error16",crop(source,72,0,150,50));
			IconStorage.icons.put("modified16",crop(source,72,50,150,50));
			IconStorage.icons.put("ok16",crop(source,72,100,150,50));
			IconStorage.icons.put("unknown16",crop(source,72,150,150,50));
			IconStorage.icons.put("dialog_alert",crop(source,72,200,33,33));
			IconStorage.icons.put("dialog_folder",crop(source,105,200,33,33));
			IconStorage.icons.put("dialog_user",crop(source,138,200,33,33));
			IconStorage.icons.put("local16BLACK",crop(source,74,0,16,16));
			IconStorage.icons.put("remote16BLACK",crop(source,74,16,16,16));
			IconStorage.icons.put("trash16",crop(source,74,32,16,16));
			IconStorage.icons.put("workspace16",crop(source,74,48,16,16));
			IconStorage.icons.put("edit16",crop(source,90,0,16,16));
			
			IconStorage.icons.put("bookmark16",crop(source,90,48,16,16));
			IconStorage.icons.put("statusOK",crop(source,113,0,127,54));
			IconStorage.icons.put("statusERROR",crop(source,113,54,127,54));
			IconStorage.icons.put("statusALERT",crop(source,240,0,127,54));
			IconStorage.icons.put("statusDEF",crop(source,240,54,127,54));
			IconStorage.icons.put("search",crop(source,74,64,16,16));
			IconStorage.icons.put("prefference16",crop(source,74,80,16,16));
			IconStorage.icons.put("close.disabled",crop(source,90,64,16,16));
			IconStorage.icons.put("maxON",crop(source,73,96,20,20));
			IconStorage.icons.put("minON",crop(source,73,96+20,20,20));
			IconStorage.icons.put("exitON",crop(source,73,96+40,20,20));
			IconStorage.icons.put("exitModifiedON",crop(source,73,96+60,20,20));
			
			IconStorage.icons.put("maxOFF",crop(source,93,96,20,20));
			IconStorage.icons.put("minOFF",crop(source,93,96+20,20,20));
			IconStorage.icons.put("exitOFF",crop(source,93,96+40,20,20));
			IconStorage.icons.put("exitModifiedOFF",crop(source,93,96+60,20,20));

			IconStorage.icons.put("popup_save",crop(source,304,153,16,16));
			IconStorage.icons.put("popup_saveAs",crop(source,304,169,16,16));
			IconStorage.icons.put("popup_copy",crop(source,304,185,16,16));
			IconStorage.icons.put("popup_cut",crop(source,304,201,16,16));
			IconStorage.icons.put("popup_delete",crop(source,304,217,16,16));
			IconStorage.icons.put("popup_save_off",crop(source,320,153,16,16));
			IconStorage.icons.put("popup_saveAs_off",crop(source,320,169,16,16));
			IconStorage.icons.put("popup_copy_off",crop(source,320,185,16,16));
			IconStorage.icons.put("popup_cut_off",crop(source,320,201,16,16));
			IconStorage.icons.put("popup_delete_off",crop(source,320,217,16,16));
			IconStorage.icons.put("popup_paste",crop(source,336,153,16,16));
			IconStorage.icons.put("popup_redo",crop(source,336,169,16,16));
			IconStorage.icons.put("popup_selectAll",crop(source,336,185,16,16));
			IconStorage.icons.put("popup_undo",crop(source,336,201,16,16));
			IconStorage.icons.put("popup_addFlag",crop(source,336,217,16,16));
			IconStorage.icons.put("popup_paste_off",crop(source,352,153,16,16));
			IconStorage.icons.put("popup_redo_off",crop(source,352,169,16,16));
			IconStorage.icons.put("popup_selectAll_off",crop(source,352,185,16,16));
			IconStorage.icons.put("popup_undo_off",crop(source,352,201,16,16));
			IconStorage.icons.put("popup_addFlag_off",crop(source,352,217,16,16));
			IconStorage.icons.put("popup_removeFlag",crop(source,368,153,16,16));
			IconStorage.icons.put("popup_fold_collapse",crop(source,368,169,16,16));
			IconStorage.icons.put("popup_fold_expand",crop(source,368,185,16,16));
			IconStorage.icons.put("popup_fold",crop(source,368,201,16,16));
			IconStorage.icons.put("popup_sub",crop(source,368,217,16,16));
			IconStorage.icons.put("popup_removeFlag_off",crop(source,384,153,16,16));
			IconStorage.icons.put("popup_fold_collapse_off",crop(source,384,169,16,16));
			IconStorage.icons.put("popup_fold_expand_off",crop(source,384,185,16,16));
			IconStorage.icons.put("popup_fold_off",crop(source,384,201,16,16));
			IconStorage.icons.put("popup_sub_off",crop(source,384,217,16,16));
			
			IconStorage.icons.put("parser16",crop(source,36,142,16,16));
			IconStorage.icons.put("note16",crop(source,36,158,16,16));
			IconStorage.icons.put("core16",crop(source,36,174,16,16));
			IconStorage.icons.put("stars1",crop(source,52,142,16,16));
			IconStorage.icons.put("stars2",crop(source,52,158,16,16));
			IconStorage.icons.put("stars3",crop(source,52,174,16,16));
			IconStorage.icons.put("stars4",crop(source,52,190,16,16));
			IconStorage.icons.put("stars5",crop(source,52,206,16,16));
			
			load("footer_separator.png"				, "footerSeparator"		);
			
			//FIXME fuse
			load("check_box_blank.png"				, "checkBoxBlank"		);
			load("check_box_full.png"				, "checkBoxFull"		);
			load("radio_button_blank.png"			, "radioBoxBlank"		);
			load("radio_button_full.png"			, "radioBoxFull"		);
			
			
			load("help/parser.png"					, "HELPparser"		);
			load("help/bookmarks.png"				, "HELPbookmarks"		);
			load("help/close.png"					, "HELPclose"			);
			load("help/fullscreen.png"				, "HELPfullscreen"		);
			load("help/plus.png"					, "HELPplus"			);
			load("help/debug.png"					, "HELPdebug"			);
			load("help/editWorkspace.png"			, "HELPeditWorkspace"	);
			load("help/localRemote.png"				, "HELPlocalRemote"		);
			load("help/thread.png"					, "HELPthread"			);
			
			load("ok16.png"				, "ok"	);
			
			load("help/Panimations.png"				, "HELPprefAnimations"	);
			load("help/Pdebug.png"					, "HELPprefDebug"		);
			load("help/Plog.png"					, "HELPprefLog"			);
			load("help/Pscroll.png"					, "HELPprefScroll"		);
			load("toolbar/help.png"					, "tool_help"	);
			load("toolbar/debug.png"				, "tool_debug"	);
			load("toolbar/debugA.png"				, "tool_debug_auto"	);
			load("toolbar/local.png"				, "tool_local"	);
			
			//FIXME deleting
			load("favorite_on.png"					, "favoriteON"	);
			load("favorite_off.png"					, "favoriteOFF"	);
			load("inputCLOSE.png"					, "inputCLOSE"	);
			
			load("ruby_16x16.png"					, "ruby16"	);
			load("xml_16x16.png"					, "xml16"	);
			load("latex_16x16.png"					, "latex16"	);
			load("html_16x16.png"					, "html16"	);
			load("shell_16x16.png"					, "shell16"	);
			
			load("tag.png"							, "tag16"	);
			load("arrow_right.png"					, "arrowRight"	);
			
			load("splash1.png"						, "splash_1");
			load("splash2.png"						, "splash_2");
			load("splash3.png"						, "splash_3");
			load("splash4.png"						, "splash_4");
			load("splash5.png"						, "splash_5");
			load("splash6.png"						, "splash_6");
			load("screen.png"						, "screen");
			load("notice.png"						, "notice16");
			
			load("large/php256.png"					, "php256");
			load("large/plain256.png"				, "plain256");
			load("large/html256.png"				, "html256");
			load("large/xml256.png"					, "xml256");
			load("large/sql256.png"					, "sql256");
			load("large/java256.png"				, "java256");
			load("large/ruby256.png"				, "ruby256");
			load("large/shell256.png"				, "shell256");
			
			load("editBlack16.png"				, "editBlack");
			load("editWhite16.png"				, "editWhite");
			
			load("workspaceWhite16.png"				, "workspaceWhite");
			load("workspaceBlack16.png"				, "workspaceBlack");
			

			IconStorage.icons.put("folderOpen16",crop(source,16,80,16,16));
			IconStorage.icons.put("folderClosed16",crop(source,16,96,16,16));
			
			load("folder_black_open.png"						, "folderBlackOpen16");
			load("folder_black_closed.png"						, "folderBlackClosed16");
			
			load("slider_knob.png"								, "slider.knob");
			load("turner_knob.png"								, "turner.knob");
			
			
			IconStorage.icons.put("transparentPixel",crop(source,0,0,1,1));
			
			
			icons.remove("all");
		}
	}
	
	/**
	 * 
	 * @param source
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	private static BufferedImage crop(BufferedImage source, int x, int y, int width, int height)
	{ return source.getSubimage(x, y, width, height); }
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	protected static BufferedImage getShaderIcon(String name)
	{ return IconStorage.get(name); }

	/**
	 * 
	 */
	protected static void disposeSplash()
	{
		IconStorage.icons.remove("splash_1");
		IconStorage.icons.remove("splash_2");
		IconStorage.icons.remove("splash_3");
		IconStorage.icons.remove("splash_4");
		IconStorage.icons.remove("splash_5");
		IconStorage.icons.remove("splash_6");
	}

	/**
	 * 
	 * @param name
	 * @param watcher
	 */
	protected void attach(String name, Watcher watcher)
	{
		if(this.colors.containsKey(name))		this.colors.get(name).attach(watcher);
		else if(this.paints.containsKey(name))	this.paints.get(name).attach(watcher);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected boolean getBool(String name)
	{
		if(bools.containsKey(name)) return  bools.get(name);
		System.err.println("bool "+name+" does not exists");
		return false;
	}

}
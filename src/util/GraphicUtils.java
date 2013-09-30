package util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import static util.calculations.Blur.blur;

/**
 * Graphic Utilities
 * 
 * @author Jan Cajthaml
 *
 */
public class GraphicUtils
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	/**
	 * DOTTED stroke
	 */
	public static final Stroke DOTTED_STROKE	= new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1, new float[] { 1f, 0f, 0f }, 1f);
	/**
	 * DASHED stroke
	 */
	public static final Stroke DASHED_STROKE	= new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1, new float[] { 1f, 0f, 1f }, 1);;
	/**
	 * Black plastic noise
	 */
	private static BufferedImage noise_black		= null;
	/**
	 * White plastic noise
	 */
	private static BufferedImage noise_white		= null;
	/**
	 * Screen resolution
	 */
	private static Dimension screen						= new Dimension(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth(),GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	private GraphicUtils()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Text Shadow
	 * 
	 * @param g graphics
	 * @param value text
	 * @param x X offset
	 * @param y Y offset
	 */
	//FIXME use enum + center/leftText
	public static void shadow(Graphics2D g, String value,int x,int y)
	{
		Paint p = g.getPaint();
		g.setPaint(new Color(0,0,0,180));
		g.drawString(value, x, y+1);
		g.setPaint(p);
		g.drawString(value, x, y);
	}
		
	/**
	 * Text Inset
	 * 
	 * @param g graphics
	 * @param value text
	 * @param x X offset
	 * @param y Y offset
	 */
	//FIXME use enum + center/leftText
	public static void inset(Graphics2D g, String value, int x,int y)
	{
		Paint p = g.getPaint();
		g.setPaint(new Color(255,255,255,100));
		g.drawString(value, x, y+1);
		g.setPaint(p);
		g.drawString(value, x, y);
	}
		
	private static BufferedImage makeNoiseWhite(Dimension dim)
	{	
		BufferedImage bi		= new BufferedImage(dim.width,dim.height,BufferedImage.TYPE_INT_ARGB);
		float _128 =20;
		int[] rgbs = new int[dim.width* dim.height];
		
		for(int x=0; x<dim.width; x++) for(int y=0; y< dim.height; y++)
	    {
	    	int col			=	noise(x/_128, y/_128, 6);

	    	int b = (int)Math.max(col-90, 0);
	    	
	    	int ARGB=b;
            ARGB=(ARGB<<8)+b;
            ARGB=(ARGB<<8)+b;
            
            int RGB=col;
            RGB=(RGB<<8)+col;
            RGB=(RGB<<8)+col;
	    	
            int alpha		= (RGB << 8) | 0x00FFFFFF;
	    	RGB = alpha * ARGB;
	    	rgbs[y*dim.width+x]	= RGB;
	    }
		
	    bi.setRGB(0, 0, dim.width,  dim.height, rgbs, 0, dim.width);
		return bi;
	}
	
	private static BufferedImage makeNoiseBlack(Dimension dim)
	{
		BufferedImage bi		= new BufferedImage(dim.width,dim.height,BufferedImage.TYPE_INT_ARGB);
		float _128 =1;
		int[] rgbs = new int[dim.width* dim.height];
		
		for(int x=0; x<dim.width; x++) for(int y=0; y< dim.height; y++)
	    {
	    	int col			=	noise(x/_128, y/_128, 7);

            int red		= col;
            int green	= col;
            int blue	= col;
            
            int	RGB		= red;
            	RGB		= (RGB<<8)+green;
            	RGB		= (RGB<<8)+blue;
	    	
            int alpha	= (RGB << 8) & 0xFF000000;
            	RGB		= alpha * RGB;

	    	rgbs[y*dim.width+x]	= RGB;
	    }
	    
	    bi.setRGB(0, 0, dim.width,  dim.height, rgbs, 0, dim.width);
		return bi;
	}

	/**
	 * Gets gray negative for given Color
	 * 
	 * @param color source Color
	 * 
	 * @return negative
	 */
	public static Color getContrastColor(Color color)
	{
		int c = (Math.max(0, Math.min(255, 150-color.getRed()))+Math.max(0, Math.min(255, 150-color.getGreen()))+Math.max(0, Math.min(255, 150-color.getBlue())))/3;
		return new Color(c,c,c);
	}
	
	/**
	 * Gets BLACK | WHITE negative for given Color
	 * 
	 * @param color source Color
	 * 
	 * @return BLACK | WHITE
	 */
	public static Color getExactContrastColor(Color color)
	{
		int c = (Math.max(0, Math.min(255, 150-color.getRed()))+Math.max(0, Math.min(255, 150-color.getGreen()))+Math.max(0, Math.min(255, 150-color.getBlue())))/3;
		if(c<255/3) c=0;
		if(c>255/3) c=255;
		return new Color(c,c,c);
	}


	/**
	 * 
	 * @param g
	 * @param area
	 * @param color
	 * @param factor
	 */
	public static void shadow(Graphics2D g, Shape area, Color color,float factor)
	{
		AffineTransform at	= g.getTransform();
		Paint p				= g.getPaint();	
		double cx			= area.getBounds2D().getCenterX();
		double cy			= area.getBounds2D().getCenterY();
		AffineTransform old	= g.getTransform();
		int red				= color.getRed();
		int green			= color.getGreen();
		int blue			= color.getBlue();
		int width			= area.getBounds().width;
		int height			= area.getBounds().height;
		float wfactor		= 1.0f;
		float hfactor		= 1.0f;
		
		if(width>height)	wfactor = (1/((float)width/(float)height));
		else				hfactor = (1/((float)height/(float)width));
		
		float hseed	= (1.0f*hfactor*factor);
		float wseed	= (1.0f*wfactor*factor);
		float zoomh	= 1.0f+hseed;
		float zoomw	= 1.0f+wseed;
		int alpha	= (int) (20*((float)color.getAlpha()/(float)255));
		
		for(; zoomw>1.0 || zoomh>1.0;)
		{
			zoomw -= wseed/20;
			zoomh -= hseed/20;
			g.setPaint(new Color(red,green,blue,alpha));
			AffineTransform tr2 =AffineTransform.getTranslateInstance(-cx, -cy);
			AffineTransform  tr= AffineTransform.getScaleInstance(zoomw,zoomh);
			tr.concatenate(tr2);
			tr2	= tr;
			tr	= AffineTransform.getTranslateInstance(cx, cy);
			tr.concatenate(tr2);	tr2 = tr;
			tr= new AffineTransform(old);
			tr.concatenate(tr2);	tr2 = tr;
			g.setTransform(tr2);
			g.fill(area);
			g.setTransform(old);
		}
		g.setPaint(p);
		g.setTransform(at);	
	}
	
	/**
	 * 
	 * @param g
	 * @param text
	 * @param size
	 * @param color
	 * @param bounds
	 */
	public static void leftTextShadow(Graphics2D g, String text, int size, Paint color, Rectangle bounds)
	{ leftTextShadow(g,text,size,color,bounds,Font.PLAIN); }
	
	/**
	 * 
	 * @param g
	 * @param text
	 * @param size
	 * @param color
	 * @param bounds
	 * @param style
	 */
	public static void leftTextShadow(Graphics2D g, String text, int size, Paint color, Rectangle bounds, int style)
	{
		Paint paint		= g.getPaint();
		Font f			= g.getFont();
		
		if(f==null) f=FontUtils.DEFAULT_FONT;
		
		g.setFont(new Font(f.getFamily(),style,size));
		g.translate(bounds.x, bounds.y);
		FontMetrics fm	= g.getFontMetrics();
		int y = ((fm.getAscent() + (bounds.height - (fm.getAscent() + fm.getDescent())) / 2));
		
		g.setPaint(new Color(0,0,0,80));
		g.drawString(text, y/3, y+1);
		g.setPaint(color);
		g.drawString(text, y/3, y);
		
		g.translate(-bounds.x, -bounds.y);
		g.setPaint(paint);	
	}

	/**
	 * 
	 * @param g
	 * @param text
	 * @param size
	 * @param color
	 * @param bounds
	 * @param style
	 */
	public static void centerTextInset(Graphics2D g, String text, int size, Paint color, Rectangle bounds, int style)
	{
		Paint paint	= g.getPaint();
		Font f		= g.getFont();
		if(f==null) f=FontUtils.DEFAULT_FONT;
		
		g.setFont(new Font(f.getFamily(),style,size));
		g.translate(bounds.x, bounds.y);
		FontMetrics fm = g.getFontMetrics();
		g.setPaint(new Color(255,255,255,90));
		int x	= ((bounds.width - fm.stringWidth(text)) / 2);
		int y	= ((fm.getAscent() + (bounds.height - (fm.getAscent() + fm.getDescent())) / 2));
		g.drawString(text, x,y+1);
		g.setPaint(color);
		g.drawString(text, x,y);
		g.translate(-bounds.x, -bounds.y);
		g.setPaint(paint);
	}

	/**
	 * 
	 * @param g
	 * @param text
	 * @param size
	 * @param color
	 * @param shade
	 * @param bounds
	 * @param style
	 */
	public static void centerTextInset(Graphics2D g, String text, int size, Paint color, Color shade, Rectangle bounds, int style)
	{
		Paint paint	= g.getPaint();
		Font f		= g.getFont();
		if(f==null) f=FontUtils.DEFAULT_FONT;
		
		g.setFont(new Font(f.getFamily(),style,size));
		g.translate(bounds.x, bounds.y);
		FontMetrics fm = g.getFontMetrics();
		g.setPaint(shade);
		int x	= ((bounds.width - fm.stringWidth(text)) / 2);
		int y	= ((fm.getAscent() + (bounds.height - (fm.getAscent() + fm.getDescent())) / 2));
		g.drawString(text, x,y+1);
		g.setPaint(color);
		g.drawString(text, x,y);
		g.translate(-bounds.x, -bounds.y);
		g.setPaint(paint);
	}
	
	/**
	 * Draws centering text bounded to source bounds
	 * 
	 * @param g graphics
	 * @param text source text
	 * @param size font size
	 * @param color foreground color
	 * @param bounds clip bounds
	 * @param style font style
	 */
	public static void centerText(Graphics2D g, String text, int size, Paint color, Rectangle bounds, int style)
	{
		Paint paint	= g.getPaint();
		Font f		= g.getFont();
		if(f==null) f=FontUtils.DEFAULT_FONT;
		
		g.setPaint(color);
		g.setFont(new Font(f.getFamily(),style,size));
		g.translate(bounds.x, bounds.y);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(text, ((bounds.width - fm.stringWidth(text)) / 2), ((fm.getAscent() + (bounds.height - (fm.getAscent() + fm.getDescent())) / 2)));	
		g.translate(-bounds.x, -bounds.y);
		g.setPaint(paint);
	}
	
	/**
	 * Draws text bounded to source bounds
	 * 
	 * @param g graphics
	 * @param text source text
	 * @param size font size
	 * @param color foreground color
	 * @param bounds clip bounds
	 * @param style font style
	 */
	public static void leftText(Graphics2D g, String text, int size, Paint color, Rectangle bounds, int style)
	{
		Paint paint		= g.getPaint();
		Font f			= g.getFont();
		
		if(f==null) f=FontUtils.DEFAULT_FONT;
		g.setPaint(color);
		g.setFont(new Font(f.getFamily(),style,size));
		g.translate(bounds.x, bounds.y);
		FontMetrics fm	= g.getFontMetrics();
		int y = ((fm.getAscent() + (bounds.height - (fm.getAscent() + fm.getDescent())) / 2));
		g.drawString(text, y/3, y);
		g.translate(-bounds.x, -bounds.y);
		g.setPaint(paint);
	}

	/**
	 * Generates noise
	 * 
	 * @param g graphics
	 * @param shape clipping shape
	 * @param BLACK opacity of black noise
	 * @param WHITE opacity of white noise
	 */
	public static void noise(Graphics2D g, Shape shape,float BLACK, float WHITE)
	{
		Rectangle bounds	= shape.getBounds();
		int x				= bounds.x;
		int y				= bounds.y+(screen.height-bounds.height);
		int w				= bounds.width;
		int h				= bounds.height;
		Composite c			= g.getComposite();
		
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, WHITE));
		g.drawImage(noise_white.getSubimage(x, y, w, h), x,bounds.y, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, BLACK));
		g.drawImage(noise_black.getSubimage(x, y, w, h), x,bounds.y, null);
		
		g.setComposite(c);
	}
	
	/**
	 * Generates noise with 4% black and 6% white opacity
	 * 
	 * @param g graphics
	 * @param shape clipping shape
	 */
	public static void noise(Graphics2D g, Shape shape)
	{ noise(g,shape,0.04f,0.06f); }

	/**
	 * Converts Color to HTML standart String
	 * 
	 * @param color source Color
	 * 
	 * @return HTML standartised Color string
	 */
	//FIXME to Atomic
	public static String ColorToHtml(Color color)
	{
		if (color==null)			return "black";
			
		String hexRed = Integer.toHexString(color.getRed());
		
		if (hexRed.length()==1)		hexRed = "0" + hexRed;
		
		String hexGreen = Integer.toHexString(color.getGreen());
		
		if (hexGreen.length()==1)	hexGreen = "0" + hexGreen;
		
		String hexBlue = Integer.toHexString(color.getBlue());
		
		if (hexBlue.length()==1)	hexBlue = "0" + hexBlue;
		
		return "#" + hexRed + hexGreen + hexBlue;
	}

	/**
	 * Gets screen resolution
	 * 
	 * @return screen resolution
	 */
	public static int getScreenResolution()
	{ return Toolkit.getDefaultToolkit().getScreenResolution(); }

	/**
	 * Gets screen size
	 * 
	 * @return screen size
	 */
	public static Dimension getScreenSize()
	{ return Toolkit.getDefaultToolkit().getScreenSize(); }
	
	/**
	 * 
	 * @param g
	 * @param a
	 * @param color
	 * @param size
	 * @param opacity
	 */
	public static void innerGlow(Graphics2D g, Area a, Paint color, int size, float opacity)
	{
		 size=Math.min(size, 20);
		 g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		 int x = a.getBounds().x;
		 int y = a.getBounds().y;
	    int width	= a.getBounds().width	+ 2;
	    int height	= a.getBounds().height	+ 2;
	    int h		=	height>>1;
	    int w		=	width>>1;
	    int ww		=	width<<1;
	    int hh		=	height<<1;
	    
	    Area area1 = new Area(new Rectangle(-w, -h, ww, hh));
	    a.transform(AffineTransform.getTranslateInstance(-x, -y));
	    area1.subtract(a);
	 
	    BufferedImage tmp = new BufferedImage(ww, hh, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g3 = tmp.createGraphics();
	    g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g3.setPaint(color);
	    g3.translate(w, h);
	    g3.fill(area1);
	 
	    fastBlur(tmp,size);

	    g3.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.0f));
	    g3.fill(area1);

	    a.transform(AffineTransform.getTranslateInstance(1, 1));
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
	    g.translate(x, y);
	    g.translate(-w, -h);
	    
	    g.drawImage(tmp, null, -1, -1);
	    g.drawImage(tmp, null, -1, -1);
	    
	    g.translate(-x, -y);
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	    a.transform(AffineTransform.getTranslateInstance(x-1, y-1));
	    g.translate(w, h);
	  }

	/**
	 * Nice Blurs source Image
	 * 
	 * @param image source Image
	 * @param size blur size
	 */
	 public static void niceBlur(BufferedImage image, int size)
	 { blur(size, image, 0, 0, image.getWidth(), image.getHeight()); }
	 
	 /**
	  * Fast Blurs source Image
	  * 
	  * @param image source Image
	  * @param size blur size
	  */
	 public static void fastBlur(BufferedImage image, int size)
	 { blur(image, size); }
	 

		/**
		 * Brighten source color by given percentage
		 * 
		 * @param c source Color
		 * @param FACTOR percentage to be left
		 * 
		 * @return brighter Color
		 */
		private static Color brighter(Color c,float FACTOR)
		{
			int r	= c.getRed();
			int g	= c.getGreen();
			int b	= c.getBlue();
			int i	= (int)(1.0/(1.0-FACTOR));
			
			if ( r == 0 && g == 0 && b == 0) return new Color(i, i, i);
			if ( r > 0 && r < i ) r = i;
			if ( g > 0 && g < i ) g = i;
			if ( b > 0 && b < i ) b = i;
			return new Color(Math.min((int)(r/FACTOR), 255), Math.min((int)(g/FACTOR), 255), Math.min((int)(b/FACTOR), 255));
		}
		
	 /**
	  * Darken source color by given percentage
	  * 
	  * @param c source Color
	  * @param FACTOR percentage to be left
	  * 
	  * @return darker Color
	  */
	public static Color darker(Color c, float FACTOR)
	{ return new Color(Math.max((int)(c.getRed()  *FACTOR), 0), Math.max((int)(c.getGreen()*FACTOR), 0), Math.max((int)(c.getBlue() *FACTOR), 0)); }
	
	/**
	 * Brighten source color by 10%
	 * 
	 * @param c source Color
	 * 
	 * @return brighter Color
	 */
	public static Color brighten(Color c)
	{ return brighter(c,0.9f); }
	
	/**
	 * Brighten source color by 40%
	 *  
	 * @param c source Color
	 * 
	 * @return brighter Color
	 */
	public static Color brightenMore(Color c)
	{ return brighter(c,0.6f); }
	
	/**
	 * Darken source color by 10%
	 * 
	 * @param c source Color
	 * 
	 * @return darker Color
	 */
	public static Color darker(Color c)
	{ return darker(c,0.9f); }
	
	/**
	 * Darken source color by 30%
	 * 
	 * @param c source Color
	 * 
	 * @return darker Color
	 */
	public static Color darkerMore(Color c)
	{ return darker(c,0.7f); }
	
	/**
	 * Draws centering text bounded to source bounds
	 * 
	 * @param g graphics
	 * @param text source text
	 * @param size font size
	 * @param color foreground color
	 * @param bounds clip bounds
	 */
	public static void centerText(Graphics2D g, String text, int size, Paint color, Rectangle bounds)
	{ centerText(g,text,size,color,bounds,Font.PLAIN); }
	
	/**
	 * Draws text bounded to source bounds
	 * 
	 * @param g graphics
	 * @param text source text
	 * @param size font site
	 * @param color foreground color
	 * @param bounds clip bounds
	 */
	public static void leftText(Graphics2D g, String text, int size, Paint color, Rectangle bounds)
	{ leftText(g,text,size,color,bounds,Font.PLAIN); }

	/**
	 * Applies alias setting to source Graphics
	 * 
	 * @param t graphics
	 * 
	 * @return Setup Graphics
	 */
	public static Graphics2D getGraphics(Graphics t)
	{
		Graphics2D g = (Graphics2D)t;
		setFastHints(g);
		return g;
	}
	
	/**
	 * Applies alias setting to source Graphics
	 * 
	 * @param t graphics2D
	 * 
	 */
	public static void setFastHints(Graphics2D g)
	{
		
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING,		RenderingHints.VALUE_ANTIALIAS_ON					);
		g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,	RenderingHints.VALUE_TEXT_ANTIALIAS_ON				);
		g.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,	RenderingHints.VALUE_FRACTIONALMETRICS_ON			);
		g.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION,	RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED		);
        g.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING,		RenderingHints.VALUE_COLOR_RENDER_SPEED				);
        g.setRenderingHint( RenderingHints.KEY_DITHERING,			RenderingHints.VALUE_DITHER_DISABLE					);
        g.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,	RenderingHints.VALUE_FRACTIONALMETRICS_OFF			);
        g.setRenderingHint( RenderingHints.KEY_INTERPOLATION,		RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR	);
        g.setRenderingHint( RenderingHints.KEY_RENDERING,			RenderingHints.VALUE_RENDER_SPEED					);
        g.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL,		RenderingHints.VALUE_STROKE_NORMALIZE				);
		
	}
	
	/**
	 * Draws multiline string text bounded to source bounds
	 * 
	 * @param g graphics
	 * @param message source text
	 * @param size font size
	 * @param color foreground color
	 * @param bounds clip bounds
	 * @param style font style
	 */
	public static void drawMultilineString(Graphics2D g, String message, int size, Color color, Rectangle bounds, int style)
	{
		if(message.equalsIgnoreCase("")) return;
		int paragraphStart	= 0;
		int paragraphEnd	= 0;
		Font font			= g.getFont();
		Paint paint			= g.getPaint();
		String[] out		= message.split("\n");
        float breakWidth	= bounds.width-20;
        float drawPosY		= 0;
        
        g.setPaint(color);
		g.setFont(new Font(font.getFamily(),style,size));
		
		for(String text : out)
		{
			AttributedString attribute = new AttributedString(text);
			attribute.addAttribute(TextAttribute.FONT, g.getFont());
			
            AttributedCharacterIterator paragraph	= attribute.getIterator();
            paragraphStart							= paragraph.getBeginIndex();
            paragraphEnd							= paragraph.getEndIndex();
            LineBreakMeasurer lineMeasurer			= new LineBreakMeasurer(paragraph, g.getFontRenderContext());
            
            lineMeasurer.setPosition(paragraphStart);

            while (lineMeasurer.getPosition() < paragraphEnd)
            {
            	TextLayout layout	= lineMeasurer.nextLayout(breakWidth);
            	float drawPosX		= layout.isLeftToRight() ? 0 : breakWidth - layout.getAdvance();
            	drawPosY			+= layout.getAscent();
            	layout.draw(g, drawPosX+bounds.x+10, drawPosY+bounds.y+10);
            	drawPosY			+= layout.getDescent() + layout.getLeading();
            }
            
            drawPosY+=4;
		}
        g.setPaint(paint);
        g.setFont(font);
	}

	//FIXME !!!!
	   private static int noise(double x,double y,int nbOctave)
	   {
	      int result		= 0;
	      int sx			= (int)((x)*256); 
	      int sy			= (int)((y)*256); 
	      int octave		= nbOctave;
	      
	      while(octave!=0) 
	      {
	         int bX		= sx&0xFF;
	         int bY		= sy&0xFF;

	         int sxp	= sx>>8;
	         int syp	= sy>>8;
	         
	         int Y1376312589_00		= syp*1376312589;
	         int Y1376312589_01		= Y1376312589_00+1376312589;

	         int XY1376312589_00	= sxp+Y1376312589_00;
	         int XY1376312589_10	= XY1376312589_00+1;
	         int XY1376312589_01	= sxp+Y1376312589_01;
	         int XY1376312589_11	= XY1376312589_01+1;

	         int XYBASE_00	= (XY1376312589_00<<13)^XY1376312589_00;
	         int XYBASE_10	= (XY1376312589_10<<13)^XY1376312589_10;
	         int XYBASE_01	= (XY1376312589_01<<13)^XY1376312589_01;
	         int XYBASE_11	= (XY1376312589_11<<13)^XY1376312589_11;

	         int alt1	= (XYBASE_00 * (XYBASE_00 * XYBASE_00 * 15731 + 789221) + 1376312589) ;
	         int alt2	= (XYBASE_10 * (XYBASE_10 * XYBASE_10 * 15731 + 789221) + 1376312589) ;
	         int alt3	= (XYBASE_01 * (XYBASE_01 * XYBASE_01 * 15731 + 789221) + 1376312589) ;
	         int alt4	= (XYBASE_11 * (XYBASE_11 * XYBASE_11 * 15731 + 789221) + 1376312589) ;
	         
	         int grad1X	= (alt1&0xFF)-128;
	         int grad1Y	= ((alt1>>8)&0xFF)-128;
	         int grad2X	= (alt2&0xFF)-128;
	         int grad2Y	= ((alt2>>8)&0xFF)-128;
	         int grad3X	= (alt3&0xFF)-128;
	         int grad3Y	= ((alt3>>8)&0xFF)-128;
	         int grad4X	= (alt4&0xFF)-128;
	         int grad4Y	= ((alt4>>8)&0xFF)-128;
	          
	         int sX1	= bX>>1;
	         int sY1	= bY>>1;
	         int sX2	= 128-sX1;
	         int sY2	= sY1;
	         int sX3	= sX1;
	         int sY3	= 128-sY1;
	         int sX4	= 128-sX1;
	         int sY4	= 128-sY1;
	         alt1		= (grad1X*sX1+grad1Y*sY1)+16384+((alt1&0xFF0000)>>9); //to avoid seams to be 0 we use an offset
	         alt2		= (grad2X*sX2+grad2Y*sY2)+16384+((alt2&0xFF0000)>>9);
	         alt3		= (grad3X*sX3+grad3Y*sY3)+16384+((alt3&0xFF0000)>>9);
	         alt4		= (grad4X*sX4+grad4Y*sY4)+16384+((alt4&0xFF0000)>>9);
	         
	         int bX2	= (bX*bX)>>8;
	         int bX3	= (bX2*bX)>>8;
	         int _3bX2	= 3*bX2;
	         int _2bX3	= bX3<<1;
	         int alt12	= alt1 - (((_3bX2 - _2bX3) * (alt1-alt2)) >> 8);
	         int alt34	= alt3 - (((_3bX2 - _2bX3) * (alt3-alt4)) >> 8);
	         
	         int bY2	= (bY*bY)>>8;
	         int bY3	= (bY2*bY)>>8;
	         int _3bY2	= 3*bY2;
	         int _2bY3	= bY3<<1;
	         int val	= alt12 - (((_3bY2 - _2bY3) * (alt12-alt34)) >> 8);
	         
	         val	<<= 8;
	         
	         result	+= (val<<octave);
	         
	         octave--;
	         sx<<=1; 
	         sy<<=1;
	         
	      }
	      return result>>>(16+nbOctave+1);   
	   }

	   /**
	    * 
	    * @param g
	    * @param text
	    * @param size
	    * @param color
	    * @param outline
	    * @param bounds
	    * @param style
	    */
	   public static void centerOutlineText(Graphics2D g, String text, int size, Paint color, Paint outline, Rectangle bounds, int style)
	   {
		   Paint paint		= g.getPaint();
		   Font f			= g.getFont();
		   if(f==null) f	= FontUtils.DEFAULT_FONT;
		
		   g.setFont(new Font(f.getFamily(),style,size));
		
		   g.translate(bounds.x, bounds.y);
		
		   FontMetrics fm	= g.getFontMetrics();
		   int x			= ((bounds.width - fm.stringWidth(text)) / 2);
		   int y			= ((fm.getAscent() + (bounds.height - (fm.getAscent() + fm.getDescent())) / 2));
		
		   g.translate(x,y);

		   g.setPaint(outline);

		   g.drawString(text, -1, 0);
		   g.drawString(text, 1, 0);
		   g.drawString(text, 0, -1);
		   g.drawString(text, 0, 1);
		
		   g.setPaint(color);
		   g.drawString(text, 0,0);
		
		   g.translate(-bounds.x-x, -bounds.y-y);
		   g.setPaint(paint);
	}
	
	// >-------[post-process]---------------------------------------------------------------------------------------< //
	
	static
	{
		noise_black = makeNoiseBlack(screen);
		noise_white = makeNoiseWhite(screen);
	}

}
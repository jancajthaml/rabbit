package util.calculations;

import java.awt.image.BufferedImage;

/**
 * Gaussian blur
 * 
 * @author Jan Cajthaml
 *
 */
public class Blur
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private int radius		= 0;
	private int kernelSize	= 0;
	private int[] kernel	= null;
	private int[][] mult	= null;
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Internal instance
	 * 
	 * @param size blur size
	 */
	private Blur (int size) 
	{ this.setRadius(size);}

	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Blurs source image FAST with given size blur 
	 * 
	 * @param source source image
	 * @param size blur size
	 */
	public static void blur(BufferedImage source, int size)
	{ new Blur(size).blur(source); }

	/**
	 * Blurs source image NICE with given size blur, size and offset
	 * 
	 * @param size blur size
	 * @param source source image
	 * @param x x offset
	 * @param y y offset
	 * @param w width
	 * @param h height
	 */
	public static void blur(int size, BufferedImage source, int x, int y, int w, int h)
	{ new Blur(size).blur(source, x, y, w, h); }
	
	private void blur(BufferedImage img)
	{
		if (radius < 1) return;
		
		int w		= img.getWidth();
		int h		= img.getHeight();
		int[] pix	= img.getRGB(0, 0, w, h, null, 0,w);
		int wm		= w - 1;
		int hm		= h - 1;
		int wh		= w * h;
		int div		= radius + radius + 1;

		int a[]		= new int[wh];
		int r[]		= new int[wh];
		int g[]		= new int[wh];
		int b[]		= new int[wh];
		
		int asum,rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		
		int vmin[]	= new int[Math.max(w, h)];
		int divsum	= (div + 1) >> 1;
		
		divsum		*= divsum;
		
		int dv[]	= new int[256 * divsum];
		
		for (i = 0; i < 256 * divsum; i++) dv[i] = (i / divsum);

		yw = yi = 0;

		int[][] stack = new int[div][4];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum, aoutsum;
		int rinsum, ginsum, binsum, ainsum;

		for (y = 0; y < h; y++)
		{
			aoutsum = ainsum =rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = asum= 0;
			for (i = -radius; i <= radius; i++)
			{
				p		= pix[yi + Math.min(wm, Math.max(i, 0))];
				sir		= stack[i + radius];
				sir[0]	= (p >> 24) & 0xFF;
				sir[1]	= (p & 0xff0000) >> 16;
				sir[2]	= (p & 0x00ff00) >> 8;
				sir[3]	= (p & 0x0000ff);
				rbs		= r1 - Math.abs(i);
				asum	+= sir[0] * rbs;
				rsum	+= sir[1] * rbs;
				gsum	+= sir[2] * rbs;
				bsum	+= sir[3] * rbs;
				
				if (i > 0)
				{
					ainsum	+= sir[0];
					rinsum	+= sir[1];
					ginsum	+= sir[2];
					binsum	+= sir[3];
				}
				else
				{
					aoutsum	+= sir[0];
					routsum	+= sir[1];
					goutsum	+= sir[2];
					boutsum	+= sir[3];
				}
			}
			
			stackpointer = radius;

			for (x = 0; x < w; x++)
			{

				a[yi]		= dv[asum];
				r[yi]		= dv[rsum];
				g[yi]		= dv[gsum];
				b[yi]		= dv[bsum];

				rsum		-= routsum;
				gsum		-= goutsum;
				bsum		-= boutsum;
				asum		-= aoutsum;

				stackstart	= stackpointer - radius + div;
				sir			= stack[stackstart % div];

				aoutsum		-= sir[0];
				routsum		-= sir[1];
				goutsum		-= sir[2];
				boutsum		-= sir[3];

				if (y == 0)
					vmin[x] = Math.min(x + radius + 1, wm);
				
				p			= pix[yw + vmin[x]];

				sir[0]		= (p >> 24) & 0xFF;
				sir[1]		= (p & 0xff0000) >> 16;
				sir[2]		= (p & 0x00ff00) >> 8;
				sir[3]		= (p & 0x0000ff);

				ainsum		+= sir[0];
				rinsum		+= sir[1];
				ginsum		+= sir[2];
				binsum		+= sir[3];

				asum		+= ainsum;
				rsum		+= rinsum;
				gsum		+= ginsum;
				bsum		+= binsum;

				stackpointer	= (stackpointer + 1) % div;
				sir				= stack[(stackpointer) % div];

				aoutsum		+= sir[0];
				routsum		+= sir[1];
				goutsum		+= sir[2];
				boutsum		+= sir[3];

				ainsum		-= sir[0];
				rinsum		-= sir[1];
				ginsum		-= sir[2];
				binsum		-= sir[3];

				yi++;
			}
			
			yw += w;
		}
		
		for (x = 0; x < w; x++)
		{
			ainsum	= asum = aoutsum = rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp		= -radius * w;
			
			for (i = -radius; i <= radius; i++)
			{
				yi		= Math.max(0, yp) + x;
				sir		= stack[i + radius];
				sir[0]	= r[yi];
				sir[1]	= r[yi];
				sir[2]	= g[yi];
				sir[3]	= b[yi];
				rbs		= r1 - Math.abs(i);
				asum	+= r[yi] * rbs;
				rsum	+= r[yi] * rbs;
				gsum	+= g[yi] * rbs;
				bsum	+= b[yi] * rbs;

				if (i > 0)
				{
					ainsum	+= sir[0];
					rinsum	+= sir[1];
					ginsum	+= sir[2];
					binsum	+= sir[3];
				}
				else
				{
					aoutsum	+= sir[0];
					routsum	+= sir[1];
					goutsum	+= sir[2];
					boutsum	+= sir[3];
				}

				if (i < hm) yp += w;
			}
			
			yi				= x;
			stackpointer	= radius;
			
			for (y = 0; y < h; y++)
			{
				pix[yi] = (dv[asum] << 24) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
				
				asum	-= aoutsum;
				rsum	-= routsum;
				gsum	-= goutsum;
				bsum	-= boutsum;

				stackstart	= stackpointer - radius + div;
				sir			= stack[stackstart % div];

				aoutsum	-= sir[0];
				routsum	-= sir[1];
				goutsum	-= sir[2];
				boutsum	-= sir[3];

				if (x == 0)
					vmin[y] = Math.min(y + r1, hm) * w;
				
				p = x + vmin[y];

				sir[0]	= a[p];
				sir[1]	= r[p];
				sir[2]	= g[p];
				sir[3]	= b[p];

				ainsum	+= sir[0];
				rinsum	+= sir[1];
				ginsum	+= sir[2];
				binsum	+= sir[3];

				asum	+= ainsum;
				rsum	+= rinsum;
				gsum	+= ginsum;
				bsum	+= binsum;

				stackpointer	= (stackpointer + 1) % div;
				sir				= stack[stackpointer];

				aoutsum	+= sir[0];
				routsum	+= sir[1];
				goutsum	+= sir[2];
				boutsum	+= sir[3];

				ainsum	-= sir[0];
				rinsum	-= sir[1];
				ginsum	-= sir[2];
				binsum	-= sir[3];

				yi		+= w;
			}
		}
		
		img.setRGB(0, 0, w, h, pix, 0,w);
	}
	
	private void setRadius(int sz)
	{
		int i, j;
		sz = Math.min(Math.max(1, sz), 248);
		
		if (radius == sz) return;
		
		kernelSize	= 1 + sz * 2;
		radius		= sz;
		kernel		= new int[1 + sz * 2];
		mult		= new int[1 + sz * 2][256];

		for (i = 1; i < sz; i++)
		{
			int szi			= sz - i;
			kernel[sz + i]	= kernel[szi] = szi * szi;
			
			for (j = 0; j < 256; j++) mult[sz + i][j] = mult[szi][j] = kernel[szi] * j;
		}
		
		kernel[sz] = sz * sz;
		for (j = 0; j < 256; j++) mult[sz][j] = kernel[sz] * j;
	}

	private void blur(BufferedImage img, int x, int y, int w, int h)
	{
		int sum, cr, cg, cb, ca, read, i, ri, xl, yl, yi, ym, riw;
		
		int[] pix	= img.getRGB(x, y, w, h, null, 0,w);
		int iw		= img.getWidth();
		int wh		= iw * img.getHeight();
		int a[]		= new int[wh];
		int r[]		= new int[wh];
		int g[]		= new int[wh];
		int b[]		= new int[wh];
		int a2[]	= new int[wh];
		int r2[]	= new int[wh];
		int g2[]	= new int[wh];
		int b2[]	= new int[wh];
		
		for (i = 0; i < wh; i++)
		{
			ri		= pix[i];
			a[i]	= (ri >> 24) & 0xFF;
			r[i]	= (ri & 0xff0000) >> 16;
			g[i]	= (ri & 0x00ff00) >> 8;
			b[i]	= (ri & 0x0000ff);
		}

		x	= Math.max(0, x);
		y	= Math.max(0, y);
		w	= x + w - Math.max(0, (x + w) - iw);
		h	= y + h - Math.max(0, (y + h) - img.getHeight());
		yi	= y * iw;

		for (yl = y; yl < h; yl++)
		{
			for (xl = x; xl < w; xl++)
			{
				ca = cb = cg = cr = sum = 0;
				ri = xl - radius;
				
				for (i = 0; i < kernelSize; i++)
				{
					read = ri + i;
					if (read >= x && read < w)
					{
						read	+= yi;
						ca		+= mult[i][a[read]];
						cr		+= mult[i][r[read]];
						cg		+= mult[i][g[read]];
						cb		+= mult[i][b[read]];
						sum		+= kernel[i];
					}
				}
				ri		= yi + xl;
				a2[ri]	= ca / sum;
				r2[ri]	= cr / sum;
				g2[ri]	= cg / sum;
				b2[ri]	= cb / sum;
			}
			yi += iw;
		}
		
		yi = y * iw;

		for (yl = y; yl < h; yl++)
		{
			ym	= yl - radius;
			riw	= ym * iw;
			
			for (xl = x; xl < w; xl++)
			{
				ca		= cb = cg = cr = sum = 0;
				ri		= ym;
				read	= xl + riw;
				
				for (i = 0; i < kernelSize; i++)
				{
					if (ri < h && ri >= y)
					{
						ca	+= mult[i][a2[read]];
						cr	+= mult[i][r2[read]];
						cg	+= mult[i][g2[read]];
						cb	+= mult[i][b2[read]];
						sum	+= kernel[i];
					}
					
					ri++;
					read += iw;
				}
				
				pix[xl + yi] =  (ca / sum) << 24 | (cr / sum) << 16 | (cg / sum) << 8 | (cb / sum);
			}
			
			yi += iw;
		}
		img.setRGB(0, 0, w, h, pix, 0,w);
	}
	
}

package util.calculations;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ImagingOpException;
import java.awt.image.RescaleOp;

/**
 * Image Scale Utility class
 * 
 * @author Jan Cajthaml
 *
 */
//NOT OK
//UNCHECKED
public class Scale
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	public static final RescaleOp OP_DARKER				= new RescaleOp(0.9f, 0, null);
	public static final RescaleOp OP_BRIGHTER			= new RescaleOp(1.1f, 0, null);
	public static final ColorConvertOp OP_GRAYSCALE		= new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
	public static final int THRESHOLD_BALANCED_SPEED	= 1600;
	public static final int THRESHOLD_QUALITY_BALANCED	= 800;

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	private Scale()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	public static BufferedImage resize(BufferedImage src, int targetSize, BufferedImageOp... ops) throws IllegalArgumentException, ImagingOpException
	{ return resize(src, Method.AUTOMATIC, Mode.AUTOMATIC, targetSize, targetSize, ops); }

	public static BufferedImage resize(BufferedImage src, Method scalingMethod, int targetSize, BufferedImageOp... ops) throws IllegalArgumentException, ImagingOpException
	{ return resize(src, scalingMethod, Mode.AUTOMATIC, targetSize, targetSize, ops); }

	public static BufferedImage resize(BufferedImage src, Mode resizeMode, int targetSize, BufferedImageOp... ops) throws IllegalArgumentException, ImagingOpException
	{ return resize(src, Method.AUTOMATIC, resizeMode, targetSize, targetSize, ops); }

	public static BufferedImage resize(BufferedImage src, Method scalingMethod, Mode resizeMode, int targetSize, BufferedImageOp... ops) throws IllegalArgumentException, ImagingOpException
	{ return resize(src, scalingMethod, resizeMode, targetSize, targetSize, ops); }

	public static BufferedImage resize(BufferedImage src, int targetWidth, int targetHeight, BufferedImageOp... ops) throws IllegalArgumentException, ImagingOpException
	{ return resize(src, Method.AUTOMATIC, Mode.AUTOMATIC, targetWidth, targetHeight, ops); }

	public static BufferedImage resize(BufferedImage src, Method scalingMethod, int targetWidth, int targetHeight, BufferedImageOp... ops)
	{ return resize(src, scalingMethod, Mode.AUTOMATIC, targetWidth, targetHeight, ops); }

	public static BufferedImage resize(BufferedImage src, Mode resizeMode, int targetWidth, int targetHeight, BufferedImageOp... ops) throws IllegalArgumentException, ImagingOpException
	{ return resize(src, Method.AUTOMATIC, resizeMode, targetWidth, targetHeight, ops); }

	public static BufferedImage resize(BufferedImage src, Method scalingMethod, Mode resizeMode, int targetWidth, int targetHeight, BufferedImageOp... ops) throws IllegalArgumentException, ImagingOpException
	{
		if (src == null)			throw new IllegalArgumentException("src cannot be null");
		if (targetWidth < 0)		throw new IllegalArgumentException("targetWidth must be >= 0");
		if (targetHeight < 0)		throw new IllegalArgumentException("targetHeight must be >= 0");
		if (scalingMethod == null)	throw new IllegalArgumentException("scalingMethod cannot be null. A good default value is Method.AUTOMATIC.");
		if (resizeMode == null)		throw new IllegalArgumentException("resizeMode cannot be null. A good default value is Mode.AUTOMATIC.");

		BufferedImage result	= null;
		int currentWidth		= src.getWidth();
		int currentHeight		= src.getHeight();
		float ratio				= ((float) currentHeight / (float) currentWidth);

		if (resizeMode != Mode.FIT_EXACT)
		{
			if ((resizeMode == Mode.FIT_TO_WIDTH))
			{
				if (targetWidth == src.getWidth()) return src;
				targetHeight = Math.round((float) targetWidth * ratio);
			}
			else if((ratio <= 1 && resizeMode == Mode.AUTOMATIC))
			{
				if		((targetWidth)>(targetHeight))	targetWidth		= Math.round((float) targetHeight * ((float) currentWidth / (float) currentHeight));
				else if	((targetWidth)<(targetHeight))	targetHeight	= Math.round((float) targetWidth * ((float) currentHeight / (float) currentWidth));
				else									return src;
			}
			else
			{
				if (targetHeight == src.getHeight()) return src;
				targetWidth = Math.round((float) targetHeight / ratio);
			}
		}
		
		if (scalingMethod == Scale.Method.AUTOMATIC)	scalingMethod = determineScalingMethod(targetWidth, targetHeight, ratio);

		result = (scalingMethod == Scale.Method.SPEED)?scaleImage(src, targetWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR):((scalingMethod == Scale.Method.BALANCED)?scaleImage(src, targetWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR):((scalingMethod == Scale.Method.QUALITY || scalingMethod == Scale.Method.ULTRA_QUALITY)?((targetWidth > currentWidth || targetHeight > currentHeight)?scaleImage(src, targetWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_BICUBIC):scaleImageIncrementally(src, targetWidth, targetHeight, scalingMethod, RenderingHints.VALUE_INTERPOLATION_BICUBIC)):result));

		if (ops != null && ops.length > 0)
			result = apply(result, ops);

		return result;
	}

	private static BufferedImage createOptimalImage(BufferedImage src, int width, int height) throws IllegalArgumentException
	{
		if (width < 0 || height < 0) throw new IllegalArgumentException("width [" + width + "] and height [" + height + "] must be >= 0");
		return new BufferedImage(width, height, (src.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB));
	}

	private static BufferedImage copyToOptimalImage(BufferedImage src) throws IllegalArgumentException
	{
		if (src == null)
			throw new IllegalArgumentException("src cannot be null");

		int type = (src.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
		BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), type);

		Graphics g = result.getGraphics();
		g.drawImage(src, 0, 0, null);
		g.dispose();

		return result;
	}

	private static Method determineScalingMethod(int targetWidth, int targetHeight, float ratio)
	{
		int length = (ratio <= 1 ? targetWidth : targetHeight);

		Method result = Method.SPEED;

		if (length <= Scale.THRESHOLD_QUALITY_BALANCED)		result = Method.QUALITY;
		else if (length <= Scale.THRESHOLD_BALANCED_SPEED)	result = Method.BALANCED;
		return result;
	}

	private static BufferedImage scaleImage(BufferedImage src, int targetWidth, int targetHeight, Object interpolationHintValue)
	{

		BufferedImage result = createOptimalImage(src, targetWidth, targetHeight);
		Graphics2D resultGraphics = result.createGraphics();

		resultGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolationHintValue);
		resultGraphics.drawImage(src, 0, 0, targetWidth, targetHeight, null);

		resultGraphics.dispose();
		return result;
	}

	private static BufferedImage scaleImageIncrementally(BufferedImage src, int targetWidth, int targetHeight, Method scalingMethod, Object interpolationHintValue)
	{
		boolean hasReassignedSrc = false;

		int currentWidth = src.getWidth();
		int currentHeight = src.getHeight();

		int fraction = (scalingMethod == Method.ULTRA_QUALITY ? 7 : 2);

		do
		{
			int prevCurrentWidth = currentWidth;
			int prevCurrentHeight = currentHeight;

			if (currentWidth > targetWidth)
			{
				currentWidth -= (currentWidth / fraction);

				if (currentWidth < targetWidth)
					currentWidth = targetWidth;
			}

			if (currentHeight > targetHeight)
			{
				currentHeight -= (currentHeight / fraction);

				if (currentHeight < targetHeight)
					currentHeight = targetHeight;
			}

			if (prevCurrentWidth == currentWidth && prevCurrentHeight == currentHeight)
				break;

			BufferedImage incrementalImage = scaleImage(src, currentWidth, currentHeight, interpolationHintValue);

			if (hasReassignedSrc) src.flush();

			src					= incrementalImage;
			hasReassignedSrc	= true;
		}
		while (currentWidth != targetWidth || currentHeight != targetHeight);

		return src;
	}

	private static BufferedImage apply(BufferedImage src, BufferedImageOp... ops) throws IllegalArgumentException, ImagingOpException
	{
		if (src == null)					throw new IllegalArgumentException("src cannot be null");
		if (ops == null || ops.length == 0)	throw new IllegalArgumentException("ops cannot be null or empty");
		int type = src.getType();

		if (!(type == BufferedImage.TYPE_INT_RGB || type == BufferedImage.TYPE_INT_ARGB))
			src = copyToOptimalImage(src);

		boolean hasReassignedSrc = false;

		for (int i = 0; i < ops.length; i++)
		{
			BufferedImageOp op = ops[i];

			if (op == null) continue;

			Rectangle2D resultBounds = op.getBounds2D(src);

			if (resultBounds == null) throw new ImagingOpException("BufferedImageOp [" + op.toString() + "] getBounds2D(src) returned null bounds for the target image; this should not happen and indicates a problem with application of this type of op.");

			BufferedImage dest		= createOptimalImage(src,(int) Math.round(resultBounds.getWidth()),(int) Math.round(resultBounds.getHeight()));
			BufferedImage result	= op.filter(src, dest);

			if (hasReassignedSrc)
				src.flush();

			src					= result;
			hasReassignedSrc	= true;
		}
		return src;
	}

	// >-------[nested]---------------------------------------------------------------------------------------< //
	
	public static enum Method	{ AUTOMATIC, SPEED, BALANCED, QUALITY, ULTRA_QUALITY;	}
	public static enum Mode		{ AUTOMATIC, FIT_EXACT, FIT_TO_WIDTH, FIT_TO_HEIGHT;	}

}
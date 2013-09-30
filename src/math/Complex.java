package math;

@SuppressWarnings("serial")
public final class Complex extends Number implements Cloneable
{
	private double	re	= 0.0;
	private double	im	= 0.0;
  
	private static double hypot(double x, double y)
	{ return Math.sqrt(x*x + y*y); }

	private Complex rtop() 
	{ return new Complex(hypot(re, im), Math.atan2(im, re));}
  
	private Complex ptor() 
	{ return new Complex(re*Math.cos(im), re*Math.sin(im)); }
  
	public Complex(double r, double i)
	{
		re = r;
		im = i;
	}

	public Complex(double r)
	{ this(r,0); }
  
	public Complex()
	{ this(0,0); }
  
	public Complex(Complex d)
	{
		re = d.re;
		im = d.im;
	}  

	public void set(double re, double im)
	{
		this.re = re;
		this.im = im;
	}

	public double real()
	{ return re; }
  
	public double aimag()
	{ return im; }

	public double abs()
	{ return hypot(re, im);}

	public double arg()
	{ return Math.atan2(im, re);}

	public Complex conjg()
	{ return new Complex(re, -im); }

	public Complex exp( Complex d)
	{
		double r = Math.exp(re);
		return new Complex(r*Math.cos(im), r*Math.sin(im));
	}

	public Complex pow(double r)
	{
		Complex polar = this.rtop();
		polar.re = Math.pow(polar.re, r);
		polar.im *= r;
		return polar.ptor();
	}

	public Complex power(int r)
	{
		switch(r)
		{
			case 0: return new Complex(1);
			case 1: return new Complex(this);
			case 2: return this.multiply(this);
			default: 
				Complex polar = this.rtop();
				polar.re = Math.pow(polar.re, r);
				polar.im *= r;
				return polar.ptor();
		}
	}

	public Complex sqrt()
	{
		Complex polar = this.rtop();
		polar.re = Math.sqrt(polar.re);
		polar.im = 0.5*polar.im;
		return polar.ptor();
	}

	public Complex add( Complex d2)
	{ return new Complex(re+d2.re, im+d2.im); }

	public Complex negate()
	{ return new Complex(-re, -im); }

	public Complex subtract(Complex d2)
	{ return new Complex(re-d2.re, im-d2.im); }

	public Complex multiply(Complex d2)
	{ return new Complex(re*d2.re-im*d2.im, re*d2.im+im*d2.re); }

	public Complex divide(Complex d2)
	{
		double denom = d2.re*d2.re+d2.im*d2.im;
		return new Complex((re*d2.re+im*d2.im)/denom, (im*d2.re-re*d2.im)/denom);
	}

	public Complex addEqual(Complex d)
	{
		re += d.re;
		im += d.im;
		return this;
	}

	public Complex subtractEqual( Complex d)
	{
		re -= d.re;
		im -= d.im;
		return this;
	}

	public Complex multiplyEqual( Complex d)
	{
		double oldRe = re;
		double oldDRe = d.re;
		re = re*d.re - im*d.im; 
		im = im*oldDRe + oldRe*d.im;
		return this;
	}

	public Complex divideEqual( Complex d)
	{
		double denom	= d.re*d.re+d.im*d.im;
		double oldRe	= re;
		double oldDRe	= d.re;
		re	= (re*d.re + im*d.im)/denom;
		im	= (im*oldDRe - oldRe*d.im)/denom;
		return this;
	}

	public boolean equals(Object d1)
	{
		try
		{ return (re==((Complex)d1).re && im==((Complex)d1).im); }
		catch(ClassCastException e)
		{ return false; }
	}
	
	public Object clone() 
	{ return new Complex(re,im); }

	public String toString()
	{ return  "(" + new Double(re) + "," + new Double(im) + ")"; }

	public int hashCode()
	{ return (new Double(re/2+im/2)).hashCode(); }

	public byte byteValue() 
	{ return (byte) re; }
  
	public double doubleValue() 
	{ return re; }

	public float floatValue() 
	{ return (float) re; }

	public int intValue() 
	{ return (int) re; }

	public long longValue() 
	{ return (long) re; }

	public short shortValue() 
	{ return (short) re; }

}
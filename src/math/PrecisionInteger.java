package math;

public final class PrecisionInteger extends PrecisionNumber
{

	public PrecisionInteger()
	{ super(true, precision_digits); }
  
	public PrecisionInteger(int precision)
	{ super(true, precision); }
  
	public PrecisionInteger( PrecisionInteger in) 
	{ super((PrecisionNumber)in); }

	public PrecisionInteger(double d, int precision)
	{
		super(true, precision);
		
		PrecisionNumber mpt1	= new PrecisionNumber(6,false); 
		PrecisionNumber mpt2	= new PrecisionNumber(8,false);
		
		dmc		(new NumberChunk(d), mpt1);
		infr	(mpt1, this, mpt2, Math.min(maxnw - 2, nw));
	}
  
	public PrecisionInteger(String str)
	{ this(str, precision_digits); }
  
	public PrecisionInteger(String str, int precision)
	{
		super(true, precision);
		
		int lnw	= Math.min(nw, maxnw-2);
		int nw2	= lnw+2;
		
		PrecisionNumber mpt1	= new PrecisionNumber(nw2, false);
		PrecisionNumber mpt2	= new PrecisionNumber(nw2, false);
		
		dexc	(str.toCharArray(), str.length(), mpt1, lnw);
		infr	(mpt1, this, mpt2, lnw);
	}
  
	public PrecisionInteger(PrecisionReal mpr)
	{ this(mpr, precision_digits); }
  
	public PrecisionInteger(PrecisionReal mpr, int precision)
	{
		super(true, precision);
		
		int lnw				= Math.min(nw, maxnw - 2);
		PrecisionNumber mpt1	= new PrecisionNumber(lnw+1,false);
		PrecisionNumber mpt2	= new PrecisionNumber(lnw+2,false);
		
		eq		(mpr, mpt1, lnw);
		infr	(mpt1, this, mpt2, lnw);
	}
  
	public PrecisionInteger(PrecisionComplex mpc)
	{ this(mpc, precision_digits); }
  
	public PrecisionInteger(PrecisionComplex mpc, int precision)
	{
		super(true, precision);
		
		int lnw				= Math.min(nw, maxnw - 2);
		PrecisionNumber mpt1	= new PrecisionNumber(lnw+1,false); 
		PrecisionNumber mpt2	= new PrecisionNumber(lnw+2,false);
		
		eq		(mpc.r, mpt1, lnw);
		infr	(mpt1, this, mpt2, lnw);
	}
  
	public PrecisionInteger assign(PrecisionNumber ja)
	{
		if(ja != this)
		{
			if(ja.maxnw == this.maxnw && ja instanceof PrecisionInteger)
				PrecisionNumber.eq(ja, this, Math.min(nw, this.maxnw - 1));
			else
			{
				int nw1			= Math.min(nw, ja.maxnw-1);
				int nw2			= Math.min(nw, maxnw-2);
				PrecisionReal mpt1	= new PrecisionReal();  
				PrecisionReal mpt2	= new PrecisionReal();
				
				eq		(ja, mpt1, nw1);
				infr	(mpt1, this, mpt2, nw2);
			}
		}
		return this;
	}

	public PrecisionInteger add(PrecisionInteger ja)
	{
		PrecisionInteger res	= new PrecisionInteger(); 
		PrecisionInteger mpt1	= new PrecisionInteger();
		PrecisionInteger mpt2	= new PrecisionInteger();
		
		add		(ja, this, mpt1, nw);
		infr	(mpt1, res, mpt2, nw);
		
		return res;
	}

	public PrecisionInteger subtract(PrecisionInteger ja)
	{
		PrecisionInteger res	= new PrecisionInteger();
		PrecisionInteger mpt1	= new PrecisionInteger();
		PrecisionInteger mpt2	= new PrecisionInteger();
		
		sub		(this, ja, mpt1, nw);
		infr	(mpt1, res, mpt2, nw);
		
		return res;
	}
	
	public PrecisionInteger negate()
	{
		PrecisionInteger res = new PrecisionInteger();
		
		eq	(this, res, nw);
		
		res.sign = !this.sign;
		
		return res;
	}
  
	public PrecisionInteger multiply(PrecisionInteger ja)
	{
		PrecisionInteger res	= new PrecisionInteger(); 
		PrecisionInteger mpt1	= new PrecisionInteger();
		PrecisionInteger mpt2	= new PrecisionInteger();
		
		_mul	(ja, this, mpt1, nw);
		infr	(mpt1, res, mpt2, nw);
		
		return res;
	}

	public PrecisionInteger divide(PrecisionInteger ja)
	{
		PrecisionInteger res	= new PrecisionInteger(); 
		PrecisionInteger mpt1	= new PrecisionInteger();
		PrecisionInteger mpt2	= new PrecisionInteger();

		_div	(this, ja, mpt1, nw);
		infr	(mpt1, res, mpt2, nw);
		
		return res;
	}

	public PrecisionInteger mod(PrecisionInteger ja)
	{
		PrecisionInteger res	= new PrecisionInteger();
		PrecisionInteger mpt1	= new PrecisionInteger();
		PrecisionInteger mpt2	= new PrecisionInteger(); 
		PrecisionInteger mpt3	= new PrecisionInteger();
		
		_div	(this, ja, mpt1, nw);
		infr	(mpt1, mpt2, mpt3, nw);
		_mul	(ja, mpt2, mpt1, nw);
		sub		(this, mpt1, res, nw);
		
		return res;
	}

	public PrecisionInteger abs()
	{
		PrecisionInteger res = new PrecisionInteger();  
		
		eq	(this, res, nw);
		
		res.sign = true;
		
		return res;
	}

	public PrecisionInteger max(PrecisionInteger val)
	{ return (compare(this, val, nw) >= 0)?this:val; }
 
	public PrecisionInteger min(PrecisionInteger val)
	{ return (compare(this, val, nw) < 0)?this:val; }
  
	public PrecisionInteger sign(PrecisionNumber val)
	{
		PrecisionInteger res = new PrecisionInteger();
		
		eq	(this, res, nw);
		
		res.sign = val.sign;
		
		return res;
	}

	public PrecisionInteger pow(PrecisionInteger exponent)
	{
		PrecisionInteger res	= new PrecisionInteger(); 
		PrecisionInteger mpt1	= new PrecisionInteger();
		PrecisionInteger mpt2	= new PrecisionInteger();

		_log	(this, PrecisionReal.PI, PrecisionReal.LOG2, mpt1, nw);
		_mul	(mpt1, exponent, mpt2, nw);
		_exp	(mpt2, PrecisionReal.PI, PrecisionReal.LOG2, mpt1, nw);
		nint	(mpt1, res, nw);
		
		return res;
	}

	public PrecisionInteger pow(int exponent)
	{
		PrecisionInteger res= new PrecisionInteger(), mpt1= new PrecisionInteger();  
		
		_npw	(this, exponent, mpt1, nw);
		nint	(mpt1, res, nw);
		
		return res;
	}

}
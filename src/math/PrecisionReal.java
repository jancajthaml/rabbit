package math;

import static math.PrecisionComplex._cos;
import static math.PrecisionComplex._ang;

public final class PrecisionReal extends PrecisionNumber
{

	public static PrecisionReal LOG2; 
	public static PrecisionReal LOG10;
	public static PrecisionReal PI;
	public static PrecisionReal EPSILON;

	public PrecisionReal()
	{ super(true, precision_digits); }

	public PrecisionReal(boolean b, int precision)
	{ super(b, precision); }

	public PrecisionReal(int size, boolean b)
	{ super(size, b); }

	public PrecisionReal(PrecisionReal in) 
	{ super((PrecisionNumber)in); }

	public PrecisionReal(double d)
	{ super(d, precision_digits);}

	public PrecisionReal(double d, int precision)
	{ super(d, precision);}

	public PrecisionReal(String str)
	{ super(str, precision_digits); }

	public PrecisionReal(String str, int precision)
	{ super(str, precision); }
  
	public PrecisionReal(PrecisionInteger in)
	{ super((PrecisionNumber)in); };

	PrecisionReal(int size)
	{ super(size, false); }

	public PrecisionReal(PrecisionComplex mpc)
	{ this(mpc, precision_digits); }

	public PrecisionReal(PrecisionComplex mpc, int precision)
	{
		super(true, precision);

		eq (mpc.r, this, Math.min(nw, maxnw - 1));
	}

	public PrecisionReal assign(PrecisionNumber ja)
	{
		if(ja != this)
			eq (ja, this, Math.min(nw, this.maxnw - 1));

		return this;
	}

	public PrecisionReal add(PrecisionReal ja)
	{
		PrecisionReal res = new PrecisionReal();  
		
		add (this, ja, res, nw);
		
		return res;
	}

	public PrecisionReal subtract(PrecisionReal ja)
	{
		PrecisionReal res = new PrecisionReal();  
		
		sub (this, ja, res, nw);
		
		return res;
	}

	public PrecisionReal negate()
	{
		PrecisionReal res = new PrecisionReal();
    
		eq (this, res, nw);
		
		res.sign = !this.sign;
		
		return res;
	}

	public PrecisionReal multiply(PrecisionReal ja)
	{
		PrecisionReal res = new PrecisionReal();  
		
		_mul (this, ja, res, nw);
		return res;
	}

	public PrecisionReal divide(PrecisionReal ja)
	{
		PrecisionReal res = new PrecisionReal();  
		
		_div (this, ja, res, nw);
		
		return res;
	}

	public PrecisionReal abs()
	{
		PrecisionReal res = new PrecisionReal();  
		
		eq (this, res, nw);
		
		res.sign = true;
		
		return res;
	}

	public PrecisionReal max(PrecisionReal val)
	{ return (compare(this, val, nw) >= 0)?this:val; }
 
	public PrecisionReal min(PrecisionReal val)
	{ return (compare(this, val, nw) < 0)?this:val; }
  
	public PrecisionReal sign(PrecisionNumber val)
	{
		PrecisionReal res = new PrecisionReal();
		
		eq (this, res, nw);
		
		res.sign = val.sign;
		
		return res;
	}

	public PrecisionReal pow(PrecisionNumber exponent)
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();
		PrecisionNumber mpt2	= new PrecisionNumber();
		
		_log (this, PI, LOG2, mpt1, nw);
		_mul (mpt1, exponent, mpt2, nw);
		_exp (mpt2, PI, LOG2, mpt1, nw);
		
		return res;    
	}

	public PrecisionReal pow(int exponent)
	{
		PrecisionReal res = new PrecisionReal();
		
		_npw (this, exponent, res, nw);
		
		return res;
	}

	public PrecisionReal pow(double exponent)
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();
		PrecisionNumber mpt2	= new PrecisionNumber();
    
		_log (this, PI, LOG2, mpt1, nw);
		muld (mpt1, new NumberChunk(exponent), mpt2, nw);
		_exp (mpt2, PI, LOG2, res, nw);

		return res;
	}

	public PrecisionReal acos()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();
		PrecisionNumber mpt2	= new PrecisionNumber();
		PrecisionNumber mpt3	= new PrecisionNumber();
    
		dmc		(new NumberChunk(1), mpt1);
		_mul	(this, this, mpt2, nw);
		sub		(mpt1, mpt2, mpt3, nw);
		_sqr	(mpt3, mpt1, nw);
		_ang	(this, mpt1, PI, res, nw);
		
		return res;
	}

	public PrecisionReal aint()
	{
		PrecisionReal res = new PrecisionReal(); 
		
		infr	(this, res, new PrecisionNumber(), nw);
		
		return res;
	}

	public PrecisionReal anint()
	{
		PrecisionReal res = new PrecisionReal();
		
		nint	(this, res, nw);
		
		return res;
	}

	public PrecisionReal asin()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();
		PrecisionNumber mpt2	= new PrecisionNumber();
		PrecisionNumber mpt3	= new PrecisionNumber();

		dmc		(new NumberChunk(1), mpt1);
		_mul	(this, this, mpt2, nw);
		sub		(mpt1, mpt2, mpt3, nw);
		_sqr	(mpt3, mpt1, nw);
		_ang	(mpt1, this, PI, res, nw);
		
		return res;
	}

	public PrecisionReal atan()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber(6,false);

		dmc		(new NumberChunk(1), mpt1);
		_ang	(this, mpt1, PI, res, nw);
		
		return res;
	}

	public PrecisionReal atan2(PrecisionReal val)
	{
		PrecisionReal res = new PrecisionReal();

		_ang (val, this, PI, res, nw);
		
		return res;
	}

	public PrecisionReal cos()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();

		_cos (this, PI, res, mpt1, nw);
		
		return res;
	}

	public PrecisionReal cosh()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();

		_cosh	(this, PI, LOG2, res, mpt1, nw);

		return res;
	}

	public PrecisionReal exp()
	{
		PrecisionReal res = new PrecisionReal();
		
		_exp	(this, PI, LOG2, res, nw);

		return res;
	}

	public PrecisionReal log()
	{
		PrecisionReal res = new PrecisionReal();

		_log	(this, PI, LOG2, res, nw);

		return res;
	}

	public PrecisionReal log10()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();

		_log	(this, PI, LOG2, mpt1, nw);
		_div	(mpt1, LOG10, res, nw);
		
		return res;    
	}

	public void csshf(PrecisionReal cosh, PrecisionReal sinh)
	{ _cosh(this, PI, LOG2, cosh, sinh, nw); }

	public void cssnf(PrecisionReal cosine, PrecisionReal sine)
	{ _cos(this, PI, cosine, sine, nw); }
  
	public PrecisionReal nrtf(int ib)
	{
		PrecisionReal res = new PrecisionReal();
		
		_nrt	(this, ib, res, nw);
		
		return res;
	}

	public static PrecisionReal rand()
	{
		PrecisionReal res = new PrecisionReal();

		rand	(res, nw);
		
		return res;
	}

	public PrecisionInteger nint()
	{
		PrecisionInteger res  = new PrecisionInteger();

		nint	(this, res, nw);
		
		return res;
	}

	public PrecisionReal sin()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();

		_cos(this, PI, mpt1, res, nw);
		
		return res;
	}

	public PrecisionReal sinh()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();

		_cosh	(this, PI, LOG2, mpt1, res, nw);
		
		return res;
	}

	public PrecisionReal sqrt()
	{
		PrecisionReal res = new PrecisionReal();

		_sqr	(this, res, nw);
		
		return res;
	}

	public PrecisionReal tan()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();
		PrecisionNumber mpt2	= new PrecisionNumber();

		_cos	(this, PI, mpt1, mpt2, nw);
		_div	(mpt1, mpt2, res, nw);
		
		return res;
	}

	public PrecisionReal tanh()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();
		PrecisionNumber mpt2	= new PrecisionNumber();

		_cosh	(this, PI, PI, mpt1, mpt2, nw);
		_div	(mpt1, mpt2, res, nw);
		
		return res;
	}

	static
	{
		PI = new PrecisionReal(mp21,false);
		_pi		(PI, nw+1);

		LOG2 = new PrecisionReal(mp21,false);
		
		PrecisionNumber t2 = new PrecisionNumber(6,false);
		
		dmc		(new NumberChunk(2), t2);
		_log	(t2, PI, LOG2, LOG2, nw+1);

		LOG10 = new PrecisionReal(mp21,false);
		
		dmc		(new NumberChunk(10), t2);
		_log	(t2, PI, LOG2, LOG10, nw+1);

		EPSILON = new PrecisionReal(mp21,false);
		
		dmc		(new NumberChunk(10), t2);
		_npw	(t2, ellog10, EPSILON, nw+1);

		PI.number_words--;
		LOG2.number_words--;
		LOG10.number_words--;
		EPSILON.number_words--;
	}
	
}
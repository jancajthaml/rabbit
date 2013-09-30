package math;


import static math.PrecisionNumber.inp_complex;

import static math.PrecisionNumber._sq;
import static math.PrecisionNumber._sqr;
import static math.PrecisionNumber._div;
import static math.PrecisionNumber._mul;

import static math.PrecisionNumber.muld;
import static math.PrecisionNumber.dmc;

import static math.PrecisionNumber.eq;
import static math.PrecisionNumber.mul;
import static math.PrecisionNumber.sub;
import static math.PrecisionNumber.compare;

import static math.PrecisionNumber.ssn_complex;

public final class PrecisionComplex extends MathGlobal 
{

	PrecisionNumber r		= null;
	PrecisionNumber i		= null;

	private PrecisionComplex(int size, boolean b)
	{
		r = new PrecisionNumber(size, b);
		i = new PrecisionNumber(size, b);
	}

	private static PrecisionComplex createComplex(int size)
	{ return new PrecisionComplex(size,false); }

	private int constHelper(int precision)
	{
		int maxnw	= precisionToSize(precision);
		r			= new PrecisionNumber(maxnw,false); 
		i			= new PrecisionNumber(maxnw,false);
		
		return maxnw;
	}

	public PrecisionComplex()
	{ constHelper(precision_digits); }

	public PrecisionComplex(boolean b, int precision)
	{ constHelper(precision); }

	public PrecisionComplex(double real, int precision)
	{
		constHelper(precision);
		dmc(new NumberChunk(real), r);
	}

	public PrecisionComplex(Complex dc)
	{ this(dc, precision_digits); }

	public PrecisionComplex(Complex dc, int precision)
	{
		constHelper(precision);
		
		dmc	(new NumberChunk(dc.real()), r);
		dmc	(new NumberChunk((double)dc.aimag()), i);
	}

	public PrecisionComplex(String real)
	{ this(real, precision_digits); }

	public PrecisionComplex(String real, int precision)
	{ inp_complex (real.toCharArray(), real.length(), r, Math.min(nw, constHelper(precision)-2)); }

	public PrecisionComplex(PrecisionNumber real)
	{
		int maxnw	= real.maxnw;
		r			= new PrecisionNumber(maxnw,false);
		i			= new PrecisionNumber(maxnw,false);

		eq	(real, r, nw);
	}
  
	public PrecisionComplex(PrecisionComplex in)
	{
		r	= new PrecisionNumber(in.r.maxnw,false);
		i	= new PrecisionNumber(in.i.maxnw,false);
		
		eq_complex	(in, this, nw);
	}

	public PrecisionComplex(int real, int imag)
	{ this((double)real, (double)imag, precision_digits); }
  
	public PrecisionComplex(int real, int imag, int precision)
	{ this((double)real, (double)imag, precision); }

	public PrecisionComplex(double real, double imag)
	{ this(real, imag, precision_digits);  }

	public PrecisionComplex(double real, double imag, int precision)
	{
		constHelper(precision);

		dmc(new NumberChunk(real), r); 
		dmc(new NumberChunk(imag), i);
	}

	public PrecisionComplex(String real, String imag)
	{ this (real, imag, precision_digits); }

	public PrecisionComplex(String real, String imag, int precision)
	{
		int lnw	= Math.min(nw, constHelper(precision) -1);
		
		inp_complex	(real.toCharArray(), real.length(), r, lnw);
		inp_complex	(imag.toCharArray(), imag.length(), i, lnw);
	}

	public PrecisionComplex(PrecisionNumber real,  PrecisionNumber imag)
	{
		r = new PrecisionNumber(real.maxnw,false);
		i = new PrecisionNumber(imag.maxnw,false);

		eq(real, r, nw); 
		eq(imag, i, nw);
	}

	public PrecisionComplex assign(PrecisionComplex zb)
	{
		eq_complex	(zb, this, Math.min(nw, r.maxnw));
		
		return this;
	}

	public PrecisionComplex add(PrecisionComplex ja)
	{
		PrecisionComplex res = new PrecisionComplex();
		
		add_complex(this, ja, res, nw);
		
		return res;
	}

	public PrecisionComplex subtract(PrecisionComplex ja)
	{
		PrecisionComplex res = new PrecisionComplex();
		
		csub(this, ja, res, nw);
		
		return res;
	}

	public PrecisionComplex negate()
	{
		PrecisionComplex res = new PrecisionComplex();

		eq_complex(this, res, nw);
		
		res.r.sign = !res.r.sign;
		res.i.sign = !res.i.sign;
		return res;
	}

	public PrecisionComplex multiply(PrecisionComplex ja)
	{
		PrecisionComplex res = new PrecisionComplex();  
		_cml(this, ja, res, nw);
		return res;
	}

	public PrecisionComplex divide(PrecisionComplex ja)
	{
		PrecisionComplex res = new PrecisionComplex();  
		_cdv(this, ja, res, nw);
		return res;
	}

	public boolean equals(Object o)
	{
		if(o==this) return true;
		try								{ return 0 == compare(r, ((PrecisionComplex)o).r, nw) && 0 == compare(i, ((PrecisionComplex)o).i, nw); }
		catch(ClassCastException cce)	{ return false;}
	}

	public String toString()
	{ return (r.toString() + i.toString()); }

	public PrecisionReal abs()
	{
		PrecisionReal res		= new PrecisionReal();
		PrecisionNumber mpt1	= new PrecisionNumber();
		PrecisionNumber mpt2	= new PrecisionNumber();
		PrecisionNumber mpt3	= new PrecisionNumber();

		_mul(r, r, mpt1, nw);
		_mul(i, i, mpt2, nw);
		PrecisionNumber.add(mpt1, mpt2, mpt3, nw);
		_sqr(mpt3, res, nw);
		return res;
	}

	public PrecisionReal real()
	{
		PrecisionReal res = new PrecisionReal();

		eq (r, res, nw);
		
		return res;
	}

	public PrecisionReal aimag()
	{
		PrecisionReal res = new PrecisionReal();

		eq (i, res, nw);
		
		return res;
	}

	public PrecisionComplex conjg()
	{
		PrecisionComplex res = new PrecisionComplex();

		eq_complex(this, res, nw);

		res.i.sign = ! i.sign;
		
		return res;
	}

	public double doubleValue()
	{ return r.doubleValue(); }

	public int intValue()
	{ return r.intValue(); }

	public float floatValue()
	{ return r.floatValue(); }

	public Complex complexValue()
	{ return new Complex(r.toDPE().value(), i.toDPE().value()); }

	public PrecisionComplex sqrt()
	{
		PrecisionComplex res = new PrecisionComplex();

		sqt_complex(this, res, nw);
		
		return res;
	}

	public PrecisionComplex pow(int exponent)
	{
		PrecisionComplex res = new PrecisionComplex();  
		
		_cpw(this, exponent, res, nw);
		
		return res;
	}

	static void add_complex(PrecisionComplex a, PrecisionComplex b, PrecisionComplex c, int lnw)
	{
		PrecisionNumber.add (a.r, b.r, c.r, lnw);
		PrecisionNumber.add (a.i, b.i, c.i, lnw);
	}

	static void div_complex(PrecisionComplex a, PrecisionComplex b, PrecisionComplex c, int lnw)
	{
		int nw2 = lnw+2;
		PrecisionNumber f = new PrecisionNumber(6,false);
		PrecisionNumber sk0 = new PrecisionNumber(nw2,false);
		PrecisionNumber sk1 = new PrecisionNumber(nw2,false);
		PrecisionNumber sk2 = new PrecisionNumber(nw2,false);
		PrecisionNumber sk3 = new PrecisionNumber(nw2,false);
		PrecisionNumber sk4 = new PrecisionNumber(nw2,false); 
		PrecisionNumber ar=a.r;
		PrecisionNumber ai=a.i;
		PrecisionNumber br=b.r;
		PrecisionNumber bi=b.i;

		if (b.r.number_words  == 0 && b.i.number_words == 0) 
			throw new ArithmeticException("div_complex: Divisor is zero.");
    
		f.number_words=1;
		f.exponent=0;
		f.sign=true;
		f.mantissa[0]=1;
		f.mantissa[1]=0;

		mul (ar, br, sk0, lnw);
		mul (ai, bi, sk1, lnw);
		PrecisionNumber.add (sk0, sk1, sk2, lnw);
		sub (sk0, sk1, sk3, lnw);
		PrecisionNumber.add (ar, ai, sk0, lnw);
		sub (br, bi, sk1, lnw);
		mul (sk0, sk1, sk4, lnw);
		sub (sk4, sk3, sk1, lnw);
		mul (br, br, sk0, lnw);
		mul (bi, bi, sk3, lnw);
		PrecisionNumber.add (sk0, sk3, sk4, lnw);
		PrecisionNumber.mpdiv (f, sk4, sk0, lnw);
		mul (sk2, sk0, c.r, lnw);
		mul (sk1, sk0, c.i, lnw);    
	}

	static void eq_complex (PrecisionComplex a, PrecisionComplex b, int lnw)
	{
		int i				= 0;
		int n1				= Math.min (a.r.number_words, lnw);
		int n2				= Math.min (a.i.number_words, lnw);
		b.r.sign			= a.r.sign;
		b.r.number_words	= n1;
		b.r.exponent		= a.r.exponent;
		b.i.sign			= a.i.sign;
		b.i.number_words	= n2;
		b.i.exponent		= a.i.exponent;

		for (i = 0; i<n1; i++)	b.r.mantissa[i] = a.r.mantissa[i];
		for (i=0; i<n2; i++)	b.i.mantissa[i] = a.i.mantissa[i];
	}

	static void mpcmul(PrecisionComplex a, PrecisionComplex b, PrecisionComplex c, int lnw)
	{
		int nw2 = lnw+2;
		PrecisionNumber sk0 = new PrecisionNumber(nw2,false);
		PrecisionNumber sk1 = new PrecisionNumber(nw2,false);
		PrecisionNumber sk2 = new PrecisionNumber(nw2,false);
		PrecisionNumber sk3 = new PrecisionNumber(nw2,false);

		mul (a.r, b.r, sk0, lnw);
		mul (a.i, b.i, sk1, lnw);
		sub (sk0, sk1, c.r, lnw);
		PrecisionNumber.add (sk0, sk1, sk2, lnw);
		PrecisionNumber.add (a.r, a.i, sk0, lnw);
		PrecisionNumber.add (b.r, b.i, sk1, lnw);
		mul (sk0, sk1, sk3, lnw);
		sub (sk3, sk2, c.i, lnw);    
	}

	static void mpcpwr(PrecisionComplex a, int n, PrecisionComplex b, int lnw)
	{
		int j;
		double t1;
		int na1 = Math.min (a.r.number_words, lnw);
		int na2 = Math.min (a.i.number_words, lnw);

		if (na1 == 0 && na2 == 0) 
		{
			if (n >= 0) 
			{
				zero(b);
				return;
			}
			else throw new ArithmeticException("mpcpwr: Argument is zero and N is negative or zero.");
		}

		int nws = lnw;
		
		lnw++;
		
		int nn = Math.abs (n);

		if (nn == 0) 
		{
			b.r.number_words=1;
			b.r.sign=true;
			b.r.exponent=0;
			b.r.mantissa[0]=1;
			b.r.mantissa[1]=0;
			PrecisionNumber.zero(b.i);
			return;
		}

		PrecisionComplex f		= createComplex(6);
		PrecisionComplex sk0	= createComplex(lnw+3);
		PrecisionComplex sk1	= createComplex(lnw+3);
		PrecisionComplex sk2	= createComplex(lnw+3);   

		eq_complex (a, sk0, lnw); 

		f.r.number_words=1;
		f.r.sign=true;
		f.r.exponent=0;
		f.r.mantissa[0]=1;
		f.r.mantissa[1]=0;
		boolean skip = false;

		if (nn == 1) 
		{
			eq_complex (sk0, sk2, lnw); 
			skip = true;
		}
		else if (nn == 2) 
		{
			mpcmul (sk0, sk0, sk2, lnw);
			skip = true;
		}

		if (!skip)
		{
			int mn;
			int kn;
			int kk;
			t1 = nn;
			mn = (int)(CL2 * Math.log (t1) + 1.0 + 5.6843418860808015e-14);
			eq_complex (f, sk2, lnw);
			kn = nn;
      
			for (j = 1; j<=mn; j++)
			{
				kk = kn / 2;
				
				if (kn != 2 * kk) 
				{
					mpcmul (sk2, sk0, sk1, lnw);
					eq_complex (sk1, sk2, lnw);
				}
				
				kn = kk;
				
				if (j < mn) 
				{
					mpcmul (sk0, sk0, sk1, lnw);
					eq_complex (sk1, sk0, lnw);
				}
			}
		}

		if (n < 0) 
		{
			eq_complex	(f, sk1, lnw);
			div_complex	(sk1, sk2, sk0, lnw);
			eq_complex	(sk0, sk2, lnw);
		}
		
		eq_complex		(sk2, b, lnw);
		roun_complex	(b, nws);
	}

	private static void roun_complex(PrecisionComplex in, int lnw)
	{ PrecisionNumber.round(in.r, lnw); PrecisionNumber.round(in.i, lnw);}

	static void sqt_complex (PrecisionComplex a, PrecisionComplex b, int lnw)
	{
		int nw2			= lnw+2;
		PrecisionNumber sk0 = new PrecisionNumber(nw2,false);
		PrecisionNumber sk1 = new PrecisionNumber(nw2,false); 
		PrecisionNumber sk2 = new PrecisionNumber(nw2,false);

		if (a.r.number_words == 0 && a.i.number_words == 0) 
		{
			zero(b);
			return;
		}

		mul	(a.r, a.r, sk0, lnw);
		mul	(a.i, a.i, sk1, lnw);
		PrecisionNumber.add		(sk0, sk1, sk2, lnw);
		PrecisionNumber.mpsqrt	(sk2, sk0, lnw);
		eq (a.r, sk1, lnw);
		
		sk1.sign = true;
		
		PrecisionNumber.add (sk0, sk1, sk2, lnw);
		muld (sk2, new NumberChunk(0.50), sk1, lnw);
		PrecisionNumber.mpsqrt (sk1, sk0, lnw);
		muld (sk0, new NumberChunk(2.0), sk1, lnw);
		
		if (a.r.number_words >= 0) 
		{
			eq (sk0, b.r, lnw);
			PrecisionNumber.mpdiv (a.i, sk1, b.i, lnw);
		}
		else 
		{
			PrecisionNumber.mpdiv (a.i, sk1, b.r, lnw);
      
			b.r.sign = true;
			
			eq (sk0, b.i, lnw);
			
			b.i.number_words = a.i.number_words;
		}
	}

	static void csub (PrecisionComplex a, PrecisionComplex b, PrecisionComplex c, int lnw)
	{
		sub (a.r, b.r, c.r, lnw);
		sub (a.i, b.i, c.i, lnw);
	}

	private static void zero(PrecisionComplex in)
	{
		PrecisionNumber.zero(in.r);
		PrecisionNumber.zero(in.i);
	}
	
	static void _cdv (PrecisionComplex a, PrecisionComplex b, PrecisionComplex c, int lnw)
	{
		int nw2	= lnw+2;
		PrecisionNumber f		= new PrecisionNumber(6,false);
		PrecisionNumber sk0		= new PrecisionNumber(nw2,false);
		PrecisionNumber sk1		= new PrecisionNumber(nw2,false);
		PrecisionNumber sk2		= new PrecisionNumber(nw2,false);
		PrecisionNumber sk3		= new PrecisionNumber(nw2,false);
		PrecisionNumber sk4		= new PrecisionNumber(nw2,false); 
		PrecisionNumber ar		= a.r;
		PrecisionNumber ai		= a.i;
		PrecisionNumber br		= b.r;
		PrecisionNumber bi		= b.i;

		if (b.r.number_words  == 0 && b.i.number_words == 0)
			throw new ArithmeticException("_cdv: Divisor is zero.");
    
		f.number_words			= 1;
		f.exponent		= 0;
		f.sign			= true;
		f.mantissa[0]	= 1;
		f.mantissa[1]	= 0;

		_mul (ar, br, sk0, lnw);
		_mul (ai, bi, sk1, lnw);
		PrecisionNumber.add (sk0, sk1, sk2, lnw);
		sub (sk0, sk1, sk3, lnw);
		PrecisionNumber.add (ar, ai, sk0, lnw);
		sub (br, bi, sk1, lnw);
		_mul (sk0, sk1, sk4, lnw);
		sub (sk4, sk3, sk1, lnw);
		_sq (br,  sk0, lnw);
		_sq (bi, sk3, lnw);
		PrecisionNumber.add (sk0, sk3, sk4, lnw);
		_div (f, sk4, sk0, lnw);
		mul (sk2, sk0, c.r, lnw);
		mul (sk1, sk0, c.i, lnw);  
	}

	static void _cml (PrecisionComplex a, PrecisionComplex b, PrecisionComplex c, int lnw)
	{
		int nw2				= lnw+2;
		PrecisionNumber sk0		= new PrecisionNumber(nw2,false);
		PrecisionNumber sk1		= new PrecisionNumber(nw2,false);
		PrecisionNumber sk2		= new PrecisionNumber(nw2,false);
		PrecisionNumber sk3		= new PrecisionNumber(nw2,false);

		_mul	(a.r, b.r, sk0, lnw);
		_mul	(a.i, b.i, sk1, lnw);
		sub		(sk0, sk1, c.r, lnw);
		
		PrecisionNumber.add (sk0, sk1, sk2, lnw);
		PrecisionNumber.add (a.r, a.i, sk0, lnw);
		PrecisionNumber.add (b.r, b.i, sk1, lnw);
		
		_mul	(sk0, sk1, sk3, lnw);
		sub		(sk3, sk2, c.i, lnw);
	}

	static void _cpw (PrecisionComplex a, int n, PrecisionComplex b, int lnw)
	{
		int j		= 0;
		double t1	= 0.0;
		int na1		= Math.min (a.r.number_words, lnw);
		int na2		= Math.min (a.i.number_words, lnw);
		int ncr		= (int)(Math.pow(2, pointer));
		
		if (na1 <= ncr && na2 <= ncr) 
		{
			mpcpwr (a, n, b, lnw);
			return;
		}

		if (na1 == 0 && na2 == 0) 
		{
			if (n >= 0) 
			{
				zero(b);
				return;
			}
			else throw new ArithmeticException("_cpw: Argument is zero and N is negative or zero.");
		}
		
		int nn = Math.abs (n);

		if (nn == 0) 
		{
			b.r.number_words	= 1;
			b.r.sign			= true;
			b.r.exponent		= 0;
			b.r.mantissa[0]		= 1;
			b.r.mantissa[1]		= 0;
			
			PrecisionNumber.zero(b.i);
			return;
		}

		PrecisionComplex f		= createComplex(6);
		PrecisionComplex sk0	= createComplex(lnw+3);
		PrecisionComplex sk1	= createComplex(lnw+3);
		PrecisionComplex sk2	= createComplex(lnw+3);   

		eq_complex (a, sk0, lnw); 

		f.r.number_words			= 1;
		f.r.sign		= true;
		f.r.exponent	= 0;
		f.r.mantissa[0]	= 1;
		f.r.mantissa[1]	= 0;
		boolean skip	= false;

		if (nn == 1) 
		{
			eq_complex (sk0, sk2, lnw); 
			skip = true;
		}
		else if (nn == 2) 
		{
			mpcmul (sk0, sk0, sk2, lnw);
			skip = true;
		}

		if (!skip)
		{
			int kn;
			int kk;
			
			t1		= nn;
			int mn	= (int)(CL2 * Math.log (t1) + 1.0 + 5.6843418860808015e-14);
			
			eq_complex (f, sk2, lnw);
			
			kn	= nn;

			for (j = 1; j<=mn; j++)
			{
				kk = kn / 2;
				if (kn != 2 * kk) 
				{
					_cml (sk2, sk0, sk1, lnw);
					eq_complex (sk1, sk2, lnw);
				}
				
				kn = kk;
				
				if (j < mn) 
				{
					_cml	(sk0, sk0, sk1, lnw);
					eq_complex		(sk1, sk0, lnw);
				}
			}
		}

		if (n < 0) 
		{
			eq_complex (f, sk1, lnw);
			_cdv (sk1, sk2, sk0, lnw);
			eq_complex (sk0, sk2, lnw);
		}
		
		eq_complex (sk2, b, lnw);  
	}

	static void sq_complex (PrecisionComplex a, PrecisionComplex b, int lnw)
	{
		int nw2	= lnw+2;
		PrecisionNumber sk0		= new PrecisionNumber(nw2,false);
		PrecisionNumber sk1		= new PrecisionNumber(nw2,false);
		PrecisionNumber sk2		= new PrecisionNumber(nw2,false); 

		if (a.r.number_words == 0 && a.i.number_words == 0) 
		{
			zero(b);
			return;
		}

		_sq (a.r, sk0, lnw);
		_sq (a.i, sk1, lnw);
		PrecisionNumber.add (sk0, sk1, sk2, lnw);
		_sqr (sk2, sk0, lnw);
		eq (a.r, sk1, lnw);
		
		sk1.sign = true;
		
		PrecisionNumber.add (sk0, sk1, sk2, lnw);
		muld (sk2, new NumberChunk(0.50), sk1, lnw);
		_sqr (sk1, sk0, lnw);
		muld (sk0, new NumberChunk(2.0), sk1, lnw);

		if (a.r.sign) 
		{
			eq (sk0, b.r, lnw);
			_div (a.i, sk1, b.i, lnw);
		}
		else 
		{
			_div (a.i, sk1, b.r, lnw);
			
			b.r.sign = true;
			
			eq (sk0, b.i, lnw);
			
			b.i.sign = a.i.sign;
		}
	}

	static void _ang (PrecisionNumber x, PrecisionNumber y, PrecisionNumber pi, PrecisionNumber a, int lnw)
	{
		int ix	= x.sign ? 1 : -1;
		int nx	= Math.min (x.number_words, lnw);
		int iy	= y.sign ? 1: -1;
		int ny	= Math.min (y.number_words, lnw);
		int ncr	= (int)(Math.pow(2, pointer));

		if (lnw <= ncr) 
		{
			PrecisionNumber.mpang (x, y, pi, a, lnw);
			return;
		}

		if (nx == 0 && ny == 0) 
			throw new ArithmeticException("_ang: Both arguments are zero.");

		NumberChunk t1 = new NumberChunk();
		
		PrecisionNumber.mdc (pi, t1);
		
		if (t1.n != 0 || Math.abs (t1.a - CPI) > 3.552713678800501e-15) 
			throw new ArithmeticException("_ang: PI must be precomputed.");

		if (nx == 0)
		{
			if (iy > 0) 	muld (pi, new NumberChunk(0.5), a, lnw); 
			else			muld (pi, new NumberChunk(-0.5), a, lnw); 
			return;
		}
		else if (ny == 0) 
		{
			if (ix > 0) 	PrecisionNumber.zero(a);
			else			eq (pi, a, lnw);
			return;
		}
		
		PrecisionComplex sk0 = createComplex(lnw+2); 
		PrecisionComplex sk1 = createComplex(lnw+2);
		PrecisionComplex sk2 = createComplex(lnw+2);
		PrecisionComplex sk3 = createComplex(lnw+2);

		PrecisionNumber.mdc (x, t1);

		int n2		= 24 * (lnw / 2 + 2) - t1.n;
		NumberChunk dpe1	= new NumberChunk(1.0, n2);

		muld (x, dpe1, sk0.r, lnw);
		muld (y, dpe1, sk0.i, lnw);

		sk1.r.number_words			= 1;
		sk1.r.sign			= true;
		sk1.r.exponent		= 0;
		sk1.r.mantissa[0]	= 1;
		sk1.r.mantissa[1]	= 0;

		PrecisionNumber.zero(sk1.i);

		sk3.r.number_words			= 1;
		sk3.r.sign			= true;
		sk3.r.exponent		= 0;
		sk3.r.mantissa[0]	= 4;
		sk3.r.mantissa[1]	= 0;

		PrecisionNumber.zero(sk3.i);
		_cdv	(sk3, sk0, sk2, lnw);
		_cag	(sk1, sk2, lnw);

		dpe1.a	= 2.0;
		dpe1.n	= 0;

		muld	(sk1.r, dpe1, sk0.r, lnw);
		muld	(sk1.i, dpe1, sk0.i, lnw);
		eq		(pi, sk2.r, lnw);
		PrecisionNumber.zero(sk2.i);
		_cdv	(sk2, sk0, sk1, lnw);
		eq		(sk1.i, a, lnw);  
	}

	static void _cag (PrecisionComplex a, PrecisionComplex b, int lnw)
	{
		int l1					= 0;
		PrecisionComplex sk0	= createComplex(lnw+2);
		PrecisionComplex sk1	= createComplex(lnw+2);  
		int s1					= 0;
		NumberChunk dpe1				= new NumberChunk(0.50,0);
		
		do
		{
			l1++;
			
			if (l1 == 50) 
				throw new ArithmeticException("_cag: Iteration limit exceeded.");
			
			s1 = sk0.r.exponent;
			
			add_complex	(a, b, sk0, lnw);
			muld		(sk0.r, dpe1, sk1.r, lnw);
			muld		(sk0.i, dpe1, sk1.i, lnw);
			_cml		(a, b, sk0, lnw);
			sq_complex	(sk0, b, lnw);
			eq_complex	(sk1, a, lnw);
			sub			(a.r, b.r, sk0.r, lnw);
		}
		while (sk0.r.number_words != 0 && (sk0.r.exponent < s1 || sk0.r.exponent >= -2));
	}

	static void _cos (PrecisionNumber a, PrecisionNumber pi, PrecisionNumber x, PrecisionNumber y, int lnw)
	{
		int k			= 0;
		double t2		= 0.0;
		final int nit	= 1;
		PrecisionNumber f1			= new PrecisionNumber(6,false); 
		int na			= Math.min (a.number_words, lnw);
		int ncr			= (int)(Math.pow(2, pointer));

		if (lnw <= ncr) 
		{
			ssn_complex (a, pi, x, y, lnw);
			return;
		}

		if (na == 0) 
		{
			x.sign			= true;
			x.number_words	= 1;
			x.exponent		= 0;
			x.mantissa[0]	= 1;
			
			PrecisionNumber.zero(y);
			return;
		}

		NumberChunk t1 = new NumberChunk();
		
		PrecisionNumber.mdc (pi, t1);
		
		if (t1.n != 0 || Math.abs (t1.a - CPI) > 3.552713678800501e-15) 
			throw new ArithmeticException("cssx: PI must be precomputed.");
		
		f1.number_words			= 1;
		f1.sign					= true;
		f1.exponent				= 0;
		f1.mantissa[0]			= 1;
		f1.mantissa[1]			= 0;
		int nws					= lnw;
		PrecisionComplex sk0	= createComplex(lnw+2); 
		PrecisionComplex sk1	= createComplex(lnw+2);
		PrecisionComplex sk2	= createComplex(lnw+2);
		PrecisionComplex sk3	= createComplex(lnw+2);

		muld	(pi, new NumberChunk(2.0), sk0.r, lnw);
		_div	(a, sk0.r, sk1.r, lnw);
		PrecisionNumber.nint	(sk1.r, sk2.r, lnw);
		mul		(sk2.r, sk0.r, sk1.r, lnw);
		sub		(a, sk1.r, sk0.r,  lnw);

		t2		= nws;
		int mq	= (int)(CL2 * Math.log (t2) + 1.0 - 5.6843418860808015e-14);

		eq (f1, sk2.r, lnw);

		lnw	= ncr;

		ssn_complex (sk0.r, pi, sk3.r, sk3.i, lnw);

		int iq = 0;

		for (k = pointer + 1; k<=mq; k++)
		{
			lnw			= Math.min(lnw<<1, nws);
			boolean cont	= true;
			
			while (cont)
			{
				_ang		(sk3.r, sk3.i, pi, sk1.r, lnw);
				sub			(sk0.r, sk1.r, sk2.i, lnw);
				_cml		(sk3, sk2, sk1, lnw);
				eq_complex	(sk1, sk3, lnw);
				
				if (k == mq - nit && iq == 0) 	iq		= 1;
				else							cont	= false;
			}
		}

		_sq		(sk3.r, sk0.r, lnw);
		_sq		(sk3.i, sk0.i, lnw);
		
		PrecisionNumber.add (sk0.r, sk0.i, sk1.r, lnw);
		
		_sqr	(sk1.r, sk2.r, lnw);
		_div	(sk3.r, sk2.r, sk0.r, lnw);
		_div	(sk3.i, sk2.r, sk0.i, lnw);
		eq		(sk0.r, x, lnw);
		eq		(sk0.i, y, lnw);    
	}

}
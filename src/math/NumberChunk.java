package math;

final class NumberChunk extends MathGlobal
{

	double a	= 0.0;
	int n		= 0;

	NumberChunk() {}

	NumberChunk(double A)
	{ this(A,0); }
	
	NumberChunk(double A, int N)
	{
		a = A;
		n = N;
	}

	double value() 
	{ return a * Math.pow(2,n); }

	static void dpdec(NumberChunk a, NumberChunk b)
	{
		if (a.a != 0.0) 
		{
			double t1 = 0.3010299956639812 * a.n + log10(Math.abs(a.a));
			b.n = (int)t1; 
			
			if (t1 < 0.0) b.n -= 1;

			b.a = fSign (Math.pow(10.0, (t1 - b.n)), a.a);
		}
		else
		{
			b.a = 0.0;
			b.n = 0;
		}
	}

}
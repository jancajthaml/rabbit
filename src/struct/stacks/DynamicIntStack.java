package struct.stacks;

public class DynamicIntStack
{

	private int[] stack;
	private int size;
	private int position;

	public DynamicIntStack(int size)
	{
		stack		= new int[this.size = size];
		position	= 0;
	}

	public void reset()
	{ position = 0; }

	public int pop()
	{
		assert (position > 0);
		return stack[--position];
	}

	public void push(int i)
	{
		if (position == size)
		{
			int[] old = stack;
			stack = new int[size<<1];
			size = stack.length;
			System.arraycopy(old, 0, stack, 0, old.length);
		}
		stack[position++] = i;
	}

	public int getCount()
	{ return position; }
  
}
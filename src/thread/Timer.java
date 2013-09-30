package thread;

import java.util.concurrent.Future;

import lang.Destructable;
import util.storage.PrefferenceStorage;
import static thread.ThreadBase.executor;

/**
 * Interpolation Timer implementation
 * 
 * @author Jan Cajthaml
 *
 */
public abstract class Timer implements Destructable
{
	
	// >-------[abstract]---------------------------------------------------------------------------------------< //

	/**
	 * 
	 * @param alfa percent value
	 */
	public abstract void interpolation(float alfa);

	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private Future<?> publisher			= null;
	private volatile boolean kill		= false;
	private volatile boolean suspend	= false;
    private final Object lock			= new Object();
	private Direction direction			= Direction.FORWARD;
	private int fps						= 30;
	private int time 					= 1000;
	private boolean loops				= true;
	private boolean toogle				= false;
	private float seed					= 1.0f/time;
	private float percent				= 0;
	private boolean linear				= true;
	private double increase				= Math.PI  / 100;
	private double counter				= 0;
	private PausableTimer Timer			= new PausableTimer()
	{
		@Override public void run()
		{
			try
			{
				while(!kill)
				{
					 while (!suspend)
					 {
						 switch(direction)
							{
								case FORWARD :
								{
										if(percent!=1.0f)
										{
											if(PrefferenceStorage.getBool("mode.ui.animation"))
											percent += seed;
											else percent=1.0f;
										}
										else
										{
											if(loops)
											{
												if(toogle)
												{
													direction=Direction.BACKWARD;
													counter=0; 
												}
												else reset(); 
											}
											else pause();
										}
								}
								break;
								case BACKWARD :
								{
									
										if(percent!=0)
										{
											if(PrefferenceStorage.getBool("mode.ui.animation"))
												percent -= seed;
											else percent=0.0f;
										}
										else
										{
											if(loops)
											{
												if(toogle)
												{
													direction = Direction.FORWARD;
													counter	 = 0; 
												}
												else reset(); 
											}
											else pause();
										}
								} break;
							}
							
							float animationFactor;
							
						    if (linear || !PrefferenceStorage.getBool("mode.ui.animation"))
						    { animationFactor = percent; }
						    else
						    {
						    	switch(direction)
						    	{
						    		case FORWARD	: animationFactor = 1.0f-(float) (Math.cos(counter)/ 2 + 0.5);break;
						    		default			: animationFactor = (float) (Math.cos(counter)/ 2 + 0.5);break;
						    	}
						        counter+=increase;
						    }
						    animationFactor = Math.min(animationFactor, 1.0f);
						    animationFactor = Math.max(animationFactor, 0.0f);
						    percent=animationFactor;

						    interpolation(percent);
							try { Thread.sleep(fps); }
							catch (InterruptedException e) { }
							
							if(kill) throw new ConsumentException();
		                }
		                synchronized (lock){
		                    try {
		                        lock.wait();
		                    } catch (InterruptedException e) {
		                        Thread.currentThread().interrupt();
		                        return;
		                    }
		                }
				}
			}
			catch (ConsumentException e)	{ /*e.printStackTrace();*/	}
			
		}
	};
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new Timer
	 */
	public Timer()
	{
		Timer.start();
		pause();
	}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Set interpolation toogle
	 * 
	 * @param b true for toogle
	 */
	public void setToogle(boolean b)
	{ toogle = b; }
	
	/**
	 * Set interpolation Loop
	 * 
	 * @param b true for loop
	 */
	public void setLoop(boolean b)
	{ loops = b; }
	
	/**
	 * Set interpolation time
	 * 
	 * @param time value in ms
	 */
	public void setDuration(int time)
	{
		this.time=time;
		setLinear(linear);
	}
	
	/**
	 * Rollback interpolation
	 */
	public void reset()
	{ counter = percent = 0; }
	
	/**
	 * Check whenever interpolation will loop e.g. 0-1-0- ... -1-0-1
	 * @return true when interpolation loops
	 */
	public boolean loop()
	{ return loops; }
	
	/**
	 * Set interpolation type true=linear, false=sin
	 * 
	 * @param b true for linear false for sin
	 */
	//FIXME use enum in future
	public void setLinear(boolean b)
	{
		float t	= time/fps;
		linear	= b;
		seed	= (1.0f/t);
		if(!linear) increase=((Math.PI/(t)));
	}
	
	/**
	 * Set interpolation direction
	 * 
	 * @param dir FORWARD | BACKWARD
	 */
	public void setDirection(Direction dir)
	{ this.direction = dir; }
	
	/**
	 * Set delay between interpolation steps
	 * 
	 * @param fps delay value
	 */
	public void setInterpolationDelay(int fps)
	{
		this.fps = fps;
		setLinear(linear);
	}
	
	/**
	 * Pause timer for given time
	 * 
	 * @param time time to sleep
	 */
	public void sleep(int time)
	{
		try								{ Thread.sleep(time);		}
		catch (InterruptedException e)	{ /* Ignore Exception */	}
	}

	/**
	 * kill this Timer
	 */
	public void kill()
	{ $(); }
	
	/**
	 * Check whenever Timer is Alive
	 * 
	 * @return is alive
	 */
	public boolean isAlive()
	{ return !kill; }
	
	/**
	 * description for destructor work
	 */
	public boolean $()
	{
		kill = true;

		terminate();
		publisher	= null;
		Timer		= null;
		
		try					{ super.finalize();		}
		catch (Throwable e)	{ e.printStackTrace();	}
		System.gc();
		return true;
	}

	/**
	 * Starts Timer
	 */
	public void start()
	{
		reset();
		resume();
	}
	
	/**
	 * Stops Timer
	 */
	public void stop()
	{
		reset();
		suspend = true;
        synchronized (lock)
        {
            lock.notifyAll();
        }
	}

	/**
	 * Pauses Timer
	 */
	public void pause()
	{
		paused();
		suspend = true;
	}

	/**
	 * Resumes Timer
	 */
    public void resume()
    {
    	suspend = false;
    	synchronized (lock)
    	{ lock.notifyAll(); }
    }
    
    /**
     * Safely kills Timer
     */
	private void terminate()
	{
		try								{ publisher.cancel(true);	}
		catch(NullPointerException e)	{							}
	}
	
	public void paused()
	{
		
	}
	
	// >-------[nested]---------------------------------------------------------------------------------------< //

	/**
	 * Represents Runnable that doesn't exist when killed (exist only while is Alive)
	 * 
	 * @author Jan Cajthaml
	 *
	 */
	private abstract class PausableTimer implements Runnable
	{
		public void start()
		{ publisher	= executor.submit(this); }		
	}

	/**
	 * Represents interpolation direction
	 * 
	 * @author Jan Cajthaml
	 *
	 */
	public static enum Direction
	{
		FORWARD,
		BACKWARD;
	}
	
	

}
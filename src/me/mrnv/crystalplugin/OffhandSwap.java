package me.mrnv.crystalplugin;

public class OffhandSwap
{
	private String name;
	private long starttime;
	private int count;
	
	public OffhandSwap( String name )
	{
		this.name = name;
		this.starttime = System.currentTimeMillis( );
		this.count = 0;
	}
	
	public String getName( )
	{
		return name;
	}
	
	public long getStartTime( )
	{
		return starttime;
	}
	
	public int getCount( )
	{
		return count;
	}
	
	public void setCount( int count )
	{
		this.count = count;
	}
}

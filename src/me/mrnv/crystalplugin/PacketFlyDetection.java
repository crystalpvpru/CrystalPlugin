package me.mrnv.crystalplugin;

import org.bukkit.Location;

public class PacketFlyDetection
{
	private String player;
	private int detections;
	private long lastdetectiontime;
	private Location lastpos;
	
	public PacketFlyDetection( String player, Location lastpos )
	{
		this.player = player;
		this.detections = 0;
		this.lastdetectiontime = 0;
		this.lastpos = lastpos;
	}
	
	public String getPlayer( )
	{
		return player;
	}
	
	public int getDetections( )
	{
		return detections;
	}
	
	public void setDetections( int detections )
	{
		this.lastdetectiontime = System.currentTimeMillis( );
		this.detections = detections;
	}
	
	public long getLastDetectionTime( )
	{
		return lastdetectiontime;
	}
	
	public Location getLastPosition( )
	{
		return lastpos;
	}
	
	public void setLastPosition( Location lastpos )
	{
		this.lastpos = lastpos;
	}
}

package me.mrnv.crystalplugin;

import org.bukkit.Location;

public class PlayerDuelsData
{
	private String player;
	private Location location;
	
	public PlayerDuelsData( String player, Location location )
	{
		this.player = player;
		this.location = location;
	}
	
	public String getPlayer( )
	{
		return player;
	}
	
	public Location getLocation( )
	{
		return location;
	}
	
	public void setLocation( Location location )
	{
		this.location = location;
	}
}

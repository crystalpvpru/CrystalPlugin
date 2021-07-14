package me.mrnv.crystalplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.mrnv.crystalplugin.Main;

public class AntiVoid implements Listener
{
	private Main plugin;
	
	public AntiVoid( Main plugin )
	{
		this.plugin = plugin;
	}
	
	public void addTask( )
	{
		// красава мне похуй
		/*Bukkit.getScheduler( ).scheduleSyncRepeatingTask( plugin,
		new Runnable( )
		{
			public void run( )
			{
				for( Player player : Bukkit.getOnlinePlayers( ) )
				{
					World world = player.getWorld( );
					if( world == null ||
						world.getEnvironment( ) == Environment.THE_END )
						continue;
					
					Location pos = player.getLocation( );
					if( pos.getY( ) < 0 )
					{
						Location newpos = new Location( world, pos.getX( ), 0.0D, pos.getZ( ) );
						player.teleport( newpos );
					}
				}
			}
		}, 20, 20 );*/
	}
}

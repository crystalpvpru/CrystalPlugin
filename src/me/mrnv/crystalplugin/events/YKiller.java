package me.mrnv.crystalplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import me.mrnv.crystalplugin.Main;

public class YKiller
{
	public void addTask( Main plugin )
	{
		Bukkit.getScheduler( ).scheduleSyncRepeatingTask( plugin,
		new Runnable( )
		{
			public void run( )
			{
				for( Player player : Bukkit.getOnlinePlayers( ) )
				{
					if( player.getGameMode( ) == GameMode.SURVIVAL )
					{
						Location location = player.getLocation( );
						
						if( location.getY( ) < 0 || 
							( player.getWorld( ).getEnvironment( ) == Environment.NORMAL && location.getY( ) >= 255 ) )
						{
							if( player.getHealth( ) > 0 && !player.isDead( ) )
								player.setHealth( 0 );
						}
					}
				}
			}
		}, 10, 10 );
	}
}

package me.mrnv.crystalplugin.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.mrnv.crystalplugin.Main;
import me.realized.duels.api.Duels;

public class AntiDuelsVoid
{
	private Main plugin;
	private Duels duels;
	private Map< String, Location > data = new HashMap< >( );
	
	public AntiDuelsVoid( Main plugin, Duels duels )
	{
		this.plugin = plugin;
		this.duels = duels;
		this.data = new HashMap< >( );
	}
	
	public void addTask( )
	{
		Bukkit.getScheduler( ).scheduleSyncRepeatingTask( plugin,
		new Runnable( )
		{
			public void run( )
			{
				for( Player player : Bukkit.getOnlinePlayers( ) )
				{
					if( duels.getArenaManager( ).isInMatch( player ) &&
						!player.isDead( ) )
					{
						Location pos = player.getLocation( );
						
						if( pos.getBlock( ).getType( ) == Material.AIR &&
							pos.add( 0, 1, 0 ).getBlock( ).getType( ) == Material.AIR )
						{
							if( pos.getY( ) < 1.0 )
							{
								if( data.containsKey( player.getName( ) ) )
								{
									Bukkit.getLogger( ).info( "[CrystalPlugin] teleporting " + player.getName( ) );
									Location oldloc = data.get( player.getName( ) );
									player.teleport( oldloc );
								}
								else
								{
									Bukkit.getLogger( ).info( "[CrystalPlugin] [ERROR] " + player.getName( ) +
											" has no data in AntiDuelsVoid" );
								}
							}
							else
							{
								//Bukkit.getLogger( ).info( "[CrystalPlugin] updated " + player.getName( ) + " pos to " + pos.getX( ) + " " + pos.getY( ) + " " + pos.getZ( ) );
								
								data.put( player.getName( ), pos );
							}
						}
					}
					else
					{
						if( data.containsKey( player.getName( ) ) )
							data.remove( player.getName( ) );
					}
				}
			}
		}, 20, 20 );
	}
}

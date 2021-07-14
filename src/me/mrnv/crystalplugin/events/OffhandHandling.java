package me.mrnv.crystalplugin.events;

import me.mrnv.crystalplugin.Main;
import me.mrnv.crystalplugin.OffhandSwap;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class OffhandHandling implements Listener
{
	private Main plugin;
	private List< OffhandSwap > swaps = new ArrayList< >( );
	
	public OffhandHandling( Main plugin )
	{
		this.plugin = plugin;
		this.swaps = new ArrayList< >( );
	}
	
	public void addTask( )
	{
		Bukkit.getScheduler( ).scheduleSyncRepeatingTask( plugin,
		new Runnable( )
		{
			public void run( )
			{
				if( swaps != null )
				{
					try
					{
						for( OffhandSwap swap : swaps )
						{
							try
							{
								if( swap != null &&
									System.currentTimeMillis( ) > ( swap.getStartTime( ) + 1000 ) )
									swaps.remove( swap );
							}
							catch( Exception e )
							{
								
							}
						}
					}
					catch( ConcurrentModificationException e2 )
					{
						// pizdec prikol but i dont really care
					}
				}
			}
		}, 5, 5 );
	}
	
	@EventHandler
	public void onSwapHandsEvent( PlayerSwapHandItemsEvent event )
	{
		String playername = event.getPlayer( ).getName( );
		
		OffhandSwap data = getSwapData( playername );
		if( data == null ) return; // how
		
		data.setCount( data.getCount( ) + 1 );
		if( data.getCount( ) >= 20 )
		{
			Bukkit.getLogger( ).info( "[CrystalPlugin] Kicking " + playername + " [offhand crash attempt]" );
			event.getPlayer( ).kickPlayer( "Disconnected" );
		}
	}
	
	public OffhandSwap getSwapData( String name )
	{
		for( OffhandSwap swap : swaps )
		{
			if( swap.getName( ).equalsIgnoreCase( name ) )
				return swap;
		}
		
		OffhandSwap swap = new OffhandSwap( name );
		swaps.add( swap );
		return swap;
	}
}

package me.mrnv.crystalplugin.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.mrnv.crystalplugin.Main;
import me.realized.duels.api.Duels;
import me.realized.duels.api.arena.Arena;

public class OffhandCrystalHandler implements Listener
{
	private Main plugin;
	private Duels duels;
	private Map< String, Long > data = new HashMap< >( );
	
	public OffhandCrystalHandler( Main plugin, Duels duels )
	{
		this.plugin = plugin;
		this.duels = duels;
		this.data = new HashMap< >( );
	}
	
	@EventHandler( priority = EventPriority.MONITOR )
	public void onPlayerInteract( final PlayerInteractEvent event )
	{
		Player player = event.getPlayer( );
		if( player.getWorld( ).getEnvironment( ) == Environment.NORMAL ) return;
		
		try
		{
			if( duels.getArenaManager( ).isInMatch( player ) )
			{
				Arena arena = duels.getArenaManager( ).get( player );

				if( !arena.getMatch( ).isOffhandpatched( ) )
					return;
			}
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}

		if( event.getHand( ) == EquipmentSlot.OFF_HAND &&
			event.getAction( ) == Action.RIGHT_CLICK_BLOCK &&
			event.getMaterial( ) == Material.END_CRYSTAL )
		{
			if( data.containsKey( player.getName( ) ) )
				event.setCancelled( true );
			else
				data.put( player.getName( ), System.currentTimeMillis( ) );
		}
	}
	
	/*@EventHandler
    public void onPlayerItemConsume( PlayerItemConsumeEvent event )
    {
		Player player = event.getPlayer( );
		if( player.getWorld( ).getEnvironment( ) == Environment.NETHER ) return;
		
		if( data.containsKey( player.getName( ) ) )
			event.setCancelled( true );
    }*/
	
	public void addTask( )
	{
		Bukkit.getScheduler( ).scheduleSyncRepeatingTask( plugin,
		new Runnable( )
		{
			public void run( )
			{
				for( String name : data.keySet( ) )
				{
					long time = data.get( name );
					if( System.currentTimeMillis( ) > ( time + 2000 ) )
						data.remove( name );
				}
			}
		}, 5, 5 );
	}
}

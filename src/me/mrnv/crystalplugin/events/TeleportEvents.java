package me.mrnv.crystalplugin.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.mrnv.crystalplugin.Main;
import net.minecraft.server.v1_12_R1.Blocks;

public class TeleportEvents implements Listener
{
	private Main plugin;
	private Random random = new Random( );
	private Map< String, Boolean > portalstates = new HashMap< >( );
	
	public TeleportEvents( Main plugin )
	{
		this.plugin = plugin;
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
					World world = player.getWorld( );
					if( world.getEnvironment( ) != Environment.NORMAL ) continue;
					
					if( world.getBlockAt( player.getLocation( ) ).getType( ) == Material.PORTAL )
					{
						if( !portalstates.containsKey( player.getName( ) ) ||
							!portalstates.get( player.getName( ) ) )
							portalstates.put( player.getName( ), true );
						else
						{
							portalstates.remove( player.getName( ) );
							
							// teleport player to the nether
							
							World nether = Bukkit.getWorlds( ).get( 1 );
							
							int minX = -100;
							int minZ = -100;
							int maxX = 100;
							int maxZ = 100;
							
							Location safelocation = null;
							while( safelocation == null )
							{
								double x = 0.5D + ( double )( random.nextInt( maxX - minX ) + minX );
								double z = 0.5D + ( double )( random.nextInt( maxZ - minZ ) + minZ );
								
								safelocation = new Location( nether, x, 99.0D, z );
								if( !safelocation.getChunk( ).isLoaded( ) )
									safelocation.getChunk( ).load( );
								
								Block block = nether.getBlockAt( safelocation );
								if( block.getType( ) == Material.AIR )
									safelocation = null;
							}
							
							safelocation.setY( safelocation.getY( ) + 1.5 );
							player.teleport( safelocation );
						}
					}
					else
					{
						if( portalstates.containsKey( player.getName( ) ) )
							portalstates.remove( player.getName( ) );
					}
				}
			}
		}, 3, 3 );
	}
	
	@EventHandler( priority = EventPriority.MONITOR )
	public void onPlayerPortal( PlayerPortalEvent event )
	{
		if( event.getCause( ) == TeleportCause.END_GATEWAY )
			event.setCancelled( true );
		else if( event.getCause( ) == TeleportCause.END_PORTAL )
		{
			World theend = Bukkit.getWorlds( ).get( 2 );
			
			int minX = -113;
			int minZ = -141;
			int maxX = 152;
			int maxZ = 115;
			
			Location safelocation = null;
			while( safelocation == null )
			{
				double x = 0.5D + ( double )( random.nextInt( maxX - minX ) + minX );
				double z = 0.5D + ( double )( random.nextInt( maxZ - minZ ) + minZ );
				
				safelocation = new Location( theend, x, 255.0D, z );
				if( !safelocation.getChunk( ).isLoaded( ) )
					safelocation.getChunk( ).load( );
				
				Block block = theend.getBlockAt( safelocation );
				if( block.getType( ) == Material.AIR )
					safelocation = null;
			}
			
			safelocation.setY( safelocation.getY( ) + 1 );

			event.setCancelled( true );
			event.getPlayer( ).teleport( safelocation );
		}
	}
}

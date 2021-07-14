package me.mrnv.crystalplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.realized.duels.api.Duels;
import me.realized.duels.api.arena.Arena;

public class BlockPlace implements Listener
{
	private Duels duels;
	private Location spawnpos1;
	private Location spawnpos2;
	
	public BlockPlace( )
	{
		duels = ( Duels )Bukkit.getPluginManager( ).getPlugin( "Duels" );
		spawnpos1 = new Location( Bukkit.getWorlds( ).get( 0 ), -200, 0, -200 );
		spawnpos2 = new Location( Bukkit.getWorlds( ).get( 0 ), 200, 255, 200 );
	}
	
	private boolean isLocationInArena( Location loc )
	{
		for( Arena arena : duels.getArenaManager( ).getArenas( ) )
		{
			Location pos1 = arena.getPosition( 1 );
			Location pos2 = arena.getPosition( 2 );
			
			double minx = Math.min( pos1.getX( ), pos2.getX( ) );
			double maxx = Math.max( pos1.getX( ), pos2.getX( ) );
			double minz = Math.min( pos1.getZ( ), pos2.getZ( ) );
			double maxz = Math.max( pos1.getZ( ), pos2.getZ( ) );
			
			boolean check1x =
					loc.getX( ) >= minx && loc.getX( ) <= maxx;
					
			boolean check1z =
					loc.getZ( ) >= minz && loc.getZ( ) <= maxz;
					
			boolean check2x =
					loc.getX( ) >= minx && loc.getX( ) <= maxx;
					
			boolean check2z =
					loc.getZ( ) >= minz && loc.getZ( ) <= maxz;
					
			boolean check1 = ( check1x && check1z );
			boolean check2 = ( check2x && check2z );
			
			if( check1 || check2 )
				return true;
		}
		
		return false;
	}
	
	private boolean isBlockInSpawn( Location loc )
	{
		double minx = Math.min( spawnpos1.getX( ), spawnpos2.getX( ) );
		double maxx = Math.max( spawnpos1.getX( ), spawnpos2.getX( ) );
		double minz = Math.min( spawnpos1.getZ( ), spawnpos2.getZ( ) );
		double maxz = Math.max( spawnpos1.getZ( ), spawnpos2.getZ( ) );
		
		boolean check1x =
				loc.getX( ) >= minx && loc.getX( ) <= maxx;
				
		boolean check1z =
				loc.getZ( ) >= minz && loc.getZ( ) <= maxz;
				
		boolean check2x =
				loc.getX( ) >= minx && loc.getX( ) <= maxx;
				
		boolean check2z =
				loc.getZ( ) >= minz && loc.getZ( ) <= maxz;
				
		boolean check1 = ( check1x && check1z );
		boolean check2 = ( check2x && check2z );
		
		if( check1 || check2 )
			return true;
		
		return false;
	}
	
	@EventHandler
	public void onBlockPlace( BlockPlaceEvent event )
	{
		Block block = event.getBlockPlaced( );
		Player player = event.getPlayer( );
		Location pos = player.getLocation( );
		
		if( player.isOp( ) && player.getGameMode( ) == GameMode.CREATIVE ) return;
		
		if( duels.getArenaManager( ).isInMatch( player ) )
		{
			if( !isLocationInArena( block.getLocation( ) ) )
			{
				Bukkit.getLogger( ).info( "[CrystalPlugin] " + player.getName( ) +
						" tried to place a block outside of duel arena" );
				
				event.setCancelled( true );
			}
		}
		else
		{
			if( player.getWorld( ).getEnvironment( ) == Environment.NORMAL )
			{
				if( !isBlockInSpawn( block.getLocation( ) ) )
				{
					Bukkit.getLogger( ).info( "[CrystalPlugin] " + player.getName( ) +
							" tried to place a block outside of main arena" );
					
					event.setCancelled( true );
				}
			}
		}
	}
}

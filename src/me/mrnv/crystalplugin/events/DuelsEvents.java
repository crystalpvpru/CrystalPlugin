package me.mrnv.crystalplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.mrnv.crystalplugin.Main;
import me.realized.duels.api.Duels;
import me.realized.duels.api.arena.Arena;
import me.realized.duels.api.event.match.MatchStartEvent;

public class DuelsEvents implements Listener
{
	public Main plugin;
	public Duels duels;
	
	public DuelsEvents( Main plugin, Duels duels )
	{
		this.plugin = plugin;
		this.duels = duels;
	}
	
	@EventHandler
	public void onMatchStart( MatchStartEvent event )
	{
		try
		{
			Arena arena = event.getMatch( ).getArena( );
			
			Location pos1 = arena.getPosition( 1 );
			Location pos2 = arena.getPosition( 2 );
			World world = pos1.getWorld( );
			
			plugin.getUtils( ).cleanLocation( pos1, pos2, world, 255 );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
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
	
	@EventHandler
	public void onPlayerTeleport( PlayerTeleportEvent event )
	{
		final Player player = event.getPlayer( );
		
		Location from = event.getFrom( );
		Location to = event.getTo( );
		
		boolean checkfrom = isLocationInArena( from );
		boolean checkto = isLocationInArena( to );
		
		if( event.getCause( ) == TeleportCause.CHORUS_FRUIT ||
			event.getCause( ) == TeleportCause.UNKNOWN )
		{
			if( checkfrom != checkto )
			{
				Bukkit.getLogger( ).info( "[CrystalPlugin] " + player.getName( ) + " tried to teleport in or out of an arena" );
				event.setCancelled( true );
			}
		}
		
		/*if( event.getCause( ) != TeleportCause.CHORUS_FRUIT &&
			event.getCause( ) != TeleportCause.UNKNOWN )
			return;*/

		/*if( isLocationInArena( from ) != isLocationInArena( to ) )
		{
			Bukkit.getLogger( ).info( "[CrystalPlugin] " + player.getName( ) + " tried to teleport in or out of an arena" );
			event.setCancelled( true );
		}*/
		//if( duels.getArenaManager( ).ge )
	}
	
	@EventHandler
	public void onPlayerMove( PlayerMoveEvent event )
	{
		if( !duels.getArenaManager( ).isInMatch( event.getPlayer( ) ) ) return;
		
		if( event.getTo( ).getY( ) < 1.0 )
		{
			
		}
	}
}

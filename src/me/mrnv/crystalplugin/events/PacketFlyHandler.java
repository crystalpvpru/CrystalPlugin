package me.mrnv.crystalplugin.events;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

import me.mrnv.crystalplugin.Main;
import me.mrnv.crystalplugin.PacketFlyDetection;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import net.minecraft.server.v1_12_R1.PacketPlayOutPosition;

public class PacketFlyHandler implements Listener
{
	private Main plugin;
	private List< PacketFlyDetection > data = new ArrayList< >( );
	
	public PacketFlyHandler( Main plugin )
	{
		this.plugin = plugin;
		this.data = new ArrayList< >( );
	}
	
	public void update( Player player, Location pos, PacketEvent event )
	{
		if( player.getTicksLived( ) <= 20 ) return;
		if( player != null || player == null ) return;
		
		PacketFlyDetection playerdata = getData( player );
		if( playerdata == null ) return;
		
		int ydiff = Math.abs( pos.getBlockY( ) - player.getLocation( ).getBlockY( ) );
		//int ydiff2 = Math.abs( pos.getBlockY( ) - playerdata.getLastPosition( ).getBlockY( ) );
		
		//Bukkit.getLogger( ).info( "[CrystalPlugin] [PRE-DEBUG] ydiff: " + ydiff + " | ydiff2: " + ydiff2 );
		
		//if( pos.getY( ) < 1 || pos.getY( ) > 250 )//if( ydiff > 25 ) // non-experimental - put 256 here
		if( ydiff > 25 )
		{
			// experimental
			int absx = Math.abs( pos.getBlockX( ) );
			int absz = Math.abs( pos.getBlockZ( ) );
			int xdiff = Math.abs( pos.getBlockX( ) - player.getLocation( ).getBlockX( ) );
			int zdiff = Math.abs( pos.getBlockZ( ) - player.getLocation( ).getBlockZ( ) );
			//int xdiff = Math.abs( pos.getBlockX( ) );
			//int zdiff = Math.abs( pos.getBlockZ( ) );
			
			//Bukkit.getLogger( ).info("[CrystalPlugin] [DEBUG] absx: " + absx + " | absz: " + absz + " | xdiff: " + xdiff + " | zdiff: " + zdiff );
			
			boolean experimental =
					( absx > 10 || absz > 10 ) &&
					( xdiff < 5 && zdiff < 5 );
			/*boolean experimental = ( xdiff > 10 && zdiff > 10 &&
					Math.abs( pos.getBlockX( ) ) < 5.f &&
					Math.abs( pos.getBlockZ( ) ) < 5.f );*/
			
			if( !experimental ) return;
			
			//event.setCancelled( true );
			
			playerdata.setDetections( playerdata.getDetections( ) + 1 );
			Bukkit.getLogger( ).info( "[CrystalPlugin] " + event.getPlayer( ).getName( ) +
					" is using PacketFly [detections: " + playerdata.getDetections( ) +
					"] [fromY: " + playerdata.getLastPosition( ).getBlockY( ) + "] [toY: " + pos.getBlockY( ) +
					/*"] [difference: " + ydiff +*/ "] [experimental check: " + experimental + "]" );
			
			if( playerdata.getDetections( ) >= 10 )
			{
				Bukkit.getLogger( ).info( "[CrystalPlugin] Kicking " + event.getPlayer( ).getName( ) +
						" [PacketFly detection]" );
				
				data.remove( playerdata );
				
				Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin,
				new Runnable( )
				{
					public void run( )
					{
						event.getPlayer( ).kickPlayer( ChatColor.YELLOW + "Disable PacketFly" );
					}
				} );
			}
		}
		else
			playerdata.setLastPosition( pos );
	}
	
	public void addTask( )
	{
		ProtocolLibrary.getProtocolManager( ).addPacketListener(
		new PacketAdapter( plugin, ListenerPriority.NORMAL,
		PacketType.Play.Client.POSITION )
		{
			@Override
			public void onPacketReceiving( PacketEvent event )
			{
				PacketPlayInFlying.PacketPlayInPosition packet =
						( PacketPlayInFlying.PacketPlayInPosition )event.getPacket( ).getHandle( );
				
				StructureModifier< Double > doubles = event.getPacket( ).getDoubles( );
				
				double x = doubles.readSafely( 0 );
				double y = doubles.readSafely( 1 );
				double z = doubles.readSafely( 2 );
				
				//String print = String.format( "pos [x:%f y:%f z:%f]", x, y, z );
				
				Player player = event.getPlayer( );
				
				Location loc = new Location( player.getWorld( ),
						x, y, z );
				
				if( player.isDead( ) || player.getHealth( ) <= 0 )
					removeData( player );
				else
					update( event.getPlayer( ), loc, event );
				
				//Bukkit.broadcastMessage( print );
				//Bukkit.broadcastMessage( event.getPacket( ).getHandle( ).getClass( ).toString( ) );
			}
		} );
		
		ProtocolLibrary.getProtocolManager( ).addPacketListener(
		new PacketAdapter( plugin, ListenerPriority.NORMAL,
		PacketType.Play.Client.POSITION_LOOK )
		{
			@Override
			public void onPacketReceiving( PacketEvent event )
			{
				PacketPlayInFlying.PacketPlayInPositionLook packet =
						( PacketPlayInFlying.PacketPlayInPositionLook )event.getPacket( ).getHandle( );
				
				StructureModifier< Double > doubles = event.getPacket( ).getDoubles( );
				
				double x = doubles.readSafely( 0 );
				double y = doubles.readSafely( 1 );
				double z = doubles.readSafely( 2 );
				
				//String print = String.format( "pos [x:%f y:%f z:%f]", x, y, z );
				
				Player player = event.getPlayer( );
				
				Location loc = new Location( player.getWorld( ),
						x, y, z );
				
				if( player.isDead( ) || player.getHealth( ) <= 0 )
					removeData( player );
				else
					update( event.getPlayer( ), loc, event );
				
				//Bukkit.broadcastMessage( print );
				//Bukkit.broadcastMessage( event.getPacket( ).getHandle( ).getClass( ).toString( ) );
			}
		} );

		Bukkit.getScheduler( ).scheduleSyncRepeatingTask( plugin,
		new Runnable( )
		{
			public void run( )
			{
				try
				{
					for( PacketFlyDetection detection : data )
					{
						try
						{
							if( detection.getLastDetectionTime( ) != 0 &&
								System.currentTimeMillis( ) > ( detection.getLastDetectionTime( ) + 10000 ) )
								data.remove( detection );
						}
						catch( Exception e )
						{
							
						}
					}
				}
				catch( ConcurrentModificationException e2 )
				{
					
				}
			}
		}, 10, 10 );
	}
	
	public PacketFlyDetection getData( Player player )
	{
		for( PacketFlyDetection detection : data )
		{
			if( detection.getPlayer( ).equalsIgnoreCase( player.getName( ) ) )
				return detection;
		}
		
		PacketFlyDetection ret = new PacketFlyDetection( player.getName( ), player.getLocation( ) );
		data.add( ret );
		return ret;
	}
	
	public void removeData( Player player )
	{
		for( PacketFlyDetection detection : data )
		{
			if( detection.getPlayer( ).equalsIgnoreCase( player.getName( ) ) )
			{
				data.remove( detection );
				break;
			}
		}
	}
}

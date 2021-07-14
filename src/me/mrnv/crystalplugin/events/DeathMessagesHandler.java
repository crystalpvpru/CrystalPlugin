package me.mrnv.crystalplugin.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import email.com.gmail.cosmoconsole.bukkit.deathmsg.DeathMessageBroadcastEvent;
import email.com.gmail.cosmoconsole.bukkit.deathmsg.DeathMessagePreparedEvent;
import email.com.gmail.cosmoconsole.bukkit.deathmsg.DeathMessagesPrime;
import email.com.gmail.cosmoconsole.bukkit.deathmsg.DeathPreDMPEvent;
import me.mrnv.crystalplugin.Main;

public class DeathMessagesHandler implements Listener
{
	private DeathMessagesPrime dmpplugin;
	private Main plugin;
	
	public DeathMessagesHandler( Main plugin )
	{
		this.plugin = plugin;
	}
	
	public void init( )
	{
		dmpplugin = ( DeathMessagesPrime )Bukkit.getPluginManager( ).getPlugin( "DeathMessagesPrime" );
	}
	
	@EventHandler
	public void onDeathPreDMPEvent( DeathPreDMPEvent event )
	{
		Player victim = event.getPlayer( );
		
		//Bukkit.broadcastMessage( "death cause > " + event.getCause( ).toString( ) );
		
		if( event.getDamager( ) == null ||
			event.getCause( ) == DamageCause.VOID )
		{
			String msg = plugin.getDeathMessages( ).getRandomUnknown( victim.getName( ) );
			if( msg != null )
				Bukkit.broadcastMessage( msg );
		}
		else
		{
			if( event.getDamager( ) instanceof EnderCrystal )
			{
				if( event.getDamager( ).hasMetadata( "dmp.enderCrystalPlacer" ) )
				{
					UUID uuid = UUID.fromString( event.getDamager( ).getMetadata( "dmp.enderCrystalPlacer" ).get( 0 ).asString( ) );
					String name = plugin.getUtils( ).getNameByUUID( uuid );
					if( name != null )
					{
						if( name.equals( victim.getName( ) ) )
						{
							String msg = plugin.getDeathMessages( ).getRandomCrystalSuicide(
									victim.getName( ), ( EnderCrystal )event.getDamager( ) );
							
							if( msg != null )
								Bukkit.broadcastMessage( msg );
						}
						else
						{
							String msg = plugin.getDeathMessages( ).getRandomCrystal(
									victim.getName( ), ( EnderCrystal )event.getDamager( ), name );
							
							if( msg != null )
								Bukkit.broadcastMessage( msg );
						}
					}
				}
				else
				{
					Bukkit.getLogger( ).info( "[CrystalPlugin] " + victim.getName( ) +
							" died because of an end crystal explosion, but dmp.enderCrystalPlacer is unknown" );
					
					String msg = plugin.getDeathMessages( ).getRandomUnknown( victim.getName( ) );
					if( msg != null )
						Bukkit.broadcastMessage( msg );
				}
				
				return;
			}
			
			if( !( event.getDamager( ) instanceof Player ) ) return;
			
			Player killer = ( Player )event.getDamager( );
			if( killer == null ) return; // how
			
			String itemname = null;
			
			if( killer.getItemInHand( ) != null &&
				killer.getItemInHand( ).hasItemMeta( ) &&
				killer.getItemInHand( ).getItemMeta( ).getDisplayName( ) != null )
				itemname = killer.getItemInHand( ).getItemMeta( ).getDisplayName( );
			
			if( itemname != null )
			{
				String msg = plugin.getDeathMessages( ).getRandomWeapon(
						victim.getName( ), killer.getName( ), itemname );
				
				if( msg != null )
					Bukkit.broadcastMessage( msg );
			}
			else
			{
				String msg = plugin.getDeathMessages( ).getRandomNormal(
						victim.getName( ), killer.getName( ) );
				
				if( msg != null )
					Bukkit.broadcastMessage( msg );
			}
		}
	}
	
	@EventHandler
	public void onDeathMessageBroadcastEvent( DeathMessageBroadcastEvent event )
	{
		//Bukkit.broadcastMessage( "cancelled DeathMessageBroadcastEvent [victim: " + event.getPlayer( ).getName( ) + "]" );
		event.setCancelled( true );
	}
	
	@EventHandler
	public void onDeathMessagePreparedEvent( DeathMessagePreparedEvent event )
	{
		//Bukkit.broadcastMessage( "msg > " + event.getMessage().toPlainText() );
 		//Bukkit.broadcastMessage( "DeathMessagePreparedEvent [victim: "+ event.getPlayer( ).getName( ) + "] >> message: " );
	}
}

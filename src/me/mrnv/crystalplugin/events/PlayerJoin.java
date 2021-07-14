package me.mrnv.crystalplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import me.mrnv.crystalplugin.Main;

public class PlayerJoin implements Listener
{
	public Main plugin;
	private Location spawnlocation;
	
	public PlayerJoin( Main plugin )
	{
		this.plugin = plugin;
		this.spawnlocation = new Location(
				Bukkit.getServer( ).getWorlds( ).get( 0 ), -0.5D, 118.5D, 0.5D );
	}
	
	@EventHandler
	public void onPlayerJoin( PlayerJoinEvent event )
	{
		Player player = event.getPlayer( );
		if( player.isDead( ) )
			player.spigot( ).respawn( );
		
		player.teleport( spawnlocation );
		
		if( !player.isOp( ) && !player.getGameMode( ).equals( GameMode.SURVIVAL ) )
			player.setGameMode( GameMode.SURVIVAL );
		
		if( !player.isDead( ) )
		{
			for( PotionEffect effect : player.getActivePotionEffects( ) )
				player.removePotionEffect( effect.getType( ) );
			
			player.setHealth( player.getMaxHealth( ) );
			player.setFoodLevel( 20 ); // this should be the max one
			player.setExp( 0.0F );
			player.setLevel( 0 );
			
			if( player.isFlying( ) )
				player.setFlying( false );
			
			player.getInventory( ).clear( );
			player.updateInventory( );
		}
	}
	
	@EventHandler
	public void onPlayerQuit( PlayerQuitEvent event )
	{
		plugin.getInventoryManager( ).remove( event.getPlayer( ).getUniqueId( ) );
	}
	
	@EventHandler
	public void onPlayerKick( PlayerKickEvent event )
	{
		plugin.getInventoryManager( ).remove( event.getPlayer( ).getUniqueId( ) );
	}
	
	@EventHandler
	public void onPlayerRespawn( PlayerRespawnEvent event )
	{
		event.setRespawnLocation( spawnlocation );
	}
}
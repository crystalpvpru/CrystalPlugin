package me.mrnv.crystalplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftHopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import me.mrnv.crystalplugin.Main;

public class EntityDamage implements Listener
{
	private Main plugin;
	
	public EntityDamage( Main plugin )
	{
		this.plugin = plugin;
	}
	
	/*@EventHandler
	public void onEntityDamage( EntityDamageEvent event )
	{
		if( event.getCause( ) != DamageCause.FALL ||
			!( event.getEntity( ) instanceof Player ) )
			return;
		
		Player player = ( Player )event.getEntity( );
		if( player == null ||
			player.getWorld( ) == null ||
			player.getWorld( ).getEnvironment( ).equals( Environment.THE_END ) )
			return;
		
		Location location = player.getLocation( );
		if( location.getX( ) <= 100.0D && location.getX( ) >= -100.0D &&
			location.getZ( ) <= 100.0D && location.getZ( ) >= -100.0D )
		{
			event.setCancelled( true );
			event.setDamage( 0.0D );
		}
	}*/
	
	@EventHandler
	public void onEntityDamageByEntity( EntityDamageByEntityEvent event )
	{
		if( !( event.getDamager( ) instanceof Player ) ) return;
		
		Player damager = ( Player )event.getDamager( );
		ItemStack item = damager.getItemInHand( );
		
		if( damager.getWorld( ) == null )
			return;
		
		if( plugin.getUtils( ).isOverEnchanted( item ) )
		{
			if( damager.getWorld( ).getEnvironment( ).equals( Environment.THE_END ) )
			{
				if( damager.getOpenInventory( ) == null ||
					damager.getOpenInventory( ).getTopInventory( ) == null ||
					!( damager.getOpenInventory( ).getTopInventory( ).getType( ).equals( InventoryType.HOPPER ) ) ||
					!plugin.getInventoryManager( ).check( damager.getOpenInventory( ).getTopInventory( ) ) )
				{
					event.setCancelled( true );
					event.setDamage( 0.0D );
				}
			}
			else
			{
				event.setCancelled( true );
				event.setDamage( 0.0D );
			}
		}
	}
}
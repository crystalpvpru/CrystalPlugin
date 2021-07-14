package me.mrnv.crystalplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftHopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import me.mrnv.crystalplugin.Main;

public class InventoryHandling implements Listener
{
	private Main plugin;
	
	public InventoryHandling( Main plugin )
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onOpen( InventoryOpenEvent event )
	{
		plugin.getInventoryManager( ).add( event.getPlayer( ).getUniqueId( ), event.getInventory( ) );
		plugin.getUtils( ).revertInventory( event.getInventory( ) );
		plugin.getUtils( ).revertInventory( event.getPlayer( ).getInventory( ) );
		
		Player player = ( Player )event.getPlayer( );
		player.updateInventory( );
	}
	
	@EventHandler
	public void onClose( InventoryCloseEvent event )
	{
		plugin.getInventoryManager( ).remove( event.getPlayer( ).getUniqueId( ) );
		plugin.getUtils( ).revertInventory( event.getInventory( ) );
		plugin.getUtils( ).revertInventory( event.getPlayer( ).getInventory( ) );
		
		Player player = ( Player )event.getPlayer( );
		player.updateInventory( );
	}
	
	@EventHandler
	public void onInventoryMoveItem( InventoryMoveItemEvent event )
	{
		if( event.getInitiator( ).getHolder( ) instanceof CraftHopper )
		{
			if( !plugin.getInventoryManager( ).check( event.getInitiator( ) ) )
				event.setCancelled( true );
		}
	}
}

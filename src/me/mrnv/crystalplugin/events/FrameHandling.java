package me.mrnv.crystalplugin.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.mrnv.crystalplugin.Main;

public class FrameHandling implements Listener
{
	private Main plugin;
	
	public FrameHandling( Main plugin )
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInteractEntity( PlayerInteractEntityEvent event )
	{
		if( event.getRightClicked( ) == null ||
			!( event.getRightClicked( ) instanceof ItemFrame ) )
			return;
		
		ItemFrame itemframe = ( ItemFrame )event.getRightClicked( );
		if( itemframe.getItem( ) == null ||
			itemframe.getItem( ).getType( ).equals( Material.AIR ) )
			return;
		
		event.setCancelled( true );
		itemframe.setRotation( itemframe.getRotation( ) );
		
		plugin.getUtils( ).openInventory( event.getPlayer( ), itemframe.getItem( ) );
	}
	
	@EventHandler
	public void onEntityDamageByEntity( EntityDamageByEntityEvent event )
	{
		if( !( event.getEntity( ) instanceof ItemFrame ) ) return;
		
		if( !( event.getDamager( ) instanceof Player ) )
		{
			event.setCancelled( true );
			return;
		}
		
		Player player = ( Player )event.getDamager( );
		if( !player.getGameMode( ).equals( GameMode.CREATIVE ) )
			event.setCancelled( true );
	}
}

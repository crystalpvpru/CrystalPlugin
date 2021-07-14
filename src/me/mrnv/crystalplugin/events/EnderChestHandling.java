package me.mrnv.crystalplugin.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderChestHandling implements Listener
{
	@EventHandler
	public void onPlayerInteract( PlayerInteractEvent event )
	{
		if( event.getAction( ) == Action.RIGHT_CLICK_BLOCK &&
			event.getClickedBlock( ).getType( ) == Material.ENDER_CHEST )
		{
			Player player = event.getPlayer( );
			if( !player.isOp( ) &&
				( player.getGameMode( ) == GameMode.SURVIVAL ||
				player.getGameMode( ) == GameMode.SPECTATOR ) )
				event.setCancelled( true );
		}
	}
}

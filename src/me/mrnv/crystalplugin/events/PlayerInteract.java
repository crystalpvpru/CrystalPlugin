package me.mrnv.crystalplugin.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener
{
	@EventHandler
	public void onPlayerInteract( PlayerInteractEvent event )
	{
		if( event.getAction( ) == Action.RIGHT_CLICK_BLOCK &&
			event.getClickedBlock( ).getType( ) == Material.NOTE_BLOCK )
		{
			if( !event.getPlayer( ).isOp( ) )
				event.setCancelled( true );
		}
	}
}

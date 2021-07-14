package me.mrnv.crystalplugin.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class NoVineGrowth implements Listener
{
	@EventHandler
	public void onBlockGrow( BlockGrowEvent event )
	{
		if( event.getNewState( ).getType( ) == Material.VINE )
			event.setCancelled( true );
	}
}

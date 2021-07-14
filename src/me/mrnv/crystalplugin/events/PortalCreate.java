package me.mrnv.crystalplugin.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalCreate implements Listener
{
	// todo: test this
	@SuppressWarnings( "unlikely-arg-type" )
	@EventHandler
	public void onPortalCreate( PortalCreateEvent event )
	{
		if( event.getBlocks( ).contains( Material.PORTAL ) )
		{}//event.setCancelled( true );
	}
}

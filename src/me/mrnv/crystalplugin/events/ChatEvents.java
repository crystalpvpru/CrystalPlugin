package me.mrnv.crystalplugin.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.mrnv.crystalplugin.Main;

public class ChatEvents implements Listener
{
	public Main plugin;
	
	public ChatEvents( Main plugin )
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerCommand( PlayerCommandPreprocessEvent event )
	{
		if( event.getMessage( ).startsWith( "/?" ) )
		{
			plugin.getUtils( ).sendHelp( event.getPlayer( ) );
			event.setCancelled( true );
		}
	}
}

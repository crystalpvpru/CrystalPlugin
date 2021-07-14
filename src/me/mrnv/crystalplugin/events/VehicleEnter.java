package me.mrnv.crystalplugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class VehicleEnter implements Listener
{
	@EventHandler
	public void onVehicleEnter( VehicleEnterEvent event )
	{
		if( event.getEntered( ) instanceof Player )
			event.setCancelled( true );
	}
}

package me.mrnv.crystalplugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.inventory.Inventory;

public class InventoryManager
{
	private Map< UUID, Inventory > data = new HashMap< >( );
	
	public InventoryManager( )
	{
		data = new HashMap< >( );
	}
	
	public void add( UUID uuid, Inventory inventory )
	{
		data.put( uuid, inventory );
	}
	
	public void remove( UUID uuid )
	{
		if( data.containsKey( uuid ) )
			data.remove( uuid );
	}
	
	public boolean check( Inventory inv )
	{
		return data.containsValue( inv );
	}
}

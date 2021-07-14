package me.mrnv.crystalplugin;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;

public class Tab
{
	private Main plugin;
	private String spaces = "                                                                                   ";
	
	public Tab( Main plugin )
	{
		this.plugin = plugin;
	}
	
	public void sendTabPacketToPlayer( Player player, PacketPlayOutPlayerListHeaderFooter packet )
	{
		CraftPlayer craftplayer = ( CraftPlayer )player;
		craftplayer.getHandle( ).playerConnection.sendPacket( packet );
	}
	
	public void sendTabPacketToPlayer( Player player )
	{
		sendTabPacketToPlayer( player, getPacket( player ) );
	}
	
	public void addTask( )
	{
		Bukkit.getScheduler( ).scheduleAsyncRepeatingTask( plugin,
		new Runnable( )
		{
			public void run( )
			{
				if( Bukkit.getOnlinePlayers( ).size( ) == 0 ) return;
				
				for( Player player : Bukkit.getOnlinePlayers( ) )
					sendTabPacketToPlayer( player, getPacket( player ) );
			}	
		}, 20, 20 );
	}
	
	public PacketPlayOutPlayerListHeaderFooter getPacket( Player player )
	{
		try
		{
			CraftPlayer craftplayer = ( CraftPlayer )player;
			
			PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter( );
			
			Field header = packet.getClass( ).getDeclaredField( "a" );
			header.setAccessible( true );
			
			Field footer = packet.getClass( ).getDeclaredField( "b" );
			footer.setAccessible( true );
			
			ChatComponentText headertext = new ChatComponentText(
					spaces + "\n" +
					ChatColor.RED + "crystalpvp.ru\n" +
					ChatColor.LIGHT_PURPLE + "BETA\n\n" +
					ChatColor.GOLD +
					"Взять кит: /kit\n" +
					"Создать кит: /kitcreator\n" +
					"Сохранить кит: /createukit <название>\n" );
			
			long maxmem = Runtime.getRuntime( ).totalMemory( ) / 1048576;
			long usedmem = maxmem - Runtime.getRuntime( ).freeMemory( ) / 1048576;
			
			ChatComponentText footertext = new ChatComponentText(
					"\n" +
					ChatColor.GRAY + "tps " + plugin.getUtils( ).getTPS( true ) +
					ChatColor.GRAY + " пинг " + ChatColor.WHITE + craftplayer.getHandle( ).ping +
					ChatColor.GRAY + " онлайн " + ChatColor.WHITE + Bukkit.getOnlinePlayers( ).size( ) +
					ChatColor.GRAY + " аптайм " + ChatColor.WHITE + plugin.getUtils( ).getUptime( ) +
					"\n" + ChatColor.GRAY + "ram " + ChatColor.WHITE + usedmem + "Mb" + ChatColor.GRAY + "/" + ChatColor.WHITE + maxmem + "Mb\n\n" +
					ChatColor.GRAY +
					"сайт: crystalpvp.ru\n" +
					"дискорд: crystalpvp.ru/discord\n" );
			
			header.set( packet, headertext );
			footer.set( packet, footertext );
			
			return packet;
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
		
		return null;
	}
}

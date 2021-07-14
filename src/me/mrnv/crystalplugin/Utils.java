package me.mrnv.crystalplugin;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.realized.duels.api.arena.Arena;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MinecraftServer;

public class Utils
{
	private Main plugin;
	private long nexttpstime = 0;
	private String lasttps = "";
	private Map< UUID, String > uuiddata = new HashMap< >( );
	
	public Utils( Main plugin )
	{
		this.plugin = plugin;
	}
	
	public void sendHelp( Player player )
	{
		String str = ChatColor.YELLOW + "я еще не придумал что написать в help";
		
		player.sendMessage( str );
	}
	
	public String getTPS( boolean colored )
	{
		if( System.currentTimeMillis( ) < nexttpstime ) return lasttps;
		
		double tps = MinecraftServer.getServer( ).recentTps[ 0 ];
		if( tps > 20 ) tps = 20;
		
		lasttps = String.format( "%.2f", tps );
		if( colored )
		{
			if( tps >= 16.0D )
				lasttps = ChatColor.GREEN + lasttps;
			else if( tps >= 12.0D )
				lasttps = ChatColor.YELLOW + lasttps;
			else
				lasttps = ChatColor.RED + lasttps;
		}
		
		nexttpstime = System.currentTimeMillis( ) + 3000;
		
		return lasttps;
	}
	
	public String getUptime( )
	{
		long uptime = System.currentTimeMillis( ) - ManagementFactory.getRuntimeMXBean( ).getStartTime( );
		uptime /= 1000;
		
		int days = ( int )( uptime / 86400 );
		int hours = ( int )( ( uptime / 3600 ) % 24 );
		int minutes = ( int )( ( uptime / 60 ) % 60 );
		int seconds = ( int )( uptime % 60 );
		
		StringBuilder sb = new StringBuilder( );
		if( days > 0 )
			sb.append( days + "d " );
		
		if( days > 0 || hours > 0 )
			sb.append( hours + "h " );
		
		if( days > 0 || hours > 0 || minutes > 0 )
			sb.append( minutes + "m " );
		
		if( days > 0 || hours > 0 || minutes > 0 || seconds > 0 )
			sb.append( seconds + "s" );
		
		return sb.toString( );
	}
	
	public void openInventory( Player player, ItemStack item )
	{
		item.setAmount( item.getMaxStackSize( ) );
		
		Inventory inv = Bukkit.createInventory( null, 9, item.getI18NDisplayName( ) );
		for( int i = 0; i < 9; i++ )
			inv.setItem( i, item );
		
		player.openInventory( inv );
	}
	
	public boolean isOverEnchanted( ItemStack item )
	{
		Map< Enchantment, Integer > enchantments =
				( item.getItemMeta( ) != null && item.getItemMeta( ).hasEnchants( ) )
				? item.getItemMeta( ).getEnchants( )
				: item.getEnchantments( );
				
		if( enchantments != null )
		{
			for( Enchantment ench : Enchantment.values( ) )
			{
				if( ench == null ) continue;
				
				if( enchantments.containsKey( ench ) )
				{
					if( item.getEnchantmentLevel( ench ) > ench.getMaxLevel( ) ||
						!ench.canEnchantItem( item ) )
						return true;
				}
			}
		}
		
		return false;
	}
	
	public void revertInventory( Inventory inventory )
	{
		for( ItemStack item : inventory.getContents( ) )
		{
			if( item == null ) continue;
			
			Map< Enchantment, Integer > enchantments = null;

			if( item.hasItemMeta( ) && item.getItemMeta( ) != null &&
				item.getItemMeta( ).hasEnchants( ) && item.getItemMeta( ).getEnchants( ) != null )
				enchantments = item.getItemMeta( ).getEnchants( );
			else
				enchantments = item.getEnchantments( );
					
			if( enchantments == null ) continue;
			
			for( Enchantment ench : Enchantment.values( ) )
			{
				if( ench == null ) continue;
				
				if( enchantments.containsKey( ench ) )
				{
					if( !ench.canEnchantItem( item ) )
						item.removeEnchantment( ench );
					else if( item.getEnchantmentLevel( ench ) > ench.getMaxLevel( ) )
						item.addUnsafeEnchantment( ench, ench.getMaxLevel( ) );
				}
			}
		}
	}
	
	public void cleanLocation( Location pos1, Location pos2, World world, int height )
	{
		try
		{
			Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin,
			new Runnable( )
			{
				public void run( )
				{
					int chunk1x = pos1.getBlockX( ) >> 4;
					int chunk1z = pos1.getBlockZ( ) >> 4;
					int chunk2x = pos2.getBlockX( ) >> 4;
					int chunk2z = pos2.getBlockZ( ) >> 4;
					
					int xmin = Math.min( chunk1x, chunk2x );
					int xmax = Math.max( chunk1x, chunk2x );
					int zmin = Math.min( chunk1z, chunk2z );
					int zmax = Math.max( chunk1z, chunk2z );
					
					for( int x = xmin; x <= xmax; x++ )
					{
						for( int z = zmin; z <= zmax; z++ )
						{
							world.getChunkAtAsync( x, z, new World.ChunkLoadCallback( )
							{
								@Override
								public void onLoad( Chunk chunk )
								{
									for( Entity entity : chunk.getEntities( ) )
									{
										if( !( entity instanceof Player ) )
											entity.remove( );
									}
									
									// blocks
									for( int xblock = 0; xblock <= 15; xblock++ )
									{
										for( int yblock = 0; yblock <= height; yblock++ )
										{
											for( int zblock = 0; zblock <= 15; zblock++ )
											{
												Block block = chunk.getBlock( xblock, yblock, zblock );
												if( !block.getType( ).equals( Material.BEDROCK ) &&
													!block.getType( ).equals( Material.AIR ) )
													block.setType( Material.AIR );
											}
										}
									}
								}
							} );
						}
					}
				}
			} );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
	
	public void cleanEntities( Location pos1, Location pos2, World world )
	{
		try
		{
			Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin,
			new Runnable( )
			{
				public void run( )
				{
					int chunk1x = pos1.getBlockX( ) >> 4;
					int chunk1z = pos1.getBlockZ( ) >> 4;
					int chunk2x = pos2.getBlockX( ) >> 4;
					int chunk2z = pos2.getBlockZ( ) >> 4;
					
					int xmin = Math.min( chunk1x, chunk2x );
					int xmax = Math.max( chunk1x, chunk2x );
					int zmin = Math.min( chunk1z, chunk2z );
					int zmax = Math.max( chunk1z, chunk2z );
					
					for( int x = xmin; x <= xmax; x++ )
					{
						for( int z = zmin; z <= zmax; z++ )
						{
							world.getChunkAtAsync( x, z, new World.ChunkLoadCallback( )
							{
								@Override
								public void onLoad( Chunk chunk )
								{
									for( Entity entity : chunk.getEntities( ) )
									{
										if( !( entity instanceof Player ) )
											entity.remove( );
									}
								}
							} );
						}
					}
				}
			} );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
	
	public void cleanTheEnd( Location pos1, Location pos2, World world, int height )
	{
		try
		{
			Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin,
			new Runnable( )
			{
				public void run( )
				{
					int chunk1x = pos1.getBlockX( ) >> 4;
					int chunk1z = pos1.getBlockZ( ) >> 4;
					int chunk2x = pos2.getBlockX( ) >> 4;
					int chunk2z = pos2.getBlockZ( ) >> 4;
					
					int xmin = Math.min( chunk1x, chunk2x );
					int xmax = Math.max( chunk1x, chunk2x );
					int zmin = Math.min( chunk1z, chunk2z );
					int zmax = Math.max( chunk1z, chunk2z );
					
					for( int x = xmin; x <= xmax; x++ )
					{
						for( int z = zmin; z <= zmax; z++ )
						{
							world.getChunkAtAsync( x, z, new World.ChunkLoadCallback( )
							{
								@Override
								public void onLoad( Chunk chunk )
								{
									for( Entity entity : chunk.getEntities( ) )
									{
										if( !( entity instanceof Player ) )
											entity.remove( );
									}
									
									// blocks
									for( int xblock = 0; xblock <= 15; xblock++ )
									{
										for( int yblock = 0; yblock <= height; yblock++ )
										{
											for( int zblock = 0; zblock <= 15; zblock++ )
											{
												Block block = chunk.getBlock( xblock, yblock, zblock );
												if( block.getType( ).equals( Material.HOPPER ) ||
													block.getState( ) instanceof ShulkerBox )
													block.setType( Material.AIR );
											}
										}
									}
								}
							} );
						}
					}
				}
			} );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
		/*try
		{
			world.getChunkAtAsync( pos1, new World.ChunkLoadCallback( )
			{
				public void onLoad( Chunk chunk1 )
				{
					world.getChunkAtAsync( pos2, new World.ChunkLoadCallback( )
					{
						public void onLoad( Chunk chunk2 )
						{
							int xmin = Math.min( chunk1.getX( ), chunk2.getX( ) );
							int xmax = Math.max( chunk1.getX( ), chunk2.getX( ) );
							int zmin = Math.min( chunk1.getZ( ), chunk2.getZ( ) );
							int zmax = Math.max( chunk1.getZ( ), chunk2.getZ( ) );
							
							for( int x = xmin; x <= xmax; x++ )
							{
								for( int z = zmin; z <= zmax; z++ )
								{
									world.getChunkAtAsync( x, z, new World.ChunkLoadCallback( )
									{
										public void onLoad( Chunk chunk )
										{
											for( Entity entity : chunk.getEntities( ) )
											{
												if( !( entity instanceof Player ) )
													entity.remove( );
											}
											
											// blocks
											for( int xblock = 0; xblock <= 15; xblock++ )
											{
												for( int yblock = 0; yblock <= height; yblock++ )
												{
													for( int zblock = 0; zblock <= 15; zblock++ )
													{
														Block block = chunk.getBlock( xblock, yblock, zblock );
														if( block.getType( ).equals( Material.HOPPER ) ||
															block.getState( ) instanceof ShulkerBox )
															block.setType( Material.AIR );
													}
												}
											}
										}
									} );
								}
							}
						}
					} );
				}
			} );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}*/
	}
	
	public String getNameByUUID( UUID uuid )
	{
		for( UUID saveduuid : uuiddata.keySet( ) )
		{
			if( uuid.equals( saveduuid ) )
				return uuiddata.get( saveduuid );
		}
		
		for( Player player : Bukkit.getOnlinePlayers( ) )
		{
			if( player.getUniqueId( ).equals( uuid ) )
			{
				uuiddata.put( uuid, player.getName( ) );
				return player.getName( );
			}
		}
		
		return null;
	}
	
	public void broadcastToOps( String str )
	{
		for( Player player : Bukkit.getOnlinePlayers( ) )
		{
			if( player != null && player.isOp( ) )
				player.sendMessage(
						ChatColor.GRAY + "[" +
						ChatColor.DARK_RED + "CrystalPlugin" +
						ChatColor.GRAY + "] " + str );
		}
	}
	
	public void clean( )
	{
		Bukkit.getLogger( ).info( "[CrystalPlugin] Trying to clean main areas" );
		
		World overworld = Bukkit.getWorlds( ).get( 0 );
		World nether = Bukkit.getWorlds( ).get( 1 );
		World theend = Bukkit.getWorlds( ).get( 2 );
		
		Location overstart = new Location( overworld,
				200.0D, 0.0D, 200.0D );
		
		Location overend = new Location( overworld,
				-200.0D, 60.0D, -200.0D );
		
		Location kitroomstart = new Location( overworld,
				-300.0D, 140.0D, -385.0D );
		
		Location kitroomend = new Location( overworld,
				-380.0D, 215.0D, -320.0D );
		
		Location netherstart = new Location( nether,
				100.0D, 0.0D, 100.0D );
		
		Location netherend = new Location( nether,
				-100.0D, 95.0D, -100.0D );
		
		Location theendstart = new Location( theend,
				-200.0D, 0.0D, -200.0D );
		
		Location theendend = new Location( theend,
				200.0D, 256.0D, 200.0D );
		
		cleanLocation( overstart, overend, overworld, 60 );
		//cleanEntities( kitroomstart, kitroomend, overworld );
		cleanLocation( netherstart, netherend, nether, 95 );
		cleanTheEnd( theendstart, theendend, theend, 256 );
		
		Bukkit.getLogger( ).info( "[CrystalPlugin] Cleared all main areas" );
	}
}

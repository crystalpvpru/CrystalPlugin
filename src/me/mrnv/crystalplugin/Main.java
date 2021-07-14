package me.mrnv.crystalplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import me.mrnv.crystalplugin.events.AntiDuelsVoid;
import me.mrnv.crystalplugin.events.AntiVoid;
import me.mrnv.crystalplugin.events.BlockPlace;
import me.mrnv.crystalplugin.events.ChatEvents;
import me.mrnv.crystalplugin.events.DeathMessagesHandler;
import me.mrnv.crystalplugin.events.DuelsEvents;
import me.mrnv.crystalplugin.events.EnderChestHandling;
import me.mrnv.crystalplugin.events.EntityDamage;
import me.mrnv.crystalplugin.events.FrameHandling;
import me.mrnv.crystalplugin.events.InventoryHandling;
import me.mrnv.crystalplugin.events.NoVineGrowth;
import me.mrnv.crystalplugin.events.OffhandCrystalHandler;
import me.mrnv.crystalplugin.events.OffhandHandling;
import me.mrnv.crystalplugin.events.PacketFlyHandler;
import me.mrnv.crystalplugin.events.PlayerInteract;
import me.mrnv.crystalplugin.events.PlayerJoin;
import me.mrnv.crystalplugin.events.TeleportEvents;
import me.mrnv.crystalplugin.events.VehicleEnter;
import me.mrnv.crystalplugin.events.YKiller;
import me.realized.duels.api.Duels;

public class Main extends JavaPlugin
{
	private static Tab tab;
	private static Utils utils;
	private static InventoryManager invManager;
	private static DeathMessages deathMessages;
	private static YKiller yKiller;
	
	private static PlayerJoin playerJoinEvent;
	private static FrameHandling frameHandlingEvents;
	private static EntityDamage entityDamageEvents;
	private static InventoryHandling inventoryEvents;
	private static DuelsEvents duelsEvents;
	private static OffhandHandling offhandEvents;
	private static DeathMessagesHandler deathMessagesHandler;
	private static TeleportEvents teleportEvents;
	private static OffhandCrystalHandler offhandCrystalHandler;
	private static AntiVoid antiVoid;
	private static PacketFlyHandler packetFlyHandler;
	private static EnderChestHandling enderChestHandling;
	private static ChatEvents chatEvents;
	private static VehicleEnter vehicleEnterEvent;
	private static PlayerInteract playerInteractEvent;
	private static AntiDuelsVoid antiDuelsVoid;
	private static BlockPlace blockPlace;
	private static NoVineGrowth noVineGrowth;
	
	private Duels duels;
	private ProtocolManager protocolLib;
	
	private boolean shouldsaveend = false;
	
	public void onEnable( )
	{
		duels = ( Duels )Bukkit.getServer( ).getPluginManager( ).getPlugin( "Duels" );
		
		tab = new Tab( this );
		utils = new Utils( this );
		invManager = new InventoryManager( );
		
		playerJoinEvent = new PlayerJoin( this );
		frameHandlingEvents = new FrameHandling( this );
		entityDamageEvents = new EntityDamage( this );
		inventoryEvents = new InventoryHandling( this );
		duelsEvents = new DuelsEvents( this, duels );
		offhandEvents = new OffhandHandling( this );
		deathMessagesHandler = new DeathMessagesHandler( this );
		deathMessages = new DeathMessages( );
		teleportEvents = new TeleportEvents( this );
		offhandCrystalHandler = new OffhandCrystalHandler( this, duels );
		antiVoid = new AntiVoid( this );
		packetFlyHandler = new PacketFlyHandler( this );
		enderChestHandling = new EnderChestHandling( );
		chatEvents = new ChatEvents( this );
		vehicleEnterEvent = new VehicleEnter( );
		playerInteractEvent = new PlayerInteract( );
		antiDuelsVoid = new AntiDuelsVoid( this, duels );
		blockPlace = new BlockPlace( );
		yKiller = new YKiller( );
		noVineGrowth = new NoVineGrowth( );
		
		//tab.addTask( );
		offhandEvents.addTask( );
		teleportEvents.addTask( );
		offhandCrystalHandler.addTask( );
		antiVoid.addTask( );
		//packetFlyHandler.addTask( );
		antiDuelsVoid.addTask( );
		yKiller.addTask( this );
		
		protocolLib = ProtocolLibrary.getProtocolManager( );
		
		Bukkit.getPluginManager( ).registerEvents( playerJoinEvent, this );
		Bukkit.getPluginManager( ).registerEvents( frameHandlingEvents, this );
		Bukkit.getPluginManager( ).registerEvents( entityDamageEvents, this );
		Bukkit.getPluginManager( ).registerEvents( inventoryEvents, this );
		Bukkit.getPluginManager( ).registerEvents( offhandEvents, this );
		Bukkit.getPluginManager( ).registerEvents( teleportEvents, this );
		Bukkit.getPluginManager( ).registerEvents( offhandCrystalHandler, this );
		Bukkit.getPluginManager( ).registerEvents( packetFlyHandler, this );
		Bukkit.getPluginManager( ).registerEvents( enderChestHandling, this );
		Bukkit.getPluginManager( ).registerEvents( chatEvents, this );
		Bukkit.getPluginManager( ).registerEvents( vehicleEnterEvent, this );
		Bukkit.getPluginManager( ).registerEvents( playerInteractEvent, this );
		Bukkit.getPluginManager( ).registerEvents( blockPlace, this );
		Bukkit.getPluginManager( ).registerEvents( noVineGrowth, this );
		
		deathMessagesHandler.init( );
		Bukkit.getPluginManager( ).registerEvents( deathMessagesHandler, this );
		Bukkit.getPluginManager( ).registerEvents( duelsEvents, this );
		
		World theend = Bukkit.getWorlds( ).get( 2 );
		if( theend != null )
			theend.setAutoSave( false );
		
		boolean cleanspawn = false;
		if( cleanspawn )
			getUtils( ).clean( );
		
		// disable mob spawning n shieeeet
		Bukkit.dispatchCommand( Bukkit.getConsoleSender( ), "gamerule doMobSpawning false" );
		
		for( World world : Bukkit.getWorlds( ) )
		{
			WorldBorder border = world.getWorldBorder( );
			border.setDamageBuffer( 0.5 );
			border.setDamageAmount( 40 );
			
			world.setGameRuleValue( "doMobSpawning", "false" );
			world.setDifficulty( Difficulty.HARD );
		}

		Bukkit.getLogger( ).info( "[CrystalPlugin] Enabled" );
	}
	
	public void onDisable( )
	{
		/*if( !shouldsaveend )
		{
			World theend = Bukkit.getWorlds( ).get( 2 );
			if( theend != null )
				Bukkit.getServer( ).unloadWorld( theend, true );
		}*/
		
		Bukkit.getScheduler( ).cancelTasks( this );
		Bukkit.getLogger( ).info( "[CrystalPlugin] Disabled" );
	}
	
	public boolean onCommand( CommandSender sender, Command cmd, String lable, String[ ] args )
	{
		if( sender instanceof ConsoleCommandSender ||
			( sender instanceof Player && sender.isOp( ) ) )
		{
			if( cmd.getName( ).equalsIgnoreCase( "clean" ) )
			{
				sender.sendMessage( ChatColor.YELLOW + "Cleaning spawn areas..." );
				getUtils( ).clean( );
				
				return true;
			}
			else if( cmd.getName( ).equalsIgnoreCase( "endsaving" ) )
			{
				shouldsaveend = !shouldsaveend;
				
				World theend = Bukkit.getWorlds( ).get( 2 );
				if( theend != null )
					theend.setAutoSave( shouldsaveend );
					
				sender.sendMessage( ChatColor.YELLOW +
						( shouldsaveend
						? "Enabled. The End will be saved on server stop"
						: "Disabled. The End will NOT be saved on server stop" ) );
				
				return true;
			}
		}
		
		if( cmd.getName( ).equalsIgnoreCase( "help" ) &&
			( sender instanceof Player ) )
		{
			getUtils( ).sendHelp( ( Player )sender );
			
			return true;
		}
		
		return false;
	}
	
	public Tab getTab( )
	{
		return tab;
	}
	
	public Utils getUtils( )
	{
		return utils;
	}
	
	public InventoryManager getInventoryManager( )
	{
		return invManager;
	}
	
	public DeathMessages getDeathMessages( )
	{
		return deathMessages;
	}
	
	public ProtocolManager getProtocolManager( )
	{
		return protocolLib;
	}
}

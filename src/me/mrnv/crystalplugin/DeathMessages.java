package me.mrnv.crystalplugin;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;

public class DeathMessages
{
	public String[ ] unknown =
	{
		"%player% умер"
	};
	
	public String[ ] endcrystal =
	{
		"%player% умер от %endcrystala% игрока %killer%",
		"%killer% подорвал %player% с помощью %endcrystala%",
		"%killer% взорвал %player% с помощью %endcrystala%",
		"%player% был взорван %killer% с помощью %endcrystala%",
		"%killer% опробовал свой %endcrystal% на %player%"
	};
	
	public String[ ] endcrystalsuicide =
	{
		"%player% умер от своего же %endcrystala%",
		"%player% подорвал себя с помощью своего же %endcrystala%",
		"%player% был взорван с помощью своего же %endcrystala%"
	};
	
	public String[ ] normal =
	{
		"%player% умер от рук %killer%",
		"%killer% убил %player%"
	};
	
	public String[ ] normalweapon =
	{
		"%killer% убил %player% держа в руках %weapon%",
		"%killer% убил %player% с помощью %weapon%"
	};
	
	private Random random = new Random( );
	
	public String getRandomUnknown( String player )
	{
		String ret = null;
		while( ret == null )
			ret = unknown[ random.nextInt( unknown.length ) ];
		
		return getColored( ret, player, null, null, null );
	}
	
	public String getRandomCrystal( String player, EnderCrystal crystal, String killer )
	{
		String ret = null;
		while( ret == null )
			ret = endcrystal[ random.nextInt( endcrystal.length ) ];
		
		return getColored( ret, player, killer, null, crystal );
	}
	
	public String getRandomCrystalSuicide( String player, EnderCrystal crystal )
	{
		String ret = null;
		while( ret == null )
			ret = endcrystalsuicide[ random.nextInt( endcrystalsuicide.length ) ];
		
		return getColored( ret, player, null, null, crystal );
	}
	
	public String getRandomNormal( String player, String killer )
	{
		String ret = null;
		while( ret == null )
			ret = normal[ random.nextInt( normal.length ) ];
		
		return getColored( ret, player, killer, null, null );
	}
	
	public String getRandomWeapon( String player, String killer, String weapon )
	{
		String ret = null;
		while( ret == null )
			ret = normalweapon[ random.nextInt( normalweapon.length ) ];
		
		return getColored( ret, player, killer, weapon, null );
	}
	
	public String getColored( String str, String player, String killer, String weapon, EnderCrystal crystal )
	{
		if( player != null )
			str = str.replaceAll( "%player%", ChatColor.DARK_AQUA + player + ChatColor.DARK_RED );
		
		if( killer != null &&
			str.contains( "%killer%" ) )
			str = str.replaceAll( "%killer%", ChatColor.DARK_AQUA + killer + ChatColor.DARK_RED );
		
		if( crystal != null )
		{
			if( str.contains( "%endcrystal%" ) )
				str = str.replaceAll( "%endcrystal%", ChatColor.GOLD + "энд кристалл" + ChatColor.DARK_RED );
			
			if( str.contains( "%endcrystala%" ) )
				str = str.replaceAll( "%endcrystala%", ChatColor.GOLD + "энд кристалла" + ChatColor.DARK_RED );
		}
		
		if( weapon != null &&
			str.contains( "%weapon%" ) )
			str = str.replaceAll( "%weapon%", ChatColor.GOLD + weapon + ChatColor.DARK_RED );
		
		return ChatColor.DARK_RED + str;
	}
}

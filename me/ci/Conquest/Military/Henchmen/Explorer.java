package me.ci.Conquest.Military.Henchmen;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.ci.Community.Plot;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.Henchman;

public class Explorer implements Henchman{
	public boolean use(final Player p, final Plot plot){ return Kingdom.claimChunk(p, plot.getChunk(), true); }
	public void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use the explorer, click anywhere in the wilderness and you can scout it, even if its not touching your kingdom!"); }
}
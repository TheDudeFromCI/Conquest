package me.ci.Conquest.Military.Henchmen;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.Henchman;

public class BountyHunter implements Henchman{
	public boolean use(final Player p, final Plot plot){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(!kingdom.hasBountyHunter()){
			kingdom.addBountyHunter();
			p.sendMessage(ChatColor.GREEN+"Your kingdom is now protected by a bounty hunter.");
			return true;
		}else{
			p.sendMessage(ChatColor.RED+"Your kingdom already has a bounty hunter!");
			explain(p);
		}
		return false;
	}
	public void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use the bounty hunter, click anywhere. Your kingdom will then be given a bounty hunter which will protect your kingdom. The next time a henchman is used against your kingdom, it will be stopped."); }
}
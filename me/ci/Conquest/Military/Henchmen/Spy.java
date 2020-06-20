package me.ci.Conquest.Military.Henchmen;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.Henchman;

public class Spy implements Henchman{
	public boolean use(final Player p, final Plot plot){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(plot.isKingdomPlot()){
			if(plot.getKingdom()!=kingdom){
				if(!plot.getKingdom().isRuins()){
					if(plot.getKingdom().isOwnerOnline()){
						if(plot.getKingdom().hasBountyHunter()){
							p.sendMessage(ChatColor.LIGHT_PURPLE+"Your henchman was captured by a bounty hunter!");
							Bukkit.getPlayer(plot.getKingdom().getOwner()).sendMessage(ChatColor.LIGHT_PURPLE+"A henchman from the kingdom of "+kingdom.getName()+", owned by "+p.getName()+" has tried to attack your kingdom. Your bounty hunter has captured them in the act.");
							plot.getKingdom().useBountyHunter();
							return true;
						}
						plot.getBuilding().addSpy(kingdom);
						p.sendMessage(ChatColor.GREEN+"You are now spying on this building.");
						final Player enemy = Bukkit.getPlayer(plot.getKingdom().getOwner());
						enemy.sendMessage(ChatColor.LIGHT_PURPLE+"There are rumors of a spy in your kingdom.");
						if(plot.getKingdom().hasLiveWatchTower())enemy.sendMessage(ChatColor.LIGHT_PURPLE+kingdom.getName()+", owned by "+p.getName()+" was responsible for this attack. The spy is located in your "+plot.getBuilding().getType().getName().toLowerCase()+".");
						return true;
					}else{
						p.sendMessage(ChatColor.RED+"The owner of this kingdom is not online!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"This kingdom is in ruins!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"You cannot spy on your own kingdom!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"There is no kingdom here!");
			explain(p);
		}
		return false;
	}
	public void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use the spy, simply place it on an enemy building. From then on, until the building is destroyed, you will be able to view info on the building anytime."); }
}
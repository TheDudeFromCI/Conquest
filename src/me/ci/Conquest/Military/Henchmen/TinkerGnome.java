package me.ci.Conquest.Military.Henchmen;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.Henchman;

public class TinkerGnome implements Henchman{
	public boolean use(final Player p, final Plot plot){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(plot.isKingdomPlot()){
			if(plot.getKingdom()==kingdom){
				if(plot.getBuilding().getType()!=BuildingType.ARMY){
					if(!plot.getBuilding().hasTrap()){
						plot.getBuilding().addTrap();
						p.sendMessage(ChatColor.GREEN+"This building has been trapped.");
						return true;
					}else{
						p.sendMessage(ChatColor.RED+"There is already a trap on this building!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"You cannot put traps on army units!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This is not your kingdom!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"There is no kingdom here!");
			explain(p);
		}
		return false;
	}
	public void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use the tinker gnome, click on a building in your kingdom and he will build a trap on it for you. Next time that building is attacked, the attacking army unit will recive the damage instead."); }
}
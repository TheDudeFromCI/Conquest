package me.ci.Conquest.Military.Henchmen;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.Henchman;

public class Wizard implements Henchman{
	public boolean use(final Player p, final Plot plot){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(plot.isKingdomPlot()){
			if(plot.getKingdom()==kingdom){
				if(plot.getBuilding() instanceof Army){
					Army army = (Army)plot.getBuilding();
					if(!army.hasWizard()){
						army.addWizard();
						p.sendMessage(ChatColor.GREEN+"This unit has been given a wizard to help them.");
						return true;
					}else{
						p.sendMessage(ChatColor.RED+"This army unit already has a wizard with them!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"This is not an army unit!");
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
	public void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use the wizard, simply click on an army unit. From then on, they will be given the ability to levitate, so they will always do atleast normal damage, no matter the elevation difference."); }
}
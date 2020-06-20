package me.ci.Conquest.Military.Henchmen;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.Henchman;

public class Commander implements Henchman{
	public boolean use(final Player p, final Plot plot){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(plot.isKingdomPlot()){
			if(plot.getKingdom()==kingdom){
				if(plot.getBuilding() instanceof Army){
					final Army army = (Army)plot.getBuilding();
					if(!army.hasCommander()){
						army.addCommander();
						p.sendMessage(ChatColor.GREEN+"This unit now has its own commander.");
						return true;
					}else{
						p.sendMessage(ChatColor.RED+"This troop already has a commander!");
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
	public void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use the commander, cast it on a military unit and he will take control of it. From then on, that unit will automaticly move, guard areas, reform, camp, and attack enemies. You will still be able to override its actions and force it to move or attack. The commander will not however, use any special abilities."); }
}
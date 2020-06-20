package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.PowerDetails;

public class Catapult_Boulder implements PowerDetails{
	private static final int stamina = 8;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(plot.getKingdom()!=kingdom){
					if(!plot.getKingdom().isRuins()){
						if(plot.getKingdom().isOwnerOnline()){
							final Building previousTarget = troop.getTarget();
							troop.setTarget(plot.getBuilding());
							if(troop.isTargetAttackable(true)){
								troop.usePower(plot.getKingdom(), "Your "+plot.getBuilding().getType().toString().toLowerCase()+" was hit by a boulder!");
								troop.attack(1.75, false, false, true);
								troop.setStamina(troop.getStamina()-stamina);
								p.sendMessage(ChatColor.GREEN+"Boulder launched.");
							}else{
								p.sendMessage(ChatColor.RED+"This unit is not attackable!");
								explain(p);
							}
							troop.setTarget(previousTarget);
						}else{
							p.sendMessage(ChatColor.RED+"This owner of this kingdom is not online!");
							explain(p);
						}
					}else{
						p.sendMessage(ChatColor.RED+"This kingdom is in ruins!");
						explain(p);
					}
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to throw a boulder.");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on an enemy building or military unit. It will be damaged at 175% of this units attack."); }
}
package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.PowerDetails;

public class Knight_Protect implements PowerDetails{
	private static final int stamina = 5;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(plot.isKingdomPlot()){
					if(plot.getKingdom()==kingdom){
						if(plot.getBuilding() instanceof Army){
							final Army army = (Army)plot.getBuilding();
							if(army!=troop){
								if(!army.isProtected()){
									army.protect(troop);
									troop.setStamina(troop.getStamina()-stamina);
									p.sendMessage(ChatColor.GREEN+"Ally protected.");
								}else{
									p.sendMessage(ChatColor.RED+"This ally is already protected!");
									explain(p);
								}
							}else{
								p.sendMessage(ChatColor.RED+"This unit can't protect itself!");
								explain(p);
							}
						}else{
							p.sendMessage(ChatColor.RED+"This is not a military unit!");
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
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to protect anyone!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on another military unit and this knight will be linked to it, aborbing half of all the damage that it takes for 5 minutes."); }
}
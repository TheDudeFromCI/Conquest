package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Military.PowerDetails;

public class Jumper_Port implements PowerDetails{
	private static final int stamina = 3;
	public void run(final Player p, final Location click, final Army troop){
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(!plot.isClaimed()){
					if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=8
							&&Math.abs(plot.getZ()-troop.getNorthWestCourner().getZ())<=8){
						troop.moveTo(plot, false);
						troop.setStamina(troop.getStamina()-stamina);
						p.sendMessage(ChatColor.GREEN+"Unit ported.");
					}else{
						p.sendMessage(ChatColor.RED+"That area is too far away to port to!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"This area is blocked!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to port!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on an area of wilderness up to 8 chunks away, and this unit will instantly teleport to it."); }
}
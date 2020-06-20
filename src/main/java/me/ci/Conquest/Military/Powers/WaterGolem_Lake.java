package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Buildings.Main.Lake;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.PowerDetails;

public class WaterGolem_Lake implements PowerDetails{
	private static final int stamina = 6;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(!plot.isClaimed()){
					if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=1
							&&Math.abs(plot.getZ()-troop.getNorthWestCourner().getZ())<=1){
						if(kingdom.getLandOwned()<kingdom.getLandMax()){
							if(kingdom.getLandOwned()<PlayerMg.getLandMax(p.getName(), 2)){
								new Lake(plot.getChunk(), kingdom);
								troop.setStamina(troop.getStamina()-stamina);
								p.sendMessage(ChatColor.GREEN+"Lake formed.");
							}else{
								p.sendMessage(ChatColor.RED+"You have reached your kingdom size limit. If you wish to build a larger kingdom, please follow this link and buy \"Increase Land Max.\"");
								p.sendMessage(ChatColor.YELLOW+"http://82x78ni7zhoyn5p.buycraft.net/category/5730");
							}
						}else{
							p.sendMessage(ChatColor.RED+"This kingdom cannot hold any more land!");
							explain(p);
						}
					}else{
						p.sendMessage(ChatColor.RED+"This area is too far away to make a lake!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"This area is occupied!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to form a lake!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on an area in the wilderness that is touching this unit. A lake will be created there that can only be destroyed by bridgemakers. It is unpassable to all armies."); }
}
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

public class Mangonel_GreekFire implements PowerDetails{
	private static final int stamina = 9;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				final Plot plot = PlotMg.getPlotAt(click.getChunk());
				if(plot.isKingdomPlot()){
					if(plot.getKingdom()!=kingdom){
						if(!plot.getKingdom().isRuins()){
							if(plot.getKingdom().isOwnerOnline()){
								if(!(plot.getBuilding() instanceof Army)){
									plot.getBuilding().burn();
									troop.usePower(plot.getKingdom(), "Your "+plot.getBuilding().getType().toString().toLowerCase()+" has gone up in flames!");
									troop.setStamina(troop.getStamina()-stamina);
									p.sendMessage(ChatColor.GREEN+"This building has gone up in flames.");
								}else{
									p.sendMessage(ChatColor.RED+"This is not a building!");
									explain(p);
								}
							}else{
								p.sendMessage(ChatColor.RED+"This owner of this kingdom is not online!");
								explain(p);
							}
						}else{
							p.sendMessage(ChatColor.RED+"This kingdom is in ruins!");
							explain(p);
						}
					}else{
						p.sendMessage(ChatColor.RED+"You cannot burn up your own kingdom!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"There is no kingdom here!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to fling fire at the enemy!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullifed!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on an enemy building it will be set on fire. Dealing 100-500 damage per minute until the fire goes out. Each attack adds 3 minutes to the burn time."); }
}
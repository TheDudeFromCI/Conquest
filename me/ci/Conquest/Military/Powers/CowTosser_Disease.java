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
import me.ci.Conquest.BuildingInterfaces.VillagerWorkable;;

public class CowTosser_Disease implements PowerDetails{
	private static final int stamina = 16;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(plot.isKingdomPlot()){
					if(plot.getKingdom()!=kingdom){
						if(!plot.getKingdom().isRuins()){
							if(plot.getKingdom().isOwnerOnline()){
								if(!plot.getBuilding().isDiseased()){
									if(!(plot.getBuilding() instanceof VillagerWorkable)){
										troop.usePower(plot.getKingdom(), "Your "+plot.getBuilding().getType().toString().toLowerCase()+" was diseased!");
										plot.getBuilding().setDiseased();
										troop.setStamina(troop.getStamina()-stamina);
										p.sendMessage(ChatColor.GREEN+"Enemy diseased.");
									}else{
										p.sendMessage(ChatColor.RED+"You cannot disease buildings that villagers don't work in!");
										explain(p);
									}
								}else{
									p.sendMessage(ChatColor.RED+"This building is already diseased!");
									explain(p);
								}
							}else{
								p.sendMessage(ChatColor.RED+"The owner of this kingdom is offline!");
								explain(p);
							}
						}else{
							p.sendMessage(ChatColor.RED+"This kingdom is in ruins!");
							explain(p);
						}
					}else{
						p.sendMessage(ChatColor.RED+"You cannot disease your own kingdom!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"There is no kingdom here!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to disease the enemy!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on an enemy and it will permanently become diseased, killing off troops and villagers over time."); }
}
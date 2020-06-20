package me.ci.Conquest.Military.Powers;

import java.util.ArrayList;

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

public class Trebuchet_Demolish implements PowerDetails{
	private static final int stamina = 12;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(plot.getKingdom()!=kingdom){
					if(!plot.getKingdom().isRuins()){
						if(plot.getKingdom().isOwnerOnline()){
							final Building previousTarget = troop.getTarget();
							final int offX = plot.getX();
							final int offZ = plot.getZ();
							final ArrayList<Kingdom> enemies = new ArrayList<>();
							Plot plot1;
							for(int x = -1; x<=1; x++){
								for(int z = -1; z<=1; z++){
									plot1=PlotMg.getPlotAt(plot.getWorld(), x+offX, z+offZ);
									if(plot1.isKingdomPlot()
											&&plot1.getKingdom()!=kingdom){
										if(!enemies.contains(plot1.getKingdom()))enemies.add(plot1.getKingdom());
										troop.setTarget(plot1.getBuilding());
										if(troop.isTargetAttackable(true))troop.attack(0.9, false, false, false);
									}
								}
							}
							troop.takeBlessing();
							for(Kingdom e : enemies)troop.usePower(e, "Parts of your kingdom were demolished!");
							troop.setStamina(troop.getStamina()-stamina);
							p.sendMessage(ChatColor.GREEN+"Enemy demolished.");
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
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on an enemy building or military unit. It will be damaged, and the enemies around it will be damaged at 90% of this units attack."); }
}
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

public class EarthGolem_Sinkhole implements PowerDetails{
	private static final int stamina = 9;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=1
						&&Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=1){
					if(plot.isKingdomPlot()){
						if(plot.getKingdom()!=kingdom){
							if(plot.getBuilding().getLength()==1){
								if(plot.getHeight()>1){
									final Building previousTarget = troop.getTarget();
									troop.setTarget(plot.getBuilding());
									troop.usePower(plot.getKingdom(), "Your "+plot.getBuilding().getType().toString().toLowerCase()+" has sunked into the ground!");
									if(troop.isTargetAttackable(true))troop.attack(1.25, false, false, true);
									plot.setHeight(plot.getHeight()-5, true);
									troop.setStamina(troop.getStamina()-stamina);
									p.sendMessage(ChatColor.GREEN+"This area has sunk into the ground.");
									troop.setTarget(previousTarget);
								}else{
									p.sendMessage(ChatColor.RED+"This area can't be dropped anymore!");
									explain(p);
								}
							}else{
								p.sendMessage(ChatColor.RED+"This building is too big to sink!");
								explain(p);
							}
						}else{
							p.sendMessage(ChatColor.RED+"You cannot sink your own kingdom!");
							explain(p);
						}
					}else{
						if(plot.getHeight()>1){
							plot.setHeight(plot.getHeight()-5, true);
							troop.setStamina(troop.getStamina()-stamina);
							p.sendMessage(ChatColor.GREEN+"This area has sunk into the ground.");
						}else{
							p.sendMessage(ChatColor.RED+"This area can't be dropped anymore!");
							explain(p);
						}
					}
				}else{
					p.sendMessage(ChatColor.RED+"That area is too far away!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to sink the ground!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on a touching wilderness chunk, enemy unit, or 1x1 enemy building, and it will drop 5 meters."); }
}
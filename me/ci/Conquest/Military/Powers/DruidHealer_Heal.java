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

public class DruidHealer_Heal implements PowerDetails{
	private static final int stamina = 5;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(plot.isKingdomPlot()){
					if(plot.getKingdom()==kingdom){
						if(plot.getBuilding() instanceof Army){
							if(Math.abs(troop.getNorthWestCourner().getX()-plot.getX())<=1
									&&Math.abs(troop.getNorthWestCourner().getZ()-plot.getZ())<=1){
								if(plot.getBuilding().getHp()<plot.getBuilding().getMaxHp()){
									plot.getBuilding().setHp(Math.min(plot.getBuilding().getHp()+100, plot.getBuilding().getMaxHp()), true);
									troop.setStamina(troop.getStamina()-stamina);
									p.sendMessage(ChatColor.GREEN+"Unit healed.");
								}else{
									p.sendMessage(ChatColor.RED+"This unit is not damaged!");
									explain(p);
								}
							}else{
								p.sendMessage(ChatColor.RED+"That target is too far away!");
								explain(p);
							}
						}else{
							p.sendMessage(ChatColor.RED+"This unit can only heal other military units!");
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
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to heal!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on an ally troop unit and it will be restored 100 hp."); }
}
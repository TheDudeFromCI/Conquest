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

public class Ballista_Aim implements PowerDetails{
	private static final int stamina = 6;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				final Plot plot = PlotMg.getPlotAt(click.getChunk());
				if(plot.isKingdomPlot()){
					if(plot.getKingdom()!=kingdom){
						if(!plot.getKingdom().isRuins()){
							if(plot.getKingdom().isOwnerOnline()){
								if(plot.getBuilding() instanceof Army){
									final Building previousTarget = troop.getTarget();
									troop.setTarget(plot.getBuilding());
									if(troop.isTargetAttackable(true)){
										troop.usePower(plot.getKingdom(), "Your army was shot by a ballista!");
										troop.attack(1.5, false, false, true);
										troop.setStamina(troop.getStamina()-stamina);
										p.sendMessage(ChatColor.GREEN+"Target attacked.");
									}else{
										p.sendMessage(ChatColor.RED+"This unit is not attackable!");
										explain(p);
									}
									troop.setTarget(previousTarget);
								}else{
									p.sendMessage(ChatColor.RED+"This is not a military unit!");
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
						p.sendMessage(ChatColor.RED+"You cannot attack your own kingdom!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"There is no kingdom here!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to use aim!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullifed!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on an enemy military unit and this unit will attack it."); }
}
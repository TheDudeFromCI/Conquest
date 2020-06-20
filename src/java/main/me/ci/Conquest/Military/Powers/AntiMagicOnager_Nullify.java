package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.PowerDetails;

public class AntiMagicOnager_Nullify implements PowerDetails{
	private static final int stamina = 10;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = troop.getKingdom();
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(plot.isKingdomPlot()){
				if(plot.getKingdom()!=kingdom){
					if(plot.getKingdom().isOwnerOnline()){
						if(troop.getStamina()>=stamina){
							if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=troop.getArmyType().getRange()
									&&Math.abs(plot.getZ()-troop.getNorthWestCourner().getZ())<=troop.getArmyType().getRange()){
								if(plot.getBuilding().getType()==BuildingType.ARMY){
									Army other = (Army)plot.getBuilding();
									if(!other.isNullified()){
										other.nullify();
										troop.usePower(other.getKingdom(), "Your army was nullified!");
										troop.setStamina(troop.getStamina()-stamina);
										p.sendMessage(ChatColor.GREEN+"Enemy nullified.");
									}else{
										p.sendMessage(ChatColor.RED+"This army unit is already nullified!");
										explain(p);
									}
								}else{
									p.sendMessage(ChatColor.RED+"This is not an army unit!");
									explain(p);
								}
							}else{
								p.sendMessage(ChatColor.RED+"This unit is too far away!");
								explain(p);
							}
						}else{
							p.sendMessage(ChatColor.RED+"You do not have enough stamina to preform this skill!");
							explain(p);
						}
					}else{
						p.sendMessage(ChatColor.RED+"This army unit is already nullified!");
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
		}else p.sendMessage(ChatColor.RED+"This troop is nullified! This means they cannot use their abilities anymore!");
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, simply click on an enemy army unit within range. That unit will become nullified, and never be able to use their powers again."); }
}
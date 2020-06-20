package me.ci.Conquest.Military.Powers;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.PowerDetails;

public class Paladin_Sacrifice implements PowerDetails{
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(!troop.isNullified()){
			if(troop.getStamina()>0){
				final ArrayList<Army> allies = new ArrayList<>();
				final int offX = troop.getNorthWestCourner().getX();
				final int offZ = troop.getNorthWestCourner().getZ();
				Plot plot;
				for(int x = -1; x<=1; x++){
					for(int z = -1; z<=1; z++){
						plot=PlotMg.getPlotAt(troop.getWorld(), x+offX, z+offZ);
						if(plot.isKingdomPlot()
								&&plot.getKingdom()==kingdom
								&&plot.getBuilding() instanceof Army
								&&plot.getBuilding()!=troop
								&&((Army)plot.getBuilding()).getStamina()<20)allies.add((Army)plot.getBuilding());
					}
				}
				if(allies.size()>0){
					if(allies.size()<=troop.getStamina()){
						final int each = troop.getStamina()/allies.size();
						final int left = troop.getStamina()%allies.size();
						troop.setStamina(left);
						for(Army a : allies)a.setStamina(Math.max(a.getStamina()+each, 20));
						p.sendMessage(ChatColor.GREEN+"Allies' stamina refilled.");
					}else{
						p.sendMessage(ChatColor.RED+"There are too many other units and not enough stamina to give to them!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"There are no nearby units to give stamina to!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"You have no stamina to give!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, simply click anywhere. All of the stamina from this unit will be taken and divided up between all touching units."); }
}
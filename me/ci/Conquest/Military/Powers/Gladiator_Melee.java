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

public class Gladiator_Melee implements PowerDetails{
	private static final int stamina = 15;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				final Building previousTarget = troop.getTarget();
				final int offX = troop.getNorthWestCourner().getX();
				final int offZ = troop.getNorthWestCourner().getZ();
				final ArrayList<Kingdom> enemies = new ArrayList<>();
				Plot plot;
				for(int x = -1; x<=1; x++){
					for(int z = -1; z<=1; z++){
						plot=PlotMg.getPlotAt(troop.getWorld(), x+offX, z+offZ);
						if(plot.isKingdomPlot()
								&&plot.getKingdom()!=kingdom
								&&!plot.getKingdom().isRuins()
								&&plot.getKingdom().isOwnerOnline()){
							if(!enemies.contains(plot.getKingdom()))enemies.add(plot.getKingdom());
							troop.setTarget(plot.getBuilding());
							troop.attack(2.0, false, false, false);
						}
					}
				}
				troop.takeBlessing();
				for(Kingdom e : enemies)troop.usePower(e, "Your kingdom was attacked in a violent melee attack!");
				troop.setTarget(previousTarget);
				troop.setStamina(troop.getStamina()-stamina);
				p.sendMessage(ChatColor.GREEN+"Enemies meleed.");
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to preform a melee attack!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, just click anywhere. This unit will attack all touching enemies at 200% damage."); }
}
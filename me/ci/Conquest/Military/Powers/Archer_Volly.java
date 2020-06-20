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
import me.ci.Conquest.Misc.Direction;

public class Archer_Volly implements PowerDetails{
	private static final int stamina = 4;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				final Direction d = troop.getDirection();
				final int x = troop.getNorthWestCourner().getX();
				final int z = troop.getNorthWestCourner().getZ();
				final Building currentTarget = troop.getTarget();
				final ArrayList<Kingdom> enemies = new ArrayList<>();
				Plot plot;
				if(d==Direction.NORTH){
					for(int i = 1; i<=6; i++){
						plot=PlotMg.getPlotAt(troop.getWorld(), x, z-i);
						if(plot.isKingdomPlot()
								&&plot.getKingdom()!=kingdom
								&&!plot.getKingdom().isRuins()
								&&plot.getKingdom().isOwnerOnline()){
							if(!enemies.contains(plot.getKingdom()))enemies.add(plot.getKingdom());
							troop.setTarget(plot.getBuilding());
							if(troop.isTargetAttackable(true))troop.attack(1.0, false, false, false);
						}
					}
				}else if(d==Direction.EAST){
					for(int i = 1; i<=6; i++){
						plot=PlotMg.getPlotAt(troop.getWorld(), x+i, z);
						if(plot.isKingdomPlot()
								&&plot.getKingdom()!=kingdom
								&&!plot.getKingdom().isRuins()
								&&plot.getKingdom().isOwnerOnline()){
							if(!enemies.contains(plot.getKingdom()))enemies.add(plot.getKingdom());
							troop.setTarget(plot.getBuilding());
							if(troop.isTargetAttackable(true))troop.attack(1.0, false, false, false);
						}
					}
				}else if(d==Direction.SOUTH){
					for(int i = 1; i<=6; i++){
						plot=PlotMg.getPlotAt(troop.getWorld(), x, z+i);
						if(plot.isKingdomPlot()
								&&plot.getKingdom()!=kingdom
								&&!plot.getKingdom().isRuins()
								&&plot.getKingdom().isOwnerOnline()){
							if(!enemies.contains(plot.getKingdom()))enemies.add(plot.getKingdom());
							troop.setTarget(plot.getBuilding());
							if(troop.isTargetAttackable(true))troop.attack(1.0, false, false, false);
						}
					}
				}else{
					for(int i = 1; i<=6; i++){
						plot=PlotMg.getPlotAt(troop.getWorld(), x-i, z);
						if(plot.isKingdomPlot()
								&&plot.getKingdom()!=kingdom
								&&!plot.getKingdom().isRuins()
								&&plot.getKingdom().isOwnerOnline()){
							if(!enemies.contains(plot.getKingdom()))enemies.add(plot.getKingdom());
							troop.setTarget(plot.getBuilding());
							if(troop.isTargetAttackable(true))troop.attack(1.0, false, false, false);
						}
					}
				}
				troop.takeBlessing();
				troop.setTarget(currentTarget);
				troop.setStamina(troop.getStamina()-stamina);
				p.sendMessage(ChatColor.GREEN+"Enemies vollyed.");
				for(Kingdom k : enemies)troop.usePower(k, "Your kingdom was hit with a volly of arrows!");
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to do a volly!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, simply click anywhere. Arrows will be fired in all chunks in front of the archer in a line, attacking up to 6 enemies at once."); }
}
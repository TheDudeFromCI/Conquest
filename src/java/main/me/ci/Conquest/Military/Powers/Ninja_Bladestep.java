package me.ci.Conquest.Military.Powers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.PowerDetails;
import me.ci.Conquest.Misc.PathFinder;
import me.ci.Conquest.Misc.PathFinder.DistanceForumula;
import me.ci.Conquest.Misc.PathFinder.Node;

public class Ninja_Bladestep implements PowerDetails{
	private static final int stamina = 6;
	public void run(final Player p, final Location click, final Army troop){
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(!plot.isClaimed()){
					if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=4
							&&Math.abs(plot.getZ()-troop.getNorthWestCourner().getZ())<=4){
						if(!troop.isMoving()){
							final HashMap<Building,Integer> attacks = canMoveTo(plot, troop);
							if(attacks!=null){
								final Building previousTarget = troop.getTarget();
								troop.moveTo(plot, false);
								final ArrayList<Kingdom> enemies = new ArrayList<>();
								for(Building e : attacks.keySet()){
									if(!enemies.contains(e.getKingdom()))enemies.add(e.getKingdom());
									for(int i = 0; i<attacks.get(e); i++){
										troop.setTarget(e);
										troop.attack(1.0, false, false, false);
									}
								}
								troop.takeBlessing();
								for(Kingdom e : enemies)troop.usePower(e, "Your troops have been attacked by a bladestep!");
								troop.setStamina(troop.getStamina()-stamina);
								troop.setTarget(previousTarget);
								p.sendMessage(ChatColor.GREEN+"Bladestep used.");
							}else{
								p.sendMessage(ChatColor.RED+"That area cannot be reached using bladestep!");
								explain(p);
							}
						}else{
							p.sendMessage(ChatColor.RED+"You cannot use this ability while moving!");
							explain(p);
						}
					}else{
						p.sendMessage(ChatColor.RED+"That area is too far away!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"That area is occupied!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to use bladestep!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private HashMap<Building,Integer> canMoveTo(final Plot to, final Army troop){
		final HashMap<Building,Integer> attacks = new HashMap<>();
		try{
			troop.pf=new PathFinder();
			troop.pf.setStart(troop.pf.getNodeAt(to.getX(), to.getZ()));
			troop.pf.setEnd(troop.pf.getNodeAt(troop.getNorthWestCourner().getX(), troop.getNorthWestCourner().getZ()));
			troop.pf.setPathBuilder(troop.path);
			troop.pf.findPath(DistanceForumula.DISTANCE, 5);
			if(troop.pf.getPath().getSize()-1>4)return null;
			troop.pf.getPath().getNext();
			Plot p;
			Node n;
			while(troop.pf.getPath().hasNext(false)){
				n=troop.pf.getPath().getNext();
				p=PlotMg.getPlotAt(to.getWorld(), n.getX(), n.getY());
				for(Plot plot : getTouchingPlots(p)){
					if(plot.isKingdomPlot()
							&&plot.getKingdom()!=troop.getKingdom()
							&&!plot.getKingdom().isRuins()
							&&plot.getKingdom().isOwnerOnline()){
						if(attacks.containsKey(plot.getBuilding()))attacks.put(plot.getBuilding(), attacks.get(plot)+1);
						else attacks.put(plot.getBuilding(), 1);
					}
				}
			}
			troop.pf=null;
			return attacks;
		}catch(final Exception exception){}
		return null;
	}
	private Plot[] getTouchingPlots(final Plot plot){
		return null;
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on a wilderness chunk up to 4 move spaces away. This unit will move towards it, attacking anything is passes on the way."); }
}
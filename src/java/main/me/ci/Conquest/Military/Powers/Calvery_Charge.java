package me.ci.Conquest.Military.Powers;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.PowerDetails;
import me.ci.Conquest.Misc.PathFinder;
import me.ci.Conquest.Misc.PathFinder.DistanceForumula;
import me.ci.Conquest.Misc.PathFinder.Node;
import me.ci.Conquest.Misc.PathFinder.PathBuilder;
import me.ci.Conquest.Misc.PathFinder.PathFindingException;
import me.ci.Conquest.Misc.PriorityList;

public class Calvery_Charge implements PowerDetails{
	private PathFinder pf;
	private static final int stamina = 3;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(plot.isKingdomPlot()){
					if(plot.getKingdom()!=kingdom){
						if(!plot.getKingdom().isRuins()){
							if(plot.getKingdom().isOwnerOnline()){
								if(plot.getBuilding() instanceof Army){
									final Plot to;
									try{ to=getClosestAttackSpot(troop, plot.getBuilding());
									}catch(final Exception exception){ return; }
									if(to!=null){
										final Building previousTarget = troop.getTarget();
										troop.moveTo(to, false);
										troop.setTarget(plot.getBuilding());
										if(troop.isTargetAttackable(true)){
											troop.usePower(plot.getKingdom(), "Your army was charged by calvery!");
											troop.attack(1.0, false, false, true);
											troop.setStamina(troop.getStamina()-stamina);
											p.sendMessage(ChatColor.GREEN+"Enemy charged.");
										}else{
											p.sendMessage(ChatColor.RED+"Target is not attackable!");
											explain(p);
										}
										troop.setTarget(previousTarget);
									}else{
										p.sendMessage(ChatColor.RED+"There is no way to reach this target using charge!");
										explain(p);
									}
								}else{
									p.sendMessage(ChatColor.RED+"This is not a military unit!");
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
						p.sendMessage(ChatColor.RED+"You cannot attack your own kingdom!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"There is no kingdom!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to charge!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private Plot getClosestAttackSpot(final Army troop, final Building enemy)throws PathFindingException{
		final Chunk nwc = troop.getNorthWestCourner();
		final PathBuilder builder = new PathBuilder(){
			public ArrayList<Node> getOpenPoints(Node o){
				final ArrayList<Node> nodes = new ArrayList<Node>();
				if(pf==null
						||nwc==null)return nodes;
				final Node n1 = pf.getNodeAt(o.getX()+1, o.getY());
				final Node n2 = pf.getNodeAt(o.getX()-1, o.getY());
				final Node n3 = pf.getNodeAt(o.getX(), o.getY()+1);
				final Node n4 = pf.getNodeAt(o.getX(), o.getY()-1);
				final Node n5 = pf.getNodeAt(o.getX()+1, o.getY()+1);
				final Node n6 = pf.getNodeAt(o.getX()+1, o.getY()-1);
				final Node n7 = pf.getNodeAt(o.getX()-1, o.getY()+1);
				final Node n8 = pf.getNodeAt(o.getX()-1, o.getY()-1);
				n1.setMoveCost(10);
				n2.setMoveCost(10);
				n3.setMoveCost(10);
				n4.setMoveCost(10);
				n5.setMoveCost(14);
				n6.setMoveCost(14);
				n7.setMoveCost(14);
				n8.setMoveCost(14);
				if(!PlotMg.getPlotAt(nwc.getWorld(), n1.getX(), n1.getY()).isClaimed())nodes.add(n1);
				if(!PlotMg.getPlotAt(nwc.getWorld(), n2.getX(), n2.getY()).isClaimed())nodes.add(n2);
				if(!PlotMg.getPlotAt(nwc.getWorld(), n3.getX(), n3.getY()).isClaimed())nodes.add(n3);
				if(!PlotMg.getPlotAt(nwc.getWorld(), n4.getX(), n4.getY()).isClaimed())nodes.add(n4);
				if(!PlotMg.getPlotAt(nwc.getWorld(), n5.getX(), n5.getY()).isClaimed())nodes.add(n5);
				if(!PlotMg.getPlotAt(nwc.getWorld(), n6.getX(), n6.getY()).isClaimed())nodes.add(n6);
				if(!PlotMg.getPlotAt(nwc.getWorld(), n7.getX(), n7.getY()).isClaimed())nodes.add(n7);
				if(!PlotMg.getPlotAt(nwc.getWorld(), n8.getX(), n8.getY()).isClaimed())nodes.add(n8);
				final int h = PlotMg.getPlotAt(nwc).getHeight();
				for(Node n : nodes){ n.setMoveCost(n.getMoveCost()*(Math.abs(h-PlotMg.getPlotAt(nwc.getWorld(), n.getX(), n.getY()).getHeight())+1)); }
				return nodes;
			}
		};
		final PriorityList<Plot> list = new PriorityList<>();
		final int offX = enemy.getNorthWestCourner().getX();
		final int offZ = enemy.getNorthWestCourner().getZ();
		Plot plot;
		int cost;
		for(int x = -2; x<=2; x++){
			for(int z = -2; z<=2; z++){
				plot=PlotMg.getPlotAt(nwc.getWorld(), x+offX, z+offZ);
				pf=new PathFinder();
				pf.setStart(pf.getNodeAt(nwc.getX(), nwc.getZ()));
				pf.setEnd(pf.getNodeAt(plot.getX(), plot.getZ()));
				pf.setPathBuilder(builder);
				cost=getPathCost();
				list.add(plot, cost);
			}
		}
		return list.getMostImportant();
	}
	private int getPathCost(){
		try{
			pf.findPath(DistanceForumula.DISTANCE, 2);
			if(pf.getPath().getSize()>3)return -1;
			return 10-pf.getPath().getSize();
		}catch(final Exception exception){}
		return -1;
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on an enemy military unit. This unit will move towards it, up to 2 chunks forward, and attack the enemy in the same move."); }
}
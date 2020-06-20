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

public class WindGolem_Gust implements PowerDetails{
	private static final int stamina = 5;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				final ArrayList<Kingdom> enemies = new ArrayList<>();
				final Building previousTarget = troop.getTarget();
				final int offX = troop.getNorthWestCourner().getX();
				final int offZ = troop.getNorthWestCourner().getZ();
				Plot plot;
				Army a;
				for(int x = -1; x<=1; x++){
					for(int z = -1; z<=1; z++){
						plot=PlotMg.getPlotAt(troop.getWorld(), x+offX, z+offZ);
						if(plot.isKingdomPlot()
								&&plot.getKingdom()!=kingdom
								&&!plot.getKingdom().isRuins()
								&&plot.getKingdom().isOwnerOnline()
								&&plot.getBuilding() instanceof Army){
							a=(Army)plot.getBuilding();
							push(a, subpush(a, x, z), troop);
						}
					}
				}
				for(Kingdom e : enemies)troop.usePower(e, "Your units were blown away!");
				troop.takeBlessing();
				troop.setTarget(previousTarget);
				troop.setStamina(troop.getStamina()-stamina);
				p.sendMessage(ChatColor.GREEN+"Enemies pushed.");
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to make a wind strong enough to push away your enemies!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private void push(final Army a, final int[] d, final Army troop){
		if(a==null)return;
		if(d[0]==1){
			double damage = 0.25;
			damage+=d[1]*0.1;
			troop.setTarget(a);
			troop.attack(damage, false, false, false);
		}
	}
	private int[] subpush(final Army a, final int x, final int z){
		final Plot plot = PlotMg.getPlotAt(a.getWorld(), a.getNorthWestCourner().getX()+x, a.getNorthWestCourner().getZ()+z);
		final Plot o = PlotMg.getPlotAt(a.getWorld(), a.getNorthWestCourner().getX(), a.getNorthWestCourner().getZ());
		if(plot.isClaimed())return new int[]{0};
		if(plot.getHeight()>o.getHeight())return new int[]{0};
		a.moveTo(plot, false);
		return new int[]{1, plot.getHeight()-o.getHeight()};
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, simply click anywhere. A strong wind will appear and push away all nearby units and deal slight damage. Units can also recive fall damage if they fall off a cliff."); }
}
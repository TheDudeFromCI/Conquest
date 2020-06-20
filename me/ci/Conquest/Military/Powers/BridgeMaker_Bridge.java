package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Buildings.Main.Moat;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.PowerDetails;

public class BridgeMaker_Bridge implements PowerDetails{
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
								if(plot.getBuilding() instanceof Moat){
									troop.usePower(plot.getKingdom(), "Your "+plot.getBuilding().getType().toString().toLowerCase()+" was covered up by a bridge!");
									plot.getBuilding().delete(true, false);
									troop.setStamina(troop.getStamina()-stamina);
									p.sendMessage(ChatColor.GREEN+"Bridge made.");
								}else{
									p.sendMessage(ChatColor.RED+"This is not a moat or lake!");
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
						p.sendMessage(p.getName());
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"There is no kingdom!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to make a bridge!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on a moat or lake and it will be destroyed instantly."); }
}
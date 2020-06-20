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

public class Illusionist_Illusion implements PowerDetails{
	private static final int stamina = 5;
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
									if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=1
											&&Math.abs(plot.getZ()-troop.getNorthWestCourner().getZ())<=1){
										final Army army = (Army)plot.getBuilding();
										if(!army.isUnderIllusion()){
											army.illusion();
											troop.usePower(plot.getKingdom(), "Your troops are under an illusion!");
											troop.setStamina(troop.getStamina()-stamina);
											p.sendMessage(ChatColor.GREEN+"The enemy is now under an illusion.");
										}else{
											p.sendMessage(ChatColor.RED+"This target is already under an illusion!");
											explain(p);
										}
									}else{
										p.sendMessage(ChatColor.RED+"That target is too far away!");
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
						p.sendMessage(ChatColor.RED+"You cannot make illusions against your own kingdom!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"There is no kingdom here!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to make illusions!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on a touching enemy unit, and for the next minute, it will begin to attack itself. It cannot kill itself, however."); }
}
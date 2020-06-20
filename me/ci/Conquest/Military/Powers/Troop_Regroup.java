package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.ArmyType;
import me.ci.Conquest.Military.PowerDetails;

public class Troop_Regroup implements PowerDetails{
	private static final int stamina = 1;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(plot.isKingdomPlot()){
				if(plot.getKingdom()==kingdom){
					if(plot.getBuilding() instanceof Army){
						final Army other = (Army)plot.getBuilding();
						if(!other.isNullified()){
							if(other!=troop){
								if(other.getArmyType()==ArmyType.TROOP){
									if(other.getLevel()==troop.getLevel()){
										if(other.getStamina()>=stamina){
											if(troop.getStamina()>=stamina){
												if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=1
														&&Math.abs(plot.getZ()-troop.getNorthWestCourner().getZ())<=1){
													if(other.getHp()<other.getMaxHp()){
														final int empty = other.getMaxHp()-other.getHp();
														if(empty>=troop.getHp()){
															if(troop.hasCommander()
																	||other.hasCommander())other.addCommander();
															if(troop.hasWizard()
																	||other.hasWizard())other.addWizard();
															other.setHp(other.getHp()+troop.getHp(), true);
															troop.delete(true, false);
															p.sendMessage(ChatColor.GREEN+"Units combined.");
															other.setStamina(other.getStamina()-stamina);
														}else{
															troop.setHp(troop.getHp()-empty, true);
															other.setHp(other.getMaxHp(), true);
															p.sendMessage(ChatColor.GREEN+"Troops regrouped.");
															troop.setStamina(troop.getStamina()-stamina);
															other.setStamina(other.getStamina()-stamina);
														}
													}else{
														p.sendMessage(ChatColor.RED+"The unit you are trying to regroup with already is too full to hold any more troops!");
														explain(p);
													}
												}else{
													p.sendMessage(ChatColor.RED+"This unit is too far away!");
													explain(p);
												}
											}else{
												p.sendMessage(ChatColor.RED+"The troop you are trying to combine does not have enough stamina to preform this action.");
												explain(p);
											}
										}else{
											p.sendMessage(ChatColor.RED+"The troop you are trying to combine with does not have enough stamina to preform this action.");
											explain(p);
										}
									}else{
										p.sendMessage(ChatColor.RED+"These two units are not the same level!");
										explain(p);
									}
								}else{
									p.sendMessage(ChatColor.RED+"This is not a troop unit!");
									explain(p);
								}
							}else{
								p.sendMessage(ChatColor.RED+"You cannot reform a troop unit with itself!");
								explain(p);
							}
						}else p.sendMessage(ChatColor.RED+"That troop is nullified! This means they cannot use their abilities anymore!");
					}else{
						p.sendMessage(ChatColor.RED+"This is not an army chunk!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"This is not your kingdom!");
					explain(p);
				}
			}else{
				if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=1
						&&Math.abs(plot.getZ()-troop.getNorthWestCourner().getZ())<=1){
					if(troop.getStamina()>=stamina){
						if(troop.getHp()>1){
							boolean extra = troop.getHp()%2==1;
							Army a = new Army(plot.getChunk(), kingdom, troop.getHp()/2, troop.getRealArmyType(), troop.getLevel());
							troop.setHp(troop.getHp()/2+(extra?1:0), true);
							p.sendMessage(ChatColor.GREEN+"A new unit has been formed.");
							troop.setStamina(troop.getStamina()-stamina);
							a.setStamina(troop.getStamina());
							if(troop.getRealArmyType()!=troop.getArmyType())a.transform(troop.getArmyType());
						}else{
							p.sendMessage(ChatColor.RED+"This troop just not have enough men to form a new unit!");
							explain(p);
						}
					}else{
						p.sendMessage(ChatColor.RED+"The troop you are trying to combine does not have enough stamina to preform this action.");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"This area is too far away!");
					explain(p);
				}
			}
		}else p.sendMessage(ChatColor.RED+"This troop is nullified! This means they cannot use their abilities anymore!");
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on another troop unit that is touching this one. Then as many of troops from this unit as possible will move into that unit. If there is enough room, the two units will be combined into a single unit. Both units must be troops types and must be the same level in order to work. Clicking on a touching wilderness chunk will split this unit in half and distribute the troops evenly between the two units."); }
}
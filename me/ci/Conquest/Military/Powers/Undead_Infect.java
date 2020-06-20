package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.ArmyFormation;
import me.ci.Conquest.Military.ArmyType;
import me.ci.Conquest.Military.PowerDetails;
import me.ci.Conquest.Misc.Direction;

public class Undead_Infect implements PowerDetails{
	private static final int stamina = 11;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				final Plot plot = PlotMg.getPlotAt(click.getChunk());
				if(plot.isKingdomPlot()){
					if(plot.getKingdom()!=kingdom){
						if(!plot.getKingdom().isRuins()){
							if(plot.getKingdom().isOwnerOnline()){
								if(plot.getBuilding() instanceof Army){
									if(plot.getBuilding().getHp()<troop.getHp()){
										troop.usePower(plot.getKingdom(), "Your unit was infected by the undead!");
										final Army army = (Army)plot.getBuilding();
										final int hp = army.getHp();
										final int stam = army.getStamina();
										final int lvl = army.getLevel();
										final ArmyFormation formation = army.getFormation();
										final Direction direction = army.getDirection();
										final long reverttime = army.reverttime;
										final long flytime = army.flytime;
										final long slaytime = army.slaytime;
										final long illusiontime = army.illusiontime;
										final long protecttime = army.protecttime;
										final long speartime = army.speartime;
										final long pillagetime = army.pillagetime;
										final Army protector = army.protector;
										final boolean nullified = army.nullified;
										final boolean wizard = army.wizard;
										final boolean commander = army.commander;
										army.delete(false, false);
										final Army a = new Army(plot.getChunk(), kingdom, hp, ArmyType.UNDEAD, lvl);
										a.stamina=stam;
										a.formation=formation;
										a.direction=direction;
										a.reverttime=reverttime;
										a.flytime=flytime;
										a.slaytime=slaytime;
										a.illusiontime=illusiontime;
										a.protecttime=protecttime;
										a.protector=protector;
										a.speartime=speartime;
										a.pillagetime=pillagetime;
										a.nullified=nullified;
										a.wizard=wizard;
										a.commander=commander;
										a.updateGraphics(false);
										troop.setStamina(troop.getStamina()-stamina);
										p.sendMessage(ChatColor.GREEN+"Unit infected.");
									}else{
										p.sendMessage(ChatColor.RED+"You can only attack weaker units!");
										explain(p);
									}
								}else{
									p.sendMessage(ChatColor.RED+"This is not a military unit!");
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
						p.sendMessage(ChatColor.RED+"You cannot attack your own kingdom!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"There is no kingdom here!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to infect other units!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullifed!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on a weaker enemy military unit and this unit will infect it. Only unit with less hp then this unit can be infected. Once infected, they will be yours to command."); }
}
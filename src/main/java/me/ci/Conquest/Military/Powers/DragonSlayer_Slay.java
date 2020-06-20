package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Military.PowerDetails;

public class DragonSlayer_Slay implements PowerDetails{
	private static final int stamina = 5;
	public void run(final Player p, final Location click, final Army troop){
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(!troop.isSlaying()){
					troop.slay();
					troop.setStamina(troop.getStamina()-stamina);
					p.sendMessage(ChatColor.GREEN+"This unit is now slaying dragon riders.");
				}else{
					p.sendMessage(ChatColor.RED+"This unit is already slaying dragon riders!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to slay dragon riders!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, simply click anywhere. For the next 3 minutes, all nearby dragon riders will slowly start taking damage."); }
}
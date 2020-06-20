package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Military.PowerDetails;

public class Pikeman_Spear implements PowerDetails{
	private static final int stamina = 4;
	public void run(final Player p, final Location click, final Army troop){
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(!troop.isInSpearMode()){
					troop.spearMode();
					troop.setStamina(troop.getStamina()-stamina);
					p.sendMessage(ChatColor.GREEN+"Spears ready.");
				}else{
					p.sendMessage(ChatColor.RED+"This unit is already in spear mode!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to enter spear mode!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, simply click anywhere. This unit will be placed into spear mode for the next 4 minutes. In this mode, all flank attacks are countered."); }
}
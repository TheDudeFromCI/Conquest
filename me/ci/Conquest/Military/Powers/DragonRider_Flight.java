package me.ci.Conquest.Military.Powers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Military.PowerDetails;

public class DragonRider_Flight implements PowerDetails{
	private static final int stamina = 10;
	public void run(final Player p, final Location click, final Army troop){
		if(!troop.isNullified()){
			if(troop.getStamina()>=stamina){
				if(!troop.isFlying()){
					troop.fly();
					troop.setStamina(troop.getStamina()-stamina);
					p.sendMessage(ChatColor.GREEN+"This unit is now flying.");
				}else{
					p.sendMessage(ChatColor.RED+"This unit is already flying!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to fly!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, simply click anywhere. For the next 5 minutes, terrain movement will not cost this unit any stamina."); }
}
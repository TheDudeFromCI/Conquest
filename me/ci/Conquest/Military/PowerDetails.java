package me.ci.Conquest.Military;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Conquest.Buildings.Main.Army;

public interface PowerDetails{
	public void run(final Player player, final Location click, final Army troop);
}
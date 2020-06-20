package me.ci.Conquest.Military;

import me.ci.Community.Plot;

import org.bukkit.entity.Player;

public interface Henchman{
	public boolean use(final Player p, final Plot plot);
	public void explain(final Player p);
}
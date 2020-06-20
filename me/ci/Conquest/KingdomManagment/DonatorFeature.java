package me.ci.Conquest.KingdomManagment;

import org.bukkit.entity.Player;

public interface DonatorFeature{
	public boolean canBuy(final Player p);
	public void buy(final Player p);
}
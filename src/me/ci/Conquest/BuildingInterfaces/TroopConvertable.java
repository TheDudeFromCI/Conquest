package me.ci.Conquest.BuildingInterfaces;

import org.bukkit.Chunk;

import me.ci.Conquest.Military.ArmyType;

public interface TroopConvertable{
	public boolean isConvertingTroops();
	public ArmyType getConvertTo();
	public void convert(int amount, Chunk to);
	public void removeTime(int i);
	public int getConvertAmount();
}
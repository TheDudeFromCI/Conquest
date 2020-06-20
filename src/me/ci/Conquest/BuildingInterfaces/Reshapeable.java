package me.ci.Conquest.BuildingInterfaces;

import me.ci.Conquest.Buildings.Constructors.BuildingType;

public interface Reshapeable{
	public boolean connectsTo(BuildingType type);
	public int getHeightVariance();
}
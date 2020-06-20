package me.ci.Conquest.BuildingInterfaces;

import me.ci.Conquest.Buildings.Constructors.ResourceType;

public interface ResourceHolder{
	public int[] getResourceLevels();
	public ResourceType[] getResources();
	public double[] getResourcePercents();
	public void setResource(ResourceType type, int amount);
	public int getResourceLevel(ResourceType type);
	public int getMaxResourceLevel();
	public String getResourcePool();
}
package me.ci.Conquest.BuildingInterfaces;

import me.ci.Conquest.Misc.ResearchType;

public interface Researcher{
	public boolean isResearching();
	public double getResearchPercent();
	public int getTimeLeft();
	public ResearchType getResearchType();
	public void setResearchType(ResearchType type);
}
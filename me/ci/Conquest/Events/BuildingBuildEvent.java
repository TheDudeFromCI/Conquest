package me.ci.Conquest.Events;

import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.KingdomManagment.Kingdom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuildingBuildEvent extends Event{
	private Building building;
	public BuildingBuildEvent(Building building){ this.building=building; }
	public Building getBuilding(){ return this.building; }
	public Kingdom getKingdom(){ return this.building.getKingdom(); }
	public Player getBuilder(){ return Bukkit.getPlayer(getKingdom().getOwner()); }
	public BuildingType getType(){ return this.building.getType(); }
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers(){ return handlers; }
	public static HandlerList getHandlerList(){ return handlers; }
}
package me.ci.Conquest.Events;

import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.KingdomManagment.Kingdom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuildingDestroyEvent extends Event{
	private Building building;
	private boolean fully;
	public BuildingDestroyEvent(Building building, boolean fully){
		this.building=building;
		this.fully=fully;
	}
	public Building getBuilding(){
		return this.building;
	}
	public Kingdom getKingdom(){
		return this.building.getKingdom();
	}
	public Player getBuilder(){
		return Bukkit.getPlayer(getKingdom().getOwner());
	}
	public BuildingType getType(){
		return this.building.getType();
	}
	public boolean isFullyDestoryed(){
		return this.fully;
	}
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers(){
		return handlers;
	}
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
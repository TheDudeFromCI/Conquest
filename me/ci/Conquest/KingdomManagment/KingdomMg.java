package me.ci.Conquest.KingdomManagment;

import java.util.LinkedList;
import java.util.List;

import me.ci.WhCommunity;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Community.Save;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class KingdomMg{
	private static LinkedList<Kingdom> kingdoms = new LinkedList<Kingdom>();
	public static Kingdom getKingdom(String name){
		if(WhCommunity.debug)WhCommunity.printDebug();
		for(Kingdom k : kingdoms){
			if(k.getName().equalsIgnoreCase(name))return k;
		}
		return null;
	}
	public static Building getBuilding(Chunk nwc){
		if(WhCommunity.debug)WhCommunity.printDebug();
		Plot plot = PlotMg.getPlotAt(nwc);
		if(plot.isKingdomPlot())return plot.getBuilding();
		return null;
	}
	public static Location getSpectateArea(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return new Location(Bukkit.getWorlds().get(0), 0, 100, 0);
	}
	public static Location getCreateFlagLocation(World w, byte id){
		if(id==0)return new Location(w, 71, 162, -4);
		if(id==1)return new Location(w, 64, 162, 0);
		if(id==2)return new Location(w, 60, 162, 7);
		if(id==3)return new Location(w, 64, 162, 15);
		return new Location(w, 71, 162, 19);
	}
	public static List<Kingdom> getKingdoms(){ return kingdoms; }
	public static void newKingdom(Kingdom kingdom){
		kingdoms.add(kingdom);
	}
	public static void removeKingdom(Kingdom kingdom){
		if(WhCommunity.debug)WhCommunity.printDebug();
		kingdoms.remove(kingdom);
	}
	public static void saveKingdoms(){
		for(Kingdom kingdom : kingdoms){ kingdom.save(); }
	}
	private static void loadKingdom(String name){
		newKingdom(new Kingdom(name));
	}
	public static void loadKingdoms(){
		for(String s : Save.getFileNames("Kingdoms")){
			loadKingdom(s);
		}
	}
}
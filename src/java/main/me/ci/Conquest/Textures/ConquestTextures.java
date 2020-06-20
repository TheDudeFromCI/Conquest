package me.ci.Conquest.Textures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ci.WhCommunity;
import me.ci.Community.Save;
import me.ci.Conquest.Buildings.Constructors.BuildingType;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class ConquestTextures{
	private static Map<String,Map<Integer,BuildingTexture>> builds = new HashMap<String,Map<Integer,BuildingTexture>>();
	public static void load(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(!WhCommunity.CONFIG_Save_Conquest_To_Ram)return;
		for(BuildingType type : BuildingType.values()){
			for(int l = 1; l<=type.getMaxLevel(); l++){
				try{
					load(type, l);
				}catch(Exception exception){}
			}
		}
	}
	private static void load(BuildingType type, int l){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String in = Save.get("Buildings", type.getName(), String.valueOf(l));
		if(in==null)return;
		Map<Integer,BuildingTexture> b;
		if(builds.containsKey(type.getName()))b=builds.get(type.getName());
		else b=new HashMap<Integer,BuildingTexture>();
		b.put(l, new BuildingTexture(in));
		builds.put(type.getName(), b);
	}
	public static void save(Chunk NWCourner, String building, int lvl){
		if(WhCommunity.debug)WhCommunity.printDebug();
		BuildingType type = BuildingType.getByName(building);
		save(NWCourner, type, lvl);
	}
	public static void save(Chunk NWCourner, BuildingType type, int lvl){
		if(WhCommunity.debug)WhCommunity.printDebug();
		ArrayList<Location> waypoints = new ArrayList<Location>();
		String code = "";
		Percent done = new Percent();
		double total = type.getLength()*type.getLength()*23040;
		for(int x = 0; x<type.getLength(); x++){
			for(int z = 0; z<type.getLength(); z++){
				Chunk c = NWCourner.getWorld().getChunkAt(x+NWCourner.getX(), z+NWCourner.getZ());
				code+=";"+saveChunk(c, total, done, lvl, type.getName(), waypoints, x, z);
			}
		}
		code=code.substring(1);
		Save.set("Buildings", type.getName(), String.valueOf(lvl), code);
		System.out.println("[Conquest] Finished Saving level "+lvl+" "+type.getName());
		String pointlist = "";
		for(Location l : waypoints){
			pointlist+=";"+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ();
		}
		while(pointlist.startsWith(";"))pointlist=pointlist.substring(1);
		Save.set("Resources", "Building Waypoints", type.getName(), pointlist);
		if(WhCommunity.CONFIG_Save_Conquest_To_Ram)load(type, lvl);
	}
	private static String saveChunk(Chunk chunk, double total, Percent done, int lvl, String name, List<Location> waypoints, int offchunkx, int offchunkz){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String out = "";
		for(int y = 0; y<90; y++){
			for(int x = 0; x<16; x++){
				for(int z = 0; z<16; z++){
					Block b = chunk.getBlock(x, y, z);
					if(b.getTypeId()==0)out+=",";
					else{
						if(b.getTypeId()==122){
							waypoints.add(new Location(chunk.getWorld(), offchunkx*16+x, y, offchunkz*16+z));
							out+=",";
						}else{
							if(b.getData()==0)out+=","+b.getTypeId();
							else out+=","+b.getTypeId()+"-"+b.getData();
						}
					}
					done.done++;
				}
			}
			int current = ((int)(done.done/total*100));
			if(current!=done.last){
				System.out.println("[WhCommunity] Saving level "+lvl+" "+name+"... ("+current+"%)");
				done.last=current;
			}
		}
		out=out.substring(1);
		return out;
	}
	public static BuildingTexture getTexture(BuildingType building, int lvl){
		return getTexture(building.getName(), lvl);
	}
	private static BuildingTexture getTexture(String building, int lvl){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(WhCommunity.CONFIG_Save_Conquest_To_Ram){
			if(builds.containsKey(building)){
				Map<Integer,BuildingTexture> build = builds.get(building);
				if(build.containsKey(lvl))return build.get(lvl);
		 	}
		}else{
			String in = Save.getOnce("Buildings", building, String.valueOf(lvl));
			if(in==null)return new BuildingTexture();
			return new BuildingTexture(in);
		}
		return new BuildingTexture();
	}
	private static class Percent{
		public double done;
		public int last;
	}
}
package me.ci.Conquest.Misc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.ci.Community.Save;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.EventProperties;

public class EventScript{
	private EventProperties props;
	private WraithavenScript script;
	private BuildingType[] buildingtypes;
	private HashMap<String,String> settings;
	private double spawn = -1;
	private final File file;
	public EventScript(final String name){ file=Save.getFile("Resources", "Events"+File.separatorChar+name+".whs"); }
	public EventScript(final File name){ file=name; }
	public void load(final EventProperties a){
		props=a;
		script=new WraithavenScript(file, props);
	}
	public EventProperties getProperties(){ return props; }
	public WraithavenScript getScript(){ return script; }
	public File getFile(){ return file; }
	public BuildingType[] getBuildingTypes(){
		if(buildingtypes==null){
			if(script==null){
				if(settings==null)settings=WraithavenScript.getSettings(file);
				if(settings.containsKey("BuildingTypes")){
					final String[] b = settings.get("BuildingTypes").split(",");
					buildingtypes=new BuildingType[b.length];
					for(int x = 0; x<b.length; x++){ buildingtypes[x]=BuildingType.getByName(b[x]); }
				}else buildingtypes=new BuildingType[0];
			}else{
				final String builds = script.getSetting("BuildingTypes");
				if(builds==null)buildingtypes=new BuildingType[0];
				else{
					final String[] b = builds.split(",");
					buildingtypes=new BuildingType[b.length];
					for(int x = 0; x<b.length; x++){ buildingtypes[x]=BuildingType.getByName(b[x]); }
				}
			}
		}
		return buildingtypes;
	}
	public double getSpawnChance(){
		if(spawn==-1){
			if(script==null){
				if(settings==null)settings=WraithavenScript.getSettings(file);
				if(settings.containsKey("EventChance"))spawn=Double.valueOf(settings.get("EventChance"));
				else spawn=0;
			}else{
				final String b = script.getSetting("EventChance");
				if(b==null)spawn=0;
				else spawn=Double.valueOf(b);
			}
		}
		return spawn;
	}
	public static ArrayList<EventScript> getEvents(){
		final ArrayList<EventScript> es = new ArrayList<>();
		for(File f : Save.getFiles("Resources"+File.separatorChar+"Events")){ es.add(new EventScript(f)); }
		return es;
	}
	public static void filterByBuildingType(final BuildingType type, ArrayList<EventScript> es){
		final ArrayList<EventScript> remove = new ArrayList<>();
		for(EventScript e : es){ if(!Arrays.asList(e.getBuildingTypes()).contains(type))remove.add(e); }
		for(EventScript e : remove){ es.remove(e); }
	}
}
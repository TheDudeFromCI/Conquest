package me.ci.Community;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class PermissionGroup{
	private final List<String> perms;
	private final String name;
	private final String world;
	private final String prefix;
	private final String suffix;
	public PermissionGroup(String name, String world, FileConfiguration config, int id, String prefix, String suffix){
		this.name=name;
		this.world=world;
		this.prefix=prefix;
		this.suffix=suffix;
		perms=new ArrayList<String>();
		int i = 1;
		while(config.contains("Worlds."+world+".PermissionGroups."+id)&&i<=id){
			for(String s : config.getStringList("Worlds."+world+".PermissionGroups."+i+".Permissions")){
				if(!perms.contains(s))perms.add(s);
			}
			i++;
		}
	}
	public PermissionGroup(String name, String world, String prefix, String suffix){
		this.name=name;
		this.world=world;
		this.prefix=prefix;
		this.suffix=suffix;
		perms=new ArrayList<String>();
	}
	public String getName(){
		return this.name;
	}
	public String getWorld(){
		return this.world;
	}
	public boolean hasPermissionNode(String node){
		return containsIgnoreCase(this.perms, node);
	}
	private boolean containsIgnoreCase(List<String> list, String string){
		for(String s : list){
			if(s.equalsIgnoreCase(string))return true;
		}
		return false;
	}
	public List<String> getPermissionNodes(){
		return this.perms;
	}
	public String getPrefix(){
		return this.prefix;
	}
	public String getSuffix(){
		return this.suffix;
	}
}
package me.ci.Community;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.configuration.file.FileConfiguration;

/**
 * Alls the settings for a specific world.
 * @author TheDudeFromCI
 */
public class WorldSettings{
	public final String world;
	public final boolean CONFIG_Fire_Spread;
	public final boolean CONFIG_Pvp_Global;
	public final boolean CONFIG_Always_Day;
	public final boolean CONFIG_Building_In_Wild;
	public final boolean CONFIG_Plot_Barrier_Liquid_Flow;
	public final int CONFIG_Plot_Cost;
	public final boolean CONFIG_Explosions;
	public final boolean CONFIG_Spawn_Mobs_Naturally_Hostile;
	public final boolean CONFIG_Spawn_Mobs_Naturally_Peaceful;
	public final boolean CONFIG_Spawn_Mobs_Plugin;
	public final int CONFIG_Plot_Rent;
	public final long CONFIG_Tax_Cooldown;
	public final boolean CONFIG_Plots;
	public final String CONFIG_Chat_Default_Layout;
	public final String CONFIG_Chat_Message_Layout;
	public final String CONFIG_Chat_Shout_Layout;
	public final String CONFIG_Chat_Whisper_Layout;
	public final String CONFIG_Chat_Action_Layout;
	public final int CONFIG_Chat_Default_Range;
	public final int CONFIG_Chat_Message_Range;
	public final int CONFIG_Chat_Shout_Range;
	public final int CONFIG_Chat_Whisper_Range;
	public final int CONFIG_Chat_Action_Range;
	public final int CONFIG_Chat_Channels_Limit;
	public final List<String> CONFIG_Auto_Messages;
	public final boolean CONFIG_Conquest_Enabled;
	public int automessageplace = 0;
	public List<Channel> channels = new ArrayList<Channel>();
	public List<Rank> ranks = new ArrayList<Rank>();
	public List<PermissionGroup> permissiongroups = new ArrayList<PermissionGroup>();
	public WorldSettings(String world, FileConfiguration config){
		this.world=world;
		String path = "Worlds."+world+".";
		if(!config.contains(path+"Auto_Messages"))config.set(path+"Auto_Messages", new ArrayList<String>());
		this.CONFIG_Fire_Spread=(boolean)get(path+"Fire_Spread", config, false);
		this.CONFIG_Pvp_Global=(boolean)get(path+"Pvp.Global", config, false);
		this.CONFIG_Always_Day=(boolean)get(path+"Always_Day", config, false);
		this.CONFIG_Building_In_Wild=(boolean)get(path+"Building_In_Wild", config, true);
		this.CONFIG_Plot_Barrier_Liquid_Flow=(boolean)get(path+"Plot_Barrier_Liquid_Flow", config, true);
		this.CONFIG_Plot_Cost=(int)get(path+"Plot_Cost", config, 500);
		this.CONFIG_Explosions=(boolean)get(path+"Explosions", config, false);
		this.CONFIG_Spawn_Mobs_Naturally_Hostile=(boolean)get(path+"Spawn_Mobs.Naturally.Hostile", config, false);
		this.CONFIG_Spawn_Mobs_Naturally_Peaceful=(boolean)get(path+"Spawn_Mobs.Naturally.Peaceful", config, false);
		this.CONFIG_Spawn_Mobs_Plugin=(boolean)get(path+"Spawn_Mobs.Plugin", config, true);
		this.CONFIG_Plot_Rent=(int)get(path+"Plot_Rent", config, 50);
		this.CONFIG_Tax_Cooldown=(int)get(path+"Tax_Cooldown", config, 1440)*60000;
		this.CONFIG_Plots=(boolean)get(path+"Plots", config, true);
		this.CONFIG_Chat_Default_Layout=(String)get(path+"Chat.Default.Layout", config, "&f[%pgroup%] %nick%: %message%");
		this.CONFIG_Chat_Default_Range=(int)get(path+"Chat.Default.Range", config, 100);
		this.CONFIG_Chat_Message_Layout=(String)get(path+"Chat.Message.Layout", config, "&8[%sender% -> %receiver%]&f: %message%");
		this.CONFIG_Chat_Message_Range=(int)get(path+"Chat.Message.Range", config, -1);
		this.CONFIG_Chat_Shout_Layout=(String)get(path+"Chat.Shout.Layout", config, "&f[Shout] %nick%: %message%!");
		this.CONFIG_Chat_Shout_Range=(int)get(path+"Chat.Shout.Range", config, -1);
		this.CONFIG_Chat_Whisper_Layout=(String)get(path+"Chat.Whisper.Layout", config, "&f[Whisper] %nick%: %message%...");
		this.CONFIG_Chat_Whisper_Range=(int)get(path+"Chat.Whisper.Range", config, 10);
		this.CONFIG_Chat_Action_Layout=(String)get(path+"Chat.Action.Layout", config, "&f* %nick% %message%");
		this.CONFIG_Chat_Action_Range=(int)get(path+"Chat.Action.Range", config, 100);
		this.CONFIG_Chat_Channels_Limit=(int)get(path+"Chat.Channels.Limit", config, 15)+1;
		this.CONFIG_Auto_Messages=config.getStringList(path+"Auto_Messages");
		this.CONFIG_Conquest_Enabled=(boolean)get(path+"Conquest.Enabled", config, false);
		loadChannels(config);
		loadRanks(config, path);
		loadPermissions(config);
	}
	public Object get(String path, FileConfiguration config, Object def){
		if(!config.contains(path)){
			config.set(path, def);
			return def;
		}
		return config.get(path);
	}
	public Channel getChannelById(int id){
		for(Channel c : this.channels){
			if(c.getId()==id)return c;
		}
		return null;
	}
	public void loadChannels(FileConfiguration config){
		for(int i = 0; i<this.CONFIG_Chat_Channels_Limit; i++){
			String name = config.getString("Worlds."+world+".Chat.Channels.Names."+i);
			this.channels.add(new Channel(i, name==null?String.valueOf(i):name));
		}
	}
	public String getNextAutoMessage(){
		if(!this.CONFIG_Auto_Messages.isEmpty()){
			String m = this.CONFIG_Auto_Messages.get(this.automessageplace);
			this.automessageplace++;
			if(this.automessageplace==this.CONFIG_Auto_Messages.size())this.automessageplace=0;
			return m;
		}
		return null;
	}
	public void loadRanks(FileConfiguration config, String path){
		int i = 1;
		while(true){
			if(!config.contains(path+"Ranks."+i))break;
			Rank rank = new Rank(config, path+"Ranks."+i, i);
			if(rank.loaded())this.ranks.add(rank);
			i++;
		}
	}
	public Rank getNextRank(String current){
		for(Rank rank : this.ranks){
			if(rank.from.equalsIgnoreCase(current))return rank;
		}
		return null;
	}
	private void loadPermissions(FileConfiguration config){
		String path = "Worlds."+world+".PermissionGroups.";
		int i = 1;
		while(config.contains(path+i)){
			this.permissiongroups.add(new PermissionGroup((String)get(path+i+".Name", config, "Group "+i), this.world, config, i, (String)get(path+i+".Prefix", config, ""), (String)get(path+i+".Suffix", config, "")));
			i++;
		}
		if(i==1){
			this.permissiongroups.add(new PermissionGroup("Default", this.world, "Default", ""));
			config.set(path+"1.Name", "Default");
			config.set(path+"1.Prefix", "");
			config.set(path+"1.Suffix", "");
			config.set(path+"1.Permissions", new ArrayList<String>());
		}
	}
	public PermissionGroup getDefaultPermissionsGroup(){
		return this.permissiongroups.get(0);
	}
	public PermissionGroup getPermissionGroup(String name){
		for(PermissionGroup group : this.permissiongroups){
			if(group.getName().equalsIgnoreCase(name))return group;
		}
		return getDefaultPermissionsGroup();
	}
	public class Rank{
		public String from;
		public String to;
		public int minutes;
		public final int id;
		public Rank(FileConfiguration config, String path, int id){
			this.id=id;
			try{
				if(config.contains(path+".From"))from=config.getString(path+".From");
				if(config.contains(path+".To"))to=config.getString(path+".To");
				if(config.contains(path+".MinutesActive"))minutes=config.getInt(path+".MinutesActive");
			}catch(Exception exception){}
		}
		public boolean loaded(){
			if(from==null)return false;
			if(to==null)return false;
			if(minutes==0)return false;
			return true;
		}
	}
}
package me.ci;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ci.Community.AutobuyCommand;
import me.ci.Community.CplotCommand;
import me.ci.Community.GmCommand;
import me.ci.Community.HomeCommand;
import me.ci.Community.MeCommand;
import me.ci.Community.MiscEvents;
import me.ci.Community.MsgCommand;
import me.ci.Community.NicknameCommand;
import me.ci.Community.PlayerMg;
import me.ci.Community.PlotCommand;
import me.ci.Community.PvpCommand;
import me.ci.Community.RankCommand;
import me.ci.Community.Save;
import me.ci.Community.SetgroupCommand;
import me.ci.Community.SethomeCommand;
import me.ci.Community.ShoutCommand;
import me.ci.Community.TimeKeeper;
import me.ci.Community.WhCommunityListeners;
import me.ci.Community.WhisperCommand;
import me.ci.Community.WorldSettings;
import me.ci.Conquest.BuildingInterfaces.TroopConvertable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.Flag;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomCommand;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import me.ci.Conquest.Misc.ConquestTimer;
import me.ci.Conquest.Misc.WikiTipPage;
import me.ci.Conquest.Textures.ConquestTextures;
import me.ci.Console.Console;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class WhCommunity extends JavaPlugin{
	public static File folder;
	private static List<WorldSettings> CONFIG_Worlds;
	public static boolean CONFIG_Pvp_Player_Can_Toggle;
	public static int CONFIG_Pvp_Toggle_Cooldown;
	public static int CONFIG_Max_Plots;
	public static String CONFIG_Whitelist_Message;
	public static boolean CONFIG_Local_Chat;
	public static int CONFIG_Auto_Message_Delay;
	public static String CONFIG_Auto_Message_name;
	public static int CONFIG_Restart_Minutes;
	public static boolean CONFIG_Save_Conquest_To_Ram;
	public static Economy economy;
    public static Permission permission;
    public static Chat chat;
    public static FileConfiguration config;
    public static boolean SHUTTING_DOWN = false;
    public static int threads = 0;
    public static Plugin plugin;
    public static boolean debug = false;
    private static File debuglog;
    private static File bugreports;
    private static boolean crash = false;
    public static boolean GRAPHICS_FIX = false;
    public static HashMap<Integer,WikiTipPage> wikitips = new HashMap<>();
    public static HashMap<String,HashMap<String,String>> helptool = new HashMap<>();
	public void onEnable(){
		System.out.println("[WhCommunity] Enabling plugin...");
		plugin=this;
		try{
			WhCommunity.folder=this.getDataFolder();
			debuglog=new File(WhCommunity.folder.getAbsolutePath()+File.separatorChar+"Debug", "Debug Log.log");
			if(!debuglog.exists()){
				debuglog.getParentFile().mkdirs();
				debuglog.createNewFile();
			}
			bugreports=new File(WhCommunity.folder.getAbsolutePath()+File.separatorChar+"Debug", "Bug Reports.log");
			if(!bugreports.exists()){
				bugreports.getParentFile().mkdirs();
				bugreports.createNewFile();
			}
			try{
				try{
					this.getConfig().load(new File(this.getDataFolder(), "config.yml"));
				}catch(FileNotFoundException e){
					System.out.println("[WhCommunity] Config not found, generating it now.");
					this.saveDefaultConfig();
				}
				generateConfig();
				loadConfig();
			}catch(Exception e){
				System.err.println("[WhCommunity] And error has been detected in the config! Reseting values.");
				resetConfig();
				loadConfig();
			}
			MiscEvents.Link(this);
			WhCommunity.config=this.getConfig();
			this.getServer().getPluginManager().registerEvents(new WhCommunityListeners(), this);
			this.getCommand("plot").setExecutor(new PlotCommand());
			this.getCommand("pvp").setExecutor(new PvpCommand());
			this.getCommand("shout").setExecutor(new ShoutCommand());
			this.getCommand("whisper").setExecutor(new WhisperCommand());
			this.getCommand("msg").setExecutor(new MsgCommand());
			this.getCommand("me").setExecutor(new MeCommand());
			this.getCommand("autobuy").setExecutor(new AutobuyCommand());
			this.getCommand("rank").setExecutor(new RankCommand());
			this.getCommand("home").setExecutor(new HomeCommand());
			this.getCommand("sethome").setExecutor(new SethomeCommand());
			this.getCommand("cplot").setExecutor(new CplotCommand());
			this.getCommand("gm").setExecutor(new GmCommand());
			this.getCommand("setgroup").setExecutor(new SetgroupCommand());
			this.getCommand("nickname").setExecutor(new NicknameCommand());
			this.getCommand("kingdom").setExecutor(new KingdomCommand());
			if(setupEconomy())System.out.println("[WhCommunity] Successfully linked to Vault Economy!");
			else System.out.println("[WhCommunity] Could not link to Vault Economy! Is an economy plugin installed?");
			if(setupPermissions())System.out.println("[WhCommunity] Successfully linked to Vault Permissions!");
			else System.out.println("[WhCommunity] Could not link to Vault Permissions! Is a permissions plugin installed?");
			if(setupChat())System.out.println("[WhCommunity] Successfully linked to Vault Chat!");
			else System.out.println("[WhCommunity] Could not link to Vault Chat! Is a chat plugin installed?");
			TimeKeeper.start();
			Save.startSaveTimer();
			ConquestTextures.load();
			KingdomMg.loadKingdoms();
			ConquestTimer.start();
			WraithavenScript.loadGlobleClasses();
			Army.startTimer();
		}catch(Exception e){
			System.err.println("[WhCommunity] Error trying to load plugin!");
			e.printStackTrace();
		}
		for(World w : Bukkit.getWorlds()){
			if(getWorldSettings(w.getName()).CONFIG_Conquest_Enabled){
				new Flag(new Byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).draw(KingdomMg.getCreateFlagLocation(w, (byte)0).getBlock());
				new Flag(new Byte[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).draw(KingdomMg.getCreateFlagLocation(w, (byte)1).getBlock());
				new Flag(new Byte[]{2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).draw(KingdomMg.getCreateFlagLocation(w, (byte)2).getBlock());
				new Flag(new Byte[]{3, 0, 0, 0, 0, 0, 0, 0, 0, 0}).draw(KingdomMg.getCreateFlagLocation(w, (byte)3).getBlock());
				new Flag(new Byte[]{4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).draw(KingdomMg.getCreateFlagLocation(w, (byte)4).getBlock());
			}
		}
		final Thread serverthread = Thread.currentThread();
		Bukkit.getScheduler().runTaskTimer(this, new Runnable(){
			long loops = 0;
			public void run(){
				try{
					final Player[] players = Bukkit.getOnlinePlayers();
					final long time = System.currentTimeMillis();
					for(Player p : players){
						if(PlayerMg.getExtraLandTime(p.getName())<=time){
							p.sendMessage(ChatColor.DARK_PURPLE+"Congragulations! You have recived an extra point towards your account land max. Your next point shall be given in 8 hours if you log in, or remain logged in during the time.");
							p.sendMessage(ChatColor.DARK_PURPLE+"Your permanent land max is now "+PlayerMg.getLandMax(p.getName(), 1)+".");
							p.sendMessage(ChatColor.DARK_PURPLE+"Your buffer land max is now "+PlayerMg.getLandMax(p.getName(), 0)+".");
							p.sendMessage(ChatColor.DARK_PURPLE+"Your total land max is now "+PlayerMg.getLandMax(p.getName(), 2)+".");
							PlayerMg.increasedLandMax(p.getName(), 3, true);
							PlayerMg.setExtraLandTime(p.getName());
						}
						try{
							if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Conquest_Enabled){
								if(loops%2==0)p.setLevel(Math.max(p.getLevel()-1, 0));
								Kingdom kingdom = PlayerMg.getKingdom(p.getName());
								if(kingdom!=null){
									if(loops%10==0){
										p.setExp(kingdom.getMorale()/100.0f);
										int food = kingdom.getResourceLevels(ResourceType.FOOD)-kingdom.getVillagers(true);
										int maxfood = kingdom.getMaxResourceLevel(ResourceType.FOOD)-kingdom.getVillagers(true);
										try{ p.setFoodLevel(food/(maxfood/20));
										}catch(Exception exception){}
									}
									TroopConvertable con;
									final List<Building> b1 = new ArrayList<>(Building.getBuildings(kingdom));
									synchronized(b1){
										for(Building b : b1){
											if(b instanceof TroopConvertable){
												con=(TroopConvertable)b;
												if(con.isConvertingTroops()
														&&b.isAlive())con.removeTime(1);
											}
										}
									}
								}else{
									p.setExp(0.0f);
									p.setHealth(20.0);
									p.setFoodLevel(20);
								}
							}
						}catch(Exception exception){ exception.printStackTrace(); }
					}
					if(loops%2==0){
						String days = Save.get("Resources", "Timer Storage", "Conquest Days");
						String hours = Save.get("Resources", "Timer Storage", "Conquest Hours");
						if(days==null)days="0";
						if(hours==null)hours="60";
						int d = Integer.valueOf(days);
						int h = Integer.valueOf(hours);
						h--;
						PlayerMg.updateDefaultScoreboard(d, h);
						Save.set("Resources", "Timer Storage", "Conquest Hours", String.valueOf(h));
					}
				}catch(final Exception exception){ exception.printStackTrace(); }
				if(!serverthread.isAlive()){
					crash=true;
					Bukkit.shutdown();
				}
				loops++;
				if(System.currentTimeMillis()>=1373925600000L){
					Bukkit.setWhitelist(false);
					System.out.println("WATCH OUT!!!!!!!!!!! WE'RE OPEN!");
					System.out.println("NOOB FEST!");
				}
			}
		}, 10, 10);
		final File wikifolder = Save.getAbsoluteFile("Resources", "Wiki");
		if(wikifolder!=null){
			final File[] wikis = wikifolder.listFiles();
			for(int i = 0; i<wikis.length; i++)wikitips.put(i, new WikiTipPage(i, wikis[i]));
		}
		final Map<String,String> htc = Save.getAllPaths("Resources", "Help Tool");
		String link;
		String[] cats;
		HashMap<String,String> links;
		for(String s : htc.keySet()){
			link=htc.get(s);
			cats=s.split("-");
			if(helptool.containsKey(cats[0]))links=helptool.get(cats[0]);
			else links=new HashMap<>();
			links.put(cats[1], link);
			helptool.put(cats[0], links);
		}
		new Console();
		System.out.println("[WhCommunity] Enabled!");
	}
	public void onDisable(){
		if(WhCommunity.SHUTTING_DOWN)return;
		System.out.println("[WhCommunity] Disabling plugin...");
		System.out.println("[WhCommunity] Kicking players...");
		Player[] players = Bukkit.getOnlinePlayers();
		for(Player p : players.clone()){
			if(crash)p.kickPlayer("The server has experienced a sudden crash, restarting server now. Please relog in about 30 seconds.");
			else p.kickPlayer("Server is restarting, please relog in about 30 seconds.");
		}
		SHUTTING_DOWN=true;
		System.out.println("[WhCommunity] Stopping timers...");
		Army.stopTimer();
		ConquestTimer.stop();
		TimeKeeper.stop();
		System.out.println("[WhCommunity] Saving files...");
		WraithavenScript.saveGlobleClasses();
		saveConfig();
		Save.safeShutDown();
		System.out.println("[WhCommunity] Finishing builds...");
		int wait = 0;
		do{
			try{ Thread.sleep(250);
			}catch(Exception exception){}
			wait++;
		}while(threads>0&&wait<120);
		System.out.println("[WhCommunity] Disabled!");
	}
	public void generateConfig() throws IOException{
		WhCommunity.CONFIG_Worlds=new ArrayList<WorldSettings>();
		for(World w : Bukkit.getWorlds()){
			WhCommunity.CONFIG_Worlds.add(new WorldSettings(w.getName(), this.getConfig()));
		}
		if(!this.getConfig().contains("Pvp.Player_Can_Toggle"))this.getConfig().set("Pvp.Player_Can_Toggle", false);
		if(!this.getConfig().contains("Pvp.Toggle_Cooldown"))this.getConfig().set("Pvp.Toggle_Cooldown", 120);
		if(!this.getConfig().contains("Max_Plots"))this.getConfig().set("Max_Plots", -1);
		if(!this.getConfig().contains("Whitelist_Message"))this.getConfig().set("Whitelist_Message", "&7Server Whitelisted");
		if(!this.getConfig().contains("Local_Chat"))this.getConfig().set("Local_Chat", true);
		if(!this.getConfig().contains("Auto_Message.Name"))this.getConfig().set("Auto_Message.Name", "Auto Message");
		if(!this.getConfig().contains("Auto_Message.Delay"))this.getConfig().set("Auto_Message.Delay", 120);
		if(!this.getConfig().contains("Restart_Minutes"))this.getConfig().set("Restart_Minutes", 240);
          if(!this.getConfig().contains("Save_Conquest_To_Ram"))this.getConfig().set("Save_Conquest_To_Ram", true);
		this.saveConfig();
	}
	public void loadConfig(){
		WhCommunity.CONFIG_Worlds=new ArrayList<WorldSettings>();
		for(World w : Bukkit.getWorlds()){
			WhCommunity.CONFIG_Worlds.add(new WorldSettings(w.getName(), this.getConfig()));
		}
		WhCommunity.CONFIG_Pvp_Player_Can_Toggle=this.getConfig().getBoolean("Pvp.Player_Can_Toggle");
		WhCommunity.CONFIG_Pvp_Toggle_Cooldown=this.getConfig().getInt("Pvp.Toggle_Cooldown");
		WhCommunity.CONFIG_Max_Plots=this.getConfig().getInt("Max_Plots");
		WhCommunity.CONFIG_Whitelist_Message=this.getConfig().getString("Whitelist_Message");
		WhCommunity.CONFIG_Local_Chat=this.getConfig().getBoolean("Local_Chat");
		WhCommunity.CONFIG_Auto_Message_name=this.getConfig().getString("Auto_Message.Name");
		WhCommunity.CONFIG_Auto_Message_Delay=this.getConfig().getInt("Auto_Message.Delay");
		WhCommunity.CONFIG_Restart_Minutes=this.getConfig().getInt("Restart_Minutes");
          WhCommunity.CONFIG_Save_Conquest_To_Ram=this.getConfig().getBoolean("Save_Conquest_To_Ram");
	}
	public void resetConfig(){
		WhCommunity.CONFIG_Worlds=new ArrayList<WorldSettings>();
		for(World w : Bukkit.getWorlds()){
			WhCommunity.CONFIG_Worlds.add(new WorldSettings(w.getName(), this.getConfig()));
		}
		this.getConfig().set("Pvp.Player_Can_Toggle", false);
		this.getConfig().set("Max_Plots", -1);
		this.getConfig().set("Pvp.Toggle_Cooldown", 120);
		this.getConfig().set("Whitelist_Message", "&7Server Whitelisted");
		this.getConfig().set("Local_Chat", true);
		this.getConfig().set("Auto_Message.Name", "Auto Message");
		this.getConfig().set("Auto_Message.Delay", 120);
		this.getConfig().set("Restart_Minutes", 240);
          this.getConfig().set("Save_Conquest_To_Ram", true);
		this.saveConfig();
	}
	public boolean setupEconomy(){
		try{
			if(this.getServer().getPluginManager().getPlugin("Vault")!=null){
				RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
				if(economyProvider!=null)economy=economyProvider.getProvider();
				return economy!=null;
			}else return false;
		}catch(Exception e){
			return false;
		}
	}
	public static WorldSettings getWorldSettings(String world){
		for(WorldSettings settings : WhCommunity.CONFIG_Worlds){
			if(settings.world.equals(world))return settings;
		}
		return null;
	}
	public static List<WorldSettings> getWorldSettings(){
		return WhCommunity.CONFIG_Worlds;
	}
    private boolean setupPermissions(){
		try{
			if(this.getServer().getPluginManager().getPlugin("Vault")!=null){
		        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		        if(permissionProvider!=null)permission=permissionProvider.getProvider();
		        return (permission!=null);
			}else return false;
		}catch(Exception e){
			return false;
		}
    }
    private boolean setupChat(){
		try{
			if(this.getServer().getPluginManager().getPlugin("Vault")!=null){
		        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		        if(chatProvider!=null)chat=chatProvider.getProvider();
		        return(chat!=null);
			}else return false;
		}catch(Exception e){
			return false;
		}
    }
    public static boolean moneyEnabled(){
    	return WhCommunity.economy!=null;
    }
    public static void takeMoney(String player, double amount){
    	WhCommunity.economy.withdrawPlayer(player, amount);
    }
    public static void giveMoney(String player, double amount){
    	WhCommunity.economy.depositPlayer(player, amount);
    }
    public static boolean hasMoney(String player, double amount){
    	return WhCommunity.economy.getBalance(player)>=amount;
    }
    public static void printDebug(){
    	try{
        	PrintWriter writer = new PrintWriter(new FileWriter(debuglog, true));
        	Thread t = Thread.currentThread();
        	writer.println("["+new Date(System.currentTimeMillis()).toString()+"] "+t.getName()+" = "+t.getStackTrace()[2].toString());
        	writer.close();
    	}catch(final Exception exception){ exception.printStackTrace(); }
    }
    public static void reportBug(final String message){
    	try{
        	PrintWriter writer = new PrintWriter(new FileWriter(bugreports, true));
        	writer.println("["+new Date(System.currentTimeMillis()).toString()+"] "+message);
        	writer.close();
    	}catch(final Exception exception){ exception.printStackTrace(); }
    }
}
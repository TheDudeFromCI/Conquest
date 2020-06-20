package me.ci.Community;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.ci.WhCommunity;
import me.ci.Conquest.BuildingInterfaces.ResourceHolder;
import me.ci.Conquest.BuildingInterfaces.VillagerWorkable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Buildings.Main.Cottage;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import me.ci.Conquest.Misc.ChunkCords;
import me.ci.Conquest.Misc.Tool;
import me.ci.Conquest.Misc.WikiTipPage;
import me.ci.Conquest.Misc.WikiTipPage.WikiTip;
import net.minecraft.server.v1_6_R2.Packet;
import net.minecraft.server.v1_6_R2.Packet206SetScoreboardObjective;
import net.minecraft.server.v1_6_R2.Packet207SetScoreboardScore;
import net.minecraft.server.v1_6_R2.Packet208SetScoreboardDisplayObjective;
import net.minecraft.server.v1_6_R2.Scoreboard;
import net.minecraft.server.v1_6_R2.ScoreboardBaseCriteria;
import net.minecraft.server.v1_6_R2.ScoreboardScore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerMg{
	public static final int BUILD_BUILDING = 3;
	public static final int DESTROY_BUILDING = 3;
	public static final int REPAIR_BUILDING = 3;
	public static final int UPGRADE_BUILDING = 3;
	public static final int SCOUT = 3;
	public static final int COLLECT_RESOURCES = 3;
	public static final int UPDATE = 8;
	public static final int USE_MAP = 15;
	public static final int REFORM_ARMY = 0;
	public static final int MOVE_ARMY = 3;
	public static final int CHANGE_HEIGHT = 1;
	public static HashMap<String,Integer> invpage = new HashMap<>();
	public static HashMap<String,Integer> invscroll = new HashMap<>();
	private static HashMap<String,HashMap<String,Integer>> channels = new HashMap<>();
	private static HashMap<String,Scoreboard> scores = new HashMap<>();
	public static boolean hasBuildPermission(Location l, Player p){
		if(p.isOp())return true;
		WorldSettings settings = WhCommunity.getWorldSettings(p.getWorld().getName());
		if(settings.CONFIG_Plots){
			Plot plot = PlotMg.getPlotAt(l.getChunk());
			if(plot.isClaimed()){
				if(p.hasPermission("whc.build.plot"))return true;
				return plot.canBuildHere(p.getName());
			}
		}
		if(settings.CONFIG_Building_In_Wild)return true;
		if(p.hasPermission("whc.build.wild"))return true;
		return false;
	}
	public static boolean hasPvpEnabled(Player p){
		String in = Save.get("Players", p.getName(), "Pvp");
		if(in==null)return false;
		return Boolean.valueOf(in);
	}
	public static boolean canRentMorePlots(Player p){
		if(WhCommunity.CONFIG_Max_Plots==-1)return true;
		String in = Save.get("Players", p.getName(), "Plot Count");
		if(in==null)return true;
		return Integer.valueOf(in)<WhCommunity.CONFIG_Max_Plots;
	}
	public static void claimPlot(Player p){
		String in = Save.get("Players", p.getName(), "Plot Count");
		if(in==null)Save.set("Players", p.getName(), "Plot Count", "1");
		else Save.set("Players", p.getName(), "Plot Count", String.valueOf(Integer.valueOf(in)+1));
	}
	public static void unclaimPlot(Player p){
		String in = Save.get("Players", p.getName(), "Plot Count");
		if(in==null)Save.set("Players", p.getName(), "Plot Count", "0");
		else Save.set("Players", p.getName(), "Plot Count", String.valueOf(Integer.valueOf(in)-1));
	}
	public static void setPvpEnable(Player p, boolean enabled){
		Save.set("Players", p.getName(), "Pvp", String.valueOf(enabled));
	}
	public static void createChunkGrid(final Player p, final Plot plot, final boolean message){
		new Thread(new Runnable(){
			public void run(){
				try{
					Location l = p.getLocation();
					int y = l.getBlockY()+15;
					if(y<255){
						byte color = 14;
						if(plot.isClaimed()){
							if(plot.getOwner().equals(p.getName()))color=5;
							else color=14;
						}else if(plot.isStreet())color=3;
						else color=7;
						Chunk c = l.getWorld().getChunkAt(plot.getX(), plot.getZ());
						int cxl = c.getBlock(0, 0, 0).getX();
						int cxh = c.getBlock(15, 0, 15).getX();
						int czl = c.getBlock(0, 0, 0).getZ();
						int czh = c.getBlock(15, 0, 15).getZ();
						World w = p.getWorld();
						for(int x = cxl; x<=cxh; x++){
							for(int z = czl; z<=czh; z++){
								Location loc = new Location(w, x, y, z);
								if(x==cxl||x==cxh||z==czl||z==czh)p.sendBlockChange(loc, 35, color);
								else p.sendBlockChange(loc, 20, (byte)0);
							}
						}
						if(message)p.sendMessage(ChatColor.DARK_AQUA+"A fake chunk grid has been created in your chunk. Relog, or leave the area to get rid of it.");
					}else if(message)p.sendMessage(ChatColor.RED+"You are too high up! A chunk grid could not be created.");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	public static void createChunkGrid(final Player p){
		new Thread(new Runnable(){
			public void run(){
				try{
					Location l = p.getLocation();
					int y = l.getBlockY()+15;
					if(y<255){
						for(int x1 = l.getChunk().getX()-1; x1<=l.getChunk().getX()+1; x1++){
							for(int z1 = l.getChunk().getZ()-1; z1<=l.getChunk().getZ()+1; z1++){
								byte color = 14;
								Plot plot = PlotMg.getPlotAt(l.getWorld(), x1, z1);
								if(plot.isClaimed()){
									if(plot.getOwner().equals(p.getName()))color=5;
									else color=14;
								}else if(plot.isStreet())color=3;
								else color=7;
								Chunk c = l.getWorld().getChunkAt(x1, z1);
								int cxl = c.getBlock(0, 0, 0).getX();
								int cxh = c.getBlock(15, 0, 15).getX();
								int czl = c.getBlock(0, 0, 0).getZ();
								int czh = c.getBlock(15, 0, 15).getZ();
								World w = p.getWorld();
								for(int x = cxl; x<=cxh; x++){
									for(int z = czl; z<=czh; z++){
										Location loc = new Location(w, x, y, z);
										if(x==cxl||x==cxh||z==czl||z==czh)p.sendBlockChange(loc, 35, color);
										else p.sendBlockChange(loc, 20, (byte)0);
									}
								}
							}
						}
						p.sendMessage(ChatColor.DARK_AQUA+"A fake chunk grid has been created in your chunk. Relog, or leave the area to get rid of it.");
					}else p.sendMessage(ChatColor.RED+"You are too high up! A chunk grid could not be created.");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	public static Channel getChannel(Player p){
		try{
			ItemStack item = p.getItemInHand();
			if(item==null||item.getTypeId()!=Material.EYE_OF_ENDER.getId())return WhCommunity.getWorldSettings(p.getWorld().getName()).getChannelById(0);
			int id = 0;
			HashMap<String,Integer> c;
			if(PlayerMg.channels.containsKey(p.getName()))c=PlayerMg.channels.get(p.getName());
			else c=new HashMap<String,Integer>();
			id=c.get(p.getWorld().getName());
			return WhCommunity.getWorldSettings(p.getWorld().getName()).getChannelById(id);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static void setChannel(Player p, int id){
		HashMap<String,Integer> c;
		if(PlayerMg.channels.containsKey(p.getName()))c=PlayerMg.channels.get(p.getName());
		else c=new HashMap<String,Integer>();
		c.put(p.getWorld().getName(), id);
		PlayerMg.channels.put(p.getName(), c);
	}
	public static Location getHome(String p){
		try{
			String in = Save.get("Players", p, "Home");
			if(in==null)return null;
			double x = 0;
			double y = 0;
			double z = 0;
			float yaw = 0;
			float pitch = 0;
			String[] parts = in.split(",");
			x=Double.valueOf(parts[1]);
			y=Double.valueOf(parts[2]);
			z=Double.valueOf(parts[3]);
			yaw=Float.valueOf(parts[4]);
			pitch=Float.valueOf(parts[5]);
			return new Location(Bukkit.getWorld(parts[0]), x, y, z, yaw, pitch);
		}catch(Exception exception){
			return null;
		}
	}
	public static void setHome(String p, Location l){
		String out = l.getWorld().getName();
		out+=","+l.getX();
		out+=","+l.getY();
		out+=","+l.getZ();
		out+=","+l.getYaw();
		out+=","+l.getPitch();
		Save.set("Players", p, "Home", out);
	}
	public static Kingdom getKingdom(String p){
		String in = Save.get("Players", p, "Kingdom");
		if(in==null)return null;
		return KingdomMg.getKingdom(in);
	}
	public static PermissionGroup getPermissionGroup(Player p){
		WorldSettings settings = WhCommunity.getWorldSettings(p.getWorld().getName());
		String in = Save.get("Players", p.getName(), "P Group-"+p.getWorld().getName());
		if(in==null)return settings.getDefaultPermissionsGroup();
		return settings.getPermissionGroup(in);
	}
	public static boolean hasPermission(Player p, String node){
		if(p.isOp())return true;
		return getPermissionGroup(p).hasPermissionNode(node);
	}
	public static void setPermissionGroup(Player p, String group){
		Save.set("Players", p.getName(), "P Group-"+p.getWorld().getName(), group);
		MiscEvents.reloadPermissions(p);
	}
	public static void setPermissionGroup(Player p, PermissionGroup group){
		Save.set("Players", p.getName(), "P Group-"+p.getWorld().getName(), group.getName());
		MiscEvents.reloadPermissions(p);
	}
	public static String getPrefix(Player p){
		if(WhCommunity.chat!=null)return WhCommunity.chat.getPlayerPrefix(p);
		String s = getPermissionGroup(p).getPrefix();
		s=ChatColor.translateAlternateColorCodes('&', s);
		return s;
	}
	public static String getSuffix(Player p){
		if(WhCommunity.chat!=null)return WhCommunity.chat.getPlayerSuffix(p);
		String s = getPermissionGroup(p).getSuffix();
		s=ChatColor.translateAlternateColorCodes('&', s);
		return s;
	}
	public static String getNickname(String p){
		String in = Save.get("Players", p, "Nickname");
		if(in==null)return p;
		return in;
	}
	public static void setNickname(String p, String name){
		Save.set("Players", p, "Nickname", name);
	}
	public static void setKingdom(String p, String kingdom){
		if(p==null)return;
		Save.set("Players", p, "Kingdom", kingdom);
		Player p1 = Bukkit.getPlayer(p); 
		try{
			if(kingdom==null){
				PlayerMg.setSelectedArmy(p, null);
				p1.kickPlayer("Game Over, please relog to play again.");
			}
		}catch(Exception exception){ exception.printStackTrace(); }
	}
	public synchronized static void resetConquestToolkit(Player p){
		if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Conquest_Enabled){
			PlayerInventory i = p.getInventory();
			i.clear();
			i.setMaxStackSize(100);
			ItemStack item;
			ItemMeta meta;
			final Kingdom kingdom = getKingdom(p.getName());
			if(!invscroll.containsKey(p.getName()))invscroll.put(p.getName(), 0);
			int scroll = invscroll.get(p.getName());
			if(scroll==0
					&&kingdom==null)scroll=3;
			else if(scroll==1
					&&kingdom==null)scroll=4;
			final ArrayList<Tool> tools = new ArrayList<>();
			tools.add(new Tool(Material.PAPER, "Info Tool", p.getName(), 'A', 1, "Click on areas to get information on", "the area you clicked."));
			tools.add(new Tool(Material.LEATHER_BOOTS, "Scouting Tool", p.getName(), 'B', 1, "Click areas in the wilderness to", "scout more land for your kingdom."));
			tools.add(new Tool(Material.IRON_SPADE, "Resource Collection Tool", p.getName(), 'C', 1, "Click on resource filled areas of land to harvest", "the available resources."));
			tools.add(new Tool(Material.CLAY_BRICK, "Build or Repair Tool", p.getName(), 'D', 1, "Right click to scroll through buildings.", "Then click on the ground in an area of land", "to build the building there.", "Clicking on a building repairs it."));
			tools.add(new Tool(Material.NETHER_STAR, "Upgrade Tool", p.getName(), 'E', 1, "Click on buildings to upgrade them."));
			tools.add(new Tool(Material.FLINT_AND_STEEL, "Delete Area Tool", p.getName(), 'F', 1, "Click on buildings to clear them back", "into land. Or click on land to revert the", "area back into wilderness."));
			tools.add(new Tool(Material.FEATHER, "Message Tool", p.getName(), 'G', 1, "Click on buildings to get", "the next message event."));
			tools.add(new Tool(Material.BOOK_AND_QUILL, "Scoreboard Tool", p.getName(), 'H', 1, "Use to check the scores of all kingdoms."));
			tools.add(new Tool(Material.DIAMOND_SWORD, "Military Control Tool", p.getName(), 'I', 1, "Click on a military unit to select it", "then click on something else to", "assign it a task. Clicking in", "the wilderness will cause it to move.", "Clicking on another kingdom's land or military", "unit will cause it to attack.", "Clicking on the wool reforms it."));
			tools.add(new Tool(Material.COMPASS, "Teleportation Tool", p.getName(), 'J', 1, "Click to teleport to different areas", "of the map. The arrow will point to", "to the location of your capital."));
			tools.add(new Tool(Material.BEACON, "Capital Warp Tool", p.getName(), 'K', 1, "Place the beacon to warp back to your capital."));
			tools.add(new Tool(Material.ARROW, "Speed Tool", p.getName(), 'L', 1, "Place the beacon to warp back to your capital."));
			tools.add(new Tool(Material.GOLD_HELMET, "Villager Movement Tool", p.getName(), 'M', 1, "Click on buildings to move villagers.", "Click on troop conversion buildings to allow", "them to start turning villagers into", "troops for you."));
			tools.add(new Tool(Material.FIREBALL, "Delete Kingdom Tool", p.getName(), 'N', 1, "Deletes your kingdom."));
			tools.add(new Tool(Material.ANVIL, "Fix Tool", p.getName(), 'O', 1, "Use this to fix broken buildings."));
			tools.add(new Tool(Material.SKULL_ITEM, "Bug Report Tool", p.getName(), 'P', 1, "Use this to report any bugs your find", "in the game."));
			tools.add(new Tool(Material.SIGN, "Itembar Manager Tool", p.getName(), 'Q', 1, "Click to switch around the placement of items", "in your toolbars. Exit the inventory view", "to apply changes."));
			tools.add(new Tool(Material.DIAMOND, "Achivement Tool", p.getName(), 'R', 1, "Use this to view all your kingdom's current", "achivements."));
			tools.add(new Tool(Material.GOLD_INGOT, "Henchmen Tool", p.getName(), 'S', 1, "Right click to scroll through current", "henchmen, and left click to use", "them. Click on a tavern to hire them."));
			tools.add(new Tool(Material.LEVER, "Info Monitor Tool", p.getName(), 'T', 1, "Click to scroll through display types.", "Clicking on buildings will monitor that building", "or a list of buildings."));
			tools.add(new Tool(Material.BLAZE_ROD, "Troop Special Tool", p.getName(), 'U', 1, "Click to activate a troops special", "abilities."));
			tools.add(new Tool(Material.MINECART, "Change Height Tool", p.getName(), 'V', 1, "Left click land to raise it's height,", "and right click to lower it."));
			tools.add(new Tool(Material.CAKE, "Help Tool", p.getName(), 'W', 1, "Click on a building to the help for it."));
			tools.add(new Tool(Material.WEB, "Website Tool", p.getName(), 'X', 1, "Click to recive a link to our website."));
			tools.add(new Tool(Material.GOLD_NUGGET, "Buy Feature Tool", p.getName(), 'Y', 1, "Right click to scroll through donator features,", "and left click to buy them."));
			tools.add(new Tool(Material.GOLD_BLOCK, "Donate Tool", p.getName(), 'Z', 1, "Gives a link to where you can donate and", "earn donator points."));
			tools.add(new Tool(Material.FIREWORK, "Credits Tool", p.getName(), '!', 1, "Click to view conquest's credits."));
			if(scroll<3){
				final Tool[] itembar = getItembar(tools, scroll+1);
				for(Tool t : itembar){ i.addItem(t.getItem()); }
				p.setCompassTarget(kingdom.getBuildingLocations(BuildingType.CAPITOL).get(0).getChunk().getBlock(0, 0, 0).getLocation());
			}else if(scroll==3){
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)0));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)1));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)2));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)3));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)4));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)5));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)6));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)7));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)8));
			}else if(scroll==4){
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)9));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)10));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)11));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)12));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)13));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)14));
				i.addItem(new ItemStack(Material.INK_SACK, 1, (byte)15));
				//Add Reset Color Tool
				item=new ItemStack(Material.WATER_BUCKET);
				meta=item.getItemMeta();
				meta.setDisplayName("Reset Color Tool");
				meta.setLore(Arrays.asList(new String[]{"Click your flag to reset all the colors back to white."}));
				item.setItemMeta(meta);
				i.addItem(item);
				//Add Randomize Color Tool
				item=new ItemStack(Material.LAVA_BUCKET);
				meta=item.getItemMeta();
				meta.setDisplayName("Randomize Color Tool");
				meta.setLore(Arrays.asList(new String[]{"Click your flag to randomize the colors on it."}));
				item.setItemMeta(meta);
				i.addItem(item);
			}
			//Add Tool Tips
			if(!invpage.containsKey(p.getName()))invpage.put(p.getName(), 1);
			int page = invpage.get(p.getName());
			item=new ItemStack(Material.SUGAR);
			meta=item.getItemMeta();
			meta.setDisplayName("Next Page");
			meta.setLore(Arrays.asList(new String[]{"Click to go to the next page.", ChatColor.GRAY+"Current Page: "+page}));
			item.setItemMeta(meta);
			i.setItem(35, item);
			item=new ItemStack(Material.SUGAR);
			meta=item.getItemMeta();
			meta.setDisplayName("Previous Page");
			meta.setLore(Arrays.asList(new String[]{"Click to go back a page.", ChatColor.GRAY+"Current Page: "+page}));
			item.setItemMeta(meta);
			i.setItem(27, item);
			try{
				final WikiTipPage wiki = WhCommunity.wikitips.get(page);
				WikiTip tip;
				for(int x = 0; x<25; x++){
					tip=wiki.getTip(x);
					item=new ItemStack(Material.BOOK_AND_QUILL);
					meta=item.getItemMeta();
					meta.setDisplayName(tip.getName()+ChatColor.YELLOW+"(Page: "+page+")");
					meta.setLore(tip.getLore());
					item.setItemMeta(meta);
					item.setAmount(x+1);
					i.addItem(item);
				}
			}catch(final Exception exception){}
			if(!p.getAllowFlight())p.setAllowFlight(true);
		}
	}
	public static void setStaminaTime(Player p, int time){
		if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Conquest_Enabled)p.setLevel(time);
	}
	public static void setSelectedArmy(String p, Army army){
		if(army==null){
			Save.set("Players", p, "Army", null);
			return;
		}
		Save.set("Players", p, "Army", String.valueOf(army.getId()));
	}
	public static Army getSelectedArmy(String p){
		String in = Save.get("Players", p, "Army");
		if(in==null)return null;
		Building building = Building.getById(PlayerMg.getKingdom(p), Integer.valueOf(in));
		if(building instanceof Army)return (Army)building;
		return null;
	}
	public static List<ChunkCords> getPreviousKingdomLocations(String p){
		List<ChunkCords> l = new ArrayList<ChunkCords>();
		String in = Save.get("Players", p, "Kingdom Locations");
		if(in==null)return l;
		String[] s = in.split(";");
		String[] ss;
		for(String s1 : s){
			ss=s1.split(",");
			l.add(new ChunkCords(ss[0], Integer.valueOf(ss[1]), Integer.valueOf(ss[2])));
		}
		return l;
	}
	public static void addKingdomLocatin(String p, ChunkCords cc){
		List<ChunkCords> l = getPreviousKingdomLocations(p);
		l.add(cc);
		String s = "";
		for(ChunkCords c : l){ s+=";"+c.getWorld().getName()+","+c.getX()+","+c.getZ(); }
		s=s.substring(1);
		Save.set("Players", p, "Kingdom Locations", s);
	}
	public static void setScoardBoardValues(final Player player, final HashMap<String,Integer> values){
		Bukkit.getScheduler().runTask(WhCommunity.plugin, new Runnable(){
			@Override
			public void run(){
				final String dis = player.getName();
				Scoreboard score;
				if(scores.containsKey(player.getName()))score=scores.get(player.getName());
				else{
					score=new Scoreboard();
					scores.put(dis, score);
				}
				if(score.getObjective(dis)==null){
					score.registerObjective(player.getName(), new ScoreboardBaseCriteria(dis));
					Packet206SetScoreboardObjective packet = new Packet206SetScoreboardObjective(score.getObjective(dis), 0);
					Packet208SetScoreboardDisplayObjective display = new Packet208SetScoreboardDisplayObjective(1, score.getObjective(dis));
					sendPacket(player, packet);
					sendPacket(player, display);
				}
				score.resetPlayerScores(dis);
				for(String s : values.keySet()){
					ScoreboardScore sc = score.getPlayerScoreForObjective(s, score.getObjective(dis));
					sc.setScore(values.get(s));
					Packet207SetScoreboardScore pScoreItem1 = new Packet207SetScoreboardScore(sc, 0);
					sendPacket(player, pScoreItem1);
				}
			}
		});
	}
	public static void updateDefaultScoreboard(final int days, final int hours){
		try{
			Save.set("Resources", "Timer Storage", "Conquest Hours", String.valueOf(hours));
			for(Player p : Bukkit.getOnlinePlayers()){ PlayerMg.setScoardBoardValues(p, getDefaultValues(p, days, hours)); }
		}catch(Exception exception){ exception.printStackTrace(); }
	}
	private static HashMap<String,Integer> getDefaultValues(final Player p, final int days, final int seconds){
		final HashMap<String,Integer> v = new HashMap<>();
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final long m = getMonitors(p.getName());
		if(kingdom==null||(m&1)==1)v.put(ChatColor.GOLD+"Days", -days);
		if(kingdom==null||(m&2)==2)v.put(ChatColor.GOLD+"Next Day", -seconds);
		if(kingdom!=null){
			int idle = kingdom.getIdlingVillagers();
			int villagers = idle;
			int troops = 0;
			int wood = 0;
			int food = 0;
			int stone = 0;
			int iron = 0;
			int arms = 0;
			int lands = 0;
			int messages = 0;
			int villagerearnings = kingdom.getMorale()/10;
			ResourceHolder r;
			for(Building b : Building.getBuildings(kingdom)){
				if(b instanceof VillagerWorkable)villagers+=((VillagerWorkable)b).getWorkingVillagers();
				if(b instanceof Army){
					villagers+=((Army)b).getHp();
					troops+=((Army)b).getHp();
				}
				if(b instanceof ResourceHolder){
					r=(ResourceHolder)b;
					if(Arrays.asList(r.getResources()).contains(ResourceType.WOOD))wood+=r.getResourceLevel(ResourceType.WOOD);
					if(Arrays.asList(r.getResources()).contains(ResourceType.FOOD))food+=r.getResourceLevel(ResourceType.FOOD);
					if(Arrays.asList(r.getResources()).contains(ResourceType.IRON))iron+=r.getResourceLevel(ResourceType.IRON);
					if(Arrays.asList(r.getResources()).contains(ResourceType.ARMS))arms+=r.getResourceLevel(ResourceType.ARMS);
					if(Arrays.asList(r.getResources()).contains(ResourceType.STONE))stone+=r.getResourceLevel(ResourceType.STONE);
				}
				if(b instanceof Cottage)villagerearnings++;
				lands+=Math.pow(b.getLength(), 2);
				messages+=b.getTotalMessages();
			}
			int goldearnings = (int)(villagers*(kingdom.getMorale()/100.0));
			if((m&4)==4)v.put(ChatColor.GREEN+"Wood", wood);
			if((m&8)==8)v.put(ChatColor.YELLOW+"Gold", kingdom.getMoney());
			if((m&16)==16)v.put(ChatColor.GREEN+"Stone", stone);
			if((m&32)==32)v.put(ChatColor.GREEN+"Arms", arms);
			if((m&64)==64)v.put(ChatColor.GREEN+"Iron", iron);
			if((m&128)==128)v.put(ChatColor.GREEN+"Food", food);
			if((m&256)==256)v.put(ChatColor.AQUA+"Population", villagers);
			if((m&512)==512)v.put(ChatColor.AQUA+"Troops", troops);
			if((m&1024)==1024)v.put(ChatColor.AQUA+"Idle Villagers", idle);
			if((m&2048)==2048)v.put(ChatColor.GREEN+"Land Owned", lands);
			if((m&4096)==4096)v.put(ChatColor.GRAY+"Messages", messages);
			if((m&8192)==8192)v.put(ChatColor.LIGHT_PURPLE+"Gold/Day", goldearnings);
			if((m&16384)==16384)v.put(ChatColor.LIGHT_PURPLE+"Villagers/Day", villagerearnings);
		}
		return v;
	}
	public static long getMonitors(final String p){
		String in = Save.get("Players", p, "Monitors");
		if(in==null)return 32767L;
		return Long.valueOf(in);
	}
	public static void setItembar(final Player p, final String i){
		Save.set("Players", p.getName(), "Items", i);
		PlayerMg.resetConquestToolkit(p);
	}
	private static Tool[] getItembar(final ArrayList<Tool> tools, final int bar){
		final Tool[] itembar = new Tool[9];
		Tool t;
		for(int i = 8; i>=0; i--){
			t=getNextTool(tools, bar);
			tools.remove(t);
			itembar[i]=t;
		}
		return itembar;
	}
	private static Tool getNextTool(final ArrayList<Tool> tools, final int bar){
		final ArrayList<Tool> itembar = new ArrayList<>();
		for(Tool t : tools){ if(t.getItembar()==bar)itembar.add(t); }
		Tool tool = null;
		int pos = 100;
		for(Tool t : itembar){
			if(t.getPosition()<pos){
				pos=t.getPosition();
				tool=t;
			}
		}
		return tool;
	}
	public static int getDonatorPoints(final String p){
		final String in = Save.get("Players", p, "DP");
		if(in==null)return 0;
		return Integer.valueOf(in);
	}
	public static int getLandMax(final String p, final int buffer){
		final String in;
		if(buffer==0){
			in=Save.get("Players", p, "LandBuffer");
			if(in==null)return 218;
			return Integer.valueOf(in);
		}else if(buffer==1){
			in=Save.get("Players", p, "LandMax");
			if(in==null)return 150;
			return Integer.valueOf(in);
		}
		return getLandMax(p, 0)+getLandMax(p, 1);
	}
	public static long getExtraLandTime(final String p){
		final String in = Save.get("Players", p, "Extra Land Time");
		if(in==null)return 0;
		return Long.valueOf(in);
	}
	public static void increasedLandMax(final String p, final int amount, final boolean buffer){
		if(buffer)Save.set("Players", p, "LandBuffer", String.valueOf(getLandMax(p, 0)+amount));
		else Save.set("Players", p, "LandMax", String.valueOf(getLandMax(p, 1)+amount));
	}
	public static void setExtraLandTime(final String p){ Save.set("Players", p, "Extra Land Time", String.valueOf(System.currentTimeMillis()+86400000L)); }
	public static void setKingdomCreateTime(final String p){ Save.set("Players", p, "Kingdom Create Time", String.valueOf(System.currentTimeMillis()+172800000l)); }
	public static void addDonatorPoints(final String p, final int points){ Save.set("Players", p, "DP", String.valueOf(getDonatorPoints(p)+points)); }
	public static void removeDonatorPoints(final String p, final int points){ Save.set("Players", p, "DP", String.valueOf(getDonatorPoints(p)-points)); }
	public static void setMonitors(final String p, final long m){ Save.set("Players", p, "Monitors", String.valueOf(m)); }
	private static void sendPacket(Player player, Packet packet){ ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet); }
	public static void removeScoreboard(final String p){ scores.remove(p); }
}
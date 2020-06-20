package me.ci.Conquest.KingdomManagment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ci.WhCommunity;
import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Community.Save;
import me.ci.Conquest.BuildingInterfaces.KingdomManager;
import me.ci.Conquest.BuildingInterfaces.ResourceHolder;
import me.ci.Conquest.BuildingInterfaces.TroopConvertable;
import me.ci.Conquest.BuildingInterfaces.VillagerWorkable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Buildings.Constructors.Flag;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Buildings.Main.Gatehouse;
import me.ci.Conquest.Buildings.Main.Land;
import me.ci.Conquest.Buildings.Main.Moat;
import me.ci.Conquest.Buildings.Main.Wall;
import me.ci.Conquest.Buildings.Main.WatchTower;
import me.ci.Conquest.Events.BuildingDestroyEvent;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.ClassType;
import me.ci.Conquest.Military.HenchmenType;
import me.ci.Conquest.Misc.Achievement;
import me.ci.Conquest.Misc.CallName;
import me.ci.Conquest.Misc.ChunkCords;
import me.ci.Conquest.Textures.BuildingTexture;
import me.ci.Conquest.Textures.ConquestTextures;


public class Kingdom{
	private String owner;
	private final String name;
	private Flag flag;
	private boolean plagued;
	private int ildingvillagers;
	private int money;
	private int morale;
	private World world;
	private Map<ResourceType,Integer> resources = new HashMap<ResourceType,Integer>();
	private ResourceType mainresource;
	private boolean king;
	private boolean bountyhunter;
	private final HashMap<String,ClassType> classes = new HashMap<>();
	private final HashMap<HenchmenType,Integer> henchmen = new HashMap<>();
	private final ArrayList<Achievement> achievements = new ArrayList<>();
	public Kingdom(String owner, String name, World world, ResourceType mainresource, boolean isking){
		this.owner=owner;
		this.name=name;
		this.world=world;
		this.mainresource=mainresource;
		this.morale=50;
		KingdomMg.newKingdom(this);
		Random r = new Random();
		int left = 0;
		int wood = 0, iron = 0, stone = 0, plain = 0, food = 0, gold = 0;
		int n;
		while(left<100){
			n=r.nextInt(6);
			if(n==0)wood++;
			else if(n==1)iron++;
			else if(n==2)stone++;
			else if(n==3)plain++;
			else if(n==4)food++;
			else gold++;
			left++;
		}
		plain+=100;
		resources.put(ResourceType.WOOD, wood);
		resources.put(ResourceType.IRON, iron);
		resources.put(ResourceType.NONE, plain);
		resources.put(ResourceType.STONE, stone);
		resources.put(ResourceType.GOLD, gold);
		resources.put(ResourceType.FOOD, food);
		resources.put(mainresource, resources.get(mainresource)+50);
		king=isking;
	}
	public Kingdom(String name){
		this.name=name;
		this.owner=Save.get("Kingdoms", name, "Owner");
		String flagin = Save.get("Kingdoms", name, "Flag");
		if(flagin!=null)this.flag=new Flag(flagin);
		this.plagued=Boolean.valueOf(Save.get("Kingdoms", name, "HasPlauge"));
		this.ildingvillagers=Integer.valueOf(Save.get("Kingdoms", name, "Idling Villagers"));
		this.money=Integer.valueOf(Save.get("Kingdoms", name, "Money"));
		this.morale=Integer.valueOf(Save.get("Kingdoms", name, "Morale"));
		this.world=Bukkit.getWorld(Save.get("Kingdoms", name, "World"));
		this.mainresource=ResourceType.getById(Integer.valueOf(Save.get("Kingdoms", name, "Main Resource")));
		String[] r = Save.get("Kingdoms", name, "Resources").split(",");
		this.resources.put(ResourceType.FOOD, Integer.valueOf(r[0]));
		this.resources.put(ResourceType.GOLD, Integer.valueOf(r[1]));
		this.resources.put(ResourceType.IRON, Integer.valueOf(r[2]));
		this.resources.put(ResourceType.STONE, Integer.valueOf(r[3]));
		this.resources.put(ResourceType.WOOD, Integer.valueOf(r[4]));
		this.resources.put(ResourceType.NONE, Integer.valueOf(r[5]));
		this.king=Boolean.valueOf(Save.get("Kingdoms", name, "King"));
		this.bountyhunter=Boolean.valueOf(Save.get("Kingdoms", name, "BountyHunter"));
		final String hench = Save.get("Kingdoms", getName(), "Henchmen");
		if(hench!=null){
			final String[] types = hench.split(";");
			String[] s;
			for(String a : types){
				s=a.split(",");
				henchmen.put(HenchmenType.getById(Integer.valueOf(s[0])), Integer.valueOf(s[1]));
			}
		}
		Building.load(this);
		loadAchivements();
	}
	public String getOwner(){ return this.owner; }
	public String getName(){ return this.name; }
	public void setFlag(final Flag flag){ this.flag=flag; }
	public Flag getFlag(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.flag;
	}
	public boolean isRevolting(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.morale==0;
	}
	public boolean hasPlague(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.plagued;
	}
	public int getIdlingVillagers(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.ildingvillagers;
	}
	public void setIdlingVillagers(int amount){ this.ildingvillagers=amount; }
	public int getVillagers(final boolean counttroops){
		int villagers = ildingvillagers;
		TroopConvertable t;
		synchronized(Building.class){
			for(Building b : Building.getBuildings(this)){
				if(b instanceof VillagerWorkable)villagers+=((VillagerWorkable)b).getWorkingVillagers();
				if(b instanceof TroopConvertable){
					t=(TroopConvertable)b;
					if(t.isConvertingTroops())villagers+=t.getConvertAmount();
				}
				if(b instanceof Army
						&&counttroops)villagers+=b.getHp();
			}
		}
		return villagers;
	}
	public void addVillagers(int amount){ this.ildingvillagers+=amount; }
	public void addMoney(int value){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.money+=value;
	}
	public int getMorale(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.morale;
	}
	public int getMoney(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.money;
	}
	public void addMorale(int value){
		this.morale=Math.min(this.morale+value, 100);
	}
	public void removeMoney(int value){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.money=Math.max(this.money-=value, 0);
	}
	public boolean hasMoney(int amount){
		return amount<=getMoney();
	}
	public void removeMorale(int value){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.morale=Math.max(this.morale-=value, 0);
	}
	public synchronized void buildBuilding(Building building, Chunk nwc){
		if(WhCommunity.debug)WhCommunity.printDebug();
		buildBuilding(building, nwc, null);
	}
	public synchronized void buildBuilding(Building building, Chunk nwc, BuildingTexture texture){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String in = Save.get("Kingdoms", this.name, building.getName());
		if(in==null)in="";
		in+=";"+nwc.getX()+","+nwc.getZ();
		if(in.startsWith(";"))in=in.substring(1);
		Save.set("Kingdoms", this.name, building.getName(), in);
		if(texture!=null)texture.buildAt(nwc, building);
		else ConquestTextures.getTexture(building.getType(), building.getLevel()).buildAt(nwc, building);
	}
	public List<ChunkCords> getBuildingLocations(BuildingType type){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getBuildingLocations(type.getName());
	}
	public int getLandOwned(){
		int t = 0;
		for(Building b : Building.buildings.get(this)){
			if(b.getType()==BuildingType.ARMY)continue;
			t+=b.getType().getLength()*b.getType().getLength();
		}
		return t;
	}
	public int getLandMax(){
		int t = 0;
		for(Building b : Building.getBuildings(this)){
			t+=b.getLandBonus();
		}
		return t;
	}
	public List<ChunkCords> getBuildingLocations(String building){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String in = Save.get("Kingdoms", this.name, building);
		if(in==null)return new LinkedList<ChunkCords>();
		String[] s = in.split(";");
		List<ChunkCords> cords = new LinkedList<ChunkCords>();
		for(String i : s){
			try{
				String[] a = i.split(",");
				cords.add(new ChunkCords(this.world, Integer.valueOf(a[0]), Integer.valueOf(a[1])));
			}catch(Exception exception){}
		}
		return cords;
	}
	public synchronized void removeBuilding(final Building building, final boolean placeland, boolean updategraphics, boolean threadsleep){
		try{
			List<ChunkCords> cords = getBuildingLocations(building.getName());
			cords.remove(new ChunkCords(this.world, building.getNorthWestCourner().getX(), building.getNorthWestCourner().getZ()));
			String t = "";
			for(ChunkCords cord : cords){ if(!cords.equals(new ChunkCords(building.getWorld(), building.getNorthWestCourner().getX(), building.getNorthWestCourner().getZ())))t+=";"+cord.getX()+","+cord.getZ(); }
			if(t.startsWith(";"))t=t.substring(1);
			Save.set("Kingdoms", this.name, building.getName(), t);
			Building.getBuildings(this).remove(building);
			Chunk c;
			for(int x = building.getNorthWestCourner().getX(); x<building.getNorthWestCourner().getX()+building.getType().getLength(); x++){
				for(int z = building.getNorthWestCourner().getZ(); z<building.getNorthWestCourner().getZ()+building.getType().getLength(); z++){
					c=getWorld().getChunkAt(x, z);
					if(placeland)new Land(c, this, ResourceType.NONE);
					else{
						if(updategraphics)ConquestTextures.getTexture(BuildingType.WILDERNESS, 1).buildAt(c, null);
						Plot plot = PlotMg.getPlotAt(c);
						plot.unclaim(false);
					}
				}
			}
			Save.set("Kingdoms", building.getKingdom().getName(), "B-"+building.getId(), null);
			Bukkit.getPluginManager().callEvent(new BuildingDestroyEvent(building, !placeland));
		}catch(Exception exception){ exception.printStackTrace(); }
	}
	public void deleteKingdom(){
		try{
			System.out.println("[Conquest] Deleting kingdom: "+getName()+".");
			Bukkit.getScheduler().runTaskAsynchronously(WhCommunity.plugin, new Runnable(){
				public void run(){
					try{
						PlayerMg.setKingdom(owner, null);
						owner=null;
						try{
							ArrayList<Building> buildings = null;
							synchronized(Building.class){ buildings=new ArrayList<>(Building.getBuildings(Kingdom.this)); }
							final int total = buildings.size();
							int pos = 0;
							int lastpercent = -1;
							int percent;
							for(Building b : buildings){
								pos++;
								percent=(int)((double)pos/total*100);
								if(percent!=lastpercent){
									System.out.println("[Conquest] Deleting kingdom... ("+percent+"% Complete)");
									lastpercent=percent;
								}
								try{
									final Building b1 = b;
									Bukkit.getScheduler().runTask(WhCommunity.plugin, new Runnable(){
										public void run(){
											try{
												if(b1 instanceof Army)b1.delete(true, false);
												else{
													b1.setHp(b1.getMaxHp(), false);
													b1.updateGraphics(false);
												}
											}catch(final Exception exception){ exception.printStackTrace(); }
										}
									});
									if(!WhCommunity.SHUTTING_DOWN)Thread.sleep(10000);
									else Thread.sleep(100);
								}catch(final Exception exception){ exception.printStackTrace(); }
							}
						}catch(Exception exception){ exception.printStackTrace(); }
					}catch(final Exception exception){ exception.printStackTrace(); }
					WhCommunity.threads--;
				}
			});
		}catch(final Exception exception){ exception.printStackTrace(); }
	}
	public void save(){
		Save.set("Kingdoms", getName(), "Owner", this.owner);
		if(this.flag!=null)Save.set("Kingdoms", getName(), "Flag", this.flag.toString());
		Save.set("Kingdoms", getName(), "HasPlauge", this.hasPlague()+"");
		Save.set("Kingdoms", getName(), "Idling Villagers", this.ildingvillagers+"");
		Save.set("Kingdoms", getName(), "Money", this.money+"");
		Save.set("Kingdoms", getName(), "Morale", this.morale+"");
		Save.set("Kingdoms", getName(), "World", this.world.getName());
		Save.set("Kingdoms", getName(), "Main Resource", String.valueOf(this.mainresource.getId()));
		Save.set("Kingdoms", getName(), "King", String.valueOf(king));
		Save.set("Kingdoms", getName(), "BountyHunter", String.valueOf(bountyhunter));
		String r = "";
		r+=this.resources.get(ResourceType.FOOD)+",";
		r+=this.resources.get(ResourceType.GOLD)+",";
		r+=this.resources.get(ResourceType.IRON)+",";
		r+=this.resources.get(ResourceType.STONE)+",";
		r+=this.resources.get(ResourceType.WOOD)+",";
		r+=this.resources.get(ResourceType.NONE);
		Save.set("Kingdoms", getName(), "Resources", r);
		String h = "";
		for(HenchmenType type : henchmen.keySet()){ h+=";"+type.getId()+","+henchmen.get(type); }
		while(h.startsWith(";"))h=h.substring(1);
		Save.set("Kingdoms", getName(), "Henchmen", h);
		Building.save(this);
		ClassType cl;
		for(String s : classes.keySet()){
			cl=classes.get(s);
			Save.set("Kingdoms", getName(), "C-"+cl.getTypeName()+":"+s, cl.getValueAsString());
		}
		saveAchievements();
	}
	public World getWorld(){ return this.world; }
	public boolean ownsChunk(Chunk c){
		Plot plot = PlotMg.getPlotAt(c);
		if(plot.isKingdomPlot()){
			try{ if(plot.getKingdom().equals(this))return true;
			}catch(Exception exception){}
		}
		return false;
	}
	public boolean hasLiveWatchTower(){
		try{
			Plot plot;
			WatchTower t;
			for(ChunkCords cc : getBuildingLocations(BuildingType.WATCH_TOWER)){
				plot=PlotMg.getPlotAt(cc.getChunk());
				if(plot.getBuilding().isAlive()){
					t=(WatchTower)plot.getBuilding();
					if(Math.random()*100<t.getSightChance())return true;
				}
			}
		}catch(final Exception exception){}
		return false;
	}
	public int getVillagerMax(){
		int v = 0;
		for(Building b : Building.getBuildings(this)){ v+=b.getVillagerMaxBonus(); }
		return v;
	}
	public void killVillagers(int amount){
		final int mg = (int)((double)amount/getVillagers(true)*100/3);
		removeMorale(mg);
		if(amount>=getVillagers(false)){
			deleteKingdom();
			return;
		}
		this.morale=Math.max(this.morale-amount/5, 0);
		List<Building> builds = new LinkedList<Building>();
		for(Building b : Building.getBuildings(this)){
			if(b instanceof VillagerWorkable
					&&((VillagerWorkable)b).getWorkingVillagers()>0)builds.add(b);
			else if(b instanceof KingdomManager
					&&this.ildingvillagers>0)builds.add(b);
		}
		Building b;
		VillagerWorkable work;
		while(amount>0){
			if(builds.size()==0){
				deleteKingdom();
				return;
			}
			b=builds.get((int)(Math.random()*builds.size()));
			int loss;
			if(b instanceof VillagerWorkable){
				work=(VillagerWorkable)b;
				loss=(int)Math.min(Math.random()*amount+1, work.getWorkingVillagers());
				work.setWorkingVillagers(work.getWorkingVillagers()-loss);
				if(loss==work.getWorkingVillagers())builds.remove(b);
			}else{
				loss=(int)Math.min(Math.random()*amount+1, this.ildingvillagers);
				this.ildingvillagers-=loss;
				if(loss==this.ildingvillagers)builds.remove(b);
			}
			amount-=loss;
		}
	}
	public void killVillagers(final VillagerWorkable b, final int amount){
		if(b.getWorkingVillagers()==0)return;
		final int w = b.getWorkingVillagers();
		final int left = Math.max(w-amount, 0);
		final int killed = w-left;
		b.setWorkingVillagers(left);
		morale=Math.max(killed/5, 0);
	}
	public boolean isDead(){
		return this.owner==null;
	}
	public int getMaxResourceLevel(ResourceType type){
		int amount = 0;
		for(Building b : Building.getBuildings(this)){
			if(b instanceof ResourceHolder){
				if(Arrays.asList(((ResourceHolder)b).getResources()).contains(type))amount+=((ResourceHolder)b).getMaxResourceLevel();
			}
		}
		return amount;
	}
	public boolean isOwnerOnline(){
		if(owner==null)return false;
		return Bukkit.getPlayer(owner)!=null;
	}
	public static boolean claimChunk(final Player p, final Chunk c, final boolean explorer){
		Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(c);
		CallName.deleteExtras(p.getName(), "ClaimChunk");
		if(kingdom.getWorld()!=c.getWorld()){
			p.sendMessage(ChatColor.RED+"You cannot scout chunks in worlds other then the one your kingdom is in.");
			return false;
		}
		boolean nope = false;
		if(plot.isKingdomPlot()){
			if(!plot.getKingdom().isRuins())nope=true;
		}else if(plot.isClaimed())nope=true;
		if(nope){
			p.sendMessage(ChatColor.RED+"This land is currently occupied!");
			return false;
		}
		boolean near = false;
		if(explorer)near=true;
		else{
			for(Plot p1 : getTouchingPlots(c)){
				if(p1.isKingdomPlot()){
					if(p1.getBuilding() instanceof Army)continue;
					if(p1.getKingdom()==kingdom)near=true;
				}
			}
		}
		if(!near){
			p.sendMessage(ChatColor.RED+"You cannot scout land here, it is too far from your kingdom!");
			return false;
		}
		HashMap<ResourceType,Integer> bonus = new HashMap<ResourceType,Integer>();
		if(plot.isKingdomPlot()&&plot.getKingdom().isRuins()){
			if(plot.getBuilding() instanceof ResourceHolder){
				for(ResourceType rt : ((ResourceHolder)plot.getBuilding()).getResources())
					bonus.put(rt, plot.getBuilding().getResources(rt));
			}
		}
		if(kingdom.getLandOwned()+1<=kingdom.getLandMax()){
			if(kingdom.getLandOwned()<PlayerMg.getLandMax(p.getName(), 2)){
				if(plot.isKingdomPlot())plot.getBuilding().delete(true, false);
				p.sendMessage(ChatColor.GREEN+"Land scouted.");
				if(!bonus.isEmpty()){
					for(ResourceType rt : bonus.keySet()){
						int amount = kingdom.addResources(rt, bonus.get(rt));
						p.sendMessage(ChatColor.DARK_AQUA+"While scouting, you found "+ChatColor.AQUA+amount+" "+ChatColor.GRAY+rt.name().toLowerCase()+ChatColor.DARK_AQUA+" from the ruins.");
					}
				}
				final Plot plot10 = PlotMg.getPlotAt(c);
				if(plot10.getRealHeight()==0){
					int high = 0;
					int low = 0;
					for(int x = -2; x<=2; x++){
						for(int z = -2; z<=2; z++){
							if(x==0&&z==0)continue;
							int l = PlotMg.getPlotAt(c.getWorld(), c.getX()+x, c.getZ()+z).getRealHeight();
							if(l>0){
								high+=l;
								low++;
							}
						}
					}
					if(low>0)high/=low;
					plot10.setHeight((int)(high+(Math.random()*3-1))+getHeightBonus(), false);
				}
				new Land(c, kingdom);
				PlayerMg.setStaminaTime(p, PlayerMg.SCOUT);
				kingdom.awardAchievement(Achievement.BOY_SCOUT_GIRL_SCOUT);
				return true;
			}else{
				p.sendMessage(ChatColor.RED+"You have reached your kingdom size limit. If you wish to build a larger kingdom, please follow this link and buy \"Increase Land Max.\"");
				p.sendMessage(ChatColor.YELLOW+"http://82x78ni7zhoyn5p.buycraft.net/category/5730");
			}
		}else p.sendMessage(ChatColor.RED+"This kingdom cannot hold any more land!");
		return false;
	}
	private static Plot[] getTouchingPlots(Chunk c){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return new Plot[]{
		PlotMg.getPlotAt(c.getWorld(), c.getX()-1, c.getZ()),
		PlotMg.getPlotAt(c.getWorld(), c.getX()+1, c.getZ()),
		PlotMg.getPlotAt(c.getWorld(), c.getX(), c.getZ()-1),
		PlotMg.getPlotAt(c.getWorld(), c.getX(), c.getZ()+1),
		PlotMg.getPlotAt(c.getWorld(), c.getX()+1, c.getZ()+1),
		PlotMg.getPlotAt(c.getWorld(), c.getX()+1, c.getZ()-1),
		PlotMg.getPlotAt(c.getWorld(), c.getX()-1, c.getZ()+1),
		PlotMg.getPlotAt(c.getWorld(), c.getX()-1, c.getZ()-1)};
	}
	public ResourceType getMainResource(){ return this.mainresource; }
	public Map<ResourceType,Integer> getResourcePercents(){ return this.resources; }
	public int getResourceLevels(ResourceType r){
		if(r==ResourceType.GOLD)return getMoney();
		int total = 0;
		ResourceHolder rh;
		for(Building b : Building.getBuildings(this)){
			if(b instanceof ResourceHolder){
				rh=(ResourceHolder)b;
				total+=rh.getResourceLevel(r);
			}
		}
		return total;
	}
	public int addResources(ResourceType r, int amount){
		if(r==ResourceType.GOLD){
			this.money+=amount;
			return 0;
		}
		for(Building b : Building.getBuildings(this)){
			if(amount<=0)return 0;
			amount=b.addResources(r, amount);
		}
		return amount;
	}
	public int collectResources(ResourceType r, int amount){
		if(r==ResourceType.GOLD){
			this.money-=amount;
			return 0;
		}
		for(Building b : Building.getBuildings(this)){
			if(amount<=0)return 0;
			int b4 = b.getResources(r);
			if(b4>=amount){
				b.removeResources(r, amount);
				return 0;
			}
			amount-=b4;
			b.removeResources(r, b4);
		}
		return amount;
	}
	public void loadPlots(){
		Bukkit.getScheduler().runTask(WhCommunity.plugin, new Runnable(){
			public void run(){
				Plot plot;
				List<Building> todelete = new ArrayList<Building>();
				for(Building b : Building.getBuildings(Kingdom.this)){
					if(todelete.contains(b))continue;
					for(int x = b.getNorthWestCourner().getX(); x<b.getNorthWestCourner().getX()+b.getLength(); x++){
						for(int z = b.getNorthWestCourner().getZ(); z<b.getNorthWestCourner().getZ()+b.getLength(); z++){
							plot=PlotMg.getPlotAt(world, x, z);
							if(plot.isKingdomPlot()
									&&plot.getBuilding()!=b){
								if(b.getType()==BuildingType.LAND){
									todelete.add(b);
									continue;
								}else todelete.add(plot.getBuilding());
							}
							plot.setKingdom(Kingdom.this, b);
						}
					}
				}
				for(Building b : todelete){ b.delete(false, false); }
			}
		});
	}
	public void addHenchmen(final HenchmenType h){
		if(henchmen.containsKey(h)){
			final int a = henchmen.get(h)+1;
			henchmen.put(h, a);
		}else henchmen.put(h, 1);
	}
	public void removeHenchmen(final HenchmenType h){
		if(henchmen.containsKey(h)){
			final int a = henchmen.get(h)-1;
			henchmen.put(h, a);
		}
	}
	public void awardAchievement(final Achievement a1){
		if(achievements.contains(a1))return;
		if(owner==null)return;
		achievements.add(a1);
		int tuts = 0;
		for(Achievement a : achievements){ if(a.isTutorial())tuts++; }
		if(tuts==49){
			awardAchievement(Achievement.TUTORIAL_MASTER);
			return;
		}
		final Player p = Bukkit.getPlayer(owner);
		if(p==null)return;
		final Inventory i = Bukkit.createInventory(null, 54, ChatColor.BLUE+"New Achivement Unlocked!");
		final boolean tut = a1.isTutorial();
		ItemStack it;
		ItemMeta meta;
		ArrayList<String> lore;
		for(Achievement a : getAchievements()){
			if(tut!=a.isTutorial())continue;
			if(a==a1)it=new ItemStack(Material.DIAMOND_BLOCK);
			else it=new ItemStack(Material.DIAMOND);
			meta=it.getItemMeta();
			lore=new ArrayList<>();
			meta.setDisplayName(ChatColor.GOLD.toString()+ChatColor.BOLD.toString()+a.getName()+"!");
			lore.add(ChatColor.DARK_AQUA.toString()+a.getPoints()+" Point"+(a.getPoints()==1?"":"s")+" awarded!");
			for(String s : a.getDescription()){ lore.add(ChatColor.AQUA+s); }
			meta.setLore(lore);
			it.setItemMeta(meta);
			i.addItem(it);
		}
		for(Achievement a : Achievement.values()){
			if(tut!=a.isTutorial())continue;
			if(hasAchievement(a))continue;
			it=new ItemStack(Material.DIAMOND_ORE);
			meta=it.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD.toString()+ChatColor.BOLD.toString()+a.getName()+"!");
			if(tut){
				lore=new ArrayList<>();
				for(String s : a.getDescription()){ lore.add(ChatColor.AQUA+s); }
				meta.setLore(lore);
			}
			it.setItemMeta(meta);
			i.addItem(it);
		}
		p.openInventory(i);
	}
	public void saveAchievements(){
		String s = "";
		for(Achievement a : achievements)s+=","+a.getId();
		if(s.startsWith(","))s=s.substring(1);
		Save.set("Kingdoms", getName(), "Achievements", s);
	}
	public void loadAchivements(){
		final String in = Save.get("Kingdoms", getName(), "Achievements");
		if(in==null)return;
		final String[] s = in.split(",");
		for(String a : s)achievements.add(Achievement.getById(Integer.valueOf(a)));
	}
	public int getRank(final int catagory){
		if(catagory==1)return getPowerRank();
		if(catagory==2)return getAchievementsRank();
		if(catagory==3)return getWealthRank();
		if(catagory==4)return getEstateRank();
		if(catagory==5)return getDefensesRank();
		if(catagory==6)return getTechnologyRank();
		if(catagory==7)return getResourcesRank();
		if(catagory==8)return getPopulationRank();
		return getTotalRank();
	}
	private int getPowerRank(){
		int r = 0;
		for(Building b : Building.getBuildings(this))if(b instanceof Army)r+=b.getHp();
		r/=25;
		return r;
	}
	private int getAchievementsRank(){
		int r = 0;
		for(Achievement a : achievements)r+=a.getPoints();
		return r;
	}
	private int getDefensesRank(){
		int r = 0;
		for(Building b : Building.getBuildings(this)){
			if(b instanceof Wall
					||b instanceof Moat
					||b instanceof WatchTower
					||b instanceof Gatehouse)r+=b.getHp();
		}
		r/=10;
		return r;
	}
	private int getTechnologyRank(){
		int r = 0;
		for(Building b : Building.getBuildings(this))r+=b.getLevel();
		r*=1.5;
		return r;
	}
	private int getResourcesRank(){
		return (getResourceLevels(ResourceType.ARMS)
				+getResourceLevels(ResourceType.IRON)
				+getResourceLevels(ResourceType.FOOD)
				+getResourceLevels(ResourceType.STONE)
				+getResourceLevels(ResourceType.WOOD))/20;
	}
	private int getTotalRank(){
		double d = 0.0;
		d+=getPowerRank();
		d+=getAchievementsRank();
		d+=getWealthRank();
		d+=getWealthRank();
		d+=getDefensesRank();
		d+=getTechnologyRank();
		d+=getResourcesRank();
		d+=getPopulationRank();
		return (int)(d/8);
	}
	private int getWealthRank(){ return (int)((double)money/getVillagers(true)*100/40); }
	private int getEstateRank(){ return getLandOwned(); }
	public boolean hasBountyHunter(){ return bountyhunter; }
	public void addBountyHunter(){ bountyhunter=true; }
	public void useBountyHunter(){ bountyhunter=false; }
	private int getPopulationRank(){ return getVillagers(true); }
	public ArrayList<Achievement> getAchievements(){ return achievements; }
	public boolean hasAchievement(final Achievement a){ return achievements.contains(a); }
	public boolean isRuins(){ return this.owner==null; }
	public HashMap<String,ClassType> getClasses(){ return classes; }
	public boolean isKing(){ return king; }
	public HashMap<HenchmenType,Integer> getHenchmen(){ return henchmen; }
	private static int getHeightBonus(){
		final int o = (int)(Math.random()*15);
		if(o==0)return -3;
		if(o==1)return -2;
		if(o==2)return -2;
		if(o==3)return -1;
		if(o==4)return -1;
		if(o==5)return -1;
		if(o==6)return 0;
		if(o==7)return 0;
		if(o==8)return 0;
		if(o==9)return 1;
		if(o==10)return 1;
		if(o==11)return 1;
		if(o==12)return 2;
		if(o==13)return 2;
		return 3;
	}
}
package me.ci.Conquest.Buildings.Constructors;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.ci.WhCommunity;
import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Community.Save;
import me.ci.Conquest.BuildingInterfaces.KingdomManager;
import me.ci.Conquest.BuildingInterfaces.LandHolder;
import me.ci.Conquest.BuildingInterfaces.ResourceHolder;
import me.ci.Conquest.BuildingInterfaces.TroopConvertable;
import me.ci.Conquest.BuildingInterfaces.VillagerHoldable;
import me.ci.Conquest.BuildingInterfaces.VillagerWorkable;
import me.ci.Conquest.Buildings.Main.AncientDojo;
import me.ci.Conquest.Buildings.Main.AntiMagicOnagerWorkshop;
import me.ci.Conquest.Buildings.Main.ArcheryRange;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Buildings.Main.BallisticWorkshop;
import me.ci.Conquest.Buildings.Main.Barrack;
import me.ci.Conquest.Buildings.Main.BridgeMakerWorkshop;
import me.ci.Conquest.Buildings.Main.Blacksmith;
import me.ci.Conquest.Buildings.Main.Capitol;
import me.ci.Conquest.Buildings.Main.CatapultWorkshop;
import me.ci.Conquest.Buildings.Main.Church;
import me.ci.Conquest.Buildings.Main.Coliseum;
import me.ci.Conquest.Buildings.Main.Cottage;
import me.ci.Conquest.Buildings.Main.CowTosserWorkshop;
import me.ci.Conquest.Buildings.Main.DragonPit;
import me.ci.Conquest.Buildings.Main.DragonsLair;
import me.ci.Conquest.Buildings.Main.DruidsHallow;
import me.ci.Conquest.Buildings.Main.EarthTemple;
import me.ci.Conquest.Buildings.Main.FireTemple;
import me.ci.Conquest.Buildings.Main.Gatehouse;
import me.ci.Conquest.Buildings.Main.Granary;
import me.ci.Conquest.Buildings.Main.Graveyard;
import me.ci.Conquest.Buildings.Main.HolyBattlefield;
import me.ci.Conquest.Buildings.Main.IllusionistsTower;
import me.ci.Conquest.Buildings.Main.Land;
import me.ci.Conquest.Buildings.Main.MagesTower;
import me.ci.Conquest.Buildings.Main.MangonelWorkshop;
import me.ci.Conquest.Buildings.Main.Marketplace;
import me.ci.Conquest.Buildings.Main.MeadHall;
import me.ci.Conquest.Buildings.Main.MetalMining;
import me.ci.Conquest.Buildings.Main.MilitaryFort;
import me.ci.Conquest.Buildings.Main.Moat;
import me.ci.Conquest.Buildings.Main.NobleManor;
import me.ci.Conquest.Buildings.Main.PortalGate;
import me.ci.Conquest.Buildings.Main.Road;
import me.ci.Conquest.Buildings.Main.Sanctuary;
import me.ci.Conquest.Buildings.Main.Sawmill;
import me.ci.Conquest.Buildings.Main.SiegeTowerWorkshop;
import me.ci.Conquest.Buildings.Main.Stable;
import me.ci.Conquest.Buildings.Main.StoneQuarry;
import me.ci.Conquest.Buildings.Main.Tavern;
import me.ci.Conquest.Buildings.Main.TradeRoute;
import me.ci.Conquest.Buildings.Main.TrebuchetWorkshop;
import me.ci.Conquest.Buildings.Main.University;
import me.ci.Conquest.Buildings.Main.Wall;
import me.ci.Conquest.Buildings.Main.WatchTower;
import me.ci.Conquest.Buildings.Main.WaterTemple;
import me.ci.Conquest.Buildings.Main.WindTemple;
import me.ci.Conquest.Events.BuildingBuildEvent;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.ClassType;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.EventProperties;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Misc.Achievement;
import me.ci.Conquest.Misc.PathFinder;
import me.ci.Conquest.Misc.PathFinder.DistanceForumula;
import me.ci.Conquest.Misc.PathFinder.Node;
import me.ci.Conquest.Misc.PathFinder.PathBuilder;
import me.ci.Conquest.Misc.PathFinder.PathFindingException;
import me.ci.Conquest.Textures.BuildingTexture;
import me.ci.Conquest.BuildingInterfaces.Researcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Building{
	protected Chunk nwc;
	protected Kingdom kingdom;
	protected boolean building = false;
	protected LinkedList<WraithavenScript> messages = new LinkedList<>();
	private long id;
	protected int hp = 0;
	protected int lvl = 1;
	protected Object[] temp;
	private BuildingType type;
	private StatusType laststatus = StatusType.NORMAL;
	private int messageslastcheck;
	protected boolean alive;
	private boolean trapped;
	private boolean divineprotection;
	private int fire;
	private boolean diseased;
	private final ArrayList<String> spys = new ArrayList<>();
	public static Map<Kingdom,List<Building>> buildings = new HashMap<Kingdom,List<Building>>();
	public Building(final Chunk nwc, final Kingdom kingdom){
		synchronized(Building.class){
			if(WhCommunity.debug)WhCommunity.printDebug();
			Building.this.nwc=nwc;
			Building.this.kingdom=kingdom;
			List<Building> builds;
			if(buildings.containsKey(kingdom))builds=buildings.get(kingdom);
			else builds=new LinkedList<Building>();
			Building.this.id=getNextId(kingdom);
			builds.add(Building.this);
			buildings.put(kingdom, builds);
			kingdom.buildBuilding(Building.this, nwc);
			for(int x = 0; x<getLength(); x++){
				for(int z = 0; z<getLength(); z++){
					PlotMg.getPlotAt(Building.this.nwc.getWorld(), x+nwc.getX(), z+nwc.getZ()).setKingdom(kingdom, Building.this);
				}
			}
			Building.this.hp=getType().getMaxHp(1);
			new BukkitRunnable(){
				public void run(){
					Bukkit.getPluginManager().callEvent(new BuildingBuildEvent(Building.this));
				}
			}.runTaskAsynchronously(WhCommunity.plugin);
			if(this instanceof Capitol)alive=true;
		}
	}
	public Building(Chunk nwc, Kingdom kingdom, BuildingTexture texture){
		synchronized(Building.class){
			this.nwc=nwc;
			this.kingdom=kingdom;
			List<Building> builds;
			if(buildings.containsKey(kingdom))builds=buildings.get(kingdom);
			else builds=new LinkedList<Building>();
			this.id=getNextId(kingdom);
			builds.add(this);
			buildings.put(kingdom, builds);
			kingdom.buildBuilding(this, nwc, texture);
			this.hp=getType().getMaxHp(1);
			for(int x = 0; x<getLength(); x++){
				for(int z = 0; z<getLength(); z++){
					PlotMg.getPlotAt(this.nwc.getWorld(), x+nwc.getX(), z+nwc.getZ()).setKingdom(kingdom, this);
				}
			}
			new BukkitRunnable(){
				public void run(){
					Bukkit.getPluginManager().callEvent(new BuildingBuildEvent(Building.this));
				}
			}.runTaskAsynchronously(WhCommunity.plugin);
			if(this instanceof Capitol)alive=true;
		}
	}
	public Building(Chunk nwc, Kingdom kingdom, Object[] temp){
		synchronized(Building.class){
			this.nwc=nwc;
			this.kingdom=kingdom;
			this.temp=temp;
			List<Building> builds;
			if(buildings.containsKey(kingdom))builds=buildings.get(kingdom);
			else builds=new LinkedList<Building>();
			this.id=getNextId(kingdom);
			builds.add(this);
			buildings.put(kingdom, builds);
			kingdom.buildBuilding(this, nwc, (BuildingTexture)temp[0]);
			this.hp=getType().getMaxHp(1);
			for(int x = 0; x<getLength(); x++){
				for(int z = 0; z<getLength(); z++){
					PlotMg.getPlotAt(this.nwc.getWorld(), x+nwc.getX(), z+nwc.getZ()).setKingdom(kingdom, this);
				}
			}
			new BukkitRunnable(){
				public void run(){
					Bukkit.getPluginManager().callEvent(new BuildingBuildEvent(Building.this));
				}
			}.runTaskAsynchronously(WhCommunity.plugin);
			if(this instanceof Capitol)alive=true;
		}
	}
	public Building(Kingdom kingdom, String code, Chunk nwc, long id){
		this.nwc=nwc;
		this.id=id;
		this.kingdom=kingdom;
		if(code.contains("%")){
			String[] s = code.split("%");
			code=s[0];
			alive=Boolean.valueOf(s[1]);
			String[] s1 = s[2].split(",");
			for(String s2 : s1){ spys.add(s2); }
			trapped=Boolean.valueOf(s[3]);
			fire=Integer.valueOf(s[4]);
			try{ diseased=Boolean.valueOf(s[5]);
			}catch(final Exception exception){}
			try{ divineprotection=Boolean.valueOf(s[6]);
			}catch(final Exception exception){}
		}
		if(kingdom.getOwner()!=null){
			final String in = Save.get("Kingdoms", kingdom.getName(), "Events-"+id);
			if(in!=null){
				String[] events = in.split(";");
				for(String e : events){ messages.add(new WraithavenScript(Save.getFile("Resources", "Events"+File.separatorChar+e+".whs"), new EventProperties(this))); }
			}
		}
		messageslastcheck=messages.size();
		try{ loadSaveStats(code);
		}catch(Exception exception){
			System.err.println("[Conquest] Could not load building! File corrupted!");
			exception.printStackTrace();
		}
	}
	private long getNextId(Kingdom kingdom){
		try{
			List<Building> builds;
			if(buildings.containsKey(kingdom))builds=buildings.get(kingdom);
			else return 0;
			if(builds.size()==0)return 0;
			long i = 0;
			checker:while(true){
				for(Building b : builds){
					if(b.getId()==i){
						i++;
						continue checker;
					}
				}
				return i;
			}
		}catch(ConcurrentModificationException exception){
			try{ Thread.sleep(10);
			}catch(Exception exception2){}
			return getNextId(kingdom);
		}
	}
	public void addEvent(final WraithavenScript script){
		messages.add(script);
		updateStatus();
	}
	public WraithavenScript getOldestScript(){
		if(messages.isEmpty())return null;
		return messages.getFirst();
	}
	public void removeScript(final WraithavenScript script){
		messages.remove(script);
		messageslastcheck=messages.size();
		updateStatus();
	}
	public void updateStatus(final Building... source){
		Status s = new Status(this, getStatus(source), getDamageSegment());
		s.draw();
	}
	public boolean isOffical(){ return buildings.get(kingdom).contains(this); }
	public String getName(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.getClass().getSimpleName();
	}
	public int getLength(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getType().getLength();
	}
	public Chunk getNorthWestCourner(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.nwc;
	}
	public Chunk getSouthEastCourner(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getWorld().getChunkAt(this.nwc.getX()+getLength()-1, this.nwc.getZ()+getLength()-1);
	}
	public World getWorld(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.nwc.getWorld();
	}
	public void updateStatus(StatusType type, byte damagesegment){
		new Status(this, type, damagesegment).draw();
	}
	public BuildingType getType(){
		if(this.type==null)type=BuildingType.getByName(getName());
		return this.type;
	}
	public int getMaxHp(){ return getType().getMaxHp(lvl); }
	public int getMaxLevel(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getType().getMaxLevel();
	}
	public Kingdom getKingdom(){ return this.kingdom; }
	public StatusType getStatus(final Building... source){
		checkForLife();
		StatusType type;
		if(building)type=StatusType.BUILDING;
		else if(this instanceof KingdomManager&&this.kingdom.isRevolting())type=StatusType.REVOLT;
		else if(!this.messages.isEmpty())type=StatusType.MESSAGE;
		else if(getType().needsRoad()&&!this.isAlive())type=StatusType.NOT_ALIVE;
		else if(this instanceof KingdomManager&&this.kingdom.hasPlague())type=StatusType.PLAGUE;
		else if(hasNoResources())type=StatusType.NO_RESORCES;
		else if(hasLowResources())type=StatusType.LOW_RESOURCES;
		else if(this instanceof TroopConvertable&&((TroopConvertable)this).isConvertingTroops())type=StatusType.TROOP_CONVERSION;
		else if(this instanceof Researcher&&((Researcher)this).isResearching())type=StatusType.RESEARCHING;
		else if((this instanceof KingdomManager&&this.kingdom.getIdlingVillagers()>25)||(this instanceof Tavern&&((Tavern)this).getHenchmen()!=null)
				||(this instanceof VillagerWorkable&&((VillagerWorkable)this).getWorkingVillagers()<(int)Math.max(hp*0.08, 1)))type=StatusType.IDLING_VILLAGERS;
		else type=StatusType.NORMAL;
		if(laststatus!=null
				&&type!=laststatus){
			Plot plot;
			for(int x = nwc.getX()-1; x<=nwc.getX()+1; x++){
				for(int z = nwc.getZ()-1; z<=nwc.getZ()+1; z++){
					if(x==nwc.getX()
							&&z==nwc.getZ())continue;
					plot=PlotMg.getPlotAt(getWorld(), x, z);
					if(plot.isKingdomPlot()
							&&plot.getKingdom()==kingdom
							&&!Arrays.asList(source).contains(plot.getBuilding())
							&&plot.getBuilding()!=this)plot.getBuilding().updateStatus(addSource(this, source));
				}
			}
		}
		laststatus=type;
		return type;
	}
	private boolean hasNoResources(){
		if(!(this instanceof ResourceHolder))return false;
		for(int i : ((ResourceHolder)this).getResourceLevels()){ if(i>0)return false; }
		return true;
	}
	private boolean hasLowResources(){
		if(!(this instanceof ResourceHolder))return false;
		for(double i : ((ResourceHolder)this).getResourcePercents()){ if(i<=0.25)return true; }
		return false;
	}
	public void setHp(final int amount, final boolean updategraphics){
		this.hp=amount;
		if(this.hp<1)delete(true, false);
		else updateStatus();
	}
	public int addResources(ResourceType type, int amount){
		if(!this.isAlive())return amount;
		if(this instanceof ResourceHolder){
			ResourceHolder holder = (ResourceHolder)this;
			if(Arrays.asList(holder.getResources()).contains(type)){
				if(holder.getResourceLevel(type)==holder.getMaxResourceLevel())return amount;
				if(holder.getResourceLevel(type)+amount>holder.getMaxResourceLevel()){
					amount=holder.getResourceLevel(type)+amount-holder.getMaxResourceLevel();
					holder.setResource(type, holder.getMaxResourceLevel());
					return amount;
				}
				holder.setResource(type, holder.getResourceLevel(type)+amount);
				return 0;
			}
			updateStatus();
		}
		return amount;
	}
	public void removeResources(ResourceType type, int amount){
		if(this instanceof ResourceHolder){
			ResourceHolder holder = (ResourceHolder)this;
			int r = Math.max(holder.getResourceLevel(type)-amount, 0);
			holder.setResource(type, r);
			updateStatus();
		}
	}
	public int getResources(ResourceType type){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(this instanceof ResourceHolder){
			ResourceHolder holder = (ResourceHolder)this;
			return holder.getResourceLevel(type);
		}
		return 0;
	}
	public boolean isBuildingOrUpgrading(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.building;
	}
	public void setIsBuilding(boolean building){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.building=building;
		updateStatus();
	}
	public long getId(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.id;
	}
	public int getHp(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.hp;
	}
	public int getLevel(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.lvl;
	}
	protected byte getDamageSegment(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		double a = (double)this.hp/getMaxHp()*100;
		if(a==1)return 5;
		if(1<a&&a<=20)return 4;
		if(20<a&&a<=40)return 3;
		if(40<a&&a<=60)return 2;
		if(60<a&&a<=80)return 1;
		return 0;
	}
	public void damage(final Kingdom k, int amount){
		int newhp = Math.max(this.hp-amount, 0);
		if(this instanceof VillagerWorkable){
			final VillagerWorkable work = (VillagerWorkable)this;
			int lost = (int)((this.hp*0.08)-(newhp*0.08));
			lost=Math.min(lost, work.getWorkingVillagers());
			work.setWorkingVillagers(work.getWorkingVillagers()-lost);
		}
		if(this instanceof ResourceHolder){
			final ResourceHolder work = (ResourceHolder)this;
			int lost = Math.max((int)((this.hp*0.75)-(newhp*0.75)), 1);
			int i = 0;
			for(int x : work.getResourceLevels())i+=x;
			i=Math.min(lost, i);
			HashMap<ResourceType,Integer> r = new HashMap<>();
			while(i>0){
				int take = 0;
				ResourceType rt = work.getResources()[(int)(Math.random()*work.getResources().length)];
				take=Math.min(work.getResourceLevel(rt), i);
				r.put(rt, take);
				i-=take;
				removeResources(rt, take);
				int left = 0;
				for(int level : work.getResourceLevels()){ left+=level; }
				if(i>left){
					for(ResourceType re : work.getResources()){ work.setResource(re, 0); }
					break;
				}
			}
			if(k!=null)for(ResourceType rt : r.keySet()){ k.addResources(rt, r.get(rt)); }
		}
		if(newhp>0){
			byte segbefore = getDamageSegment();
			this.hp=newhp;
			byte segafter = getDamageSegment();
			if(segbefore!=segafter)updateStatus();
		}else{
			if(k!=kingdom){
				try{
					final int buffer = PlayerMg.getLandMax(k.getOwner(), 0);
					final int want = getLength()*getLength();
					final int steal = buffer-Math.max(buffer-want, 0);
					if(buffer>0){
						PlayerMg.increasedLandMax(k.getOwner(), -steal, true);
						PlayerMg.increasedLandMax(kingdom.getOwner(), steal, true);
						try{
							final Player player = Bukkit.getPlayer(kingdom.getOwner());
							player.sendMessage(ChatColor.DARK_PURPLE+"You stole "+steal+" buffer land"+(steal==1?"":"s")+" max from "+k.getOwner()+".");
							player.sendMessage(ChatColor.DARK_PURPLE+"Your permanent land max is now "+PlayerMg.getLandMax(player.getName(), 1)+".");
							player.sendMessage(ChatColor.DARK_PURPLE+"Your buffer land max is now "+PlayerMg.getLandMax(player.getName(), 0)+".");
							player.sendMessage(ChatColor.DARK_PURPLE+"Your total land max is now "+PlayerMg.getLandMax(player.getName(), 2)+".");
						}catch(final Exception exception){}
					}
				}catch(final Exception exception){}
			}
			kingdom.removeMorale(getLength());
			delete(true, false);
		}
	}
	public void delete(boolean updatetexture, boolean threadsleep){ kingdom.removeBuilding(this, false, updatetexture, threadsleep); }
	public boolean repair(int amount){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(this.hp==getMaxHp())return false;
		int newhp = Math.min(this.hp+amount, getMaxHp());
		byte segbefore = getDamageSegment();
		this.hp=newhp;
		byte segafter = getDamageSegment();
		if(segbefore!=segafter)updateStatus();
		updateStatus();
		return true;
	}
	@Override
	public String toString(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.kingdom.getName()+";"+this.id+";"+this.hp+";"+this.lvl+";"+this.getName();
	}
	public String getSaveStats(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getType().getId()
				+";"+getNorthWestCourner().getX()+","+getNorthWestCourner().getZ()
				+";"+getHp()
				+";"+getLevel();
	}
	public boolean canBeUpgraded(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getType().canUpgrade(lvl);
	}
	public void setLevel(int lvl){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.lvl=lvl;
		updateStatus();
	}
	public Chunk getCenter(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.nwc.getWorld().getChunkAt(this.nwc.getX()+(getLength()-1/2), this.nwc.getZ()+(getLength()-1/2));
	}
	public ResourceType[] getRepairType(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		try{
			int[] cost = getType().getCost(lvl);
			ArrayList<ResourceType> rs = new ArrayList<ResourceType>();
			rs.add(ResourceType.GOLD);
			if(cost[1]>0)rs.add(ResourceType.WOOD);
			if(cost[2]>0)rs.add(ResourceType.STONE);
			ResourceType[] r = new ResourceType[rs.size()];
			for(int x = 0; x< rs.size(); x++){ r[x]=rs.get(x); }
			return r;
		}catch(Exception exception){ return new ResourceType[]{ResourceType.GOLD}; }
	}
	public String getRepairTypeAsString(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String s = "";
		for(ResourceType r : getRepairType()){
			s+=", "+toTitleCase(r.name().toLowerCase());
		}
		if(s.startsWith(", "))s=s.substring(2);
		return s;
	}
	public String getUpgradeCostAsString(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(this.canBeUpgraded()){
			String s = "";
			int[] cost = getType().getCost(lvl+1);
			if(cost[0]>0)s+=ChatColor.DARK_AQUA+"["+ChatColor.GRAY+"Gold: "+ChatColor.AQUA+cost[0]+ChatColor.DARK_AQUA+"]";
			if(cost[1]>0)s+=ChatColor.DARK_AQUA+"["+ChatColor.GRAY+"Wood: "+ChatColor.AQUA+cost[1]+ChatColor.DARK_AQUA+"]";
			if(cost[2]>0)s+=ChatColor.DARK_AQUA+"["+ChatColor.GRAY+"Stone: "+ChatColor.AQUA+cost[2]+ChatColor.DARK_AQUA+"]";
			s=s.replace("]"+ChatColor.DARK_AQUA+"[", "] [");
			return s;
		}else return ChatColor.GRAY+"Max Level";
	}
	public int getVillagerMaxBonus(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(this instanceof VillagerHoldable){ return (int)(this.hp*0.08); }
		return 0;
	}
	public String getResourcePool(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(this instanceof ResourceHolder){
			String s = "";
			String temp;
			for(ResourceType r : ((ResourceHolder)this).getResources()){
				temp=ChatColor.DARK_AQUA+"["+ChatColor.GRAY+r.name().charAt(0)+": "+ChatColor.AQUA+((ResourceHolder)this).getResourceLevel(r)+"/"+((ResourceHolder)this).getMaxResourceLevel()+ChatColor.DARK_AQUA+"]";
				s+=temp;
			}
			s=s.replace("]"+ChatColor.DARK_AQUA+"[", "] [");
			return s;
		}else return ChatColor.GRAY+"Does not hold resources";
	}
	public int getLandBonus(){
		if(this instanceof LandHolder)return (int)Math.pow((lvl+1)*2+7, 2)+49;
		else return 0;
	}
	public int getMaxResourceLevel(){
		if(this instanceof ResourceHolder)return (int)(hp*0.75/((ResourceHolder)this).getResourceLevels().length);
		else return 0;
	}
	public final boolean isAlive(){
		if(this instanceof Capitol)return true;
		if(!getType().needsRoad())return false;
		return alive;
	}
	public void checkForLife(){
		if(this instanceof Capitol){
			alive=true;
			return;
		}
		if(!getType().needsRoad()){
			alive=false;
			return;
		}
		final ArrayList<Building> b1 = new ArrayList<>(Building.getBuildings(kingdom));
		for(Building b : b1){
			if(b!=this
					&&b instanceof KingdomManager
					&&b.isAlive()
					&&attemptConnect(b)){
				alive=true;
				if(getType()==BuildingType.GRANARY)kingdom.awardAchievement(Achievement.PATH_TO_GREATNESS);
				return;
			}
		}
		alive=false;
	}
	private boolean attemptConnect(final Building end){
		final PathFinder pf = new PathFinder();
		try{
			pf.setStart(pf.getNodeAt(nwc.getX(), nwc.getZ()));
			pf.setEnd(pf.getNodeAt(end.getNorthWestCourner().getX(), end.getNorthWestCourner().getZ()));
			pf.setPathBuilder(new PathBuilder(){
				public ArrayList<Node> getOpenPoints(Node o){
					final ArrayList<Node> nodes = new ArrayList<Node>();
					Plot n = PlotMg.getPlotAt(nwc.getWorld(), o.getX(), o.getY()-1);
					Plot s = PlotMg.getPlotAt(nwc.getWorld(), o.getX(), o.getY()+1);
					Plot e = PlotMg.getPlotAt(nwc.getWorld(), o.getX()+1, o.getY());
					Plot w = PlotMg.getPlotAt(nwc.getWorld(), o.getX()-1, o.getY());
					Plot c = PlotMg.getPlotAt(nwc.getWorld(), o.getX(), o.getY());
					if(Math.abs(n.getHeight()-c.getHeight())<=1
							&&n.isKingdomPlot()
							&&n.getKingdom()==kingdom)
						if(n.getBuilding() instanceof Road
								||n.getBuilding() instanceof Gatehouse
								||n.getBuilding()==Building.this
								||n.getBuilding()==end)nodes.add(pf.getNodeAt(n.getX(), n.getZ()));
					if(Math.abs(s.getHeight()-c.getHeight())<=1
							&&s.isKingdomPlot()
							&&s.getKingdom()==kingdom)
						if(s.getBuilding() instanceof Road
								||s.getBuilding() instanceof Gatehouse
								||s.getBuilding()==Building.this
								||s.getBuilding()==end)nodes.add(pf.getNodeAt(s.getX(), s.getZ()));
					if(Math.abs(e.getHeight()-c.getHeight())<=1
							&&e.isKingdomPlot()
							&&e.getKingdom()==kingdom)
						if(e.getBuilding() instanceof Road
								||e.getBuilding() instanceof Gatehouse
								||e.getBuilding()==Building.this
								||e.getBuilding()==end)nodes.add(pf.getNodeAt(e.getX(), e.getZ()));
					if(Math.abs(w.getHeight()-c.getHeight())<=1
							&&w.isKingdomPlot()
							&&w.getKingdom()==kingdom)
						if(w.getBuilding() instanceof Road
								||w.getBuilding() instanceof Gatehouse
								||w.getBuilding()==Building.this
								||w.getBuilding()==end)nodes.add(pf.getNodeAt(w.getX(), w.getZ()));
					for(Node no : nodes){ no.setMoveCost(1); }
					return nodes;
				}
			});
			pf.findPath(DistanceForumula.MANHATTEN, 25);
			return true;
		}catch(PathFindingException exception){}
		return false;
	}
	public int getNewMessages(){
		final int i = messages.size()-messageslastcheck;
		messageslastcheck=messages.size();
		return i;
	}
	public void saveEvents(){
		String code = "";
		for(WraithavenScript script : messages){ code+=";"+script.getId(); }
		while(code.startsWith(";"))code=code.substring(1);
		Save.set("Kingdoms", kingdom.getName(), "Events-"+getId()+"", code);
	}
	public void burn(){
		if(fire>0){
			damage(null, (int)(Math.random()*400+100));
			fire--;
		}
	}
	public boolean isBurning(){ return fire>0; }
	public void addFire(){ fire+=5; }
	public void addTrap(){ trapped=true; }
	public void removeTrop(){ trapped=false; }
	public boolean hasTrap(){ return trapped; }
	public void addSpy(final Kingdom k){ spys.add(k.getName()); }
	public boolean hasSpy(final Kingdom k){ return spys.contains(k.getName()); }
	public int getOldMessages(){ return Math.max(messages.size()-messageslastcheck, 0); }
	public int getTotalMessages(){ return messages.size(); }
	public boolean isDiseased(){ return diseased; }
	public void setDiseased(){ diseased=true; }
	public void divineProtection(){ divineprotection=true; }
	public boolean hasDivineProtection(){ return divineprotection; }
	public static List<Building> getLoadedBuilding(Kingdom kingdom, BuildingType type){
		List<Building> bs = new ArrayList<Building>();
		for(Building b : buildings.get(kingdom)){ if(b.getType()==type)bs.add(b); }
		return bs;
	}
	public static Building getBuilding(Kingdom kingdom, BuildingType type, String code, Chunk nwc, long id){
		if(type==BuildingType.LAND)return new Land(kingdom, code, nwc, id);
		if(type==BuildingType.CAPITOL)return new Capitol(kingdom, code, nwc, id);
		if(type==BuildingType.ROAD)return new Road(kingdom, code, nwc, id);
		if(type==BuildingType.UNIVERSITY)return new University(kingdom, code, nwc, id);
		if(type==BuildingType.GRANARY)return new Granary(kingdom, code, nwc, id);
		if(type==BuildingType.SAWMILL)return new Sawmill(kingdom, code, nwc, id);
		if(type==BuildingType.STONE_QUARRY)return new StoneQuarry(kingdom, code, nwc, id);
		if(type==BuildingType.BLACKSMITH)return new Blacksmith(kingdom, code, nwc, id);
		if(type==BuildingType.COTTAGE)return new Cottage(kingdom, code, nwc, id);
		if(type==BuildingType.ARMY)return new Army(kingdom, code, nwc, id);
		if(type==BuildingType.BARRACK)return new Barrack(kingdom, code, nwc, id);
		if(type==BuildingType.ANCIENT_DOJO)return new AncientDojo(kingdom, code, nwc, id);
		if(type==BuildingType.ANTIMAGIC_ONAGER_WORKSHOP)return new AntiMagicOnagerWorkshop(kingdom, code, nwc, id);
		if(type==BuildingType.ARCHERY_RANGE)return new ArcheryRange(kingdom, code, nwc, id);
		if(type==BuildingType.BALLISTIC_WORKSHOP)return new BallisticWorkshop(kingdom, code, nwc, id);
		if(type==BuildingType.BATTERING_RAM_WORKSHOP)return new BridgeMakerWorkshop(kingdom, code, nwc, id);
		if(type==BuildingType.BRIDGEMAKER_WORKSHOP)return new BridgeMakerWorkshop(kingdom, code, nwc, id);
		if(type==BuildingType.CATAPULT_WORKSHOP)return new CatapultWorkshop(kingdom, code, nwc, id);
		if(type==BuildingType.CHURCH)return new Church(kingdom, code, nwc, id);
		if(type==BuildingType.COLISEUM)return new Coliseum(kingdom, code, nwc, id);
		if(type==BuildingType.COW_TOSSER_WORKSHOP)return new CowTosserWorkshop(kingdom, code, nwc, id);
		if(type==BuildingType.DRAGON_PIT)return new DragonPit(kingdom, code, nwc, id);
		if(type==BuildingType.DRAGONS_LAIR)return new DragonsLair(kingdom, code, nwc, id);
		if(type==BuildingType.DRUIDS_HALLOW)return new DruidsHallow(kingdom, code, nwc, id);
		if(type==BuildingType.EARTH_TEMPLE)return new EarthTemple(kingdom, code, nwc, id);
		if(type==BuildingType.FIRE_TEMPLE)return new FireTemple(kingdom, code, nwc, id);
		if(type==BuildingType.GRAVEYARD)return new Graveyard(kingdom, code, nwc, id);
		if(type==BuildingType.HOLY_BATTLEFIELD)return new HolyBattlefield(kingdom, code, nwc, id);
		if(type==BuildingType.ILLUSIONISTS_TOWER)return new IllusionistsTower(kingdom, code, nwc, id);
		if(type==BuildingType.MAGES_TOWER)return new MagesTower(kingdom, code, nwc, id);
		if(type==BuildingType.MANGONEL_WORKSHOP)return new MangonelWorkshop(kingdom, code, nwc, id);
		if(type==BuildingType.MEAD_HALL)return new MeadHall(kingdom, code, nwc, id);
		if(type==BuildingType.MILITARY_FORT)return new MilitaryFort(kingdom, code, nwc, id);
		if(type==BuildingType.NOBLE_MANOR)return new NobleManor(kingdom, code, nwc, id);
		if(type==BuildingType.PORTAL_GATE)return new PortalGate(kingdom, code, nwc, id);
		if(type==BuildingType.SANCTUARY)return new Sanctuary(kingdom, code, nwc, id);
		if(type==BuildingType.SIEGE_TOWER_WORKSHOP)return new SiegeTowerWorkshop(kingdom, code, nwc, id);
		if(type==BuildingType.STABLE)return new Stable(kingdom, code, nwc, id);
		if(type==BuildingType.TREBUCHET_WORKSHOP)return new TrebuchetWorkshop(kingdom, code, nwc, id);
		if(type==BuildingType.WALL)return new Wall(kingdom, code, nwc, id);
		if(type==BuildingType.WATER_TEMPLE)return new WaterTemple(kingdom, code, nwc, id);
		if(type==BuildingType.WIND_TEMPLE)return new WindTemple(kingdom, code, nwc, id);
		if(type==BuildingType.METAL_MINING)return new MetalMining(kingdom, code, nwc, id);
		if(type==BuildingType.MOAT)return new Moat(kingdom, code, nwc, id);
		if(type==BuildingType.TRADE_ROUTE)return new TradeRoute(kingdom, code, nwc, id);
		if(type==BuildingType.GATEHOUSE)return new Gatehouse(kingdom, code, nwc, id);
		if(type==BuildingType.WATCH_TOWER)return new WatchTower(kingdom, code, nwc, id);
		if(type==BuildingType.TAVERN)return new Tavern(kingdom, code, nwc, id);
		if(type==BuildingType.MARKETPLACE)return new Marketplace(kingdom, code, nwc, id);
		return null;
	}
	public static void save(Kingdom kingdom){
		if(kingdom==null)return;
		if(!buildings.containsKey(kingdom))return;
		String s;
		for(Building b : buildings.get(kingdom)){
			s="";
			for(String spy : b.spys){ s+=","+spy; }
			while(s.startsWith(","))s=s.substring(1);
			try{ Save.set("Kingdoms",
					kingdom.getName(),
					"B-"+b.getId(),
					b.getSaveStats()
					+"%"+b.isAlive()
					+"%"+s
					+"%"+b.trapped
					+"%"+b.fire
					+"%"+b.diseased);
				b.saveEvents();
			}catch(Exception exception){ exception.printStackTrace(); }
		}
	}
	public static void load(Kingdom kingdom){
		save(kingdom);
		Map<String,String> paths = Save.getAllPaths("Kingdoms", kingdom.getName());
		LinkedList<Building> bs = new LinkedList<Building>();
		for(String s : paths.keySet()){
			try{
				if(s.startsWith("B-")){
					long id = Long.valueOf(s.substring(2));
					String code = paths.get(s);
					String[] a = code.split(";");
					BuildingType type = BuildingType.getById(Integer.valueOf(a[0]));
					Chunk nwc = kingdom.getWorld().getChunkAt(Integer.valueOf(a[1].split(",")[0]), Integer.valueOf(a[1].split(",")[1]));
					bs.add(getBuilding(kingdom, type, code, nwc, id));
				}else if(s.startsWith("C-")){
					String[] names = s.substring(2).split(":");
					ClassType cl = WraithavenScript.generateClassType(names[0]);
					kingdom.getClasses().put(names[1], cl);
					cl.setValueFromString(paths.get(s));
				}
			}catch(Exception exception){ exception.printStackTrace(); }
		}
		buildings.put(kingdom, bs);
	}
	protected static String toTitleCase(String input){
		input=input.replace("_", " ");
		StringBuilder titleCase = new StringBuilder();
		boolean nextTitleCase = true;
		for(char c : input.toCharArray()){
			if(Character.isSpaceChar(c))nextTitleCase=true;
			else if(nextTitleCase){
				c=Character.toTitleCase(c);
				nextTitleCase=false;
			}
			titleCase.append(c);
		}
		return titleCase.toString();
	}
	public static Building getById(Kingdom kingdom, long id){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(kingdom==null)return null;
		List<Building> builds;
		if(buildings.containsKey(kingdom))builds=buildings.get(kingdom);
		else return null;
		for(Building b : builds){ if(b.getId()==id)return b; }
		return null;
	}
	public static synchronized List<Building> getBuildings(Kingdom kingdom){ return buildings.get(kingdom); }
	public static synchronized void deleteBuildings(Kingdom kingdom){ buildings.remove(kingdom); }
	public abstract void updateGraphics(boolean threadsleep);
	public abstract String[] getInfo();
	public abstract void loadSaveStats(String code);
	private static Building[] addSource(final Building a, final Building... b){
		final Building[] c = new Building[b.length+1];
		for(int i = 0; i<b.length; i++){ c[i]=b[i]; }
		c[b.length]=a;
		return c;
	}
}
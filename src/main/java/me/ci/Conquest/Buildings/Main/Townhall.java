package me.ci.Conquest.Buildings.Main;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;

import me.ci.WhCommunity;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.BuildingInterfaces.KingdomManager;
import me.ci.Conquest.BuildingInterfaces.LandHolder;
import me.ci.Conquest.BuildingInterfaces.ResourceHolder;
import me.ci.Conquest.BuildingInterfaces.VillagerHoldable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Misc.ChunkCords;
import me.ci.Conquest.Misc.PathFinder;
import me.ci.Conquest.Misc.PathFinder.DistanceForumula;
import me.ci.Conquest.Misc.PathFinder.Node;
import me.ci.Conquest.Misc.PathFinder.PathBuilder;
import me.ci.Conquest.Textures.ConquestTextures;

public class Townhall extends Building implements KingdomManager, ResourceHolder, VillagerHoldable, LandHolder{
	private int wood;
	private int stone;
	private int iron;
	private int arms;
	private int food;
	public double[] getResourcePercents(){
		double m = getMaxResourceLevel();
		return new double[]{wood/m, stone/m, iron/m, arms/m, food/m};
	}
	public void setResource(ResourceType type, int amount){
		if(type.equals(ResourceType.WOOD))wood=amount;
		if(type.equals(ResourceType.STONE))stone=amount;
		if(type.equals(ResourceType.IRON))iron=amount;
		if(type.equals(ResourceType.ARMS))arms=amount;
		if(type.equals(ResourceType.FOOD))food=amount;
	}
	public int getResourceLevel(ResourceType type){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(type.equals(ResourceType.WOOD))return wood;
		if(type.equals(ResourceType.STONE))return stone;
		if(type.equals(ResourceType.IRON))return iron;
		if(type.equals(ResourceType.ARMS))return arms;
		if(type.equals(ResourceType.FOOD))return food;
		return 0;
	}
	@Override
	public String getSaveStats(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getType().getId()
				+";"+getNorthWestCourner().getX()+","+getNorthWestCourner().getZ()
				+";"+getHp()
				+";"+getLevel()
				+";"+wood+","+stone+","+iron+","+arms+","+food;
	}
	@Override
	public void loadSaveStats(String code){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = code.split(";");
		this.hp=Integer.valueOf(s[2]);
		this.lvl=Integer.valueOf(s[3]);
		String[] r = s[4].split(",");
		wood=Integer.valueOf(r[0]);
		stone=Integer.valueOf(r[1]);
		iron=Integer.valueOf(r[2]);
		arms=Integer.valueOf(r[3]);
		food=Integer.valueOf(r[4]);
	}
	public void updateGraphics(boolean threadsleep){ ConquestTextures.getTexture(getType(), lvl).buildAt(nwc, this); }
	public String[] getInfo(){
		String[] s = new String[19];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"Townhall";
		s[2]=ChatColor.DARK_AQUA+"Gold ................... "+ChatColor.AQUA+getKingdom().getMoney();
		s[3]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[4]=ChatColor.DARK_AQUA+"Information ............ "+ChatColor.AQUA+getNewMessages()+ChatColor.GRAY+" New, "+ChatColor.AQUA+getOldMessages()+ChatColor.GRAY+" Unanswered Messages";
		s[5]=ChatColor.DARK_AQUA+"Idle Villagers ......... "+ChatColor.AQUA+getKingdom().getIdlingVillagers();
		s[6]=ChatColor.DARK_AQUA+"Kingdom Name ........... "+ChatColor.GRAY+getKingdom().getName();
		s[7]=ChatColor.DARK_AQUA+"Lands Owned ............ "+ChatColor.AQUA+getKingdom().getLandOwned()+"/"+getKingdom().getLandMax();
		s[8]=ChatColor.DARK_AQUA+"Population ............. "+ChatColor.AQUA+getKingdom().getVillagers(true)+"/"+getKingdom().getVillagerMax();
		s[9]=ChatColor.DARK_AQUA+"Repair Type ............ "+ChatColor.GRAY+getRepairTypeAsString();
		s[10]=ChatColor.DARK_AQUA+"Resource Pool .......... "+getResourcePool();
		s[11]=ChatColor.DARK_AQUA+"Ruler .................. "+ChatColor.GRAY+getKingdom().getOwner();
		s[12]=ChatColor.DARK_AQUA+"Status ................. "+ChatColor.GRAY+toTitleCase(getStatus().name().toLowerCase());
		s[13]=ChatColor.DARK_AQUA+"Tech Level ............. "+ChatColor.AQUA+getLevel();
		s[14]=ChatColor.DARK_AQUA+"Type of Building ....... "+ChatColor.GRAY+"Primary";
		s[15]=ChatColor.DARK_AQUA+"Upgrade Cost ........... "+getUpgradeCostAsString();
		s[16]=ChatColor.DARK_AQUA+"Connected to Kingdom ... "+ChatColor.GRAY+alive;
		s[17]=ChatColor.DARK_AQUA+"Villagers Storage ...... "+ChatColor.AQUA+"+"+getVillagerMaxBonus()+ChatColor.GRAY+" Villager Max";
		s[18]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
	@Override
	public void checkForLife(){
		final PathFinder pf = new PathFinder();
		try{
			final ChunkCords cc;
			try{ cc=kingdom.getBuildingLocations(BuildingType.CAPITOL).get(0);
			}catch(final Exception exception){
				alive=false;
				return;
			}
			pf.setStart(pf.getNodeAt(nwc.getX(), nwc.getZ()));
			pf.setEnd(pf.getNodeAt(cc.getX(), cc.getZ()));
			pf.setPathBuilder(new PathBuilder(){
				public ArrayList<Node> getOpenPoints(Node o){
					final ArrayList<Node> nodes = new ArrayList<>();
					final Plot n = PlotMg.getPlotAt(getWorld(), o.getX(), o.getY()-1);
					final Plot s = PlotMg.getPlotAt(getWorld(), o.getX(), o.getY()+1);
					final Plot w = PlotMg.getPlotAt(getWorld(), o.getX()-1, o.getY());
					final Plot e = PlotMg.getPlotAt(getWorld(), o.getX()+1, o.getY());
					final Plot c = PlotMg.getPlotAt(getWorld(), o.getX(), o.getY());
					if(Math.abs(n.getHeight()-c.getHeight())<=1
							&&n.isKingdomPlot()
							&&n.getKingdom()==kingdom)
						if(n.getBuilding()==Townhall.this
								||n.getBuilding() instanceof Capitol
								||n.getBuilding() instanceof Gatehouse
								||n.getBuilding() instanceof TradeRoute)nodes.add(pf.getNodeAt(n.getX(), n.getZ()));
					if(Math.abs(s.getHeight()-c.getHeight())<=1
							&&s.isKingdomPlot()
							&&s.getKingdom()==kingdom)
						if(s.getBuilding()==Townhall.this
								||s.getBuilding() instanceof Capitol
								||s.getBuilding() instanceof Gatehouse
								||s.getBuilding() instanceof TradeRoute)nodes.add(pf.getNodeAt(s.getX(), s.getZ()));
					if(Math.abs(w.getHeight()-c.getHeight())<=1
							&&w.isKingdomPlot()
							&&w.getKingdom()==kingdom)
						if(w.getBuilding()==Townhall.this
								||w.getBuilding() instanceof Capitol
								||w.getBuilding() instanceof Gatehouse
								||w.getBuilding() instanceof TradeRoute)nodes.add(pf.getNodeAt(w.getX(), w.getZ()));
					if(Math.abs(e.getHeight()-c.getHeight())<=1
							&&e.isKingdomPlot()
							&&e.getKingdom()==kingdom)
						if(e.getBuilding()==Townhall.this
								||e.getBuilding() instanceof Capitol
								||e.getBuilding() instanceof Gatehouse
								||e.getBuilding() instanceof TradeRoute)nodes.add(pf.getNodeAt(e.getX(), e.getZ()));
					return nodes;
				}
			});
			pf.findPath(DistanceForumula.MANHATTEN, 0);
			alive=true;
			return;
		}catch(final Exception exception){}
		alive=false;
	}
	public Townhall(Chunk NWcourner, Kingdom kingdom){ super(NWcourner, kingdom); }
	public Townhall(Kingdom kingdom, String code, Chunk nwc, long id){ super(kingdom, code, nwc, id); }
	public int[] getResourceLevels(){ return new int[]{wood, stone, iron, arms, food}; }
	public ResourceType[] getResources(){ return new ResourceType[]{ResourceType.WOOD, ResourceType.STONE, ResourceType.IRON, ResourceType.ARMS, ResourceType.FOOD}; }
}
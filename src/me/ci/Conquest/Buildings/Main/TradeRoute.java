package me.ci.Conquest.Buildings.Main;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;

import me.ci.WhCommunity;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.BuildingInterfaces.Reshapeable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.ArmyFormation;
import me.ci.Conquest.Misc.PathFinder;
import me.ci.Conquest.Misc.PathFinder.DistanceForumula;
import me.ci.Conquest.Misc.PathFinder.Node;
import me.ci.Conquest.Misc.PathFinder.PathBuilder;
import me.ci.Conquest.Misc.PathFinder.PathFindingException;
import me.ci.Conquest.Textures.BuildingTexture;
import me.ci.Conquest.Textures.ConquestTextures;

public class TradeRoute extends Building implements Reshapeable{
	public TradeRoute(Chunk nwc, Kingdom kingdom){
		super(nwc, kingdom, new BuildingTexture());
	}
	public TradeRoute(Kingdom kingdom, String code, Chunk nwc, long id){
		super(kingdom, code, nwc, id);
	}
	@Override
	public String getSaveStats(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getType().getId()
				+";"+getNorthWestCourner().getX()+","+getNorthWestCourner().getZ()
				+";"+getHp()
				+";"+getLevel();
	}
	@Override
	public void loadSaveStats(String code){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = code.split(";");
		this.hp=Integer.valueOf(s[2]);
		this.lvl=Integer.valueOf(s[3]);
	}
	public boolean connectsTo(BuildingType type){
		if(type==BuildingType.CAPITOL)return true;
		if(type==BuildingType.TOWNHALL)return true;
		if(type==BuildingType.TRADE_ROUTE)return true;
		if(type==BuildingType.GATEHOUSE)return true;
		return false;
	}
	public void updateGraphics(boolean threadsleep){
		boolean n1 = false;
		boolean e1 = false;
		boolean s1 = false;
		boolean w1 = false;
		int h = PlotMg.getPlotAt(nwc).getHeight();
		Plot plot = PlotMg.getPlotAt(getWorld(), nwc.getX(), nwc.getZ()-1);
		if(Math.abs(h-plot.getHeight())<=getHeightVariance()
				&&plot.isKingdomPlot()
				&&plot.getKingdom().equals(kingdom)
				&&connectsTo(plot.getBuilding().getType()))n1=true;
		plot=PlotMg.getPlotAt(getWorld(), nwc.getX(), nwc.getZ()+1);
		if(Math.abs(h-plot.getHeight())<=getHeightVariance()
				&&plot.isKingdomPlot()
				&&plot.getKingdom().equals(kingdom)
				&&connectsTo(plot.getBuilding().getType()))s1=true;
		plot=PlotMg.getPlotAt(getWorld(), nwc.getX()-1, nwc.getZ());
		if(Math.abs(h-plot.getHeight())<=getHeightVariance()
				&&plot.isKingdomPlot()
				&&plot.getKingdom().equals(kingdom)
				&&connectsTo(plot.getBuilding().getType()))w1=true;
		plot=PlotMg.getPlotAt(getWorld(), nwc.getX()+1, nwc.getZ());
		if(Math.abs(h-plot.getHeight())<=getHeightVariance()
				&&plot.isKingdomPlot()
				&&plot.getKingdom().equals(kingdom)
				&&connectsTo(plot.getBuilding().getType()))e1=true;
		ArmyFormation form = ArmyFormation.getBySides(n1, e1, s1, w1);
		ConquestTextures.getTexture(BuildingType.TRADE_ROUTE, BuildingType.TRADE_ROUTE.getGraphicsLevelFor(form, lvl)).buildAt(nwc, TradeRoute.this);
	}
	public String[] getInfo(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = new String[13];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"TradeRoute";
		s[2]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[3]=ChatColor.DARK_AQUA+"Information ............ "+ChatColor.AQUA+getNewMessages()+ChatColor.GRAY+" New, "+ChatColor.AQUA+getOldMessages()+ChatColor.GRAY+" Unanswered Messages";
		s[4]=ChatColor.DARK_AQUA+"Kingdom Name ........... "+ChatColor.GRAY+getKingdom().getName();
		s[5]=ChatColor.DARK_AQUA+"Repair Type ............ "+ChatColor.GRAY+getRepairTypeAsString();
		s[6]=ChatColor.DARK_AQUA+"Resource Pool .......... "+getResourcePool();
		s[7]=ChatColor.DARK_AQUA+"Status ................. "+ChatColor.GRAY+toTitleCase(getStatus().name().toLowerCase());
		s[8]=ChatColor.DARK_AQUA+"Tech Level ............. "+ChatColor.AQUA+getLevel();
		s[9]=ChatColor.DARK_AQUA+"Type of Building ....... "+ChatColor.GRAY+"Travelable";
		s[10]=ChatColor.DARK_AQUA+"Upgrade Cost ........... "+getUpgradeCostAsString();
		s[11]=ChatColor.DARK_AQUA+"Connected to Kingdom ... "+ChatColor.GRAY+isAlive();
		s[12]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
	public int getHeightVariance(){ return 1; }
	@Override
	public void checkForLife(){
		final PathFinder pf = new PathFinder();
		try{
			final Building end = Building.getLoadedBuilding(kingdom, BuildingType.CAPITOL).get(0);
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
						if(n.getBuilding() instanceof TradeRoute
								||n.getBuilding() instanceof Gatehouse
								||n.getBuilding() instanceof Capitol
								||n.getBuilding() instanceof Townhall
								||n.getBuilding()==end)nodes.add(pf.getNodeAt(n.getX(), n.getZ()));
					if(Math.abs(s.getHeight()-c.getHeight())<=1
							&&s.isKingdomPlot()
							&&s.getKingdom()==kingdom)
						if(s.getBuilding() instanceof TradeRoute
								||s.getBuilding() instanceof Gatehouse
								||s.getBuilding() instanceof Capitol
								||s.getBuilding() instanceof Townhall
								||s.getBuilding()==end)nodes.add(pf.getNodeAt(s.getX(), s.getZ()));
					if(Math.abs(e.getHeight()-c.getHeight())<=1
							&&e.isKingdomPlot()
							&&e.getKingdom()==kingdom)
						if(e.getBuilding() instanceof TradeRoute
								||e.getBuilding() instanceof Gatehouse
								||e.getBuilding() instanceof Capitol
								||e.getBuilding() instanceof Townhall
								||e.getBuilding()==end)nodes.add(pf.getNodeAt(e.getX(), e.getZ()));
					if(Math.abs(w.getHeight()-c.getHeight())<=1
							&&w.isKingdomPlot()
							&&w.getKingdom()==kingdom)
						if(w.getBuilding() instanceof TradeRoute
								||w.getBuilding() instanceof Gatehouse
								||w.getBuilding() instanceof Capitol
								||w.getBuilding() instanceof Townhall
								||w.getBuilding()==end)nodes.add(pf.getNodeAt(w.getX(), w.getZ()));
					for(Node no : nodes){ no.setMoveCost(1); }
					return nodes;
				}
			});
			pf.findPath(DistanceForumula.MANHATTEN, 0);
			alive=true;
			return;
		}catch(PathFindingException exception){}
		alive=false;
	}
}
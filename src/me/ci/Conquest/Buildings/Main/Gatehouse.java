package me.ci.Conquest.Buildings.Main;

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
import me.ci.Conquest.Textures.BuildingTexture;
import me.ci.Conquest.Textures.ConquestTextures;

public class Gatehouse extends Building implements Reshapeable{
	public Gatehouse(Chunk nwc, Kingdom kingdom){
		super(nwc, kingdom, new BuildingTexture());
	}
	public Gatehouse(Kingdom kingdom, String code, Chunk nwc, long id){
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
	public boolean connectsTo(final BuildingType type){
		return type==BuildingType.WALL
				||type==BuildingType.GATEHOUSE;
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
		ConquestTextures.getTexture(BuildingType.GATEHOUSE, BuildingType.GATEHOUSE.getGraphicsLevelFor(form, lvl)).buildAt(nwc, Gatehouse.this);
	}
	public String[] getInfo(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = new String[13];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"Gatehouse";
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
}
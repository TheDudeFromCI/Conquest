package me.ci.Conquest.Buildings.Main;

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

import org.bukkit.ChatColor;
import org.bukkit.Chunk;

public class Wall extends Building implements Reshapeable{
	public Wall(Chunk nwc, Kingdom kingdom){ super(nwc, kingdom, new BuildingTexture()); }
	public Wall(Kingdom kingdom, String code, Chunk nwc, long id){ super(kingdom, code, nwc, id); }
	@Override
	public String getSaveStats(){
		return getType().getId()
				+";"+getNorthWestCourner().getX()+","+getNorthWestCourner().getZ()
				+";"+getHp()
				+";"+getLevel();
	}
	@Override
	public void loadSaveStats(String code){
		String[] s = code.split(";");
		this.hp=Integer.valueOf(s[2]);
		this.lvl=Integer.valueOf(s[3]);
	}
	public void updateGraphics(boolean threadsleep){
		boolean n1 = false;
		boolean e1 = false;
		boolean s1 = false;
		boolean w1 = false;
		Plot plot = PlotMg.getPlotAt(getWorld(), nwc.getX(), nwc.getZ()-1);
		if(plot.isKingdomPlot()
				&&plot.getKingdom().equals(kingdom)
				&&connectsTo(plot.getBuilding().getType()))n1=true;
		plot=PlotMg.getPlotAt(getWorld(), nwc.getX(), nwc.getZ()+1);
		if(plot.isKingdomPlot()
				&&plot.getKingdom().equals(kingdom)
				&&connectsTo(plot.getBuilding().getType()))s1=true;
		plot=PlotMg.getPlotAt(getWorld(), nwc.getX()-1, nwc.getZ());
		if(plot.isKingdomPlot()
				&&plot.getKingdom().equals(kingdom)
				&&connectsTo(plot.getBuilding().getType()))w1=true;
		plot=PlotMg.getPlotAt(getWorld(), nwc.getX()+1, nwc.getZ());
		if(plot.isKingdomPlot()
				&&plot.getKingdom().equals(kingdom)
				&&connectsTo(plot.getBuilding().getType()))e1=true;
		ArmyFormation form = ArmyFormation.getBySides(n1, e1, s1, w1);
		ConquestTextures.getTexture(BuildingType.WALL, BuildingType.WALL.getGraphicsLevelFor(form, lvl)).buildAt(nwc, this);
	}
	public String[] getInfo(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = new String[13];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"Wall";
		s[2]=ChatColor.DARK_AQUA+"Kingdom Name ........... "+ChatColor.GRAY+getKingdom().getName();
		s[3]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[4]=ChatColor.DARK_AQUA+"Information ............ "+ChatColor.AQUA+getNewMessages()+ChatColor.GRAY+" New, "+ChatColor.AQUA+getOldMessages()+ChatColor.GRAY+" Unanswered Messages";
		s[6]=ChatColor.DARK_AQUA+"Repair Type ............ "+ChatColor.GRAY+getRepairTypeAsString();
		s[8]=ChatColor.DARK_AQUA+"Status ................. "+ChatColor.GRAY+toTitleCase(getStatus().name().toLowerCase());
		s[9]=ChatColor.DARK_AQUA+"Tech Level ............. "+ChatColor.AQUA+getLevel();
		s[10]=ChatColor.DARK_AQUA+"Type of Building ....... "+ChatColor.GRAY+"Defensive";
		s[11]=ChatColor.DARK_AQUA+"Upgrade Cost ........... "+getUpgradeCostAsString();
		s[12]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
	public boolean connectsTo(BuildingType type){
		return type==BuildingType.WALL
			||type==BuildingType.GATEHOUSE;
	}
	public int getHeightVariance(){ return 5; }
}
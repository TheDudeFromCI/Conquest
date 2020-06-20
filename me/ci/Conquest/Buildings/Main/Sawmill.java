package me.ci.Conquest.Buildings.Main;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;

import me.ci.WhCommunity;
import me.ci.Conquest.BuildingInterfaces.ResourceHolder;
import me.ci.Conquest.BuildingInterfaces.VillagerWorkable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Textures.ConquestTextures;

public class Sawmill extends Building implements ResourceHolder, VillagerWorkable{
	private int wood;
	private int workers;
	public Sawmill(Chunk nwc, Kingdom kingdom){
		super(nwc, kingdom);
		if(WhCommunity.debug)WhCommunity.printDebug();
	}
	public Sawmill(Kingdom kingdom, String code, Chunk nwc, long id){
		super(kingdom, code, nwc, id);
		if(WhCommunity.debug)WhCommunity.printDebug();
	}
	@Override
	public String getSaveStats(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getType().getId()
				+";"+getNorthWestCourner().getX()+","+getNorthWestCourner().getZ()
				+";"+getHp()
				+";"+getLevel()
				+";"+wood
				+";"+workers;
	}
	@Override
	public void loadSaveStats(String code){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = code.split(";");
		this.hp=Integer.valueOf(s[2]);
		this.lvl=Integer.valueOf(s[3]);
		this.wood=Integer.valueOf(s[4]);
		this.workers=Integer.valueOf(s[5]);
	}
	public void updateGraphics(boolean threadsleep){
		ConquestTextures.getTexture(getType(), lvl).buildAt(nwc, this);
	}
	public int[] getResourceLevels(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return new int[]{wood};
	}
	public ResourceType[] getResources(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return new ResourceType[]{ResourceType.WOOD};
	}
	public double[] getResourcePercents(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return new double[]{wood/(double)getMaxResourceLevel()};
	}
	public void setResource(ResourceType type, int amount){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(type==ResourceType.WOOD)wood=amount;
	}
	public int getResourceLevel(ResourceType type){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(type==ResourceType.WOOD)return wood;
		return 0;
	}
	public int getWorkingVillagers(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.workers;
	}
	public void setWorkingVillagers(int amount){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.workers=amount;
	}
	public String[] getInfo(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = new String[15];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"Sawmill";
		s[2]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[3]=ChatColor.DARK_AQUA+"Information ............ "+ChatColor.AQUA+getNewMessages()+ChatColor.GRAY+" New, "+ChatColor.AQUA+getOldMessages()+ChatColor.GRAY+" Unanswered Messages";
		s[4]=ChatColor.DARK_AQUA+"Workers ................ "+ChatColor.AQUA+this.workers+"/"+(int)Math.max(hp*0.08, 1);
		s[5]=ChatColor.DARK_AQUA+"Kingdom Name ........... "+ChatColor.GRAY+getKingdom().getName();
		s[6]=ChatColor.DARK_AQUA+"Repair Type ............ "+ChatColor.GRAY+getRepairTypeAsString();
		s[7]=ChatColor.DARK_AQUA+"Resource Pool .......... "+getResourcePool();
		s[8]=ChatColor.DARK_AQUA+"Status ................. "+ChatColor.GRAY+toTitleCase(getStatus().name().toLowerCase());
		s[9]=ChatColor.DARK_AQUA+"Tech Level ............. "+ChatColor.AQUA+getLevel();
		s[10]=ChatColor.DARK_AQUA+"Type of Building ....... "+ChatColor.GRAY+"Storage";
		s[11]=ChatColor.DARK_AQUA+"Upgrade Cost ........... "+getUpgradeCostAsString();
		s[12]=ChatColor.DARK_AQUA+"Connected to Kingdom ... "+ChatColor.GRAY+isAlive();
		s[13]=ChatColor.DARK_AQUA+"Resources Per Minute ... "+ChatColor.AQUA+(int)(kingdom.getMorale()/5.0*10/100*getWorkingVillagers()*(Arrays.asList(getResources()).contains(kingdom.getMainResource())?2:1));
		s[14]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
}
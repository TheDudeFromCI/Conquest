package me.ci.Conquest.Buildings.Main;

import me.ci.Conquest.BuildingInterfaces.VillagerWorkable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Textures.ConquestTextures;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;

public class WatchTower extends Building implements VillagerWorkable{
	private int villagers;
	@Override
	public String getSaveStats(){
		return getType().getId()
				+";"+getNorthWestCourner().getX()+","+getNorthWestCourner().getZ()
				+";"+getHp()
				+";"+getLevel()
				+";"+villagers;
	}
	public void loadSaveStats(String code){
		String[] s = code.split(";");
		this.hp=Integer.valueOf(s[2]);
		this.lvl=Integer.valueOf(s[3]);
		this.villagers=Integer.valueOf(s[4]);
	}
	public String[] getInfo(){
		String[] s = new String[15];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"WatchTower";
		s[2]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[3]=ChatColor.DARK_AQUA+"Information ............ "+ChatColor.AQUA+getNewMessages()+ChatColor.GRAY+" New, "+ChatColor.AQUA+getOldMessages()+ChatColor.GRAY+" Unanswered Messages";
		s[4]=ChatColor.DARK_AQUA+"Kingdom Name ........... "+ChatColor.GRAY+getKingdom().getName();
		s[5]=ChatColor.DARK_AQUA+"Repair Type ............ "+ChatColor.GRAY+getRepairTypeAsString();
		s[6]=ChatColor.DARK_AQUA+"Ruler .................. "+ChatColor.GRAY+getKingdom().getOwner();
		s[7]=ChatColor.DARK_AQUA+"Status ................. "+ChatColor.GRAY+toTitleCase(getStatus().name().toLowerCase());
		s[8]=ChatColor.DARK_AQUA+"Tech Level ............. "+ChatColor.AQUA+getLevel();
		s[9]=ChatColor.DARK_AQUA+"Type of Building ....... "+ChatColor.GRAY+"Defensive";
		s[10]=ChatColor.DARK_AQUA+"Upgrade Cost .......... "+getUpgradeCostAsString();
		s[11]=ChatColor.DARK_AQUA+"Spotters .............. "+ChatColor.AQUA+villagers+"/"+(int)Math.max(hp*0.08, 1);
		s[12]=ChatColor.DARK_AQUA+"Sighting Chance ....... "+ChatColor.AQUA+getSightChance()+"%";
		s[13]=ChatColor.DARK_AQUA+"Range ................. "+ChatColor.AQUA+getRange();
		s[14]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
	public double getSightChance(){ return villagers/Math.max(hp*0.08, 1)*100; }
	public double getRange(){ return Math.max(hp*0.05/16, 1); }
	public int getWorkingVillagers(){ return villagers; }
	public void setWorkingVillagers(final int amount){ villagers=amount; }
	public WatchTower(Chunk nwc, Kingdom kingdom){ super(nwc, kingdom); }
	public WatchTower(Kingdom kingdom, String code, Chunk nwc, long id){ super(kingdom, code, nwc, id); }
	public void updateGraphics(boolean threadsleep){ ConquestTextures.getTexture(BuildingType.WATCH_TOWER, lvl).buildAt(nwc, this); }
}
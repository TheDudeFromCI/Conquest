package me.ci.Conquest.Buildings.Main;

import me.ci.WhCommunity;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Textures.ConquestTextures;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;

public class Lake extends Moat{
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
	public String[] getInfo(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = new String[13];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"Lake";
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
	public void updateGraphics(boolean threadsleep){ ConquestTextures.getTexture(BuildingType.LAKE, 1); }
	public Lake(Chunk nwc, Kingdom kingdom){ super(nwc, kingdom); }
	public Lake(Kingdom kingdom, String code, Chunk nwc, long id){ super(kingdom, code, nwc, id); }
}
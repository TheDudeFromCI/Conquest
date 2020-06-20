package me.ci.Conquest.Buildings.Main;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;

import me.ci.WhCommunity;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Textures.ConquestTextures;

public class University extends Building{
	public University(Chunk nwc, Kingdom kingdom){
		super(nwc, kingdom);
		if(WhCommunity.debug)WhCommunity.printDebug();
	}
	public University(Kingdom kingdom, String code, Chunk nwc, long id){
		super(kingdom, code, nwc, id);
		if(WhCommunity.debug)WhCommunity.printDebug();
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
	public void updateGraphics(boolean threadsleep){
		ConquestTextures.getTexture(getType(), lvl).buildAt(nwc, this);
	}
	public String[] getInfo(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = new String[12];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"University";
		s[2]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[3]=ChatColor.DARK_AQUA+"Information ............ "+ChatColor.AQUA+getNewMessages()+ChatColor.GRAY+" New, "+ChatColor.AQUA+getOldMessages()+ChatColor.GRAY+" Unanswered Messages";
		s[4]=ChatColor.DARK_AQUA+"Kingdom Name ........... "+ChatColor.GRAY+getKingdom().getName();
		s[5]=ChatColor.DARK_AQUA+"Repair Type ............ "+ChatColor.GRAY+getRepairTypeAsString();
		s[6]=ChatColor.DARK_AQUA+"Status ................. "+ChatColor.GRAY+toTitleCase(getStatus().name().toLowerCase());
		s[7]=ChatColor.DARK_AQUA+"Tech Level ............. "+ChatColor.AQUA+getLevel();
		s[8]=ChatColor.DARK_AQUA+"Type of Building ....... "+ChatColor.GRAY+"Educational";
		s[9]=ChatColor.DARK_AQUA+"Upgrade Cost ........... "+getUpgradeCostAsString();
		s[10]=ChatColor.DARK_AQUA+"Connected to Kingdom ... "+ChatColor.GRAY+isAlive();
		s[11]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
}
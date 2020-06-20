package me.ci.Conquest.Buildings.Main;

import me.ci.WhCommunity;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.HenchmenType;
import me.ci.Conquest.Textures.ConquestTextures;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;

public class Tavern extends Building{
	private HenchmenType man;
	private int cost;
	public Tavern(Chunk nwc, Kingdom kingdom){ super(nwc, kingdom); }
	public Tavern(Kingdom kingdom, String code, Chunk nwc, long id){ super(kingdom, code, nwc, id); }
	@Override
	public String getSaveStats(){
		return getType().getId()
				+";"+getNorthWestCourner().getX()+","+getNorthWestCourner().getZ()
				+";"+getHp()
				+";"+getLevel()
				+";"+(man==null?-1:man.getId())
				+";"+cost;
	}
	@Override
	public void loadSaveStats(String code){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = code.split(";");
		this.hp=Integer.valueOf(s[2]);
		this.lvl=Integer.valueOf(s[3]);
		this.man=HenchmenType.getById(Integer.valueOf(s[4]));
		this.cost=Integer.valueOf(s[5]);
	}
	public void updateGraphics(boolean threadsleep){ ConquestTextures.getTexture(BuildingType.TAVERN, lvl).buildAt(nwc, this); }
	public String[] getInfo(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = new String[14];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"Tavern";
		s[2]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[3]=ChatColor.DARK_AQUA+"Information ............ "+ChatColor.AQUA+getNewMessages()+ChatColor.GRAY+" New, "+ChatColor.AQUA+getOldMessages()+ChatColor.GRAY+" Unanswered Messages";
		s[4]=ChatColor.DARK_AQUA+"Kingdom Name ........... "+ChatColor.GRAY+getKingdom().getName();
		s[5]=ChatColor.DARK_AQUA+"Repair Type ............ "+ChatColor.GRAY+getRepairTypeAsString();
		s[6]=ChatColor.DARK_AQUA+"Ruler .................. "+ChatColor.GRAY+getKingdom().getOwner();
		s[7]=ChatColor.DARK_AQUA+"Status ................. "+ChatColor.GRAY+toTitleCase(getStatus().name().toLowerCase());
		s[8]=ChatColor.DARK_AQUA+"Tech Level ............. "+ChatColor.AQUA+getLevel();
		s[9]=ChatColor.DARK_AQUA+"Type of Building ....... "+ChatColor.GRAY+"Entertainment";
		s[10]=ChatColor.DARK_AQUA+"Upgrade Cost ........... "+getUpgradeCostAsString();
		s[11]=ChatColor.DARK_AQUA+"Henchmen ............... "+ChatColor.GRAY+(man==null?"None":man.getName());
		s[12]=ChatColor.DARK_AQUA+"Cost ................... "+ChatColor.AQUA+cost;
		s[13]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
	public void addHenchmen(){
		if(man!=null)return;
		int id = (int)(Math.random()*HenchmenType.values().length);
		man=HenchmenType.values()[id];
		cost=(int)(Math.random()*(man.getMaxCost()-man.getMinCost())+man.getMinCost());
		updateStatus();
	}
	public void collectHenchmen(){
		man=null;
		cost=0;
		updateStatus();
	}
	public int getCost(){ return cost; }
	public HenchmenType getHenchmen(){ return man; }
}
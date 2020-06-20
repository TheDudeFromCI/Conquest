
package me.ci.Conquest.Buildings.Main;


import java.util.Map;

import me.ci.WhCommunity;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Textures.BuildingTexture;
import me.ci.Conquest.Textures.ConquestTextures;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;

public class Land extends Building{
	private ResourceType resourceheld;
	public Land(Chunk nwc, Kingdom kingdom){
		super(nwc, kingdom, scout(kingdom));
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.resourceheld=(ResourceType)this.temp[1];
	}
	public Land(Kingdom kingdom, String code, Chunk nwc, long id){
		super(kingdom, code, nwc, id);
		if(WhCommunity.debug)WhCommunity.printDebug();
	}
	public Land(Chunk nwc, Kingdom kingdom, ResourceType r){
		super(nwc, kingdom, getBaseTexture(r));
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.resourceheld=r;
	}
	private static BuildingTexture getBaseTexture(ResourceType r){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(r.equals(ResourceType.FOOD))return ConquestTextures.getTexture(BuildingType.LAND, 1);
		if(r.equals(ResourceType.GOLD))return ConquestTextures.getTexture(BuildingType.LAND, 2);
		if(r.equals(ResourceType.IRON))return ConquestTextures.getTexture(BuildingType.LAND, 3);
		if(r.equals(ResourceType.STONE))return ConquestTextures.getTexture(BuildingType.LAND, 4);
		if(r.equals(ResourceType.WOOD))return ConquestTextures.getTexture(BuildingType.LAND, 5);
		return ConquestTextures.getTexture(BuildingType.LAND, 6);
	}
	private static Object[] scout(Kingdom kingdom){
		if(WhCommunity.debug)WhCommunity.printDebug();
		Object[] temp = new Object[2];
		Map<ResourceType,Integer> c = kingdom.getResourcePercents();
		int r = (int)(Math.random()*250.0+1);
		if(r<=c.get(ResourceType.FOOD)){
			temp[0]=(Object)ConquestTextures.getTexture(BuildingType.LAND, 1);
			temp[1]=ResourceType.FOOD;
		}else if(r<=c.get(ResourceType.FOOD)
				+c.get(ResourceType.GOLD)){
			temp[0]=(Object)ConquestTextures.getTexture(BuildingType.LAND, 2);
			temp[1]=ResourceType.GOLD;
		}else if(r<=c.get(ResourceType.FOOD)
				+c.get(ResourceType.GOLD)
				+c.get(ResourceType.IRON)){
			temp[0]=(Object)ConquestTextures.getTexture(BuildingType.LAND, 3);
			temp[1]=ResourceType.IRON;
		}else if(r<=c.get(ResourceType.FOOD)
				+c.get(ResourceType.GOLD)
				+c.get(ResourceType.IRON)
				+c.get(ResourceType.STONE)){
			temp[0]=(Object)ConquestTextures.getTexture(BuildingType.LAND, 4);
			temp[1]=ResourceType.STONE;
		}else if(r<=c.get(ResourceType.FOOD)
				+c.get(ResourceType.GOLD)
				+c.get(ResourceType.IRON)
				+c.get(ResourceType.STONE)
				+c.get(ResourceType.WOOD)){
			temp[0]=(Object)ConquestTextures.getTexture(BuildingType.LAND, 5);
			temp[1]=ResourceType.WOOD;
		}else{
			temp[0]=(Object)ConquestTextures.getTexture(BuildingType.LAND, 6);
			temp[1]=ResourceType.NONE;
		}
		return temp;
	}
	@Override
	public String getSaveStats(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getType().getId()
				+";"+getNorthWestCourner().getX()+","+getNorthWestCourner().getZ()
				+";"+getHp()
				+";"+getLevel()
				+";"+this.resourceheld.getId();
	}
	@Override
	public void loadSaveStats(String code){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = code.split(";");
		this.hp=Integer.valueOf(s[2]);
		this.lvl=Integer.valueOf(s[3]);
		this.resourceheld=ResourceType.getById(Integer.valueOf(s[4]));
	}
	public ResourceType getHeldResource(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.resourceheld;
	}
	public void setHeldResource(ResourceType type){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.resourceheld=type;
	}
	public void updateGraphics(boolean threadsleep){
		getBaseTexture(resourceheld).buildAt(nwc, this);
	}
	public String[] getInfo(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = new String[12];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+"Land";
		s[2]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[3]=ChatColor.DARK_AQUA+"Information ............ "+ChatColor.AQUA+getNewMessages()+ChatColor.GRAY+" New, "+ChatColor.AQUA+getOldMessages()+ChatColor.GRAY+" Unanswered Messages";
		s[4]=ChatColor.DARK_AQUA+"Kingdom Name ........... "+ChatColor.GRAY+getKingdom().getName();
		s[5]=ChatColor.DARK_AQUA+"Repair Type ............ "+ChatColor.GRAY+getRepairTypeAsString();
		s[6]=ChatColor.DARK_AQUA+"Ruler .................. "+ChatColor.GRAY+getKingdom().getOwner();
		s[7]=ChatColor.DARK_AQUA+"Status ................. "+ChatColor.GRAY+toTitleCase(getStatus().name().toLowerCase());
		s[8]=ChatColor.DARK_AQUA+"Tech Level ............. "+ChatColor.AQUA+getLevel();
		s[9]=ChatColor.DARK_AQUA+"Type of Building ....... "+ChatColor.GRAY+"Field";
		s[10]=ChatColor.DARK_AQUA+"Upgrade Cost ........... "+getUpgradeCostAsString();
		s[11]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
}
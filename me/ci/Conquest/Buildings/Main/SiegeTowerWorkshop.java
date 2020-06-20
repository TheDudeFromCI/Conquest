package me.ci.Conquest.Buildings.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;

import me.ci.WhCommunity;
import me.ci.Community.PlotMg;
import me.ci.Conquest.BuildingInterfaces.TroopConvertable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.ArmyType;
import me.ci.Conquest.Textures.ConquestTextures;

public class SiegeTowerWorkshop extends Building implements TroopConvertable{
	private int converttime;
	private int amount;
	private Chunk to;
	private static final int totalconverttime = 120;
	public SiegeTowerWorkshop(Chunk nwc, Kingdom kingdom){
		super(nwc, kingdom);
		if(WhCommunity.debug)WhCommunity.printDebug();
	}
	public SiegeTowerWorkshop(Kingdom kingdom, String code, Chunk nwc, long id){
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
				+";"+converttime
				+";"+amount
				+";"+(to==null?"0,0":to.getX()+","+to.getZ());
	}
	public void loadSaveStats(String code){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = code.split(";");
		this.hp=Integer.valueOf(s[2]);
		this.lvl=Integer.valueOf(s[3]);
		this.converttime=Integer.valueOf(s[4]);
		this.amount=Integer.valueOf(s[5]);
		String[] s1 = s[6].split(",");
		to=getCenter().getWorld().getChunkAt(Integer.valueOf(s1[0]), Integer.valueOf(s1[1]));
	}
	public void updateGraphics(boolean threadsleep){
		ConquestTextures.getTexture(getType(), lvl).buildAt(nwc, this);
	}
	public String[] getInfo(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		String[] s = new String[16];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"Building Name .......... "+ChatColor.GRAY+getName();
		s[2]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[3]=ChatColor.DARK_AQUA+"Information ............ "+ChatColor.AQUA+getNewMessages()+ChatColor.GRAY+" New, "+ChatColor.AQUA+getOldMessages()+ChatColor.GRAY+" Unanswered Messages";
		s[4]=ChatColor.DARK_AQUA+"Kingdom Name ........... "+ChatColor.GRAY+getKingdom().getName();
		s[5]=ChatColor.DARK_AQUA+"Repair Type ............ "+ChatColor.GRAY+getRepairTypeAsString();
		s[6]=ChatColor.DARK_AQUA+"Status ................. "+ChatColor.GRAY+toTitleCase(getStatus().name().toLowerCase());
		s[7]=ChatColor.DARK_AQUA+"Tech Level ............. "+ChatColor.AQUA+getLevel();
		s[8]=ChatColor.DARK_AQUA+"Type of Building ....... "+ChatColor.GRAY+"Military";
		s[9]=ChatColor.DARK_AQUA+"Upgrade Cost ........... "+getUpgradeCostAsString();
		s[10]=ChatColor.DARK_AQUA+"Connected to Kingdom ... "+ChatColor.GRAY+isAlive();
		s[11]=ChatColor.DARK_AQUA+"Conversion Percent ..... "+ChatColor.AQUA+(int)((totalconverttime-converttime)/(double)totalconverttime*100)+"%";
		s[12]=ChatColor.DARK_AQUA+"Conversion Amount ...... "+ChatColor.AQUA+amount;
		s[13]=ChatColor.DARK_AQUA+"Conversion Location .... "+ChatColor.AQUA+(to==null?"0,0":to.getX()+","+to.getZ());
		s[14]=ChatColor.DARK_AQUA+"Convert To ........ "+ChatColor.GRAY+getConvertTo().toString();
		s[15]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
	public boolean isConvertingTroops(){
		return converttime>0;
	}
	public ArmyType getConvertTo(){
		return ArmyType.SIEGETOWER;
	}
	public void convert(int amount, Chunk to){
		this.amount=amount;
		this.to=to;
		this.converttime=totalconverttime;
		updateStatus();
		for(int x = 0; x<16; x++){
			for(int z = 0; z<16; z++){
				if(x==0
						||x==15
						||z==0
						||z==15)to.getBlock(x, PlotMg.getPlotAt(nwc).getHeight()+3, z).setTypeIdAndData(35, (byte)14, true);
			}
		}
	}
	public void removeTime(final int i){
		new Thread(new Runnable(){
			public void run(){
				converttime-=i;
				if(converttime<=0){
					if(!PlotMg.getPlotAt(to).isKingdomPlot())new Army(to, kingdom, amount, getConvertTo(), lvl);
					else{
						kingdom.addVillagers(amount);
						amount=0;
						to=null;
						Bukkit.getPlayer(getKingdom().getOwner()).sendMessage(ChatColor.RED+"Could not place military unit, area taken!");
					}
					updateStatus();
				}
			}
		}).start();
	}
	public int getConvertAmount(){ return amount; }
}
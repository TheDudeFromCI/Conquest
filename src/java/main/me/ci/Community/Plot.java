package me.ci.Community;

import java.util.LinkedList;
import java.util.List;

import me.ci.WhCommunity;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import me.ci.Conquest.Textures.ConquestTextures;

import org.bukkit.Chunk;
import org.bukkit.World;

public class Plot{
	protected String owner;
	protected List<String> canBuild;
	protected String renter;
	protected String taxTime;
	protected final int x;
	protected final int z;
	protected final World w;
	protected boolean rentable;
	protected Kingdom kingdom;
	protected Building building;
	protected int height;
	public static final int plotVersion = 6;
	public Plot(int x, int z, World w){
		this.owner="0";
		this.canBuild=new LinkedList<String>();
		this.renter="0";
		this.taxTime="0";
		this.rentable=false;
		this.x=x;
		this.z=z;
		this.w=w;
	}
	public Plot(int x, int z, World w, Kingdom kingdom, Building building){
		this.owner="0";
		this.canBuild=new LinkedList<String>();
		this.renter="0";
		this.taxTime="0";
		this.rentable=false;
		this.x=x;
		this.z=z;
		this.w=w;
		this.kingdom=kingdom;
		this.building=building;
	}
	public Plot(String code, int x, int z, World w){
		if(WhCommunity.debug)WhCommunity.printDebug();
		try{
			String[] c = code.split("=");
			if(c[0].equals("1")){
				String[] s1 = c[1].split(";");
				this.owner=s1[0];
				this.renter=s1[2];
				this.taxTime=s1[3];
				this.rentable=Boolean.valueOf(s1[4]);
				this.canBuild=new LinkedList<String>();
				for(String s : s1[1].split(",")){
					this.canBuild.add(s);
				}
			}else if(c[0].equals("2")){
				String[] s1 = c[1].split(";");
				this.owner=s1[0];
				this.renter=s1[2];
				this.taxTime=s1[3];
				this.rentable=Boolean.valueOf(s1[4]);
				this.canBuild=new LinkedList<String>();
				for(String s : s1[1].split(",")){
					this.canBuild.add(s);
				}
			}else if(c[0].equals("3")){
				String[] s1 = c[1].split(";");
				this.owner=s1[0];
				this.renter=s1[2];
				this.taxTime=s1[3];
				this.rentable=Boolean.valueOf(s1[4]);
				this.canBuild=new LinkedList<String>();
				for(String s : s1[1].split(",")){
					this.canBuild.add(s);
				}
			}else if(c[0].equals("4")){
				String[] s1 = c[1].split(";");
				this.owner=s1[0];
				this.renter=s1[2];
				this.taxTime=s1[3];
				this.rentable=Boolean.valueOf(s1[4]);
				this.canBuild=new LinkedList<String>();
				for(String s : s1[1].split(",")){
					this.canBuild.add(s);
				}
				this.kingdom=(s1[5].isEmpty()?null:KingdomMg.getKingdom(s1[5]));
				this.building=(s1[6].isEmpty()?null:Building.getById(this.kingdom, Integer.valueOf(s1[6])));
			}else if(c[0].equals("5")){
				String[] s1 = c[1].split(";");
				this.owner=s1[0];
				this.renter=s1[2];
				this.taxTime=s1[3];
				this.rentable=Boolean.valueOf(s1[4]);
				this.canBuild=new LinkedList<String>();
				for(String s : s1[1].split(",")){
					this.canBuild.add(s);
				}
				this.kingdom=(s1[5].isEmpty()?null:KingdomMg.getKingdom(s1[5]));
				this.building=(s1[6].isEmpty()?null:Building.getById(this.kingdom, Integer.valueOf(s1[6])));
			}else if(c[0].equals("6")){
				String[] s1 = c[1].split(";");
				this.owner=s1[0];
				this.renter=s1[2];
				this.taxTime=s1[3];
				this.rentable=Boolean.valueOf(s1[4]);
				this.canBuild=new LinkedList<String>();
				for(String s : s1[1].split(",")){
					this.canBuild.add(s);
				}
				this.kingdom=(s1[5].isEmpty()?null:KingdomMg.getKingdom(s1[5]));
				this.building=(s1[6].isEmpty()?null:Building.getById(this.kingdom, Integer.valueOf(s1[6])));
				this.height=Integer.valueOf(s1[7]);
			}
		}catch(ArrayIndexOutOfBoundsException exception){
			this.owner="0";
			this.canBuild=new LinkedList<String>();
			this.renter="0";
			this.taxTime="0";
			this.rentable=false;
		}catch(Exception exception){
			System.err.println("[WhCommunity] Could not load plot! Code unreadable!");
			exception.printStackTrace();
			this.owner="0";
			this.canBuild=new LinkedList<String>();
			this.renter="0";
			this.taxTime="0";
			this.rentable=false;
		}
		this.x=x;
		this.z=z;
		this.w=w;
	}
	public void addPlayerToPlot(String p){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(!this.canBuild.contains(p))this.canBuild.add(p);
	}
	public void removePlayerFromPlot(String p){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.canBuild.remove(p);
	}
	public String getOwner(){
		if(this.owner.equals("0"))return null;
		return this.owner;
	}
	public void setOwner(String owner){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(owner==null){
			this.owner="0";
			this.canBuild=new LinkedList<String>();
			this.renter="0";
			this.taxTime="0";
			this.rentable=false;
			this.kingdom=null;
			this.building=null;
		}else this.owner=owner;
	}
	public void setTaxTime(long taxTime){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.taxTime=String.valueOf(taxTime);
	}
	public long getTaxTime(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return Long.valueOf(this.taxTime);
	}
	public String getRenter(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(this.renter.equals("0"))return null;
		return this.renter;
	}
	public void setRenter(String renter){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(renter==null){
			this.renter="0";
			this.taxTime="0";
		}else{
			this.renter=renter;
			setTaxTime(System.currentTimeMillis()+86400000);
		}
		this.rentable=false;
		this.canBuild=new LinkedList<String>();
	}
	@Override
	public String toString(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(isClaimed()){
			String t = "";
			for(String s : this.canBuild){
				t+=","+s;
			}
			while(t.startsWith(","))t=t.substring(1);
			return Plot.plotVersion
					+"="+this.owner
					+";"+t
					+";"+this.renter
					+";"+this.taxTime
					+";"+this.rentable
					+";"+(isKingdomPlot()?this.kingdom.getName():"")
					+";"+(isKingdomPlot()?this.building.getId():"")
					+";"+this.height;
		}
		return null;
	}
	public boolean isClaimed(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(this.owner==null)this.owner="0";
		if(this.kingdom!=null||this.building!=null)return true;
		if(this.owner.equals("0"))return false;
		return true;
	}
	public int getX(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.x;
	}
	public int getZ(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.z;
	}
	public World getWorld(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.w;
	}
	public void save(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		PlotMg.savePlot(this);
	}
	public boolean canBuildHere(String p){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(this.owner.equals(p)){
			if(this.renter==null
					||this.renter.equals("0"))return true;
			else return false;
		}
		if(this.renter!=null&&this.renter.equals(p))return true;
		if(this.canBuild.contains(p))return true;
		return false;
	}
	public void setRentable(boolean rentable){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.rentable=rentable;
	}
	public boolean isRentable(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.rentable;
	}
	public List<String> getAllowedPlayerList(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.canBuild;
	}
	public boolean isStreet(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		if(this.owner==null
				||this.owner.equals("0")){
			for(int x = getX()-1; x<=getX()+1; x++){
				for(int z = getZ()-1; z<=getZ()+1; z++){
					Plot plot2 = PlotMg.getPlotAt(getWorld(), x, z);
					if(plot2.isClaimed())return true;
				}
			}
			return false;
		}else return false;
	}
	public Chunk getChunk(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return getWorld().getChunkAt(getX(), getZ());
	}
	public Kingdom getKingdom(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.kingdom;
	}
	public Building getBuilding(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		return this.building;
	}
	public boolean isKingdomPlot(){
		if(this.kingdom==null)return false;
		if(this.building==null)return false;
		return true;
	}
	public boolean hasKingdom(){
		return this.kingdom!=null;
	}
	public void setKingdom(Kingdom kingdom, Building building){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.kingdom=kingdom;
		this.building=building;
		this.owner=this.kingdom.getOwner();
		this.canBuild=new LinkedList<String>();
		this.renter="0";
		this.taxTime="0";
		this.rentable=false;
	}
	public void setBuilding(Building building){
		if(WhCommunity.debug)WhCommunity.printDebug();
		this.building=building;
	}
	public void unclaim(boolean updategraphics){
		if(WhCommunity.debug)WhCommunity.printDebug();
		unclaim(false, updategraphics);
	}
	public void unclaim(boolean threadSleep, boolean updategraphics){
		setOwner(null);
		if(updategraphics
				&&isConquestChunk())ConquestTextures.getTexture(BuildingType.WILDERNESS, 1).buildAt(getChunk(), null);
	}
	public boolean isConquestChunk(){
		return WhCommunity.getWorldSettings(this.w.getName()).CONFIG_Conquest_Enabled;
	}
	public int getHeight(){
		return (height==0?6:height);
	}
	public int getRealHeight(){
		return height;
	}
	public boolean addHeight(final int h, final boolean update){ return setHeight(height+h, update); }
	public boolean removeHeight(int h, final boolean update){ return setHeight(height-h, update); }
	public boolean setHeight(int h, final boolean update){
		if(h<1)h=1;
		if(h>20)h=20;
		if(height==h)return false;
		height=h;
		if(isKingdomPlot()){
			final int offx = building.getNorthWestCourner().getX();
			final int offz = building.getNorthWestCourner().getZ();
			for(int x = 0; x<building.getLength(); x++){
				for(int z = 0; z<building.getLength(); z++){
					PlotMg.getPlotAt(w, x+offx, z+offz).safeSetHeight(h);
				}
			}
		}
		if(update){
			if(isKingdomPlot()){
				getBuilding().updateGraphics(false);
				getBuilding().updateStatus();
			}else ConquestTextures.getTexture(BuildingType.WILDERNESS, 1).buildAt(getChunk(), null);
		}
		return true;
	}
	private void safeSetHeight(int h){
		height=h;
	}
}
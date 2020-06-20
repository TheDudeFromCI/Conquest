package me.ci.Conquest.NaturalResources;

import me.ci.Community.PlotMg;

import org.bukkit.Chunk;
import org.bukkit.World;

public class Country{
	public final int x;
	public final int z;
	public final World w;
	public Country(World w, int x, int z){
		this.x=x;
		this.z=z;
		this.w=w;
	}
	public Chunk getProvinceSection(int x, int z){
		x*=27;
		int newx = this.x*x;
		if(newx>0)newx-=26;
		z*=27;
		int newz = this.z*z;
		if(newz>0)newz-=26;
		return w.getChunkAt(newx, newz);
	}
	public boolean canHoldKingdom(){
		Chunk chunk = getProvinceSection(1, 1);
		for(int x = 10; x<17; x++){
			for(int z = 10; z<17; z++){
				if(PlotMg.getPlotAt(chunk.getWorld(), chunk.getX()+x, chunk.getZ()+z).isClaimed()){
					try{ Thread.sleep(1000);
					}catch(Exception exception){}
					return false;
				}
			}
		}
		try{ Thread.sleep(1000);
		}catch(Exception exception){}
		return true;
	}
	public Chunk getKingdomCenter(){ return getProvinceSection(1, 1); }
}
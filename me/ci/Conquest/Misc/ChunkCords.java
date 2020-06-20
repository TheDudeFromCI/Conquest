package me.ci.Conquest.Misc;

import me.ci.Conquest.NaturalResources.Country;
import me.ci.Conquest.NaturalResources.CountryMg;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class ChunkCords{
	private final int x;
	private final int z;
	private final World w;
	public ChunkCords(String w, int x, int z){
		this.x=x;
		this.z=z;
		this.w=Bukkit.getWorld(w);
	}
	public ChunkCords(World w, int x, int z){
		this.x=x;
		this.z=z;
		this.w=w;
	}
	public World getWorld(){
		return this.w;
	}
	public Chunk getChunk(){
		return getWorld().getChunkAt(this.x, this.z);
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof ChunkCords){
			ChunkCords c = (ChunkCords)o;
			if(c.w.equals(this.w)
					&&c.x==x
					&&c.z==z)return true;
		}
		return false;
	}
	public int getX(){
		return this.x;
	}
	public int getZ(){
		return this.z;
	}
	public Country getCountry(){
		int x = this.x;
		if(x>0)x+=80;
		x/=81;
		int z = this.z;
		if(z>0)z+=80;
		z/=81;
		return CountryMg.getCountryAt(this.w, x, z);
	}
	@Override
	public String toString(){ return "("+this.w.getName()+": "+x+", "+z+")"; }
	public ChunkCords getOffSetChunk(int x, int z){ return new ChunkCords(w, this.x+x, this.z+z); }
}
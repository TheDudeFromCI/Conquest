package me.ci.Conquest.NaturalResources;

import java.util.List;

import me.ci.Conquest.Misc.ChunkCords;

import org.bukkit.Location;
import org.bukkit.World;

public class CountryMg{
	public static Country getNextOpenCountry(final List<ChunkCords> ex, final World w){
		int o = 1;
		while(true){
			for(int z = -o; z<=o; z++){
				for(int x = -o; x<=o; x++){
					if(z==o
							||z==-o
							||x==o
							||x==-o){
						if(ex.contains(new ChunkCords(w, x, z)))continue;
						final Country c = getCountryAt(w, x, z);
						if(c.canHoldKingdom())return c;
					}
				}
			}
			o++;
		}
	}
	public static Country getCountryAt(Location l){
		int x = getGridCords(l.getBlockX());
		int z = getGridCords(l.getBlockZ());
		return getCountryAt(l.getWorld(), x, z);
	}
	public static Country getCountryAt(World w, int x, int z){
		Country country = new Country(w, x, z);
		return country;
	}
	private static int getGridCords(int x){
		if(x>0)x+=1599;
		x/=1600;
		return x;
	}
}
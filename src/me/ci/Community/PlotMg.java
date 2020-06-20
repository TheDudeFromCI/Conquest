package me.ci.Community;

import java.util.ArrayList;
import java.util.List;

import me.ci.WhCommunity;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlotMg{
	private static List<Plot> plots = new ArrayList<Plot>();
	public synchronized static void savePlot(final Plot plot){
		int x = plot.getX()/16;
		int z = plot.getZ()/16;
		Save.set("Plots", plot.getWorld().getName()+"-"+x+","+z, plot.getX()+","+plot.getZ(), plot.toString());
	}
	public synchronized static Plot getPlotAt(World w, int x, int z){
		Plot plot = null;
		for(Plot p : plots){
			if(p.getWorld()==w
					&&p.getX()==x
					&&p.getZ()==z){
				plot=p;
				break;
			}
		}
		if(plot!=null)return plot;
		String in = Save.get("Plots", w.getName()+"-"+(x/16)+","+(z/16), x+","+z);
		if(in==null)plot=new Plot(x, z, w);
		else plot=new Plot(in, x, z, w);
		plots.add(plot);
		return plot;
	}
	public static Plot getPlotAt(Chunk chunk){
		return getPlotAt(chunk.getWorld(), chunk.getX(), chunk.getZ());
	}
	public static List<Plot> getClusteredPlotsAt(Chunk chunk){
		if(WhCommunity.debug)WhCommunity.printDebug();
		List<Plot> plots = new ArrayList<Plot>();
		Plot main = getPlotAt(chunk);
		if(!main.isClaimed())return plots;
		touchingPlots(plots, main);
		if(!listContainsPlot(plots, main))plots.add(main);
		return plots;
	}
	public static List<Plot> getAllOwnedPlots(Player p){
		if(WhCommunity.debug)WhCommunity.printDebug();
		//TODO
		return null;
	}
	private static List<Plot> touchingPlots(List<Plot> plots, Plot main){
		if(WhCommunity.debug)WhCommunity.printDebug();
		for(Plot plot : getTouchingPlots(main)){
			if(!listContainsPlot(plots, plot)){
				if(plot.isClaimed()
						&&plot.getOwner().equals(main.getOwner())){
						plots.add(plot);
						touchingPlots(plots, plot);
				}
			}
		}
		return plots;
	}
	private static List<Plot> getTouchingPlots(Plot plot){
		if(WhCommunity.debug)WhCommunity.printDebug();
		List<Plot> plots = new ArrayList<Plot>();
		plots.add(getPlotAt(plot.getWorld(), plot.getX()+1, plot.getZ()));
		plots.add(getPlotAt(plot.getWorld(), plot.getX()-1, plot.getZ()));
		plots.add(getPlotAt(plot.getWorld(), plot.getX(), plot.getZ()+1));
		plots.add(getPlotAt(plot.getWorld(), plot.getX(), plot.getZ()-1));
		return plots;
	}
	private static boolean listContainsPlot(List<Plot> plots, Plot plot){
		if(WhCommunity.debug)WhCommunity.printDebug();
		for(Plot p : plots){
			if(p.getWorld().getName().equals(plot.getWorld().getName())
					&&p.getX()==plot.getX()
					&&p.getZ()==plot.getZ()
					&&p.toString().equals(plot.toString()))return true;
		}
		return false;
	}
	public static void savePlots(){
		final ArrayList<Plot> save = new ArrayList<>(plots);
		for(Plot p : save)p.save();
	}
}
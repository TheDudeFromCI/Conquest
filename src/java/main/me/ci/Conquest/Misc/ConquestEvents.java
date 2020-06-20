package me.ci.Conquest.Misc;

import java.util.ArrayList;
import java.util.List;

import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.BuildingInterfaces.Reshapeable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Events.BuildingBuildEvent;
import me.ci.Conquest.Events.BuildingDestroyEvent;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.ArmyFormation;
import me.ci.Conquest.Textures.ConquestTextures;

public class ConquestEvents{
	public static void e(BuildingBuildEvent e){
		checkForBuildingShapeUpdates(e.getBuilding());
		final Kingdom kingdom = e.getKingdom();
		if(e.getType()==BuildingType.GRANARY)kingdom.awardAchievement(Achievement.FEED_THE_PEOPLE);
		if(e.getType()==BuildingType.UNIVERSITY)kingdom.awardAchievement(Achievement.KNOWLEDGE_IS_POWER);
		if(e.getType()==BuildingType.ROAD)kingdom.awardAchievement(Achievement.ROAD_LESS_TRAVELED);
		if(e.getType()==BuildingType.SAWMILL)kingdom.awardAchievement(Achievement.SAW_SAW);
		if(e.getType()==BuildingType.STONE_QUARRY)kingdom.awardAchievement(Achievement.STONE_FOR_STONE);
		if(e.getType()==BuildingType.METAL_MINING)kingdom.awardAchievement(Achievement.METAL_MADNESS);
		if(e.getType()==BuildingType.BLACKSMITH)kingdom.awardAchievement(Achievement.MR_BLACKSMITH);
		if(e.getType()==BuildingType.BARRACK)kingdom.awardAchievement(Achievement.MEN_AT_ARMS);
		if(e.getType()==BuildingType.COTTAGE)kingdom.awardAchievement(Achievement.HOME_SWEET_COTTAGE);
		if(e.getType()==BuildingType.TAVERN)kingdom.awardAchievement(Achievement.WELL_DRINK_TO_THAT);
		if(e.getType()==BuildingType.WATCH_TOWER)kingdom.awardAchievement(Achievement.HAWKEYES);
		if(e.getType()==BuildingType.WALL)kingdom.awardAchievement(Achievement.DEFEND_THE_KEEP);
	}
	public static void e(BuildingDestroyEvent e){
		if(e.getType()==BuildingType.CAPITOL)e.getKingdom().deleteKingdom();
		else checkForBuildingShapeUpdates(e.getBuilding());
	}
	private static void checkForBuildingShapeUpdates(final Building building){
		Thread t = new Thread(new Runnable(){
			public void run(){
				try{
					List<Building> shape = new ArrayList<Building>();
					for(int x = building.getNorthWestCourner().getX()-1; x<=building.getNorthWestCourner().getX()+building.getType().getLength(); x++){
						for(int z = building.getNorthWestCourner().getZ()-1; z<=building.getNorthWestCourner().getZ()+building.getType().getLength(); z++){
							try{
								Plot plot = PlotMg.getPlotAt(building.getWorld(), x, z);
								if(plot.isKingdomPlot()){
									if(plot.getBuilding().getType().isReshapeable()
											&&plot.getKingdom().equals(building.getKingdom())
											&&!shape.contains(plot.getBuilding()))shape.add(plot.getBuilding());
									plot.getBuilding().updateStatus();
									plot.getBuilding().checkForLife();
								}
							}catch(Exception exception){ exception.printStackTrace(); }
						}
					}
					for(Building b : shape){
						boolean n1 = false;
						boolean e1 = false;
						boolean s1 = false;
						boolean w1 = false;
						int h = PlotMg.getPlotAt(b.getNorthWestCourner()).getHeight();
						Plot plot = PlotMg.getPlotAt(b.getWorld(), b.getNorthWestCourner().getX(), b.getNorthWestCourner().getZ()-1);
						if(plot.isKingdomPlot()){
							if(plot.getKingdom().equals(b.getKingdom())){
								if(Math.abs(plot.getHeight()-h)<=((Reshapeable)b).getHeightVariance()
										&&((Reshapeable)b).connectsTo(plot.getBuilding().getType()))n1=true;
							}
						}
						plot=PlotMg.getPlotAt(b.getWorld(), b.getNorthWestCourner().getX(), b.getNorthWestCourner().getZ()+1);
						if(plot.isKingdomPlot()){
							if(plot.getKingdom().equals(b.getKingdom())){
								if(Math.abs(plot.getHeight()-h)<=((Reshapeable)b).getHeightVariance()
										&&((Reshapeable)b).connectsTo(plot.getBuilding().getType()))s1=true;
							}
						}
						plot=PlotMg.getPlotAt(b.getWorld(), b.getNorthWestCourner().getX()-1, b.getNorthWestCourner().getZ());
						if(plot.isKingdomPlot()){
							if(plot.getKingdom().equals(b.getKingdom())){
								if(Math.abs(plot.getHeight()-h)<=((Reshapeable)b).getHeightVariance()
										&&((Reshapeable)b).connectsTo(plot.getBuilding().getType()))w1=true;
							}
						}
						plot=PlotMg.getPlotAt(b.getWorld(), b.getNorthWestCourner().getX()+1, b.getNorthWestCourner().getZ());
						if(plot.isKingdomPlot()){
							if(plot.getKingdom().equals(b.getKingdom())){
								if(Math.abs(plot.getHeight()-h)<=((Reshapeable)b).getHeightVariance()
										&&((Reshapeable)b).connectsTo(plot.getBuilding().getType()))e1=true;
							}
						}
						ArmyFormation form = ArmyFormation.getBySides(n1, e1, s1, w1);
						ConquestTextures.getTexture(b.getType(), b.getType().getGraphicsLevelFor(form, b.getLevel())).buildAt(b.getNorthWestCourner(), b);
					}
				}catch(Exception exception){ exception.printStackTrace(); }
			}
		});
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}
}
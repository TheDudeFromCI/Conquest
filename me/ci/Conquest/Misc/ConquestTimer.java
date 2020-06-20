package me.ci.Conquest.Misc;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.ci.Community.Save;
import me.ci.Conquest.BuildingInterfaces.ResourceHolder;
import me.ci.Conquest.BuildingInterfaces.VillagerWorkable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.Buildings.Constructors.StatusType;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Buildings.Main.Granary;
import me.ci.Conquest.Buildings.Main.Marketplace;
import me.ci.Conquest.Buildings.Main.Tavern;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.EventProperties;

public class ConquestTimer{
	private static Timer task;
	private static final int timerSeconds = 30;
	public static void start(){
		stop();
		task=new Timer();
		task.scheduleAtFixedRate(new Go(), timerSeconds*1000L, timerSeconds*1000L);
		Save.set("Resources", "Timer Storage", "Conquest Hours", "60");
	}
	public static void stop(){ if(task!=null)task.cancel(); }
	private static class Go extends TimerTask{
		private long loops = 0;
		public void run(){
			try{
				loops++;
				Granary g;
				for(Kingdom kingdom : KingdomMg.getKingdoms()){
					if(kingdom.getOwner()!=null
							&&kingdom.isOwnerOnline()){
						try{
							synchronized(Building.class){
								int addWood=0, addFood=0, addStone=0, addIron=0, addArms=0;
								int ppls = kingdom.getVillagers(true);
								int maxppls = kingdom.getVillagerMax();
								kingdom.collectResources(ResourceType.FOOD, Math.max((int)(ppls*0.2), 1));
								int food = kingdom.getResourceLevels(ResourceType.FOOD);
								if(food<ppls){
									kingdom.killVillagers(ppls-food);
									if(kingdom.isDead())continue;
								}else if(ppls<maxppls
										&&!kingdom.isRevolting()){
									int bonus = ((int)(Math.random()*(kingdom.getMorale()/10.0)+1));
									bonus+=kingdom.getBuildingLocations(BuildingType.COTTAGE).size();
									bonus=Math.min(bonus+ppls, food)-ppls;
									bonus=Math.min(bonus+ppls, maxppls)-ppls;
									final int mg = (int)((double)bonus/ppls*100/3);
									kingdom.addMorale(mg);
									kingdom.addVillagers(bonus);
								}
								ppls=kingdom.getVillagers(true);
								kingdom.addMoney(Math.max((int)(kingdom.getMorale()/100.0*ppls), 1));
								if(loops%3==0)checkForEvent(kingdom);
								final ArrayList<Building> builds;
								synchronized(Building.buildings){ builds=new ArrayList<>(Building.getBuildings(kingdom)); }
								for(Building b : builds){
									StatusType statusbefore = b.getStatus();
									if(b instanceof VillagerWorkable){
										if(b.isAlive()){
											if(b instanceof ResourceHolder){
												int left = ((VillagerWorkable)b).getWorkingVillagers();
												ResourceType[] type = ((ResourceHolder)b).getResources();
												left*=kingdom.getMorale()/5.0*10/100;
												for(ResourceType rt : type){
													if(rt==ResourceType.ARMS){
														left=Math.min(left/type.length, kingdom.getResourceLevels(ResourceType.IRON));
														kingdom.collectResources(ResourceType.IRON, left);
														left=b.addResources(rt, left);
														left=kingdom.addResources(ResourceType.ARMS, left);
														kingdom.addResources(ResourceType.IRON, left);
													}else{
														if(kingdom.getMainResource()==rt)left*=2;
														if(b instanceof Granary){
															g=(Granary)b;
															if(g.hasRats())left*=0.75;
														}
														left=b.addResources(rt, left/type.length);
														if(rt==ResourceType.FOOD)addFood+=left;
														if(rt==ResourceType.WOOD)addWood+=left;
														if(rt==ResourceType.IRON)addIron+=left;
														if(rt==ResourceType.STONE)addStone+=left;
													}
												}
											}
										}
										if(b.isDiseased())kingdom.killVillagers((VillagerWorkable)b, (int)(((VillagerWorkable)b).getWorkingVillagers()*0.1));
									}
									if(b instanceof Army){
										Army army = (Army)b;
										if(army.isCamping()
												&&army.getStaminaPercent()<100
												&&kingdom.getResourceLevels(ResourceType.FOOD)>0){
											army.setStamina(Math.min(army.getStamina()+2, army.getMaxStamina()));
											kingdom.collectResources(ResourceType.FOOD, 2);
										}
									}
									if(b instanceof Tavern
											&&b.isAlive())((Tavern)b).addHenchmen();
									if(b instanceof Marketplace
											&&b.isAlive()){
										final int type = (int)(Math.random()*6);
										if(type==0)addFood+=Math.random()*100+100;
										if(type==1)addWood+=Math.random()*100+100;
										if(type==2)addStone+=Math.random()*100+100;
										if(type==3)addArms+=Math.random()*100+100;
										if(type==4)addIron+=Math.random()*100+100;
									}
									if(b.getStatus()!=statusbefore)b.updateStatus();
									if(kingdom.isRevolting())b.damage(null, (int)(Math.random()*101));
									b.burn();
								}
								if(addFood>0)kingdom.addResources(ResourceType.FOOD, addFood);
								else kingdom.collectResources(ResourceType.FOOD, addFood);
								kingdom.addResources(ResourceType.WOOD, addWood);
								kingdom.addResources(ResourceType.IRON, addIron);
								kingdom.addResources(ResourceType.STONE, addStone);
								kingdom.addResources(ResourceType.ARMS, addArms);
							}
						}catch(final Exception exception){ exception.printStackTrace(); }
					}
				}
			}catch(final Exception exception){ exception.printStackTrace(); }
			String in = Save.get("Resources", "Timer Storage", "Conquest Days");
			if(in==null)in=String.valueOf(timerSeconds);
			int d = Integer.valueOf(in);
			d++;
			Save.set("Resources", "Timer Storage", "Conquest Days", String.valueOf(d));
			Save.set("Resources", "Timer Storage", "Conquest Hours", String.valueOf(timerSeconds));
		}
	}
	private static boolean checkForEvent(final Kingdom k){
		final ArrayList<BuildingType> types = new ArrayList<>();
		for(Building b : Building.getBuildings(k)){ if(!types.contains(b.getType()))types.add(b.getType()); }
		if(types.isEmpty())return false;
		final BuildingType type = types.get((int)(Math.random()*types.size()));
		final ArrayList<Building> buildings = new ArrayList<>();
		for(Building b : Building.getBuildings(k)){ if(b.getType()==type)buildings.add(b); }
		if(buildings.isEmpty())return false;
		final ArrayList<EventScript> scripts = EventScript.getEvents();
		EventScript.filterByBuildingType(type, scripts);
		if(scripts.isEmpty())return false;
		final Building build = buildings.get((int)(Math.random()*buildings.size()));
		final EventScript script = scripts.get((int)(Math.random()*scripts.size()));
		final EventProperties props = new EventProperties(build);
		script.load(props);
		build.addEvent(script.getScript());
		return true; 
	}
}
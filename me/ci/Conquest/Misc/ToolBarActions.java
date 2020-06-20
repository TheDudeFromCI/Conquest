package me.ci.Conquest.Misc;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.ci.WhCommunity;
import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.BuildingInterfaces.ResourceHolder;
import me.ci.Conquest.BuildingInterfaces.TroopConvertable;
import me.ci.Conquest.BuildingInterfaces.VillagerWorkable;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.Buildings.Constructors.BuildingType.BuildBuilding;
import me.ci.Conquest.Buildings.Main.AncientDojo;
import me.ci.Conquest.Buildings.Main.AntiMagicOnagerWorkshop;
import me.ci.Conquest.Buildings.Main.ArcheryRange;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Buildings.Main.BallisticWorkshop;
import me.ci.Conquest.Buildings.Main.Barrack;
import me.ci.Conquest.Buildings.Main.BridgeMakerWorkshop;
import me.ci.Conquest.Buildings.Main.Blacksmith;
import me.ci.Conquest.Buildings.Main.CatapultWorkshop;
import me.ci.Conquest.Buildings.Main.Church;
import me.ci.Conquest.Buildings.Main.Coliseum;
import me.ci.Conquest.Buildings.Main.Cottage;
import me.ci.Conquest.Buildings.Main.CowTosserWorkshop;
import me.ci.Conquest.Buildings.Main.DragonPit;
import me.ci.Conquest.Buildings.Main.DragonsLair;
import me.ci.Conquest.Buildings.Main.DruidsHallow;
import me.ci.Conquest.Buildings.Main.EarthTemple;
import me.ci.Conquest.Buildings.Main.FireTemple;
import me.ci.Conquest.Buildings.Main.Gatehouse;
import me.ci.Conquest.Buildings.Main.Granary;
import me.ci.Conquest.Buildings.Main.Graveyard;
import me.ci.Conquest.Buildings.Main.HolyBattlefield;
import me.ci.Conquest.Buildings.Main.IllusionistsTower;
import me.ci.Conquest.Buildings.Main.Land;
import me.ci.Conquest.Buildings.Main.MagesTower;
import me.ci.Conquest.Buildings.Main.MangonelWorkshop;
import me.ci.Conquest.Buildings.Main.Marketplace;
import me.ci.Conquest.Buildings.Main.MeadHall;
import me.ci.Conquest.Buildings.Main.MetalMining;
import me.ci.Conquest.Buildings.Main.MilitaryFort;
import me.ci.Conquest.Buildings.Main.Moat;
import me.ci.Conquest.Buildings.Main.NobleManor;
import me.ci.Conquest.Buildings.Main.PortalGate;
import me.ci.Conquest.Buildings.Main.Road;
import me.ci.Conquest.Buildings.Main.Sanctuary;
import me.ci.Conquest.Buildings.Main.Sawmill;
import me.ci.Conquest.Buildings.Main.SiegeTowerWorkshop;
import me.ci.Conquest.Buildings.Main.Stable;
import me.ci.Conquest.Buildings.Main.StoneQuarry;
import me.ci.Conquest.Buildings.Main.Tavern;
import me.ci.Conquest.Buildings.Main.Townhall;
import me.ci.Conquest.Buildings.Main.TradeRoute;
import me.ci.Conquest.Buildings.Main.TrebuchetWorkshop;
import me.ci.Conquest.Buildings.Main.University;
import me.ci.Conquest.Buildings.Main.Wall;
import me.ci.Conquest.Buildings.Main.WatchTower;
import me.ci.Conquest.Buildings.Main.WaterTemple;
import me.ci.Conquest.Buildings.Main.WindTemple;
import me.ci.Conquest.KingdomManagment.DF;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.Military.ArmyFormation;
import me.ci.Conquest.Military.HenchmenType;
import me.ci.Conquest.Textures.ConquestTextures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ToolBarActions{
	private static List<String> movingvillagers = new LinkedList<String>();
	private static Map<String,Chunk> converttroops = new HashMap<String,Chunk>();
	private static HashMap<String,Long> monitors;
	private static List<String> henchmenchoosers = new ArrayList<>();
	public static void go(final Player p, final Action action){
		if(action==Action.PHYSICAL)return;
		new Thread(new Runnable(){
			public void run(){
				try{
					Block b = null;
					try{ b=p.getTargetBlock(null, 400);
					}catch(final Exception exception){ return; }
					final ItemStack item = p.getItemInHand();
					final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
					if(kingdom==null)return;
					if(item==null)return;
					if(item.getTypeId()==Material.LEATHER_BOOTS.getId()){
						if(p.getLevel()==0){
							if(action==Action.RIGHT_CLICK_AIR
									||action==Action.RIGHT_CLICK_BLOCK)kingdom.awardAchievement(Achievement.DERP_SCOUT);
							Kingdom.claimChunk(p, b.getChunk(), false);
						}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
					}else if(item.getTypeId()==Material.IRON_SPADE.getId()){
						final Plot plot = PlotMg.getPlotAt(b.getChunk());
						if(plot.isKingdomPlot()){
							if(plot.getKingdom().equals(kingdom)){
								if(plot.getBuilding().getType().equals(BuildingType.LAND)){
									if(!plot.getBuilding().isBuildingOrUpgrading()){
										final ResourceType r = ((Land)plot.getBuilding()).getHeldResource();
										if(p.getLevel()==0){
											if(r!=ResourceType.NONE){
												final Block b1 = b;
												Bukkit.getScheduler().runTask(WhCommunity.plugin, new Runnable(){
													public void run(){
														int bonus;
														if(r==ResourceType.GOLD)bonus=(int)(Math.random()*101)+100;
														else bonus=(int)(Math.random()*26)+25;
														int left = plot.getKingdom().addResources(r, bonus);
														if(left==bonus)p.sendMessage(ChatColor.RED+"Your kingdom cannot hold any more of this resource.");
														else{
															if(r==ResourceType.FOOD)kingdom.awardAchievement(Achievement.FOOD_COLLECTION_TIME);
															if(r==ResourceType.WOOD)kingdom.awardAchievement(Achievement.WOOD_COLLECTION_TIME);
															if(r==ResourceType.STONE)kingdom.awardAchievement(Achievement.STONE_COLLECTION_TIME);
															if(r==ResourceType.IRON)kingdom.awardAchievement(Achievement.IRON_COLLECTION_TIME);
															if(r==ResourceType.GOLD)kingdom.awardAchievement(Achievement.GOLD_COLLECTION_TIME);
															if(left==0)p.sendMessage(ChatColor.GREEN+"You have collected "+(bonus-left)+" "+r.name().toLowerCase()+".");
															else p.sendMessage(ChatColor.GREEN+"You have collected "+(bonus-left)+" out of "+bonus+" possible "+r.name().toLowerCase()+".");
															((Land)plot.getBuilding()).setHeldResource(ResourceType.NONE);
															ConquestTextures.getTexture(BuildingType.LAND, 6).buildAt(b1.getChunk(), plot.getBuilding());
															PlayerMg.setStaminaTime(p, PlayerMg.COLLECT_RESOURCES);
														}
													}
												});
											}else{
												p.sendMessage(ChatColor.RED+"There are no resources here to collect!");
												ConquestTextures.getTexture(BuildingType.LAND, 6).buildAt(b.getChunk(), plot.getBuilding());
											}
										}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
									}else p.sendMessage(ChatColor.RED+"You cannot collect resources from land that is still being scouted!");
								}else p.sendMessage(ChatColor.RED+"You can only collect resourcs from land!");
							}else p.sendMessage(ChatColor.RED+"You do not own this land, so you cannot collect resources from it!");
						}
					}else if(item.getTypeId()==Material.COMPASS.getId()){
						if(b.getTypeId()==0)return;
						else{
							try{ while(b.getTypeId()!=0)b=b.getRelative(BlockFace.UP);
							}catch(Exception exception){}
							Location l = b.getLocation();
							l.setPitch(p.getLocation().getPitch());
							l.setYaw(p.getLocation().getYaw());
							p.teleport(l);
							kingdom.awardAchievement(Achievement.TELEPORTATION);
						}
					}else if(item.getTypeId()==Material.NETHER_STAR.getId()){
						if(p.getWorld().equals(kingdom.getWorld())){
							final Plot plot = PlotMg.getPlotAt(b.getLocation().getChunk());
							if(plot.isKingdomPlot()){
								if(plot.getKingdom().equals(kingdom)){
									if(!plot.getBuilding().isBuildingOrUpgrading()){
										if(p.getLevel()==0){
											final Kingdom k = plot.getKingdom();
											boolean university = false;
											synchronized(Building.class){
												for(Building u : Building.getLoadedBuilding(kingdom, BuildingType.UNIVERSITY)){
													if(u.isAlive()){
														university=true;
														break;
													}
												}
											}
											if(university){
												synchronized(plot.getBuilding()){
													if(plot.getBuilding().canBeUpgraded()){
														if(plot.getBuilding().getHp()==plot.getBuilding().getMaxHp()){
															int[] cost = plot.getBuilding().getType().getCost(plot.getBuilding().getLevel()+1);
															if(plot.getBuilding().getType().hasResourcesToBuild(k, plot.getBuilding().getLevel()+1)){
																if(p.getLevel()==0){
																	p.sendMessage(ChatColor.DARK_AQUA+"Your villagers begin to upgrade your "+ChatColor.GRAY+plot.getBuilding().getName()+ChatColor.DARK_AQUA+" to level "+ChatColor.AQUA+(plot.getBuilding().getLevel()+1)+ChatColor.DARK_AQUA+", which has cost you "+ChatColor.AQUA+cost[0]+ChatColor.GRAY+" gold"+ChatColor.DARK_AQUA+", "+ChatColor.AQUA+cost[1]+ChatColor.GRAY+" wood"+ChatColor.DARK_AQUA+", and "+ChatColor.AQUA+cost[2]+ChatColor.GRAY+" stone"+ChatColor.DARK_AQUA+".");
																	k.collectResources(ResourceType.GOLD, cost[0]);
																	k.collectResources(ResourceType.WOOD, cost[1]);
																	k.collectResources(ResourceType.STONE, cost[2]);
																	plot.getBuilding().setLevel(plot.getBuilding().getLevel()+1);
																	plot.getBuilding().setHp(plot.getBuilding().getMaxHp(), true);
																	plot.getBuilding().updateGraphics(false);
																	if(plot.getBuilding().getLevel()==plot.getBuilding().getMaxLevel()
																			&&kingdom.getMorale()<100)kingdom.addMorale(1);
																	PlayerMg.setStaminaTime(p, PlayerMg.UPGRADE_BUILDING);
																	kingdom.awardAchievement(Achievement.LEVEL_UP);
																}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
															}else{
																p.sendMessage(ChatColor.RED+"Your kingdom does not have the required amount of resources to build this building!");
																p.sendMessage(ChatColor.RED+"Required Resources:");
																if(k.getMoney()<cost[0])p.sendMessage(ChatColor.RED+"Money: "+k.getMoney()+"/"+cost[0]);
																else p.sendMessage(ChatColor.GREEN+"Money: "+k.getMoney()+"/"+cost[0]);
																if(k.getResourceLevels(ResourceType.WOOD)<cost[1])p.sendMessage(ChatColor.RED+"Wood: "+k.getResourceLevels(ResourceType.WOOD)+"/"+cost[1]);
																else p.sendMessage(ChatColor.GREEN+"Wood: "+k.getResourceLevels(ResourceType.WOOD)+"/"+cost[1]);
																if(k.getResourceLevels(ResourceType.STONE)<cost[2])p.sendMessage(ChatColor.RED+"Stone: "+k.getResourceLevels(ResourceType.STONE)+"/"+cost[2]);
																else p.sendMessage(ChatColor.GREEN+"Stone: "+k.getResourceLevels(ResourceType.STONE)+"/"+cost[2]);
															}
														}else p.sendMessage(ChatColor.RED+"You cannot upgrade a damaged building!");
													}else p.sendMessage(ChatColor.RED+"This building is at max level!");
												}
											}else p.sendMessage(ChatColor.RED+"You need to have at least 1 university in this kingdom to upgrade buildings!");
										}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
									}else p.sendMessage(ChatColor.RED+"You cannot upgrade a building that is already being built, upgraded, or updating!");
								}else p.sendMessage(ChatColor.RED+"This is not your kingdom, so you cannot upgrade here!");
							}else p.sendMessage(ChatColor.RED+"There is no kingdom here!");
						}else p.sendMessage(ChatColor.RED+"You cannot upgrade outside of the world that your kingdom is in.");
					}else if(item.getTypeId()==Material.CLAY_BRICK.getId()){
						final Plot plot = PlotMg.getPlotAt(b.getChunk());
						if(plot.isKingdomPlot()){
							if(plot.getKingdom()==kingdom){
								if(plot.getBuilding().getType()!=BuildingType.ARMY){
									if(p.getLevel()==0){
										if(plot.getBuilding().getType()==BuildingType.LAND){
											final Inventory in = Bukkit.createInventory(null, 54, "Build which building?");
											in.setMaxStackSize(1);
											ItemStack i1;
											ItemMeta meta;
											ArrayList<String> lore;
											int[] c;
											final int km = kingdom.getResourceLevels(ResourceType.GOLD);
											final int kw = kingdom.getResourceLevels(ResourceType.WOOD);
											final int ks = kingdom.getResourceLevels(ResourceType.STONE);
											for(BuildingType t : BuildingType.values()){
												i1=new ItemStack(Material.PAPER);
												meta=i1.getItemMeta();
												meta.setDisplayName(ChatColor.YELLOW+t.getName());
												lore=new ArrayList<>();
												c=t.getCost(1);
												if(c[0]<=km)lore.add(ChatColor.DARK_AQUA+"Gold: "+ChatColor.GREEN+km+"/"+c[0]);
												else lore.add(ChatColor.DARK_AQUA+"Gold: "+ChatColor.RED+km+"/"+c[0]);
												if(c[1]<=kw)lore.add(ChatColor.DARK_AQUA+"Wood: "+ChatColor.GREEN+kw+"/"+c[1]);
												else lore.add(ChatColor.DARK_AQUA+"Wood: "+ChatColor.RED+kw+"/"+c[1]);
												if(c[2]<=ks)lore.add(ChatColor.DARK_AQUA+"Stone: "+ChatColor.GREEN+ks+"/"+c[2]);
												else lore.add(ChatColor.DARK_AQUA+"Stone: "+ChatColor.RED+ks+"/"+c[2]);
												lore.add(ChatColor.GRAY+"Size: "+t.getLength()+"x"+t.getLength());
												meta.setLore(lore);
												i1.setItemMeta(meta);
												if(t.canBeBuilt())in.addItem(i1);
											}
											p.openInventory(in);
											final InventorySelect inv = new InventorySelect(p, "Create Building", in);
											BuildingType type;
											while(true){
												try{ Thread.sleep(50);
												}catch(final Exception exception){}
												if(!p.isOnline()){
													inv.close();
													return;
												}
												if(inv.hasResponed()){
													try{
														type=BuildingType.getByName(ChatColor.stripColor(inv.getItem().getItemMeta().getDisplayName()));
														p.closeInventory();
														inv.close();
														break;
													}catch(final Exception exception){ inv.setResponse(null); }
												}
												if(inv.getInventory().getViewers().size()==0){
													inv.close();
													return;
												}
											}
											if(type!=null){
												final BuildBuilding bb = type.hasRoomToBuildFromCenter(kingdom, plot.getChunk());
												if(bb.canbuild){
													int[] cost = type.getCost(1);
													if(type.hasResourcesToBuild(bb.kingdom, 1)){
														if(p.getLevel()==0){
															p.sendMessage(ChatColor.DARK_AQUA+"Your villagers begin to build your "+ChatColor.GRAY+type.getName()+ChatColor.DARK_AQUA+", which has cost you "+ChatColor.AQUA+cost[0]+ChatColor.GRAY+" gold"+ChatColor.DARK_AQUA+", "+ChatColor.AQUA+cost[1]+ChatColor.GRAY+" wood"+ChatColor.DARK_AQUA+", and "+ChatColor.AQUA+cost[2]+ChatColor.GRAY+" stone"+ChatColor.DARK_AQUA+".");
															kingdom.collectResources(ResourceType.GOLD, cost[0]);
															kingdom.collectResources(ResourceType.WOOD, cost[1]);
															kingdom.collectResources(ResourceType.STONE, cost[2]);
															Chunk nwc = getNWCourner(plot.getChunk(), type.getLength());
															for(int x = nwc.getX(); x<nwc.getX()+type.getLength(); x++){ for(int z = nwc.getZ(); z<nwc.getZ()+type.getLength(); z++){ bb.kingdom.removeBuilding(PlotMg.getPlotAt(bb.kingdom.getWorld().getChunkAt(x, z)).getBuilding(), false, false, false); } }
															if(type==BuildingType.ROAD)new Road(nwc, kingdom);
															if(type==BuildingType.UNIVERSITY)new University(nwc, kingdom);
															if(type==BuildingType.GRANARY)new Granary(nwc, kingdom);
															if(type==BuildingType.SAWMILL)new Sawmill(nwc, kingdom);
															if(type==BuildingType.BLACKSMITH)new Blacksmith(nwc, kingdom);
															if(type==BuildingType.STONE_QUARRY)new StoneQuarry(nwc, kingdom);
															if(type==BuildingType.COTTAGE)new Cottage(nwc, kingdom);
															if(type==BuildingType.BARRACK)new Barrack(nwc, kingdom);
															if(type==BuildingType.ANCIENT_DOJO)new AncientDojo(nwc, kingdom);
															if(type==BuildingType.ANTIMAGIC_ONAGER_WORKSHOP)new AntiMagicOnagerWorkshop(nwc, kingdom);
															if(type==BuildingType.ARCHERY_RANGE)new ArcheryRange(nwc, kingdom);
															if(type==BuildingType.BALLISTIC_WORKSHOP)new BallisticWorkshop(nwc, kingdom);
															if(type==BuildingType.BATTERING_RAM_WORKSHOP)new BridgeMakerWorkshop(nwc, kingdom);
															if(type==BuildingType.BRIDGEMAKER_WORKSHOP)new BridgeMakerWorkshop(nwc, kingdom);
															if(type==BuildingType.CATAPULT_WORKSHOP)new CatapultWorkshop(nwc, kingdom);
															if(type==BuildingType.CHURCH)new Church(nwc, kingdom);
															if(type==BuildingType.COLISEUM)new Coliseum(nwc, kingdom);
															if(type==BuildingType.COW_TOSSER_WORKSHOP)new CowTosserWorkshop(nwc, kingdom);
															if(type==BuildingType.DRAGON_PIT)new DragonPit(nwc, kingdom);
															if(type==BuildingType.DRAGONS_LAIR)new DragonsLair(nwc, kingdom);
															if(type==BuildingType.DRUIDS_HALLOW)new DruidsHallow(nwc, kingdom);
															if(type==BuildingType.EARTH_TEMPLE)new EarthTemple(nwc, kingdom);
															if(type==BuildingType.FIRE_TEMPLE)new FireTemple(nwc, kingdom);
															if(type==BuildingType.GRAVEYARD)new Graveyard(nwc, kingdom);
															if(type==BuildingType.HOLY_BATTLEFIELD)new HolyBattlefield(nwc, kingdom);
															if(type==BuildingType.ILLUSIONISTS_TOWER)new IllusionistsTower(nwc, kingdom);
															if(type==BuildingType.MAGES_TOWER)new MagesTower(nwc, kingdom);
															if(type==BuildingType.MANGONEL_WORKSHOP)new MangonelWorkshop(nwc, kingdom);
															if(type==BuildingType.MEAD_HALL)new MeadHall(nwc, kingdom);
															if(type==BuildingType.MILITARY_FORT)new MilitaryFort(nwc, kingdom);
															if(type==BuildingType.NOBLE_MANOR)new NobleManor(nwc, kingdom);
															if(type==BuildingType.PORTAL_GATE)new PortalGate(nwc, kingdom);
															if(type==BuildingType.SANCTUARY)new Sanctuary(nwc, kingdom);
															if(type==BuildingType.SIEGE_TOWER_WORKSHOP)new SiegeTowerWorkshop(nwc, kingdom);
															if(type==BuildingType.STABLE)new Stable(nwc, kingdom);
															if(type==BuildingType.TREBUCHET_WORKSHOP)new TrebuchetWorkshop(nwc, kingdom);
															if(type==BuildingType.WALL)new Wall(nwc, kingdom);
															if(type==BuildingType.WATER_TEMPLE)new WaterTemple(nwc, kingdom);
															if(type==BuildingType.WIND_TEMPLE)new WindTemple(nwc, kingdom);
															if(type==BuildingType.METAL_MINING)new MetalMining(nwc, kingdom);
															if(type==BuildingType.MOAT)new Moat(nwc, kingdom);
															if(type==BuildingType.TOWNHALL)new Townhall(nwc, kingdom);
															if(type==BuildingType.TRADE_ROUTE)new TradeRoute(nwc, kingdom);
															if(type==BuildingType.GATEHOUSE)new Gatehouse(nwc, kingdom);
															if(type==BuildingType.WATCH_TOWER)new WatchTower(nwc, kingdom);
															if(type==BuildingType.TAVERN)new Tavern(nwc, kingdom);
															if(type==BuildingType.MARKETPLACE)new Marketplace(nwc, kingdom);
															PlayerMg.setStaminaTime(p, PlayerMg.BUILD_BUILDING);
														}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
													}else{
														p.sendMessage(ChatColor.RED+"Your kingdom does not have the required amount of resources to build this building!");
														p.sendMessage(ChatColor.RED+"Required Resources:");
														if(bb.kingdom.getMoney()<cost[0])p.sendMessage(ChatColor.RED+"Money: "+bb.kingdom.getMoney()+"/"+cost[0]);
														else p.sendMessage(ChatColor.GREEN+"Money: "+bb.kingdom.getMoney()+"/"+cost[0]);
														if(bb.kingdom.getResourceLevels(ResourceType.WOOD)<cost[1])p.sendMessage(ChatColor.RED+"Wood: "+bb.kingdom.getResourceLevels(ResourceType.WOOD)+"/"+cost[1]);
														else p.sendMessage(ChatColor.GREEN+"Wood: "+bb.kingdom.getResourceLevels(ResourceType.WOOD)+"/"+cost[1]);
														if(bb.kingdom.getResourceLevels(ResourceType.STONE)<cost[2])p.sendMessage(ChatColor.RED+"Stone: "+bb.kingdom.getResourceLevels(ResourceType.STONE)+"/"+cost[2]);
														else p.sendMessage(ChatColor.GREEN+"Stone: "+bb.kingdom.getResourceLevels(ResourceType.STONE)+"/"+cost[2]);
													}
												}else p.sendMessage(ChatColor.RED+"You cannot build this building here! The path is blocked.");
											}
										}else{
											if(plot.getBuilding().getHp()<plot.getBuilding().getMaxHp()){
												final double cost = (plot.getBuilding().getMaxHp()-(double)plot.getBuilding().getHp())/(double)plot.getBuilding().getMaxHp();
												final int[] c = new int[3];
												final int[] real = plot.getBuilding().getType().getCost(plot.getBuilding().getLevel());
												c[0]=(int)(real[0]*cost);
												c[1]=(int)(real[1]*cost);
												c[2]=(int)(real[2]*cost);
												final int gold = kingdom.getResourceLevels(ResourceType.GOLD);
												final int wood = kingdom.getResourceLevels(ResourceType.WOOD);
												final int stone = kingdom.getResourceLevels(ResourceType.STONE);
												if(gold>=c[0]
														&&wood>=c[1]
																&&stone>=c[2]){
													plot.getBuilding().setHp(plot.getBuilding().getMaxHp(), true);
													kingdom.collectResources(ResourceType.GOLD, c[0]);
													kingdom.collectResources(ResourceType.WOOD, c[1]);
													kingdom.collectResources(ResourceType.STONE, c[2]);
													p.sendMessage(ChatColor.GREEN+"Building repaired.");
													kingdom.awardAchievement(Achievement.BRICK_BY_BRICK);
													plot.getBuilding().updateStatus();
													p.setLevel(PlayerMg.REPAIR_BUILDING);
												}else{
													p.sendMessage(ChatColor.RED+"Not enough resources!");
													if(gold>=c[0])p.sendMessage(ChatColor.GREEN+"Gold: "+gold+"/"+c[0]);
													else p.sendMessage(ChatColor.RED+"Gold: "+gold+"/"+c[0]);
													if(wood>=c[1])p.sendMessage(ChatColor.GREEN+"Wood: "+wood+"/"+c[1]);
													else p.sendMessage(ChatColor.RED+"Wood: "+wood+"/"+c[1]);
													if(stone>=c[2])p.sendMessage(ChatColor.GREEN+"Stone: "+stone+"/"+c[2]);
													else p.sendMessage(ChatColor.RED+"Stone: "+stone+"/"+c[2]);
												}
											}else p.sendMessage(ChatColor.RED+"This building is not damaged!");
										}
									}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
								}else p.sendMessage(ChatColor.RED+"This is not a building or land!");
							}else p.sendMessage(ChatColor.RED+"This is not your kingdom!");
						}else p.sendMessage(ChatColor.RED+"There is no kingdom here!");
					}else if(item.getTypeId()==Material.ANVIL.getId()){
						if(p.getWorld().equals(kingdom.getWorld())){
							final Plot plot = PlotMg.getPlotAt(b.getLocation().getChunk());
							if(plot.isKingdomPlot()){
								if(plot.getKingdom().equals(kingdom)){
									if(!plot.getBuilding().isBuildingOrUpgrading()){
										if(p.getLevel()==0){
											plot.getBuilding().updateGraphics(false);
											plot.getBuilding().checkForLife();
											p.sendMessage(ChatColor.GREEN+"Area fixed.");
											kingdom.awardAchievement(Achievement.FIXING_WITH_THE_ANVIL);
											PlayerMg.setStaminaTime(p, PlayerMg.UPDATE);
										}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
									}else p.sendMessage(ChatColor.RED+"You cannot fixed a building that is already being built, upgraded, or updating!");
								}else p.sendMessage(ChatColor.RED+"This is not your kingdom, so you cannot fix buildings areas here!");
							}else{
								if(p.getLevel()==0){
									if(!plot.isClaimed()){
										ConquestTextures.getTexture(BuildingType.WILDERNESS, 1).buildAt(plot.getChunk(), null);
									}else if(plot.getOwner().equals(p.getName())){
										Chunk cords;
										maker:for(Building build : Building.getBuildings(kingdom)){
											cords=build.getNorthWestCourner();
											if(cords.getX()<=plot.getX()&&plot.getX()<=cords.getX()+build.getType().getLength()
													&&cords.getZ()<=plot.getZ()&&plot.getZ()<=cords.getZ()+build.getType().getLength()){
												for(int x = cords.getX(); x<cords.getX()+build.getType().getLength(); x++){
													for(int z = cords.getZ(); z<cords.getZ()+build.getType().getLength(); z++){
														PlotMg.getPlotAt(plot.getWorld(), x, z).setKingdom(kingdom, build);
													}
												}
												break maker;
											}
										}
										if(plot.isKingdomPlot())plot.getBuilding().updateGraphics(false);
										else plot.setOwner(null);
									}else{
										p.sendMessage(ChatColor.RED+"You do not own this area!");
										return;
									}
									p.sendMessage(ChatColor.GREEN+"Area fixed.");
									kingdom.awardAchievement(Achievement.FIXING_WITH_THE_ANVIL);
								}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
							}
						}else p.sendMessage(ChatColor.RED+"You cannot update areas outside of the world that your kingdom is in.");
					}else if(item.getTypeId()==Material.FIREBALL.getId()){
						p.sendMessage(ChatColor.YELLOW+"You have choosen to delete your kingdom. Please be aware this will remove all of your kingdom, including land, gold, villagers, troops, achievements, etc. This cannot be undone. If you still wish to delete your kingdom, please enter the name of your kingdom exactly. This is case sensitive, so please type it exactly. If you have changed your mind, and wish to keep your kingdom, ignore this message.");
						p.sendMessage(ChatColor.YELLOW+"Kingdom Name: "+ChatColor.GOLD+kingdom.getName());
						final CallName cn = new CallName(p, 0, null, "Delete Kingdom");
						int loops = 0;
						while(true){
							try{ Thread.sleep(50);
							}catch(final Exception exception){}
							loops++;
							if(cn.hasResponed()){
								final String s = cn.getName();
								cn.close();
								if(s.equals(kingdom.getName())){
									kingdom.deleteKingdom();
									return;
								}else{
									p.sendMessage(ChatColor.RED+"The names do not match. Kingdom deletion canceled.");
									return;
								}
							}
							if(loops>=900){
								cn.close();
								return;
							}
						}
					}else if(item.getTypeId()==Material.PAPER.getId()){
						if(action==Action.RIGHT_CLICK_AIR
								||action==Action.RIGHT_CLICK_BLOCK){
							final Inventory i = Bukkit.createInventory(null, 18, "Advanced Kingdom Info");
							int wood=0, arms=0, stone=0, iron=0, food=0, gold=0, villagers=0,
									wpd=0, spd=0, ipd=0, fpd=0, apd=0, vpd=5, gpd=0,
									wm=0, sm=0, im=0, fm=0, am=0, vm=0,
									land=0, lm=0, jobs=0, jobsmax=0,
									troops=0, units=0, siegeweapons=0, militarypower=0,
									fworkers=0, sworkers=0, aworkers=0, iworkers=0, wworkers=0, guards=0, jobless=0,
									maxfoodworkers=0, maxstoneworkers=0, maxironworkers=0, maxarmsworkers=0, maxwoodworkers=0, maxguards=0;
							villagers+=kingdom.getIdlingVillagers();
							gold=kingdom.getMoney();
							ResourceHolder r;
							TroopConvertable t;
							for(Building build : Building.getBuildings(kingdom)){
								if(build instanceof VillagerWorkable){
									jobs+=((VillagerWorkable)build).getWorkingVillagers();
									jobsmax+=(int)Math.max(build.getHp()*0.08, 1);
									villagers+=((VillagerWorkable)build).getWorkingVillagers();
									if(build instanceof WatchTower){
										guards+=((VillagerWorkable)build).getWorkingVillagers();
										maxguards+=(int)Math.max(build.getHp()*0.08, 1);
									}
								}
								if(build instanceof TroopConvertable){
									t=(TroopConvertable)build;
									if(t.isConvertingTroops())villagers+=t.getConvertAmount();
								}
								if(build instanceof Army){
									villagers+=build.getHp();
									troops+=build.getHp();
									militarypower+=build.getMaxHp();
									units++;
									if(((Army)build).getArmyType().isSiegeWeapon())siegeweapons++;
								}
								if(build instanceof ResourceHolder){
									r=(ResourceHolder)build;
									if(Arrays.asList(r.getResources()).contains(ResourceType.WOOD)){
										wood+=r.getResourceLevel(ResourceType.WOOD);
										wm+=r.getMaxResourceLevel();
										if(build instanceof VillagerWorkable){
											wpd+=(int)(kingdom.getMorale()/5.0*10/100*((VillagerWorkable)build).getWorkingVillagers()*(Arrays.asList(r.getResources()).contains(kingdom.getMainResource())?2:1))/r.getResourceLevels().length;
											wworkers+=((VillagerWorkable)build).getWorkingVillagers();
											maxwoodworkers+=(int)Math.max(build.getHp()*0.08, 1);
										}
									}
									if(Arrays.asList(r.getResources()).contains(ResourceType.FOOD)){
										food+=r.getResourceLevel(ResourceType.FOOD);
										fm+=r.getMaxResourceLevel();
										if(build instanceof VillagerWorkable){
											fpd+=(int)(kingdom.getMorale()/5.0*10/100*((VillagerWorkable)build).getWorkingVillagers()*(Arrays.asList(r.getResources()).contains(kingdom.getMainResource())?2:1))/r.getResourceLevels().length;
											fworkers+=((VillagerWorkable)build).getWorkingVillagers();
											maxfoodworkers+=(int)Math.max(build.getHp()*0.08, 1);
										}
									}
									if(Arrays.asList(r.getResources()).contains(ResourceType.IRON)){
										iron+=r.getResourceLevel(ResourceType.IRON);
										im+=r.getMaxResourceLevel();
										if(build instanceof VillagerWorkable){
											ipd+=(int)(kingdom.getMorale()/5.0*10/100*((VillagerWorkable)build).getWorkingVillagers()*(Arrays.asList(r.getResources()).contains(kingdom.getMainResource())?2:1))/r.getResourceLevels().length;
											iworkers+=((VillagerWorkable)build).getWorkingVillagers();
											maxironworkers+=(int)Math.max(build.getHp()*0.08, 1);
										}
									}
									if(Arrays.asList(r.getResources()).contains(ResourceType.ARMS)){
										arms+=r.getResourceLevel(ResourceType.ARMS);
										am+=r.getMaxResourceLevel();
										if(build instanceof VillagerWorkable){
											apd+=(int)(kingdom.getMorale()/5.0*10/100*((VillagerWorkable)build).getWorkingVillagers()*(Arrays.asList(r.getResources()).contains(kingdom.getMainResource())?2:1))/r.getResourceLevels().length;
											aworkers+=((VillagerWorkable)build).getWorkingVillagers();
											maxarmsworkers+=(int)Math.max(build.getHp()*0.08, 1);
										}
									}
									if(Arrays.asList(r.getResources()).contains(ResourceType.STONE)){
										stone+=r.getResourceLevel(ResourceType.STONE);
										sm+=r.getMaxResourceLevel();
										if(build instanceof VillagerWorkable){
											spd+=(int)(kingdom.getMorale()/5.0*10/100*((VillagerWorkable)build).getWorkingVillagers()*(Arrays.asList(r.getResources()).contains(kingdom.getMainResource())?2:1))/r.getResourceLevels().length;
											sworkers+=((VillagerWorkable)build).getWorkingVillagers();
											maxstoneworkers+=(int)Math.max(build.getHp()*0.08, 1);
										}
									}
								}
								if(build instanceof Cottage)vpd++;
								vm+=build.getVillagerMaxBonus();
								land+=Math.pow(build.getLength(), 2);
								lm+=build.getLandBonus();
							}
							gpd=(int)(kingdom.getMorale()/100.0*villagers);
							jobless=kingdom.getIdlingVillagers();
							final String moralestage;
							final String moraleeffects;
							final int foodworkerstosurvive = (int)(kingdom.getMainResource()==ResourceType.FOOD?(villagers*0.1):(villagers*0.2));
							if(kingdom.getMorale()==0){
								moralestage=ChatColor.GRAY+"Revolt";
								moraleeffects=ChatColor.GRAY+"No building, no upgrading, no repairing.";
							}else if(1<=kingdom.getMorale()&&kingdom.getMorale()<=20){
								moralestage=ChatColor.GRAY+"Grim";
								moraleeffects=ChatColor.GRAY+"No repairing, no upgrading.";
							}else if(21<=kingdom.getMorale()&&kingdom.getMorale()<=40){
								moralestage=ChatColor.GRAY+"Depressed";
								moraleeffects=ChatColor.GRAY+"No upgrading.";
							}else if(41<=kingdom.getMorale()&&kingdom.getMorale()<=60){
								moralestage=ChatColor.GRAY+"Normal";
								moraleeffects=ChatColor.GRAY+"Nothing.";
							}else if(61<=kingdom.getMorale()&&kingdom.getMorale()<=80){
								moralestage=ChatColor.GRAY+"Happy";
								moraleeffects=ChatColor.GRAY+"25% off upgrade costs.";
							}else if(81<=kingdom.getMorale()&&kingdom.getMorale()<=99){
								moralestage=ChatColor.GRAY+"Joyous";
								moraleeffects=ChatColor.GRAY+"50% off upgrade costs, x2 troop conversion speed.";
							}else{
								moralestage=ChatColor.GRAY+"Exuberant";
								moraleeffects=ChatColor.GRAY+"75% off upgrade costs, x3 troop conversion speed.";
							}
							i.addItem(
									makeItem(ChatColor.YELLOW+"Wood", Material.WOOD.getId(),
											ChatColor.DARK_AQUA+"Amount: "+ChatColor.AQUA+wood,
											ChatColor.DARK_AQUA+"Max: "+ChatColor.AQUA+wm,
											ChatColor.DARK_AQUA+"Percent ("+(int)((double)wood/wm*100)+"%): "+ChatColor.DARK_GRAY+"{"+makeBarGraph(wood, wm)+ChatColor.DARK_GRAY+"}",
											ChatColor.DARK_AQUA+"Gain/Day: "+ChatColor.AQUA+wpd),
											makeItem(ChatColor.YELLOW+"Food", Material.APPLE.getId(),
													ChatColor.DARK_AQUA+"Amount: "+ChatColor.AQUA+food,
													ChatColor.DARK_AQUA+"Max: "+ChatColor.AQUA+fm,
													ChatColor.DARK_AQUA+"Percent: ("+(int)((double)food/fm*100)+"%)"+ChatColor.DARK_GRAY+"{"+makeBarGraph(food, fm)+ChatColor.DARK_GRAY+"}",
													ChatColor.DARK_AQUA+"Gain/Day: "+ChatColor.AQUA+fpd,
													ChatColor.DARK_AQUA+"Required Food Workers to Survive: "+ChatColor.AQUA+foodworkerstosurvive,
													ChatColor.DARK_AQUA+"Food Workers: "+(fworkers>=foodworkerstosurvive?ChatColor.GREEN:ChatColor.RED)+fworkers),
													makeItem(ChatColor.YELLOW+"Stone", Material.STONE.getId(),
															ChatColor.DARK_AQUA+"Amount: "+ChatColor.AQUA+stone,
															ChatColor.DARK_AQUA+"Max: "+ChatColor.AQUA+sm,
															ChatColor.DARK_AQUA+"Percent: ("+(int)((double)stone/sm*100)+"%)"+ChatColor.DARK_GRAY+"{"+makeBarGraph(stone, sm)+ChatColor.DARK_GRAY+"}",
															ChatColor.DARK_AQUA+"Gain/Day: "+ChatColor.AQUA+spd),
															makeItem(ChatColor.YELLOW+"Arms", Material.IRON_SWORD.getId(),
																	ChatColor.DARK_AQUA+"Amount: "+ChatColor.AQUA+arms,
																	ChatColor.DARK_AQUA+"Max: "+ChatColor.AQUA+am,
																	ChatColor.DARK_AQUA+"Percent: ("+(int)((double)arms/am*100)+"%)"+ChatColor.DARK_GRAY+"{"+makeBarGraph(arms, am)+ChatColor.DARK_GRAY+"}",
																	ChatColor.DARK_AQUA+"Gain/Day: "+ChatColor.AQUA+apd),
																	makeItem(ChatColor.YELLOW+"Iron", Material.IRON_INGOT.getId(),
																			ChatColor.DARK_AQUA+"Amount: "+ChatColor.AQUA+iron,
																			ChatColor.DARK_AQUA+"Max: "+ChatColor.AQUA+im,
																			ChatColor.DARK_AQUA+"Percent: ("+(int)((double)iron/im*100)+"%)"+ChatColor.DARK_GRAY+"{"+makeBarGraph(iron, im)+ChatColor.DARK_GRAY+"}",
																			ChatColor.DARK_AQUA+"Gain/Day: "+ChatColor.AQUA+ipd),
																			makeItem(ChatColor.YELLOW+"Land", Material.GRASS.getId(),
																					ChatColor.DARK_AQUA+"Amount: "+ChatColor.AQUA+land,
																					ChatColor.DARK_AQUA+"Max: "+ChatColor.AQUA+lm,
																					ChatColor.DARK_AQUA+"Permanent Land Max: "+ChatColor.AQUA+PlayerMg.getLandMax(p.getName(), 1),
																					ChatColor.DARK_AQUA+"Buffer Land Max: "+ChatColor.AQUA+PlayerMg.getLandMax(p.getName(), 0),
																					ChatColor.DARK_AQUA+"Total Land Max: "+ChatColor.AQUA+PlayerMg.getLandMax(p.getName(), 2),
																					ChatColor.DARK_AQUA+"Percent: ("+(int)((double)land/lm*100)+"%)"+ChatColor.DARK_GRAY+"{"+makeBarGraph(land, lm)+ChatColor.DARK_GRAY+"}"),
																					makeItem(ChatColor.YELLOW+"Gold", Material.GOLD_INGOT.getId(),
																							ChatColor.DARK_AQUA+"Amount: "+ChatColor.AQUA+gold,
																							ChatColor.DARK_AQUA+"Gain/Day: "+ChatColor.AQUA+gpd),
																							makeItem(ChatColor.YELLOW+"Villagers", Material.LEATHER_BOOTS.getId(),
																									ChatColor.DARK_AQUA+"Amount: "+ChatColor.AQUA+villagers,
																									ChatColor.DARK_AQUA+"Max: "+ChatColor.AQUA+vm,
																									ChatColor.DARK_AQUA+"Percent: ("+(int)((double)villagers/vm*100)+"%)"+ChatColor.DARK_GRAY+"{"+makeBarGraph(villagers, vm)+ChatColor.DARK_GRAY+"}",
																									ChatColor.DARK_AQUA+"Gain/Day: "+ChatColor.AQUA+vpd),
																									makeItem(ChatColor.YELLOW+"Jobs", Material.STONE_PICKAXE.getId(),
																											ChatColor.DARK_AQUA+"Amount: "+ChatColor.AQUA+jobs,
																											ChatColor.DARK_AQUA+"Max: "+ChatColor.AQUA+jobsmax,
																											ChatColor.DARK_AQUA+"Percent: ("+(int)((double)jobs/jobsmax*100)+"%)"+ChatColor.DARK_GRAY+"{"+makeBarGraph(jobs, jobsmax)+ChatColor.DARK_GRAY+"}",
																											ChatColor.DARK_AQUA+"Jobless: "+ChatColor.AQUA+jobless,
																											ChatColor.DARK_AQUA+"Workers/Food: "+makeJobPercent(fworkers, maxfoodworkers),
																											ChatColor.DARK_AQUA+"Workers/Wood: "+makeJobPercent(wworkers, maxwoodworkers),
																											ChatColor.DARK_AQUA+"Workers/Iron: "+makeJobPercent(iworkers, maxironworkers),
																											ChatColor.DARK_AQUA+"Workers/Arms: "+makeJobPercent(aworkers, maxarmsworkers),
																											ChatColor.DARK_AQUA+"Workers/Stone: "+makeJobPercent(sworkers, maxstoneworkers),
																											ChatColor.DARK_AQUA+"Workers/Garrisoned: "+makeJobPercent(guards, maxguards),
																											ChatColor.DARK_AQUA+"Workers/Military: "+makeJobPercent(troops, militarypower)),
																											makeItem(ChatColor.YELLOW+"Morale", Material.FIREWORK.getId(),
																													ChatColor.DARK_AQUA+"Amount: "+kingdom.getMorale(),
																													ChatColor.DARK_AQUA+"Stage: "+moralestage,
																													ChatColor.DARK_AQUA+"Effects: "+moraleeffects),
																													makeItem(ChatColor.YELLOW+"Troops", Material.DIAMOND_SWORD.getId(),
																															ChatColor.DARK_AQUA+"Amount: "+troops,
																															ChatColor.DARK_AQUA+"Units: "+units,
																															ChatColor.DARK_AQUA+"Siegeweapons: "+siegeweapons,
																															ChatColor.DARK_AQUA+"Max Power: "+militarypower,
																															ChatColor.DARK_AQUA+"Percent: ("+(int)((double)troops/militarypower*100)+"%)"+ChatColor.DARK_GRAY+"{"+makeBarGraph(troops, militarypower)+ChatColor.DARK_GRAY+"}")
									);
							p.openInventory(i);
							kingdom.awardAchievement(Achievement.ADVANCED_KINGDOM_INFO);
						}else{
							if(p.getWorld().equals(kingdom.getWorld())){
								final Plot plot = PlotMg.getPlotAt(b.getLocation().getChunk());
								if(plot.isKingdomPlot()){
									if(plot.getKingdom().equals(kingdom)
											||plot.getBuilding().hasSpy(kingdom)){
										String[] st = plot.getBuilding().getInfo();
										for(String s : st){ p.sendMessage(s); }
										kingdom.awardAchievement(Achievement.KINGDOM_INFO);
									}else p.sendMessage(ChatColor.RED+"This is not your kingdom, so you cannot view info here!");
								}else p.sendMessage(ChatColor.RED+"There is no kingdom here!");
							}else p.sendMessage(ChatColor.RED+"You cannot view info in areas outside of the world that your kingdom is in.");
						}
					}else if(item.getTypeId()==Material.FLINT_AND_STEEL.getId()){
						if(p.getWorld().equals(kingdom.getWorld())){
							Plot plot = PlotMg.getPlotAt(b.getChunk());
							if(p.getLevel()==0){
								if(plot.isKingdomPlot()){
									if(plot.getKingdom().equals(kingdom)){
										if(plot.getBuilding().getType().canBeBuilt()){
											PlayerMg.setStaminaTime(p, PlayerMg.DESTROY_BUILDING);
											Building build = plot.getBuilding();
											kingdom.removeBuilding(plot.getBuilding(), true, true, false);
											if(build instanceof VillagerWorkable)kingdom.addVillagers(((VillagerWorkable)build).getWorkingVillagers());
											if(build instanceof ResourceHolder)for(ResourceType rt : ((ResourceHolder)build).getResources()){ kingdom.addResources(rt, build.getResources(rt)); }
											p.sendMessage(ChatColor.GREEN+"Building destroyed.");
										}else if(plot.getBuilding().getType()==BuildingType.LAND){
											PlayerMg.setStaminaTime(p, PlayerMg.DESTROY_BUILDING);
											kingdom.removeBuilding(plot.getBuilding(), false, true, false);
											p.sendMessage(ChatColor.GREEN+"Area unclaimed.");
										}else if(plot.getBuilding().getType()==BuildingType.ARMY){
											PlayerMg.setStaminaTime(p, PlayerMg.DESTROY_BUILDING);
											kingdom.addVillagers(plot.getBuilding().getHp());
											plot.getBuilding().delete(true, false);
											p.sendMessage(ChatColor.GREEN+"Troops sent home.");
										}else p.sendMessage(ChatColor.RED+"You cannot destroy this building!");
									}else p.sendMessage(ChatColor.RED+"This is not your kingdom!");
								}else p.sendMessage(ChatColor.RED+"There is no kingdom here!");
							}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
						}else p.sendMessage(ChatColor.RED+"You cannot destroy buildings outside of the world your kingdom is in!");
					}else if(item.getTypeId()==Material.FEATHER.getId()){
						final Plot plot = PlotMg.getPlotAt(b.getChunk());
						if(plot.getKingdom()==kingdom){
							final WraithavenScript s = plot.getBuilding().getOldestScript();
							if(s!=null){
								plot.getBuilding().removeScript(s);
								Bukkit.getScheduler().runTaskAsynchronously(WhCommunity.plugin, new Runnable(){ public void run(){ s.run(); } });
							}else p.sendMessage(ChatColor.RED+"This building has no events currently!");
						}else p.sendMessage(ChatColor.RED+"This is not your kingdom!");
					}else if(item.getTypeId()==Material.BOOK_AND_QUILL.getId()){
						Inventory i = Bukkit.createInventory(null, 54, ChatColor.RED+"Total"+ChatColor.BLUE+" Score");
						redrawInventory(i, 9);
						final InventorySelect inv = new InventorySelect(p, "Score", i);
						p.openInventory(i);
						while(true){
							try{ Thread.sleep(50);
							}catch(final Exception exception){}
							if(inv.hasResponed()){
								if(45<=inv.getSlot()&&inv.getSlot()<=53){
									if(inv.getSlot()==45)i=Bukkit.createInventory(null, 54, ChatColor.RED+"Power"+ChatColor.BLUE+" Score");
									if(inv.getSlot()==46)i=Bukkit.createInventory(null, 54, ChatColor.RED+"Achievements"+ChatColor.BLUE+" Score");
									if(inv.getSlot()==47)i=Bukkit.createInventory(null, 54, ChatColor.RED+"Wealth"+ChatColor.BLUE+" Score");
									if(inv.getSlot()==48)i=Bukkit.createInventory(null, 54, ChatColor.RED+"Estate"+ChatColor.BLUE+" Score");
									if(inv.getSlot()==49)i=Bukkit.createInventory(null, 54, ChatColor.RED+"Defenses"+ChatColor.BLUE+" Score");
									if(inv.getSlot()==50)i=Bukkit.createInventory(null, 54, ChatColor.RED+"Technology"+ChatColor.BLUE+" Score");
									if(inv.getSlot()==51)i=Bukkit.createInventory(null, 54, ChatColor.RED+"Resources"+ChatColor.BLUE+" Score");
									if(inv.getSlot()==52)i=Bukkit.createInventory(null, 54, ChatColor.RED+"Population"+ChatColor.BLUE+" Score");
									if(inv.getSlot()==53)i=Bukkit.createInventory(null, 54, ChatColor.RED+"Total"+ChatColor.BLUE+" Score");
									p.openInventory(i);
									inv.setInventory(i);
									redrawInventory(i, inv.getSlot()-44);
								}
								inv.setResponse(null);
							}
							if(i.getViewers().size()==0){
								inv.close();
								return;
							}
						}
					}else if(item.getTypeId()==Material.DIAMOND_SWORD.getId()){
						Army selected = PlayerMg.getSelectedArmy(p.getName());
						Plot plot = PlotMg.getPlotAt(b.getChunk());
						boolean reform = false;
						int x = b.getX()&0x000F;
						int z = b.getZ()&0x000F;
						int y = b.getY();
						if(plot.isKingdomPlot()
								&&plot.getBuilding()==selected){
							Army army = (Army)plot.getBuilding();
							ArmyFormation form = army.getFormation();
							if(y==3+plot.getHeight()){
								if(x==0){
									if(p.getLevel()==0){
										army.setFormation(ArmyFormation.getBySides(form.usesSide(Direction.NORTH), form.usesSide(Direction.EAST), form.usesSide(Direction.SOUTH), !form.usesSide(Direction.WEST)));
										p.sendMessage(ChatColor.GREEN+"Formation updated.");
										kingdom.awardAchievement(Achievement.POSITION_THE_TROOPS);
										if(army.isCamping())kingdom.awardAchievement(Achievement.GOING_CAMPING);
										PlayerMg.setStaminaTime(p, PlayerMg.REFORM_ARMY);
										reform=true;
									}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
								}else if(x==15){
									if(p.getLevel()==0){
										army.setFormation(ArmyFormation.getBySides(form.usesSide(Direction.NORTH), !form.usesSide(Direction.EAST), form.usesSide(Direction.SOUTH), form.usesSide(Direction.WEST)));
										p.sendMessage(ChatColor.GREEN+"Formation updated.");
										kingdom.awardAchievement(Achievement.POSITION_THE_TROOPS);
										if(army.isCamping())kingdom.awardAchievement(Achievement.GOING_CAMPING);
										PlayerMg.setStaminaTime(p, PlayerMg.REFORM_ARMY);
										reform=true;
									}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
								}else if(z==0){
									if(p.getLevel()==0){
										army.setFormation(ArmyFormation.getBySides(!form.usesSide(Direction.NORTH), form.usesSide(Direction.EAST), form.usesSide(Direction.SOUTH), form.usesSide(Direction.WEST)));
										p.sendMessage(ChatColor.GREEN+"Formation updated.");
										kingdom.awardAchievement(Achievement.POSITION_THE_TROOPS);
										if(army.isCamping())kingdom.awardAchievement(Achievement.GOING_CAMPING);
										PlayerMg.setStaminaTime(p, PlayerMg.REFORM_ARMY);
										reform=true;
									}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
								}else if(z==15){
									if(p.getLevel()==0){
										army.setFormation(ArmyFormation.getBySides(form.usesSide(Direction.NORTH), form.usesSide(Direction.EAST), !form.usesSide(Direction.SOUTH), form.usesSide(Direction.WEST)));
										p.sendMessage(ChatColor.GREEN+"Formation updated.");
										kingdom.awardAchievement(Achievement.POSITION_THE_TROOPS);
										if(army.isCamping())kingdom.awardAchievement(Achievement.GOING_CAMPING);
										PlayerMg.setStaminaTime(p, PlayerMg.REFORM_ARMY);
										reform=true;
									}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
								}
							}
						}
						if(!reform){
							if(selected==null){
								if(p.getWorld().equals(kingdom.getWorld())){
									if(p.getLevel()==0){
										if(plot.isKingdomPlot()){
											if(plot.getKingdom()==kingdom){
												if(plot.getBuilding().getType()==BuildingType.ARMY){
													Army army = (Army)plot.getBuilding();
													army.setSelected(true);
													PlayerMg.setSelectedArmy(p.getName(), army);
													p.sendMessage(ChatColor.GREEN+"Unit selected.");
													kingdom.awardAchievement(Achievement.TROOP_YOURE_IT);
												}else p.sendMessage(ChatColor.RED+"This is not an army chunk!");
											}else p.sendMessage(ChatColor.RED+"This is not your kingdom!");
										}else p.sendMessage(ChatColor.RED+"There is no kingdom here!");
									}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
								}else p.sendMessage(ChatColor.RED+"You cannot reform armies outside of the world your kingdom is in!");
							}else{
								if(p.getWorld().equals(kingdom.getWorld())){
									if(p.getLevel()==0){
										if(!plot.isKingdomPlot()){
											if(selected.getStamina()>=1){
												if(!selected.isCamping()
														||selected.hasCommander()){
													final int sx = selected.getNorthWestCourner().getX();
													final int sz = selected.getNorthWestCourner().getZ();
													final int cx = b.getChunk().getX();
													final int cz = b.getChunk().getZ();
													if(sx-25<=cx&&cx<=sx+25
															&&sz-25<=cz&&cz<=sz+25){
														if(selected.isAttacking())selected.setTarget(null);
														selected.setMoveGoal(b.getChunk().getX(), b.getChunk().getZ());
														PlayerMg.setStaminaTime(p, PlayerMg.MOVE_ARMY);
														p.sendMessage(ChatColor.GREEN+"Unit moved.");
														kingdom.awardAchievement(Achievement.MOVE_EM_OUT);
													}else p.sendMessage(ChatColor.RED+"This troop cannot move that far!");
												}else p.sendMessage(ChatColor.RED+"This unit is in camping formation, and cannot recive commands in this formation.");
											}else p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to preform this action!");
										}else{
											if(plot.getKingdom()!=kingdom){
												if(!selected.isCamping()
														||selected.hasCommander()){
													if(selected.getStamina()>0){
														boolean direction = false;
														if(plot.getZ()<selected.getNorthWestCourner().getZ()
																&&plot.getX()==selected.getNorthWestCourner().getX()){
															if(selected.getFormation().usesSide(Direction.NORTH)){
																if(selected.getNorthWestCourner().getZ()-plot.getZ()<=selected.getArmyType().getRange())direction=true;
																else p.sendMessage(ChatColor.RED+"This unit cannot attack over a range that far!");
															}else p.sendMessage(ChatColor.RED+"This unit is not facing north!");
														}else if(plot.getZ()>selected.getNorthWestCourner().getZ()
																&&plot.getX()==selected.getNorthWestCourner().getX()){
															if(selected.getFormation().usesSide(Direction.SOUTH)){
																if(plot.getZ()-selected.getNorthWestCourner().getZ()<=selected.getArmyType().getRange())direction=true;
																else p.sendMessage(ChatColor.RED+"This unit cannot attack over a range that far!");
															}else p.sendMessage(ChatColor.RED+"This unit is not facing south!");
														}else if(plot.getX()>selected.getNorthWestCourner().getX()
																&&plot.getZ()==selected.getNorthWestCourner().getZ()){
															if(selected.getFormation().usesSide(Direction.EAST)){
																if(plot.getX()-selected.getNorthWestCourner().getX()<=selected.getArmyType().getRange())direction=true;
																else p.sendMessage(ChatColor.RED+"This unit cannot attack over a range that far!");
															}else p.sendMessage(ChatColor.RED+"This unit is not facing east!");
														}else{
															if(selected.getFormation().usesSide(Direction.WEST)){
																if(selected.getNorthWestCourner().getX()-plot.getX()<=selected.getArmyType().getRange())direction=true;
																else p.sendMessage(ChatColor.RED+"This unit cannot attack over a range that far!");
															}else p.sendMessage(ChatColor.RED+"This unit is not facing west!");
														}
														if(direction
																||selected.hasCommander()){
															selected.setTarget(plot.getBuilding());
															PlayerMg.setStaminaTime(p, PlayerMg.REFORM_ARMY);
															p.sendMessage(ChatColor.GREEN+"This unit is now attacking.");
														}
													}else p.sendMessage(ChatColor.RED+"That unit is out of stamina!");
												}else p.sendMessage(ChatColor.RED+"This unit is camping, and cannot preform that action!");
											}else{
												if(plot.getBuilding()==selected){
													selected.setSelected(false);
													PlayerMg.setSelectedArmy(p.getName(), null);
													p.sendMessage(ChatColor.GREEN+"Unit unselected.");
												}else if(plot.getBuilding() instanceof Army){
													Army army = (Army)plot.getBuilding();
													selected.setSelected(false);
													army.setSelected(true);
													PlayerMg.setSelectedArmy(p.getName(), army);
													p.sendMessage(ChatColor.GREEN+"Unit selected.");
												}else p.sendMessage(ChatColor.RED+"Unknown unit command!");
											}
										}
									}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
								}else p.sendMessage(ChatColor.RED+"You cannot reform armies outside of the world your kingdom is in!");
							}
						}
					}else if(item.getTypeId()==Material.BEACON.getId()){
						kingdom.awardAchievement(Achievement.HOME_AT_LAST);
						p.teleport(kingdom.getBuildingLocations(BuildingType.CAPITOL).get(0).getOffSetChunk(3, 3).getChunk().getBlock(7, 100, 7).getLocation());
					}else if(item.getTypeId()==Material.GOLD_HELMET.getId()){
						if(converttroops.containsKey(p.getName())){
							converttroops.put(p.getName(), b.getChunk());
							return;
						}
						if(!movingvillagers.contains(p.getName())){
							final Plot plot = PlotMg.getPlotAt(b.getChunk());
							if(plot.getWorld()==kingdom.getWorld()){
								if(plot.isKingdomPlot()){
									if(plot.getKingdom()==kingdom){
										if(plot.getKingdom()==kingdom){
											if(!plot.getBuilding().isBuildingOrUpgrading()){
												if(plot.getBuilding() instanceof VillagerWorkable){
													VillagerWorkable v = (VillagerWorkable)plot.getBuilding();
													int toput = chooseVillagers(p, kingdom.getIdlingVillagers(), (int)Math.max(plot.getBuilding().getHp()*0.08, 1)-v.getWorkingVillagers());
													if(toput==-1)return;
													if(v.getWorkingVillagers()+toput<=Math.max(plot.getBuilding().getHp()*0.08, 1)){
														v.setWorkingVillagers(v.getWorkingVillagers()+toput);
														kingdom.setIdlingVillagers(kingdom.getIdlingVillagers()-toput);
														plot.getBuilding().updateStatus();
														p.sendMessage(ChatColor.GREEN+"Villagers put to work.");
														kingdom.awardAchievement(Achievement.PUT_EM_TO_WORK);
													}else p.sendMessage(ChatColor.RED+"There is not enough room in this building to put that many villagers!");
												}else if(plot.getBuilding() instanceof TroopConvertable){
													TroopConvertable barrack = (TroopConvertable)plot.getBuilding();
													Chunk c;
													if(!barrack.isConvertingTroops()){
														int toput = chooseVillagers(p, kingdom.getIdlingVillagers(), plot.getBuilding().getLevel()*100);
														if(toput==-1)return;
														if(toput<=plot.getBuilding().getLevel()*100){
															p.sendMessage(ChatColor.GREEN+"Please select a chunk where the troops shall be delployed at. Select by clicking with the gold helmet. Note: If this area is blocked when the troops are finished training, they cannot deploy and the conversion is cancelled.");
															converttroops.put(p.getName(), null);
															int loops = 0;
															while(true){
																loops++;
																if(loops>=6000
																		||!p.isOnline()){
																	converttroops.remove(p.getName());
																	return;
																}
																try{ Thread.sleep(50);
																}catch(Exception exception){}
																c=converttroops.get(p.getName());
																if(c!=null){
																	converttroops.remove(p.getName());
																	break;
																}
															}
															Plot p1 = PlotMg.getPlotAt(c);
															if(!p1.isKingdomPlot()){
																boolean tk = false;
																Plot p2;
																for(int x = p1.getX()-1; x<=p1.getX()+1; x++){
																	for(int z = p1.getZ()-1; z<=p1.getZ()+1; z++){
																		p2=PlotMg.getPlotAt(p1.getWorld(), x, z);
																		if(p2.isKingdomPlot()
																				&&p2.getKingdom().equals(kingdom)
																				&&!(p2.getBuilding() instanceof Army))tk=true;
																	}
																}
																if(tk){
																	barrack.convert(toput, c);
																	kingdom.setIdlingVillagers(kingdom.getIdlingVillagers()-toput);
																	p.sendMessage(ChatColor.GREEN+"Converting troops now.");
																	kingdom.awardAchievement(Achievement.MAKING_SOLDIERS);
																}else p.sendMessage(ChatColor.RED+"the chunk must be touching your kingdom!");
															}else p.sendMessage(ChatColor.RED+"This chunk is already in use!");
														}else p.sendMessage(ChatColor.RED+"This building is too low to create a unit of that size!");
													}else p.sendMessage(ChatColor.RED+"This building is already converting troops!");
												}else p.sendMessage(ChatColor.RED+"This building does not hold villagers or convert troops!");
											}else p.sendMessage(ChatColor.RED+"You cannot move villagers into, or out of a building that is building or upgrading!");
										}else p.sendMessage(ChatColor.RED+"These villagers are not from that kingdom!");
									}else p.sendMessage(ChatColor.RED+"This is not your kingdom!");
								}else p.sendMessage(ChatColor.RED+"There is no kingdom here!");
							}else p.sendMessage(ChatColor.RED+"You cannot move villagers outside of the world your in!");
						}
					}else if(item.getTypeId()==Material.SKULL_ITEM.getId()){
						p.sendMessage(ChatColor.GREEN+"Please type in your bug and it will automaticly be submitted. NOTE: Abuse of this tool can and will result in a ban.");
						final CallName cn = new CallName(p, 0, null, "Bug Report");
						while(true){
							try{ Thread.sleep(50);
							}catch(final Exception exception){}
							if(cn.hasResponed()){
								p.sendMessage(ChatColor.GREEN+"Bug submitted. Thank you.");
								WhCommunity.reportBug(cn.getName()+"; "+p.getName()+"; "+kingdom.getName());
								cn.close();
								return;
							}
							if(!p.isOnline()){
								cn.close();
								return;
							}
						}
					}else if(item.getTypeId()==Material.SIGN.getId()){
						final Inventory i = Bukkit.createInventory(null, 27, "Itembar Manager");
						final ArrayList<Tool> tools = new ArrayList<>();
						tools.add(new Tool(Material.PAPER, "Info Tool", p.getName(), 'A', 1, "Click on areas to get information on", "the area you clicked."));
						tools.add(new Tool(Material.LEATHER_BOOTS, "Scouting Tool", p.getName(), 'B', 1, "Click areas in the wilderness to", "scout more land for your kingdom."));
						tools.add(new Tool(Material.IRON_SPADE, "Resource Collection Tool", p.getName(), 'C', 1, "Click on resource filled areas of land to harvest", "the available resources."));
						tools.add(new Tool(Material.CLAY_BRICK, "Build or Repair Tool", p.getName(), 'D', 1, "Right click to scroll through buildings.", "Then click on the ground in an area of land", "to build the building there.", "Clicking on a building repairs it."));
						tools.add(new Tool(Material.NETHER_STAR, "Upgrade Tool", p.getName(), 'E', 1, "Click on buildings to upgrade them."));
						tools.add(new Tool(Material.FLINT_AND_STEEL, "Delete Area Tool", p.getName(), 'F', 1, "Click on buildings to clear them back", "into land. Or click on land to revert the", "area back into wilderness."));
						tools.add(new Tool(Material.FEATHER, "Message Tool", p.getName(), 'G', 1, "Click on buildings to get", "the next message event."));
						tools.add(new Tool(Material.BOOK_AND_QUILL, "Scoreboard Tool", p.getName(), 'H', 1, "Use to check the scores of all kingdoms."));
						tools.add(new Tool(Material.DIAMOND_SWORD, "Military Control Tool", p.getName(), 'I', 1, "Click on a military unit to select it", "then click on something else to", "assign it a task. Clicking in", "the wilderness will cause it to move.", "Clicking on another kingdom's land or military", "unit will cause it to attack.", "Clicking on the wool reforms it."));
						tools.add(new Tool(Material.COMPASS, "Teleportation Tool", p.getName(), 'J', 1, "Click to teleport to different areas", "of the map. The arrow will point to", "to the location of your capital."));
						tools.add(new Tool(Material.BEACON, "Capital Warp Tool", p.getName(), 'K', 1, "Place the beacon to warp back to your capital."));
						tools.add(new Tool(Material.ARROW, "Speed Tool", p.getName(), 'L', 1, "Place the beacon to warp back to your capital."));
						tools.add(new Tool(Material.GOLD_HELMET, "Villager Movement Tool", p.getName(), 'M', 1, "Click on buildings to move villagers.", "Click on troop conversion buildings to allow", "them to start turning villagers into", "troops for you."));
						tools.add(new Tool(Material.FIREBALL, "Delete Kingdom Tool", p.getName(), 'N', 1, "Deletes your kingdom."));
						tools.add(new Tool(Material.ANVIL, "Fix Tool", p.getName(), 'O', 1, "Use this to fix broken buildings."));
						tools.add(new Tool(Material.SKULL_ITEM, "Bug Report Tool", p.getName(), 'P', 1, "Use this to report any bugs your find", "in the game."));
						tools.add(new Tool(Material.SIGN, "Itembar Manager Tool", p.getName(), 'Q', 1, "Click to switch around the placement of items", "in your toolbars. Exit the inventory view", "to apply changes."));
						tools.add(new Tool(Material.DIAMOND, "Achivement Tool", p.getName(), 'R', 1, "Use this to view all your kingdom's current", "achivements."));
						tools.add(new Tool(Material.GOLD_INGOT, "Henchmen Tool", p.getName(), 'S', 1, "Right click to scroll through current", "henchmen, and left click to use", "them. Click on a tavern to hire them."));
						tools.add(new Tool(Material.LEVER, "Info Monitor Tool", p.getName(), 'T', 1, "Click to scroll through display types.", "Clicking on buildings will monitor that building", "or a list of buildings."));
						tools.add(new Tool(Material.BLAZE_ROD, "Troop Special Tool", p.getName(), 'U', 1, "Click to activate a troops special", "abilities."));
						tools.add(new Tool(Material.MINECART, "Change Height Tool", p.getName(), 'V', 1, "Left click land to raise it's height,", "and right click to lower it."));
						tools.add(new Tool(Material.CAKE, "Help Tool", p.getName(), 'W', 1, "Click on a building to the help for it."));
						tools.add(new Tool(Material.WEB, "Website Tool", p.getName(), 'X', 1, "Click to recive a link to our website."));
						tools.add(new Tool(Material.GOLD_NUGGET, "Buy Feature Tool", p.getName(), 'Y', 1, "Right click to scroll through donator features,", "and left click to buy them."));
						tools.add(new Tool(Material.GOLD_BLOCK, "Donate Tool", p.getName(), 'Z', 1, "Gives a link to where you can donate and", "earn donator points."));
						tools.add(new Tool(Material.FIREWORK, "Credits Tool", p.getName(), '!', 1, "Click to view conquest's credits."));
						for(Tool t : orderItemList(tools)){ i.addItem(t.getItem()); }
						p.openInventory(i);
						kingdom.awardAchievement(Achievement.ORGANIZE_ME);
						while(true){
							try{ Thread.sleep(50);
							}catch(final Exception exception){}
							if(i.getViewers().size()==0)break;
						}
						try{
							String order = "";
							ItemStack meh;
							for(int x = 26; x>=0; x--){
								meh=i.getItem(x);
								if(meh.getType()!=Material.SUGAR)order+=getToolFromItem(tools, i.getItem(x)).getSymbol();
							}
							PlayerMg.setItembar(p, order);
						}catch(final Exception exception){}
					}else if(item.getTypeId()==Material.DIAMOND.getId()){
						final Inventory i;
						final boolean tut = action==Action.LEFT_CLICK_AIR
								||action==Action.LEFT_CLICK_BLOCK;
						if(tut)i=Bukkit.createInventory(null, 54, ChatColor.AQUA+"Tutorial Achivements");
						else i=Bukkit.createInventory(null, 54, ChatColor.AQUA+"Kingdom Achivements");
						ItemStack it;
						ItemMeta meta;
						ArrayList<String> lore;
						for(Achievement a : kingdom.getAchievements()){
							if(tut!=a.isTutorial())continue;
							it=new ItemStack(Material.DIAMOND);
							meta=it.getItemMeta();
							lore=new ArrayList<>();
							meta.setDisplayName(ChatColor.GOLD.toString()+ChatColor.BOLD.toString()+a.getName()+"!");
							lore.add(ChatColor.DARK_AQUA.toString()+a.getPoints()+" Point"+(a.getPoints()==1?"":"s")+" awarded!");
							for(String s : a.getDescription()){ lore.add(ChatColor.AQUA+s); }
							meta.setLore(lore);
							it.setItemMeta(meta);
							i.addItem(it);
						}
						for(Achievement a : Achievement.values()){
							if(tut!=a.isTutorial())continue;
							if(kingdom.hasAchievement(a))continue;
							it=new ItemStack(Material.DIAMOND_ORE);
							meta=it.getItemMeta();
							meta.setDisplayName(ChatColor.GOLD.toString()+ChatColor.BOLD.toString()+a.getName()+"!");
							if(tut){
								lore=new ArrayList<>();
								for(String s : a.getDescription()){ lore.add(ChatColor.AQUA+s); }
								meta.setLore(lore);
							}
							it.setItemMeta(meta);
							i.addItem(it);
						}
						p.openInventory(i);
					}else if(item.getTypeId()==Material.GOLD_INGOT.getId()){
						final Plot plot = PlotMg.getPlotAt(b.getChunk());
						if(action==Action.RIGHT_CLICK_AIR
								||action==Action.RIGHT_CLICK_BLOCK){
							if(plot.isKingdomPlot()){
								if(plot.getKingdom()==kingdom){
									if(plot.getBuilding().getType()==BuildingType.TAVERN){
										kingdom.awardAchievement(Achievement.HENCHMEN_FOR_HIRE);
										final Tavern t = (Tavern)plot.getBuilding();
										if(t.getHenchmen()!=null){
											if(henchmenchoosers.contains(p.getName()))return;
											henchmenchoosers.add(p.getName());
											final HenchmenType h = t.getHenchmen();
											final int cost = t.getCost();
											p.sendMessage(ChatColor.GREEN+"Would you like to hire a "+h.getName()+" for "+cost+" gold?");
											h.explain(p);
											p.sendMessage(ChatColor.GREEN+"Type "+ChatColor.GOLD+"YES"+ChatColor.GREEN+" to hire him.");
											p.sendMessage(ChatColor.GREEN+"Type "+ChatColor.GOLD+"NO"+ChatColor.GREEN+" to ignore him.");
											final CallName cn = new CallName(p, -1, null, "Hire Henchman");
											while(true){
												try{ Thread.sleep(50);
												}catch(final Exception exception){}
												if(cn.hasResponed()){
													final String r = cn.getName();
													if(r.equalsIgnoreCase("yes")){
														if(kingdom.hasMoney(cost)){
															kingdom.collectResources(ResourceType.GOLD, cost);
															t.collectHenchmen();
															kingdom.addHenchmen(h);
															p.sendMessage(ChatColor.GREEN+"You have hired the henchman.");
															cn.close();
															henchmenchoosers.remove(p.getName());
															return;
														}else{
															p.sendMessage(ChatColor.RED+"You do not have enough gold to do that!");
															cn.close();
															henchmenchoosers.remove(p.getName());
															return;
														}
													}else if(r.equalsIgnoreCase("no")){
														p.sendMessage(ChatColor.GREEN+"You did not hire the "+h.getName()+".");
														cn.close();
														henchmenchoosers.remove(p.getName());
														return;
													}else{
														p.sendMessage(ChatColor.GREEN+"I'm sorry, I couldn't understand you. What did you say?");
														p.sendMessage(ChatColor.GREEN+"Would you like to hire a "+h.getName()+" for "+cost+" gold?");
														h.explain(p);
														p.sendMessage(ChatColor.GREEN+"Type "+ChatColor.GOLD+"YES"+ChatColor.GREEN+" to hire him.");
														p.sendMessage(ChatColor.GREEN+"Type "+ChatColor.GOLD+"NO"+ChatColor.GREEN+" to ignore him.");
														cn.setResponse(null);
														continue;
													}
												}
												if(!p.isOnline()){
													cn.close();
													henchmenchoosers.remove(p.getName());
													return;
												}
											}
										}else p.sendMessage(ChatColor.RED+"This tavern doesn't have a henchmen in it!");
									}else p.sendMessage(ChatColor.RED+"This building is not a tavern!");
								}else p.sendMessage(ChatColor.RED+"This is not your kingdom!");
							}else p.sendMessage(ChatColor.RED+"There is no kingdom here!");
						}else{
							final Inventory i = Bukkit.createInventory(null, 54, "Choose Henchman");
							final HashMap<HenchmenType,Integer> h = kingdom.getHenchmen();
							if(h.isEmpty()){
								p.sendMessage(ChatColor.RED+"Your kingdom doesn't have any henchmen left!");
								return;
							}
							ItemStack stack;
							ItemMeta meta;
							i.setMaxStackSize(100);
							for(HenchmenType type : h.keySet()){
								if(h.get(type)==0)continue;
								stack=new ItemStack(type.getMaterial(), 1);
								stack.setAmount(h.get(type));
								meta=stack.getItemMeta();
								meta.setDisplayName(type.getName());
								stack.setItemMeta(meta);
								i.addItem(stack);
							}
							p.openInventory(i);
							kingdom.awardAchievement(Achievement.SEND_A_HENCHMAN);
							final InventorySelect inv = new InventorySelect(p, "Choose Henchman", i);
							while(true){
								try{ Thread.sleep(50);
								}catch(final Exception exception){}
								if(inv.hasResponed()){
									p.closeInventory();
									final ItemStack r = inv.getItem();
									final String name = r.getItemMeta().getDisplayName();
									final HenchmenType type = HenchmenType.getByName(name);
									if(type.use(p, plot))kingdom.removeHenchmen(type);
									inv.close();
									return;
								}
								if(i.getViewers().size()==0){
									inv.close();
									return;
								}
							}
						}
					}else if(item.getTypeId()==Material.LEVER.getId()){
						long m = PlayerMg.getMonitors(p.getName());
						final Inventory i = Bukkit.createInventory(null, 18, "Choose Monitors");
						populateInventory(i, m);
						final InventorySelect inv = new InventorySelect(p, "Choose Monitors", i);
						p.openInventory(i);
						kingdom.awardAchievement(Achievement.INFO_HUD_CHANGED);
						ItemStack it;
						String name;
						while(true){
							try{
								Thread.sleep(10);
								if(inv.hasResponed()){
									it=inv.getItem();
									name=ChatColor.stripColor(it.getItemMeta().getDisplayName());
									if(it.getTypeId()==123){
										m+=getItemId(name);
										populateInventory(i, m);
									}else if(it.getTypeId()==124){
										m-=getItemId(name);
										populateInventory(i, m);
									}else if(it.getTypeId()==57){
										PlayerMg.setMonitors(p.getName(), m);
										p.kickPlayer("Please relog for changes to take effect.");
										inv.close();
										return;
									}
									inv.setResponse(null);
								}
								if(i.getViewers().size()==0){
									inv.close();
									return;
								}
							}catch(final Exception exception){ populateInventory(i, m); }
						}
					}else if(item.getTypeId()==Material.BLAZE_ROD.getId()){
						final Army sel = PlayerMg.getSelectedArmy(p.getName());
						if(sel!=null){
							sel.getArmyType().runPower(p, b.getLocation());
							kingdom.awardAchievement(Achievement.BLAZE_POWERS_ACTIVATE);
						}else p.sendMessage(ChatColor.RED+"You do not have an army selected, so you cannot use an army power!");
					}else if(item.getTypeId()==Material.MINECART.getId()){
						final Plot plot = PlotMg.getPlotAt(b.getChunk());
						if(plot.isKingdomPlot()){
							if(plot.getKingdom()==kingdom){
								if(plot.getBuilding().getType()==BuildingType.LAND){
									if(kingdom.hasMoney(50)){
										if(p.getLevel()==0){
											boolean changed;
											if(action==Action.LEFT_CLICK_AIR
													||action==Action.LEFT_CLICK_BLOCK)changed=plot.addHeight(1, true);
											else changed=plot.removeHeight(1, true);
											if(changed){
												if(action==Action.LEFT_CLICK_AIR
														||action==Action.LEFT_CLICK_BLOCK)kingdom.awardAchievement(Achievement.THE_UPS_OF_CONQUEST);
												else kingdom.awardAchievement(Achievement.THE_DOWNS_OF_CONQUEST);
												final int offX = plot.getX();
												final int offZ = plot.getZ();
												int h = plot.getHeight();
												int x3 = 0;
												int x5 = 0;
												Plot p1;
												for(int x = -4; x<=4; x++){
													for(int z = -4; z<=4; z++){
														p1=PlotMg.getPlotAt(plot.getWorld(), x+offX, z+offZ);
														if(p1.isClaimed()
																&&p1.getKingdom()==kingdom
																&&p1.getBuilding() instanceof Land){
															if(p1.getHeight()!=h)continue;
															if(-2<=x&&x<=2
																	&&-2<=z&&z<=2)x3++;
															x5++;
														}
													}
												}
												if(x3>=9)kingdom.awardAchievement(Achievement.LEVEL_A_3X3);
												if(x5>=25)kingdom.awardAchievement(Achievement.LEVEL_A_5X5);
												kingdom.collectResources(ResourceType.GOLD, 50);
												p.setLevel(PlayerMg.CHANGE_HEIGHT);
											}else p.sendMessage(ChatColor.RED+"This area cannot be raised or lowered anymore!");
										}else p.sendMessage(ChatColor.RED+"You must wait until your stamina meter gets back down to zero before you can preform this action!");
									}else p.sendMessage(ChatColor.RED+"Not enough money to do this!");
								}else p.sendMessage(ChatColor.RED+"You can only change the height of land!");
							}else p.sendMessage(ChatColor.RED+"This is not your kingdom!");
						}else p.sendMessage(ChatColor.RED+"You cannot change the height of wilderness chunks!");
					}else if(item.getTypeId()==Material.WEB.getId()){
						p.sendMessage(ChatColor.DARK_AQUA+"Please visit our website at: ");
						p.sendMessage(ChatColor.YELLOW+"wiki.wraithaven.com");
						kingdom.awardAchievement(Achievement.WWWEB);
					}else if(item.getTypeId()==Material.CAKE.getId()){
						kingdom.awardAchievement(Achievement.CAKE_ISNT_A_LIE);
						final Inventory i = Bukkit.createInventory(null, 54, "Help Catagories");
						ItemStack it;
						ItemMeta meta;
						synchronized(WhCommunity.helptool){
							for(String s : WhCommunity.helptool.keySet()){
								it=new ItemStack(Material.PAPER);
								meta=it.getItemMeta();
								meta.setDisplayName(ChatColor.DARK_PURPLE+s);
								it.setItemMeta(meta);
								i.addItem(it);
							}
						}
						final InventorySelect inv = new InventorySelect(p, "Help Catagories", i);
						p.openInventory(i);
						while(true){
							try{ Thread.sleep(50);
							}catch(final Exception exception){}
							if(inv.hasResponed()){
								inv.close();
								final String catagory = ChatColor.stripColor(inv.getItem().getItemMeta().getDisplayName());
								final Inventory i2 = Bukkit.createInventory(null, 54, "Help Videos");
								synchronized(WhCommunity.helptool){
									for(String s : WhCommunity.helptool.get(catagory).keySet()){
										it=new ItemStack(Material.PAPER);
										meta=it.getItemMeta();
										meta.setDisplayName(ChatColor.DARK_PURPLE+s);
										it.setItemMeta(meta);
										i2.addItem(it);
									}
								}
								final InventorySelect inv2 = new InventorySelect(p, "Help Videos", i2);
								p.openInventory(i2);
								while(true){
									try{ Thread.sleep(50);
									}catch(final Exception exception){}
									if(inv2.hasResponed()){
										final String link = ChatColor.stripColor(inv2.getItem().getItemMeta().getDisplayName());
										p.closeInventory();
										p.sendMessage(ChatColor.GRAY+"Here is a link to the requested help video.");
										p.sendMessage(ChatColor.GREEN+WhCommunity.helptool.get(catagory).get(link));
										inv2.close();
										return;
									}
									if(i2.getViewers().size()==0){
										inv2.close();
										return;
									}
								}
							}
							if(i.getViewers().size()==0){
								inv.close();
								return;
							}
						}
					}else if(item.getTypeId()==Material.GOLD_NUGGET.getId()){
						kingdom.awardAchievement(Achievement.BUTTER_NUGGET);
						final int dp = PlayerMg.getDonatorPoints(p.getName());
						final Inventory i = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE+"You have "+ChatColor.LIGHT_PURPLE+dp+ChatColor.DARK_PURPLE+" Donator Points");
						ItemStack it;
						ItemMeta meta;
						ArrayList<String> lore;
						final DF[] dfs = DF.values();
						for(int x = 0; x<DF.values().length; x++){
							it=new ItemStack(Material.PAPER);
							meta=it.getItemMeta();
							lore=new ArrayList<>();
							meta.setDisplayName(ChatColor.YELLOW+dfs[x].getName());
							if(dfs[x].getCost()>=dp)lore.add(ChatColor.GREEN+"Cost: "+dfs[x].getCost()+" DP");
							else lore.add(ChatColor.RED+"Cost: "+dfs[x].getCost()+" DP");
							lore.add(ChatColor.LIGHT_PURPLE+"Times Purchased: "+dfs[x].getBuyCount(p.getName()));
							lore.add(ChatColor.GRAY.toString()+ChatColor.ITALIC+dfs[x].getDescription());
							meta.setLore(lore);
							it.setItemMeta(meta);
						}
						final InventorySelect inv = new InventorySelect(p, "DP", i);
						p.openInventory(i);
						while(true){
							try{ Thread.sleep(50);
							}catch(final Exception exception){}
							if(inv.hasResponed()){
								p.closeInventory();
								inv.close();
								final DF df = DF.getById(inv.getSlot());
								if(!df.isRebuyable()
										&&df.hasBoughtBefore(p.getName())){
									p.sendMessage(ChatColor.RED+"You have already bought this donator feature!");
									return;
								}
								if(df.getCost()>dp){
									p.sendMessage(ChatColor.RED+"You don't have enough donator points to buy this feature!");
									return;
								}
								if(df.canBuy(p)){
									p.sendMessage(ChatColor.GREEN+"Are you sure you wish to buy the \""+df.getName()+"\" donator feature for "+df.getCost()+" donator points?");
									p.sendMessage(ChatColor.GREEN+"Type YES to confirm.");
									p.sendMessage(ChatColor.GREEN+"Type anything else to deny.");
									final CallName cn = new CallName(p, 0, null, "Confirm DF");
									while(true){
										try{ Thread.sleep(50);
										}catch(final Exception exception){}
										if(cn.hasResponed()){
											if(cn.getName().equalsIgnoreCase("yes")){
												cn.close();
												break;
											}else{
												p.sendMessage(ChatColor.RED+"Donator feature request denied.");
												cn.close();
												return;
											}
										}
									}
									PlayerMg.removeDonatorPoints(p.getName(), df.getCost());
									p.sendMessage(ChatColor.GREEN+"You have bought: "+df.getName());
									p.sendMessage(ChatColor.GREEN+"You now have "+PlayerMg.getDonatorPoints(p.getName())+" donator points.");
									df.buy(p);
								}
								return;
							}
							if(i.getViewers().size()==0){
								inv.close();
								return;
							}
						}
					}else if(item.getTypeId()==Material.GOLD_BLOCK.getId()){
						kingdom.awardAchievement(Achievement.DONATE_TOOL);
						p.sendMessage(ChatColor.GREEN+"Heres a link to the donate page where you can donate and earn donator points to spend on tons of cool features for your kingdom! Or, where you can increase your server land max and claim more land!");
						p.sendMessage(ChatColor.YELLOW+"http://82x78ni7zhoyn5p.buycraft.net/category/5730");
					}else if(item.getTypeId()==Material.FIREWORK.getId()){
						kingdom.awardAchievement(Achievement.WHO_AM_I);
						p.sendMessage(ChatColor.DARK_GRAY+"===================");
						p.sendMessage(ChatColor.GRAY+"~~~{ "+ChatColor.AQUA+"Designer"+ChatColor.GRAY+" }~~~");
						p.sendMessage(ChatColor.DARK_AQUA+"- Phealoon");
						p.sendMessage(ChatColor.GRAY+"~~~{ "+ChatColor.AQUA+"Concept Assist"+ChatColor.GRAY+" }~~~");
						p.sendMessage(ChatColor.DARK_AQUA+"- TheDudeFromCI");
						p.sendMessage(ChatColor.GRAY+"~~~{ "+ChatColor.AQUA+"Programmer"+ChatColor.GRAY+" }~~~");
						p.sendMessage(ChatColor.DARK_AQUA+"- TheDudeFromCI");
						p.sendMessage(ChatColor.GRAY+"~~~{ "+ChatColor.AQUA+"Head Builders"+ChatColor.GRAY+" }~~~");
						p.sendMessage(ChatColor.DARK_AQUA+"- Phealooon");
						p.sendMessage(ChatColor.DARK_AQUA+"- TheDogWithAFro");
						p.sendMessage(ChatColor.DARK_AQUA+"- Merick118");
						p.sendMessage(ChatColor.DARK_AQUA+"- Dark_Lord19191");
						p.sendMessage(ChatColor.DARK_AQUA+"- Zunnik");
						p.sendMessage(ChatColor.GRAY+"~~~{ "+ChatColor.AQUA+"Network Tech"+ChatColor.GRAY+" }~~~");
						p.sendMessage(ChatColor.DARK_AQUA+"- AGN_Ripper");
						p.sendMessage(ChatColor.GRAY+"~~~{ "+ChatColor.AQUA+"Special Thanks"+ChatColor.GRAY+" }~~~");
						p.sendMessage(ChatColor.DARK_AQUA+"- ThePenguino");
						p.sendMessage(ChatColor.DARK_AQUA+"- Bandit1452");
						p.sendMessage(ChatColor.DARK_AQUA+"- MewTwo2");
						p.sendMessage(ChatColor.DARK_AQUA+"- Talkh");
						p.sendMessage(ChatColor.DARK_AQUA+"- Voidbreath");
						p.sendMessage(ChatColor.DARK_AQUA+"- DenimDoodle");
						p.sendMessage(ChatColor.DARK_AQUA+"- LucasJosephKane");
						p.sendMessage(ChatColor.DARK_AQUA+"- Maconorr");
						p.sendMessage(ChatColor.DARK_GRAY+"===================");
					}
				}catch(final Exception exception){ exception.printStackTrace(); }
			}
		}).start();
	}
	private static Chunk getNWCourner(Chunk center, int length){
		length--;
		length/=2;
		return center.getWorld().getChunkAt(center.getX()-length, center.getZ()-length);
	}
	private static void populateInventory(final Inventory i, final long m){
		i.clear();
		for(String s : getMonitors().keySet()){ i.addItem(makeItem(s, (((m&getItemId(s))==getItemId(s))?124:123))); }
		i.setItem(17, makeItem("Apply Changes", 57));
	}
	private static ItemStack makeItem(final String name, final int id){
		final ItemStack item = new ItemStack(id);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW+name);
		item.setItemMeta(meta);
		return item;
	}
	public static HashMap<String,Long> getMonitors(){
		if(monitors!=null)return monitors;
		monitors=new HashMap<>();
		monitors.put("Days", 1L);
		monitors.put("Next Day", 2L);
		monitors.put("Wood", 4L);
		monitors.put("Gold", 8L);
		monitors.put("Stone", 16L);
		monitors.put("Arms", 32L);
		monitors.put("Iron", 64L);
		monitors.put("Food", 128L);
		monitors.put("Population", 256L);
		monitors.put("Troops", 512L);
		monitors.put("Idle Villagers", 1024L);
		monitors.put("Land Owned", 2048L);
		monitors.put("Messages", 4096L);
		monitors.put("Gold/Day", 8192L);
		monitors.put("Villagers/Day", 16384L);
		return monitors;
	}
	private static ItemStack makeItem(final String name, final int id, final String... lore){
		final ItemStack item = new ItemStack(id);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}
	private static String makeBarGraph(final int current, final int max){
		String bar = ChatColor.GREEN.toString();
		final int percent = (int)((double)current/max*100)/5;
		for(int i = 0; i<20; i++){
			if(i==percent)bar+=ChatColor.RED;
			bar+="=";
		}
		return bar;
	}
	private static Tool[] orderItemList(final ArrayList<Tool> tools){
		final Tool[] items = new Tool[27];
		final ArrayList<Tool> copy = new ArrayList<>(tools);
		for(int i = 26; i>=0; i--){
			items[i]=getNextTool(copy);
			copy.remove(items[i]);
		}
		return items;
	}
	private static Tool getNextTool(final ArrayList<Tool> tools){
		Tool t = null;
		int p = 100;
		for(Tool tool : tools){
			if(tool.getPosition()<p){
				p=tool.getPosition();
				t=tool;
			}
		}
		return t;
	}
	private static Tool getToolFromItem(final ArrayList<Tool> tools, final ItemStack item){
		for(Tool t : tools){
			t.updateName();
			try{ if(ChatColor.stripColor(t.getName()).equalsIgnoreCase(ChatColor.stripColor(item.getItemMeta().getDisplayName())))return t;
			}catch(final Exception exception){}
		}
		return null;
	}
	private static int chooseVillagers(final Player p, final int total, final int max){
		int toput = 0;
		p.sendMessage(ChatColor.GREEN+"How many villagers do you want to put in this building? Available: "+total+". Max: "+max);
		final CallName cn = new CallName(p, -1, null, "Moving Villagers");
		int loops = 0;
		while(true){
			loops++;
			if(loops>=200
					||!p.isOnline()){
				cn.close();
				return -1;
			}
			try{ Thread.sleep(50);
			}catch(Exception exception){}
			if(cn.hasResponed()){
				String name = cn.getName();
				try{
					int num = Integer.valueOf(name);
					if(num<=total){
						toput=num;
						cn.close();
						break;
					}else p.sendMessage(ChatColor.RED+"You kingdom does not have this many idling villagers!");
				}catch(NumberFormatException exception1){ p.sendMessage(ChatColor.RED+"That is not a number!"); }
				cn.close();
				return -1;
			}
		}
		return toput;
	}
	private static void redrawInventory(final Inventory i, final int catagory){
		addCatagories(i);
		final PriorityList<Kingdom> kingdoms = new PriorityList<>();
		final HashMap<Kingdom,Integer> ranks = new HashMap<>();
		int r;
		synchronized(Building.buildings){
			for(Kingdom kingdom : KingdomMg.getKingdoms()){
				if(kingdom.isRuins())continue;
				r=kingdom.getRank(catagory);
				kingdoms.add(kingdom, r);
				ranks.put(kingdom, r);
			}
		}
		Kingdom k;
		ItemStack item;
		for(int x = 0; x<36; x++){
			k=kingdoms.getMostImportant();
			if(k==null)break;
			kingdoms.remove(k);
			item=new ItemStack(Material.PAPER);
			setStats(item,
					ChatColor.YELLOW+"#"+(x+1)+") "+k.getName(),
					ChatColor.GRAY+"Owned by "+k.getOwner(),
					ChatColor.LIGHT_PURPLE+"Score: "+NumberFormat.getInstance().format(ranks.get(k)));
			i.addItem(item);
		}
	}
	private static void addCatagories(final Inventory i){
		final ItemStack power = new ItemStack(Material.DIAMOND_SWORD);
		final ItemStack achievements = new ItemStack(Material.DIAMOND);
		final ItemStack wealth = new ItemStack(Material.GOLD_INGOT);
		final ItemStack estate = new ItemStack(Material.GRASS);
		final ItemStack defenses = new ItemStack(Material.COBBLE_WALL);
		final ItemStack technology = new ItemStack(Material.NETHER_STAR);
		final ItemStack resources = new ItemStack(Material.IRON_SPADE);
		final ItemStack population = new ItemStack(Material.BED);
		final ItemStack total = new ItemStack(Material.FIREWORK);
		setStats(power, ChatColor.RED+"Kingdom Power", ChatColor.GRAY+"The power of the kingdom's army.");
		setStats(achievements, ChatColor.RED+"Kingdom Achievements", ChatColor.GRAY+"The achievement points this kingdom has earned.");
		setStats(wealth, ChatColor.RED+"Kingdom Wealth", ChatColor.GRAY+"The kingdom's wealth per villager.");
		setStats(estate, ChatColor.RED+"Kingdom Estate", ChatColor.GRAY+"The kingdom's size in land.");
		setStats(defenses, ChatColor.RED+"Kingdom Defenses", ChatColor.GRAY+"The Defense level of the kingdom.");
		setStats(technology, ChatColor.RED+"Kingdom Technology", ChatColor.GRAY+"The kingdom's technology level.");
		setStats(resources, ChatColor.RED+"Kingdom Resources", ChatColor.GRAY+"The amount of resources this kingdom has.");
		setStats(population, ChatColor.RED+"Kingdom Population", ChatColor.GRAY+"The kingdom's population based on cottages.");
		setStats(total, ChatColor.RED+"Kingdom Total Score", ChatColor.GRAY+"The adverage score of this kingdom.");
		i.setItem(45, power);
		i.setItem(46, achievements);
		i.setItem(47, wealth);
		i.setItem(48, estate);
		i.setItem(49, defenses);
		i.setItem(50, technology);
		i.setItem(51, resources);
		i.setItem(52, population);
		i.setItem(53, total);
	}
	private static void setStats(final ItemStack item, final String name, final String... des){
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		final ArrayList<String> lore = new ArrayList<>();
		for(String s : des)lore.add(s);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	private static String makeJobPercent(final int a, final int b){ return ChatColor.DARK_AQUA+"("+(int)((double)a/b*100)+"%)"+ChatColor.DARK_GRAY+"{"+makeBarGraph(a, b)+ChatColor.DARK_GRAY+"}"+ChatColor.DARK_AQUA+"("+ChatColor.AQUA.toString()+a+"/"+b+ChatColor.DARK_AQUA+")"; }
	private static long getItemId(final String name){ return getMonitors().get(name); }
}
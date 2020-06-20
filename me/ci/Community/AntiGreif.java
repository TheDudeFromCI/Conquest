package me.ci.Community;

import java.util.HashMap;
import java.util.Map;

import me.ci.WhCommunity;
import me.ci.Conquest.Buildings.Constructors.Flag;
import me.ci.Conquest.KingdomManagment.KingdomCommand;
import me.ci.Conquest.KingdomManagment.KingdomMaker;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import me.ci.Conquest.Misc.ToolBarActions;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class AntiGreif{
	public static Map<String,Integer> channels = new HashMap<String,Integer>();
	public static void e(PlayerBucketEmptyEvent e){
		Player p = e.getPlayer();
		Block bc = e.getBlockClicked();
		BlockFace bf = e.getBlockFace();
		Location l = bc.getLocation();
		if(bf.equals(BlockFace.UP))l.add(0, 1, 0);
		else if(bf.equals(BlockFace.DOWN))l.add(0, -1, 0);
		else if(bf.equals(BlockFace.EAST))l.add(1, 0, 0);
		else if(bf.equals(BlockFace.WEST))l.add(-1, 0, 0);
		else if(bf.equals(BlockFace.SOUTH))l.add(0, 0, 1);
		else if(bf.equals(BlockFace.NORTH))l.add(0, 0, -1);
		if(!PlayerMg.hasBuildPermission(l, p)){
			e.setCancelled(true);
			yellAtPlayer(l, p);
		}
	}
	public static void yellAtPlayer(Location l, Player p){
		p.sendMessage(ChatColor.RED+"No permission for area!");
		try{
			l.getWorld().playEffect(l, Effect.MOBSPAWNER_FLAMES, 0);
			l.getWorld().playEffect(l, Effect.EXTINGUISH, 0);
		}catch(Exception exception){}
	}
	public static void e(PlayerBucketFillEvent e){
		Player p = e.getPlayer();
		Location l = e.getBlockClicked().getLocation();
		if(!PlayerMg.hasBuildPermission(l, p)){
			e.setCancelled(true);
			yellAtPlayer(l, p);
		}
	}
	public static void e(PaintingBreakByEntityEvent e){
		Entity mob = e.getRemover();
		if(mob instanceof Player){
			Player p = (Player)mob;
			Location l = e.getPainting().getLocation();
			if(!PlayerMg.hasBuildPermission(l, p)){
				e.setCancelled(true);
				yellAtPlayer(l, p);
			}
		}else e.setCancelled(true);
	}
	public static void e(PaintingBreakEvent e){
		RemoveCause reason = e.getCause();
		WorldSettings settings = WhCommunity.getWorldSettings(e.getPainting().getWorld().getName());
		if(reason.equals(RemoveCause.FIRE)
				&&!settings.CONFIG_Fire_Spread)e.setCancelled(true);
	}
	public static void e(PaintingPlaceEvent e){
		Player p = e.getPlayer();
		Location l = e.getPainting().getLocation();
		if(!PlayerMg.hasBuildPermission(l, p)){
			e.setCancelled(true);
			yellAtPlayer(l, p);
		}
	}
	public static void e(BlockBreakEvent e){
		Player p = e.getPlayer();
		Location l = e.getBlock().getLocation();
		if(!PlayerMg.hasBuildPermission(l, p)){
			e.setCancelled(true);
			yellAtPlayer(l, p);
		}
	}
	public static void e(BlockPlaceEvent e){
		final Player p = e.getPlayer();
		final Block b = e.getBlock();
		final Location l = b.getLocation();
		if(!PlayerMg.hasBuildPermission(l, p)){
			e.setCancelled(true);
			yellAtPlayer(l, p);
		}else{
			if(p.hasPermission("whc.cmd.kingdom.savechunk")
					&&KingdomCommand.flagsPlanted.containsKey(p.getName())
					&&KingdomCommand.flagsPlanted.get(p.getName())==null){
				ItemStack item = p.getItemInHand();
				if(item!=null&&item.getTypeId()==Material.FENCE.getId()){
					KingdomCommand.flagsPlanted.put(p.getName(), l);
					e.setCancelled(true);
				}
			}
		}
	}
	public static void e(EntityBreakDoorEvent e){
		e.setCancelled(true);
	}
	public static void e(EntityChangeBlockEvent e){
		e.setCancelled(true);
	}
	public static void e(BlockBurnEvent e){
		Location l = e.getBlock().getLocation();
		WorldSettings settings = WhCommunity.getWorldSettings(l.getWorld().getName());
		if(!settings.CONFIG_Fire_Spread)e.setCancelled(true);
	}
	public static void e(BlockIgniteEvent e){
		IgniteCause reason = e.getCause();
		Location l = e.getBlock().getLocation();
		WorldSettings settings = WhCommunity.getWorldSettings(l.getWorld().getName());
		if(reason.equals(IgniteCause.FLINT_AND_STEEL)){
			Player p = e.getPlayer();
			if(!PlayerMg.hasBuildPermission(l, p)){
				e.setCancelled(true);
				yellAtPlayer(l, p);
			}
		}else if(!settings.CONFIG_Fire_Spread)e.setCancelled(true);
	}
	public static void e(BlockFromToEvent e){
		Plot p1 = PlotMg.getPlotAt(e.getBlock().getChunk());
		Plot p2 = PlotMg.getPlotAt(e.getToBlock().getChunk());
		WorldSettings settings = WhCommunity.getWorldSettings(e.getBlock().getWorld().getName());
		if(settings.CONFIG_Plot_Barrier_Liquid_Flow){
			if(p1.getX()!=p2.getX()
					||p1.getZ()!=p2.getZ()
					&&p2.isClaimed()){
				if(p1.isClaimed()&&!p1.getOwner().equals(p2.getOwner()))return;
				else e.setCancelled(true);
			}
		}
	}
	public static void e(EntityExplodeEvent e){
		WorldSettings settings = WhCommunity.getWorldSettings(e.getLocation().getWorld().getName());
		if(!settings.CONFIG_Explosions)e.blockList().clear();
	}
	public static void e(CreatureSpawnEvent e){
        if(e.isCancelled())return;
		SpawnReason reason = e.getSpawnReason();
        EntityType mob = e.getEntityType();
		WorldSettings settings = WhCommunity.getWorldSettings(e.getLocation().getWorld().getName());
		if(reason.equals(SpawnReason.CUSTOM)){
			if(!settings.CONFIG_Spawn_Mobs_Plugin)e.setCancelled(true);
		}else{
			boolean hostile = true;
			if(mob.equals(EntityType.BAT))hostile=false;
			else if(mob.equals(EntityType.CHICKEN))hostile=false;
			else if(mob.equals(EntityType.COW))hostile=false;
			else if(mob.equals(EntityType.IRON_GOLEM))hostile=false;
			else if(mob.equals(EntityType.MUSHROOM_COW))hostile=false;
			else if(mob.equals(EntityType.OCELOT))hostile=false;
			else if(mob.equals(EntityType.PIG))hostile=false;
			else if(mob.equals(EntityType.SHEEP))hostile=false;
			else if(mob.equals(EntityType.SNOWMAN))hostile=false;
			else if(mob.equals(EntityType.SQUID))hostile=false;
			else if(mob.equals(EntityType.VILLAGER))hostile=false;
			else if(mob.equals(EntityType.WOLF))hostile=false;
			if(hostile){
				if(!settings.CONFIG_Spawn_Mobs_Naturally_Hostile)e.setCancelled(true);
			}else if(!settings.CONFIG_Spawn_Mobs_Naturally_Peaceful)e.setCancelled(true);
		}
	}
	public static void e(PlayerInteractEvent e){
		try{
			final Player p = e.getPlayer();
			final Block b = e.getClickedBlock();
			Action a = e.getAction();
			if(b!=null){
				Location l = b.getLocation();
				int bid = b.getTypeId();
				if(!PlayerMg.hasBuildPermission(l, p)){
					if(!WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Conquest_Enabled
							&&bid==54
							||bid==84
							||bid==92
							||bid==120
							||bid==138
							||bid==145
							||bid==26
							||bid==96
							||bid==60
							||bid==23
							||bid==25
							||bid==107
							||bid==61
							||bid==122){
						e.setCancelled(true);
						yellAtPlayer(l, p);
					}else{
						ItemStack item = p.getItemInHand();
						if(item!=null&&item.getTypeId()==Material.MONSTER_EGGS.getId()){
							e.setCancelled(true);
							yellAtPlayer(l, p);
						}
					}
				}else{
					WorldSettings settings = WhCommunity.getWorldSettings(p.getWorld().getName());
					ItemStack item = p.getItemInHand();
					if(item!=null){
						if(item.getTypeId()==Material.EYE_OF_ENDER.getId()){
							if(PlayerMg.hasPermission(p, "whc.action.eyeofender")){
								if(a.equals(Action.LEFT_CLICK_BLOCK)){
									if(AntiGreif.channels.containsKey(p.getName())){
										int id = AntiGreif.channels.get(p.getName());
										Channel c = PlayerMg.getChannel(p); 
										if(c.getId()!=id){
											if(id<=settings.CONFIG_Chat_Channels_Limit){
												if(PlayerMg.hasPermission(p, "whc.chat.join."+p.getWorld().getName()+".*")
														||PlayerMg.hasPermission(p, "whc.chat.join."+p.getWorld().getName()+"."+id)){
													c.leaveChannel(p);
													Channel newc = settings.getChannelById(id); 
													newc.joinChannel(p);
													AntiGreif.channels.remove(p.getName());
													p.sendMessage(ChatColor.DARK_AQUA+"You have joined channel: "+newc);
												}else p.sendMessage(ChatColor.RED+"You do not have permission to join this chat channel!");
											}else p.sendMessage(ChatColor.RED+"This channel does not exist for this world!");
										}else p.sendMessage(ChatColor.RED+"You are already in this channel!");
									}else p.sendMessage(ChatColor.RED+"You have not selected a channel yet, so you cannot join one! Right click to scroll through channels and then pick one by left clicking.");
								}else if(a.equals(Action.RIGHT_CLICK_BLOCK)){
									if(settings.CONFIG_Chat_Channels_Limit==0)p.sendMessage(ChatColor.RED+"There are no chat channels for this world!");
									else{
										int c = -1;
										if(AntiGreif.channels.containsKey(p.getName()))c=AntiGreif.channels.get(p.getName());
										c++;
										c%=settings.CONFIG_Chat_Channels_Limit;
										AntiGreif.channels.put(p.getName(), c);
										Channel channel = settings.getChannelById(c);
										p.sendMessage(ChatColor.DARK_AQUA+"Left click to join channel "+ChatColor.AQUA+channel+ChatColor.DARK_AQUA+".");
									}
								}
								e.setCancelled(true);
							}
						}
					}
				}
				ItemStack item = p.getItemInHand();
				if(item!=null){
					if(item.getTypeId()==Material.INK_SACK.getId()){
						if(KingdomMaker.colorflag.containsKey(p.getName())){
							Byte[] flaginfo = KingdomMaker.colorflag.get(p.getName());
							Block flagbase = KingdomMg.getCreateFlagLocation(b.getWorld(), flaginfo[0]).getBlock();
							final byte data = getWoolColor(item.getData().getData());
							if(Flag.isPartOfWool(flagbase, b, flaginfo[0])){
								Flag.addColor(flaginfo, flagbase, b, data);
								e.setCancelled(true);
								new Thread(new Runnable(){
									public void run(){
										try{
											Thread.sleep(5);
										}catch(Exception exception){}
										p.sendBlockChange(b.getLocation(), Material.WOOL, data);
									}
								}).start();
							}
						}
					}else if(item.getTypeId()==Material.LAVA_BUCKET.getId()){
						if(KingdomMaker.colorflag.containsKey(p.getName())){
							final Byte[] flaginfo = KingdomMaker.colorflag.get(p.getName());
							final Block flagbase = KingdomMg.getCreateFlagLocation(b.getWorld(), flaginfo[0]).getBlock();
							if(Flag.isPartOfWool(flagbase, b, flaginfo[0])){
								for(int i = 1; i<flaginfo.length; i++){
									flaginfo[i]=(byte)(Math.random()*16);
								}
								e.setCancelled(true);
								new Thread(new Runnable(){
									public void run(){
										try{
											Thread.sleep(5);
										}catch(Exception exception){}
										for(int i = 1; i<flaginfo.length; i++){
											p.sendBlockChange(Flag.getBlockAt(flaginfo, flagbase, i).getLocation(), Material.WOOL, flaginfo[i]);
										}
									}
								}).start();
							}
						}
					}else if(item.getTypeId()==Material.WATER_BUCKET.getId()){
						if(KingdomMaker.colorflag.containsKey(p.getName())){
							final Byte[] flaginfo = KingdomMaker.colorflag.get(p.getName());
							final Block flagbase = KingdomMg.getCreateFlagLocation(b.getWorld(), flaginfo[0]).getBlock();
							if(Flag.isPartOfWool(flagbase, b, flaginfo[0])){
								e.setCancelled(true);
								new Thread(new Runnable(){
									public void run(){
										try{
											Thread.sleep(5);
										}catch(Exception exception){}
										for(int i = 1; i<flaginfo.length; i++){
											p.sendBlockChange(Flag.getBlockAt(flaginfo, flagbase, i).getLocation(), Material.WOOL, (byte)0);
										}
									}
								}).start();
							}
						}
					}
				}
				if(KingdomMaker.chooseflag.containsKey(p.getName())){
					byte id = (byte)-1;
					if(Flag.isPartOfFlag(KingdomMg.getCreateFlagLocation(b.getWorld(), (byte)0).getBlock(), b, (byte)0))id=(byte)0;
					else if(Flag.isPartOfFlag(KingdomMg.getCreateFlagLocation(b.getWorld(), (byte)1).getBlock(), b, (byte)1))id=(byte)1;
					else if(Flag.isPartOfFlag(KingdomMg.getCreateFlagLocation(b.getWorld(), (byte)2).getBlock(), b, (byte)2))id=(byte)2;
					else if(Flag.isPartOfFlag(KingdomMg.getCreateFlagLocation(b.getWorld(), (byte)3).getBlock(), b, (byte)3))id=(byte)3;
					else if(Flag.isPartOfFlag(KingdomMg.getCreateFlagLocation(b.getWorld(), (byte)4).getBlock(), b, (byte)4))id=(byte)4;
					if(id>-1){
						KingdomMaker.chooseflag.put(p.getName(), id);
						e.setCancelled(true);
					}
				}
			}
			if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Conquest_Enabled){
				ToolBarActions.go(p, e.getAction());
				e.setCancelled(true);
			}
		}catch(final Exception exception){ exception.printStackTrace(); }
	}
	private static byte getWoolColor(byte dye){
		if(dye==15)return 0;
		if(dye==7)return 8;
		if(dye==8)return 7;
		if(dye==0)return 15;
		if(dye==3)return 12;
		if(dye==1)return 14;
		if(dye==14)return 1;
		if(dye==11)return 4;
		if(dye==10)return 5;
		if(dye==2)return 13;
		if(dye==6)return 9;
		if(dye==12)return 3;
		if(dye==4)return 11;
		if(dye==5)return 10;
		if(dye==13)return 2;
		if(dye==9)return 6;
		return 15;
	}
	public static void e(EntityDamageByEntityEvent e){
		Entity attacker = e.getDamager();
		Entity victum = e.getEntity();
		if(attacker instanceof Player){
			if(!simulateAttack((Player)attacker, victum))e.setCancelled(true);
		}else if(attacker instanceof Arrow){
			Entity shooter = ((Arrow)attacker).getShooter();
			if(shooter!=null&&shooter instanceof Player){
				if(!simulateAttack((Player)shooter, victum))e.setCancelled(true);
			}
		}
	}
	public static boolean simulateAttack(Player p, Entity e){
		if(e instanceof Player){
			Player v = (Player)e;
			WorldSettings settings = WhCommunity.getWorldSettings(e.getWorld().getName());
			if(settings.CONFIG_Pvp_Global)return true;
			if(PlayerMg.hasPvpEnabled(p)&&PlayerMg.hasPvpEnabled(v))return true;
			else{
				if(PlayerMg.hasPvpEnabled(p))p.sendMessage(ChatColor.RED+"You do not have Pvp turned on, you cannot attack that player!");
				else p.sendMessage(ChatColor.RED+"That player does not have Pvp turned on, you cannot attack them!");
				return false;
			}
		}else{
			EntityType type = e.getType();
			boolean hostile = true;
			if(type.equals(EntityType.BAT))hostile=false;
			else if(type.equals(EntityType.CHICKEN))hostile=false;
			else if(type.equals(EntityType.COW))hostile=false;
			else if(type.equals(EntityType.IRON_GOLEM))hostile=false;
			else if(type.equals(EntityType.MUSHROOM_COW))hostile=false;
			else if(type.equals(EntityType.OCELOT))hostile=false;
			else if(type.equals(EntityType.PIG))hostile=false;
			else if(type.equals(EntityType.SHEEP))hostile=false;
			else if(type.equals(EntityType.SNOWMAN))hostile=false;
			else if(type.equals(EntityType.SQUID))hostile=false;
			else if(type.equals(EntityType.VILLAGER))hostile=false;
			else if(type.equals(EntityType.WOLF))hostile=false;
			if(!hostile){
				if(PlayerMg.hasBuildPermission(e.getLocation(), p))return true;
				else{
					p.sendMessage(ChatColor.RED+"You cannot attack peaceful mobs in areas you do not have permission for!");
					return false;
				}
			}
		}
		return true;
	}
	public static void e(BlockPhysicsEvent e){
		WorldSettings settings = WhCommunity.getWorldSettings(e.getBlock().getWorld().getName());
		if(settings.CONFIG_Conquest_Enabled)e.setCancelled(true);
	}
	public static void e(EntityDamageEvent e){
		Entity mob = e.getEntity();
		if(mob instanceof Player){
			Player p = (Player)mob;
			if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Conquest_Enabled)e.setCancelled(true);
		}
	}
}
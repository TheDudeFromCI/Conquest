package me.ci.Conquest.KingdomManagment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.ci.WhCommunity;
import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Community.Save;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Textures.ConquestTextures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingdomCommand implements CommandExecutor{
	public static Map<String,Location> flagsPlanted = new HashMap<String,Location>();
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length==0){
			sender.sendMessage(ChatColor.DARK_GRAY+"<]--------{ Menu }--------[>");
			sender.sendMessage(ChatColor.GOLD+"/kingdom regen "+ChatColor.GRAY+"Regenerates the entire world.");
			sender.sendMessage(ChatColor.GOLD+"/kingdom delete [kingdom] "+ChatColor.GRAY+"Deletes a kingdom.");
			sender.sendMessage(ChatColor.GOLD+"/kingdom ban [player] "+ChatColor.GRAY+"Bans this player, their ip, and deletes all kingdoms under this ip.");
			sender.sendMessage(ChatColor.GOLD+"/kingdom landmax [player] "+ChatColor.GRAY+"Increases the land max of this player.");
			sender.sendMessage(ChatColor.GOLD+"/kingdom savechunk [buildingtype] [level] "+ChatColor.GRAY+"Saves a chunk to the conquest texture pack.");
			sender.sendMessage(ChatColor.GOLD+"/kingdom donate [player] [amount] "+ChatColor.GRAY+"Gives donator points to a player.");
			sender.sendMessage(ChatColor.DARK_GRAY+"<]------------------------[>");
		}else if(args.length==1){
			if(args[0].equalsIgnoreCase("regen")){
				if(sender.hasPermission("whc.cmd.kingdom.regen")){
					new Thread(new Runnable(){
						public void run(){
							WhCommunity.GRAPHICS_FIX=true;
							for(World w : Bukkit.getWorlds()){
								if(WhCommunity.getWorldSettings(w.getName()).CONFIG_Conquest_Enabled){
									final ArrayList<Chunk> chunks = getAllChunks(w);
									System.out.println("[WhCommunity] Starting update on map: "+w.getName()+". Chunks: "+chunks.size()+".");
									final int total = chunks.size();
									int done = 0;
									int percent = 0;
									int temp;
									Plot plot;
									for(Chunk c : chunks){
										plot=PlotMg.getPlotAt(c);
										if(plot.isClaimed()){
											if(plot.isKingdomPlot())plot.getBuilding().updateGraphics(true);
										}else ConquestTextures.getTexture(BuildingType.WILDERNESS, 1);
										try{ Thread.sleep(5000);
										}catch(final Exception exception){}
										temp=(int)(done/(double)total*100);
										if(temp>percent){
											percent=temp;
											System.out.println("[WhCommunity] Updating map: "+w.getName()+". Progress: "+percent+"%");
										}
									}
								}
							}
							WhCommunity.GRAPHICS_FIX=false;
						}
					}).start();
				}else sender.sendMessage(ChatColor.RED+"No permission to regen the world!");
			}else{
				sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please try:");
				sender.sendMessage(ChatColor.GOLD+"/kingdom");
				sender.sendMessage(ChatColor.GREEN+"for help.");
			}
		}else if(args.length==2){
			if(args[0].equalsIgnoreCase("delete")){
				if(sender.hasPermission("whc.cmd.kingdom.delete.other")){
					Kingdom kingdom = KingdomMg.getKingdom(args[1].replace("_", " "));
					if(kingdom!=null){
						kingdom.deleteKingdom();
						sender.sendMessage(ChatColor.YELLOW+"This kingdom has been deleted.");
					}else sender.sendMessage(ChatColor.RED+"Kingdom not found!");
				}else sender.sendMessage(ChatColor.RED+"No permission to delete other kingdoms!");
			}else if(args[0].equalsIgnoreCase("ban")){
				if(sender.hasPermission("whc.cmd.kingdom.ban")){
					OfflinePlayer p = Bukkit.getPlayer(args[1]);
					if(p==null)p=Bukkit.getOfflinePlayer(args[1]);
					if(p!=null){
						final String ip = Save.get("Players", p.getName(), "IP");
						if(p.isOnline())((Player)p).kickPlayer("You have been banned from Conquest. Game Over.");
						Bukkit.banIP(ip);
						Kingdom k;
						for(OfflinePlayer off : Bukkit.getOfflinePlayers()){
							if(!Save.get("Players", off.getName(), "IP").equals(ip))continue;
							if(off.isOnline())((Player)off).kickPlayer("You have been banned from Conquest. Game Over.");
							off.setBanned(true);
							k=PlayerMg.getKingdom(off.getName());
							if(k==null)continue;
							k.deleteKingdom();
						}
						sender.sendMessage(ChatColor.GREEN+"Ip banned, and kingdoms deleted.");
					}else sender.sendMessage(ChatColor.RED+"Player not found!");
				}else sender.sendMessage(ChatColor.RED+"No permission to delete other kingdoms!");
			}else if(args[0].equalsIgnoreCase("landmax")){
				if(sender.hasPermission("whc.cmd.kingdom.landmax")){
					PlayerMg.increasedLandMax(args[1], 150, false);
					Bukkit.broadcastMessage(ChatColor.GRAY+args[1]+ChatColor.DARK_AQUA+" has donated and increased their land max!");
				}else sender.sendMessage(ChatColor.RED+"You do not have permission to give out donator points!");
			}else{
				sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please try:");
				sender.sendMessage(ChatColor.GOLD+"/kingdom");
				sender.sendMessage(ChatColor.GREEN+"for help.");
			}
		}else if(args.length==3){
			if(args[0].equalsIgnoreCase("savechunk")){
				if(sender instanceof Player){
					final Player p = (Player)sender;
					if(p.hasPermission("whc.cmd.kingdom.savechunk")){
						final BuildingType type = BuildingType.getByName(args[1]);
						if(type!=null){
							final int lvl;
							try{
								lvl=Integer.valueOf(args[2]);
								if(lvl<=type.getGraphicLevels().length){
									if(lvl>0){
										final Chunk nw = getNWCourner(p.getLocation().getChunk(), type.getLength());
										new Thread(new Runnable(){
											public void run(){
												p.sendMessage(ChatColor.DARK_AQUA+"Starting save for a level "+lvl+" "+type.getName()+".");
												ConquestTextures.save(nw, type, lvl);
												p.sendMessage(ChatColor.DARK_AQUA+"Building saved for a level "+lvl+" "+type.getName()+".");
											}
										}).start();
									}else p.sendMessage(ChatColor.RED+"Buildings cannot have a level lower then 1!");
								}else p.sendMessage(ChatColor.RED+"That building does not use a level that high!");
							}catch(Exception exception){
								p.sendMessage(ChatColor.RED+"Thats not a number!");
							}
						}else p.sendMessage(ChatColor.RED+"Building type not found!");
					}else p.sendMessage(ChatColor.RED+"You do not have permission to save chunks to the conquest texture pack!");
				}else sender.sendMessage(ChatColor.RED+"You must be a player to preform this command!");
			}else if(args[0].equalsIgnoreCase("donate")){
				if(sender.hasPermission("whc.cmd.kingdom.donate")){
					PlayerMg.addDonatorPoints(args[1], (int)(Double.valueOf(args[2])*100));
					Bukkit.broadcastMessage(ChatColor.GRAY+args[1]+ChatColor.DARK_AQUA+" has donated and recived "+ChatColor.AQUA+args[2]+ChatColor.DARK_AQUA+" donator points!");
				}else sender.sendMessage(ChatColor.RED+"You do not have permission to give out donator points!");
			}else{
				sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please try:");
				sender.sendMessage(ChatColor.GOLD+"/kingdom");
				sender.sendMessage(ChatColor.GREEN+"for help.");
			}
		}else{
			sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please try:");
			sender.sendMessage(ChatColor.GOLD+"/kingdom");
			sender.sendMessage(ChatColor.GREEN+"for help.");
		}
		return false;
	}
	private Chunk getNWCourner(Chunk center, int length){
		length--;
		length/=2;
		return center.getWorld().getChunkAt(center.getX()-length, center.getZ()-length);
	}
	private static ArrayList<Chunk> getAllChunks(final World world){
		final ArrayList<Chunk> c1 = new ArrayList<>();
		Bukkit.getScheduler().runTask(WhCommunity.plugin, new Runnable(){
			public void run(){
				final ArrayList<Chunk> chunks = new ArrayList<>();
				final Pattern regionPattern = Pattern.compile("r\\.([0-9-]+)\\.([0-9-]+)\\.mca");
				final File worldDir = new File(Bukkit.getWorldContainer(), world.getName());
				final File regionDir = new File(worldDir, "region");
				File[] regionFiles = regionDir.listFiles(new FilenameFilter(){
					@Override
					public boolean accept(final File dir, final String name){ return regionPattern.matcher(name).matches(); }
				});
				for(File f : regionFiles){
					final Matcher matcher = regionPattern.matcher(f.getName());
					if(!matcher.matches())continue;
					final int mcaX = Integer.parseInt(matcher.group(1));
					final int mcaZ = Integer.parseInt(matcher.group(2));
					for(int cx = 0; cx < 32; cx++){
						for(int cz = 0; cz < 32; cz++){
							chunks.add(world.getChunkAt((mcaX << 5)+cx, (mcaZ<<5)+cz));
						}
					}
				}
				synchronized(c1){ for(Chunk c2 : chunks)c1.add(c2); }
			}
		});
		while(c1.isEmpty()){
			try{ Thread.sleep(50);
			}catch(final Exception exception){}
		}
		return c1;
	}
}
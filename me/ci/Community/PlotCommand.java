package me.ci.Community;

import me.ci.WhCommunity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		try{
			if(args.length==0){
				sender.sendMessage(ChatColor.DARK_GRAY+"=====");
				sender.sendMessage(ChatColor.GOLD+"/plot buy "+ChatColor.GRAY+"Lets you buy the plot you are currently standing in.");
				sender.sendMessage(ChatColor.GOLD+"/plot leave "+ChatColor.GRAY+"Sets the plot you are currently standing in back to unowned.");
				sender.sendMessage(ChatColor.GOLD+"/plot add [player] "+ChatColor.GRAY+"Gives another player permission to build in the plot you are currently standing in.");
				sender.sendMessage(ChatColor.GOLD+"/plot remove [player] "+ChatColor.GRAY+"Stops a player from having build permission in the plot you are currently standing in.");
				sender.sendMessage(ChatColor.GOLD+"/plot rentable "+ChatColor.GRAY+"Lets make the plot your standing in rentable. This means other players can rent this plot.");
				sender.sendMessage(ChatColor.GOLD+"/plot unrentable "+ChatColor.GRAY+"Lets the plot your standing in stop being renable.");
				sender.sendMessage(ChatColor.GOLD+"/plot rent "+ChatColor.GRAY+"Lets you rent the plot you are currently standing in.");
				sender.sendMessage(ChatColor.GOLD+"/plot unrent "+ChatColor.GRAY+"Lets you stop renting the plot that your standing in.");
				sender.sendMessage(ChatColor.GOLD+"/plot kick "+ChatColor.GRAY+"Lets you kick out anyone renting the plot that your standing in.");
				sender.sendMessage(ChatColor.GOLD+"/plot info "+ChatColor.GRAY+"Gives you information on the plot that you are standing in.");
				sender.sendMessage(ChatColor.GOLD+"/plot tax "+ChatColor.GRAY+"Lets you tax your plots that other players are renting.");
				sender.sendMessage(ChatColor.GOLD+"/plot autobuy "+ChatColor.GRAY+"Lets you automaticly buy plots as you walk.");
				sender.sendMessage(ChatColor.DARK_GRAY+"=====");
			}else if(args.length==1){
				if(args[0].equalsIgnoreCase("buy")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.buy")){
							if(PlayerMg.canRentMorePlots(p)){
								WorldSettings settings = WhCommunity.getWorldSettings(p.getWorld().getName());
								if(settings.CONFIG_Plots){
									Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
									if(!plot.isClaimed()){
										if(WhCommunity.moneyEnabled()){
											if(WhCommunity.hasMoney(p.getName(), settings.CONFIG_Plot_Cost)){
												boolean allow = true;
												if(plot.isStreet()){
													for(int x = plot.getX()-1; x<=plot.getX()+1; x++){
														for(int z = plot.getZ()-1; z<=plot.getZ()+1; z++){
															Plot plot2 = PlotMg.getPlotAt(plot.getWorld(), x, z);
															if(plot2.isClaimed()
																	&&!plot2.getOwner().equals(p.getName()))allow=false;
														}
													}
												}
												if(allow){
													WhCommunity.takeMoney(p.getName(), settings.CONFIG_Plot_Cost);
													plot.setOwner(p.getName());
													PlayerMg.claimPlot(p);
													p.sendMessage(ChatColor.DARK_AQUA+"You have bought this plot. $"+ChatColor.AQUA+settings.CONFIG_Plot_Cost+ChatColor.DARK_AQUA+" has been removed from your bank account.");
												}else p.sendMessage(ChatColor.RED+"You are currently standing on a road. You cannot buy roads unless they are touching plots you own. You may not, however, buy roads if they are touching another player's plot.");
											}else p.sendMessage(ChatColor.RED+"You do not have enough money to buy this plot! Earn some money and try again later.");
										}else p.sendMessage(ChatColor.RED+"You are unable to buy plots currently, because the server's economy plugin is currently not working. This is a safely measure to prevent players to buying and renting more then they normally would have permission for.");
									}else p.sendMessage(ChatColor.RED+"This plot is already owned by your or another player!");
								}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
							}else p.sendMessage(ChatColor.RED+"You are not allowed to rent anymore plots! You have ether reached or passed your max plot limit of "+ChatColor.GOLD+WhCommunity.CONFIG_Max_Plots+ChatColor.RED+" plots!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to buy plots!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("leave")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.leave")){
							if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								if(plot.getOwner().equals(p.getName())){
									plot.setOwner(null);
									PlayerMg.unclaimPlot(p);
									p.sendMessage(ChatColor.DARK_AQUA+"This plot has been unclaimed, and is now wilderness again. You may buy it again later on if you choose.");
								}else p.sendMessage(ChatColor.RED+"You do not own this plot, so you cannot leave it! If you are renting this plot and wish to stop renting it, you must type \""+ChatColor.GOLD+"/plot unrent"+ChatColor.RED+"\".");
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to leave plots you buy!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("unrentable")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.unrentable")){
							if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								if(plot.getOwner().equals(p.getName())){
									if(plot.getRenter()==null){
										if(plot.isRentable()){
											plot.setRentable(false);
											p.sendMessage(ChatColor.DARK_AQUA+"This plot has been removed from the market. Players can no longer rent this plot.");
										}else p.sendMessage(ChatColor.RED+"This plot is not on the market and already is set to unrentable!");
									}else p.sendMessage(ChatColor.RED+"Someone is already renting this plot! If you wish to kick them out of the plot, you must use the command \""+ChatColor.GOLD+"/plot kick"+ChatColor.RED+"\". The plot will stay unrentable afterwards, until changed by you.");
								}else p.sendMessage(ChatColor.RED+"You do not own this plot, so you cannot take it off the market! If you wish to buy it, please type the command \""+ChatColor.GOLD+"/plot rent"+ChatColor.RED+"\" instead.");
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to take rentable plots off the market!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("rent")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.rent")){
							WorldSettings settings = WhCommunity.getWorldSettings(p.getWorld().getName());
							if(settings.CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								if(plot.isClaimed()){
									if(!plot.getOwner().equals(p.getName())){
										if(plot.isClaimed()){
											if(plot.getRenter()==null){
												if(plot.isRentable()){
													if(WhCommunity.moneyEnabled()){
														if(WhCommunity.hasMoney(p.getName(), settings.CONFIG_Plot_Rent)){
															plot.setRenter(p.getName());
															plot.setTaxTime(System.currentTimeMillis()+settings.CONFIG_Tax_Cooldown);
															WhCommunity.takeMoney(p.getName(), settings.CONFIG_Plot_Rent);
															p.sendMessage(ChatColor.DARK_AQUA+"You have rented this plot. $"+ChatColor.AQUA+settings.CONFIG_Plot_Rent+ChatColor.DARK_AQUA+" has been taken from your bank account and given the the plot owner's. " +
																	"This can be done again if the owner of this plot chooses to tax you, which they are allowed to do once every 24 hours. If the owner chooses to tax you, and you do not have enough money to pay them, " +
																	"you will automaticly be removed from your plot. The owner can also forcefully kick you from your plot if they choose. To view how much time you have left until this plot is allowed to be taxed again, " +
																	"look at the info menu for this plot. You can do this by clicking with a piece of paper, or using the command \""+ChatColor.GOLD+"/plot info"+ChatColor.RED+"\".");
														}else p.sendMessage(ChatColor.RED+"You do not have enough money to rent this plot! Earn some money and try again later.");
													}else p.sendMessage(ChatColor.RED+"You are unable to buy plots currently, because the server's economy plugin is currently not working. This is a safely measure to prevent players to buying and renting more then they normally would have permission for.");
												}else p.sendMessage(ChatColor.RED+"This plot has not been set as rentable, so you cannot rent it at this time. If this plot is suposed to be rentable, then talk to the plot owner about it.");
											}else p.sendMessage(ChatColor.RED+"Another player is already renting this plot, so you can't rent it.");
										}else p.sendMessage(ChatColor.RED+"You cannot rent plots in the wilderness! They must be owned by another player first.");
									}else p.sendMessage(ChatColor.RED+"You are the owner of this plot, so you do not need to rent it!");
								}else p.sendMessage(ChatColor.RED+"This plot is not owned!");
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to rent plots!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("unrent")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.unrent")){
							if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								if(!plot.getOwner().equals(p.getName())){
									if(plot.isClaimed()&&plot.getRenter().equals(p.getName())){
										plot.setRenter(null);
										p.sendMessage(ChatColor.DARK_AQUA+"You have unrented this plot.");
									}else p.sendMessage(ChatColor.RED+"You are not renting this plot!");
								}else p.sendMessage(ChatColor.RED+"You are the owner of this plot, so you cannot unrent it!");
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to unrent plots!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("kick")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.kick")){
							if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								if(plot.getOwner().equals(p.getName())){
									String rentername = plot.getRenter();
									if(rentername!=null){
										plot.setRenter(null);
										p.sendMessage(ChatColor.DARK_AQUA+"You have kicked "+rentername+" out of this plot. They are no longer renting this plot.");
										Player renter = Bukkit.getPlayerExact(rentername);
										if(renter!=null)renter.sendMessage(ChatColor.GREEN.toString()+ChatColor.ITALIC.toString()+p.getName()+" has kicked you out of one of your rented plots.");
									}else p.sendMessage(ChatColor.RED+"No one is currently renting this plot, so there is no one to kick out!");
								}else p.sendMessage(ChatColor.RED+"You are not the owner of this plot! You cannot kick renters from it!");
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to kick out players renting your plots!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("info")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.info")){
							if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								String t = "";
								for(String s : plot.getAllowedPlayerList()){
									t+=", "+s;
								}
								while(t.startsWith(", "))t=t.substring(2);
								p.sendMessage(ChatColor.DARK_GRAY+"================");
								p.sendMessage(ChatColor.DARK_AQUA+"Cords: "+ChatColor.GRAY+plot.getX()+", "+plot.getZ());
								p.sendMessage(ChatColor.DARK_AQUA+"Owner: "+ChatColor.GRAY+(plot.getOwner()==null?"None":plot.getOwner()));
								p.sendMessage(ChatColor.DARK_AQUA+"Rentable: "+ChatColor.GRAY+(plot.isRentable()?"Yes":"No"));
								p.sendMessage(ChatColor.DARK_AQUA+"Tax Time: "+ChatColor.AQUA+(plot.getTaxTime()==0?"Not Rented":((plot.getTaxTime()-System.currentTimeMillis())/1000)+" seconds."));
								p.sendMessage(ChatColor.DARK_AQUA+"Players Allowed: "+ChatColor.GRAY+(t.isEmpty()?"None":t));
								p.sendMessage(ChatColor.DARK_GRAY+"================");
								PlayerMg.createChunkGrid(p);
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to view info on plots!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("rentable")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.rentable")){
							if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								if(plot.getOwner().equals(p.getName())){
									if(!plot.isRentable()){
										plot.setRentable(true);
										p.sendMessage(ChatColor.DARK_AQUA+"Plot has been put on the market. Other players can now rent this plot, but you are still the owner.");
									}else p.sendMessage(ChatColor.RED+"Is plot is already rentable! If you would like to take this plot off the market, use the command \""+ChatColor.GOLD+"/plot unrentable"+ChatColor.RED+"\".");
								}else p.sendMessage(ChatColor.RED+"You are not the owner of this plot! You cannot make this plot rentable.");
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to make plots rentable!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("tax")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.tax")){
							WorldSettings settings = WhCommunity.getWorldSettings(p.getWorld().getName());
							if(settings.CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								if(plot.getOwner().equals(p.getName())){
									if(plot.getRenter()!=null){
										if(System.currentTimeMillis()>=plot.getTaxTime()){
											if(WhCommunity.moneyEnabled()){
												if(WhCommunity.hasMoney(plot.getRenter(), settings.CONFIG_Plot_Rent)){
													plot.setTaxTime(System.currentTimeMillis()+settings.CONFIG_Tax_Cooldown);
													WhCommunity.takeMoney(plot.getRenter(), settings.CONFIG_Plot_Rent);
													WhCommunity.giveMoney(p.getName(), settings.CONFIG_Plot_Rent);
													p.sendMessage(ChatColor.DARK_AQUA+"You have collected $"+ChatColor.AQUA+settings.CONFIG_Plot_Rent+ChatColor.DARK_AQUA+" from "+plot.getRenter()+".");
												}else{
													p.sendMessage(ChatColor.DARK_AQUA+"The renter of this plot did not have enough fund to pay rent, so they have been automaticly kicked from this plot.");
													try{
														Player renter = Bukkit.getPlayerExact(plot.getRenter());
														if(renter!=null)renter.sendMessage(ChatColor.GREEN.toString()+ChatColor.ITALIC.toString()+p.getName()+" has tried to collected rent from you, but you did not have enough money to pay them. So you were automaticly removed from your rented plot.");
													}catch(Exception exception){}
													plot.setRenter(null);
												}
											}else p.sendMessage(ChatColor.RED+"You are unable to tax plots currently, because the server's economy plugin is currently not working. This is a safely measure to prevent players from buying and renting more then they normally would have permission for.");
										}else p.sendMessage(ChatColor.RED+"You can't tax this plot yet, because it is still on cooldown! You can tax this plot again in "+((plot.getTaxTime()-System.currentTimeMillis())/1000)+" seconds.");
									}else p.sendMessage(ChatColor.RED+"This plot is not rented, so there is no one to tax!");
								}else p.sendMessage(ChatColor.RED+"You are not the owner of this plot! You cannot make this plot rentable.");
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to tax plots!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("autobuy")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						p.performCommand("autobuy");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else{
					sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please try:");
					sender.sendMessage(ChatColor.GOLD+"/plot");
					sender.sendMessage(ChatColor.GREEN+"for help.");
				}
			}else if(args.length==2){
				if(args[0].equalsIgnoreCase("add")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.add")){
							if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								boolean canAdd = false;
								if(plot.getRenter()==null){
									if(plot.getOwner().equals(p.getName())){
										canAdd=true;
									}else p.sendMessage(ChatColor.RED+"You are not the owner of this plot! You cannot add players to this plot.");
								}else{
									if(plot.getRenter().equals(p.getName())){
										canAdd=true;
									}else p.sendMessage(ChatColor.RED+"You are not the renter of this plot! You cannot add players to this plot.");
								}
								if(canAdd){
									Player add = Bukkit.getPlayer(args[1]);
									if(add!=null){
										if(!plot.getAllowedPlayerList().contains(add.getName())){
											plot.addPlayerToPlot(add.getName());
											p.sendMessage(ChatColor.DARK_AQUA+add.getName()+" is now allowed to build in this plot.");
											add.sendMessage(ChatColor.GREEN.toString()+ChatColor.ITALIC.toString()+p.getName()+" has given you permission to build in one of their plots.");
										}else p.sendMessage(ChatColor.RED+"That player is already allowed to build here!");
									}else p.sendMessage(ChatColor.RED+"Player not found or is not online!");
								}
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to add other players to your plots!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else if(args[0].equalsIgnoreCase("remove")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("whc.cmd.plot.remove")){
							if(WhCommunity.getWorldSettings(p.getWorld().getName()).CONFIG_Plots){
								Plot plot = PlotMg.getPlotAt(p.getLocation().getChunk());
								boolean canRemove = false;
								if(plot.getRenter()==null){
									if(plot.getOwner().equals(p.getName())){
										canRemove=true;
									}else p.sendMessage(ChatColor.RED+"You are not the owner of this plot! You cannot remove players from this plot.");
								}else{
									if(plot.getRenter().equals(p.getName())){
										canRemove=true;
									}else p.sendMessage(ChatColor.RED+"You are not the renter of this plot! You cannot remove players from this plot.");
								}
								if(canRemove){
									Player remove = Bukkit.getPlayer(args[1]);
									if(remove!=null){
										if(plot.getAllowedPlayerList().contains(remove.getName())){
											plot.removePlayerFromPlot(remove.getName());
											p.sendMessage(ChatColor.DARK_AQUA+remove.getName()+" is no longer allowed to build in this plot.");
											remove.sendMessage(ChatColor.GREEN.toString()+ChatColor.ITALIC.toString()+p.getName()+" has taken your permission to build in one of their plots.");
										}else p.sendMessage(ChatColor.RED+"That player is already blocked from this plot!");
									}else p.sendMessage(ChatColor.RED+"Player not found or is not online!");
								}
							}else p.sendMessage(ChatColor.RED+"Plots are not enabled in this world, so you cannot interact with them!");
						}else p.sendMessage(ChatColor.RED+"You do not have permission to remove other players from your plots!");
					}else sender.sendMessage("You must be a player to preform this command!");
				}else{
					sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please try:");
					sender.sendMessage(ChatColor.GOLD+"/plot");
					sender.sendMessage(ChatColor.GREEN+"for help.");
				}
			}else{
				sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please try:");
				sender.sendMessage(ChatColor.GOLD+"/plot");
				sender.sendMessage(ChatColor.GREEN+"for help.");
			}
			return true;
		}catch(Exception e){
			System.err.println("[WhCommunity] Error trying to run command /plot!");
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED+"There has been an error while trying to run this command.");
			return false;
		}
	}
}
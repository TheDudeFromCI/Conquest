package me.ci.Community;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import me.ci.WhCommunity;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomMaker;
import me.ci.Conquest.Misc.Achievement;
import me.ci.Conquest.Misc.CallName;
import me.ci.Conquest.Misc.InventorySelect;
import me.ci.Conquest.Textures.BuildingTexture;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class MiscEvents
{
	private static Plugin whcommunity;
	private static Map<String, Timer> playtimes = new HashMap<String, Timer>();
	private static Map<String, PermissionAttachment> permissions = new HashMap<String, PermissionAttachment>();

	public static void Link(Plugin plugin)
	{
		whcommunity = plugin;
	}

	public static void e(PlayerLoginEvent e)
	{
		Player p = e.getPlayer();
		final String ip = e	.getAddress()
							.toString();
		Save.set("Resources", "Ip List", ip, p.getName());
		Save.set("Players", p.getName(), "IP", ip);
		if (Bukkit.hasWhitelist() && (!p.isWhitelisted()))
		{
			if (System.currentTimeMillis() < 1373925600000L)
			{
				final long time = 1373925600000L - System.currentTimeMillis();
				final long seconds = time / 1000 % 60;
				final long minutes = time / (1000 * 60) % 60;
				final long hours = time / (1000 * 60 * 60) % 24;
				final long days = time / (1000 * 60 * 60 * 24);
				e.disallow(Result.KICK_WHITELIST,
						"Conquest opens in " + days + " day" + (days == 1 ? "" : "s") + ", " + hours + " hour"
								+ (hours == 1 ? "" : "s") + ", " + minutes + " minute" + (minutes == 1 ? "" : "s")
								+ ", and " + seconds + " second" + (seconds == 1 ? "" : "s") + "!");
			}
			else
			{
				String in = WhCommunity.CONFIG_Whitelist_Message;
				in = ChatColor.translateAlternateColorCodes('&', in);
				e.disallow(Result.KICK_WHITELIST, in);
			}
			return;
		}
		if (WhCommunity.GRAPHICS_FIX)
		{
			e.disallow(Result.KICK_WHITELIST,
					"The server is currently fixing the graphics of the map. Please come back later.");
			return;
		}
		PlayerMg.getChannel(p)
				.joinChannel(p);
	}

	public static void e(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		PlayerMg.getChannel(p)
				.leaveChannel(p);
		permissions.remove(p.getName());
		if (KingdomMaker.creating.contains(p.getName()))
			KingdomMaker.creating.remove(p.getName());
		List<CallName> callnames = CallName.getCallNames(p.getName());
		if (callnames != null && callnames.size() == 0)
			CallName.removeSlot(p.getName());
		// PlayerMg.removeScoreboard(p.getName()); // TODO
	}

	public static void e(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		Location to = e.getTo();
		Location from = e.getFrom();
		WorldSettings settings = WhCommunity.getWorldSettings(p	.getWorld()
																.getName());
		if (AutobuyCommand.autobuy.contains(p.getName()))
		{
			Chunk cto = to.getChunk();
			Chunk cfrom = from.getChunk();
			if (cto != cfrom)
			{
				if (PlayerMg.canRentMorePlots(p))
				{
					if (settings.CONFIG_Plots)
					{
						Plot plot = PlotMg.getPlotAt(p	.getLocation()
														.getChunk());
						if (!plot.isClaimed())
						{
							if (WhCommunity.moneyEnabled())
							{
								if (WhCommunity.hasMoney(p.getName(), settings.CONFIG_Plot_Cost))
								{
									boolean allow = true;
									if (plot.isStreet())
									{
										for (int x = plot.getX() - 1; x <= plot.getX() + 1; x++)
										{
											for (int z = plot.getZ() - 1; z <= plot.getZ() + 1; z++)
											{
												Plot plot2 = PlotMg.getPlotAt(plot.getWorld(), x, z);
												if (plot2.isClaimed() && !plot2	.getOwner()
																				.equals(p.getName()))
													allow = false;
											}
										}
									}
									if (allow)
									{
										WhCommunity.takeMoney(p.getName(), settings.CONFIG_Plot_Cost);
										plot.setOwner(p.getName());
										PlayerMg.claimPlot(p);
										p.sendMessage(ChatColor.DARK_AQUA + "You have bought this plot. $"
												+ ChatColor.AQUA + settings.CONFIG_Plot_Cost + ChatColor.DARK_AQUA
												+ " has been removed from your bank account.");
									}
									else
										p.sendMessage(ChatColor.RED
												+ "You are currently standing on a road. You cannot buy roads unless they are touching plots you own. You may not, however, buy roads if they are touching another player's plot.");
								}
								else
									p.sendMessage(ChatColor.RED
											+ "You do not have enough money to buy this plot! Earn some money and try again later.");
							}
							else
								p.sendMessage(ChatColor.RED
										+ "You are unable to buy plots currently, because the server's economy plugin is currently not working. This is a safely measure to prevent players to buying and renting more then they normally would have permission for.");
						}
						else
							p.sendMessage(ChatColor.RED + "This plot is already owned by your or another player!");
					}
					else
						p.sendMessage(ChatColor.RED
								+ "Plots are not enabled in this world, so you cannot interact with them!");
				}
				else
					p.sendMessage(ChatColor.RED
							+ "You are not allowed to rent anymore plots! You have ether reached or passed your max plot limit of "
							+ ChatColor.GOLD + WhCommunity.CONFIG_Max_Plots + ChatColor.RED + " plots!");
				PlayerMg.createChunkGrid(p);
			}
		}
		if (WhCommunity.getWorldSettings(p	.getWorld()
											.getName()).CONFIG_Conquest_Enabled)
		{
			if (p	.getItemInHand()
					.getType() == Material.ARROW)
			{
				p.setWalkSpeed(0.5f);
				p.setFlySpeed(0.5f);
			}
			else
			{
				p.setWalkSpeed(0.2f);
				p.setFlySpeed(0.1f);
			}
		}
		else
		{
			p.setWalkSpeed(0.2f);
			p.setFlySpeed(0.1f);
		}
	}

	public static void e(PlayerJoinEvent e)
	{
		Timer t = new Timer();
		Player p = e.getPlayer();
		t.scheduleAtFixedRate(new PlayTimeTimer(p), 1000, 60000);
		playtimes.put(p.getName(), t);
		permissions.put(p.getName(), p.addAttachment(whcommunity));
		reloadPermissions(p);
		for (Player player : Bukkit.getOnlinePlayers())
		{
			player.sendMessage(ChatColor.GREEN + p.getName() + " has joined in world \"" + p.getWorld()
																							.getName()
					+ "\".");
		}
		if (!p.hasPlayedBefore())
			PlayerMg.setExtraLandTime(p.getName());
		new KingdomMaker().createKingdom(p, p.getWorld());
		if (WhCommunity.getWorldSettings(p	.getWorld()
											.getName()).CONFIG_Conquest_Enabled)
		{
			PlayerMg.resetConquestToolkit(p);
		}
	}

	public static void e(PlayerTeleportEvent e)
	{
		Location to = e.getTo();
		final Player p = e.getPlayer();
		if (!e	.getFrom()
				.getWorld()
				.equals(to.getWorld()))
		{
			reloadPermissions(p);
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.sendMessage(ChatColor.GREEN + p.getName() + " has teleported to world \"" + to	.getWorld()
																										.getName()
						+ "\".");
			}
			if (KingdomMaker.creating.contains(p.getName()))
			{
				KingdomMaker.creating.remove(p.getName());
				p.sendMessage(ChatColor.RED + "[Conquest] " + ChatColor.GREEN + "Kingdom creation cancled.");
			}
			if (WhCommunity.getWorldSettings(to	.getWorld()
												.getName()).CONFIG_Conquest_Enabled)
			{
				new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							Thread.sleep(1000);
						}
						catch (Exception exception)
						{}
						PlayerMg.resetConquestToolkit(p);
					}
				}).start();
			}
			new KingdomMaker().createKingdom(p, e	.getTo()
													.getWorld());
		}
	}

	public static void reloadPermissions(Player p)
	{
		PermissionAttachment perms = permissions.get(p.getName());
		if (perms == null)
			return;
		for (String s : perms	.getPermissions()
								.keySet())
		{
			perms.unsetPermission(s);
		}
		for (String s : PlayerMg.getPermissionGroup(p)
								.getPermissionNodes())
		{
			perms.setPermission(s, true);
		}
		p.recalculatePermissions();
	}

	public static void e(ChunkUnloadEvent e)
	{
		// if (BuildingTexture.updated.containsKey(e.getChunk()))
		// e.setCancelled(true);
	}

	public static void e(InventoryClickEvent e)
	{
		HumanEntity mob = e.getWhoClicked();
		if (WhCommunity.getWorldSettings(mob.getWorld()
											.getName()).CONFIG_Conquest_Enabled)
		{
			if (mob instanceof Player)
			{
				Player p = (Player) mob;
				if (e.getCurrentItem() != null)
				{
					if (e	.getView()
							.getTitle()
							.equals("Itembar Manager"))
					{
						if (e.getRawSlot() >= 27)
							e.setCancelled(true);
					}
					else
						e.setCancelled(true);
					if (e	.getView()
							.getTitle()
							.equals("Build which building?") && e.getRawSlot() < 54)
					{
						InventorySelect	.getNewestCallName(p.getName())
										.setResponse(e.getCurrentItem());
						return;
					}
					if (e	.getView()
							.getTitle()
							.equals("Choose Monitors") && e.getRawSlot() < 18
							&& !InventorySelect	.getNewestCallName(p.getName())
												.hasResponed())
					{
						InventorySelect	.getNewestCallName(p.getName())
										.setResponse(e.getCurrentItem());
						return;
					}
					if (e	.getView()
							.getTitle()
							.equals("Choose Troop Type") && e.getRawSlot() < 36)
					{
						final InventorySelect inv = InventorySelect.getNewestCallName(p.getName());
						inv.setResponse(e.getCurrentItem());
						inv.setSlot(e.getRawSlot());
						return;
					}
					if (e	.getView()
							.getTitle()
							.equals("Unload Unit") && e.getRawSlot() < 54)
					{
						final InventorySelect inv = InventorySelect.getNewestCallName(p.getName());
						inv.setResponse(e.getCurrentItem());
						inv.setSlot(e.getRawSlot());
						return;
					}
					if (e	.getView()
							.getTitle()
							.equals("Choose Henchman") && e.getRawSlot() < 54)
					{
						final InventorySelect inv = InventorySelect.getNewestCallName(p.getName());
						inv.setResponse(e.getCurrentItem());
						return;
					}
					if (e	.getView()
							.getTitle()
							.endsWith(" Score") && e.getRawSlot() < 54)
					{
						final InventorySelect inv = InventorySelect.getNewestCallName(p.getName());
						inv.setResponse(e.getCurrentItem());
						inv.setSlot(e.getRawSlot());
						return;
					}
					if (e	.getView()
							.getTitle()
							.equals("Help Videos") && e.getRawSlot() < 54)
					{
						final InventorySelect inv = InventorySelect.getNewestCallName(p.getName());
						inv.setResponse(e.getCurrentItem());
						return;
					}
					if (e	.getView()
							.getTitle()
							.equals("Help Catagories") && e.getRawSlot() < 54)
					{
						final InventorySelect inv = InventorySelect.getNewestCallName(p.getName());
						inv.setResponse(e.getCurrentItem());
						return;
					}
				}
				else
					e.setCancelled(true);
				if (!PlayerMg.invpage.containsKey(p.getName()))
					PlayerMg.invpage.put(p.getName(), 1);
				if (e.getSlot() == 27)
				{
					PlayerMg.invpage.put(p.getName(), Math.max(PlayerMg.invpage.get(p.getName()) - 1, 1));
					PlayerMg.resetConquestToolkit(p);
				}
				else if (e.getSlot() == 35)
				{
					PlayerMg.invpage.put(p.getName(),
							Math.min(PlayerMg.invpage.get(p.getName()) + 1, WhCommunity.wikitips.size()));
					PlayerMg.resetConquestToolkit(p);
				}
			}
		}
	}

	public static void e(PlayerDropItemEvent e)
	{
		if (WhCommunity.getWorldSettings(e	.getPlayer()
											.getWorld()
											.getName()).CONFIG_Conquest_Enabled)
			e.setCancelled(true);
	}

	public static void e(PlayerPickupItemEvent e)
	{
		if (WhCommunity.getWorldSettings(e	.getPlayer()
											.getWorld()
											.getName()).CONFIG_Conquest_Enabled)
			e.setCancelled(true);
	}

	public static void e(FoodLevelChangeEvent e)
	{
		HumanEntity human = e.getEntity();
		if (human instanceof Player && WhCommunity.getWorldSettings(((Player) human).getWorld()
																					.getName()).CONFIG_Conquest_Enabled)
			e.setCancelled(true);
	}

	public static void e(final ServerListPingEvent e)
	{
		String ip = e	.getAddress()
						.toString();
		String name = Save.get("Resources", "Ip List", ip);
		if (name == null)
			e.setMotd(ChatColor.DARK_GREEN + "You should check out our server!");
		else
		{
			OfflinePlayer p = Bukkit.getOfflinePlayer(name);
			if (p.isBanned())
				e.setMotd(ChatColor.DARK_GREEN + "Oh no! Your banned " + name + "!");
			else if (Bukkit.hasWhitelist() && !p.isWhitelisted())
				e.setMotd(ChatColor.DARK_GREEN + "Sorry " + name + ", we're debugging.");
			else if (p.isOp())
				e.setMotd(ChatColor.DARK_GREEN + "Hey admin " + name + ", whats up?");
			else
				e.setMotd(ChatColor.DARK_GREEN + "Hey " + name + ", you should really log in! We miss you!");
		}
	}

	public static void e(PlayerItemHeldEvent e)
	{
		Player p = e.getPlayer();
		if (WhCommunity.getWorldSettings(p	.getWorld()
											.getName()).CONFIG_Conquest_Enabled)
		{
			if (!PlayerMg.invscroll.containsKey(p.getName()))
				PlayerMg.invscroll.put(p.getName(), 0);
			int s = PlayerMg.invscroll.get(p.getName());
			int itemslots = 2;
			if (PlayerMg.getKingdom(p.getName()) == null)
				itemslots = 1;
			if (e.getNewSlot() >= 7 && e.getPreviousSlot() <= 1)
			{
				PlayerMg.invscroll.put(p.getName(), (s - 1 < 0 ? itemslots : s - 1));
				PlayerMg.resetConquestToolkit(p);
			}
			else if (e.getNewSlot() <= 1 && e.getPreviousSlot() >= 7)
			{
				PlayerMg.invscroll.put(p.getName(), (s + 1 > itemslots ? 0 : s + 1));
				PlayerMg.resetConquestToolkit(p);
			}
			final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
			if (kingdom != null)
				kingdom.awardAchievement(Achievement.SCROLLING_THE_ITEMBAR);
		}
	}
}

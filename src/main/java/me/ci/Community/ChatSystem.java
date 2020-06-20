package me.ci.Community;

import java.util.ArrayList;
import java.util.List;

import me.ci.WhCommunity;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Misc.CallName;
import me.ci.Conquest.Misc.Reply;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatSystem
{
	public static void e(AsyncPlayerChatEvent e)
	{
		try
		{
			Player p = e.getPlayer();
			WorldSettings settings = WhCommunity.getWorldSettings(p	.getWorld()
																	.getName());
			e.setCancelled(true);
			String message = e.getMessage();
			if (Reply.awaitingResponse(p.getName()))
			{
				Reply r = Reply.getReply(p.getName());
				if (r.hasResponse(message))
					r.callResponse(message);
				else
				{
					p.sendMessage(ChatColor.RED + "That is not an available response!");
					String t = "";
					for (String s : r.getResponses())
					{
						t += ", " + s;
					}
					t = t.substring(2);
					p.sendMessage(ChatColor.RED + "Available responses are as follows:");
					p.sendMessage(ChatColor.GOLD + t);
				}
				return;
			}
			try
			{
				if (CallName.awaitingResponse(p.getName()))
				{
					CallName.getNewestCallName(p.getName())
							.setResponse(message);
					return;
				}
			}
			catch (Exception exception)
			{}
			if (PlayerMg.hasPermission(p, "whc.chat.talk." + p	.getWorld()
																.getName()
					+ ".*") || PlayerMg.hasPermission(p,
							"whc.chat.talk." + p.getWorld()
												.getName()
									+ "." + PlayerMg.getChannel(p)
													.getId())
					|| PlayerMg.hasPermission(p, "whc.chat.*." + p	.getWorld()
																	.getName()
							+ "." + PlayerMg.getChannel(p)
											.getId())
					|| PlayerMg.hasPermission(p, "whc.chat.*." + p	.getWorld()
																	.getName()
							+ ".*"))
			{
				List<Player> get = new ArrayList<Player>();
				int id;
				try
				{
					id = PlayerMg	.getChannel(p)
									.getId();
				}
				catch (NullPointerException exception)
				{
					id = 0;
				}
				if (WhCommunity.CONFIG_Local_Chat)
				{
					if (settings.CONFIG_Chat_Default_Range == -1)
					{
						for (Player player : Bukkit.getOnlinePlayers())
						{
							if (player.getWorld() != p.getWorld())
								continue;
							if (PlayerMg.getChannel(player)
										.getId() == id)
								get.add(player);
						}
					}
					else
					{
						for (Player player : getNearbyPlayers(p.getLocation(), settings.CONFIG_Chat_Default_Range))
						{
							if (PlayerMg.getChannel(player)
										.getId() == id)
								get.add(player);
						}
					}
				}
				else
				{
					for (Player player : Bukkit.getOnlinePlayers())
					{
						get.add(player);
					}
				}
				String layout = settings.CONFIG_Chat_Default_Layout;
				layout = ChatColor.translateAlternateColorCodes('&', layout);
				try
				{
					// if (WhCommunity.permission != null)
					// layout = layout.replace("%pgroup%",
					// WhCommunity.permission.getPrimaryGroup(p));
					// else
					layout = layout.replace("%pgroup%", PlayerMg.getPermissionGroup(p)
																.getName());
				}
				catch (Exception exception)
				{}
				layout = layout.replace("%prefix%", PlayerMg.getPrefix(p));
				layout = layout.replace("%suffix%", PlayerMg.getSuffix(p));
				try
				{
					// if (WhCommunity.economy != null)
					// layout = layout.replace("%money%",
					// String.valueOf(WhCommunity.economy.getBalance(p.getName())));
				}
				catch (Exception exception)
				{}
				layout = layout.replace("%biome%", String.valueOf(p	.getLocation()
																	.getBlock()
																	.getBiome()));
				layout = layout.replace("%player%", p.getName());
				layout = layout.replace("%nick%", p.getDisplayName());
				layout = layout.replace("%message%", message);
				layout = layout.replace("%world%", p.getWorld()
													.getName());
				final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
				if (kingdom != null)
				{
					layout = layout.replace("%kingdom%", kingdom.getName());
					layout = layout.replace("%gender%", (kingdom.isKing() ? "Lord" : "Lady"));
				}
				Channel c = PlayerMg.getChannel(p);
				layout = layout.replace("%channel%", c.toString());
				for (Player player : get)
				{
					player.sendMessage(layout);
				}
				System.out.println(layout);
			}
			else
				p.sendMessage(ChatColor.RED + "You do not have permission to chat here!");
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	private static List<Player> getNearbyPlayers(Location l, int range)
	{
		List<Player> near = new ArrayList<Player>();
		for (Player p : l	.getWorld()
							.getPlayers())
		{
			if (p	.getLocation()
					.distance(l) <= range)
				near.add(p);
		}
		return near;
	}
}

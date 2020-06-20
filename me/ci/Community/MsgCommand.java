package me.ci.Community;

import me.ci.WhCommunity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length<2){
			sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please use as:");
			sender.sendMessage(ChatColor.GOLD+"/msg [player] [message]");
		}else{
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(p.hasPermission("whc.cmd.msg")
						&&p.hasPermission("whc.chat.msg."+p.getWorld().getName()+".*")
						||p.hasPermission("whc.chat.msg."+p.getWorld().getName()+"."+PlayerMg.getChannel(p).getId())
						||p.hasPermission("whc.chat.*."+p.getWorld().getName()+"."+PlayerMg.getChannel(p).getId())
						||p.hasPermission("whc.chat.*."+p.getWorld().getName()+".*")){
					try{
						Player to = Bukkit.getPlayer(args[0]);
						if(to!=null){
							WorldSettings settings = WhCommunity.getWorldSettings(p.getWorld().getName());
							boolean chat = false;
							if(!WhCommunity.CONFIG_Local_Chat)chat=true;
							else if(settings.CONFIG_Chat_Message_Range==-1)chat=true;
							else if(to.getWorld().getName().equals(p.getWorld().getName())){
								Location l1 = p.getLocation();
								Location l2 = to.getLocation();
								double distance = Math.sqrt(Math.pow(l1.getX()-l2.getX(), 2)+Math.pow(l1.getZ()-l2.getZ(), 2)+Math.pow(l1.getY()-l2.getY(), 2));
								if(distance<=settings.CONFIG_Chat_Message_Range)chat=true;
								else p.sendMessage(ChatColor.RED+"That player is too far away! You cannot send a message to them.");
							}else p.sendMessage(ChatColor.RED+"You cannot send a message to that person, they are in a different world!");
							if(chat){
								String message = "";
								for(int i = 1; i<args.length; i++){
									message+=" "+args[i];
								}
								while(message.startsWith(" "))message=message.substring(1);
								Channel cid = PlayerMg.getChannel(p);
								String layout = settings.CONFIG_Chat_Message_Layout;
								layout=ChatColor.translateAlternateColorCodes('&', layout);
								try{
									if(WhCommunity.permission!=null)layout=layout.replace("%pgroup%", WhCommunity.permission.getPrimaryGroup(p));
									else layout=layout.replace("%pgroup%", PlayerMg.getPermissionGroup(p).getName());
								}catch(Exception exception){}
								layout=layout.replace("%prefix%", PlayerMg.getPrefix(p));
								layout=layout.replace("%suffix%", PlayerMg.getSuffix(p));
								if(WhCommunity.economy!=null)layout=layout.replace("%money%", String.valueOf(WhCommunity.economy.getBalance(p.getName())));
								layout=layout.replace("%biome%", String.valueOf(p.getLocation().getBlock().getBiome()));
								layout=layout.replace("%sender%", p.getDisplayName());
								layout=layout.replace("%receiver%", to.getDisplayName());
								layout=layout.replace("%player%", p.getName());
								layout=layout.replace("%nick%", p.getDisplayName());
								layout=layout.replace("%message%", message);
								layout=layout.replace("%world%", p.getWorld().getName());
								layout=layout.replace("%channel%", cid.toString());
								p.sendMessage(layout);
								to.sendMessage(layout);
							}
						}else p.sendMessage(ChatColor.RED+"Player not found or is not online!");
					}catch(Exception exception){
						exception.printStackTrace();
					}
				}else p.sendMessage(ChatColor.RED+"You do not have permission to send messages!");
			}else sender.sendMessage(ChatColor.RED+"You must be a player to preform this command!");
		}
		return false;
	}
}
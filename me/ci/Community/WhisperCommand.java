package me.ci.Community;

import java.util.ArrayList;
import java.util.List;

import me.ci.WhCommunity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WhisperCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length==0){
			sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please use as:");
			sender.sendMessage(ChatColor.GOLD+"/whisper [message]");
		}else{
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(p.hasPermission("whc.cmd.whisper")
						&&p.hasPermission("whc.chat.whisper."+p.getWorld().getName()+".*")
						||p.hasPermission("whc.chat.whisper."+p.getWorld().getName()+"."+PlayerMg.getChannel(p).getId())
						||p.hasPermission("whc.chat.*."+p.getWorld().getName()+"."+PlayerMg.getChannel(p).getId())
						||p.hasPermission("whc.chat.*."+p.getWorld().getName()+".*")){
					try{
						String message = "";
						for(int i = 0; i<args.length; i++){
							message+=" "+args[i];
						}
						while(message.startsWith(" "))message=message.substring(1);
						List<Player> get = new ArrayList<Player>();
						WorldSettings settings = WhCommunity.getWorldSettings(p.getWorld().getName());
						Channel cid = PlayerMg.getChannel(p);
						if(WhCommunity.CONFIG_Local_Chat){
							if(settings.CONFIG_Chat_Whisper_Range==-1){
								for(Player player : p.getWorld().getPlayers()){
									if(PlayerMg.getChannel(player).getId()==cid.getId())get.add(player);
								}
							}else{
								for(Entity mob : p.getNearbyEntities(settings.CONFIG_Chat_Whisper_Range, settings.CONFIG_Chat_Whisper_Range, settings.CONFIG_Chat_Whisper_Range)){
									if(mob instanceof Player
											&&PlayerMg.getChannel((Player)mob).getId()==cid.getId())get.add((Player)mob);
								}
								get.add(p);
							}
						}else{
							for(Player player : Bukkit.getOnlinePlayers()){
								get.add(player);
							}
						}
						String layout = settings.CONFIG_Chat_Whisper_Layout;
						layout=ChatColor.translateAlternateColorCodes('&', layout);
						try{
							if(WhCommunity.permission!=null)layout=layout.replace("%pgroup%", WhCommunity.permission.getPrimaryGroup(p));
							else layout=layout.replace("%pgroup%", PlayerMg.getPermissionGroup(p).getName());
						}catch(Exception exception){}
						layout=layout.replace("%prefix%", PlayerMg.getPrefix(p));
						layout=layout.replace("%suffix%", PlayerMg.getSuffix(p));
						if(WhCommunity.economy!=null)layout=layout.replace("%money%", String.valueOf(WhCommunity.economy.getBalance(p.getName())));
						layout=layout.replace("%biome%", String.valueOf(p.getLocation().getBlock().getBiome()));
						layout=layout.replace("%player%", p.getName());
						layout=layout.replace("%nick%", p.getDisplayName());
						layout=layout.replace("%message%", message);
						layout=layout.replace("%world%", p.getWorld().getName());
						layout=layout.replace("%channel%", cid.toString());
						for(Player player : get){
							player.sendMessage(layout);
						}
						System.out.println(layout);
					}catch(Exception exception){
						exception.printStackTrace();
					}
				}else p.sendMessage(ChatColor.RED+"You do not have permission to whisper messages!");
			}else sender.sendMessage(ChatColor.RED+"You must be a player to preform this command!");
		}
		return false;
	}
}
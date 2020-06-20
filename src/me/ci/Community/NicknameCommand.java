package me.ci.Community;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NicknameCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length==1){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(p.hasPermission("whc.cmd.nickname.self")){
					PlayerMg.setNickname(p.getName(), args[0]);
					p.setDisplayName(args[0]);
					p.sendMessage(ChatColor.DARK_AQUA+"Nickname set to "+args[0]+".");
				}else p.sendMessage(ChatColor.RED+"You do not have permission to set your own nickname!");
			}else sender.sendMessage(ChatColor.RED+"You must be a player to preform this command!");
		}else if(args.length==2){
			if(sender.hasPermission("whc.cmd.nickname.other")){
				Player p = Bukkit.getPlayer(args[0]);
				if(p!=null){
					PlayerMg.setNickname(p.getName(), args[1]);
					p.setDisplayName(args[1]);
					p.sendMessage(ChatColor.DARK_AQUA+"Nickname set to "+args[1]+".");
					sender.sendMessage(ChatColor.DARK_AQUA+p.getName()+"'s nickname set to "+args[1]+".");
				}else sender.sendMessage(ChatColor.RED+"Player not found or is not online!");
			}else sender.sendMessage(ChatColor.RED+"You do not have permission to set other player's nicknames!");
		}else{
			sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please use as:");
			sender.sendMessage(ChatColor.GOLD+"/nickname {player} [nickname]");
		}
		return false;
	}
}
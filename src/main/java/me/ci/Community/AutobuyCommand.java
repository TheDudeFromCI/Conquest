package me.ci.Community;

import java.util.LinkedList;
import java.util.List;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutobuyCommand implements CommandExecutor{
	public static List<String> autobuy = new LinkedList<String>();
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length==0){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(PlayerMg.hasPermission(p, "whc.cmd.autobuy")){
					if(AutobuyCommand.autobuy.contains(p.getName())){
						AutobuyCommand.autobuy.remove(p.getName());
						p.sendMessage(ChatColor.DARK_AQUA+"You have disabled autobuy mode.");
					}else{
						AutobuyCommand.autobuy.add(p.getName());
						p.sendMessage(ChatColor.DARK_AQUA+"You have enabled autobuy mode.");
					}
				}else p.sendMessage(ChatColor.RED+"You do not have permission to automaticly buy plots!");
			}else sender.sendMessage(ChatColor.RED+"You must be a player to preform this command!");
		}else{
			sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please use as:");
			sender.sendMessage(ChatColor.GOLD+"/autobuy");
		}
		return false;
	}
}
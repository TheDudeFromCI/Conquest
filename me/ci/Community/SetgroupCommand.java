package me.ci.Community;

import me.ci.WhCommunity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetgroupCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length==3){
			if(sender.hasPermission("whc.cmd.setgroup")){
				Player player = Bukkit.getPlayer(args[0]);
				if(player!=null){
					WorldSettings settings = WhCommunity.getWorldSettings(args[1]);
					if(settings!=null){
						PermissionGroup group = settings.getPermissionGroup(args[2]);
						PlayerMg.setPermissionGroup(player, group);
						player.sendMessage(ChatColor.DARK_AQUA+"Your permissions group for world \""+settings.world+"\" has been set to "+group.getName()+".");
						sender.sendMessage(ChatColor.DARK_AQUA+player.getName()+"'s permissions group for world \""+settings.world+"\" has been set to "+group.getName()+".");
					}else sender.sendMessage(ChatColor.RED+"World not found!");
				}else sender.sendMessage(ChatColor.RED+"Player not found or is not online!");
			}else sender.sendMessage(ChatColor.RED+"You do not have permission to set other player's groups!");
		}else{
			sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please use as:");
			sender.sendMessage(ChatColor.GOLD+"/setgroup [player] [world] [group]");
		}
		return false;
	}
}
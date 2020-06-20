package me.ci.Community;


import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length==0){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(PlayerMg.hasPermission(p, "whc.cmd.gm")){
					if(p.getGameMode().equals(GameMode.CREATIVE))p.setGameMode(GameMode.SURVIVAL);
					else p.setGameMode(GameMode.CREATIVE);
					p.sendMessage(ChatColor.DARK_AQUA+"Gamemode updated.");
				}else p.sendMessage(ChatColor.RED+"You do not have permission to toggle your gamemode!");
			}else sender.sendMessage(ChatColor.RED+"You must be a player to preform this command!");
		}else{
			sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please use as:");
			sender.sendMessage(ChatColor.GOLD+"/gm");
		}
		return false;
	}
}
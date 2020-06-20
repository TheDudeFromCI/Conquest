package me.ci.Community;

import me.ci.WhCommunity;
import me.ci.Community.WorldSettings.Rank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length==0){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(PlayerMg.hasPermission(p, "whc.cmd.rank")){
					int total = 0;
					for(World w : Bukkit.getWorlds()){
						String playtime = Save.get("Players", p.getName(), "PlayTime-"+w.getName());
						int time = playtime==null?0:Integer.valueOf(playtime);
						total+=time;
						String current = PlayerMg.getPermissionGroup(p).getName();
						WorldSettings settings = WhCommunity.getWorldSettings(w.getName());
						Rank next = settings.getNextRank(current);
						if(next==null)p.sendMessage(ChatColor.DARK_AQUA+w.getName()+" Play Time: "+ChatColor.AQUA+time);
						else p.sendMessage(ChatColor.DARK_AQUA+w.getName()+" Play Time: "+ChatColor.AQUA+time+ChatColor.DARK_AQUA+", Next Rank: "+ChatColor.GRAY+next.to+ChatColor.DARK_AQUA+" at "+ChatColor.AQUA+next.minutes+ChatColor.DARK_AQUA+" minutes.");
					}
					p.sendMessage(ChatColor.DARK_AQUA+"Total Player Time: "+ChatColor.AQUA+total+ChatColor.DARK_AQUA+" minutes.");
				}else p.sendMessage(ChatColor.RED+"You do not have permission to use this command!");
			}else sender.sendMessage(ChatColor.RED+"You must be a player to preform this command!");
		}else{
			sender.sendMessage(ChatColor.GREEN+"Unknown arguments. Please use as:");
			sender.sendMessage(ChatColor.GOLD+"/rank");
		}
		return false;
	}
}
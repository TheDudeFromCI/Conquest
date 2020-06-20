package me.ci.Community;

import java.util.HashMap;
import java.util.Map;

import me.ci.WhCommunity;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Runs the command to let players toggle their current Pvp status.
 * @author TheDudeFromCI
 */
public class PvpCommand implements CommandExecutor{
	private static Map<String,Long> cd = new HashMap<String,Long>();
	/**
	 * Called whenever a player issues a /pvp command. Allows toggling of the player's
	 * current Pvp status.
	 * @param sender - The player/console sending the command.
	 * @param cmd - The command being sent.
	 * @param label - The command label.
	 * @param args - The arguments following the command.
	 * @return True if the command was successfully issued, false otherwise.
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		try{
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(args.length==0){
					if(PlayerMg.hasPermission(p, "whc.cmd.pvp")){
						boolean cooled = false;
						if(PvpCommand.cd.containsKey(p.getName())){
							cooled=System.currentTimeMillis()>=PvpCommand.cd.get(p.getName());
						}else cooled = true;
						if(cooled){
							if(PlayerMg.hasPvpEnabled(p)){
								PlayerMg.setPvpEnable(p, false);
								p.sendMessage(ChatColor.DARK_AQUA+"You have toggled your Pvp off.");
							}else{
								PlayerMg.setPvpEnable(p, true);
								p.sendMessage(ChatColor.DARK_AQUA+"You have toggled your Pvp on.");
							}
							PvpCommand.cd.put(p.getName(), System.currentTimeMillis()+WhCommunity.CONFIG_Pvp_Toggle_Cooldown);
						}else p.sendMessage(ChatColor.RED+"You are still on Pvp cooldown, and cannot toggle your Pvp for another "+((PvpCommand.cd.get(p.getName())-System.currentTimeMillis())/1000)+" seconds!");
					}else p.sendMessage(ChatColor.RED+"You do not have the permission to toggle your Pvp!");
				}else{
					p.sendMessage(ChatColor.GREEN+"Unknown arguments. Please use as:");
					p.sendMessage(ChatColor.GOLD+"/pvp");
				}
			}else sender.sendMessage("You must be a player to preform this command!");
			return true;
		}catch(Exception e){
			System.err.println("[WhCommunity] Error trying to run command /pvp!");
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED+"There has been an error while trying to run this command.");
			return false;
		}
	}
}
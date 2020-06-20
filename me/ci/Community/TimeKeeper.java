package me.ci.Community;

import java.util.Timer;
import java.util.TimerTask;

import me.ci.WhCommunity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TimeKeeper extends TimerTask{
	private int loops = 0;
	private static Timer t;
	public static void start(){
		t=new Timer();
		t.scheduleAtFixedRate(new TimeKeeper(), 1000, 1000);
	}
	public static void stop(){
		if(t!=null)t.cancel();
	}
	public void run(){
		try{
			loops++;
			if(loops%WhCommunity.CONFIG_Auto_Message_Delay==0){
				for(World w : Bukkit.getWorlds()){
					WorldSettings settings = WhCommunity.getWorldSettings(w.getName());
					if(!settings.CONFIG_Auto_Messages.isEmpty()){
						String s = settings.getNextAutoMessage();
						s=ChatColor.translateAlternateColorCodes('&', s);
						for(Player p : w.getPlayers()){
							p.sendMessage(ChatColor.RED.toString()+ChatColor.ITALIC+"["+WhCommunity.CONFIG_Auto_Message_name+"] "+ChatColor.DARK_AQUA+s);
						}
					}
				}
			}
			if(loops%120==0){
				for(World w : Bukkit.getWorlds()){
					WorldSettings settings = WhCommunity.getWorldSettings(w.getName());
					if(settings.CONFIG_Always_Day)w.setFullTime(0);
				}
			}
			if(loops==WhCommunity.CONFIG_Restart_Minutes*60-600)Bukkit.broadcastMessage(ChatColor.RED+"[WhCommunity] "+ChatColor.DARK_AQUA+"10 minutes until server restart!");
			else if(loops==WhCommunity.CONFIG_Restart_Minutes*60-300)Bukkit.broadcastMessage(ChatColor.RED+"[WhCommunity] "+ChatColor.DARK_AQUA+"5 minutes until server restart!");
			else if(loops==WhCommunity.CONFIG_Restart_Minutes*60-120)Bukkit.broadcastMessage(ChatColor.RED+"[WhCommunity] "+ChatColor.DARK_AQUA+"2 minutes until server restart!");
			else if(loops==WhCommunity.CONFIG_Restart_Minutes*60-60)Bukkit.broadcastMessage(ChatColor.RED+"[WhCommunity] "+ChatColor.DARK_AQUA+"1 minute until server restart!");
			else if(loops>=WhCommunity.CONFIG_Restart_Minutes*60){
				if(!WhCommunity.GRAPHICS_FIX){
					Bukkit.shutdown();
					cancel();
				}
			}
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
}
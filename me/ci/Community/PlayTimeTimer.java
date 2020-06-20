package me.ci.Community;

import java.util.TimerTask;

import me.ci.WhCommunity;
import me.ci.Community.WorldSettings.Rank;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayTimeTimer extends TimerTask{
	private Player p;
	public PlayTimeTimer(Player p){
		this.p=p;
	}
	public void run(){
		if(p==null){
			cancel();
			return;
		}
		if(!p.isOnline()){
			cancel();
			return;
		}
		String playtime = Save.get("Players", p.getName(), "PlayTime-"+p.getWorld().getName());
		int time = playtime==null?0:Integer.valueOf(playtime)+1;
		Save.set("Players", p.getName(), "PlayTime-"+p.getWorld().getName(), String.valueOf(time));
		WorldSettings settings = WhCommunity.getWorldSettings(p.getWorld().getName());
		String current = PlayerMg.getPermissionGroup(p).getName();
		Rank next = settings.getNextRank(current);
		if(next==null)return;
		if(next.minutes<=time){
			PlayerMg.setPermissionGroup(p, next.to);
			p.sendMessage(ChatColor.DARK_GRAY+"==================================");
			p.sendMessage(ChatColor.DARK_AQUA+"Congragulation! You have been ranked up to "+ChatColor.AQUA+next.to+ChatColor.DARK_AQUA+"!");
			Rank newnext = settings.getNextRank(PlayerMg.getPermissionGroup(p).getName());
			if(newnext==null)p.sendMessage(ChatColor.DARK_AQUA+"Your now at max rank! Play time in this world will no longer rank you up.");
			else p.sendMessage(ChatColor.DARK_AQUA+"Your next rank will be "+ChatColor.AQUA+newnext.to+ChatColor.DARK_AQUA+".");
			p.sendMessage(ChatColor.DARK_GRAY+"==================================");
		}
	}
}
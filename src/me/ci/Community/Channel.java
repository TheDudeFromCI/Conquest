package me.ci.Community;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Channel{
	private final int id;
	private final String name;
	public List<String> players = new ArrayList<String>();
	public Channel(int id, String name){
		this.id=id;
		this.name=name;
	}
	public void joinChannel(Player p){
		this.players.add(p.getName());
		List<String> remove = new ArrayList<String>();
		for(String player : this.players){
			Player p1 = Bukkit.getPlayerExact(player);
			if(p1!=null)p1.sendMessage(ChatColor.GREEN+p.getName()+" has joined the channel.");
			else remove.add(player);
		}
		for(String s : remove){
			this.players.remove(s);
		}
		PlayerMg.setChannel(p, this.id);
	}
	public void leaveChannel(Player p){
		List<String> remove = new ArrayList<String>();
		for(String player : this.players){
			Player p1 = Bukkit.getPlayerExact(player);
			if(p1!=null)p1.sendMessage(ChatColor.GREEN+p.getName()+" has left the channel.");
			else remove.add(player);
		}
		for(String s : remove){
			this.players.remove(s);
		}
		this.players.remove(p.getName());
		PlayerMg.setChannel(p, 0);
	}
	@Override
	public String toString(){
		return this.name;
	}
	public int getId(){
		return this.id;
	}
	@Override
	public boolean equals(Object c){
		if(c instanceof Channel){
			return this.id==((Channel)c).getId();
		}else return false;
	}
}
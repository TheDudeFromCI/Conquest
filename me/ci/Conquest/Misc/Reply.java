package me.ci.Conquest.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.command.CommandSender;

public class Reply{
	private static Map<String,Reply> waiting = new HashMap<String,Reply>();
	private final Map<String,Runnable> responses;
	private int seconds;
	private Runnable timeout;
	private CommandSender p;
	private Timer t;
	public Reply(Map<String,Runnable> responses, CommandSender p, int time, Runnable timeout){
		if(waiting.containsKey(p.getName()));
		this.responses=responses;
		this.p=p;
		this.seconds=time;
		this.timeout=timeout;
		t=new Timer();
		t.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				seconds--;
				if(seconds==0){
					cancel();
					runTimeout();
					return;
				}
			}
		}, 1000, 1000);
	}
	public void runTimeout(){
		t.cancel();
		waiting.remove(p.getName());
		this.timeout.run();
	}
	public boolean hasResponse(String response){
		for(String s : responses.keySet()){
			if(s.equalsIgnoreCase(response))return true;
		}
		return false;
	}
	public void callResponse(String response){
		if(!hasResponse(response))return;
		t.cancel();
		waiting.remove(p.getName());
		getIgnoreCase(response).run();
	}
	private Runnable getIgnoreCase(String response){
		for(String s : responses.keySet()){
			if(s.equalsIgnoreCase(response))return responses.get(s);
		}
		return null;
	}
	public static boolean awaitingResponse(String name){
		return waiting.containsKey(name);
	}
	public static Reply getReply(String name){
		return waiting.get(name);
	}
	public Set<String> getResponses(){
		return responses.keySet();
	}
}
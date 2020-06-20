package me.ci.Conquest.Misc;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.command.CommandSender;

public class CallName implements Closeable{
	private static Map<String,LinkedList<CallName>> waiting = new HashMap<String,LinkedList<CallName>>();
	private String r;
	private int seconds;
	private Runnable timeout;
	private CommandSender p;
	private Timer t;
	private final String extra;
	public CallName(CommandSender p, int time, Runnable timeout, String extra){
		LinkedList<CallName> list;
		if(waiting.containsKey(p.getName()))list=waiting.get(p.getName());
		else list=new LinkedList<CallName>();
		list.add(this);
		waiting.put(p.getName(), list);
		this.p=p;
		this.seconds=time;
		this.timeout=timeout;
		this.extra=extra;
		t=new Timer();
		if(time>-1){
			t=new Timer();
			t.scheduleAtFixedRate(new TimerTask(){
				public void run(){
					seconds--;
					if(seconds==0){
						runTimeout();
						close();
						return;
					}
				}
			}, 1000, 1000);
		}
	}
	public void runTimeout(){ if(timeout!=null)timeout.run(); }
	public void close(){
		if(t!=null)t.cancel();
		waiting.get(p.getName()).remove(this);
	}
	public boolean hasResponed(){ return r!=null; }
	public String getName(){ return r; }
	public void setResponse(String r){ this.r=r; }
	public static boolean awaitingResponse(String name){
		try{ return !waiting.get(name).getLast().hasResponed();
		}catch(NullPointerException exception){ return false; }
	}
	public static CallName getNewestCallName(String name){
		try{ return waiting.get(name).getLast();
		}catch(Exception exception){
			removeSlot(name);
			return null;
		}
	}
	public boolean isClosed(){
		try{ return waiting.get(p.getName()).contains(this);
		}catch(NullPointerException exception){ return true; }
	}
	public String getExtra(){ return this.extra; }
	public static List<CallName> getExtras(String p, String extra){
		List<CallName> names = new ArrayList<CallName>();
		try{ for(CallName cn : waiting.get(p)){ if(cn.getExtra().equalsIgnoreCase(extra))names.add(cn); }
		}catch(NullPointerException exception){}
		return names;
	}
	public static void deleteExtras(String p, String extra){
		try{ for(CallName cn : waiting.get(p)){ if(cn.getExtra().equalsIgnoreCase(extra))cn.close(); }
		}catch(NullPointerException exception){}
	}
	public static List<CallName> getCallNames(String p){ return waiting.get(p); }
	public static void removeSlot(String p){ waiting.remove(p); }
}
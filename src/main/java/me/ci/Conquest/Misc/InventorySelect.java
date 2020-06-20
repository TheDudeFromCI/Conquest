package me.ci.Conquest.Misc;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventorySelect implements Closeable{
	private static Map<String,LinkedList<InventorySelect>> waiting = new HashMap<String,LinkedList<InventorySelect>>();
	private ItemStack r;
	private Player p;
	private Inventory i;
	private int slot;
	private final String extra;
	public InventorySelect(final Player p, final String extra, final Inventory in){
		synchronized(waiting){
			LinkedList<InventorySelect> list;
			if(waiting.containsKey(p.getName()))list=waiting.get(p.getName());
			else list=new LinkedList<InventorySelect>();
			list.add(this);
			waiting.put(p.getName(), list);
			this.p=p;
			this.extra=extra;
			i=in;
		}
	}
	public void close(){ waiting.get(p.getName()).remove(this); }
	public boolean hasResponed(){ return r!=null; }
	public ItemStack getItem(){ return r; }
	public void setResponse(final ItemStack r){ this.r=r; }
	public void setSlot(final int slot){ this.slot=slot; }
	public int getSlot(){ return slot; }
	public static boolean awaitingResponse(String name){
		try{ return !waiting.get(name).getLast().hasResponed();
		}catch(final NullPointerException exception){ return false; }
	}
	public static InventorySelect getNewestCallName(final String name){
		try{ return waiting.get(name).getLast();
		}catch(final Exception exception){
			removeSlot(name);
			return null;
		}
	}
	public boolean isClosed(){
		try{
			return waiting.get(p.getName()).contains(this);
		}catch(NullPointerException exception){
			return true;
		}
	}
	public String getExtra(){
		return this.extra;
	}
	public Inventory getInventory(){ return i; }
	public void setInventory(final Inventory in){ i=in; }
	public static List<InventorySelect> getExtras(String p, String extra){
		List<InventorySelect> names = new ArrayList<InventorySelect>();
		try{
			for(InventorySelect cn : waiting.get(p)){
				if(cn.getExtra().equalsIgnoreCase(extra))names.add(cn);
			}
		}catch(NullPointerException exception){}
		return names;
	}
	public static void deleteExtras(String p, String extra){
		try{
			for(InventorySelect cn : waiting.get(p)){
				if(cn.getExtra().equalsIgnoreCase(extra))cn.close();
			}
		}catch(NullPointerException exception){}
	}
	public static List<InventorySelect> getCallNames(String p){
		return waiting.get(p);
	}
	public static void removeSlot(String p){
		waiting.remove(p);
	}
}
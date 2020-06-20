package me.ci.Conquest.Misc;

import java.util.Arrays;

import me.ci.Community.Save;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Tool{
	private String name;
	private int pos;
	private final Material m;
	private final String p;
	private final char c;
	private final String[] lore;
	private final int number;
	private final String realname;
	public Tool(final Material mat, final String title, final String player, final char symbol, final int amount, final String... l){
		m=mat;
		p=player;
		c=symbol;
		lore=l;
		number=Math.max(amount, 1);
		realname=title;
		loadPos();
		updateName(title);
	}
	private void loadPos(){
		String in = Save.get("Players", p, "Items");
		if(in==null)in="!ZYXWVUTSRQPONMLKJIHGFEDCBA";
		pos=in.indexOf(c);
	}
	public ItemStack getItem(){
		final ItemStack item = new ItemStack(m, number);
		final ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	public void updateName(){ updateName(realname); }
	public void updateName(final String n){ name=n+"  "+ChatColor.YELLOW+"(Itembar #"+getItembar()+")"; }
	public Material getMaterial(){ return m; }
	public String getName(){ return name; }
	public int getItembar(){ return (int)(pos/9.0+1); }
	public int getPosition(){ return pos; }
	public char getSymbol(){ return c; }
	public int getAmount(){ return number; }
}
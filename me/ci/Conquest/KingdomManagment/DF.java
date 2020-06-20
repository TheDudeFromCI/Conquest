package me.ci.Conquest.KingdomManagment;

import me.ci.Community.Save;

import org.bukkit.entity.Player;


public enum DF{
	GOLD(0, "Gold", 1, "", null, true);
	private final int id;
	private final String name;
	private final int cost;
	private final String description;
	private final DonatorFeature feature;
	private final boolean rebuyable;
	private DF(final int a, final String b, final int c, final String d, final DonatorFeature e, final boolean f){
		id=a;
		name=b;
		cost=c;
		description=d;
		feature=e;
		rebuyable=f;
	}
	public boolean hasBoughtBefore(final String p){
		final String in = Save.get("Players", p, "DF-"+name);
		if(in==null)return false;
		return true;
	}
	public void buy(final Player p){
		final String in = Save.get("Players", p.getName(), "DF-"+name);
		final int last;
		if(in==null)last=0;
		else last=Integer.valueOf(in);
		Save.set("Players", p.getName(), "DF-"+name, String.valueOf(last+1));
		feature.buy(p);
	}
	public int getBuyCount(final String p){
		final String in = Save.get("Players", p, "DF-"+name);
		if(in==null)return 0;
		return Integer.valueOf(in);
	}
	public boolean isRebuyable(){ return rebuyable; }
	public int getCost(){ return cost; }
	public String getName(){ return name; }
	public boolean canBuy(final Player p){ return feature.canBuy(p); }
	public String getDescription(){ return description; }
	public int getId(){ return id; }
	public static DF getById(final int id){
		for(DF df : values())if(df.getId()==id)return df;
		return null;
	}
}
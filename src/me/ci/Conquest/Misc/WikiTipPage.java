package me.ci.Conquest.Misc;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.ChatColor;

import me.ci.Community.Save;

public class WikiTipPage{
	private final int page;
	private final ArrayList<WikiTip> tips;
	public WikiTipPage(final int pageid, final File f){
		page=pageid;
		tips=new ArrayList<>();
		final Map<String,String> paths = Save.getAllPaths("Resources", f.getName().substring(0, f.getName().length()-4));
		int id;
		String[] out;
		for(String s : paths.keySet()){
			id=Integer.valueOf(s);
			out=ChatColor.translateAlternateColorCodes('&', paths.get(s)).split("|");
			tips.add(new WikiTip(id, out[0], wordWrap(out[1], 30)));
		}
	}
	public int getPage(){ return page; }
	public ArrayList<WikiTip> getTips(){ return tips; }
	public WikiTip getTip(final int id){ return tips.get(id); }
	private static ArrayList<String> wordWrap(final String s, final int length){
		final ArrayList<String> lines = new ArrayList<>();
		final char[] chars = s.toCharArray();
		String current = "";
		String word = "";
		for(int i = 0; i<chars.length; i++){
			if(chars[i]==' '){
				if(current.length()+word.length()+1>length){
					if(current.isEmpty()){
						while(word.length()>=length){
							lines.add(word.substring(0, length));
							word=word.substring(length-1, word.length());
						}
					}else{
						lines.add(current);
						current=word;
						word="";
					}
				}else{
					current+=" "+word;
					while(current.startsWith(" "))current=current.substring(1);
					word="";
				}
			}else word+=chars[i];
		}
		if(current.length()+word.length()+1>length){
			if(current.isEmpty()){
				while(word.length()>=length){
					lines.add(word.substring(0, length));
					word=word.substring(length-1, word.length());
				}
				lines.add(word);
			}else{
				lines.add(current);
				current=word;
				word="";
				lines.add(current);
			}
		}else{
			current+=" "+word;
			while(current.startsWith(" "))current=current.substring(1);
			word="";
			lines.add(current);
		}
		return lines;
	}
	public class WikiTip{
		private final int id;
		private final String name;
		private final ArrayList<String> lore;
		public WikiTip(final int i, final String title, final ArrayList<String> lines){
			id=i;
			name=title;
			lore=lines;
		}
		public int getId(){ return id; }
		public String getName(){ return name; }
		public ArrayList<String> getLore(){ return lore; }
	}
}
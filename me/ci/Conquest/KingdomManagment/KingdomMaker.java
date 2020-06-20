package me.ci.Conquest.KingdomManagment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.ci.WhCommunity;
import me.ci.Community.PlayerMg;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Constructors.Flag;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.Misc.Achievement;
import me.ci.Conquest.Misc.CallName;
import me.ci.Conquest.NaturalResources.Country;
import me.ci.Conquest.NaturalResources.CountryMg;
import me.ci.Conquest.Textures.BuildingTexture;
import me.ci.Conquest.Buildings.Main.Capitol;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class KingdomMaker{
	public static List<String> creating = new LinkedList<String>();
	public static Map<String,Byte> chooseflag = new HashMap<String,Byte>();
	public static Map<String,Byte[]> colorflag = new HashMap<String,Byte[]>();
	private String kingdomname;
	private Flag flag;
	private String owner;
	private boolean king;
	private ResourceType resourcePreference;
	private LinkedList<String> searching = new LinkedList<>();
	private static final String breaker = ChatColor.DARK_GRAY+"===============================";
	public void createKingdom(final Player p, final World w){
		owner=p.getName();
		new Thread(new Runnable(){
			public void run(){
				if(WhCommunity.getWorldSettings(w.getName()).CONFIG_Conquest_Enabled){
					if(PlayerMg.getKingdom(p.getName())==null){
						p.teleport(new Location(w, 81.07389, 159, 7.9994, -270.0f, 0.0f));
						PlayerMg.resetConquestToolkit(p);
						creating.add(p.getName());
						ck1(p);
						if(creating.contains(p.getName()))ck2(p);
						if(creating.contains(p.getName()))ck3(p);
						if(creating.contains(p.getName()))ck4(p);
						if(creating.contains(p.getName()))ck5(p);
						if(creating.contains(p.getName()))ck6(p);
						CallName.deleteExtras(p.getName(), "KingdomMaker");
						creating.remove(p.getName());
					}
				}
			}
		}).start();
	}
	private void ck1(Player p){
		p.sendMessage(breaker);
		p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC.toString()+"Hazah! Welcome "+p.getName()+" to the realms of Conquest. I, sir Erdrick of the House of Devonel, have been instructed to train you on the ways of this world.");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"NEXT"+ChatColor.GRAY+" in chat to continue this tutorial.");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": We're sorry, but you can’t chat with other players until you have finished this Tutorial! Also, you don’t have to use all caps, but make sure your spelling the word NEXT correctly.)");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"I’m using a Laptop"+ChatColor.GRAY+": If you can’t scroll up and read the text of this tutorial, click your page-up and page-down keys.)");
		p.sendMessage(breaker);
		PlayerMg.resetConquestToolkit(p);
		CallName cn = new CallName(p, -1, null, "KingdomMaker");
		while(true){
			if(!creating.contains(p.getName())){
				cn.close();
				return;
			}
			try{ Thread.sleep(50);
			}catch(Exception exception){}
			if(cn.hasResponed()){
				String res = cn.getName();
				if(res.equalsIgnoreCase("next")){
					cn.close();
					p.sendMessage(breaker);
					p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC.toString()+"Before we begin playing, I should first explain this game to you. Conquest, is an MMORTS, a Massive Multiplayer Online Real Time Strategy Game. The overall goal, is to create and manage your very own kingdom, and survive.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"CONTINUE"+ChatColor.GRAY+" in chat to continue this tutorial.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Remember, you won’t be able to talk to other players until after this short tutorial. When you type next, it does not have to be in all caps, just spelled correctly.)");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"I’m using a Laptop"+ChatColor.GRAY+": Remember to use your page-up and page-down keys to read any text that scrolls up too high to read.)");
					p.sendMessage(breaker);
					final CallName cn1 = new CallName(p, 0, null, "KingdomMaker");
					while(true){
						try{ Thread.sleep(50);
						}catch(final Exception exception){}
						if(cn1.hasResponed()){
							if(cn1.getName().equalsIgnoreCase("continue")){
								cn1.close();
								p.sendMessage(breaker);
								p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC.toString()+"You survive by outwitting your enemies in combat, managing your kingdom well, and expanding your resources, and acquiring the most land.");
								p.sendMessage("");
								p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"GO"+ChatColor.GRAY+" in chat to continue this tutorial.");
								p.sendMessage("");
								p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": You still a few more questions to go before you can chat with other players. And remember to spell all the keywords correctly to continue.)");
								p.sendMessage("");
								p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
								p.sendMessage("");
								p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"I’m using a Laptop"+ChatColor.GRAY+": Remember to use your page-up and page-down keys to read chat.)");
								p.sendMessage(breaker);
								final CallName cn2 = new CallName(p, 0, null, "KingdomMaker");
								while(true){
									try{ Thread.sleep(50);
									}catch(final Exception exception){}
									if(cn2.hasResponed()){
										if(cn2.getName().equalsIgnoreCase("go")){
											cn2.close();
											return;
										}else{
											p.sendMessage(breaker);
											p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC.toString()+"This is starting to wear heavily on my patience. Please, just type "+ChatColor.GREEN+ChatColor.BOLD+"NEXT"+ChatColor.GRAY+ChatColor.ITALIC+", nothing more.");
											p.sendMessage("");
											p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"GO"+ChatColor.GRAY+" in chat to continue this tutorial.");
											p.sendMessage("");
											p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": You still a few more questions to go before you can chat with other players. And remember to spell all the keywords correctly to continue.)");
											p.sendMessage("");
											p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
											p.sendMessage("");
											p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"I’m using a Laptop"+ChatColor.GRAY+": Remember to use your page-up and page-down keys to read chat.)");
											p.sendMessage(breaker);
											cn2.setResponse(null);
										}
									}
									if(!creating.contains(p.getName())){
										cn2.close();
										return;
									}
								}
							}else{
								p.sendMessage(breaker);
								p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC.toString()+"Sorry, but I'm afraid you are having trouble understanding me. Just open your chat window and type the word "+ChatColor.GREEN+ChatColor.BOLD+"NEXT"+ChatColor.GRAY+ChatColor.ITALIC+". Don't add anything before it or after it. Just type Next.");
								p.sendMessage("");
								p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"CONTINUE"+ChatColor.GRAY+" in chat to continue this tutorial.");
								p.sendMessage("");
								p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Remember, you won’t be able to talk to other players until after this short tutorial.)");
								p.sendMessage("");
								p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
								p.sendMessage("");
								p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"I’m using a Laptop"+ChatColor.GRAY+": The page-up and page-down keys on your keyboard help scroll up through text you can’t see in chat.)");
								p.sendMessage(breaker);
								cn1.setResponse(null);
							}
						}
						if(!creating.contains(p.getName())){
							cn1.close();
							return;
						}
					}
				}else{
					p.sendMessage(breaker);
					p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC.toString()+"Sorry, but I think you have misunderstood me. When you are ready to listen, tell me by typing the word "+ChatColor.GREEN+ChatColor.BOLD+"NEXT"+ChatColor.GRAY+ChatColor.ITALIC+" into your chat window.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"NEXT"+ChatColor.GRAY+" in chat to continue this tutorial.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": We're sorry, but you can’t chat with other players until you have finished this Tutorial! Also, you don’t have to use all caps, but make sure your spelling the word NEXT correctly.)");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"I’m using a Laptop"+ChatColor.GRAY+": If you can’t scroll up and read the text of this tutorial, click your page-up and page-down keys.)");
					p.sendMessage(breaker);
					cn.setResponse(null);
				}
			}
			if(!creating.contains(p.getName())){
				cn.close();
				return;
			}
		}
	}
	private void ck2(Player p){
		p.sendMessage(breaker);
		p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC.toString()+"Alright let’s get started shall we. The first thing you need to do "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+ChatColor.ITALIC+", is to choose a name for your kingdom. I suggest choosing a unique medieval or fantasy name. So, what name shall I write down on the ancient scrolls.");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"Type your very own medieval fantasy name for your kingdom.");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": If you choose a name that contains foul language or symbols, insults another player in some way, copies another player's name, or violates any other Server rules, your kingdom will be deleted immediately, and you will be banned from the server.)");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
		p.sendMessage(breaker);
		CallName cn = new CallName(p, -1, null, "KingdomMaker");
		entername:while(true){
			if(!creating.contains(p.getName())){
				cn.close();
				return;
			}
			try{ Thread.sleep(50);
			}catch(Exception exception){}
			if(cn.hasResponed()){
				String name = cn.getName();
				name=publishName(name);
				if(KingdomMg.getKingdom(name)!=null){
					p.sendMessage(breaker);
					p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"I'm sorry, but there is already a kingdom by that name. Please chose another.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"Type your very own medieval fantasy name for your kingdom.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": If you choose a name that contains foul language or symbols, insults another player in some way, copies another player's name, or violates any other Server rules, your kingdom will be deleted immediately, and you will be banned from the server.)");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
					p.sendMessage(breaker);
					cn.setResponse(null);
					continue;
				}
				p.sendMessage(breaker);
				p.sendMessage(ChatColor.GRAY+"You have chosen "+ChatColor.GOLD+name);
				p.sendMessage("");
				p.sendMessage(ChatColor.GRAY+"Hmm, "+ChatColor.GOLD+name+ChatColor.GRAY+ChatColor.ITALIC+", I see, that does sound promising. Are you sure you want to keep this as your official kingdom name? Remember, this name can not be changed later, so choose wisely.");
				p.sendMessage("");
				p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"YES"+ChatColor.GRAY+" in chat, if you want to keep this name as your kingdom name.");
				p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"NO"+ChatColor.GRAY+" in chat, if you want to rename it to something else.");
				p.sendMessage("");
				p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
				p.sendMessage(breaker);
				CallName cn2 = new CallName(p, -1, null, "KingdomMaker");
				while(true){
					if(!creating.contains(p.getName())){
						cn.close();
						cn2.close();
						return;
					}
					try{
						Thread.sleep(50);
					}catch(Exception exception){}
					if(cn2.hasResponed()){
						String res = cn2.getName();
						if(res.equalsIgnoreCase("yes")){
							p.sendMessage(breaker);
							p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"YES");
							p.sendMessage("");
							p.sendMessage(ChatColor.GRAY+"Ok, I have written it down. In fact I’m sure it won’t be long before we can hear the name "+ChatColor.GOLD+name+ChatColor.GRAY+ChatColor.ITALIC+" being chanted from rooftops across your lands. Now it’s time to choose a flag that will best represent your kingdom. Move toward the 5 flag types, and look over each one carefully. As you can see, they are listed as: "+ChatColor.GREEN+ChatColor.BOLD+"BANNER"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"WEIGHTED BANNER"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"MODERN"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"GONFALON"+ChatColor.GRAY+ChatColor.ITALIC+", and "+ChatColor.GREEN+ChatColor.BOLD+"PENNON"+ChatColor.GRAY+ChatColor.ITALIC+". To make your choice, simply punch the white wool of the banner you want. Please punch, the white wool of the flag you want to use for your new kingdom.");
							p.sendMessage("");
							p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Don’t right-click! Instead, left-click or punch the white wool of the flag type you want to keep as your flag.)");
							p.sendMessage("");
							p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
							p.sendMessage(breaker);
							this.kingdomname=name;
							cn.close();
							cn2.close();
							return;
						}else if(res.equalsIgnoreCase("no")){
							cn.setResponse(null);
							cn2.close();
							p.sendMessage(breaker);
							p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"NO");
							p.sendMessage("");
							p.sendMessage(ChatColor.GRAY+"So you have changed you mind I see. Well, good thing I had not added any ink to this quill yet. Ok "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+ChatColor.ITALIC+", please tell me what name you wish to officially have as your new kingdom name.");
							p.sendMessage("");
							p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
							p.sendMessage(breaker);
							continue entername;
						}else{
							p.sendMessage(breaker);
							p.sendMessage(ChatColor.GOLD+p.getName()+ChatColor.GRAY+ChatColor.ITALIC+", you seem very distracted, or perhaps you're not as educated a noble as first thought. Please just type "+ChatColor.GREEN+ChatColor.BOLD+"YES"+ChatColor.GRAY+ChatColor.ITALIC+" if you want to keep "+ChatColor.GOLD+name+ChatColor.GRAY+ChatColor.ITALIC+" as your official kingdom name, or type "+ChatColor.GREEN+ChatColor.BOLD+"NO"+ChatColor.GRAY+ChatColor.ITALIC+" if you want to change it.");
							p.sendMessage("");
							p.sendMessage(ChatColor.GRAY+"Hmm, "+ChatColor.GOLD+name+ChatColor.GRAY+ChatColor.ITALIC+", I see, that does sound promising. Are you sure you want to keep this as your official kingdom name? Remember, this name can not be changed later, so choose wisely.");
							p.sendMessage("");
							p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"YES"+ChatColor.GRAY+" in chat, if you want to keep this name as your kingdom name.");
							p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"NO"+ChatColor.GRAY+" in chat, if you want to rename it to something else.");
							p.sendMessage("");
							p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
							p.sendMessage(breaker);
							cn2.setResponse(null);
						}
					}
				}
			}
		}
	}
	private static String publishName(String name){
		name=name.toLowerCase();
		name=name.replaceAll("_", " ");
		while(name.startsWith(" "))name=name.substring(1);
		while(name.endsWith(" "))name=name.substring(0, name.length()-1);
		StringBuilder s = new StringBuilder(name);
		for(int i = 0; i<name.length(); i++){
			char c = name.charAt(i);
			if(!(c+"").matches("[A-Za-z0-9 ]"))c=' ';
			if(i>0){
				char b = s.charAt(i-1);
				if(b==' ')c=Character.toUpperCase(c);
			}else c=Character.toUpperCase(c);
			s.setCharAt(i, c);
		}
		name=s.toString();
		name=name.replaceAll("\\s+", " ");
		name=name.replace(" Of ", " of ");
		name=name.replace(" And ", " and ");
		name=name.replace(" The ", " the ");
		name=name.replace(" By ", " by ");
		name=name.replace(" A ", " a ");
		name=name.replace(" Is ", " is ");
		return name;
	}
	private void ck3(Player p){
		chooseflag.put(p.getName(), null);
		while(chooseflag.get(p.getName())==null){
			if(!creating.contains(p.getName())){
				chooseflag.remove(p.getName());
				return;
			}
			try{ Thread.sleep(50);
			}catch(Exception exception){}
		}
		byte flagid = chooseflag.get(p.getName());
		chooseflag.remove(p.getName());
		p.sendMessage(breaker);
		p.sendMessage(ChatColor.GRAY+"You have chosen a: "+ChatColor.GOLD+Flag.getName(flagid)+ChatColor.GRAY+ChatColor.ITALIC+" flag type.");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"I'm sure the "+ChatColor.GOLD+Flag.getName(flagid)+ChatColor.GRAY+ChatColor.ITALIC+" flag would look even more glorious above your throne, if it only had some color. Try punching the white wool with the colors you like most. Be creative, and remember, if someone else has already used that design, you might have to change their position. When your done coloring your flag, type "+ChatColor.GREEN+ChatColor.BOLD+"DONE"+ChatColor.GRAY+ChatColor.ITALIC+".");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": To color your flag, scroll past the last ink color using the wheel of your mouse. The Itembar will automatically switch to the next set of colors. Punch with the water bucket if you need to reset  your colors. Punch with the lava bucket to choose random colors.)");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"I’m using a Laptop"+ChatColor.GRAY+": You can scroll to the next Itembar by pushing the number keys in this order, 5, then 9, then 1. Each time you type 591, you will switch to the next Itembar. Later you will be able to organize your Itembars the way you want.)");
		p.sendMessage(breaker);
		Byte[] colors = new Byte[Flag.getWoolBlocks(flagid)+1];
		colors[0]=flagid;
		for(int w = 1; w<colors.length; w++){
			colors[w]=new Byte((byte)0);
		}
		colorflag.put(p.getName(), colors);
		CallName cn = new CallName(p, -1, null, "KingdomMaker");
		while(true){
			if(!creating.contains(p.getName())){
				cn.close();
				return;
			}
			try{
				Thread.sleep(50);
			}catch(Exception exception){}
			if(cn.hasResponed()){
				if(cn.getName().equalsIgnoreCase("done")){
					Byte[] flagcolors = colorflag.get(p.getName());
					if(!Flag.exists(flagcolors)){
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY+"You have typed: "+ChatColor.GREEN+ChatColor.BOLD+"DONE");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"Those are some majestic colors indeed "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+ChatColor.ITALIC+". I'm sorry, but the light is giving off such a glare, it’s making quite difficult for me to tell what you look like. I don’t mean to offend, but I need written down here, a record of how you wish to be known, as either a King or a Queen.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"KING"+ChatColor.GRAY+" in chat, If you want to be known as the Lord "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+" of "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+".");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"QUEEN"+ChatColor.GRAY+" in chat, If you want to be known as the Lady "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+" of "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+".");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Choose wisely! Events and story scenarios throughout the game will change depending on your answer here.)");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
						cn.close();
						colorflag.remove(p.getName());
						this.flag=new Flag(flagcolors);
						return;
					}else{
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"Oh no! It looks like someone else already has a flag that looks like that. Try another design.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+"Type the word "+ChatColor.GREEN+ChatColor.BOLD+"DONE"+ChatColor.GRAY+" if you're finished coloring your flag.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
						cn.setResponse(null);
					}
				}else{
					p.sendMessage(breaker);
					p.sendMessage(ChatColor.GOLD+p.getName()+ChatColor.GRAY+ChatColor.ITALIC+", its really not that difficult to type the word "+ChatColor.GREEN+ChatColor.BOLD+"DONE"+ChatColor.GRAY+ChatColor.ITALIC+". Honestly, I am starting to believe you might not be as successful at managing a kingdom as we all had hoped.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY.toString()+"Type the word "+ChatColor.GREEN+ChatColor.BOLD+"DONE"+ChatColor.GRAY+" if you're finished coloring your flag.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
					p.sendMessage(breaker);
					cn.setResponse(null);
				}
			}
		}
	}
	public String getKingdomName(){
		return this.kingdomname;
	}
	public Flag getFlag(){
		return this.flag;
	}
	public String getOwner(){
		return this.owner;
	}
	private void ck4(Player p){
		CallName cn = new CallName(p, -1, null, "KingdomMaker");
		while(true){
			if(!creating.contains(p.getName())){
				cn.close();
				return;
			}
			try{ Thread.sleep(50);
			}catch(Exception exception){}
			if(cn.hasResponed()){
				if(cn.getName().equalsIgnoreCase("king")){
					p.sendMessage(breaker);
					p.sendMessage(ChatColor.GRAY+"You have typed: "+ChatColor.GREEN+ChatColor.BOLD+"KING");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"I have written it down, and I must say, that it is an honor to meet you my Lord "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+ChatColor.ITALIC+". I do have but one more question to ask before we can begin. You must choose a major resource that your kingdom thrives from. Be it "+ChatColor.GREEN+ChatColor.BOLD+"FOOD"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"WOOD"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"STONE"+ChatColor.GRAY+ChatColor.ITALIC+", or "+ChatColor.GREEN+ChatColor.BOLD+"IRON"+ChatColor.GRAY+ChatColor.ITALIC+". So, which of these 4 resources would you like to be found most in your kingdom?");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"FOOD"+ChatColor.GRAY+", if you want Food to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"WOOD"+ChatColor.GRAY+", if you want Wood to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"STONE"+ChatColor.GRAY+", if you want Stone to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"IRON"+ChatColor.GRAY+", if you want Iron to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Think of these as game difficulty. Choosing Food will be the easiest to play, and Iron being more difficult.)");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
					p.sendMessage(breaker);
					cn.close();
					this.king=true;
					return;
				}else if(cn.getName().equalsIgnoreCase("queen")){
					p.sendMessage(breaker);
					p.sendMessage(ChatColor.GRAY+"You have typed: "+ChatColor.GREEN+ChatColor.BOLD+"QUEEN");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"There, I have added that to the scrolls. Let me also add my Lady %PLAYER%, that it is a pleasure to be in your presence. I do have but one more question to ask before we can begin. You must choose a major resource that your kingdom thrives from. Be it "+ChatColor.GREEN+ChatColor.BOLD+"FOOD"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"WOOD"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"STONE"+ChatColor.GRAY+ChatColor.ITALIC+", or "+ChatColor.GREEN+ChatColor.BOLD+"IRON"+ChatColor.GRAY+ChatColor.ITALIC+". So, which of these 4 resources would you like to be found most in your kingdom?");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"FOOD"+ChatColor.GRAY+", if you want Food to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"WOOD"+ChatColor.GRAY+", if you want Wood to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"STONE"+ChatColor.GRAY+", if you want Stone to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"IRON"+ChatColor.GRAY+", if you want Iron to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Think of these as game difficulty. Choosing Food will be the easiest to play, and Iron being more difficult.)");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
					p.sendMessage(breaker);
					cn.close();
					this.king=false;
					return;
				}else if(cn.getName().equalsIgnoreCase("other")
						||cn.getName().equalsIgnoreCase("both")
						||cn.getName().equalsIgnoreCase("neither")){
					p.sendMessage(breaker);
					p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"I see, yes well perhaps this is something we should keep between us. This is not the kind of gossip you want stirred up among the people. It might seem unethical to make such an announcement about your,.. um,.. well,... situation. Of course I mean no disrespect, though a noble of high stature such as yourself, should use caution when selecting the right right words so’s not to create restlessness within one’s kingdom. For now, may I suggest that you choose either "+ChatColor.GREEN+ChatColor.BOLD+"KING"+ChatColor.GRAY+ChatColor.ITALIC+" or "+ChatColor.GREEN+ChatColor.BOLD+"QUEEN"+ChatColor.GRAY+ChatColor.ITALIC+".");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"KING"+ChatColor.GRAY+" in chat, If you want to be known as the King "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+".");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"QUEEN"+ChatColor.GRAY+" in chat, If you want to be known as the Queen "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+".");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Choose wisely! Events and story scenarios throughout the game will change depending on your answer here.)");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
					p.sendMessage(breaker);
					cn.setResponse(null);
				}else{
					p.sendMessage(breaker);
					p.sendMessage(ChatColor.GOLD+p.getName()+ChatColor.GRAY+ChatColor.ITALIC+", have you already forgotten what you look like, or have you been burdened by unsightly features and wish not to be reminded? Perhaps looking at your reflection once more may help, unless of course you think it wiser to keep your hood up during the day. For the record however, I must insist that you type "+ChatColor.GREEN+ChatColor.BOLD+"KING"+ChatColor.GRAY+ChatColor.ITALIC+" if you want to be known as a King, or type "+ChatColor.GREEN+ChatColor.BOLD+"QUEEN"+ChatColor.GRAY+ChatColor.ITALIC+" if you want to be known as a Queen.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"KING"+ChatColor.GRAY+" in chat, If you want to be known as the King "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+".");
					p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"QUEEN"+ChatColor.GRAY+" in chat, If you want to be known as the Queen "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+".");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Choose wisely! Events and story scenarios throughout the game will change depending on your answer here.)");
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
					p.sendMessage(breaker);
					cn.setResponse(null);
				}
			}
		}
	}
	public boolean isAKing(){
		return this.king;
	}
	private void ck5(Player p){
		CallName cn = new CallName(p, -1, null, "KingdomMaker");
		while(true){
			if(!creating.contains(p.getName())){
				cn.close();
				return;
			}
			try{ Thread.sleep(50);
			}catch(Exception exception){}
			if(cn.hasResponed()){
				if(cn.getName().equalsIgnoreCase("wood")){
					if(king){
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"WOOD");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"My Lord "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+ChatColor.ITALIC+", you have chosen well. May I ask that you wait here for a bit, while I get your kingdom ready for your arrival.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}else{
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"WOOD");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"I see my Lady "+ChatColor.GOLD+p.getName()+ChatColor.GRAY+ChatColor.ITALIC+", wood is a fairly good choice .Would you mind waiting over here a moment while I get your kingdom estate prepared.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}
					cn.close();
					this.resourcePreference=ResourceType.WOOD;
					return;
				}else if(cn.getName().equalsIgnoreCase("iron")){
					if(king){
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"IRON");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"Very well my Lord, Iron it should be then. Now, would you mind waiting a moment my Lord, until I have fully prepared "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+" for you.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}else{
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"IRON");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"A true maiden of Iron I’m sure my Lady. Please allow me a few moments to ready your estate.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}
					cn.close();
					this.resourcePreference=ResourceType.IRON;
					return;
				}else if(cn.getName().equalsIgnoreCase("stone")){
					if(king){
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"STONE");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"Did you say stone my Lord, ah good. Now, please allow me a moment or two to get your kingdom set up before I take you to "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+".");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}else{
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"STONE");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"Did I hear you right my Lady? Stone? A wise choice indeed. It will take me a few moments to get the kingdom of "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+" prepared for your arrival.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}
					cn.close();
					this.resourcePreference=ResourceType.STONE;
					return;
				}else if(cn.getName().equalsIgnoreCase("food")){
					if(king){
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"FOOD");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"Food, a wise choice indeed my Lord. I am sure your villagers will appreciate your kindness. Alright, my Lord, if you would please wait here, I need to complete the preparations for "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+".");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}else{
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY+"You have answered: "+ChatColor.GREEN+ChatColor.BOLD+"FOOD");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"My Lady, surely you have chosen mercifully. Will you excuse me for a moment my Lady while I go and prepare the kingdom of "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+" for you.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}
					cn.close();
					this.resourcePreference=ResourceType.FOOD;
					return;
				}else{
					if(king){
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"My Lord  %PLAYER%, it has become clear to me that you are not taking this position seriously. Perhaps being a noble was not the right path for you. However, if you are still interested in pursuing a place of nobility, try typing one of these main resources "+ChatColor.GREEN+ChatColor.BOLD+"FOOD"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"WOOD"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"STONE"+ChatColor.GRAY+ChatColor.ITALIC+", or "+ChatColor.GREEN+ChatColor.BOLD+"IRON"+ChatColor.GRAY+ChatColor.ITALIC+".");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"FOOD"+ChatColor.GRAY+", if you want Food to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"WOOD"+ChatColor.GRAY+", if you want Wood to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"STONE"+ChatColor.GRAY+", if you want Stone to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"IRON"+ChatColor.GRAY+", if you want Iron to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Think of these as game difficulty. Choosing Food will be the easiest to play, and Iron being more difficult.)");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}else{
						p.sendMessage(breaker);
						p.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"Lady %PLAYER%, perhaps if you accept your noble position with passion instead of with foolishness, the people of your lands will live better lives, and feel honored to be in your service. Please my Lady, could you just choose one of these main resources "+ChatColor.GREEN+ChatColor.BOLD+"FOOD"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"WOOD"+ChatColor.GRAY+ChatColor.ITALIC+", "+ChatColor.GREEN+ChatColor.BOLD+"STONE"+ChatColor.GRAY+ChatColor.ITALIC+", or "+ChatColor.GREEN+ChatColor.BOLD+"IRON"+ChatColor.GRAY+ChatColor.ITALIC+".");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"FOOD"+ChatColor.GRAY+", if you want Food to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"WOOD"+ChatColor.GRAY+", if you want Wood to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"STONE"+ChatColor.GRAY+", if you want Stone to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
						p.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+ChatColor.BOLD+"IRON"+ChatColor.GRAY+", if you want Iron to be "+ChatColor.GOLD+kingdomname+ChatColor.GRAY+ChatColor.ITALIC+"'s major collecting and trading resource.");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Think of these as game difficulty. Choosing Food will be the easiest to play, and Iron being more difficult.)");
						p.sendMessage("");
						p.sendMessage(ChatColor.GRAY+"("+ChatColor.BOLD+"HINT"+ChatColor.GRAY+": Click here to watch a video on how to create a kingdom. "+ChatColor.YELLOW+"http://www.youtube.com/watch?v=MdTHvusX81c"+ChatColor.RED+")");
						p.sendMessage(breaker);
					}
					cn.setResponse(null);
				}
			}
		}
	}
	public ResourceType getResourcePreference(){
		return this.resourcePreference;
	}
	private void ck6(Player p){
		searching.add(p.getName());
		while(searching.getFirst()!=p.getName()){
			try{ Thread.sleep(10);
			}catch(final Exception exception){}
		}
		Country country = CountryMg.getNextOpenCountry(PlayerMg.getPreviousKingdomLocations(p.getName()), p.getWorld());
		Kingdom kingdom = new Kingdom(getOwner(), getKingdomName(), p.getWorld(), resourcePreference, king);
		kingdom.setFlag(getFlag());
		kingdom.addMoney(20000);
		final Chunk chunk = country.getProvinceSection(1, 1);
		final Chunk c = p.getWorld().getChunkAt(chunk.getX()+10, chunk.getZ()+10);
		new Capitol(c, kingdom, new BuildingTexture());
		int h = (int)(Math.random()*20+1);
		PlotMg.getPlotAt(c).setHeight(h, true);
		p.teleport(c.getBlock(0, 100, 0).getLocation());
		kingdom.addResources(ResourceType.FOOD, 800);
		kingdom.addVillagers(25);
		PlayerMg.setKingdom(p.getName(), kingdomname);
		KingdomMg.saveKingdoms();
		PlayerMg.resetConquestToolkit(p);
		searching.remove(p.getName());
		kingdom.awardAchievement(Achievement.CREATING_A_KINGDOM);
		PlayerMg.setKingdomCreateTime(p.getName());
		System.out.println("[Conquest] Creating kingdom: "+getKingdomName()+", by "+p.getName()+".");
	}
}
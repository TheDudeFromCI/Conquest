package me.ci.Community;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import me.ci.WhCommunity;
import me.ci.Conquest.KingdomManagment.KingdomMg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Save{
	private static Timer t;
	public static Map<String,Map<String,String>> ufPlots = new HashMap<String,Map<String,String>>();
	public static Map<String,Map<String,String>> ufPlayers = new HashMap<String,Map<String,String>>();
	public static Map<String,Map<String,String>> ufBuildings = new HashMap<String,Map<String,String>>();
	public static Map<String,Map<String,String>> ufKingdoms = new HashMap<String,Map<String,String>>();
	public static Map<String,Map<String,String>> ufResources = new HashMap<String,Map<String,String>>();
	public static void startSaveTimer(){
		if(t!=null)t.cancel();
		t=new Timer();
		t.scheduleAtFixedRate(new TimerTask(){
			byte min = 0;
			public void run(){
				try{
					min++;
					if(min==5){
						Bukkit.getScheduler().runTask(WhCommunity.plugin, new Runnable(){
							public void run(){
								Bukkit.broadcastMessage(ChatColor.RED+"[WhCommunity] "+ChatColor.DARK_AQUA+"Saving files...");
								long time = System.currentTimeMillis();
								Save.saveAll();
								System.out.println("[WhCommunity] Saved files. Took "+(System.currentTimeMillis()-time)+" millisecond(s).");
								Bukkit.broadcastMessage(ChatColor.RED+"[WhCommunity] "+ChatColor.DARK_AQUA+"Saved!");
							}
						});
						min=0;
					}
				}catch(Exception exception){ exception.printStackTrace(); }
			}
		}, 60000, 60000);
	}
	public synchronized static void saveFiles(Map<String,Map<String,String>> usedFiles, String folder){
		if(usedFiles!=null){
			try{
				for(String f : usedFiles.keySet()){
					try{
						File file = new File(WhCommunity.folder.getAbsolutePath()+File.separatorChar+folder, f+".txt");
						if(!file.exists()){
							file.getParentFile().mkdirs();
							InputStream in = new ByteArrayInputStream("".getBytes());
							OutputStream out = new FileOutputStream(file);
					        byte[] buf = new byte[1024];
							int a;
							while((a=in.read(buf))>0)out.write(buf,0,a);
							in.close();
							out.close();
						}
						List<String> lines = new ArrayList<String>();
						Map<String,String> v = usedFiles.get(f);
						Set<String> values = v.keySet();
						BufferedReader in = new BufferedReader(new FileReader(file));
						String i;
						while((i=in.readLine())!=null){
							boolean free = true;
							isFree:for(String s : values){
								if(i.startsWith("'"+s+"':")){
									free=false;
									break isFree;
								}
							}
							if(free)lines.add(i);
						}
						in.close();
						BufferedWriter out = new BufferedWriter(new FileWriter(file));
						for(String s : values){
							String s1 = v.get(s);
							if(s1==null)continue;
							if(s1.equals("null"))continue;
							if(s1.isEmpty())continue;
							out.write("'"+s+"':"+s1);
							out.newLine();
						}
						for(String s : lines){
							if(s==null)continue;
							if(s.equals("null"))continue;
							if(s.isEmpty())continue;
							out.write(s);
							out.newLine();
						}
						out.flush();
						out.close();
					}catch(IOException e){
						System.err.println("[WhCommunity] Error saving file "+f+".txt!");
						e.printStackTrace();
					}
				}
			}catch(Exception e){
				System.err.println("[WhCommunity] Error saving files!");
				e.printStackTrace();
			}
			usedFiles.clear();
		}
	}
	public static void safeShutDown(){
		long time = System.currentTimeMillis();
		KingdomMg.saveKingdoms();
		PlotMg.savePlots();
		saveFiles(Save.ufPlots, "Plots");
		saveFiles(Save.ufPlayers, "Players");
		saveFiles(Save.ufBuildings, "Buildings");
		saveFiles(Save.ufKingdoms, "Kingdoms");
		saveFiles(Save.ufResources, "Resources");
		if(t!=null)t.cancel();
		System.out.println("[WhCommunity] Saved files. Took "+(System.currentTimeMillis()-time)+" millisecond(s).");
	}
	public synchronized static void set(String folder, String file, String path, String value){
		Map<String,Map<String,String>> usedFiles = null;
		if(folder.equals("Plots"))usedFiles=Save.ufPlots;
		if(folder.equals("Players"))usedFiles=Save.ufPlayers;
		if(folder.equals("Buildings"))usedFiles=Save.ufBuildings;
		if(folder.equals("Kingdoms"))usedFiles=Save.ufKingdoms;
		if(folder.equals("Resources"))usedFiles=Save.ufResources;
		if(usedFiles==null)usedFiles=new HashMap<String,Map<String,String>>();
		Map<String,String> a;
		if(usedFiles.containsKey(file))a=usedFiles.get(file);
		else a=new HashMap<String,String>();
		a.put(path, value);
		usedFiles.put(file, a);
	}
	public static String get(String folder, String file, String path){
		Map<String,Map<String,String>> usedFiles = null;
		if(folder.equals("Plots"))usedFiles=Save.ufPlots;
		if(folder.equals("Players"))usedFiles=Save.ufPlayers;
		if(folder.equals("Buildings"))usedFiles=Save.ufBuildings;
		if(folder.equals("Kingdoms"))usedFiles=Save.ufKingdoms;
		if(folder.equals("Resources"))usedFiles=Save.ufResources;
		if(usedFiles==null)usedFiles=new HashMap<String,Map<String,String>>();
		if(usedFiles.containsKey(file)){
			Map<String,String> a = usedFiles.get(file);
			if(a.containsKey(path))return a.get(path);
		}
		try{
			File f = new File(WhCommunity.folder.getAbsolutePath()+File.separatorChar+folder, file+".txt");
			BufferedReader in = new BufferedReader(new FileReader(f));
			String i;
			while((i=in.readLine())!=null){
				if(i.startsWith("'"+path+"':")){
					in.close();
					try{
						String[] s = i.split("':");
						if(s.length==1)return null;
						if(s.length>2)return null;
						if(s[1].equals("null"))return null;
						Map<String,String> a;
						if(usedFiles.containsKey(file))a=usedFiles.get(file);
						else a=new HashMap<String,String>();
						a.put(path, s[1]);
						usedFiles.put(file, a);
						return s[1];
					}catch(Exception e){
						System.err.println("[WhCommunity] Error trying to load file "+file+".txt!");
						e.printStackTrace();
						return null;
					}
				}
			}
			in.close();
		}catch(FileNotFoundException exception){
		}catch(IOException e){
			System.err.println("[WhCommunity] Error trying to load file "+file+".txt!");
			e.printStackTrace();
		}
		return null;
	}
	public static String getOnce(String folder, String file, String path){
		try{
			File f = new File(WhCommunity.folder.getAbsolutePath()+File.separatorChar+folder, file+".txt");
			BufferedReader in = new BufferedReader(new FileReader(f));
			String i;
			while((i=in.readLine())!=null){
				if(i.startsWith("'"+path+"':")){
					in.close();
					try{
						String[] s = i.split("':");
						if(s.length==1)return null;
						if(s[1].equals("null"))return null;
						return s[1];
					}catch(Exception e){
						System.err.println("[WhCommunity] Error trying to load file "+file+".txt!");
						e.printStackTrace();
						return null;
					}
				}
			}
			in.close();
		}catch(FileNotFoundException exception){
		}catch(IOException e){
			System.err.println("[WhCommunity] Error trying to load file "+file+".txt!");
			e.printStackTrace();
		}
		return null;
	}
	public static List<String> getFileNames(String folder){
		File f = new File(WhCommunity.folder.getAbsolutePath()+File.separatorChar+folder);
		if(!f.isDirectory())return new ArrayList<String>();
		List<String> files = new ArrayList<String>();
		for(File fi : f.listFiles()){
			String name = fi.getName();
			if(name.contains("."))files.add(name.substring(0, name.length()-4));
			else files.add(name);
		}
		return files;
	}
	public static List<File> getFiles(String folder){
		File f = new File(WhCommunity.folder.getAbsolutePath()+File.separatorChar+folder);
		if(!f.isDirectory())return new ArrayList<File>();
		List<File> files = new ArrayList<>();
		for(File fi : f.listFiles()){ files.add(fi); }
		return files;
	}
	public static Map<String,String> getAllPaths(String folder, String file){
		Map<String,String> paths = new HashMap<String,String>();
		Map<String,Map<String,String>> usedFiles = null;
		if(folder.equals("Plots"))usedFiles=Save.ufPlots;
		if(folder.equals("Players"))usedFiles=Save.ufPlayers;
		if(folder.equals("Buildings"))usedFiles=Save.ufBuildings;
		if(folder.equals("Kingdoms"))usedFiles=Save.ufKingdoms;
		if(folder.equals("Resources"))usedFiles=Save.ufResources;
		if(usedFiles==null)usedFiles=new HashMap<String,Map<String,String>>();
		try{
			File f = new File(WhCommunity.folder.getAbsolutePath()+File.separatorChar+folder, file+".txt");
			BufferedReader in = new BufferedReader(new FileReader(f));
			String i;
			while((i=in.readLine())!=null){
				try{
					String[] s = i.split("':");
					if(s.length==1)continue;
					if(s.length>2)continue;
					if(s[1].equals("null"))continue;
					s[0]=s[0].substring(1);
					paths.put(s[0], s[1]);
				}catch(Exception e){
					System.err.println("[WhCommunity] Error trying to load file "+file+".txt!");
					e.printStackTrace();
					in.close();
					return paths;
				}
			}
			in.close();
			return paths;
		}catch(FileNotFoundException exception){
		}catch(IOException e){
			System.err.println("[WhCommunity] Error trying to load file "+file+".txt!");
			e.printStackTrace();
		}
		return paths;
	}
	private static File getFile(final String folder, final String file, final boolean exe){
		try{
			File f = new File(WhCommunity.folder.getAbsolutePath()+File.separatorChar+folder, file+(exe?".txt":""));
			if(!f.exists()){
				if(f.getName().lastIndexOf(".")==-1)f.mkdirs();
				else f.createNewFile();
			}
			return f;
		}catch(Exception exception){
			System.err.println("[WhCommunity] Error trying to load file "+file+".txt!");
			exception.printStackTrace();
			return null;
		}
	}
	public static File getAbsoluteFile(final String folder, final String file){ return getFile(folder, file, false); }
	public static File getFile(final String folder, final String file){ return getFile(folder, file, true); }
	public static void saveAll(){
		KingdomMg.saveKingdoms();
		PlotMg.savePlots();
		Save.saveFiles(Save.ufPlots, "Plots");
		Save.saveFiles(Save.ufPlayers, "Players");
		Save.saveFiles(Save.ufBuildings, "Buildings");
		Save.saveFiles(Save.ufKingdoms, "Kingdoms");
		Save.saveFiles(Save.ufResources, "Resources");
	}
}
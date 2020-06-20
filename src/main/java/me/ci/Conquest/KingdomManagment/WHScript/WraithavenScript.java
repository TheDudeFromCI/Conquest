package me.ci.Conquest.KingdomManagment.WHScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import me.ci.Community.Save;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import me.ci.Conquest.KingdomManagment.WHScript.ActionMg.ActionLineFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class WraithavenScript{
	private boolean cracked = false;
	private final CodeBracket code = new CodeBracket();
	private final HashMap<String,ClassType> classes = new HashMap<>();
	private final EventProperties props;
	private final HashMap<String,String> settings = new HashMap<>();
	private final int id;
	private static final HashMap<String,ClassType> globleclasses = new HashMap<>();
	public WraithavenScript(final File event, final EventProperties properties){
		props=properties;
		id=Integer.valueOf(event.getName().substring(0, event.getName().length()-8));
		try{
			if(!event.exists())return;
			final BufferedReader in = new BufferedReader(new FileReader(event));
			String s;
			int lines = 0;
			LinkedList<CodeBracket> c = new LinkedList<>();
			c.add(code);
			while((s=in.readLine())!=null){
				try{
					s=s.trim().replace("\\s+", "");
					lines++;
					if(s.startsWith("if(")){
						String name = s.substring(3, s.length()-2);
						ClassType bool = getVariable(name);
						if(!(bool instanceof BooleanClass))throw new Exception(name+" is not a boolean type class.");
						CodeBracket co = new IfStatement((BooleanClass)bool);
						c.getLast().addAction(co);
						c.add(co);
					}else if(s.startsWith("while(")){
							String name = s.substring(6, s.length()-2);
							ClassType bool = getVariable(name);
							if(!(bool instanceof BooleanClass))throw new Exception(name+" is not a boolean type class.");
							CodeBracket co = new WhileStatement((BooleanClass)bool);
							c.getLast().addAction(co);
							c.add(co);
					}else if(s.startsWith("}else{")){
						if(!(c.getLast() instanceof IfStatement))throw new Exception("Broken if statement.");
						IfStatement ifs = (IfStatement)c.getLast();
						if(!ifs.creatingthen)throw new Exception("Broken if statement.");
						ifs.creatingthen=false;
					}else if(s.startsWith("}"))c.removeLast();
					else if(s.startsWith("var ")){
						final String[] s1 = s.substring(4, s.length()).split(":");
						classes.put(s1[1], generateClassType(s1[0]));
					}else if(s.startsWith("SETTING:")){
						String[] split = s.substring(8).split("=");
						settings.put(split[0], split[1]);
					}else c.getLast().addAction(ActionMg.getAction(this, s, lines));
				}catch(Exception exception){
					System.err.println("Error parsing event code! Line: "+lines+" is broken.\n"+exception.getMessage());
					cracked=true;
				}
			}
			in.close();
		}catch(Exception exception){
			exception.printStackTrace();
			cracked=true;
		}
	}
	public ClassType getVariable(final String name){
		if(classes.containsKey(name))return classes.get(name);
		if(props.k!=null&&props.k.getClasses().containsKey(name))return props.k.getClasses().get(name);
		if(globleclasses.containsKey(name))return globleclasses.get(name);
		return null;
	}
	public String getSetting(final String name){
		if(!settings.containsKey(name))return null;
		return settings.get(name);
	}
	public void run(){ if(!cracked)code.run(); }
	public Player getPlayer(){ return props.getPlayer(); }
	public Kingdom getKingdom(){ return props.getKingdom(); }
	public Building getBuilding(){ return props.getBuilding(); }
	public int getId(){ return id; }
	public static void loadGlobleClasses(){
		final Map<String,String> paths = Save.getAllPaths("Resources", "Globle Classes");
		for(String s : paths.keySet()){
			try{
				String[] names = s.split(":");
				ClassType cl = WraithavenScript.generateClassType(names[0]);
				globleclasses.put(names[1], cl);
				cl.setValueFromString(paths.get(s));
			}catch(final Exception exception){ exception.printStackTrace(); }
		}
	}
	public static void saveGlobleClasses(){
		ClassType cl;
		for(String s : globleclasses.keySet()){
			cl=globleclasses.get(s);
			Save.set("Resources", "Globle Classes", cl.getTypeName()+":"+s, cl.getValueAsString());
		}
	}
	public static HashMap<String,String> getSettings(final File file){
		final HashMap<String,String> set = new HashMap<>();
		try{
			final BufferedReader in = new BufferedReader(new FileReader(file));
			String s;
			String[] s1;
			while((s=in.readLine())!=null){
				s=s.trim().replace("\\s+", "");
				if(s.startsWith("SETTING:")){
					s1=s.substring(8).split("=");
					set.put(s1[0], s1[1]);
				}
			}
			in.close();
		}catch(final Exception exception){ exception.printStackTrace(); }
		return set;
	}
	public abstract static class Action extends CodeBracket{
		protected final ActionLineFormat args;
		protected final WraithavenScript whs;
		protected final int line;
		public Action(final WraithavenScript script, final ActionLineFormat format, final int lines){
			args=format;
			whs=script;
			line=lines;
		}
		@Override
		public abstract void run();
	}
	public static ClassType generateClassType(final String type)throws IllegalArgumentException{
		if(type.equals("Boolean"))return new BooleanClass(false);
		if(type.equals("Number"))return new NumberClass(0.0);
		if(type.equals("String"))return new StringClass("");
		if(type.equals("Player"))return new PlayerClass(null);
		if(type.equals("Kingdom"))return new KingdomClass(null);
		if(type.equals("Building"))return new BuildingClass(null);
		if(type.equals("Army"))return new ArmyClass(null);
		throw new IllegalArgumentException("Unknown variable type: "+type+".");
	}
	private static class CodeBracket{
		private final ArrayList<CodeBracket> actions = new ArrayList<>();
		public void addAction(final CodeBracket a){ actions.add(a); }
		public void run(){ for(CodeBracket a : actions){ a.run(); } }
	}
	private static class IfStatement extends CodeBracket{
		private final CodeBracket t;
		private final CodeBracket e;
		private final BooleanClass state;
		private boolean creatingthen = true;
		public IfStatement(final BooleanClass s){
			state=s;
			t=new CodeBracket();
			e=new CodeBracket();
		}
		@Override
		public void run(){
			if(state.getState())t.run();
			else e.run();
		}
		@Override
		public void addAction(final CodeBracket a){
			if(creatingthen)t.addAction(a);
			else e.addAction(a);
		}
		public CodeBracket getThenCode(){ return t; }
		public CodeBracket getElseCode(){ return e; }
		public BooleanClass getState(){ return state; }
	}
	private static class WhileStatement extends CodeBracket{
		private final CodeBracket t;
		private final BooleanClass state;
		public WhileStatement(final BooleanClass s){
			state=s;
			t=new CodeBracket();
		}
		@Override
		public void addAction(final CodeBracket a){ t.addAction(a); }
		@Override
		public void run(){ while(state.s){ t.run(); } }
		public CodeBracket getCode(){ return t; }
		public BooleanClass getState(){ return state; }
	}
	public static class BooleanClass implements ClassType{
		private boolean s;
		public BooleanClass(final boolean state){ s=state; }
		public boolean getState(){ return s; }
		public void setState(final boolean state){ s=state; }
		public void setValueFromString(final String string){ s=Boolean.valueOf(string); }
		public String getValueAsString(){ return String.valueOf(s); }
		public String getTypeName(){ return "Boolean"; }
		public PrimitiveType getPrimitiveType(){ return PrimitiveType.BOOLEAN; }
	}
	public static class NumberClass implements ClassType{
		private double s;
		public NumberClass(final double state){ s=state; }
		public double getState(){ return s; }
		public void setState(final double state){ s=state; }
		public void setValueFromString(final String string){ s=Double.valueOf(string); }
		public String getValueAsString(){ return String.valueOf(s); }
		public String getTypeName(){ return "Number"; }
		public PrimitiveType getPrimitiveType(){ return PrimitiveType.NUMBER; }
	}
	public static class StringClass implements ClassType{
		private String s;
		public StringClass(final String state){ s=state; }
		public String getState(){ return s; }
		public void setState(final String state){ s=state; }
		public void setValueFromString(final String string){ s=string; }
		public String getValueAsString(){ return String.valueOf(s); }
		public String getTypeName(){ return "String"; }
		public PrimitiveType getPrimitiveType(){ return PrimitiveType.STRING; }
	}
	public static class PlayerClass implements ClassType{
		private Player s;
		public PlayerClass(final Player state){ s=state; }
		public Player getState(){ return s; }
		public void setState(final Player state){ s=state; }
		public void setValueFromString(final String string){ s=Bukkit.getPlayer(string); }
		public String getValueAsString(){ return s.getName(); }
		public String getTypeName(){ return "Player"; }
		public PrimitiveType getPrimitiveType(){ return PrimitiveType.PLAYER; }
	}
	public static class KingdomClass implements ClassType{
		private Kingdom s;
		public KingdomClass(final Kingdom state){ s=state; }
		public Kingdom getState(){ return s; }
		public void setState(final Kingdom state){ s=state; }
		public void setValueFromString(final String string){ s=KingdomMg.getKingdom(string); }
		public String getValueAsString(){ return s.getName(); }
		public String getTypeName(){ return "Kingdom"; }
		public PrimitiveType getPrimitiveType(){ return PrimitiveType.KINGDOM; }
	}
	public static class BuildingClass implements ClassType{
		private Building s;
		public BuildingClass(final Building state){ s=state; }
		public Building getState(){ return s; }
		public void setState(final Building state){ s=state; }
		public void setValueFromString(final String string){ s=Building.getById(KingdomMg.getKingdom(string.split(",")[0]), Long.valueOf(string.split(",")[1])); }
		public String getValueAsString(){ return s.getKingdom().getName()+","+s.getId(); }
		public String getTypeName(){ return "Building"; }
		public PrimitiveType getPrimitiveType(){ return PrimitiveType.BUILDING; }
	}
	public static class ArmyClass implements ClassType{
		private Army s;
		public ArmyClass(final Army state){ s=state; }
		public Army getState(){ return s; }
		public void setState(final Army state){ s=state; }
		public void setValueFromString(final String string){ s=(Army)Building.getById(KingdomMg.getKingdom(string.split(",")[0]), Long.valueOf(string.split(",")[1])); }
		public String getValueAsString(){ return s.getKingdom().getName()+","+s.getId(); }
		public String getTypeName(){ return "Army"; }
		public PrimitiveType getPrimitiveType(){ return PrimitiveType.ARMY; }
	}
	public static class ArrayClass implements ClassType{
		private ArrayList<ClassType> s = new ArrayList<>();
		public ArrayList<ClassType> getState(){ return s; }
		public void addState(final ClassType c){ s.add(c); }
		public void removeState(final ClassType c){ s.remove(c); }
		public void setValueFromString(final String string){
			s=new ArrayList<>();
			String[] s2;
			ClassType c;
			for(String s1 : spilt(string)){
				s2=s1.split(":");
				c=WraithavenScript.generateClassType(s2[0]);
				c.setValueFromString(s2[1]);
				s.add(c);
			}
		}
		public String getValueAsString(){
			String s1 = "";
			for(ClassType c : s){
				if(c instanceof ArrayClass)s1+=";("+c.getTypeName()+":"+c.getValueAsString()+")";
				else s1+=";"+c.getTypeName()+":"+c.getValueAsString();
			}
			while(s1.startsWith(";"))s1=s1.substring(1);
			return s1;
		}
		private static ArrayList<String> spilt(final String s){
			final ArrayList<String> code = new ArrayList<>();
			int in = 0;
			String st = "";
			final char[] cs = s.toCharArray();
			for(int x = 0; x<cs.length; x++){
				if(cs[x]=='(')in++;
				if(cs[x]==')')in--;
				if(in==0){
					if(cs[x]==';'){
						code.add(st);
						st="";
					}else st+=cs[x];
				}
			}
			return code;
		}
		public String getTypeName(){ return "Array"; }
		public PrimitiveType getPrimitiveType(){ return PrimitiveType.ARRAY; }
	}
	public static class EventProperties{
		private Player p;
		private final Kingdom k;
		private final Building b;
		public EventProperties(final Building building){
			b=building;
			k=(b==null?null:b.getKingdom());
			p=(k==null?null:Bukkit.getPlayer(k.getOwner()));
		}
		public Player getPlayer(){
			if(p==null
					&&k!=null){
				p=Bukkit.getPlayer(k.getOwner());
				return p;
			}
			return p;
		}
		public Kingdom getKingdom(){ return k; }
		public Building getBuilding(){ return b; }
	}
	public static interface ClassType{
		public void setValueFromString(final String string);
		public String getValueAsString();
		public String getTypeName();
		public PrimitiveType getPrimitiveType();
	}
	public static enum PrimitiveType{ BOOLEAN, STRING, NUMBER, PLAYER, ARMY, BUILDING, KINGDOM, ARRAY; }
}
package me.ci.Conquest.KingdomManagment.WHScript;

import me.ci.Conquest.KingdomManagment.WHScript.EventsAPI.*;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.*;

public class ActionMg{
	public static Action getAction(final WraithavenScript whs, final String line, final int codeline)throws Exception{
		final ActionLineFormat format = new ActionLineFormat(whs, line);
		if(format.getCommand().equals("AssignBoolean"))return new AssignPrimative(whs, format, codeline, PrimitiveType.BOOLEAN);
		if(format.getCommand().equals("AssignString"))return new AssignPrimative(whs, format, codeline, PrimitiveType.STRING);
		if(format.getCommand().equals("AssignNumber"))return new AssignPrimative(whs, format, codeline, PrimitiveType.NUMBER);
		if(format.getCommand().equals("DisplayValues"))return new DisplayValues(whs, format, codeline);
		if(format.getCommand().equals("Add"))return new Add(whs, format, codeline);
		if(format.getCommand().equals("Divide"))return new Divide(whs, format, codeline);
		if(format.getCommand().equals("Subtract"))return new Subtract(whs, format, codeline);
		if(format.getCommand().equals("Power"))return new Power(whs, format, codeline);
		if(format.getCommand().equals("Multiply"))return new Multiply(whs, format, codeline);
		if(format.getCommand().equals("Modulus"))return new Modulus(whs, format, codeline);
		if(format.getCommand().equals("NumberEquals"))return new NumberEquals(whs, format, codeline);
		if(format.getCommand().equals("InvertBoolean"))return new InvertBoolean(whs, format, codeline);
		if(format.getCommand().equals("Max"))return new Max(whs, format, codeline);
		if(format.getCommand().equals("Min"))return new Min(whs, format, codeline);
		if(format.getCommand().equals("Round"))return new Round(whs, format, codeline);
		if(format.getCommand().equals("SquareRoot"))return new SquareRoot(whs, format, codeline);
		if(format.getCommand().equals("Random"))return new Random(whs, format, codeline);
		if(format.getCommand().equals("GetPlayer"))return new GetPlayer(whs, format, codeline);
		if(format.getCommand().equals("GetKingdom"))return new GetKingdom(whs, format, codeline);
		if(format.getCommand().equals("GetBuilding"))return new GetBuilding(whs, format, codeline);
		if(format.getCommand().equals("IsBuildingAnArmy"))return new IsBuildingAnArmy(whs, format, codeline);
		if(format.getCommand().equals("IsPlayerAKing"))return new IsPlayerAKing(whs, format, codeline);
		if(format.getCommand().equals("SendMessage"))return new SendMessage(whs, format, codeline);
		if(format.getCommand().equals("GetBuildingLevel"))return new GetBuildingLevel(whs, format, codeline);
		if(format.getCommand().equals("GetBuildingHp"))return new GetBuildingHp(whs, format, codeline);
		if(format.getCommand().equals("GetKingdomMorale"))return new GetKingdomMorale(whs, format, codeline);
		return null;
	}
	public static class ActionLineFormat{
		private String cmd;
		private ClassType[] input;
		private String[] output;
		public ActionLineFormat(final WraithavenScript whs, final String line)throws Exception{
			final String in1 = line.substring(indexOf(line, '(')+1, indexOf(line, ')')+1);
			final String in2 = line.substring(indexOf(line, '[')+1, indexOf(line, ']')+1);
			cmd=line.substring(0, line.indexOf("("));
			String[] in = in1.isEmpty()?new String[0]:spilt(in1, ',');
			output=in2.isEmpty()?new String[0]:spilt(in2, ',');
			input=new ClassType[in.length];
			if(output.length==1&&output[0].isEmpty())output=new String[0];
			if(in.length==1&&in[0].isEmpty())in=new String[0];
			for(int i = 0; i<in.length; i++){
				if(in[i].startsWith("\"")
						&&in[i].endsWith("\""))input[i]=new StringClass(in[i].substring(1, in[i].length()-1));
				else if(isNumber(in[i]))input[i]=new NumberClass(Double.valueOf(in[i]));
				else if(in[i].equals("true")
						||in[i].equals("false"))input[i]=new BooleanClass(Boolean.valueOf(in[i]));
				else input[i]=whs.getVariable(in[i]);
			}
		}
		public String getCommand(){ return cmd; }
		public ClassType[] getInputs(){ return input; }
		public String[] getOutputs(){ return output; }
		private boolean isNumber(final String s){
			try{
				Double.valueOf(s);
				return true;
			}catch(Exception exception){ return false; }
		}
		private static String[] spilt(final String s, final char c){
			if(s.isEmpty())return new String[0];
			final String[] strings = new String[numberOfOccurences(s, c)+1];
			if(strings.length==0)return strings;
			boolean q = false;
			String st = "";
			int place = 0;
			char x;
			final char[] cs = s.toCharArray();
			for(int i = 0; i<cs.length; i++){
				x=cs[i];
				if(x=='\"')q=!q;
				if(!q){
					if(x==c||i==cs.length-1){
						strings[place]=st;
						place++;
						st="";
					}else st+=x;
				}else st+=x;
			}
			return strings;
		}
		private static int numberOfOccurences(final String s, final char c){
			int x = 0;
			boolean q = false;
			for(char i : s.toCharArray()){
				if(i=='\"')q=!q;
				if(!q&&i==c)x++;
			}
			return x;
		}
		private static int indexOf(final String s, final char c){
			boolean q = false;
			final char[] cs = s.toCharArray();
			for(int x = 0; x<s.length(); x++){
				if(cs[x]=='\"')q=!q;
				if(!q&&cs[x]==c)return x;
			}
			return -1;
		}
	}
}
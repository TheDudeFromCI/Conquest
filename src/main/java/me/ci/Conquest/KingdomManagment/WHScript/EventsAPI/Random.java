package me.ci.Conquest.KingdomManagment.WHScript.EventsAPI;

import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.KingdomManagment.WHScript.ActionMg.ActionLineFormat;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.Action;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.BooleanClass;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.ClassType;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.NumberClass;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.PrimitiveType;

public class Random extends Action{
	public Random(final WraithavenScript whs, final ActionLineFormat format, final int line){
		super(whs, format, line);
		try{
			if(args.getInputs().length!=3)throw new Exception("Incorrect number of arguments!");
			if(args.getOutputs().length!=1)throw new Exception("Incorrect number of outputs!");
			if(args.getInputs()[0].getPrimitiveType()!=PrimitiveType.NUMBER)throw new Exception("Incorrect variable type: "+args.getInputs()[0].getPrimitiveType()+".");
			if(args.getInputs()[1].getPrimitiveType()!=PrimitiveType.NUMBER)throw new Exception("Incorrect variable type: "+args.getInputs()[0].getPrimitiveType()+".");
			if(args.getInputs()[2].getPrimitiveType()!=PrimitiveType.BOOLEAN)throw new Exception("Incorrect variable type: "+args.getInputs()[0].getPrimitiveType()+".");
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
	public void run(){
		try{
			final ClassType cl = whs.getVariable(args.getOutputs()[0]);
			if(cl==null)throw new Exception("Could not find variable "+args.getOutputs()[0]+".");
			if(cl.getPrimitiveType()!=PrimitiveType.NUMBER)throw new Exception("Incorrect variable type: "+cl.getTypeName()+".");
			final double min = ((NumberClass)args.getInputs()[0]).getState();
			final double max = ((NumberClass)args.getInputs()[1]).getState();
			final boolean integers = ((BooleanClass)args.getInputs()[2]).getState();
			if(min>max)throw new Exception("Minimum number is larger then maximum number.");
			if(min==max)throw new Exception("Minimum number is equal to maximum number.");
			if(max-min<1&&integers)throw new Exception("Number range offers no room for integers.");
			cl.setValueFromString(String.valueOf(generateNumber(min, max, integers)));
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
	private static double generateNumber(final double min, final double max, final boolean integers){
		double r = (max-min)*Math.random()+min;
		if(integers)r=Math.round(r);
		if(r<min)r++;
		if(r>max)r--;
		return r;
	}
}
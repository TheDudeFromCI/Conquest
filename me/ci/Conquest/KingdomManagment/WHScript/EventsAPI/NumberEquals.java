package me.ci.Conquest.KingdomManagment.WHScript.EventsAPI;

import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.KingdomManagment.WHScript.ActionMg.ActionLineFormat;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.Action;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.ClassType;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.NumberClass;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.PrimitiveType;

public class NumberEquals extends Action{
	public NumberEquals(final WraithavenScript whs, final ActionLineFormat format, final int line){
		super(whs, format, line);
		try{
			if(args.getInputs().length!=2)throw new Exception("Incorrect number of arguments!");
			if(args.getOutputs().length!=1)throw new Exception("Incorrect number of outputs!");
			if(args.getInputs()[0].getPrimitiveType()!=PrimitiveType.NUMBER)throw new Exception("Incorrect variable type: "+args.getInputs()[0].getPrimitiveType()+".");
			if(args.getInputs()[1].getPrimitiveType()!=PrimitiveType.NUMBER)throw new Exception("Incorrect variable type: "+args.getInputs()[1].getPrimitiveType()+".");
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
	public void run(){
		try{
			final ClassType cl = whs.getVariable(args.getOutputs()[0]);
			if(cl==null)throw new Exception("Could not find variable "+args.getOutputs()[0]+".");
			if(cl.getPrimitiveType()!=PrimitiveType.BOOLEAN)throw new Exception("Incorrect variable type: "+cl.getTypeName()+".");
			if(((NumberClass)args.getInputs()[0]).getState()==((NumberClass)args.getInputs()[1]).getState())cl.setValueFromString("true");
			else cl.setValueFromString("false");
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
}
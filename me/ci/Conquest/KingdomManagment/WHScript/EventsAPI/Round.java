package me.ci.Conquest.KingdomManagment.WHScript.EventsAPI;

import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.KingdomManagment.WHScript.ActionMg.ActionLineFormat;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.Action;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.ClassType;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.NumberClass;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.PrimitiveType;

public class Round extends Action{
	public Round(final WraithavenScript whs, final ActionLineFormat format, final int line){
		super(whs, format, line);
		try{
			if(args.getInputs().length!=1)throw new Exception("Incorrect number of arguments!");
			if(args.getOutputs().length!=1)throw new Exception("Incorrect number of outputs!");
			if(args.getInputs()[0].getPrimitiveType()!=PrimitiveType.NUMBER)throw new Exception("Incorrect variable type: "+args.getInputs()[0].getPrimitiveType()+".");
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
	public void run(){
		try{
			final ClassType cl = whs.getVariable(args.getOutputs()[0]);
			if(cl==null)throw new Exception("Could not find variable "+args.getOutputs()[0]+".");
			if(cl.getPrimitiveType()!=PrimitiveType.NUMBER)throw new Exception("Incorrect variable type: "+cl.getTypeName()+".");
			cl.setValueFromString(String.valueOf(Math.round(((NumberClass)args.getInputs()[0]).getState())));
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
}
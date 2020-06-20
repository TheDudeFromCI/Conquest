package me.ci.Conquest.KingdomManagment.WHScript.EventsAPI;

import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.KingdomManagment.WHScript.ActionMg.ActionLineFormat;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.Action;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.ClassType;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.PrimitiveType;

public class AssignPrimative extends Action{
	private final PrimitiveType ptype;
	public AssignPrimative(final WraithavenScript whs, final ActionLineFormat format, final int line, final PrimitiveType type){
		super(whs, format, line);
		ptype=type;
		try{
			if(args.getInputs().length!=1)throw new Exception("Incorrect number of arguments!");
			if(args.getOutputs().length!=1)throw new Exception("Incorrect number of outputs!");
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
	public void run(){
		try{
			final ClassType cl = whs.getVariable(args.getOutputs()[0]);
			if(cl==null)throw new Exception("Could not find variable "+args.getOutputs()[0]+".");
			if(cl.getPrimitiveType()!=ptype)throw new Exception("Incorrect variable type: "+cl.getTypeName()+".");
			cl.setValueFromString(args.getInputs()[0].getValueAsString());
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
}
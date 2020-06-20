package me.ci.Conquest.KingdomManagment.WHScript.EventsAPI;

import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.KingdomManagment.WHScript.ActionMg.ActionLineFormat;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.Action;

public class DisplayValues extends Action{
	public DisplayValues(final WraithavenScript whs, final ActionLineFormat format, final int line){
		super(whs, format, line);
		try{
			if(args.getOutputs().length>0)throw new Exception("Incorrect number of outputs!");
			if(args.getInputs().length!=1)throw new Exception("Incorrect number of inputs!");
		}catch(Exception exception){ exception.printStackTrace(); }
	}
	public void run(){
		try{
			System.out.println(args.getInputs()[0].getTypeName()+":"+args.getInputs()[0].getValueAsString());
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
}
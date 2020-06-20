package me.ci.Conquest.KingdomManagment.WHScript.EventsAPI;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript;
import me.ci.Conquest.KingdomManagment.WHScript.ActionMg.ActionLineFormat;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.Action;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.ClassType;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.PlayerClass;
import me.ci.Conquest.KingdomManagment.WHScript.WraithavenScript.PrimitiveType;

public class SendMessage extends Action{
	public SendMessage(final WraithavenScript whs, final ActionLineFormat format, final int line){
		super(whs, format, line);
		try{
			if(args.getInputs().length!=2)throw new Exception("Incorrect number of arguments!");
			if(args.getOutputs().length>0)throw new Exception("Incorrect number of outputs!");
			if(args.getInputs()[0].getPrimitiveType()!=PrimitiveType.PLAYER)throw new Exception("Incorrect variable type: "+args.getInputs()[0].getPrimitiveType()+".");
			if(args.getInputs()[1].getPrimitiveType()!=PrimitiveType.STRING)throw new Exception("Incorrect variable type: "+args.getInputs()[1].getPrimitiveType()+".");
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
	public void run(){
		try{
			final ClassType cl = whs.getVariable(args.getOutputs()[0]);
			if(cl==null)throw new Exception("Could not find variable "+args.getOutputs()[0]+".");
			final Player p = ((PlayerClass)cl).getState();
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', args.getInputs()[1].getValueAsString()));
		}catch(Exception exception){ System.err.println("Error parsing event code! Line: "+line+" is broken.\n"+exception.getMessage()); }
	}
}
package me.ci.Console;

import me.ci.Console.Logs.DonationAmount;
import me.ci.Console.Logs.Donations;
import me.ci.Console.Logs.KingdomCreations;
import me.ci.Console.Logs.KingdomDeletions;
import me.ci.Console.Logs.LoginAttempts;
import me.ci.Console.Logs.Logins;
import me.ci.Console.Logs.NewPlayers;
import me.ci.Console.Logs.PlayTime;

public abstract class Log{
	public Log(final String stats){}
	public abstract String save();
	public static Log getLogByType(final LogType type, final String stats){
		if(type==LogType.DONATION_AMOUNT)return new DonationAmount(stats);
		if(type==LogType.DONATIONS)return new Donations(stats);
		if(type==LogType.KINGDOM_CREATIONS)return new KingdomCreations(stats);
		if(type==LogType.KINGDOM_DELETIONS)return new KingdomDeletions(stats);
		if(type==LogType.LOGIN_ATTEMPTS)return new LoginAttempts(stats);
		if(type==LogType.LOGINS)return new Logins(stats);
		if(type==LogType.NEW_PLAYERS)return new NewPlayers(stats);
		if(type==LogType.PLAY_TIME)return new PlayTime(stats);
		return null;
	}
}
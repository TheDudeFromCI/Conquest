package me.ci.Console;

public enum LogType{
	LOGINS(0),
	LOGIN_ATTEMPTS(1),
	NEW_PLAYERS(2),
	PLAY_TIME(3),
	DONATIONS(4),
	DONATION_AMOUNT(5),
	KINGDOM_CREATIONS(6),
	KINGDOM_DELETIONS(7);
	private final int id;
	private LogType(final int a){ id=a; }
	public int getId(){ return id; }
	public static LogType getById(final int id){
		for(LogType type : values())if(type.id==id)return type;
		return null;
	}
}
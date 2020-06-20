package me.ci.Conquest.Buildings.Constructors;

public enum StatusType{
	BUILDING((byte)2),
	REVOLT((byte)0),
	MESSAGE((byte)8),
	NOT_ALIVE((byte)1),
	PLAGUE((byte)13),
	NO_RESORCES((byte)15),
	LOW_RESOURCES((byte)4),
	TROOP_CONVERSION((byte)9),
	RESEARCHING((byte)7),
	IDLING_VILLAGERS((byte)12),
	NORMAL((byte)5);
	private byte color;
	private StatusType(byte color){
		this.color=color;
	}
	public byte getColor(){
		return this.color;
	}
}
package me.ci.Conquest.Military;

import me.ci.Conquest.Misc.Direction;

public enum ArmyFormation{
	//Formation is NORTH, EAST, SOUTH, WEST
	A(0, new boolean[]{true, true, true, true}),
	B(1, new boolean[]{true, true, false, true}),
	C(2, new boolean[]{true, true, true, false}),
	D(3, new boolean[]{false, true, true, true}),
	E(4, new boolean[]{true, false, true, true}),
	F(5, new boolean[]{true, false, true, false}),
	G(6, new boolean[]{false, true, false, true}),
	H(7, new boolean[]{true, true, false, false}),
	I(8, new boolean[]{false, true, true, false}),
	J(9, new boolean[]{false, false, true, true}),
	K(10, new boolean[]{true, false, false, true}),
	L(11, new boolean[]{true, false, false, false}),
	M(12, new boolean[]{false, true, false, false}),
	N(13, new boolean[]{false, false, true, false}),
	O(14, new boolean[]{false, false, false, true}),
	P(15, new boolean[]{false, false, false, false});
	private final int id;
	private final boolean[] sides;
	private ArmyFormation(final int a, final boolean[] b){
		id=a;
		sides=b;
	}
	public static ArmyFormation getById(int id){
		for(ArmyFormation formation : ArmyFormation.values()){ if(formation.id==id)return formation; }
		return null;
	}
	public boolean usesSide(Direction face){
		if(face.equals(Direction.NORTH))return this.sides[0];
		if(face.equals(Direction.EAST))return this.sides[1];
		if(face.equals(Direction.SOUTH))return this.sides[2];
		else return this.sides[3];
	}
	public int getSideCount(){
		int s = 0;
		if(sides[0])s++;;
		if(sides[1])s++;;
		if(sides[2])s++;;
		if(sides[3])s++;;
		return s;
	}
	public static ArmyFormation getBySides(boolean north, boolean east, boolean south, boolean west){
		for(ArmyFormation formation : ArmyFormation.values()){
			if(formation.usesSide(Direction.NORTH)!=north)continue;
			if(formation.usesSide(Direction.EAST)!=east)continue;
			if(formation.usesSide(Direction.SOUTH)!=south)continue;
			if(formation.usesSide(Direction.WEST)!=west)continue;
			return formation;
		}
		return null;
	}
	public int getId(){ return this.id; }
	public boolean[] getSides(){ return this.sides; }
}
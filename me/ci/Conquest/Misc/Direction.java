package me.ci.Conquest.Misc;

import java.awt.Point;

public enum Direction{
	NORTH(0),
	EAST(1),
	SOUTH(2),
	WEST(3);
	private int id;
	private Direction(int id){ this.id=id; }
	public int getId(){ return this.id; }
	public static Direction getById(int id){
		for(Direction face : Direction.values()){ if(face.id==id)return face; }
		return null;
	}
	public Point getCoordOffset(){
		if(id==0)return new Point(0, -1);
		if(id==1)return new Point(1, 0);
		if(id==2)return new Point(0, 1);
		return new Point(-1, 0);
	}
}
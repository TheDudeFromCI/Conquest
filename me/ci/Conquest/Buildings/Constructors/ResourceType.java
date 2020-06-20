package me.ci.Conquest.Buildings.Constructors;

public enum ResourceType{
	WOOD(0),
	IRON(1),
	STONE(2),
	NONE(3),
	FOOD(4),
	GOLD(5),
	ARMS(6);
	private int id;
	private ResourceType(int id){
		this.id=id;
	}
	public int getId(){
		return this.id;
	}
	public static ResourceType getByName(String name){
		for(ResourceType type : ResourceType.values()){
			if(type.name().equals(name))return type;
		}
		return null;
	}
	public static ResourceType getById(int id){
		for(ResourceType type : ResourceType.values()){
			if(type.getId()==id)return type;
		}
		return null;
	}
}
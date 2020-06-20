package me.ci.Conquest.Misc;

import java.util.ArrayList;

public enum Achievement{
	CREATING_A_KINGDOM(0, 15, "Achievement unlocked by creating a kingdom.", "Creating a Kingdom", true),
	CAKE_ISNT_A_LIE(1, 25, "Achievement unlocked by punching the air with the Cake Tool, which opens the Help Videos Menu.", "Cake Isn't a Lie", true),
	SCROLLING_THE_ITEMBAR(2, 4, "Achievement unlocked by scrolling through the Itembar with the wheel mouse or 591 keys.", "Scrolling the Itembar", true),
	DERP_SCOUT(3, 3, "Achievement unlocked by right-clicking with the scouting tool.", "Derp Scout", true),
	BOY_SCOUT_GIRL_SCOUT(4, 5, "Achievement unlocked by scouting your first piece of land.", "Boy Scout, Girl Scout", true),
	THE_UPS_OF_CONQUEST(5, 6, "Achievement unlocked by punching your land with the minecart, aka Change Height Tool.", "The Ups of Conquest", true),
	THE_DOWNS_OF_CONQUEST(6, 6, "Achievement unlocked by right-clicking your land with the minecart, aka Change Height Tool.", "The Downs of Conquest", true),
	FOOD_COLLECTION_TIME(7, 5, "Achievement unlocked by punching a Food Resource, with the Resource Collection Tool.", "Food Collection Time", true),
	WOOD_COLLECTION_TIME(8, 5, "Achievement unlocked by punching a Wood Resource, with the Resource Collection Tool.", "Wood Collection Time", true),
	STONE_COLLECTION_TIME(9, 5, "Achievement unlocked by punching a Stone Resource, with the Resource Collection Tool.", "Stone Collection Time", true),
	IRON_COLLECTION_TIME(10, 5, "Achievement unlocked by punching a Iron Resource, with the Resource Collection Tool.", "Iron Collection Time", true),
	GOLD_COLLECTION_TIME(11, 5, "Achievement unlocked by punching a Gold Resource, with the Resource Collection Tool.", "Gold Collection Time", true),
	KINGDOM_INFO(12, 5, "Achievement unlocked by punching any land, troop or building in your kingdom with paper, aka Info Tool.", "Kingdom Info", true),
	ADVANCED_KINGDOM_INFO(13, 5, "Achievement unlocked by right-clicking the air with paper, aka Info Tool.", "Advanced Kingdom Info", true),
	LEVEL_A_3X3(14, 10, "Achievement unlocked by changing the height of 9 chunks, 3 across, and 3 down, to equal height using the Change Height Tool.", "Level a 3x3", true),
	LEVEL_A_5X5(15, 15, "Achievement unlocked by changing the height of 25 chunks, 5 across, and 5 down, to equal height using the Change Height Tool.", "Level a 5x5", true),
	FEED_THE_PEOPLE(16, 15, "Achievement unlocked by building a 3x3 Granary using the Brick, aka Build or Repair Tool.", "Feed the People", true),
	PUT_EM_TO_WORK(17, 16, "Achievement unlocked by punching your Granary with the gold helmet, aka Villager Movement Tool.", "Put 'em to Work", true),
	KNOWLEDGE_IS_POWER(18, 15, "Achievement unlocked by building a 5x5 University, using the Brick, aka Build or Repair Tool.", "Knowledge is Power", true),
	ROAD_LESS_TRAVELED(19, 10, "Achievement unlocked by building a road, using the Brick, aka Build or Repair Tool.", "Road Less Traveled", true),
	PATH_TO_GREATNESS(20, 18, "Achievement unlocked by connecting roads which lead from your Granary to your Capitol.", "Path to Greatness", true),
	SAW_SAW(21, 12, "Achievement unlocked by building a 3x3 Sawmill using the Brick, aka Build or Repair Tool.", "Saw, Saw", true),
	STONE_FOR_STONE(22, 12, "Achievement unlocked by building a 3x3 StoneQuarry using the Brick, aka Build or Repair Tool.", "Stone For Stone", true),
	METAL_MADNESS(23, 12, "Achievement unlocked by building a 3x3 MetalMining using the Brick, aka Build or Repair Tool.", "Metal Madness", true),
	MR_BLACKSMITH(24, 12, "Achievement unlocked by building a 3x3 Blacksmith using the Brick, aka Build or Repair Tool.", "Mr Blacksmith", true),
	MEN_AT_ARMS(25, 12, "Achievement unlocked by building a 3x3 Barrack using the Brick, aka Build or Repair Tool.", "Men At Arms", true),
	HOME_SWEET_COTTAGE(26, 8, "Achievement unlocked by building a 1x1 Cottage using the Brick, aka Build or Repair Tool.", "Home Sweet Cottage", true),
	WELL_DRINK_TO_THAT(27, 12, "Achievement unlocked by building a 3x3 Tavern using the Brick, aka Build or Repair Tool.", "We'll Drink to That", true),
	HENCHMEN_FOR_HIRE(28, 13, "Achievement unlocked by right-clicking the Tavern with the gold ingot, aka Henchman Tool.", "Henchmen For Hire", true),
	SEND_A_HENCHMAN(29, 3, "Achievement unlocked by punching the air with a gold ingot, aka Henchman Tool.", "Send a Henchman", true),
	LEVEL_UP(30, 17, "Achievement unlocked by punching any building with the nether star, aka Upgrade Tool, and upgrading it to the next level.", "Level UP", true),
	HAWKEYES(31, 9, "Achievement unlocked by building a 1x1 Watchtower using the Brick, aka Build or Repair Tool.", "Hawkeyes", true),
	DEFEND_THE_KEEP(32, 9, "Achievement unlocked by building a Wall using the Brick, aka Build or Repair Tool.", "Defend the Keep", true),
	FIXING_WITH_THE_ANVIL(33, 4, "Achievement unlocked by punching any land, troop, or building with the anvil aka Fix Tool to repair any glitchy graphics.", "Fixing With the Anvil", true),
	INFO_HUD_CHANGED(34, 7, "Achievement unlocked by punching the air with the lever, aka Info Monitor Tool.", "Info HUD Changed", true),
	WWWEB(35, 3, "Achievement unlocked by punching punching the air with the gold nugget, aka Buy Feature Tool.", "WWWeb", true),
	BUTTER_NUGGET(36, 8, "Achievement unlocked by punching punching the air with the gold nugget, aka Buy Feature Tool.", "Butter Nugget", true),
	DONATE_TOOL(37, 8, "Achievement unlocked by punching the air with the gold block, aka Donate Tool.", "Donate Tool", true),
	WHO_AM_I(38, 6, "Achievement unlocked by punching the air with fireworks, aka Credits Tool.", "Who Am I", true),
	TELEPORTATION(39, 4, "Achievement unlocked by punching a spot on the map with the compass, aka Teleportation Tool.", "Teleportation", true),
	HOME_AT_LAST(40, 4, "Achievement unlocked by punching the air with the beacon, aka Capitol Warp Tool.", "Home at Last", true),
	ORGANIZE_ME(41, 8, "Achievement unlocked by punching the air with the sign, aka Itembar Management Tool.", "Organize Me", true),
	MAKING_SOLDIERS(42, 26, "Achievement unlocked by punching a military building like the Barrack with the Villager Movement Tool. Then adding villagers, and punching wilderness next to your kingdom.", "Making Soldiers", true),
	TROOP_YOURE_IT(43, 14, "Achievement unlocked by punching a troop chunk with the sword, aka Military Control Tool.", "Troop, You're It", true),
	POSITION_THE_TROOPS(44, 15, "Achievement unlocked by punching at least one of the red borders of a selected troop with the sword, aka Military Control Tool.", "Position the Troops", true),
	GOING_CAMPING(45, 11, "Achievement unlocked by punching all 4 red borders of a selected troop chunk, with the sword, aka Military Control Tool, and turning them pink.", "Going Camping", true),
	MOVE_EM_OUT(46, 18, "Achievement unlocked by punching all 4 red borders of a selected troop chunk, with the sword, aka Military Control Tool, and turning them pink.", "Move 'em Out", true),
	BLAZE_POWERS_ACTIVATE(47, 18, "Achievement unlocked by clicking with the blaze rod, aka Troop Special Tool.", "Blaze Powers, Activate", true),
	BRICK_BY_BRICK(48, 21, "Achievement unlocked by punching any building with low Hit Points, with the Brick, aka Build or Repair Tool.", "Brick by Brick", true),
	TUTORIAL_MASTER(49, 207, "Achievement unlocked by unlocking all 49 Tutorial achievements.", "Tutorial Master", true);
	private final int id;
	private final int points;
	private final String[] description;
	private final String name;
	private final boolean tutorial;
	private Achievement(final int a, final int b, final String c, final String e, final boolean f){
		id=a;
		points=b;
		description=wordWrap(c, 30);
		name=e;
		tutorial=f;
	}
	public int getId(){ return id; }
	public int getPoints(){ return points; }
	public String[] getDescription(){ return description; }
	public String getName(){ return name; }
	public boolean isTutorial(){ return tutorial; }
	public static Achievement getById(final int id){
		for(Achievement a : values())if(a.getId()==id)return a;
		return null;
	}
	private static String[] wordWrap(final String s, final int length){
		final ArrayList<String> lines = new ArrayList<>();
		final char[] chars = s.toCharArray();
		String current = "";
		String word = "";
		for(int i = 0; i<chars.length; i++){
			if(chars[i]==' '){
				if(current.length()+word.length()+1>length){
					if(current.isEmpty()){
						while(word.length()>=length){
							lines.add(word.substring(0, length));
							word=word.substring(length-1, word.length());
						}
					}else{
						lines.add(current);
						current=word;
						word="";
					}
				}else{
					current+=" "+word;
					while(current.startsWith(" "))current=current.substring(1);
					word="";
				}
			}else word+=chars[i];
		}
		if(current.length()+word.length()+1>length){
			if(current.isEmpty()){
				while(word.length()>=length){
					lines.add(word.substring(0, length));
					word=word.substring(length-1, word.length());
				}
				lines.add(word);
			}else{
				lines.add(current);
				current=word;
				word="";
				lines.add(current);
			}
		}else{
			current+=" "+word;
			while(current.startsWith(" "))current=current.substring(1);
			word="";
			lines.add(current);
		}
		return lines.toArray(new String[lines.size()]);
	}
}
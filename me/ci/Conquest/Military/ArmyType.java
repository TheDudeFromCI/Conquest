package me.ci.Conquest.Military;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ci.Community.PlayerMg;
import me.ci.Conquest.Military.Powers.*;

public enum ArmyType{
	TROOP(0, new double[]{1.95, 3.25, 4.55, 5.85, 7.15, 8.45, 9.75}, new double[]{11, 10.5, 10, 9.5, 9, 8.5, 8}, 1, 1, 13, (byte)0, "Troop", false, new Troop_Regroup()),
	ANTIMAGIC_ONAGER(1, new double[]{3.2, 4.5, 5.8, 7.1, 8.4, 9.7, 11}, new double[]{13, 12.5, 12, 11.5, 11, 10.5, 10}, 5, 10, 2, (byte)0, "Antimagic Onager", true, new AntiMagicOnager_Nullify()),
	ARCHER(2, new double[]{0.85, 2.15, 3.45, 4.75, 6.05, 7.35, 8.65}, new double[]{9, 8.5, 8, 7.5, 7, 6.5, 6}, 6, 4, 5, (byte)1, "Archer", false, new Archer_Volly()),
	BALLISTA(3, new double[]{4.1, 5.39, 6.69, 8, 9.3, 10.6, 11.9}, new double[]{14, 13.5, 13, 12.5, 12, 11.5, 11}, 7, 6, 2, (byte)0, "Balistica", true, new Ballista_Aim()),
	BATTERING_RAM(4, new double[]{5, 6.3, 7.6, 8.9, 10.2, 11.5, 12.8}, new double[]{15, 14.5, 14, 13.5, 13, 12.5, 12}, 1, 10, 2, (byte)0, "Battering Ram", true, new BatteringRam_Pound()),
	SIEGETOWER(5, new double[]{0.3, 1.6, 2.9, 4.2, 5.5, 6.8, 8.1}, new double[]{8, 7.5, 7, 6.5, 6, 5.5, 5}, 1, 1, 2, (byte)0, "SeigeTower", true, new SiegeTower_Transport()),
	BRIDGEMAKER(6, new double[]{1.4, 2.7, 4, 5.3, 6.6, 7.9, 9.2}, new double[]{10, 9.5, 9, 8.5, 8, 7.5, 7}, 1, 3, 2, (byte)0, "Bridgemaker", true, new BridgeMaker_Bridge()),
	CATAPULT(7, new double[]{5, 6.3, 7.6, 8.9, 10.2, 11.5, 12.8}, new double[]{15, 14.5, 14, 13.5, 13, 12.5, 12}, 8, 8, 2, (byte)0, "Catapult", true, new Catapult_Boulder()),
	CALVERY(8, new double[]{0.85, 2.15, 3.45, 4.75, 6.05, 7.35, 8.65}, new double[]{9, 8.5, 8, 7.5, 7, 6.5, 6}, 1, 3, 4, (byte)0, "Calvery", false, new Calvery_Charge()),
	COW_TOSSER(9, new double[]{0.3, 1.6, 2.9, 4.2, 5.5, 6.8, 8.1}, new double[]{8, 7.5, 7, 6.5, 6, 5.5, 5}, 4, 16, 2, (byte)0, "Cow Tosser", true, new CowTosser_Disease()),
	DRAGON_RIDER(10, new double[]{4.1, 5.39, 6.69, 8, 9.3, 10.6, 11.9}, new double[]{14, 13.5, 13, 12.5, 12, 11.5, 11}, 3, 10, 49, (byte)0, "Dragon Rider", false, new DragonRider_Flight()),
	DRAGON_SLAYER(11, new double[]{2.5, 3.8, 5.1, 6.4, 7.7, 9, 10.3}, new double[]{12, 11.5, 11, 10.5, 10, 9.5, 9}, 1, 5, 88, (byte)0, "Dragon Slayer", false, new DragonSlayer_Slay()),
	DRUID_HEALER(12, new double[]{0.3, 1.6, 2.9, 4.2, 5.5, 6.8, 8.1}, new double[]{8, 7.5, 7, 6.5, 6, 5.5, 5}, 1, 5, 35, (byte)13, "Druid Healer", false, new DruidHealer_Heal()),
	EARTH_GOLEM(13, new double[]{5, 6.3, 7.6, 8.9, 10.2, 11.5, 12.8}, new double[]{15, 14.5, 14, 13.5, 13, 12.5, 12}, 1, 9, 3, (byte)0, "Earth Golem", false, new EarthGolem_Sinkhole()),
	FIRE_GOLEM(14, new double[]{2.5, 3.8, 5.1, 6.4, 7.7, 9, 10.3}, new double[]{12, 11.5, 11, 10.5, 10, 9.5, 9}, 1, 16, 87, (byte)0, "Fire Golem", false, new FireGolem_Supernova()),
	GLADIATOR(15, new double[]{3.2, 4.5, 5.8, 7.1, 8.4, 9.7, 11}, new double[]{13, 12.5, 12, 11.5, 11, 10.5, 10}, 1, 15, 12, (byte)0, "Gladiator", false, new Gladiator_Melee()),
	ILLUSIONIST(16, new double[]{1.4, 2.7, 4, 5.3, 6.6, 7.9, 9.2}, new double[]{10, 9.5, 9, 8.5, 8, 7.5, 7}, 1, 5, 20, (byte)0, "Illusionist", false, new Illusionist_Illusion()),
	JUMPER(17, new double[]{0.85, 2.15, 3.45, 4.75, 6.05, 7.35, 8.65}, new double[]{9, 8.5, 8, 7.5, 7, 6.5, 6}, 1, 3, 152, (byte)0, "Jumper", false, new Jumper_Port()),
	KNIGHT(18, new double[]{3.2, 4.5, 5.8, 7.1, 8.4, 9.7, 11}, new double[]{13, 12.5, 12, 11.5, 11, 10.5, 10}, 1, 5, 42, (byte)0, "Knight", false, new Knight_Protect()),
	MAGE(19, new double[]{0.3, 1.6, 2.9, 4.2, 5.5, 6.8, 8.1}, new double[]{8, 7.5, 7, 6.5, 6, 5.5, 5}, 1, 18, 155, (byte)0, "Mage", false, new Mage_Transform()),
	MANGONAL(20, new double[]{1.95, 3.25, 4.55, 5.85, 7.15, 8.45, 9.75}, new double[]{11, 10.5, 10, 9.5, 9, 8.5, 8}, 2, 9, 2, (byte)0, "Mangonal", true, new Mangonel_GreekFire()),
	NINJA(21, new double[]{1.95, 3.25, 4.55, 5.85, 7.15, 8.45, 9.75}, new double[]{11, 10.5, 10, 9.5, 9, 8.5, 8}, 1, 6, 35, (byte)15, "Ninja", false, new Ninja_Bladestep()),
	PALADIN(22, new double[]{3.2, 4.5, 5.8, 7.1, 8.4, 9.7, 11}, new double[]{13, 12.5, 12, 11.5, 11, 10.5, 10}, 1, 20, 35, (byte)7, "Paladin", false, new Paladin_Sacrifice()),
	PIKEMAN(23, new double[]{3.2, 4.5, 5.8, 7.1, 8.4, 9.7, 11}, new double[]{13, 12.5, 12, 11.5, 11, 10.5, 10}, 1, 4, 17, (byte)0, "Pikeman", false, new Pikeman_Spear()),
	PRIEST(24, new double[]{0.3, 1.6, 2.9, 4.2, 5.5, 6.8, 8.1}, new double[]{8, 7.5, 7, 6.5, 6, 5.5, 5}, 1, 6, 35, (byte)12, "Priest", false, new Priest_Bless()),
	TREBUCHET(25, new double[]{5, 6.3, 7.6, 8.9, 10.2, 11.5, 12.8}, new double[]{15, 14.5, 14, 13.5, 13, 12.5, 12}, 9, 12, 2, (byte)0, "Trebuchet", true, new Trebuchet_Demolish()),
	UNDEAD(26, new double[]{4.1, 5.39, 6.69, 8, 9.3, 10.6, 11.9}, new double[]{14, 13.5, 13, 12.5, 12, 11.5, 11}, 1, 11, 110, (byte)0, "Undead", false, new Undead_Infect()),
	VIKING(27, new double[]{3.2, 4.5, 5.8, 7.1, 8.4, 9.7, 11}, new double[]{13, 12.5, 12, 11.5, 11, 10.5, 10}, 1, 12, 41, (byte)0, "Viking", false, new Viking_Pillage()),
	WATER_GOLEM(28, new double[]{0.85, 2.15, 3.45, 4.75, 6.05, 7.35, 8.65}, new double[]{9, 8.5, 8, 7.5, 7, 6.5, 6}, 1, 6, 22, (byte)0, "Water Golem", false, new WaterGolem_Lake()),
	WIND_GOLEM(29, new double[]{0.85, 2.15, 3.45, 4.75, 6.05, 7.35, 8.65}, new double[]{9, 8.5, 8, 7.5, 7, 6.5, 6}, 1, 5, 35, (byte)8, "Wind Golem", false, new WindGolem_Gust()),
	PROPHET(29, new double[]{0.3, 1.6, 2.9, 4.2, 5.5, 6.8, 8.1}, new double[]{8, 7.5, 7, 6.5, 6, 5.5, 5}, 1, 10, 35, (byte)10, "Prophet", false, new Prophet_DivineProtection());
	private final int id;
	private final double[] attack;
	private final double[] reload;
	private final int range;
	private final int cost;
	private final int materialid;
	private final byte data;
	private final String name;
	private final boolean siegeweapon;
	private final PowerDetails power;
	private ArmyType(final int id, final double[] attack, final double[] reload, final int range, final int cost, final int materialid, final byte data, final String name, final boolean siegeweapon, final PowerDetails power){
		this.id=id;
		this.attack=attack;
		this.reload=reload;
		this.range=range;
		this.cost=cost;
		this.materialid=materialid;
		this.data=data;
		this.name=name;
		this.siegeweapon=siegeweapon;
		this.power=power;
	}
	public int getStamina(){ return 20; }
	public void runPower(final Player player, final Location click){ if(power!=null)power.run(player, click, PlayerMg.getSelectedArmy(player.getName())); }
	public int getId(){ return this.id; }
	public double getAttack(int level){ return attack[level-1]; }
	public int getRange(){ return this.range; }
	public int getMaterialId(){ return this.materialid; }
	public String getName(){ return this.name; }
	public boolean isSiegeWeapon(){ return siegeweapon; }
	public byte getData(){ return data; }
	public double getReload(int level){ return reload[level-1]; }
	public int getCost(){ return cost; }
	public static ArmyType getById(int id){
		for(ArmyType type : ArmyType.values()){ if(type.id==id)return type; }
		return null;
	}
	@Override
	public String toString(){ return name; }
	public static int getMaxAmount(final int lvl){ return lvl*100; }
}
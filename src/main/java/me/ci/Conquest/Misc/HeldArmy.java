package me.ci.Conquest.Misc;

import org.bukkit.Chunk;

import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import me.ci.Conquest.Military.ArmyFormation;
import me.ci.Conquest.Military.ArmyType;

public class HeldArmy{
	private long protecttime;
	private Army protector;
	private final Kingdom kingdom;
	private final int hp;
	private final int lvl;
	private final int stamina;
	private final ArmyType realtype;
	private final ArmyType type;
	private final ArmyFormation formation;
	private final Direction direction;
	private final long reverttime;
	private final long flytime;
	private final long slaytime;
	private final long illusiontime;
	private final long speartime;
	private final long pillagetime;
	private final boolean nullified;
	private final boolean wizard;
	private final boolean commander;
	private final boolean blessed;
	public HeldArmy(final Army a){
		kingdom=a.getKingdom();
		hp=a.getHp();
		lvl=a.getLevel();
		stamina=a.getStamina();
		realtype=a.getRealArmyType();
		type=a.getArmyType();
		formation=a.getFormation();
		direction=a.getDirection();
		nullified=a.isNullified();
		wizard=a.hasWizard();
		commander=a.hasCommander();
		blessed=a.isBlessed();
		reverttime=a.reverttime;
		flytime=a.flytime;
		slaytime=a.slaytime;
		illusiontime=a.illusiontime;
		protecttime=a.pillagetime;
		speartime=a.speartime;
		pillagetime=a.pillagetime;
		protector=a.protector;
	}
	public HeldArmy(final String code){
		final String[] s = code.split(",");
		kingdom=KingdomMg.getKingdom(s[0]);
		hp=Integer.valueOf(s[1]);
		lvl=Integer.valueOf(s[2]);
		stamina=Integer.valueOf(s[3]);
		realtype=ArmyType.getById(Integer.valueOf(s[4]));
		type=ArmyType.getById(Integer.valueOf(s[5]));
		formation=ArmyFormation.getById(Integer.valueOf(s[6]));
		direction=Direction.getById(Integer.valueOf(s[7]));
		nullified=Boolean.valueOf(s[8]);
		wizard=Boolean.valueOf(s[9]);
		commander=Boolean.valueOf(s[10]);
		blessed=Boolean.valueOf(s[11]);
		reverttime=Long.valueOf(s[12]);
		flytime=Long.valueOf(s[13]);
		slaytime=Long.valueOf(s[14]);
		illusiontime=Long.valueOf(s[15]);
		protecttime=Long.valueOf(s[16]);
		speartime=Long.valueOf(s[17]);
		pillagetime=Long.valueOf(s[18]);
		try{ protector=(Army)Building.getById(kingdom, Integer.valueOf(s[19]));
		}catch(final Exception exception){
			protector=null;
			protecttime=-1;
		}
	}
	public void unload(final Chunk nwc){
		final Army a = new Army(nwc, kingdom, hp, realtype, lvl);
		a.stamina=stamina;
		a.type=type;
		a.formation=formation;
		a.direction=direction;
		a.protector=protector;
		a.blessed=blessed;
		a.wizard=wizard;
		a.commander=commander;
		a.nullified=nullified;
		a.pillagetime=pillagetime;
		a.speartime=speartime;
		a.protecttime=protecttime;
		a.reverttime=reverttime;
		a.flytime=flytime;
		a.slaytime=slaytime;
		a.illusiontime=illusiontime;
		a.updateGraphics(false);
	}
	public String save(){
		return kingdom.getName()
				+","+hp
				+","+lvl
				+","+stamina
				+","+realtype.getId()
				+","+type.getId()
				+","+formation.getId()
				+","+direction.getId()
				+","+nullified
				+","+wizard
				+","+commander
				+","+blessed
				+","+reverttime
				+","+flytime
				+","+slaytime
				+","+illusiontime
				+","+protecttime
				+","+speartime
				+","+pillagetime
				+","+(protector==null?-1:protector.getId());
	}
	public ArmyType getRealArmyType(){ return realtype; }
	public ArmyType getArmyType(){ return type; }
	public boolean isTransformed(){ return type!=realtype; }
	public int getHp(){ return hp; }
	public int getStamina(){ return stamina; }
	public int getLevel(){ return lvl; }
	public boolean isNullified(){ return nullified; }
	public boolean hasWizard(){ return wizard; }
	public boolean hasCommander(){ return commander; }
	public boolean isBlessed(){ return blessed; }
}
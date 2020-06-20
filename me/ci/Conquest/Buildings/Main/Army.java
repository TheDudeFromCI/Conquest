
package me.ci.Conquest.Buildings.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.BuildingType;
import me.ci.Conquest.Buildings.Constructors.ResourceType;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import me.ci.Conquest.Military.ArmyFormation;
import me.ci.Conquest.Military.ArmyType;
import me.ci.Conquest.Misc.Direction;
import me.ci.Conquest.Misc.HeldArmy;
import me.ci.Conquest.Misc.PathFinder;
import me.ci.Conquest.Misc.PathFinder.DistanceForumula;
import me.ci.Conquest.Misc.PathFinder.Node;
import me.ci.Conquest.Misc.PathFinder.PathBuilder;
import me.ci.Conquest.Misc.PathFinder.PathFindingException;
import me.ci.Conquest.Misc.PriorityList;
import me.ci.Conquest.Textures.BuildingTexture;
import me.ci.Conquest.Textures.ConquestTextures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

public class Army extends Building{
	public ArmyType type;
	public ArmyType realtype;
	public ArmyFormation formation;
	private boolean selected;
	public int stamina;
	private Building attacking;
	public Direction direction = Direction.NORTH;
	public long reverttime = -1;
	public boolean nullified;
	public boolean wizard;
	public boolean commander;
	public long flytime = -1;
	public long slaytime = -1;
	public long illusiontime = -1;
	public long protecttime = -1;
	public Army protector = null;
	public boolean blessed = false;
	public long speartime = -1;
	public long pillagetime = -1;
	private long actiontime;
	private final ArrayList<HeldArmy> heldarmies = new ArrayList<>();
	private static final ArrayList<Army> armies = new ArrayList<>();
	private static Timer timer;
	public PathFinder pf;
	public final PathBuilder path = new PathBuilder(){
		public ArrayList<Node> getOpenPoints(Node o){
			final ArrayList<Node> nodes = new ArrayList<Node>();
			if(pf==null
					||nwc==null)return nodes;
			final Node n1 = pf.getNodeAt(o.getX()+1, o.getY());
			final Node n2 = pf.getNodeAt(o.getX()-1, o.getY());
			final Node n3 = pf.getNodeAt(o.getX(), o.getY()+1);
			final Node n4 = pf.getNodeAt(o.getX(), o.getY()-1);
			final Node n5 = pf.getNodeAt(o.getX()+1, o.getY()+1);
			final Node n6 = pf.getNodeAt(o.getX()+1, o.getY()-1);
			final Node n7 = pf.getNodeAt(o.getX()-1, o.getY()+1);
			final Node n8 = pf.getNodeAt(o.getX()-1, o.getY()-1);
			n1.setMoveCost(10);
			n2.setMoveCost(10);
			n3.setMoveCost(10);
			n4.setMoveCost(10);
			n5.setMoveCost(14);
			n6.setMoveCost(14);
			n7.setMoveCost(14);
			n8.setMoveCost(14);
			if(!PlotMg.getPlotAt(nwc.getWorld(), n1.getX(), n1.getY()).isClaimed())nodes.add(n1);
			if(!PlotMg.getPlotAt(nwc.getWorld(), n2.getX(), n2.getY()).isClaimed())nodes.add(n2);
			if(!PlotMg.getPlotAt(nwc.getWorld(), n3.getX(), n3.getY()).isClaimed())nodes.add(n3);
			if(!PlotMg.getPlotAt(nwc.getWorld(), n4.getX(), n4.getY()).isClaimed())nodes.add(n4);
			if(!PlotMg.getPlotAt(nwc.getWorld(), n5.getX(), n5.getY()).isClaimed())nodes.add(n5);
			if(!PlotMg.getPlotAt(nwc.getWorld(), n6.getX(), n6.getY()).isClaimed())nodes.add(n6);
			if(!PlotMg.getPlotAt(nwc.getWorld(), n7.getX(), n7.getY()).isClaimed())nodes.add(n7);
			if(!PlotMg.getPlotAt(nwc.getWorld(), n8.getX(), n8.getY()).isClaimed())nodes.add(n8);
			final int h = PlotMg.getPlotAt(nwc).getHeight();
			for(Node n : nodes){ n.setMoveCost(n.getMoveCost()*(Math.abs(h-PlotMg.getPlotAt(nwc.getWorld(), n.getX(), n.getY()).getHeight())+1)); }
			return nodes;
		}
	};
	public Army(Chunk nwc, Kingdom kingdom, int amount, ArmyType type, int level){
		super(nwc, kingdom, new BuildingTexture());
		this.hp=amount;
		this.realtype=type;
		this.formation=ArmyFormation.A;
		this.type=type;
		this.lvl=level;
		this.stamina=getMaxStamina();
		armies.add(this);
		updateGraphics(false);
	}
	public Army(Kingdom kingdom, String code, Chunk nwc, long id){
		super(kingdom, code, nwc, id);
		armies.add(this);
	}
	@Override
	public String getSaveStats(){
		return getType().getId()
				+";"+getNorthWestCourner().getX()+","+getNorthWestCourner().getZ()
				+";"+realtype.getId()
				+";"+hp
				+";"+formation.getId()
				+";"+selected
				+";"+stamina
				+";"+lvl
				+";"+(pf==null?"0":pf.getEnd().getX()+","+pf.getEnd().getY())
				+";"+nullified
				+";"+wizard
				+";"+commander
				+";"+(heldarmies.isEmpty()?"0":saveHeldArmies());
	}
	@Override
	public void loadSaveStats(String code){
		try{
			String[] s = code.split(";");
			this.type=ArmyType.getById(Integer.valueOf(s[2]));
			this.realtype=type;
			this.hp=Integer.valueOf(s[3]);
			this.formation=ArmyFormation.getById(Integer.valueOf(s[4]));
			this.selected=Boolean.valueOf(s[5]);
			this.stamina=Integer.valueOf(s[6]);
			this.attacking=null;
			this.lvl=Integer.valueOf(s[7]);
			if(!s[8].equals("0"))setMoveGoal(Integer.valueOf(s[8].split(",")[0]), Integer.valueOf(s[8].split(",")[1]));
			this.nullified=Boolean.valueOf(s[9]);
			this.wizard=Boolean.valueOf(s[10]);
			this.commander=Boolean.valueOf(s[11]);
			if(!s[12].equals("0"))loadHeldArmies(s[12]);
		}catch(final Exception exception){ exception.printStackTrace(); }
	}
	public void updateGraphics(final boolean threadsleep){
		if(!type.isSiegeWeapon())ConquestTextures.getTexture(BuildingType.ARMY, lvl).buildAt(nwc, this);
		else{
			int id;
			if(type==ArmyType.ANTIMAGIC_ONAGER)id=8;
			else if(type==ArmyType.BALLISTA)id=12;
			else if(type==ArmyType.BATTERING_RAM)id=16;
			else if(type==ArmyType.BRIDGEMAKER)id=20;
			else if(type==ArmyType.CATAPULT)id=24;
			else if(type==ArmyType.COW_TOSSER)id=28;
			else if(type==ArmyType.MANGONAL)id=32;
			else if(type==ArmyType.SIEGETOWER)id=36;
			else id=40;
			id+=direction.getId();
			ConquestTextures.getTexture(BuildingType.ARMY, id).buildAt(nwc, this);
		}
	}
	public String[] getInfo(){
		String[] s = new String[14];
		s[0]=ChatColor.DARK_GRAY+"=================================";
		s[1]=ChatColor.DARK_AQUA+"City Name .............. "+ChatColor.GRAY+getKingdom().getName();
		s[2]=ChatColor.DARK_AQUA+"Hit Points ............. "+ChatColor.AQUA+getHp()+"/"+getMaxHp();
		s[3]=ChatColor.DARK_AQUA+"Unit Type .............. "+ChatColor.GRAY+type.getName();
		s[4]=ChatColor.DARK_AQUA+"Stamina ................ "+ChatColor.AQUA+stamina+"/"+getMaxStamina();
		s[5]=ChatColor.DARK_AQUA+"Is Attacking ........... "+ChatColor.GRAY+(isAttacking()?"Yes":"No");
		s[6]=ChatColor.DARK_AQUA+"Is Moving .............. "+ChatColor.GRAY+(pf!=null?"Yes":"No");
		s[7]=ChatColor.DARK_AQUA+"Move Target ............ "+ChatColor.GRAY+(pf!=null?pf.getEnd().getX()+", "+pf.getEnd().getY():"Not Moving");
		s[8]=ChatColor.DARK_AQUA+"Can Attack ............. "+ChatColor.GRAY+canAttack();
		s[9]=ChatColor.DARK_AQUA+"Direction .............. "+ChatColor.GRAY+direction.name().toLowerCase();
		s[10]=ChatColor.DARK_AQUA+"Nullified .............. "+ChatColor.GRAY+(nullified?"Yes":"No");
		s[11]=ChatColor.DARK_AQUA+"Has Wizard ............. "+ChatColor.GRAY+(wizard?"Yes":"No");
		s[12]=ChatColor.DARK_AQUA+"Has Commander .......... "+ChatColor.GRAY+(commander?"Yes":"No");
		s[13]=ChatColor.DARK_GRAY+"=================================";
		return s;
	}
	public ArmyType getArmyType(){ return this.type; }
	public ArmyFormation getFormation(){ return formation; }
	public void setFormation(ArmyFormation formation){
		this.formation=formation;
		if(isCamping()){
			attacking=null;
			pf=null;
		}
		updateStatus();
	}
	public void setSelected(boolean selected){
		this.selected=selected;
		updateStatus();
	}
	public int getStamina(){ return stamina; }
	public double getStaminaPercent(){ return stamina/(double)getMaxStamina()*100; }
	public boolean isCamping(){ return formation==ArmyFormation.P; }
	public boolean isSelected(){ return selected; }
	public Direction getDirection(){ return direction; }
	public void setStamina(int stamina){
		this.stamina=stamina;
		updateStatus();
	}
	public void moveTo(final Plot to, boolean takestamina){
		int h = Math.abs(to.getHeight()-PlotMg.getPlotAt(nwc).getHeight())+1;
		if(flytime>-1)takestamina=false;
		if(takestamina&&getStamina()>=h)setStamina(getStamina()-h);
		else return;
		kingdom.removeBuilding(Army.this, false, true, false);
		nwc=to.getChunk();
		List<Building> builds;
		if(buildings.containsKey(kingdom))builds=buildings.get(kingdom);
		else builds=new LinkedList<Building>();
		builds.add(Army.this);
		buildings.put(kingdom, builds);
		kingdom.buildBuilding(Army.this, to.getChunk(), new BuildingTexture());
		to.setKingdom(kingdom, this);
		updateDirection();
		tellWatchTowers("There is army movement near your kingdom!", null, "The army units are from "+kingdom.getName()+", owned by "+kingdom.getOwner()+".");
		updateGraphics(false);
	}
	public boolean isAttacking(){ return attacking!=null; }
	public int getMaxStamina(){ return 20; }
	public void setTarget(Building target){
		attacking=target;
		pf=null;
		updateStatus();
		updateDirection();
	}
	public Building getTarget(){ return attacking; }
	private boolean checkMoat(){
		if(attacking==null)return true;
		if(attacking.getType()!=BuildingType.MOAT)return true;
		return false;
	}
	private void move(){
		attacking=null;
		if(pf==null)return;
		try{
			if(PlotMg.getPlotAt(getWorld(), pf.getEnd().getX(), pf.getEnd().getX()).isClaimed())return;
			pf.findPath(DistanceForumula.DISTANCE, 50);
			if(pf.getPath().hasNext(true)){
				pf.getPath().getNext();
				if(pf.getPath().hasNext(true)){
					final Node next = pf.getPath().getNext();
					moveTo(PlotMg.getPlotAt(nwc.getWorld(), next.getX(), next.getY()), true);
					if(pf.getEnd()!=next)setMoveGoal(pf.getEnd().getX(), pf.getEnd().getY());
					else pf=null;
				}else pf=null;
			}else pf=null;
		}catch(Exception exception){}
	}
	public void attack(final double percent, final boolean cost, final boolean message, final boolean takeblessing){
		dropTnt();
		int damage = randomizeAttack();
		boolean weakside = false;
		if(attacking instanceof Army){
			Army army = (Army)attacking;
			if(attacking.getNorthWestCourner().getZ()<nwc.getZ()
					&&attacking.getNorthWestCourner().getX()==nwc.getX()){
				if(!army.getFormation().usesSide(Direction.NORTH))weakside=true;
			}else if(attacking.getNorthWestCourner().getZ()>nwc.getZ()
					&&attacking.getNorthWestCourner().getX()==nwc.getX()){
				if(!army.getFormation().usesSide(Direction.SOUTH))weakside=true;
			}else if(attacking.getNorthWestCourner().getX()>nwc.getX()
					&&attacking.getNorthWestCourner().getZ()==nwc.getZ()){
				if(!army.getFormation().usesSide(Direction.EAST))weakside=true;
			}else if(!army.getFormation().usesSide(Direction.WEST))weakside=true;
		}
		damage*=percent;
		damage/=formation.getSideCount();
		if(weakside)damage*=2;
		double h = PlotMg.getPlotAt(nwc).getHeight()-PlotMg.getPlotAt(attacking.getNorthWestCourner()).getHeight();
		h/=5;
		h++;
		h=Math.min(h, 2);
		h=Math.max(h, 0.2);
		if(wizard)h=Math.max(h, 1);
		damage*=h;
		if(blessed){
			damage*=1.5;
			if(takeblessing)blessed=false;
		}
		if(attacking.hasDivineProtection()
				&&illusiontime>-1)attacking.damage(kingdom, 0);
		if(attacking.hasTrap())damage(kingdom, damage);
		else if(illusiontime>-1
				&&attacking instanceof Army
				&&((Army)attacking).speartime>-1){
			if(((Army)attacking).blessed){
				damage*=1.5;
				((Army)attacking).blessed=false;
			}
			damage(kingdom, damage);
		}else if(illusiontime==-1){
			if(pillagetime>-1)stealGold(damage/10);
			attacking.damage(kingdom, damage);
			tellWatchTowers("Your kingdom is under attack!", attacking.getKingdom(), "The attacker is from the kingdom of "+kingdom+", owned by "+kingdom.getOwner()+".");
		}
		else{
			if(hp==1)damage(kingdom, 0);
			else{
				final int todo = Math.min(hp-1, damage);
				damage(kingdom, todo);
			}
		}
		if(attacking.getHp()<1){
			if(!(attacking instanceof Army)
					&&!(attacking instanceof Land)
					&&attacking.getKingdom().getMorale()>0)attacking.getKingdom().removeMorale(1);
			attacking=null;
			if(kingdom.getMorale()<100)kingdom.addMorale(1);
		}
		if(cost)setStamina(getStamina()-1);
		kingdom.collectResources(ResourceType.ARMS, (int)(hp*0.1));
	}
	private void stealGold(final int amount){
		attacking.getKingdom().removeMoney(amount);
		kingdom.addMoney(amount);
		tellWatchTowers("Gold was stolen from your kingdom!", attacking.getKingdom(), "The pillagers are from the kingdom of "+kingdom+", owned by "+kingdom.getOwner()+".");
	}
	private void dropTnt(){
		final double x = Math.random()*16*attacking.getLength()+attacking.getNorthWestCourner().getBlock(0, 0, 0).getX();
		final double z = Math.random()*16*attacking.getLength()+attacking.getNorthWestCourner().getBlock(0, 0, 0).getZ();
		final double y = 90+PlotMg.getPlotAt(attacking.getNorthWestCourner()).getHeight();
		final Location l = new Location(attacking.getWorld(), x, y, z);
		TNTPrimed tnt = attacking.getWorld().spawn(l, TNTPrimed.class);
		tnt.setVelocity(new Vector(0, -500, 0));
		tnt.setFuseTicks(20);
		Sound s;
		final int r = (int)(Math.random()*3);
		if(r==0)s=Sound.ANVIL_USE;
		else if(r==1)s=Sound.ANVIL_LAND;
		else s=Sound.ANVIL_BREAK;
		for(Player p : Bukkit.getOnlinePlayers()){ p.playSound(l, s, 1.0f, 1.0f); }
	}
	private int randomizeAttack(){
		int normal = Math.max((int)(type.getAttack(lvl)*hp), 1);
		return (int)(Math.random()*(normal/2.0)+(normal/2.0));
	}
	public boolean isTargetInRange(){
		boolean direction = false;
		if(attacking.getNorthWestCourner().getZ()<nwc.getZ()
				&&attacking.getNorthWestCourner().getX()==nwc.getX()){
			if(formation.usesSide(Direction.NORTH)
					&&nwc.getZ()-attacking.getNorthWestCourner().getZ()<=type.getRange())direction=true;
		}else if(attacking.getNorthWestCourner().getZ()>nwc.getZ()
				&&attacking.getNorthWestCourner().getX()==nwc.getX()){
			if(formation.usesSide(Direction.SOUTH)
					&&attacking.getNorthWestCourner().getZ()-nwc.getZ()<=type.getRange())direction=true;
		}else if(attacking.getNorthWestCourner().getX()>nwc.getX()
				&&attacking.getNorthWestCourner().getZ()==nwc.getZ()){
			if(formation.usesSide(Direction.EAST)
					&&attacking.getNorthWestCourner().getX()-nwc.getX()<=type.getRange())direction=true;
		}else{
			if(formation.usesSide(Direction.WEST)
					&&nwc.getX()-attacking.getNorthWestCourner().getX()<=type.getRange())direction=true;
		}
		direction=direction&&Math.abs(PlotMg.getPlotAt(attacking.getNorthWestCourner()).getHeight()-PlotMg.getPlotAt(nwc).getHeight())/5<type.getRange();
		return direction;
	}
	@Override
	public int getMaxHp(){ return ArmyType.getMaxAmount(lvl); }
	public synchronized void setMoveGoal(final int x, final int y){
		try{
			if(pf==null){
				setupPathfinder(x, y);
			}else synchronized(pf){ setupPathfinder(x, y); }
		}catch(final Exception exception){
			exception.printStackTrace();
			pf=null;
		}
	}
	private void setupPathfinder(final int x, final int y)throws PathFindingException{
		pf=new PathFinder();
		pf.setStart(pf.getNodeAt(nwc.getX(), nwc.getZ()));
		pf.setEnd(pf.getNodeAt(x, y));
		pf.setPathBuilder(path);
	}
	public boolean isMoving(){ return pf!=null; }
	public String canAttack(){
		if(isCamping())return "No, this troop is camping.";
		if(getStamina()<=0)return "No, this troop has no stamina.";
		if(!isAttacking())return "No, This troop has no target.";
		if(!isTargetInRange())return "No, target out of range.";
		if(!kingdom.isOwnerOnline())return "No, the owner of this troop's kingdom is offline.";
		if(attacking.isBuildingOrUpgrading())return "No, the target is being build ot upgraded.";
		if(!isOffical())return "No, this troop is corrupt.";
		if(!attacking.isOffical())return "No, the target is corrupt.";
		if(!checkMoat())return "No, the target is water type.";
		if(kingdom.getResourceLevels(ResourceType.ARMS)<(int)(hp*0.1))return "No, not enough arms to attack.";
		if(!attacking.getKingdom().isOwnerOnline())return "No, the owner of the target's kingdom is offline.";
		return "Yes";
	}
	@Override
	public void damage(final Kingdom k, int amount){
		if(protecttime>-1
				&&protector.isOffical()){
			amount/=2;
			protector.damage(k, amount);
		}
		int newhp = this.hp-amount;
		if(newhp>0){
			byte segbefore = getDamageSegment();
			this.hp=newhp;
			byte segafter = getDamageSegment();
			if(segbefore!=segafter)updateStatus();
		}else{
			delete(true, false);
			if(kingdom.getMorale()>0)kingdom.removeMorale(1);
		}
	}
	@Override
	public void delete(final boolean a, final boolean b){
		new Thread(new Runnable(){
			public void run(){
				kingdom.removeBuilding(Army.this, false, a, b);
				if(PlayerMg.getSelectedArmy(kingdom.getOwner())==Army.this)PlayerMg.setSelectedArmy(kingdom.getOwner(), null);
				synchronized(armies){ armies.remove(this); }
			}
		}).start();
	}
	private void updateDirection(){
		if(attacking==null)return;
		final Direction b4 = direction;
		final double d = Math.atan2(nwc.getZ()-attacking.getNorthWestCourner().getZ(), nwc.getX()-attacking.getNorthWestCourner().getX())*180/Math.PI%360;
		if(45<=d&&d<=135)direction=Direction.NORTH;
		else if(135<=d&&d<=225)direction=Direction.WEST;
		else if(225<=d&&d<=315)direction=Direction.SOUTH;
		else direction=Direction.EAST;
		if(b4!=direction)updateGraphics(false);
	}
	private void tellWatchTowers(final String message, final Kingdom king, final String advanced){
		if(king==null){
			for(Kingdom k : KingdomMg.getKingdoms()){
				if(k==kingdom)continue;
				if(k.isRuins())continue;
				if(!k.isOwnerOnline())continue;
				messageTower(message, k, advanced);
			}
		}else messageTower(message, king, advanced);
	}
	private void messageTower(final String message, final Kingdom k, final String advanced){
		final Player p = Bukkit.getPlayer(k.getOwner());
		boolean tell = false;
		Plot plot;
		checker:for(int x = -10; x<=10; x++){
			for(int z = -10; z<=10; z++){
				plot=PlotMg.getPlotAt(getWorld(), x+nwc.getX(), z+nwc.getZ());
				if(plot.isKingdomPlot()
						&&plot.getKingdom()==k){
					tell=true;
					break checker;
				}
			}
		}
		if(tell)p.sendMessage(ChatColor.DARK_PURPLE+message);
		WatchTower tower;
		double d;
		for(Building b : Building.getBuildings(k)){
			if(b instanceof WatchTower){
				tower=(WatchTower)b;
				d=getDistance(tower);
				if(d<=tower.getRange()){
					if(Math.random()*100<tower.getSightChance()){
						if(!tell)p.sendMessage(ChatColor.DARK_PURPLE+message);
						p.sendMessage(ChatColor.DARK_PURPLE+advanced);
						return;
					}
				}
			}
		}
	}
	public void transform(final ArmyType type){
		this.type=type;
		this.reverttime=System.currentTimeMillis()+300000;
		updateGraphics(false);
	}
	private Building checkForNearbyThreats(final boolean assign){
		final PriorityList<Building> list = new PriorityList<>();
		Plot plot;
		for(int x = -8; x<=8; x++){
			for(int z = -8; z<=8; z++){
				plot=PlotMg.getPlotAt(getWorld(), x+nwc.getX(), z+nwc.getZ());
				if(plot.isKingdomPlot()
						&&plot.getKingdom()!=kingdom
						&&!plot.getKingdom().isRuins()
						&&plot.getKingdom().isOwnerOnline()){
					if(plot.getBuilding().getType()==BuildingType.ARMY)list.add(plot.getBuilding(), (int)((9-getDistance(plot.getBuilding()))*10));
				}
			}
		}
		final Building best = list.getMostImportant();
		if(best!=null)attacking=best;
		return best;
	}
	private boolean isProperAngle(){
		if(attacking==null)return false;
		if(attacking.getNorthWestCourner().getZ()==nwc.getZ()
				||attacking.getNorthWestCourner().getX()==nwc.getX())return true;
		return false;
	}
	private boolean isWithinRange(){
		if(attacking==null)return false;
		final int rangex = Math.abs(attacking.getNorthWestCourner().getX()-nwc.getX());
		final int rangez = Math.abs(attacking.getNorthWestCourner().getZ()-nwc.getZ());
		if(rangex<=type.getRange()
				&&rangez<=type.getRange())return Math.abs(PlotMg.getPlotAt(attacking.getNorthWestCourner()).getHeight()-PlotMg.getPlotAt(nwc).getHeight())/5<type.getRange();
		return false;
	}
	private Direction getEnemyDirection(){
		if(attacking==null)return null;
		if(isProperAngle()){
			if(attacking.getNorthWestCourner().getX()>nwc.getX())return Direction.EAST;
			else if(attacking.getNorthWestCourner().getX()<nwc.getX())return Direction.WEST;
			else if(attacking.getNorthWestCourner().getZ()>nwc.getZ())return Direction.SOUTH;
			return Direction.NORTH;
		}
		return null;
	}
	private boolean chooseBestAttackPosition(){
		if(attacking==null)return false;
		final int baseX = attacking.getNorthWestCourner().getX();
		final int baseZ = attacking.getNorthWestCourner().getZ();
		final Plot myP = PlotMg.getPlotAt(nwc);
		final Plot urP = PlotMg.getPlotAt(attacking.getNorthWestCourner());
		final PriorityList<Plot> spots = new PriorityList<>();
		spots.add(myP, 1);
		Plot plot;
		//Check east side
		for(int x = 1; x<=type.getRange(); x++){
			for(int o = 0; o<attacking.getType().getLength(); o++){
				plot=PlotMg.getPlotAt(nwc.getWorld(), x+baseX, baseZ);
				if(plot.isClaimed())continue;
				spots.add(plot, getPriority(plot, myP, urP, Direction.EAST));
			}
		}
		//Chect west side
		for(int x = -1; x>=-type.getRange(); x--){
			for(int o = 0; o<attacking.getType().getLength(); o++){
				plot=PlotMg.getPlotAt(nwc.getWorld(), x+baseX, baseZ);
				if(plot.isClaimed())continue;
				spots.add(plot, getPriority(plot, myP, urP, Direction.WEST));
			}
		}
		//Check south side
		for(int z = 1; z<=type.getRange(); z++){
			for(int o = 0; o<attacking.getType().getLength(); o++){
				plot=PlotMg.getPlotAt(nwc.getWorld(), baseX, z+baseZ);
				if(plot.isClaimed())continue;
				spots.add(plot, getPriority(plot, myP, urP, Direction.SOUTH));
			}
		}
		//Check north side
		for(int z = -1; z>=-type.getRange(); z--){
			for(int o = 0; o<attacking.getType().getLength(); o++){
				plot=PlotMg.getPlotAt(nwc.getWorld(), baseX, z+baseZ);
				if(plot.isClaimed())continue;
				spots.add(plot, getPriority(plot, myP, urP, Direction.NORTH));
			}
		}
		final Plot best = spots.getMostImportant();
		if(!canAttackFrom(best))return false;
		if(best!=myP){
			setMoveGoal(best.getX(), best.getZ());
			return true;
		}
		return false;
	}
	private boolean canAttackFrom(final Plot plot){
		return isWithinRange()
				&&isProperAngle();
	}
	private int getPriority(final Plot plot, final Plot myP, final Plot urP, final Direction d){
		if(!isHeightRange(plot.getHeight(), urP.getHeight()))return -1;
		int heightBonus = plot.getHeight()-urP.getHeight();
		if(wizard)heightBonus=Math.max(heightBonus, 0);
		final int walk = isPlotEasilyReachable(plot);
		if(walk==-1)return -1;
		final boolean flank = (attacking instanceof Army?!((Army)attacking).getFormation().usesSide(d):false);
		final int distance = (int)getDistance(attacking, plot);
		return 100+(heightBonus*5)+(int)(-walk*1.5)+(flank?3:0)+(int)(distance*2);
	}
	private boolean isHeightRange(final int h1, final int h2){ return Math.abs(h1-h2)/5<type.getRange(); }
	private int isPlotEasilyReachable(final Plot plot){
		try{
			final PathFinder pf = new PathFinder();
			pf.setStart(pf.getNodeAt(nwc.getX(), nwc.getZ()));
			pf.setEnd(pf.getNodeAt(plot.getX(), plot.getZ()));
			pf.setPathBuilder(path);
			pf.findPath(DistanceForumula.MANHATTEN, 15);
			int cost = 0;
			Plot temp;
			Plot b4 = PlotMg.getPlotAt(nwc);
			Node node;
			pf.getPath().getNext();
			while(pf.getPath().hasNext(true)){
				node=pf.getPath().getNext();
				temp=PlotMg.getPlotAt(getWorld(), node.getX(), node.getY());
				cost+=Math.abs(temp.getHeight()-b4.getHeight())+1;
				b4=temp;
			}
			return cost;
		}catch(final Exception exception){}
		return -1;
	}
	private void prepareAttack(){
		if(!commander)return;
		if(kingdom.isRuins())return;
		if(!kingdom.isOwnerOnline())return;
		new Thread(new Runnable(){
			public void run(){
				//Check stamina
				if(stamina<1){
					if(formation!=ArmyFormation.P)setFormation(ArmyFormation.P);
					return;
				}
				if(isMoving()){
					if(stamina>=10
							&&formation==ArmyFormation.P)setFormation(ArmyFormation.A);
					return;
				}
				//Check to see if target is worth fighting
				if(attacking!=null){
					if(attacking.getKingdom().isRuins())attacking=null;
					if(!attacking.getKingdom().isOwnerOnline())attacking=null;
					if(getDistance(attacking)>15)attacking=null;
				}
				//Check if theres an enemy
				if(attacking==null
						&&checkForNearbyThreats(true)==null){  //Check for nearby enemies
					//Camp to recover in this safty
					if(stamina<20)setFormation(ArmyFormation.P);
					else setFormation(ArmyFormation.A);
					return;
				}
				//Check if camping and needs to camp
				if(formation==ArmyFormation.P
						&&stamina<8)return;
				//Make sure target is in prefect position
				if(!chooseBestAttackPosition())checkForNearbyThreats(true);
				//Check sides
				final Direction d = getEnemyDirection();
				if(d!=null){
					boolean change = false;
					if(d==Direction.NORTH){
						if(direction!=Direction.NORTH){
							direction=Direction.NORTH;
							change=true;
						}
						if(formation!=ArmyFormation.L){
							formation=ArmyFormation.L;
							change=true;
						}
					}
					if(d==Direction.EAST){
						if(direction!=Direction.EAST){
							direction=Direction.EAST;
							change=true;
						}
						if(formation!=ArmyFormation.M){
							formation=ArmyFormation.M;
							change=true;
						}
					}
					if(d==Direction.SOUTH){
						if(direction!=Direction.SOUTH){
							direction=Direction.SOUTH;
							change=true;
						}
						if(formation!=ArmyFormation.N){
							formation=ArmyFormation.N;
							change=true;
						}
					}
					if(d==Direction.WEST){
						if(direction!=Direction.WEST){
							direction=Direction.WEST;
							change=true;
						}
						if(formation!=ArmyFormation.O){
							formation=ArmyFormation.O;
							change=true;
						}
					}
					if(change)updateGraphics(false);
				}else setFormation(ArmyFormation.A);
			}
		}).start();
	}
	public void addWizard(){
		wizard=true;
		updateStatus();
	}
	public void addCommander(){
		commander=true;
		updateStatus();
	}
	public boolean isTargetAttackable(final boolean ignoreangle){
		return !isCamping()
				&&getStamina()>0
				&&isAttacking()
				&&(isTargetInRange()
				||(ignoreangle
				&&isWithinRange()))
				&&kingdom.isOwnerOnline()
				&&!attacking.isBuildingOrUpgrading()
				&&isOffical()
				&&attacking.isOffical()
				&&kingdom.getResourceLevels(ResourceType.ARMS)>=(int)(hp*0.1)
				&&checkMoat()
				&&attacking.getKingdom().isOwnerOnline();
	}
	public void slayDragons(){
		final int offX = nwc.getX();
		final int offZ = nwc.getZ();
		final Building previousTarget = attacking;
		Plot plot;
		for(int x = -3; x<=3; x++){
			for(int z = -3; z<=3; z++){
				plot=PlotMg.getPlotAt(getWorld(), x+offX, z+offZ);
				if(plot.isKingdomPlot()
						&&plot.getKingdom()!=kingdom
						&&plot.getBuilding() instanceof Army
						&&((Army)plot.getBuilding()).getArmyType()==ArmyType.DRAGON_RIDER){
					attacking=plot.getBuilding();
					if(isTargetAttackable(true))attack(1.0, false, false, false);
				}
			}
		}
		blessed=false;
		attacking=previousTarget;
	}
	public void protect(final Army a){
		protecttime=-1;
		protector=a;
	}
	public String saveHeldArmies(){
		String s = "";
		for(HeldArmy h : heldarmies)s+="#"+h.save();
		while(s.startsWith("#"))s=s.substring(1);
		return s;
	}
	public void loadHeldArmies(final String s){
		final String[] a = s.split("#");
		for(String b : a)heldarmies.add(new HeldArmy(b));
	}
	public boolean hasWizard(){ return wizard; }
	public boolean hasCommander(){ return commander; }
	public ArmyType getRealArmyType(){ return realtype; }
	public void setRealArmyType(final ArmyType type){ realtype=type; }
	public void usePower(final Kingdom k, final String advanced){ tellWatchTowers("A military unit from "+kingdom+" is using abilities against your kingdom!", k, advanced); }
	public double getDistance(final Building t){ return Math.sqrt(Math.pow(t.getNorthWestCourner().getX()-nwc.getX(), 2)+Math.pow(t.getNorthWestCourner().getZ()-nwc.getZ(), 2)); }
	public double getDistance(final Building t, final Plot p){ return Math.sqrt(Math.pow(t.getNorthWestCourner().getX()-p.getX(), 2)+Math.pow(t.getNorthWestCourner().getZ()-p.getZ(), 2)); }
	public boolean isNullified(){ return nullified; }
	public void nullify(){ nullified=true; }
	public boolean isFlying(){ return flytime>-1; }
	public void fly(){ flytime=System.currentTimeMillis()+300000; }
	public boolean isSlaying(){ return slaytime>-1; }
	public void slay(){ slaytime=System.currentTimeMillis()+180000; }
	public boolean isUnderIllusion(){ return illusiontime>-1; }
	public void illusion(){ illusiontime=System.currentTimeMillis()+60000; }
	public boolean isProtected(){ return protecttime>-1; }
	public boolean isBlessed(){ return blessed; }
	public void bless(){ blessed=true; }
	public void takeBlessing(){ blessed=false; }
	public boolean isInSpearMode(){ return speartime>-1; }
	public void spearMode(){ speartime=System.currentTimeMillis()+240000; }
	public void pillage(){ pillagetime=System.currentTimeMillis()+300000; }
	public boolean isPillaging(){ return pillagetime>-1; }
	public void addHeldArmy(final Army a){ heldarmies.add(new HeldArmy(a)); }
	public ArrayList<HeldArmy> getHeldArmies(){ return heldarmies; }
	public void removeHeldArmy(final HeldArmy a){ heldarmies.remove(a); }
	public static void startTimer(){
		timer=new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
				try{
					synchronized(armies){
						for(Army a : armies){
							try{
								if(a.actiontime<=System.currentTimeMillis()){
									if(a.attacking!=null
											&&!a.attacking.isOffical())a.attacking=null;
									a.prepareAttack();
									if(a.isTargetAttackable(false))a.attack(1.0, true, true, true);
									if(!a.isCamping()
											&&a.getStamina()>0
											&&a.isMoving())a.move();
									a.actiontime=(long)(System.currentTimeMillis()+a.type.getReload(a.lvl)*1000L);
								}
								if(a.reverttime!=-1
										&&a.reverttime<=System.currentTimeMillis()){
									a.reverttime=-1;
									a.type=a.realtype;
									a.updateGraphics(false);
								}
								if(a.flytime!=-1
										&&a.flytime<=System.currentTimeMillis())a.flytime=-1;
								if(a.illusiontime!=-1
										&&a.illusiontime<=System.currentTimeMillis())a.illusiontime=-1;
								if(a.protecttime!=-1
										&&a.protecttime<=System.currentTimeMillis())a.protecttime=-1;
								if(a.speartime!=-1
										&&a.speartime<=System.currentTimeMillis())a.speartime=-1;
								if(a.pillagetime!=-1
										&&a.pillagetime<=System.currentTimeMillis())a.pillagetime=-1;
								if(a.slaytime!=-1
										&&a.slaytime<=System.currentTimeMillis())a.slaytime=-1;
								else if(a.slaytime>-1)a.slayDragons();
							}catch(final Exception exception){ exception.printStackTrace(); }
						}
					}
				}catch(Exception exception){ exception.printStackTrace(); }
			}
		}, 1000, 1000);
	}
	public static void stopTimer(){ if(timer!=null)timer.cancel(); }
}
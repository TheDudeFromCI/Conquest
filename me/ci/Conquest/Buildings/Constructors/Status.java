package me.ci.Conquest.Buildings.Constructors;

import me.ci.WhCommunity;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.Military.ArmyFormation;
import me.ci.Conquest.Military.ArmyType;
import me.ci.Conquest.Misc.Direction;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Status{
	private final StatusType type;
	private final byte damage;
	private final Building building;
	private final ArmyFormation formation;
	private final boolean selected;
	private final Chunk chunk;
	private int STATUS_LEVEL = 3;
	private int startx = 5;
	private int startz = 5;
	private int endx = 5;
	private int endz = 5;
	public Status(Building building, StatusType type, byte damage){
		this.building=building;
		this.type=type;
		this.damage=damage;
		this.formation=ArmyFormation.P;
		this.selected=false;
		this.chunk=this.building.getNorthWestCourner();
		STATUS_LEVEL+=PlotMg.getPlotAt(chunk).getHeight();
	}
	public StatusType getStatusType(){
		return this.type;
	}
	public byte getDamageSegments(){
		return this.damage;
	}
	public Building getBuilding(){
		return this.building;
	}
	public ArmyFormation getFormation(){
		return this.formation;
	}
	public boolean isSelected(){
		return this.selected;
	}
	private void drawBuilding(){
		if(building instanceof Army){
			Army army = (Army)building;
			ArmyType type = army.getArmyType();
			ArmyFormation form = army.getFormation();
			double percent = army.getStaminaPercent();
			int mx = building.getNorthWestCourner().getBlock(0, 0, 0).getX();
			int mz = building.getNorthWestCourner().getBlock(0, 0, 0).getZ();
			for(int x = 0; x<16; x++){
				for(int z = 0; z<16; z++){
					if(army.getStatus()!=StatusType.NORMAL){
						if(mx+x==getStartX()
								||mx+x==getEndX()
								||mz+z==getStartZ()
								||mz+z==getEndZ())army.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, this.type.getColor(), true);
						else z=15;
					}else{
						if((mx+x==getStartX()||mx+x==getEndX())&&(mz+z==getStartZ()||mz+z==getEndZ())){
							army.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)0, true);
						}else if(mx+x==getStartX()){
							if(form.usesSide(Direction.WEST))this.building.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)14, true);
							else army.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)6, true);
						}else if(mx+x==getEndX()){
							if(form.usesSide(Direction.EAST))this.building.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)14, true);
							else army.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)6, true);
						}else if(mz+z==getStartZ()){
							if(form.usesSide(Direction.NORTH))this.building.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)14, true);
							else army.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)6, true);
						}else if(mz+z==getEndZ()){
							if(form.usesSide(Direction.SOUTH))this.building.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)14, true);
							else army.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)6, true);
						}else army.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(type.getMaterialId(), type.getData(), true);
					}
				}
			}
			if(army.isSelected()){
				army.getWorld().getBlockAt(mx+2, STATUS_LEVEL, mz+2).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+2, STATUS_LEVEL, mz+3).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+3, STATUS_LEVEL, mz+2).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+12, STATUS_LEVEL, mz+2).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+13, STATUS_LEVEL, mz+2).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+13, STATUS_LEVEL, mz+3).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+2, STATUS_LEVEL, mz+12).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+2, STATUS_LEVEL, mz+13).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+3, STATUS_LEVEL, mz+13).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+12, STATUS_LEVEL, mz+13).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+13, STATUS_LEVEL, mz+12).setTypeIdAndData(35, (byte)4, true);
				army.getWorld().getBlockAt(mx+13, STATUS_LEVEL, mz+13).setTypeIdAndData(35, (byte)4, true);
			}
			for(int x = 0; x<16; x++){
				for(int z = 0; z<16; z++){
					if(((1<=x&&x<=14)&&(1==z||z==14))
							||((1==x||x==14)&&(1<=z&&z<=14))){
						if((x==1&&z==1&&percent>1.923076923076923*0)
								||(x==1&&z==2&&percent>1.923076923076923*1)
								||(x==1&&z==3&&percent>1.923076923076923*2)
								||(x==1&&z==4&&percent>1.923076923076923*3)
								||(x==1&&z==5&&percent>1.923076923076923*4)
								||(x==1&&z==6&&percent>1.923076923076923*5)
								||(x==1&&z==7&&percent>1.923076923076923*6)
								||(x==1&&z==8&&percent>1.923076923076923*7)
								||(x==1&&z==9&&percent>1.923076923076923*8)
								||(x==1&&z==10&&percent>1.923076923076923*9)
								||(x==1&&z==11&&percent>1.923076923076923*10)
								||(x==1&&z==12&&percent>1.923076923076923*11)
								||(x==1&&z==13&&percent>1.923076923076923*12)
								||(x==1&&z==14&&percent>1.923076923076923*13)
								||(x==2&&z==14&&percent>1.923076923076923*14)
								||(x==3&&z==14&&percent>1.923076923076923*15)
								||(x==4&&z==14&&percent>1.923076923076923*16)
								||(x==5&&z==14&&percent>1.923076923076923*17)
								||(x==6&&z==14&&percent>1.923076923076923*18)
								||(x==7&&z==14&&percent>1.923076923076923*19)
								||(x==8&&z==14&&percent>1.923076923076923*20)
								||(x==9&&z==14&&percent>1.923076923076923*21)
								||(x==10&&z==14&&percent>1.923076923076923*22)
								||(x==11&&z==14&&percent>1.923076923076923*23)
								||(x==12&&z==14&&percent>1.923076923076923*24)
								||(x==13&&z==14&&percent>1.923076923076923*25)
								||(x==14&&z==14&&percent>1.923076923076923*26)
								||(x==14&&z==13&&percent>1.923076923076923*27)
								||(x==14&&z==12&&percent>1.923076923076923*28)
								||(x==14&&z==11&&percent>1.923076923076923*29)
								||(x==14&&z==10&&percent>1.923076923076923*30)
								||(x==14&&z==9&&percent>1.923076923076923*31)
								||(x==14&&z==8&&percent>1.923076923076923*32)
								||(x==14&&z==7&&percent>1.923076923076923*33)
								||(x==14&&z==6&&percent>1.923076923076923*34)
								||(x==14&&z==5&&percent>1.923076923076923*35)
								||(x==14&&z==4&&percent>1.923076923076923*36)
								||(x==14&&z==3&&percent>1.923076923076923*37)
								||(x==14&&z==2&&percent>1.923076923076923*38)
								||(x==14&&z==1&&percent>1.923076923076923*39)
								||(x==13&&z==1&&percent>1.923076923076923*40)
								||(x==12&&z==1&&percent>1.923076923076923*41)
								||(x==11&&z==1&&percent>1.923076923076923*42)
								||(x==10&&z==1&&percent>1.923076923076923*43)
								||(x==9&&z==1&&percent>1.923076923076923*44)
								||(x==8&&z==1&&percent>1.923076923076923*45)
								||(x==7&&z==1&&percent>1.923076923076923*46)
								||(x==6&&z==1&&percent>1.923076923076923*47)
								||(x==5&&z==1&&percent>1.923076923076923*48)
								||(x==4&&z==1&&percent>1.923076923076923*49)
								||(x==3&&z==1&&percent>1.923076923076923*50)
								||(x==2&&z==1&&percent>1.923076923076923*51)){
							if(!army.isNullified())army.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)11, true);
							else this.building.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)7, true);
						}else{
							if(!army.isNullified())army.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)3, true);
							else this.building.getWorld().getBlockAt(mx+x, STATUS_LEVEL, mz+z).setTypeIdAndData(35, (byte)8, true);
						}
					}
				}
			}
			final int range = army.getArmyType().getRange();
			final int used = (int)(army.isAttacking()&&army.canAttack().equals("Yes")?army.getDistance(army.getTarget()):0);
			final Direction d = army.getDirection();
			final int basex;
			final int basez;
			if(d==Direction.NORTH){
				basex=army.getNorthWestCourner().getBlock(3, 0, 0).getX();
				basez=army.getNorthWestCourner().getBlock(3, 0, 0).getZ();
			}else if(d==Direction.EAST){
				basex=army.getNorthWestCourner().getBlock(15, 0, 3).getX();
				basez=army.getNorthWestCourner().getBlock(15, 0, 3).getZ();
			}else if(d==Direction.SOUTH){
				basex=army.getNorthWestCourner().getBlock(12, 0, 15).getX();
				basez=army.getNorthWestCourner().getBlock(12, 0, 15).getZ();
			}else{
				basex=army.getNorthWestCourner().getBlock(0, 0, 12).getX();
				basez=army.getNorthWestCourner().getBlock(0, 0, 12).getZ();
			}
			int x;
			int z;
			byte c;
			for(int i = 0; i<10; i++){
				if(d==Direction.NORTH){
					x=basex+i;
					z=basez;
				}else if(d==Direction.EAST){
					x=basex;
					z=basez+i;
				}else if(d==Direction.SOUTH){
					x=basex-1;
					z=basez;
				}else{
					x=basex;
					z=basez-i;
				}
				if(i<used)c=(byte)7;
				else if(i<range)c=(byte)8;
				else c=(byte)0;
				army.getWorld().getBlockAt(x, STATUS_LEVEL+5, z).setTypeIdAndData(35, c, true);
			}
			for(int i = 0; i<10; i++){
				if(d==Direction.NORTH){
					x=basex+i;
					z=basez;
				}else if(d==Direction.EAST){
					x=basex;
					z=basez+i;
				}else if(d==Direction.SOUTH){
					x=basex-1;
					z=basez;
				}else{
					x=basex;
					z=basez-i;
				}
				if(i==0){
					if(army.hasWizard())c=(byte)14;
					else c=(byte)0;
					i=8;
				}else{
					if(army.hasCommander())c=(byte)4;
					else c=(byte)0;
				}
				if(c!=0)army.getWorld().getBlockAt(x, STATUS_LEVEL+6, z).setTypeIdAndData(35, c, true);
			}
		}else{
			for(int x = getStartX(); x<=getEndX(); x++){
				for(int z = getStartZ(); z<=getEndZ(); z++){
					if(x==getStartX()
							||x==getEndX()
							||z==getStartZ()
							||z==getEndZ())this.building.getWorld().getBlockAt(x, STATUS_LEVEL, z).setTypeIdAndData(35, this.type.getColor(), true);
				}
			}
		}
	}
	private void drawHealth(){
		if(this.damage>0){
			//Courner 1
			Block b = this.building.getWorld().getBlockAt(getStartX(), STATUS_LEVEL, getStartZ());
			b.setType(Material.NETHERRACK);
			b.getRelative(BlockFace.UP).setType(Material.FIRE);
			int distance = 1;
			while(distance<=this.damage){
				Block b2 = b.getRelative(BlockFace.SOUTH, distance);
				b2.setType(Material.NETHERRACK);
				b2.getRelative(BlockFace.UP).setType(Material.FIRE);
				b2=b.getRelative(BlockFace.EAST, distance);
				b2.setType(Material.NETHERRACK);
				b2.getRelative(BlockFace.UP).setType(Material.FIRE);
				distance++;
			}
			//Courner 2
			b=this.building.getWorld().getBlockAt(getStartX(), STATUS_LEVEL, getEndZ());
			b.setType(Material.NETHERRACK);
			b.getRelative(BlockFace.UP).setType(Material.FIRE);
			distance=1;
			while(distance<=this.damage){
				Block b2 = b.getRelative(BlockFace.NORTH, distance);
				b2.setType(Material.NETHERRACK);
				b2.getRelative(BlockFace.UP).setType(Material.FIRE);
				b2=b.getRelative(BlockFace.EAST, distance);
				b2.setType(Material.NETHERRACK);
				b2.getRelative(BlockFace.UP).setType(Material.FIRE);
				distance++;
			}
			//Courner 3
			b=this.building.getWorld().getBlockAt(getEndX(), STATUS_LEVEL, getStartZ());
			b.setType(Material.NETHERRACK);
			b.getRelative(BlockFace.UP).setType(Material.FIRE);
			distance=1;
			while(distance<=this.damage){
				Block b2 = b.getRelative(BlockFace.SOUTH, distance);
				b2.setType(Material.NETHERRACK);
				b2.getRelative(BlockFace.UP).setType(Material.FIRE);
				b2=b.getRelative(BlockFace.WEST, distance);
				b2.setType(Material.NETHERRACK);
				b2.getRelative(BlockFace.UP).setType(Material.FIRE);
				distance++;
			}
			//Courner 4
			b=this.building.getWorld().getBlockAt(getEndX(), STATUS_LEVEL, getEndZ());
			b.setType(Material.NETHERRACK);
			b.getRelative(BlockFace.UP).setType(Material.FIRE);
			distance=1;
			while(distance<=this.damage){
				Block b2 = b.getRelative(BlockFace.NORTH, distance);
				b2.setType(Material.NETHERRACK);
				b2.getRelative(BlockFace.UP).setType(Material.FIRE);
				b2=b.getRelative(BlockFace.WEST, distance);
				b2.setType(Material.NETHERRACK);
				b2.getRelative(BlockFace.UP).setType(Material.FIRE);
				distance++;
			}
		}
	}
	public void draw(){
		Bukkit.getScheduler().runTask(WhCommunity.plugin, new Runnable(){
			public void run(){
				drawBuilding();
				drawHealth();
			}
		});
	}
	private int getStartX(){
		if(startx==5){
			if(this.building==null)startx=this.chunk.getBlock(0, 0, 0).getX();
			startx=this.building.getNorthWestCourner().getBlock(0, 0, 0).getX();
		}
		return startx;
	}
	private int getEndX(){
		if(endx==5){
			if(this.building==null)endx=this.chunk.getBlock(15, 0, 0).getX();
			endx=this.building.getSouthEastCourner().getBlock(15, 0, 0).getX();
		}
		return endx;
	}
	private int getStartZ(){
		if(startz==5){
			if(this.building==null)startz=this.chunk.getBlock(0, 0, 0).getZ();
			startz=this.building.getNorthWestCourner().getBlock(0, 0, 0).getZ();
		}
		return startz;
	}
	private int getEndZ(){
		if(endz==5){
			if(this.building==null)endz=this.chunk.getBlock(0, 0, 15).getZ();
			endz=this.building.getSouthEastCourner().getBlock(0, 0, 15).getZ();
		}
		return endz;
	}
}
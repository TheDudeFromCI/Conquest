package me.ci.Conquest.Textures;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.ci.WhCommunity;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Constructors.Building;
import me.ci.Conquest.Buildings.Constructors.Flag;
import me.ci.Conquest.Buildings.Main.Army;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BuildingTexture{
	final private BlockInfo[][] blocks;
	private boolean real = true;
	private boolean working = false;
	public static Map<Chunk,List<BuildingTexture>> updated = new HashMap<Chunk,List<BuildingTexture>>();
	public BuildingTexture(String code){
		if(WhCommunity.debug)WhCommunity.printDebug();
 		String[] s1 = code.split(";");
		this.blocks=new BlockInfo[s1.length][65536];
		for(int i = 0; i<s1.length; i++){
			String[] s2 = s1[i].split(",");
			for(int i1 = 0; i1<s2.length; i1++){
				try{
					if(!s2[i1].isEmpty())this.blocks[i][i1]=new BlockInfo(s2[i1]);
				}catch(ArrayIndexOutOfBoundsException exception){}
			}
		}
	}
	public BuildingTexture(){
		if(WhCommunity.debug)WhCommunity.printDebug();
		real=false;
		blocks=null;
	}
	private void draw(final Chunk nwc, final Building building){
		if(!real)return;
		WhCommunity.threads++;
		working=true;
		try{
			if(building!=null)building.setIsBuilding(true);
			final List<Chunk> used = new ArrayList<Chunk>();
			final World w = nwc.getWorld();
			final int s = (int)Math.sqrt(blocks.length);
			final BlocksDone readiedchunks = new BlocksDone();
			Bukkit.getScheduler().runTask(WhCommunity.plugin, new Runnable(){
				public void run(){
					for(int x = nwc.getX(); x<nwc.getX()+s; x++){
						for(int z = nwc.getZ(); z<nwc.getZ()+s; z++){
							Chunk c = nwc.getWorld().getChunkAt(x, z);
							if(!c.isLoaded())c.load();
							used.add(c);
						}
					}
					readiedchunks.id=1;
				}
			});
			while(readiedchunks.id==0){
				try{ Thread.sleep(10);
				}catch(Exception exception){}
			}
			uploadChunks(used, this, true);
			int[] l = new int[blocks.length];
			final Flag f;
			if(building!=null){
				f=building.getKingdom().getFlag();
				f.recalculateColors();
			}else f=null;
			final BlocksDone blocksdone = new BlocksDone();
			final BlocksDone chunksleft = new BlocksDone();
			final Plot northwest = PlotMg.getPlotAt(nwc);
			final int h = northwest.getHeight();
			final int chunkoffx = northwest.getX();
			final int chunkoffz = northwest.getZ();
			for(int cx = 0; cx<s; cx++){
				for(int cz = 0; cz<s; cz++){
					final Chunk chunk1 = PlotMg.getPlotAt(nwc.getWorld(), cx+chunkoffx, cz+chunkoffz).getChunk();
					Bukkit.getScheduler().runTask(WhCommunity.plugin, new Runnable(){
						public void run(){
							for(int i = 0; i<=h; i++){
								for(int x = 0; x<16; x++){
									for(int z = 0; z<16; z++){
										chunk1.getBlock(x, i, z).setType(Material.STONE);
									}
								}
							}
							for(int i = h+1; i<h+3; i++){
								for(int x = 0; x<16; x++){
									for(int z = 0; z<16; z++){
										chunk1.getBlock(x, i, z).setType(Material.DIRT);
									}
								}
							}
							for(int x = 0; x<16; x++){
								for(int z = 0; z<16; z++){
									chunk1.getBlock(x, h+3, z).setType(Material.GRASS);
								}
							}
						}
					});
					try{ Thread.sleep(200);
					}catch(final Exception exception){}
				}
			}
			for(int i = 0; i<91; i++){
				int y = i+h;
				for(int c = 0; c<blocks.length; c++){
					chunksleft.id++;
					int chunkx = nwc.getX()+(c/s);
					int chunkz = nwc.getZ()+(c%s);
					Chunk chunk = nwc.getWorld().getChunkAt(chunkx, chunkz);
					int basex = chunk.getBlock(0, 0, 0).getX();
					int basez = chunk.getBlock(0, 0, 0).getZ();
					try{
						boolean die = false;
						for(Chunk c1 : used){
							if(updated.get(c1).size()>1
									&&updated.get(c1).get(0)==this){
								die=true;
								break;
							}
						}
						if(die){
							uploadChunks(used, this, false);
							WhCommunity.threads--;
							working=false;
							return;
						}
					}catch(final Exception exception){ exception.printStackTrace(); }
					final int c10 = c;
					final int[] l10 = l;
					final int basex10 = basex;
					final int basez10 = basez;
					final int i10 = y;
					Runnable run = new Runnable(){
						public void run(){
							try{
								for(int x = 0; x<16; x++){
									for(int z = 0; z<16; z++){
										try{
											if(blocks[c10][l10[c10]]!=null){
												if(blocks[c10][l10[c10]].id==63
														||blocks[c10][l10[c10]].id==54
														||blocks[c10][l10[c10]].id==23
														||blocks[c10][l10[c10]].id==117
														||blocks[c10][l10[c10]].id==154
														||blocks[c10][l10[c10]].id==61
														||blocks[c10][l10[c10]].id==62
														||blocks[c10][l10[c10]].id==158
														||blocks[c10][l10[c10]].id==138
														||blocks[c10][l10[c10]].id==52
														||blocks[c10][l10[c10]].id==25
														||blocks[c10][l10[c10]].id==84
														||blocks[c10][l10[c10]].id==116
														||blocks[c10][l10[c10]].id==119
														||blocks[c10][l10[c10]].id==144
														||blocks[c10][l10[c10]].id==137
														||blocks[c10][l10[c10]].id==130){
													blocks[c10][l10[c10]].id=0;
													blocks[c10][l10[c10]].data=0;
												}
												if(blocks[c10][l10[c10]].getId()==7)w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(1, (byte)0, false);
												else if(building!=null){
													if(blocks[c10][l10[c10]].getId()==121)w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(35, (building.getKingdom().isRuins()?(byte)0:f.getPrimaryColor()), false);
													else if(blocks[c10][l10[c10]].getId()==19)w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(35, (building.getKingdom().isRuins()?(byte)0:f.getSecondaryColor()), false);
													else if(blocks[c10][l10[c10]].getId()==120)w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(35, (building.getKingdom().isRuins()?(byte)0:f.getTertiaryColor()), false);
													else if(blocks[c10][l10[c10]].getId()==7)w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(1, (byte)0, false);
													else if(blocks[c10][l10[c10]].getId()==99){
														if(!building.getKingdom().isRuins()){
															final int flagplacex = x+basex10;
															final int flagplacey = i10-4;
															final int flagplacez = z+basez10;
															w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(0, (byte)0, false);
															try{
																WhCommunity.threads++;
																Bukkit.getScheduler().runTaskLater(WhCommunity.plugin, new Runnable(){
																	public void run(){
																		try{ f.draw(w.getBlockAt(flagplacex, flagplacey, flagplacez));
																		}catch(Exception exception){ exception.printStackTrace();
																		}finally{ WhCommunity.threads--; }
																	}
																}, 5);
															}catch(final Exception exception){ exception.printStackTrace(); }
														}
													}else w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(blocks[c10][l10[c10]].getId(), blocks[c10][l10[c10]].getData(), false);
												}else w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(blocks[c10][l10[c10]].getId(), blocks[c10][l10[c10]].getData(), false);
											}else w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(0, (byte)0, false);
										}catch(final Exception exception){
											try{ w.getBlockAt(x+basex10, i10, z+basez10).setTypeIdAndData(0, (byte)0, false);
											}catch(final Exception exception1){}
										}
										l10[c10]++;
										blocksdone.id++;
									}
								}
							}catch(final Exception exception){ exception.printStackTrace(); }
							chunksleft.id--;
						}
					};
					Bukkit.getScheduler().runTaskLater(WhCommunity.plugin, run, (int)(i*1.5+5));
				}
				if(i==10
						&&building!=null){
					if(!building.isBuildingOrUpgrading()
							||building instanceof Army)building.updateStatus();
				}
			}
			while(chunksleft.id>0){
				try{ Thread.sleep(100);
				}catch(Exception exception){}
			}
			if(building!=null)building.setIsBuilding(false);
			uploadChunks(used, this, false);
		}catch(Exception exception){ exception.printStackTrace(); }
		working=false;
		WhCommunity.threads--;
	}
	public void buildAt(final Chunk nwc, final Building building){
		Bukkit.getScheduler().runTaskAsynchronously(WhCommunity.plugin, new Runnable(){
			public void run(){ draw(nwc, building); }
		});
	}
	private synchronized static void uploadChunks(final List<Chunk> chunks, final BuildingTexture text, final boolean load){
		for(Chunk c : chunks){
			List<BuildingTexture> textures;
			if(updated.containsKey(c))textures=updated.get(c);
			else textures=new LinkedList<BuildingTexture>();
			if(load)if(!textures.contains(text))textures.add(text);
			else textures.remove(text);
			Iterator<BuildingTexture> itr = textures.iterator();
			BuildingTexture t;
			while(itr.hasNext()){
				t=itr.next();
				if(!t.working)itr.remove();
			}
			if(textures.isEmpty())updated.remove(c);
			else updated.put(c, textures);
		}
	}
	public class BlockInfo{
		private int id;
		private byte data;
		public BlockInfo(int id, byte data){
			this.id=id;
			this.data=data;
		}
		public BlockInfo(String block){
			if(block.isEmpty()){
				this.id=0;
				this.data=0;
			}else{
				String[] s = block.split("-");
				if(s.length==1){
					this.id=Integer.valueOf(s[0]);
					this.data=0;
				}else{
					this.id=Integer.valueOf(s[0]);
					this.data=Byte.valueOf(s[1]);
				}
			}
		}
		public void loadBlock(Block b){
			b.setTypeIdAndData(this.id, this.data, false);
		}
		@Override
		public String toString(){
			return this.id+"-"+this.data;
		}
		public int getId(){
			return this.id;
		}
		public byte getData(){
			return this.data;
		}
	}
	public class BlocksDone{ int id; }
}